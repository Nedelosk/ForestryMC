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
package forestry.core.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.core.ForestryAPI;
import forestry.core.tiles.TileUtil;

public class GuiHandler implements IGuiHandler {
	public static void openGui(PlayerEntity PlayerEntity, IGuiHandlerEntity guiHandler) {
		openGui(PlayerEntity, guiHandler, (short) 0);
	}

	public static void openGui(PlayerEntity playerEntity, IGuiHandlerEntity guiHandler, short data) {
		int guiData = encodeGuiData(guiHandler, data);
		//TODO - can only be called on server?
		NetworkHooks.openGui(playerEntity, () -> guiHandler.getContainer(playerEntity, guiData));
		playerEntity.openGui(ForestryAPI.instance, guiData, playerEntity.world, guiHandler.getIdOfEntity(), 0, 0);
	}

	public static void openGui(PlayerEntity playerEntity, IGuiHandlerItem guiHandler) {
		openGui(playerEntity, guiHandler, (short) 0);
	}

	public static void openGui(PlayerEntity playerEntity, IGuiHandlerItem guiHandler, short data) {
		int guiData = encodeGuiData(guiHandler, data);
		playerEntity.openGui(ForestryAPI.instance, guiData, playerEntity.world, 0, 0, 0);
	}

	public static void openGui(PlayerEntity playerEntity, IGuiHandlerTile guiHandler) {
		openGui(playerEntity, guiHandler, (short) 0);
	}

	public static void openGui(PlayerEntity playerEntity, IGuiHandlerTile guiHandler, short data) {
		int guiData = encodeGuiData(guiHandler, data);
		BlockPos coordinates = guiHandler.getCoordinates();
		playerEntity.openGui(ForestryAPI.instance, guiData, playerEntity.world, coordinates.getX(), coordinates.getY(), coordinates.getZ());
	}

	private static int encodeGuiData(IGuiHandlerForestry guiHandler, short data) {
		GuiId guiId = GuiIdRegistry.getGuiIdForGuiHandler(guiHandler);
		return data << 16 | guiId.getId();
	}

	@Nullable
	private static GuiId decodeGuiID(int guiData) {
		int guiId = guiData & 0xFF;
		return GuiIdRegistry.getGuiId(guiId);
	}

	private static short decodeGuiData(int guiId) {
		return (short) (guiId >> 16);
	}

	@Override
	@Nullable
	@OnlyIn(Dist.CLIENT)
	public Object getClientGuiElement(int guiData, PlayerEntity player, World world, int x, int y, int z) {
		GuiId guiId = decodeGuiID(guiData);
		if (guiId == null) {
			return null;
		}
		short data = decodeGuiData(guiData);
		BlockPos pos = new BlockPos(x, y, z);

		switch (guiId.getGuiType()) {
			case Item: {
				for (Hand hand : Hand.values()) {
					ItemStack heldItem = player.getHeldItem(hand);
					if (!heldItem.isEmpty()) {
						Item item = heldItem.getItem();
						if (guiId.getGuiHandlerClass().isInstance(item)) {
							return ((IGuiHandlerItem) item).getGui(player, heldItem, data);
						}
					}
				}
				break;
			}
			case Tile: {
				TileEntity tileEntity = TileUtil.getTile(world, pos);
				if (guiId.getGuiHandlerClass().isInstance(tileEntity)) {
					return ((IGuiHandlerTile) tileEntity).getGui(player, data);
				}
				break;
			}
			case Entity: {
				Entity entity = world.getEntityByID(x);
				if (guiId.getGuiHandlerClass().isInstance(entity)) {
					return ((IGuiHandlerEntity) entity).getGui(player, data);
				}
				break;
			}
		}
		return null;
	}

	@Override
	@Nullable
	public Object getServerGuiElement(int guiData, PlayerEntity player, World world, int x, int y, int z) {
		GuiId guiId = decodeGuiID(guiData);
		if (guiId == null) {
			return null;
		}
		short data = decodeGuiData(guiData);
		BlockPos pos = new BlockPos(x, y, z);

		switch (guiId.getGuiType()) {
			case Item: {
				for (Hand hand : Hand.values()) {
					ItemStack heldItem = player.getHeldItem(hand);
					if (!heldItem.isEmpty()) {
						Item item = heldItem.getItem();
						if (guiId.getGuiHandlerClass().isInstance(item)) {
							return ((IGuiHandlerItem) item).getContainer(player, heldItem, data);
						}
					}
				}
				break;
			}
			case Tile: {
				TileEntity tileEntity = TileUtil.getTile(world, pos);
				if (guiId.getGuiHandlerClass().isInstance(tileEntity)) {
					return ((IGuiHandlerTile) tileEntity).getContainer(player, data);
				}
				break;
			}
			case Entity: {
				Entity entity = world.getEntityByID(x);
				if (guiId.getGuiHandlerClass().isInstance(entity)) {
					return ((IGuiHandlerEntity) entity).getContainer(player, data);
				}
				break;
			}
		}
		return null;
	}
}