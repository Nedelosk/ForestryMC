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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import forestry.api.core.IModelManager;
import forestry.core.models.IStateMapperRegister;
import forestry.apiculture.MaterialBeehive;
import forestry.apiculture.multiblock.IAlvearyControllerInternal;
import forestry.apiculture.multiblock.TileAlveary;
import forestry.apiculture.multiblock.TileAlvearyFan;
import forestry.apiculture.multiblock.TileAlvearyHeater;
import forestry.apiculture.multiblock.TileAlvearyHygroregulator;
import forestry.apiculture.multiblock.TileAlvearyPlain;
import forestry.apiculture.multiblock.TileAlvearySieve;
import forestry.apiculture.multiblock.TileAlvearyStabiliser;
import forestry.apiculture.multiblock.TileAlvearySwarmer;
import forestry.apiculture.network.packets.PacketAlvearyChange;
import forestry.core.blocks.BlockStructure;
import forestry.core.tiles.IActivatable;
import forestry.core.tiles.TileUtil;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.ItemTooltipUtil;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.Translator;

public abstract class BlockAlveary extends BlockStructure {//implements IStateMapperRegister {
	private static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);
	private static final EnumProperty<AlvearyPlainType> PLAIN_TYPE = EnumProperty.create("type", AlvearyPlainType.class);

	private enum State implements IStringSerializable {
		ON, OFF;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}

	private enum AlvearyPlainType implements IStringSerializable {
		NORMAL, ENTRANCE, ENTRANCE_LEFT, ENTRANCE_RIGHT;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}

	public static Map<BlockAlvearyType, BlockAlveary> create() {
		Map<BlockAlvearyType, BlockAlveary> blockMap = new EnumMap<>(BlockAlvearyType.class);
		for (final BlockAlvearyType type : BlockAlvearyType.VALUES) {
			BlockAlveary block = new BlockAlveary() {
				@Override
				public BlockAlvearyType getAlvearyType() {
					return type;
				}
			};
			blockMap.put(type, block);
		}
		return blockMap;
	}

	public BlockAlveary() {
		super(Block.Properties.create(MaterialBeehive.BEEHIVE_ALVEARY)
				.hardnessAndResistance(1f)
				.sound(SoundType.WOOD));

		BlockAlvearyType alvearyType = getAlvearyType();
		BlockState defaultState = this.getStateContainer().getBaseState();
		if (alvearyType == BlockAlvearyType.PLAIN) {
			defaultState = defaultState.with(PLAIN_TYPE, AlvearyPlainType.NORMAL);
		} else if (alvearyType.activatable) {
			defaultState = defaultState.with(STATE, State.OFF);
		}
		setDefaultState(defaultState);

//		setCreativeTab(ItemGroups.tabApiculture);
//		setHarvestLevel("axe", 0);
	}

	public abstract BlockAlvearyType getAlvearyType();

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return true;
	}

	//TODO - idk
	/*@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		BlockAlvearyType type = getAlvearyType();
		switch (type) {
			case SWARMER:
				return new TileAlvearySwarmer();
			case FAN:
				return new TileAlvearyFan();
			case HEATER:
				return new TileAlvearyHeater();
			case HYGRO:
				return new TileAlvearyHygroregulator();
			case STABILISER:
				return new TileAlvearyStabiliser();
			case SIEVE:
				return new TileAlvearySieve();
			case PLAIN:
			default:
				return new TileAlvearyPlain();
		}
	}*/

	/* ITEM MODELS */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		manager.registerItemModel(item, 0, "apiculture/alveary." + getAlvearyType());
	}

	//probably not needed
//	@Override
//	protected BlockStateContainer createBlockState() {
//		BlockAlvearyType alvearyType = getAlvearyType();
//
//		if (alvearyType == BlockAlvearyType.PLAIN) {
//			return new BlockStateContainer(this, PLAIN_TYPE);
//		} else if (alvearyType.activatable) {
//			return new BlockStateContainer(this, STATE);
//		} else {
//			return new BlockStateContainer(this);
//		}
//	}

	//TODO not sure how actual state works anymore, probably just means flattening
