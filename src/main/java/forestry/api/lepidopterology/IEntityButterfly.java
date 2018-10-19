/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;

import forestry.api.genetics.IIndividualForestry;

public interface IEntityButterfly extends IAnimals {

	void changeExhaustion(int change);

	int getExhaustion();

	IButterfly getButterfly();

	/**
	 * @return The entity as an EntityCreature to save casting.
	 */
	EntityCreature getEntity();

	@Nullable
	IIndividualForestry getPollen();

	void setPollen(@Nullable IIndividualForestry pollen);

	boolean canMateWith(IEntityButterfly butterfly);

	boolean canMate();
}
