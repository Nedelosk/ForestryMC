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
package forestry.apiculture.multiblock;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.climate.IClimateControlled;
import forestry.api.multiblock.IAlvearyComponent;
import forestry.api.recipes.IHygroregulatorRecipe;
import forestry.apiculture.blocks.BlockAlvearyType;
import forestry.apiculture.gui.ContainerAlvearyHygroregulator;
import forestry.apiculture.gui.GuiAlvearyHygroregulator;
import forestry.apiculture.inventory.InventoryHygroregulator;
import forestry.core.config.Constants;
import forestry.core.fluids.FilteredTank;
import forestry.core.fluids.FluidHelper;
import forestry.core.fluids.TankManager;
import forestry.core.inventory.IInventoryAdapter;
import forestry.core.recipes.HygroregulatorManager;
import forestry.core.tiles.ILiquidTankTile;

public class TileAlvearyHygroregulator extends TileAlveary implements IInventory, ILiquidTankTile, IAlvearyComponent.Climatiser {
	private final TankManager tankManager;
	private final FilteredTank liquidTank;
	private final IInventoryAdapter inventory;

	@Nullable
	private IHygroregulatorRecipe currentRecipe;
	private int transferTime;

	public TileAlvearyHygroregulator() {
		super(BlockAlvearyType.HYGRO);

		this.inventory = new InventoryHygroregulator(this);

		this.liquidTank = new FilteredTank(Constants.PROCESSOR_TANK_CAPACITY).setFilters(HygroregulatorManager.getRecipeFluids());

		this.tankManager = new TankManager(this, liquidTank);
	}

	@Override
	public IInventoryAdapter getInternalInventory() {
		return inventory;
	}

	@Override
	public boolean allowsAutomation() {
		return true;
	}

	/* UPDATING */
	@Override
	public void changeClimate(int tickCount, IClimateControlled climateControlled) {
		if (transferTime <= 0) {
			FluidStack fluid = liquidTank.getFluid();
			if (fluid != null) {
				currentRecipe = HygroregulatorManager.findMatchingRecipe(fluid);

				if (currentRecipe != null) {
					liquidTank.drainInternal(currentRecipe.getResource().amount, true);
					transferTime = currentRecipe.getTransferTime();
				}
			}
		}

		if (transferTime > 0) {

			transferTime--;
			if (currentRecipe != null) {
				climateControlled.addHumidityChange(currentRecipe.getHumidChange(), 0.0f, 1.0f);
				climateControlled.addTemperatureChange(currentRecipe.getTempChange(), 0.0f, 2.0f);
			} else {
				transferTime = 0;
			}
		}

		if (tickCount % 20 == 0) {
			// Check if we have suitable items waiting in the item slot
			FluidHelper.drainContainers(tankManager, this, 0);
		}
	}

	/* SAVING & LOADING */
	@Override
	public void readFromNBT(CompoundNBT CompoundNBT) {
		super.readFromNBT(CompoundNBT);
		tankManager.readFromNBT(CompoundNBT);

		transferTime = CompoundNBT.getInteger("TransferTime");

		if (CompoundNBT.hasKey("CurrentLiquid")) {
			FluidStack liquid = FluidStack.loadFluidStackFromNBT(CompoundNBT.getCompoundNBT("CurrentLiquid"));
			currentRecipe = HygroregulatorManager.findMatchingRecipe(liquid);
		}
	}


	@Override
	public CompoundNBT writeToNBT(CompoundNBT CompoundNBT) {
		CompoundNBT = super.writeToNBT(CompoundNBT);
		tankManager.writeToNBT(CompoundNBT);

		CompoundNBT.setInteger("TransferTime", transferTime);
		if (currentRecipe != null) {
			CompoundNBT subcompound = new CompoundNBT();
			currentRecipe.getResource().writeToNBT(subcompound);
			CompoundNBT.setTag("CurrentLiquid", subcompound);
		}
		return CompoundNBT;
	}

	/* ILIQUIDTANKCONTAINER */

	@Override
	public TankManager getTankManager() {
		return tankManager;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
			super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (super.hasCapability(capability, facing)) {
			return super.getCapability(capability, facing);
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankManager);
		}
		return null;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ContainerScreen getGui(PlayerEntity player, int data) {
		return new GuiAlvearyHygroregulator(player.inventory, this);
	}

	@Override
	public Container getContainer(PlayerEntity player, int data) {
		return new ContainerAlvearyHygroregulator(player.inventory, this);
	}
}
