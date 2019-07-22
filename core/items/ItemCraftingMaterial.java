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
package forestry.core.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.core.IModelManager;

public class ItemCraftingMaterial extends ItemForestry {

	private final String[] definition = new String[]{"pulsating_dust", "pulsating_mesh", "silk_wisp", "woven_silk", "dissipation_charge", "ice_shard", "scented_paneling", "camouflaged_paneling"};

	public ItemCraftingMaterial() {
		super((new Item.Properties())
		.maxDamage(0));
//		setHasSubtypes(true); TODO flatten?
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		if (true){//TODO flatten stack.getItemDamage() >= definition.length || stack.getItemDamage() < 0) {
			return "item.forestry.unknown";
		} else {
			return super.getTranslationKey(stack) + "." + definition[0];//TODO flatten stack.getItemDamage()];
		}
	}

	/* Models */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		for (int i = 0; i < definition.length; i++) {
			manager.registerItemModel(item, i, definition[i]);
		}
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> subItems) {
		if (this.isInGroup(tab)) {
			for (int i = 0; i < definition.length; i++) {
				subItems.add(new ItemStack(this));//TODO flatten, 1, i));
			}
		}
	}

	public ItemStack getPulsatingDust() {
		return new ItemStack(this, 1);//TODO flatten, 0);
	}

	public ItemStack getPulsatingMesh() {
		return new ItemStack(this, 1);//TODO flatten, 1);
	}

	public ItemStack getSilkWisp() {
		return new ItemStack(this, 1);//TODO flatten, 2);
	}

	public ItemStack getWovenSilk() {
		return new ItemStack(this, 1);// TODO flatten, 3);
	}

	public ItemStack getDissipationCharge() {
		return new ItemStack(this, 1);// TODO flatten , 4);
	}

	public ItemStack getIceShard(int amount) {
		return new ItemStack(this, amount);//TODO flatten , 5);
	}

	public ItemStack getScentedPaneling() {
		return new ItemStack(this, 1);// TODO flatten , 6);
	}

	public ItemStack getCamouflagedPaneling() {
		return getCamouflagedPaneling(1);
	}

	public ItemStack getCamouflagedPaneling(int stackSize) {
		return new ItemStack(this, stackSize); //TODO flatten, 7);
	}
}
