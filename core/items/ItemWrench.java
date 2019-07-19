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

import java.awt.GraphicsConfigTemplate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

//import net.minecraftforge.fml.common.Optional;

import net.minecraftforge.common.ToolType;

import forestry.core.config.Constants;

//import buildcraft.api.tools.IToolWrench;

//@Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = Constants.BCLIB_MOD_ID)
public class ItemWrench extends ItemForestry {//implements IToolWrench {

	public ItemWrench() {
		super((new Item.Properties())
		.addToolType(ToolType.get("wrench"),0));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		Direction facing = context.getFace();
		Hand hand = context.getHand();

		BlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		if (block.rotate(state, worldIn, pos, Rotation.CLOCKWISE_90) != state) {	//TODO - how to rotate based onn a direction, might need helper method
			player.swingArm(hand);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

//	@Override
//	public boolean canWrench(PlayerEntity player, Hand hand, ItemStack wrench, RayTraceResult rayTrace) {
//		return true;
//	}
//
//	@Override
//	public void wrenchUsed(PlayerEntity player, Hand hand, ItemStack wrench, RayTraceResult rayTrace) {
//	}
}