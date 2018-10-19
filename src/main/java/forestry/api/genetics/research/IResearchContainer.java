package forestry.api.genetics.research;

import net.minecraft.item.ItemStack;

import genetics.api.root.components.IRootComponent;

public interface IResearchContainer extends IRootComponent {

	void registerSuitability(ItemStack itemstack, float suitability);
}
