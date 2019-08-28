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
package forestry.arboriculture.proxy;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.multipart.Multipart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.FoliageColors;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import forestry.api.arboriculture.IWoodItemMeshDefinition;
import forestry.api.arboriculture.IWoodStateMapper;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.WoodBlockKind;
import forestry.arboriculture.IWoodTyped;
import forestry.arboriculture.ModuleArboriculture;
import forestry.arboriculture.blocks.BlockDecorativeLeaves;
import forestry.arboriculture.blocks.BlockDefaultLeaves;
import forestry.arboriculture.blocks.BlockDefaultLeavesFruit;
import forestry.arboriculture.models.ModelDecorativeLeaves;
import forestry.arboriculture.models.ModelDefaultLeaves;
import forestry.arboriculture.models.ModelDefaultLeavesFruit;
import forestry.arboriculture.models.ModelLeaves;
import forestry.arboriculture.models.MultipartModel;
import forestry.arboriculture.models.WoodModelLoader;
import forestry.arboriculture.models.WoodTextureManager;
import forestry.core.models.BlockModelEntry;
import forestry.core.models.ModelManager;
import forestry.core.models.SimpleRetexturedModel;
import forestry.core.models.WoodModelEntry;

@OnlyIn(Dist.CLIENT)
public class ProxyArboricultureClient extends ProxyArboriculture {
	private static final Set<WoodModelEntry> woodModelEntrys = new HashSet<>();
	private static final Map<IWoodTyped, IWoodStateMapper> stateMappers = Maps.newIdentityHashMap();
	private static final Map<Item, IWoodItemMeshDefinition> shapers = Maps.newHashMap();

	@Override
	public void initializeModels() {
		{
			ModelResourceLocation blockModelLocation = new ModelResourceLocation("forestry:leaves");
			ModelResourceLocation itemModelLocation = new ModelResourceLocation("forestry:leaves", "inventory");
			BlockModelEntry blockModelIndex = new BlockModelEntry(blockModelLocation, itemModelLocation,
				new ModelLeaves(), ModuleArboriculture.getBlocks().leaves);
			ModelManager.getInstance().registerCustomBlockModel(blockModelIndex);
		}

		for (BlockDecorativeLeaves leaves : ModuleArboriculture.getBlocks().leavesDecorative) {
			String resourceName = "forestry:leaves.decorative." + leaves.getBlockNumber();
			ModelResourceLocation blockModelLocation = new ModelResourceLocation(resourceName);
			ModelResourceLocation itemModeLocation = new ModelResourceLocation(resourceName, "inventory");
			BlockModelEntry blockModelIndex = new BlockModelEntry(blockModelLocation, itemModeLocation,
				new ModelDecorativeLeaves(), leaves);
			ModelManager.getInstance().registerCustomBlockModel(blockModelIndex);
		}

		for (BlockDefaultLeaves leaves : ModuleArboriculture.getBlocks().leavesDefault) {
			String resourceName = "forestry:leaves.default." + leaves.getBlockNumber();
			ModelResourceLocation blockModelLocation = new ModelResourceLocation(resourceName);
			ModelResourceLocation itemModeLocation = new ModelResourceLocation(resourceName, "inventory");
			BlockModelEntry blockModelIndex = new BlockModelEntry(blockModelLocation, itemModeLocation,
				new ModelDefaultLeaves(), leaves);
			ModelManager.getInstance().registerCustomBlockModel(blockModelIndex);
		}

		for (BlockDefaultLeavesFruit leaves : ModuleArboriculture.getBlocks().leavesDefaultFruit) {
			String resourceName = "forestry:leaves.default.fruit." + leaves.getBlockNumber();
			ModelResourceLocation blockModelLocation = new ModelResourceLocation(resourceName);
			ModelResourceLocation itemModeLocation = new ModelResourceLocation(resourceName, "inventory");
			BlockModelEntry blockModelIndex = new BlockModelEntry(blockModelLocation, itemModeLocation,
				new ModelDefaultLeavesFruit(), leaves);
			ModelManager.getInstance().registerCustomBlockModel(blockModelIndex);
		}

		ModelLoaderRegistry.registerLoader(WoodModelLoader.INSTANCE);
		//TODO data generators?
//		for (BlockArbSlab slab : ModuleArboriculture.getBlocks().slabsDouble) {
//			registerWoodModel(slab, true);
//		}
//		for (BlockArbSlab slab : ModuleArboriculture.getBlocks().slabsDoubleFireproof) {
//			registerWoodModel(slab, true);
//		}
	}

