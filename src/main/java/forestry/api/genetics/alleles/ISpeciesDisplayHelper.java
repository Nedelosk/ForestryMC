package forestry.api.genetics.alleles;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.genetics.IIndividualForestry;
import forestry.api.genetics.IIndividualRootForestry;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAlleleSpeciesForestry;

@SideOnly(Side.CLIENT)
public interface ISpeciesDisplayHelper<I extends IIndividualForestry, S extends IAlleleSpeciesForestry> {
	/**
	 * Retrieves a stack that can and should only be used on the client side in a gui.
	 *
	 * @return A empty stack, if the species was not registered before the creation of this handler or if the species is
	 * 			not a species of the {@link IIndividualRootForestry}.
	 */
	ItemStack getDisplayStack(IAlleleSpeciesForestry species, ISpeciesType type);

	ItemStack getDisplayStack(IAlleleSpeciesForestry species);
}
