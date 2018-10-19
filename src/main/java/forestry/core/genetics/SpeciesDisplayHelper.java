package forestry.core.genetics;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.minecraft.item.ItemStack;

import forestry.api.genetics.IIndividualForestry;
import forestry.api.genetics.IIndividualRootForestry;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAlleleSpeciesForestry;
import forestry.api.genetics.alleles.ISpeciesDisplayHelper;

public class SpeciesDisplayHelper implements ISpeciesDisplayHelper {
	private final Table<ISpeciesType, String, ItemStack> iconStacks = HashBasedTable.create();
	private final IIndividualRootForestry root;

	public SpeciesDisplayHelper(IIndividualRootForestry root) {
		this.root = root;
		ISpeciesType type = root.getIconType();
		for (IIndividualForestry individual : root.getIndividualTemplates()) {
			ItemStack itemStack = root.getMemberStack(individual, type);
			iconStacks.put(type, individual.getGenome().getPrimary().getUID(), itemStack);
		}
	}

	@Override
	public ItemStack getDisplayStack(IAlleleSpeciesForestry species, ISpeciesType type) {
		ItemStack stack = iconStacks.get(type, species.getUID());
		if(stack == null){
			stack = root.getMemberStack(species, type);
			iconStacks.put(type, species.getUID(), stack);
		}
		return stack;
	}

	@Override
	public ItemStack getDisplayStack(IAlleleSpeciesForestry species) {
		return getDisplayStack(species, root.getIconType());
	}
}