	public static void registerWoodMeshDefinition(Item item, IWoodItemMeshDefinition definition) {
		ModelManager.getInstance().registerItemModel(item, definition);
		shapers.put(item, definition);
	}

	public static void registerWoodStateMapper(Block block, IWoodStateMapper stateMapper) {
		if (block instanceof IWoodTyped) {
			IWoodTyped woodTyped = (IWoodTyped) block;
			ModelLoader.setCustomStateMapper(block, stateMapper);
			stateMappers.put(woodTyped, stateMapper);
		}
	}

	@SubscribeEvent
	public <T extends Block & IWoodTyped> void onModelBake(ModelBakeEvent event) {
		Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();

		for (WoodModelEntry<T> entry : woodModelEntrys) {
			T woodTyped = entry.woodTyped;
			WoodBlockKind woodKind = woodTyped.getBlockKind();
			IWoodStateMapper woodMapper = stateMappers.get(woodTyped);

			for (BlockState blockState : woodTyped.getBlockState().getValidStates()) {
				IWoodType woodType;
				ItemStack itemStack;
				if (entry.withVariants) {
					int meta = woodTyped.getMetaFromState(blockState);
					woodType = woodTyped.getWoodType(meta);
					itemStack = new ItemStack(woodTyped, 1, meta);
				} else {
					woodType = woodTyped.getWoodType(0);
					itemStack = new ItemStack(woodTyped);
				}
				IWoodItemMeshDefinition definition = shapers.get(itemStack.getItem());
				ImmutableMap<String, String> textures = WoodTextureManager.getTextures(woodType, woodKind);
				if (definition != null) {
					retextureItemModel(registry, textures, woodType, woodKind, itemStack, definition);
				}
				if (woodMapper != null) {
					retexturBlockModel(registry, textures, woodType, woodKind, blockState, woodMapper);
				}
			}
		}
	}

	private void retextureItemModel(Registry<ModelResourceLocation, IBakedModel> registry,
		ImmutableMap<String, String> textures, IWoodType woodType, WoodBlockKind woodKind, ItemStack itemStack,
		IWoodItemMeshDefinition woodDefinition) {
		if (woodKind != WoodBlockKind.DOOR) {
			ResourceLocation defaultModelLocation = woodDefinition.getDefaultModelLocation(itemStack);
			IModel basicItemModel = ModelLoaderRegistry.getModelOrMissing(defaultModelLocation);
			ModelResourceLocation basicItemLocation = woodDefinition.getModelLocation(itemStack);
			IModel retextureModel = woodKind.retextureModel(basicItemModel, woodType, textures);
			registry.putObject(basicItemLocation, new SimpleRetexturedModel(retextureModel));
		}
	}

	private void retexturBlockModel(Map<ModelResourceLocation, IBakedModel> registry,
		ImmutableMap<String, String> textures, IWoodType woodType, WoodBlockKind woodKind, BlockState blockState,
		IWoodStateMapper woodMapper) {
		ModelResourceLocation defaultModelResourceLocation = woodMapper.getDefaultModelResourceLocation(blockState);
		IModel basicModel = ModelLoaderRegistry.getModelOrMissing(defaultModelResourceLocation);
		if (basicModel instanceof MultipartModel) {
			MultipartModel multipartModel = (MultipartModel) basicModel;
			Multipart multipart = multipartModel.getMultipart();
			multipart.setStateContainer(blockState.getBlock().getBlockState());
		}
		ModelResourceLocation basicLocation = woodMapper.getModelLocation(blockState);
		IModel retextureModel = woodKind.retextureModel(basicModel, woodType, textures);
		registry.put(basicLocation, new SimpleRetexturedModel(retextureModel));

	}

	@Override
	public <T extends Block & IWoodTyped> void registerWoodModel(T woodTyped, boolean withVariants) {
		woodModelEntrys.add(new WoodModelEntry<>(woodTyped, withVariants));
	}

	@Override
	public int getFoliageColorBasic() {
		return FoliageColors.getFoliageColorBasic();
	}

	@Override
	public int getFoliageColorBirch() {
		return FoliageColors.getFoliageColorBirch();
	}

	@Override
	public int getFoliageColorPine() {
		return FoliageColors.getFoliageColorPine();
	}
}
