package forestry.api.genetics.research;

import java.util.Map;

import net.minecraft.item.ItemStack;

import genetics.api.root.components.IRootComponentBuilder;

public interface IResearchRegistry extends IRootComponentBuilder<IResearchContainer> {
	Map<ItemStack, Float> getResearchCatalysts();
}
