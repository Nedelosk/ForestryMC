package forestry.apiculture;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import forestry.api.apiculture.EnumBeeType;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.apiculture.items.ItemHoneyComb;

public class VillagerApiaristTrades {

	public static class GiveRandomCombsForItems implements VillagerEntity.ITradeList {
		public final ItemHoneyComb honeyComb;
		public final ItemStack itemToBuy;
		@Nullable
		public final VillagerEntity.PriceInfo buyInfo;
		@Nullable
		public final VillagerEntity.PriceInfo priceInfo;

		public GiveRandomCombsForItems(ItemHoneyComb honeyComb, ItemStack stack, @Nullable VillagerEntity.PriceInfo buyInfo, @Nullable VillagerEntity.PriceInfo priceInfo) {
			this.honeyComb = honeyComb;
			this.itemToBuy = stack;
			this.buyInfo = buyInfo;
			this.priceInfo = priceInfo;
		}

		@Override
		public void addMerchantRecipe(IMerchant p_190888_1_, MerchantRecipeList recipeList, Random random) {
			int sellAmount = 1;
			if (this.priceInfo != null) {
				sellAmount = this.priceInfo.getPrice(random);
			}

			int buyAmount = 1;
			if (this.buyInfo != null) {
				buyAmount = this.buyInfo.getPrice(random);
			}

			ItemStack itemToBuy = this.itemToBuy.copy();
			itemToBuy.setCount(buyAmount);
			ItemStack randomComb = honeyComb.getRandomComb(sellAmount, random, false);
			if (!randomComb.isEmpty()) {
				recipeList.add(new MerchantRecipe(itemToBuy, randomComb));
			}
		}
	}

	public static class GiveRandomHiveDroneForItems implements VillagerEntity.ITradeList {
		public final ItemStack buyingItemStack;
		@Nullable
		public final VillagerEntity.PriceInfo buyingPriceInfo;
		public final ItemStack buyingItemStackTwo;
		@Nullable
		public final VillagerEntity.PriceInfo buyingPriceItemTwoInfo;

		public GiveRandomHiveDroneForItems(
			ItemStack buyingItemStack,
			@Nullable VillagerEntity.PriceInfo buyingPriceInfo,
			ItemStack buyingItemStackTwo,
			@Nullable VillagerEntity.PriceInfo buyingPriceItemTwoInfo) {
			this.buyingItemStack = buyingItemStack;
			this.buyingPriceInfo = buyingPriceInfo;
			this.buyingItemStackTwo = buyingItemStackTwo;
			this.buyingPriceItemTwoInfo = buyingPriceItemTwoInfo;
		}

		@Override
		public void addMerchantRecipe(IMerchant p_190888_1_, MerchantRecipeList recipeList, Random random) {
			int buyAmount = 1;
			if (this.buyingPriceInfo != null) {
				buyAmount = this.buyingPriceInfo.getPrice(random);
			}

			int buyTwoAmount = 1;
			if (this.buyingPriceItemTwoInfo != null) {
				buyTwoAmount = this.buyingPriceItemTwoInfo.getPrice(random);
			}
			BeeDefinition[] forestryMundane = new BeeDefinition[]{BeeDefinition.FOREST, BeeDefinition.MEADOWS, BeeDefinition.MODEST, BeeDefinition.WINTRY, BeeDefinition.TROPICAL, BeeDefinition.MARSHY};
			ItemStack randomHiveDrone = forestryMundane[random.nextInt(forestryMundane.length)].getMemberStack(EnumBeeType.DRONE);
			ItemStack buyItemStack = this.buyingItemStack.copy();
			buyItemStack.setCount(buyAmount);
			ItemStack buyItemStackTwo = this.buyingItemStackTwo.copy();
			buyItemStackTwo.setCount(buyTwoAmount);
			recipeList.add(new MerchantRecipe(buyItemStack, buyItemStackTwo, randomHiveDrone));
		}
	}
}
