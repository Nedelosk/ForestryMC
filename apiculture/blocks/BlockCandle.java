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
package forestry.apiculture.blocks;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import forestry.api.core.ItemGroups;
import forestry.apiculture.tiles.TileCandle;
import forestry.core.blocks.IColoredBlock;
import forestry.core.tiles.TileUtil;
import forestry.core.utils.ItemStackUtil;

import static forestry.core.blocks.BlockBase.FACING;

public class BlockCandle extends TorchBlock implements IItemModelRegister, IColoredBlock {

	private static final ImmutableMap<String, Integer> colours;
	public static final Set<Item> lightingItems;
	public static final String colourTagName = "colour";

	public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);

	enum State implements IStringSerializable {
		ON("on"), OFF("off");

		private final String name;

		State(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	static {
		colours = ImmutableMap.<String, Integer>builder()
			.put("dyeWhite", new Color(255, 255, 255).getRGB())
			.put("dyeOrange", new Color(219, 125, 62).getRGB())
			.put("dyeMagenta", new Color(255, 20, 255).getRGB())
			.put("dyeLightBlue", new Color(107, 138, 201).getRGB())
			.put("dyeYellow", new Color(255, 255, 20).getRGB())
			.put("dyeLime", new Color(20, 255, 20).getRGB())
			.put("dyePink", new Color(208, 132, 153).getRGB())
			.put("dyeGray", new Color(74, 74, 74).getRGB())
			.put("dyeLightGray", new Color(154, 161, 161).getRGB())
			.put("dyeCyan", new Color(20, 255, 255).getRGB())
			.put("dyePurple", new Color(126, 61, 181).getRGB())
			.put("dyeBlue", new Color(20, 20, 255).getRGB())
			.put("dyeBrown", new Color(79, 50, 31).getRGB())
			.put("dyeGreen", new Color(53, 70, 27).getRGB())
			.put("dyeRed", new Color(150, 52, 48).getRGB())
			.put("dyeBlack", new Color(20, 20, 20).getRGB())
			.build();

		lightingItems = new HashSet<>(Arrays.asList(
			Items.FLINT_AND_STEEL,
			Items.FLINT,
			Item.getItemFromBlock(Blocks.TORCH)
		));
	}

	public BlockCandle() {
		this.setHardness(0.0F);
		this.setSoundType(SoundType.WOOD);
		setCreativeTab(ItemGroups.tabApiculture);
		setDefaultState(this.blockState.getBaseState().with(FACING, Direction.UP).with(STATE, State.OFF));
	}

//	@Override
//	protected BlockStateContainer createBlockState() {
//		return new BlockStateContainer(this, FACING, STATE);
//	}

//	@Override
//	public BlockState getActualState(BlockState state, IBlockReader world, BlockPos pos) {
//		TileCandle tileCandle = TileUtil.getTile(world, pos, TileCandle.class);
//		if (tileCandle != null && tileCandle.isLit()) {
//			state = state.with(STATE, State.ON);
//		}
//		return super.getActualState(state, world, pos);
//	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		manager.registerItemModel(item, 0, "candle");
		manager.registerItemModel(item, 1, "candle");
	}

	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		TileCandle candle = TileUtil.getTile(world, pos, TileCandle.class);
		if (candle != null && candle.isLit()) {
			return 14;
		}
		return 0;
	}

	@Override
	public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1, 0));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		TileCandle tileCandle = TileUtil.getTile(worldIn, pos, TileCandle.class);
		if (tileCandle == null) {
			return false;
		}
		final boolean isLit = tileCandle.isLit();

		boolean flag = false;
		boolean toggleLitState = true;

		ItemStack heldItem = playerIn.getHeldItem(hand);

		if (!isLit) {
			if (heldItem.isEmpty() || !lightingItems.contains(heldItem.getItem())) {
				toggleLitState = false;
			} else if (ItemStackUtil.equals(this, heldItem) && isLit(heldItem)) {
				toggleLitState = true;
			}
		}

		if (!heldItem.isEmpty()) {
			if (ItemStackUtil.equals(this, heldItem)) {
				if (!isLit(heldItem)) {
					// Copy the colour of an unlit, coloured candle.
					if (heldItem.getTag() != null && heldItem.getTag().contains(colourTagName)) {
						tileCandle.setColour(heldItem.getTag().getInt(colourTagName));
					} else {
						// Reset to white if item has no
						tileCandle.setColour(0xffffff);
					}
				} else {
					toggleLitState = true;
				}
				flag = true;
			} else {
				boolean dyed = tryDye(heldItem, isLit, tileCandle);
				if (dyed) {
					worldIn.markBlockRangeForRenderUpdate(pos, pos);
					toggleLitState = false;
					flag = true;
				}
			}
		}

		if (toggleLitState) {
			tileCandle.setLit(!isLit);
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
			worldIn.profiler.startSection("checkLight");
			worldIn.checkLight(pos);
			worldIn.profiler.endSection();
			flag = true;
		}
		return flag;
	}

	private static boolean tryDye(ItemStack held, boolean isLit, TileCandle tileCandle) {
		// Check for dye-able.
		for (Map.Entry<String, Integer> colour : colours.entrySet()) {
			String colourName = colour.getKey();
			for (ItemStack stack : OreDictionary.getOres(colourName)) {
				if (OreDictionary.itemMatches(stack, held, true)) {
					if (isLit) {
						tileCandle.setColour(colour.getValue());
					} else {
						tileCandle.addColour(colour.getValue());
					}
					return true;
				}
			}
		}
		return false;
	}

	//TODO - is this fixed?
	/* DROP HANDLING */
	// Hack: 	When harvesting we need to get the drops in onBlockHarvested,
	// 			because Mojang destroys the block and tile before calling getDrops.
	private final ThreadLocal<ItemStack> drop = new ThreadLocal<>();

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote) {
			ItemStack itemStack = getCandleDrop(world, pos);
			drop.set(itemStack);
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockReader world, BlockPos pos, BlockState state, int fortune) {
		ItemStack dropStack = drop.get();
		drop.remove();

		// not harvested, get drops normally
		if (dropStack == null) {
			dropStack = getCandleDrop(world, pos);
		}

		drops.add(dropStack);
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
		return getCandleDrop(world, pos);
	}

	private ItemStack getCandleDrop(IBlockReader world, BlockPos pos) {
		TileCandle tileCandle = TileUtil.getTile(world, pos, TileCandle.class);
		if (tileCandle == null) {
			return new ItemStack(this);
		}
		int colour = tileCandle.getColour();

		int newMeta = tileCandle.isLit() ? 1 : 0;
		ItemStack itemStack = new ItemStack(this, 1, newMeta);
		if (colour != 0xffffff) {
			// When dropped, tag new item stack with colour. Unless it's white, then do no such thing for maximum stacking.
			CompoundNBT tag = new CompoundNBT();
			tag.putInt(colourTagName, colour);
			itemStack.setTag(tag);
		}
		return itemStack;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileCandle tileCandle = TileUtil.getTile(world, pos, TileCandle.class);
		if (tileCandle != null) {
			int colour = getColourValueFromItemStack(stack);
			boolean isLit = isLit(stack);
			tileCandle.setColour(colour);
			tileCandle.setLit(isLit);
			if (tileCandle.isLit()) {
				world.profiler.startSection("checkLight");
				world.checkLight(pos);
				world.profiler.endSection();
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileCandle tileCandle = TileUtil.getTile(worldIn, pos, TileCandle.class);
		if (tileCandle != null && tileCandle.isLit()) {
			super.randomDisplayTick(stateIn, worldIn, pos, rand);
		}
	}

	private static int getColourValueFromItemStack(ItemStack itemStack) {
		int value = 0xffffff; // default to white.
		if (itemStack.getTag() != null) {
			CompoundNBT tag = itemStack.getTag();
			if (tag.contains(colourTagName)) {
				value = tag.getInt(colourTagName);
			}
		}
		return value;
	}

	public static boolean isLit(ItemStack itemStack) {
		return itemStack.getItemDamage() > 0;
	}

	public static void addItemToLightingList(Item item) {
		lightingItems.add(item);
	}

	public ItemStack getUnlitCandle(int amount) {
		return new ItemStack(this, amount, 0);
	}

	public ItemStack getLitCandle(int amount) {
		return new ItemStack(this, amount, 1);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public int colorMultiplier(BlockState state, @Nullable IBlockReader worldIn, @Nullable BlockPos pos, int tintIndex) {
		if (worldIn != null && pos != null) {
			TileCandle tileCandle = TileUtil.getTile(worldIn, pos, TileCandle.class);
			if (tileCandle != null) {
				return tileCandle.getColour();
			}
		}
		return 0xffffff;
	}
}
