/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.factory.tiles;

import javax.annotation.Nullable;
import java.io.IOException;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.core.IErrorLogic;
import forestry.core.config.Constants;
import forestry.core.errors.EnumErrorCode;
import forestry.core.fluids.ContainerFiller;
import forestry.core.fluids.DrainOnlyFluidHandlerWrapper;
import forestry.core.fluids.FilteredTank;
import forestry.core.fluids.FluidHelper;
import forestry.core.fluids.TankManager;
import forestry.core.network.PacketBufferForestry;
import forestry.core.tiles.ILiquidTankTile;
import forestry.core.tiles.TileBase;
import forestry.factory.gui.ContainerRaintank;
import forestry.factory.gui.GuiRaintank;
import forestry.factory.inventory.InventoryRaintank;

public class TileRaintank extends TileBase implements ISidedInventory, ILiquidTankTile {
	private static final FluidStack STACK_WATER = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
	private static final FluidStack WATER_PER_UPDATE = new FluidStack(FluidRegistry.WATER, Constants.RAINTANK_AMOUNT_PER_UPDATE);

	private final FilteredTank resourceTank;
	private final TankManager tankManager;
	private final ContainerFiller containerFiller;

	@Nullable
	private Boolean canDumpBelow = null;
	private boolean dumpingFluid = false;

	// client
	private int fillingProgress;

	public TileRaintank() {
		setInternalInventory(new InventoryRaintank(this));

		resourceTank = new FilteredTank(Constants.RAINTANK_TANK_CAPACITY).setFilters(FluidRegistry.WATER);

		tankManager = new TankManager(this, resourceTank);

		containerFiller = new ContainerFiller(resourceTank, Constants.RAINTANK_FILLING_TIME, this, InventoryRaintank.SLOT_RESOURCE, InventoryRaintank.SLOT_PRODUCT);
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT CompoundNBT) {
		CompoundNBT = super.writeToNBT(CompoundNBT);
		tankManager.writeToNBT(CompoundNBT);
		return CompoundNBT;
	}

	@Override
	public void readFromNBT(CompoundNBT CompoundNBT) {
		super.readFromNBT(CompoundNBT);
		tankManager.readFromNBT(CompoundNBT);
	}

	@Override
	public void writeData(PacketBufferForestry data) {
		super.writeData(data);
		tankManager.writeData(data);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void readData(PacketBufferForestry data) throws IOException {
		super.readData(data);
		tankManager.readData(data);
	}

	@Override
	public void updateServerSide() {
		if (updateOnInterval(20)) {
			IErrorLogic errorLogic = getErrorLogic();

			BlockPos pos = getPos();
			Biome biome = world.getBiome(pos);
			errorLogic.setCondition(!biome.canRain(), EnumErrorCode.NO_RAIN_BIOME);

			BlockPos posAbove = pos.up();
			boolean hasSky = world.canBlockSeeSky(posAbove);
			errorLogic.setCondition(!hasSky, EnumErrorCode.NO_SKY_RAIN_TANK);

			errorLogic.setCondition(!world.isRainingAt(posAbove), EnumErrorCode.NOT_RAINING);

			if (!errorLogic.hasErrors()) {
				resourceTank.fillInternal(WATER_PER_UPDATE, true);
			}

			containerFiller.updateServerSide();
		}

		if (canDumpBelow == null) {
			canDumpBelow = FluidHelper.canAcceptFluid(world, getPos().down(), Direction.UP, STACK_WATER);
		}

		if (canDumpBelow) {
			if (dumpingFluid || updateOnInterval(20)) {
				dumpingFluid = dumpFluidBelow();
			}
		}
	}

	private boolean dumpFluidBelow() {
		if (!resourceTank.isEmpty()) {
			IFluidHandler fluidDestination = FluidUtil.getFluidHandler(world, pos.down(), Direction.UP);
			if (fluidDestination != null) {
				return FluidUtil.tryFluidTransfer(fluidDestination, tankManager, Fluid.BUCKET_VOLUME / 20, true) != null;
			}
		}
		return false;
	}

	public boolean isFilling() {
		return fillingProgress > 0;
	}

	public int getFillProgressScaled(int i) {
		return fillingProgress * i / Constants.RAINTANK_FILLING_TIME;
	}

	/* SMP GUI */
	public void getGUINetworkData(int i, int j) {
		switch (i) {
			case 0:
				fillingProgress = j;
				break;
		}
	}

	public void sendGUINetworkData(Container container, IContainerListener iCrafting) {
		iCrafting.sendWindowProperty(container, 0, containerFiller.getFillingProgress());
	}

	@Override
	public TankManager getTankManager() {
		return tankManager;
	}

	@Override
	public void onNeighborTileChange(World world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborTileChange(world, pos, neighbor);

		if (neighbor.equals(pos.down())) {
			canDumpBelow = FluidHelper.canAcceptFluid(world, neighbor, Direction.UP, STACK_WATER);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			final IFluidHandler fluidHandler;
			if (facing == Direction.DOWN) {
				fluidHandler = new DrainOnlyFluidHandlerWrapper(tankManager);
			} else {
				fluidHandler = tankManager;
			}
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ContainerScreen getGui(PlayerEntity player, int data) {
		return new GuiRaintank(player.inventory, this);
	}

	@Override
	public Container getContainer(PlayerEntity player, int data) {
		return new ContainerRaintank(player.inventory, this);
	}
}
