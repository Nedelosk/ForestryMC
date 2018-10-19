/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import net.minecraft.entity.player.EntityPlayer;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import forestry.api.genetics.alleles.IAlleleSpeciesForestry;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IIndividualRootForestry;
import forestry.api.genetics.IMutation;

public abstract class ForestryEvent extends Event {

	private static abstract class BreedingEvent extends ForestryEvent {
		public final IIndividualRootForestry root;
		public final IBreedingTracker tracker;
		public final GameProfile username;

		private BreedingEvent(IIndividualRootForestry root, GameProfile username, IBreedingTracker tracker) {
			this.root = root;
			this.username = username;
			this.tracker = tracker;
		}
	}

	public static class SpeciesDiscovered extends BreedingEvent {
		public final IAlleleSpeciesForestry species;

		public SpeciesDiscovered(IIndividualRootForestry root, GameProfile username, IAlleleSpeciesForestry species, IBreedingTracker tracker) {
			super(root, username, tracker);
			this.species = species;
		}
	}

	public static class MutationDiscovered extends BreedingEvent {
		public final IMutation allele;

		public MutationDiscovered(IIndividualRootForestry root, GameProfile username, IMutation allele, IBreedingTracker tracker) {
			super(root, username, tracker);
			this.allele = allele;
		}
	}

	public static class SyncedBreedingTracker extends ForestryEvent {
		public final IBreedingTracker tracker;
		public final EntityPlayer player;

		public SyncedBreedingTracker(IBreedingTracker tracker, EntityPlayer player) {
			this.tracker = tracker;
			this.player = player;
		}
	}

	/**
	 * Posted before forestry registers all items and blocks.
	 *
	 * @deprecated removed in 1.13
	 */
	@Deprecated
	public static class PreInit extends ForestryEvent{
		/**
		 * The main mod instance for Forestry.
		 */
		public Object instance;
		public final FMLPreInitializationEvent event;

		public PreInit(Object instance, FMLPreInitializationEvent event) {
			this.instance = instance;
			this.event = event;
		}
	}
}
