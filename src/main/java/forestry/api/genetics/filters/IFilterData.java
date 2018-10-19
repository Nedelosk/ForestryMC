package forestry.api.genetics.filters;

import forestry.api.genetics.IIndividualForestry;
import forestry.api.genetics.IIndividualRootForestry;
import forestry.api.genetics.ISpeciesType;

public interface IFilterData {

	/**
	 * If the root is present, returns the root,
	 * otherwise throws {@code NoSuchElementException}.
	 */
	IIndividualRootForestry getRoot();

	/**
	 * If the individual is present, returns the individual,
	 * otherwise throws {@code NoSuchElementException}.
	 */
	IIndividualForestry getIndividual();

	/**
	 * If the type is present, returns the type,
	 * otherwise throws {@code NoSuchElementException}.
	 */
	ISpeciesType getType();

	/**
	 * @return True if this data contains a root, individual and type.
	 */
	boolean isPresent();
}
