package forestry.sorting;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;

import forestry.api.genetics.IIndividualForestry;
import forestry.api.genetics.IIndividualRootForestry;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.filters.IFilterData;

public class FilterData implements IFilterData {
	@Nullable
	private IIndividualRootForestry root;
	@Nullable
	private IIndividualForestry individual;
	@Nullable
	private ISpeciesType type;

	public FilterData(@Nullable IIndividualRootForestry root, @Nullable IIndividualForestry individual, @Nullable ISpeciesType type) {
		this.root = root;
		this.individual = individual;
		this.type = type;
	}

	@Override
	public IIndividualRootForestry getRoot() {
		if (root == null) {
			throw new NoSuchElementException("No root present");
		}
		return root;
	}

	@Override
	public IIndividualForestry getIndividual() {
		if (individual == null) {
			throw new NoSuchElementException("No individual present");
		}
		return individual;
	}

	@Override
	public ISpeciesType getType() {
		if (type == null) {
			throw new NoSuchElementException("No type present");
		}
		return type;
	}

	@Override
	public boolean isPresent() {
		return root != null && individual != null && type != null;
	}
}
