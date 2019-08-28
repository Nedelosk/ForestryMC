/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.items;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.ToolType;

import forestry.api.core.ItemGroups;
import forestry.core.circuits.ItemCircuitBoard;
import forestry.core.genetics.ItemResearchNote;
import forestry.core.utils.OreDictUtil;
import forestry.modules.ForestryModuleUids;
import forestry.modules.ModuleHelper;

//import net.minecraftforge.oredict.OreDictionary;

public class ItemRegistryCore extends ItemRegistry {
	/* Fertilizer */
	public final ItemForestry compost;
	public final ItemFertilizer fertilizerCompound;

	/* Gems */
	public final ItemForestry apatite;

	/* Research */
	public final ItemResearchNote researchNote;

	/* Alyzer */
	public final ItemAlyzer portableAlyzer;

	/* Ingots */
	public final ItemStack ingotCopper;
	public final ItemStack ingotTin;
	public final ItemStack ingotBronze;

	/* Tools */
	public final ItemWrench wrench;
	public final ItemPipette pipette;

	/* Packaged Tools */
	public final ItemForestry carton;
	public final ItemForestry brokenBronzePickaxe;
	public final ItemForestry brokenBronzeShovel;
	public final ItemForestryTool bronzePickaxe;
	public final ItemForestryTool bronzeShovel;
	public final ItemAssemblyKit kitShovel;
	public final ItemAssemblyKit kitPickaxe;

	/* Machine Parts */
	public final ItemForestry sturdyCasing;
	public final ItemForestry hardenedCasing;
	public final ItemForestry impregnatedCasing;
	public final ItemForestry flexibleCasing;
	public final ItemStack gearBronze;
	public final ItemStack gearCopper;
	public final ItemStack gearTin;

	/* Soldering */
	public final ItemSolderingIron solderingIron;
	public final ItemCircuitBoard circuitboards;
	public final Map<EnumElectronTube, ItemElectronTube> electronTubes = new EnumMap<>(EnumElectronTube.class);

	/* Armor */
	public final ItemArmorNaturalist spectacles;

	/* Peat */
	public final ItemForestry peat;
	public final ItemForestry ash;
	public final ItemForestry bituminousPeat;

	/* Moistener */
	public final ItemForestry mouldyWheat;
	public final ItemForestry decayingWheat;
	public final ItemForestry mulch;

	/* Rainmaker */
	public final ItemForestry iodineCharge;
	public final ItemForestry phosphor;

	/* Misc */
	public final Map<EnumCraftingMaterial, ItemCraftingMaterial> craftingMaterials = new EnumMap<>(EnumCraftingMaterial.class);
	public final ItemForestry stickImpregnated;
	public final ItemForestry woodPulp;
	public final ItemForestry beeswax;
	public final ItemForestry refractoryWax;
	public final Map<ItemFruit.EnumFruit, ItemFruit> fruits = new EnumMap<>(ItemFruit.EnumFruit.class);

