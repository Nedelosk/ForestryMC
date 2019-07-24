package forestry.core.genetics.analyzer;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.genetics.IDatabasePlugin;
import forestry.api.genetics.IDatabaseTab;
import forestry.api.genetics.IIndividual;

@OnlyIn(Dist.CLIENT)
public abstract class DatabasePlugin<I extends IIndividual> implements IDatabasePlugin<I> {
	private final IDatabaseTab<I> activeTab;
	private final IDatabaseTab<I> inactiveTab;
	private final IDatabaseTab productsTab;
	private final IDatabaseTab mutationsTab;

	public DatabasePlugin(IDatabaseTab<I> activeTab, IDatabaseTab<I> inactiveTab, IDatabaseTab productsTab, IDatabaseTab mutationsTab) {
		this.activeTab = activeTab;
		this.inactiveTab = inactiveTab;
		this.productsTab = productsTab;
		this.mutationsTab = mutationsTab;
	}

	@Override
	public IDatabaseTab[] getTabs() {
		return new IDatabaseTab[]{
			activeTab,
			inactiveTab,
			productsTab,
			mutationsTab
		};
	}
}
