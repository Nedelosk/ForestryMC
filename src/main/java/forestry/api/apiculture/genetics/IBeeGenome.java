/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import net.minecraft.util.math.Vec3i;

import genetics.api.individual.IGenomeWrapper;

import forestry.api.genetics.EnumTolerance;
import forestry.api.genetics.flowers.IFlowerProvider;

/**
 * Only the default implementation is supported.
 *
 * @author SirSengir
 */
public interface IBeeGenome extends IGenomeWrapper {

	IAlleleBeeSpecies getPrimary();

	IAlleleBeeSpecies getSecondary();

	float getSpeed();

	int getLifespan();

	int getFertility();

	EnumTolerance getToleranceTemp();

	EnumTolerance getToleranceHumid();

	boolean getNeverSleeps();

	boolean getToleratesRain();

	boolean getCaveDwelling();

	IFlowerProvider getFlowerProvider();

	int getFlowering();

	Vec3i getTerritory();

	IAlleleBeeEffect getEffect();

}