	public ItemRegistryCore() {
		compost = registerItem(new ItemForestry(), "fertilizer_bio");
		fertilizerCompound = registerItem(new ItemFertilizer(), "fertilizer_compound");

		apatite = registerItem(new ItemForestry(), "apatite");
//		OreDictionary.registerOre(OreDictUtil.GEM_APATITE, apatite);

		researchNote = registerItem(new ItemResearchNote(), "research_note");

		portableAlyzer = registerItem(new ItemAlyzer(), "portable_alyzer");

		ingotCopper = createItemForOreName(OreDictUtil.INGOT_COPPER, "ingot_copper");
		ingotTin = createItemForOreName(OreDictUtil.INGOT_TIN, "ingot_tin");
		ingotBronze = createItemForOreName(OreDictUtil.INGOT_BRONZE, "ingot_bronze");

		wrench = registerItem(new ItemWrench(), "wrench");
		pipette = registerItem(new ItemPipette(), "pipette");

		sturdyCasing = registerItem(new ItemForestry(), "sturdy_machine");
		hardenedCasing = registerItem(new ItemForestry(), "hardened_machine");
		impregnatedCasing = registerItem(new ItemForestry(), "impregnated_casing");
		flexibleCasing = registerItem(new ItemForestry(), "flexible_casing");

		for(EnumCraftingMaterial type: EnumCraftingMaterial.VALUES) {
			ItemCraftingMaterial item = new ItemCraftingMaterial(type);
			registerItem(item, type.getName());
			craftingMaterials.put(type, item);
		}

		spectacles = registerItem(new ItemArmorNaturalist(), "naturalist_helmet");

		peat = new ItemForestry() {
			@Override
			public int getBurnTime(ItemStack itemStack) {
				return 2000;
			}
		};
		registerItem(peat, "peat");
//		OreDictionary.registerOre(OreDictUtil.BRICK_PEAT, peat);

		ash = registerItem(new ItemForestry(), "ash");
//		OreDictionary.registerOre(OreDictUtil.DUST_ASH, ash);

		bituminousPeat = new ItemForestry() {
			@Override
			public int getBurnTime(ItemStack itemStack) {
				return 4200;
			}
		};
		registerItem(bituminousPeat, "bituminous_peat");

		gearBronze = createItemForOreName(OreDictUtil.GEAR_BRONZE, "gear_bronze");
		gearCopper = createItemForOreName(OreDictUtil.GEAR_COPPER, "gear_copper");
		gearTin = createItemForOreName(OreDictUtil.GEAR_TIN, "gear_tin");

		circuitboards = registerItem(new ItemCircuitBoard(), "chipsets");

		solderingIron = new ItemSolderingIron((new Item.Properties()).maxDamage(5));
//		solderingIron.setFull3D(); TODO
		registerItem(solderingIron, "soldering_iron");

		for (EnumElectronTube def : EnumElectronTube.VALUES) {
			ItemElectronTube tube = new ItemElectronTube(def);
			registerItem(tube, "electron_tube_" + def.getUid());
			electronTubes.put(def, tube);
		}	//TODO tags?

		// / CARTONS
		carton = registerItem(new ItemForestry(), "carton");

		// / CRAFTING CARPENTER
		stickImpregnated = registerItem(new ItemForestry(), "oak_stick");
		woodPulp = registerItem(new ItemForestry(), "wood_pulp");
//		OreDictionary.registerOre(OreDictUtil.PULP_WOOD, woodPulp);

		// / RECLAMATION
		brokenBronzePickaxe = registerItem(new ItemForestry(), "broken_bronze_pickaxe");
		brokenBronzeShovel = registerItem(new ItemForestry(), "broken_bronze_shovel");

		// / TOOLS
		bronzePickaxe = new ItemForestryTool(new ItemStack(brokenBronzePickaxe), (new Item.Properties()).addToolType(ToolType.PICKAXE, 3));
		registerItem(bronzePickaxe, "bronze_pickaxe");

		bronzeShovel = new ItemForestryTool(new ItemStack(brokenBronzeShovel), (new Item.Properties()).addToolType(ToolType.SHOVEL, 3));
		registerItem(bronzeShovel, "bronze_shovel");

		// / ASSEMBLY KITS
		kitShovel = new ItemAssemblyKit(new ItemStack(bronzeShovel));
		registerItem(kitShovel, "kit_shovel");

		kitPickaxe = new ItemAssemblyKit(new ItemStack(bronzePickaxe));
		registerItem(kitPickaxe, "kit_pickaxe");

		// / MOISTENER RESOURCES
		mouldyWheat = registerItem(new ItemForestry(), "mouldy_wheat");
		decayingWheat = registerItem(new ItemForestry(), "decaying_wheat");
		mulch = registerItem(new ItemForestry(), "mulch");

		// / RAINMAKER SUBSTRATES
		iodineCharge = registerItem(new ItemForestry(), "iodine_capsule");

		phosphor = registerItem(new ItemForestry(), "phosphor");

		// / BEE RESOURCES
		if (ModuleHelper.isEnabled(ForestryModuleUids.APICULTURE)) {
			beeswax = registerItem(new ItemForestry(new Item.Properties(), ItemGroups.tabApiculture), "beeswax");
		} else {
			beeswax = registerItem(new ItemForestry(), "beeswax");
		}
//		OreDictionary.registerOre(OreDictUtil.ITEM_BEESWAX, beeswax);

		refractoryWax = registerItem(new ItemForestry(), "refractory_wax");

		// FRUITS
		for (ItemFruit.EnumFruit def : ItemFruit.EnumFruit.values()) {
			ItemFruit fruit = new ItemFruit(def);
			registerItem(fruit, "fruit_" + def.getName());
			fruits.put(def, fruit);
		}	//TODO tags
	}

	public ItemStack getCraftingMaterial(EnumCraftingMaterial type, int amount) {
		return new ItemStack(craftingMaterials.get(type), amount);
	}


	public ItemStack getElectronTube(EnumElectronTube type, int amount) {
		return new ItemStack(electronTubes.get(type), amount);
	}

	public ItemStack getFruit(ItemFruit.EnumFruit type, int amount) {
		return new ItemStack(fruits.get(type), amount);
	}

}
