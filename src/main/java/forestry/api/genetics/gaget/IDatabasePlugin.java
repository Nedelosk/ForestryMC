/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics.gaget;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.genetics.IIndividualForestry;

/**
 * An ISpeciesPlugin provides methods that are used in the alyzer and database to display information about an
 * individual.
 */
@SideOnly(Side.CLIENT)
public interface IDatabasePlugin<I extends IIndividualForestry> {

	/* ALYZER */
	List<String> getHints();

	IDatabaseTab[] getTabs();

	Map<String, ItemStack> getIndividualStacks();
}
