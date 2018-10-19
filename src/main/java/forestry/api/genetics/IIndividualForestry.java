/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import genetics.api.individual.IIndividual;

import forestry.api.core.INbtWritable;

/**
 * An actual individual with genetic information.
 * <p>
 * Only the default implementation is supported.
 */
public interface IIndividualForestry extends INbtWritable, IIndividual {

	/**
	 * @return The uid of the active species of this individual.
	 */
	String getIdent();

	/**
	 * @return The display name of the active species of this individual.
	 */
	String getDisplayName();

	/**
	 * @return true if the active species of this individual has a effect.
	 */
	boolean hasEffect();

	/**
	 * @return true if the active species of this individual is secret.
	 */
	boolean isSecret();

}
