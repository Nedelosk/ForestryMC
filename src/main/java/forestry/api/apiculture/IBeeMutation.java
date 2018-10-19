/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture;

import genetics.api.mutation.IMutation;

import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.apiculture.genetics.IBeeGenome;
import forestry.api.apiculture.genetics.IBeeRoot;

public interface IBeeMutation extends IMutation {

	@Override
	IBeeRoot getRoot();

	/**
	 * @return float representing the chance for mutation to occur. note that this is 0 - 100 based, since it was an integer previously!
	 */
	float getChance(IBeeHousing housing, IAlleleBeeSpecies allele0, IAlleleBeeSpecies allele1, IBeeGenome genome0, IBeeGenome genome1);
}
