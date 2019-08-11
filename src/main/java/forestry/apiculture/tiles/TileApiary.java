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
package forestry.apiculture.tiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

//import net.minecraftforge.fml.common.Optional;

import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IHiveFrame;
import forestry.apiculture.ApiaryBeeListener;
import forestry.apiculture.ApiaryBeeModifier;
import forestry.apiculture.IApiary;
import forestry.apiculture.ModuleApiculture;
import forestry.apiculture.gui.ContainerBeeHousing;
import forestry.apiculture.inventory.IApiaryInventory;
import forestry.apiculture.inventory.InventoryApiary;

//import buildcraft.api.statements.ITriggerExternal;

public class TileApiary extends TileBeeHousingBase implements IApiary {
	private final IBeeModifier beeModifier = new ApiaryBeeModifier();
	private final IBeeListener beeListener = new ApiaryBeeListener(this);
	private final InventoryApiary inventory = new InventoryApiary();

	public TileApiary() {
		super(ModuleApiculture.getTiles().apiary, "apiary");
		setInternalInventory(inventory);
	}

	@Override
	public IBeeHousingInventory getBeeInventory() {
		return inventory;
	}

	@Override
	public IApiaryInventory getApiaryInventory() {
		return inventory;
	}

	@Override
	public Collection<IBeeModifier> getBeeModifiers() {
		List<IBeeModifier> beeModifiers = new ArrayList<>();

		beeModifiers.add(beeModifier);

		for (Tuple<IHiveFrame, ItemStack> frame : inventory.getFrames()) {
			IHiveFrame hiveFrame = frame.getA();
			ItemStack stack = frame.getB();
			IBeeModifier beeModifier = hiveFrame.getBeeModifier(stack);
			beeModifiers.add(beeModifier);
		}

		return beeModifiers;
	}

	@Override
	public Iterable<IBeeListener> getBeeListeners() {
		return Collections.singleton(beeListener);
	}

	/* ITRIGGERPROVIDER */
//	@Optional.Method(modid = Constants.BCLIB_MOD_ID)
//	@Override
//	public void addExternalTriggers(Collection<ITriggerExternal> triggers, @Nonnull Direction side, TileEntity tile) {
//		super.addExternalTriggers(triggers, side, tile);
//		triggers.add(ApicultureTriggers.missingQueen);
//		triggers.add(ApicultureTriggers.missingDrone);
//		triggers.add(ApicultureTriggers.noFrames);
//	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
		return new ContainerBeeHousing(windowId, player.inventory, this, true);
	}
}