//	@Override
//	public BlockState getActualState(BlockState state, IBlockReader world, BlockPos pos) {
//		TileAlveary tile = TileUtil.getTile(world, pos, TileAlveary.class);
//		if (tile == null) {
//			return super.getActualState(state, world, pos);
//		}
//
//		if (tile instanceof IActivatable) {
//			if (((IActivatable) tile).isActive()) {
//				state = state.with(STATE, State.ON);
//			} else {
//				state = state.with(STATE, State.OFF);
//			}
//		} else if (getAlvearyType() == BlockAlvearyType.PLAIN) {
//			if (!tile.getMultiblockLogic().getController().isAssembled()) {
//				state = state.with(PLAIN_TYPE, AlvearyPlainType.NORMAL);
//			} else {
//				BlockState blockStateAbove = world.getBlockState(pos.up());
//				Block blockAbove = blockStateAbove.getBlock();
//				if (BlockUtil.isWoodSlabBlock(blockStateAbove, blockAbove, world, pos)) {
//					List<Direction> blocksTouching = getBlocksTouching(world, pos);
//					switch (blocksTouching.size()) {
//						case 3:
//							state = state.with(PLAIN_TYPE, AlvearyPlainType.ENTRANCE);
//							break;
//						case 2:
//							if (blocksTouching.contains(Direction.SOUTH) && blocksTouching.contains(Direction.EAST) ||
//								blocksTouching.contains(Direction.NORTH) && blocksTouching.contains(Direction.WEST)) {
//								state = state.with(PLAIN_TYPE, AlvearyPlainType.ENTRANCE_LEFT);
//							} else {
//								state = state.with(PLAIN_TYPE, AlvearyPlainType.ENTRANCE_RIGHT);
//							}
//							break;
//						default:
//							state = state.with(PLAIN_TYPE, AlvearyPlainType.NORMAL);
//							break;
//					}
//				} else {
//					state = state.with(PLAIN_TYPE, AlvearyPlainType.NORMAL);
//				}
//			}
//		}
//
//		return super.getActualState(state, world, pos);
//	}


	private static List<Direction> getBlocksTouching(IBlockReader world, BlockPos blockPos) {
		List<Direction> touching = new ArrayList<>();
		//TODO - AT?
		for (Direction direction : Direction.BY_HORIZONTAL_INDEX) {	//TODO AT
			BlockState blockState = world.getBlockState(blockPos.offset(direction));
			if (blockState.getBlock() instanceof BlockAlveary) {
				touching.add(direction);
			}
		}
		return touching;
	}

//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public void registerStateMapper() {
//		ModelLoader.setCustomStateMapper(this, new AlvearyStateMapper(getAlvearyType()));
//	}
//
//	@OnlyIn(Dist.CLIENT)
//	private static class AlvearyStateMapper extends StateMapperBase {
//		private final BlockAlvearyType type;
//
//		public AlvearyStateMapper(BlockAlvearyType type) {
//			this.type = type;
//		}
//
//		@Override
//		protected ModelResourceLocation getModelResourceLocation(BlockState state) {
//			String resourceDomain = ForgeRegistries.BLOCKS.getKey(state.getBlock()).getNamespace();
//			String resourceLocation = "apiculture/alveary_" + type;
//			String propertyString = getPropertyString(state.getProperties());
//			return new ModelResourceLocation(resourceDomain + ':' + resourceLocation, propertyString);
//		}
//
//	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
		TileUtil.actOnTile(worldIn, pos, TileAlveary.class, tileAlveary -> {
			// We must check that the slabs on top were not removed
			IAlvearyControllerInternal alveary = tileAlveary.getMultiblockLogic().getController();
			alveary.reassemble();
			BlockPos referenceCoord = alveary.getReferenceCoord();
			NetworkUtil.sendNetworkPacket(new PacketAlvearyChange(referenceCoord), referenceCoord, worldIn);
		});
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			tooltip.add(new TranslationTextComponent("tile.for.alveary.tooltip"));
		} else {
			ItemTooltipUtil.addShiftInformation(stack, world, tooltip, flag);
		}
	}
}
