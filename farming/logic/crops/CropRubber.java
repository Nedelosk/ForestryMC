/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http:www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.farming.logic.crops;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

import javax.annotation.Nullable;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import forestry.core.config.Constants;
import forestry.core.network.packets.PacketFXSignal;
import forestry.core.utils.ItemStackUtil;
import forestry.core.utils.NetworkUtil;
import forestry.plugins.PluginIC2;
import forestry.plugins.PluginTechReborn;

public class CropRubber extends CropDestroy {

	public CropRubber(World world, BlockState blockState, BlockPos position) {
		super(world, blockState, position, getReplantState(blockState));
	}

	/**
	 * Convert a "wet" rubber log blockstate into the dry version.
	 * Total hack since we don't have access to the blockstates.
	 */
	private static <T extends Comparable<T>> BlockState getReplantState(BlockState sappyState) {
		if (hasRubberToHarvest(sappyState)) {
			for (Map.Entry<IProperty<?>, Comparable<?>> wetPropertyEntry : sappyState.getProperties().entrySet()) {
				String valueWetString = wetPropertyEntry.getValue().toString();
				String valueDryString = valueWetString.replace("wet", "dry");
				IProperty<?> property = wetPropertyEntry.getKey();
				if (property instanceof PropertyBool && property.getName().equals("hassap")) {
					return sappyState.with(PropertyBool.create("hassap"), false);
				}

				BlockState baseState = sappyState.getBlock().getBlockState().getBaseState();
				BlockState dryState = getStateWithValue(baseState, property, valueDryString);
				if (dryState != null) {
					return dryState;
				}
			}
		}

		return sappyState.getBlock().getDefaultState();
	}

	public static boolean hasRubberToHarvest(BlockState blockState) {
		Block block = blockState.getBlock();
		if (PluginIC2.rubberWood != null && ItemStackUtil.equals(block, PluginIC2.rubberWood)) {
			ImmutableCollection<Comparable<?>> propertyValues = blockState.getProperties().values();
			for (Comparable<?> propertyValue : propertyValues) {
				if (propertyValue.toString().contains("wet")) {
					return true;
				}
			}
		} else if (PluginTechReborn.RUBBER_WOOD != null && ItemStackUtil.equals(block, PluginTechReborn.RUBBER_WOOD)) {
			return blockState.getValue(PropertyBool.create("hassap"));
		}
		return false;
	}

	@Nullable
	private static <T extends Comparable<T>> BlockState getStateWithValue(BlockState baseState, IProperty<T> property, String valueString) {
		Optional<T> value = property.parseValue(valueString);
		if (value.isPresent()) {
			return baseState.with(property, value.get());
		}
		return null;
	}

	@Override
	protected NonNullList<ItemStack> harvestBlock(World world, BlockPos pos) {
		NonNullList<ItemStack> harvested = NonNullList.create();
		Block harvestBlock = world.getBlockState(pos).getBlock();
		if (PluginIC2.rubberWood != null && ItemStackUtil.equals(harvestBlock, PluginIC2.rubberWood)) {
			harvested.add(PluginIC2.resin.copy());
		} else if (PluginTechReborn.RUBBER_WOOD != null && ItemStackUtil.equals(harvestBlock, PluginTechReborn.RUBBER_WOOD)) {
			harvested.add(PluginTechReborn.sap.copy());
		}
		PacketFXSignal packet = new PacketFXSignal(PacketFXSignal.VisualFXType.BLOCK_BREAK, PacketFXSignal.SoundFXType.BLOCK_BREAK, pos, blockState);
		NetworkUtil.sendNetworkPacket(packet, pos, world);

		world.setBlockState(pos, replantState, Constants.FLAG_BLOCK_SYNC);
		return harvested;
	}

}
