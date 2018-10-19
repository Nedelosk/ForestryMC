package forestry.api.genetics;

import genetics.api.root.components.ComponentKey;

import forestry.api.genetics.gaget.IGadgetManager;
import forestry.api.genetics.gaget.IGadgetRegistry;
import forestry.api.genetics.research.IResearchContainer;
import forestry.api.genetics.research.IResearchRegistry;

public class ForestryComponentKeys {

	public static final ComponentKey<IResearchContainer, IResearchRegistry> RESEARCH = ComponentKey.create("research", IResearchContainer.class);
	public static final ComponentKey<IGadgetManager, IGadgetRegistry> GATGETS = ComponentKey.create("gadgets", IGadgetManager.class);

	private ForestryComponentKeys() {
	}
}
