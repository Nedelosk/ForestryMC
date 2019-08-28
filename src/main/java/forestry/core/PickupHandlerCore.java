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
package forestry.core;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IForestrySpeciesRoot;
import forestry.api.genetics.IIndividual;

public class PickupHandlerCore implements IPickupHandler {

	@Override
	public boolean onItemPickup(PlayerEntity PlayerEntity, ItemEntity entityitem) {
		ItemStack itemstack = entityitem.getItem();
		if (itemstack.isEmpty()) {
			return false;
		}

		IForestrySpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(itemstack);
		if (root != null) {
			IIndividual individual = root.create(itemstack);
			if (individual != null) {
				IBreedingTracker tracker = root.getBreedingTracker(entityitem.world, PlayerEntity.getGameProfile());
				tracker.registerPickup(individual);
			}
		}

		return false;
	}

}
