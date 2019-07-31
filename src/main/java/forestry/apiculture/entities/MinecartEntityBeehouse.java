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
package forestry.apiculture.entities;

import java.util.Collections;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.apiculture.DefaultBeeListener;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.apiculture.BeehouseBeeModifier;
import forestry.apiculture.InventoryBeeHousing;
import forestry.apiculture.ModuleApiculture;
import forestry.apiculture.gui.ContainerMinecartBeehouse;
import forestry.apiculture.gui.GuiBeeHousing;
import forestry.core.inventory.IInventoryAdapter;

public class MinecartEntityBeehouse extends MinecartEntityBeeHousingBase {
	private static final IBeeModifier beeModifier = new BeehouseBeeModifier();
	private static final IBeeListener beeListener = new DefaultBeeListener();
	private final InventoryBeeHousing beeInventory = new InventoryBeeHousing(9);

	@SuppressWarnings("unused")
	public MinecartEntityBeehouse(World world) {
		super(world);
		beeInventory.disableAutomation();
	}

	public MinecartEntityBeehouse(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ);
		beeInventory.disableAutomation();
	}

	@Override
	public String getHintKey() {
		return "bee.house";
	}

	@Override
	public BlockState getDisplayTile() {
		return ModuleApiculture.getBlocks().beeHouse.getDefaultState();
	}

	@Override
	public ItemStack getCartItem() {
		return ModuleApiculture.getItems().minecartBeehouse.getBeeHouseMinecart();
	}

	/* IBeeHousing */
	@Override
	public Iterable<IBeeModifier> getBeeModifiers() {
		return Collections.singleton(beeModifier);
	}

	@Override
	public Iterable<IBeeListener> getBeeListeners() {
		return Collections.singleton(beeListener);
	}

	@Override
	public IBeeHousingInventory getBeeInventory() {
		return beeInventory;
	}

	@Override
	protected IInventoryAdapter getInternalInventory() {
		return beeInventory;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ContainerScreen getGui(PlayerEntity player, int data) {
		ContainerMinecartBeehouse container = new ContainerMinecartBeehouse(player.inventory, this, false, data);	//TODO windowid
		return new GuiBeeHousing<>(this, container, GuiBeeHousing.Icon.BEE_HOUSE);
	}

	@Override
	public Container getContainer(PlayerEntity player, int data) {
		return new ContainerMinecartBeehouse(player.inventory, this, false, data);	//TODO windowid
	}
}