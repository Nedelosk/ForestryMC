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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import forestry.core.inventory.IInventoryAdapter;
import forestry.core.tiles.IFilterSlotDelegate;
import forestry.core.utils.InventoryUtil;

public abstract class MinecartEntityContainerForestry extends MinecartEntityForestry implements ISidedInventory, IFilterSlotDelegate {
	/**
	 * When set to true, the minecart will drop all items when setDead() is called. When false (such as when travelling
	 * dimensions) it preserves its contents.
	 */
	private boolean dropContentsWhenDead = true;

	@SuppressWarnings("unused")
	public MinecartEntityContainerForestry(World world) {
		super(world);
	}

	public MinecartEntityContainerForestry(World world, double posX, double posY, double posZ) {
		super(world, posX, posY, posZ);
	}

	@Override
	protected void readAdditional(CompoundNBT CompoundNBT) {
		super.read(CompoundNBT);
		getInternalInventory().readFromNBT(CompoundNBT);
	}

	@Override
	protected void writeAdditional(CompoundNBT CompoundNBT) {
		super.writeAdditional(CompoundNBT);
		getInternalInventory().writeToNBT(CompoundNBT);
	}

	@Override
	public void setDead() {
		if (dropContentsWhenDead && !world.isRemote) {
			InventoryUtil.dropInventory(getInternalInventory(), world, posX, posY, posZ);
		}
		super.setDead();
	}

	@Override
	protected void applyDrag() {
		int redstoneLevel = 15 - Container.calcRedstoneFromInventory(this);
		double drag = 0.98F + redstoneLevel * 0.001F;
		this.motionX *= drag;
		this.motionY *= 0.0D;
		this.motionZ *= drag;
	}

	@Override
	public Entity changeDimension(DimensionType dimensionIn) {
		this.dropContentsWhenDead = false;
		return super.changeDimension(dimensionIn);
	}

	/* IInventory */

	@Override
	public boolean isEmpty() {
		return getInternalInventory().isEmpty();
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return !isDead && player.getDistance(this) <= 64.0D;
	}

	@Override
	public int getSizeInventory() {
		return getInternalInventory().getSizeInventory();
	}

	@Override
	public final ItemStack getStackInSlot(int slotIndex) {
		return getInternalInventory().getStackInSlot(slotIndex);
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int amount) {
		return getInternalInventory().decrStackSize(slotIndex, amount);
	}

	@Override
	public ItemStack removeStackFromSlot(int slotIndex) {
		return getInternalInventory().removeStackFromSlot(slotIndex);
	}

	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
		getInternalInventory().setInventorySlotContents(slotIndex, itemstack);
	}

	@Override
	public final int getInventoryStackLimit() {
		return getInternalInventory().getInventoryStackLimit();
	}

	@Override
	public final void openInventory(PlayerEntity player) {
		getInternalInventory().openInventory(player);
	}

	@Override
	public final void closeInventory(PlayerEntity player) {
		getInternalInventory().closeInventory(player);
	}

	@Override
	public ITextComponent getDisplayName() {
		return getInternalInventory().getDisplayName();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public final boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
		return getInternalInventory().isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public final boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		return getInternalInventory().canSlotAccept(slotIndex, itemStack);
	}

	@Override
	public boolean isLocked(int slotIndex) {
		return getInternalInventory().isLocked(slotIndex);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return getInternalInventory().getSlotsForFace(side);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, Direction side) {
		return getInternalInventory().canInsertItem(slot, stack, side);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, Direction side) {
		return getInternalInventory().canExtractItem(slot, stack, side);
	}

	@Override
	public void markDirty() {

	}

	protected abstract IInventoryAdapter getInternalInventory();

	@Override
	public int getField(int id) {
		return getInternalInventory().getField(id);
	}

	@Override
	public void setField(int id, int value) {
		getInternalInventory().setField(id, value);
	}

	@Override
	public int getFieldCount() {
		return getInternalInventory().getFieldCount();
	}

	@Override
	public void clear() {
		getInternalInventory().clear();
	}
}
