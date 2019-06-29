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
package forestry.core.entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import forestry.core.gui.IGuiHandlerEntity;
import forestry.core.tiles.ITitled;
import forestry.core.utils.Translator;

public abstract class MinecartEntityForestry extends MinecartEntity implements ITitled, IGuiHandlerEntity {

	//TODO - create entity type?
	@SuppressWarnings("unused")
	public MinecartEntityForestry(World world) {
		super(EntityType.MINECART, world);
		setHasDisplayTile(true);
	}

	public MinecartEntityForestry(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ);
		setHasDisplayTile(true);
	}

	//TODO - event not in forge atm
//	@Override
//	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
//		if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, player, hand))) {
//			return true;
//		}
//
//		if (!world.isRemote) {
//			GuiHandler.openGui(player, this);
//		}
//		return true;
//	}

	/* MinecartEntity */
	@Override
	public boolean canBeRidden() {
		return false;
	}

	@Override
	public boolean isPoweredCart() {
		return false;
	}

	// cart contents
	@Override
	public abstract BlockState getDisplayTile();

	// cart itemStack
	@Override
	public abstract ItemStack getCartItem();

	@Override
	public void killMinecart(DamageSource damageSource) {
		super.killMinecart(damageSource);
		if (/*this.world.getGameRules().getBoolean("doEntityDrops")*/ true) {	//TODO - revisit when class is deobsfucated
			Block block = getDisplayTile().getBlock();
			entityDropItem(new ItemStack(block), 0.0F);
		}
	}

	// fix cart contents rendering as black in the End dimension
	@Override
	public float getBrightness() {
		return 1.0f;
	}

	@Override
	public ITextComponent getName() {
		return Translator.translateToLocal(getUnlocalizedTitle());
	}

	/* ITitled */
	@Override
	public String getUnlocalizedTitle() {
		ItemStack cartItem = getCartItem();
		return cartItem.getTranslationKey() + ".name";
	}

	@Override
	public int getIdOfEntity() {
		return getEntityId();
	}
}
