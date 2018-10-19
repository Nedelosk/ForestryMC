/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import javax.annotation.Nullable;
import java.util.Locale;

import net.minecraft.util.math.Vec3i;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IChromosomeType;
import genetics.api.root.IIndividualRoot;

import forestry.api.genetics.EnumTolerance;
import forestry.api.genetics.flowers.IFlowerProvider;

/**
 * Enum representing the order of chromosomes in a bee's genome and what they control.
 *
 * @author SirSengir
 */
public enum EnumBeeChromosome implements IChromosomeType {
	/**
	 * Species of the bee. Alleles here must implement {@link IAlleleBeeSpecies}.
	 */
	SPECIES(IAlleleBeeSpecies.class),
	/**
	 * (Production) Speed of the bee.
	 */
	SPEED(Float.class),
	/**
	 * Lifespan of the bee.
	 */
	LIFESPAN(Integer.class),
	/**
	 * Fertility of the bee. Determines number of offspring.
	 */
	FERTILITY(Integer.class),
	/**
	 * Temperature difference to its native supported one the bee can tolerate.
	 */
	TEMPERATURE_TOLERANCE(EnumTolerance.class),
	/**
	 * If true, a naturally diurnal bee can work during the night. If true, a naturally nocturnal bee can work during the day.
	 */
	NEVER_SLEEPS(Boolean.class),
	/**
	 * Humidity difference to its native supported one the bee can tolerate.
	 */
	HUMIDITY_TOLERANCE(EnumTolerance.class),
	/**
	 * If true the bee can work during rain.
	 */
	TOLERATES_RAIN(Boolean.class),
	/**
	 * If true, the bee can work without a clear view of the sky.
	 */
	CAVE_DWELLING(Boolean.class),
	/**
	 * Contains the supported flower provider.
	 */
	FLOWER_PROVIDER(IFlowerProvider.class),
	/**
	 * Determines pollination speed.
	 */
	FLOWERING(Integer.class),
	/**
	 * Determines the size of the bee's territory.
	 */
	TERRITORY(Vec3i.class),
	/**
	 * Determines the bee's effect.
	 */
	EFFECT(IAlleleBeeEffect.class);

	@Nullable
	private final Class valueClass;

	EnumBeeChromosome(Class valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public String getName() {
		return this.toString().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public Class getValueClass() {
		return valueClass;
	}

	@Override
	public int getIndex() {
		return ordinal();
	}

	@Override
	public IIndividualRoot getRoot() {
		return GeneticsAPI.apiInstance.getRoot("beeRoot").get();
	}


}
