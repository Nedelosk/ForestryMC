/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import javax.annotation.Nullable;

import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import genetics.api.organism.IOrganismType;
import genetics.api.root.IIndividualRoot;

import forestry.api.genetics.gaget.IAlyzerPlugin;
import forestry.api.genetics.gaget.IDatabasePlugin;

/**
 * Describes a class of species (i.e. bees, trees, butterflies), provides helper functions and access to common functionality.
 */
public interface IIndividualRootForestry<I extends IIndividualForestry> extends IIndividualRoot<I> {

	/**
	 * @return Integer denoting the number of (counted) species of this type in the world.
	 */
	int getSpeciesCount();

	/* BREEDING TRACKER */
	IBreedingTracker getBreedingTracker(World world, @Nullable GameProfile player);

	/**
	 * The type of the species that will be used at the given position of the mutation recipe in the gui.
	 *
	 * @param position 0 = first parent, 1 = second parent, 2 = result
	 */
	default IOrganismType getTypeForMutation(int position) {
		return getTypes().getDefaultType();
	}

	/* RESEARCH */
	/**
	 * Plugin to add information for the handheld genetic analyzer.
	 */
	IAlyzerPlugin getAlyzerPlugin();

	/**
	 * Plugin to add information for the handheld genetic analyzer and the database.
	 * @since 5.7
	 */
	@Nullable
	@SideOnly(Side.CLIENT)
	default IDatabasePlugin getSpeciesPlugin(){
		return null;
	}
}
