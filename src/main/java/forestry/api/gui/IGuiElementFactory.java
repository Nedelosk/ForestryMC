/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.gui;

import javax.annotation.Nullable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleForestrySpecies;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IAlleleTolerance;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IForestryMutation;
import forestry.api.genetics.IGeneticAnalyzer;
import forestry.api.genetics.IGeneticAnalyzerProvider;
import forestry.api.gui.style.ITextStyle;

/**
 * A helper interface to create gui elements.
 */
@OnlyIn(Dist.CLIENT)
public interface IGuiElementFactory {

	IGeneticAnalyzer createAnalyzer(IWindowElement window, int xPos, int yPos, boolean rightBoarder, IGeneticAnalyzerProvider provider);

	/* GENETIC*/

	/**
	 * @param dominant True if you want the dominant color and false if you want the recessive color.
	 * @return The color code that forestry uses to show if a allele is dominant (true) or recessive (false).
	 */
	int getColorCoding(boolean dominant);

	ITextStyle getStateStyle(boolean dominant);

	ITextStyle getGuiStyle();

	/**
	 * @return Null if the mutation is secret and undiscovered. {@link IForestryMutation#isSecret()}
	 */
	@Nullable
	IGuiElement createMutation(int x, int y, int width, int height, IForestryMutation mutation, IAllele species, IBreedingTracker breedingTracker);

	/**
	 * @return Null if the mutation is secret and undiscovered. {@link IForestryMutation#isSecret()}
	 */
	@Nullable
	IGuiElement createMutationResultant(int x, int y, int width, int height, IForestryMutation mutation, IBreedingTracker breedingTracker);

	IGuiElement createFertilityInfo(IAlleleInteger fertilityAllele, int texOffset);

	IGuiElement createToleranceInfo(IAlleleTolerance toleranceAllele, IAlleleForestrySpecies species, String text);

	IGuiElement createToleranceInfo(IAlleleTolerance toleranceAllele);

	/* LAYOUTS */
	IElementLayout createHorizontal(int xPos, int yPos, int height);

	IElementLayout createVertical(int xPos, int yPos, int width);

	IElementGroup createPane(int xPos, int yPos, int width, int height);
}
