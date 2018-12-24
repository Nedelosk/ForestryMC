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
package forestry.core.recipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;

import forestry.api.recipes.IDescriptiveRecipe;
import forestry.core.utils.ItemStackUtil;

//TODO - remove in 1.13. NBT, multiple matches etc can be handled by ingredients
//kept for API compatibility
//Note to self - always checked tags by default so replacement should too.
@Deprecated
public class ShapedRecipeCustom extends ShapedOreRecipe implements IDescriptiveRecipe {
	private final NonNullList<NonNullList<ItemStack>> stackinput;
	private final NonNullList<String> oreDicts;

	public ShapedRecipeCustom(ItemStack result, Object... recipe) {
		super(null, result, recipe);

		int length = super.getRecipeHeight() * super.getRecipeWidth();

		stackinput = NonNullList.withSize(length, NonNullList.create());
		for(int i = 0; i < length; i++) {
			stackinput.set(i, getMatchingStacks(i));
		}

		List<String> ores = new ArrayList<>();
		for(Object o : recipe) {
			if(o instanceof String) {
				String str = (String) o;
				if(OreDictionary.doesOreNameExist(str)) {
					ores.add(str);
				}
			}
		}
		oreDicts = NonNullList.from("", ores.toArray(new String[0]));
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getRecipeWidth() { return width; }

	@Override
	public int getRecipeHeight() { return  height; }

	public NonNullList<NonNullList<ItemStack>> getRawIngredients() {
		return stackinput;
	}

	@Override
	public NonNullList<String> getOreDicts() {
		return oreDicts;
	}

	@Override
	public ItemStack getOutput() {
		return getRecipeOutput();
	}

	@Override
	public boolean isDynamic() {
		return false;
	}

//	@Override
//	public boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
//		for (int x = 0; x < inv.getWidth(); x++) {
//			for (int y = 0; y < inv.getHeight(); y++) {
//				int subX = x - startX;
//				int subY = y - startY;
//				NonNullList<ItemStack> target = null;
//
//				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
//					if (mirror) {
//						target = stackinput.get(width - subX - 1 + subY * width);
//					} else {
//						target = stackinput.get(subX + subY * width);
//					}
//				}
//
//				ItemStack stackInSlot = inv.getStackInRowAndColumn(x, y);
//
//				if (target != null && !target.isEmpty()) {
//					boolean matched = false;
//
//					Iterator<ItemStack> itr = target.iterator();
//					while (itr.hasNext() && !matched) {
//						matched = ItemStackUtil.isCraftingEquivalent(itr.next(), stackInSlot);
//					}
//
//					if (!matched) {
//						return false;
//					}
//				} else if (!stackInSlot.isEmpty()) {
//					return false;
//				}
//			}
//		}
//
//		return true;
//	}

	//TODO - check this works with mirroring
	private NonNullList<ItemStack> getMatchingStacks(int index) {
		int x = index % 3;
		int y = index / 3;
		return NonNullList.from(ItemStack.EMPTY, input.get(x + y * width).getMatchingStacks());
	}
}
