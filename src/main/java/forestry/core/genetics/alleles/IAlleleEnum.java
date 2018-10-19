package forestry.core.genetics.alleles;

import java.util.Locale;

import genetics.api.alleles.IAlleleData;

public interface IAlleleEnum<V> extends IAlleleData<V> {
	boolean isDominant();

	V getValue();

	default String getCategory() {
		return getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
	}

	default String getName() {
		return toString().toLowerCase(Locale.ENGLISH);
	}
}
