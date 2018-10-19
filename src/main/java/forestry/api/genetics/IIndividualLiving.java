/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import net.minecraft.world.World;

public interface IIndividualLiving extends IIndividualForestry {

	/**
	 * @return Current health of the individual.
	 */
	int getHealth();

	/**
	 * Set the current health of the individual.
	 */
	void setHealth(int health);

	/**
	 * @return Maximum health of the individual.
	 */
	int getMaxHealth();

	/**
	 * Age the individual.
	 */
	void age(World world, float ageModifier);

	/**
	 * Mate with the given individual.
	 *
	 * @param individual the {@link IIndividualForestry} to mate this one with.
	 */
	void mate(IIndividualForestry individual);

	/**
	 * @return true if the individual is among the living.
	 */
	boolean isAlive();

}
