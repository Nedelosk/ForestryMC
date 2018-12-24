package forestry.core.recipes;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import forestry.api.recipes.IDescriptiveRecipe;

public class DescriptiveRecipe implements IDescriptiveRecipe, IShapedRecipe {

	private IShapedRecipe recipe;
	private NonNullList<NonNullList<ItemStack>> rawIngredients;
	private NonNullList<String> oreDicts;

	public DescriptiveRecipe(IShapedRecipe recipe) {
		this.recipe = recipe;

		int length = recipe.getRecipeHeight() * recipe.getRecipeWidth();
		rawIngredients = NonNullList.withSize(length, NonNullList.create());

		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		for(int i = 0; i < length; i++) {
			rawIngredients.set(i, NonNullList.from(ItemStack.EMPTY, ingredients.get(i).getMatchingStacks()));
		}

		for(Ingredient ing : ingredients) {
			if(ing instanceof OreIngredient) {
				OreIngredient oreing = (OreIngredient) ing;
			}
		}
	}

	@Override
	public int getWidth() {
		return recipe.getRecipeWidth();
	}

	@Override
	public int getHeight() {
		return recipe.getRecipeHeight();
	}

	@Override
	public NonNullList<NonNullList<ItemStack>> getRawIngredients() {
		return rawIngredients;
	}

	@Override
	public NonNullList<String> getOreDicts() {
		return oreDicts;
	}

	@Override
	public ItemStack getOutput() {
		return recipe.getRecipeOutput();
	}

	@Override
	public int getRecipeWidth() {
		return recipe.getRecipeWidth();
	}

	@Override
	public int getRecipeHeight() {
		return recipe.getRecipeHeight();
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return recipe.matches(inv, worldIn);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return recipe.getCraftingResult(inv);
	}

	@Override
	public boolean canFit(int width, int height) {
		return recipe.canFit(width, height);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipe.getRecipeOutput();
	}

	@Override
	public IRecipe setRegistryName(ResourceLocation name) {
		return recipe.setRegistryName(name);
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return recipe.getRegistryName();
	}

	@Override
	public Class<IRecipe> getRegistryType() {
		return recipe.getRegistryType();
	}
}
