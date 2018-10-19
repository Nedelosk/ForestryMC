package forestry.core.genetics_new.mutations;

import genetics.api.alleles.IAllele;
import genetics.api.individual.IGeneticDefinition;
import genetics.api.mutation.IMutation;
import genetics.api.mutation.IMutationRegistry;

import forestry.api.genetics.alleles.IAlleleSpeciesForestry;

public class MutationCollector<D extends IGeneticDefinition, S extends IAlleleSpeciesForestry, M extends Mutation> {

	private final MutationFactory<S, M> factory;
	private final IMutationRegistry<M> registry;

	public MutationCollector(IMutationRegistry<M> registry, MutationFactory<S, M> factory) {
		this.registry = registry;
		this.factory = factory;
	}

	@SuppressWarnings("unchecked")
	public M register(D firstDefinition, D secondDefinition, IAllele[] result, int chance) {
		M mutation = factory.createMutation((S) firstDefinition.getSpecies(), (S) secondDefinition.getSpecies(), result, chance);
		registry.registerMutation(mutation);
		return mutation;
	}

	@FunctionalInterface
	public interface MutationFactory<S extends IAlleleSpeciesForestry, M extends IMutation> {
		M createMutation(S firstAllele, S secondParent, IAllele[] result, int chance);
	}
}
