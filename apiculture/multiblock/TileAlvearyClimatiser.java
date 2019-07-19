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

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import forestry.api.climate.IClimateControlled;
import forestry.api.multiblock.IAlvearyComponent;
import forestry.core.network.packets.PacketActiveUpdate;
import forestry.core.tiles.IActivatable;
import forestry.core.utils.NetworkUtil;
import forestry.energy.EnergyHelper;
import forestry.energy.EnergyManager;
import forestry.energy.EnergyTransferMode;

public abstract class TileAlvearyClimatiser extends TileAlveary implements IActivatable, IAlvearyComponent.Climatiser {

	private static final int WORK_CYCLES = 1;
	private static final int ENERGY_PER_OPERATION = 50;

	protected interface IClimitiserDefinition {
		float getChangePerTransfer();

		float getBoundaryUp();

		float getBoundaryDown();
	}

	private final EnergyManager energyManager;
	private final IClimitiserDefinition definition;

	private int workingTime = 0;

	// CLIENT
	private boolean active;

	protected TileAlvearyClimatiser(IClimitiserDefinition definition) {
		this.definition = definition;

		this.energyManager = new EnergyManager(1000, 2000);
		this.energyManager.setExternalMode(EnergyTransferMode.RECEIVE);
	}

	/* UPDATING */
	@Override
	public void changeClimate(int tick, IClimateControlled climateControlled) {
		if (workingTime < 20 && EnergyHelper.consumeEnergyToDoWork(energyManager, WORK_CYCLES, ENERGY_PER_OPERATION)) {
			// one tick of work for every 10 RF
			workingTime += ENERGY_PER_OPERATION / 10;
		}

		if (workingTime > 0) {
			workingTime--;
			climateControlled.addTemperatureChange(definition.getChangePerTransfer(), definition.getBoundaryDown(), definition.getBoundaryUp());
		}

		setActive(workingTime > 0);
	}

	/* LOADING & SAVING */
	@Override
	public void readFromNBT(CompoundNBT CompoundNBT) {
		super.readFromNBT(CompoundNBT);
		energyManager.readFromNBT(CompoundNBT);
		workingTime = CompoundNBT.getInteger("Heating");
		setActive(workingTime > 0);
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT CompoundNBT) {
		CompoundNBT = super.writeToNBT(CompoundNBT);
		energyManager.writeToNBT(CompoundNBT);
		CompoundNBT.setInteger("Heating", workingTime);
		return CompoundNBT;
	}

	/* Network */
	@Override
	protected void encodeDescriptionPacket(CompoundNBT packetData) {
		super.encodeDescriptionPacket(packetData);
		packetData.setBoolean("Active", active);
	}

	@Override
	protected void decodeDescriptionPacket(CompoundNBT packetData) {
		super.decodeDescriptionPacket(packetData);
		setActive(packetData.getBoolean("Active"));
	}

	/* IActivatable */
	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		if (this.active == active) {
			return;
		}

		this.active = active;

		if (world != null) {
			if (world.isRemote) {
				world.markBlockRangeForRenderUpdate(getPos(), getPos());
			} else {
				NetworkUtil.sendNetworkPacket(new PacketActiveUpdate(this), pos, world);
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return energyManager.hasCapability(capability) || super.hasCapability(capability, facing);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		T energyCapability = energyManager.getCapability(capability);
		if (energyCapability != null) {
			return energyCapability;
		}
		return super.getCapability(capability, facing);
	}
}