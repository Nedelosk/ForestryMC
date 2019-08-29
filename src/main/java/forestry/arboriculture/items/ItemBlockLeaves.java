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
package forestry.arboriculture.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.arboriculture.ModuleArboriculture;
import forestry.arboriculture.blocks.BlockAbstractLeaves;
import forestry.arboriculture.tiles.TileLeaves;
import forestry.core.items.IColoredItem;
import forestry.core.items.ItemBlockForestry;
import forestry.core.utils.Translator;

public class ItemBlockLeaves extends ItemBlockForestry<BlockAbstractLeaves> implements IColoredItem {

	public ItemBlockLeaves(BlockAbstractLeaves block) {
		super(block);
	}

	@Override
	public ITextComponent getDisplayName(ItemStack itemstack) {
		if (itemstack.getTag() == null) {
			return new TranslationTextComponent("trees.grammar.leaves.type");
		}

		TileLeaves tileLeaves = new TileLeaves();
		tileLeaves.read(itemstack.getTag());

		String unlocalizedName = tileLeaves.getUnlocalizedName();
		return getDisplayName(unlocalizedName);
	}

	public static ITextComponent getDisplayName(String unlocalizedSpeciesName) {
		String customTreeKey = "for.trees.custom.leaves." + unlocalizedSpeciesName.replace("for.trees.species.", "");
		if (Translator.canTranslateToLocal(customTreeKey)) {
			return new TranslationTextComponent(customTreeKey);
		}

		String grammar = Translator.translateToLocal("for.trees.grammar.leaves");
		String localizedName = Translator.translateToLocal(unlocalizedSpeciesName);

		String leaves = Translator.translateToLocal("for.trees.grammar.leaves.type");
		return grammar.replaceAll("%SPECIES", localizedName).replaceAll("%TYPE", leaves);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColorFromItemstack(ItemStack itemStack, int renderPass) {
		if (itemStack.getTag() == null) {
			return ModuleArboriculture.proxy.getFoliageColorBasic();
		}

		TileLeaves tileLeaves = new TileLeaves();
		tileLeaves.read(itemStack.getTag());

		if (renderPass == BlockAbstractLeaves.FRUIT_COLOR_INDEX) {
			return tileLeaves.getFruitColour();
		} else {
			PlayerEntity player = Minecraft.getInstance().player;
			return tileLeaves.getFoliageColour(player);
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState) {
		return false;
	}

}
