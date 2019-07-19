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
package forestry.farming.blocks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.properties.EnumProperty;
//import net.minecraft.block.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.storage.loot.LootContext;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.ModelProperty;
//import net.minecraftforge.common.property.ExtendedBlockState;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import net.minecraftforge.common.property.IUnlistedProperty;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.core.IModelManager;
import forestry.api.core.ItemGroups;
import forestry.core.blocks.BlockStructure;
import forestry.core.blocks.properties.UnlistedBlockAccess;
import forestry.core.blocks.properties.UnlistedBlockPos;
import forestry.core.tiles.TileUtil;
import forestry.core.utils.ItemStackUtil;
import forestry.farming.models.EnumFarmBlockTexture;
import forestry.farming.tiles.TileFarm;
import forestry.farming.tiles.TileFarmControl;
import forestry.farming.tiles.TileFarmGearbox;
import forestry.farming.tiles.TileFarmHatch;
import forestry.farming.tiles.TileFarmPlain;
import forestry.farming.tiles.TileFarmValve;

public class BlockFarm extends BlockStructure {

	public static final EnumProperty<EnumFarmBlockType> META = EnumProperty.create("meta", EnumFarmBlockType.class);

	public BlockFarm() {
		super(Block.Properties.create(Material.ROCK)
		.hardnessAndResistance(1.0f));
//		setHarvestLevel("pickaxe", 0); TODO set in item
		setDefaultState(this.getStateContainer().getBaseState().with(META, EnumFarmBlockType.PLAIN));
//		setCreativeTab(ItemGroups.tabAgriculture); TODO set in item I think
	}

	//TODO - either flatten or work out how extended states work
//	@Override
//	public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos) {
//		return ((IExtendedBlockState) super.getExtendedState(state, world, pos)).with(UnlistedBlockPos.POS, pos)
//			.with(UnlistedBlockAccess.BLOCKACCESS, world);
//	}
//
//	@Override
//	protected BlockStateContainer createBlockState() {
//		return new ExtendedBlockState(this, new IProperty[]{META},
//			new ModelProperty<>()[]{UnlistedBlockPos.POS, UnlistedBlockAccess.BLOCKACCESS});
//	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < 6; i++) {
			if (i == 1) {
				continue;
			}

			for (EnumFarmBlockTexture block : EnumFarmBlockTexture.values()) {
				ItemStack stack = new ItemStack(this, 1);	//TODO - more meta stuff
				CompoundNBT compound = new CompoundNBT();
				block.saveToCompound(compound);
				stack.setTag(compound);
				list.add(stack);
			}
		}
	}

	//TODO - forge version doesnt seem to exist in block?
//	@Override
//	public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
////		List<ItemStack> drops = getDrops(world.getBlockState(pos), world, pos, 0);
////		if (drops.isEmpty()) {
////			return super.getPickBlock(state, target, world, pos, player);
////		}
////		return drops.get(0);
//		return new ItemStack(this);
//	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (stack.getTag() == null) {
			return;
		}

		TileFarm tile = TileUtil.getTile(world, pos, TileFarm.class);
		if (tile != null) {
			tile.setFarmBlockTexture(EnumFarmBlockTexture.getFromCompound(stack.getTag()));
		}
	}

	//TODO - idk
//	@Override
//	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest) {
//		if (!world.isRemote && canHarvestBlock(world, pos, player)) {
//			List<ItemStack> drops = getDrops(world, pos, world.getBlockState(pos), 0);
//			for (ItemStack drop : drops) {
//				ItemStackUtil.dropItemStackAsEntity(drop, world, pos);
//			}
//		}
//		return world.setBlockToAir(pos);
//	}

	//TODO not sjure about this
//	@Override
//	public static List<ItemStack> getDrops(BlockState state, ServerWorld worldIn, BlockPos pos, @Nullable TileEntity tileEntityIn) {
////		int meta = getMetaFromState(state);
//
//		List<ItemStack> ret = new ArrayList<>();
//		if(!(tileEntityIn instanceof TileFarm)) {
//			return ret;
//		}
//
//		ItemStack stack = new ItemStack(state.getBlock());
//		CompoundNBT compound = new CompoundNBT();
//		((TileFarm) te).getFarmBlockTexture().saveToCompound(compound);
//		stack.setTag(compound);
//		ret.add(stack);
//		return ret;
//	}

	//TODO - not sure where this lives now
//	@Override
//	public TileEntity createNewTileEntity(World world, int meta) {
//		switch (meta) {
//			case 2:
//				return new TileFarmGearbox();
//			case 3:
//				return new TileFarmHatch();
//			case 4:
//				return new TileFarmValve();
//			case 5:
//				return new TileFarmControl();
//			default:
//				return new TileFarmPlain();
//		}
//	}

	/* MODELS */
	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		for (int i = 0; i < 6; i++) {
			if (i == 1) {
				continue;
			}
			//TODO
//			ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation("forestry:ffarm", "inventory"));
		}
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return state.get(META) == EnumFarmBlockType.CONTROL;
	}

	public ItemStack get(EnumFarmBlockType type, int amount) {
		return new ItemStack(this, amount);//TODO, type.ordinal());
	}
}