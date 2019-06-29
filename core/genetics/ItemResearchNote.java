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
package forestry.core.genetics;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;


import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.api.distmarker.OnlyIn;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesRoot;
import forestry.core.genetics.mutations.EnumMutateChance;
import forestry.core.items.ItemForestry;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.PlayerUtil;
import forestry.core.utils.Translator;

public class ItemResearchNote extends ItemForestry {

	public enum EnumNoteType {
		NONE, MUTATION, SPECIES;

		public static final EnumNoteType[] VALUES = values();

		@Nullable
		private static IMutation getEncodedMutation(ISpeciesRoot root, CompoundNBT compound) {
			IAllele allele0 = AlleleManager.alleleRegistry.getAllele(compound.getString("AL0"));
			IAllele allele1 = AlleleManager.alleleRegistry.getAllele(compound.getString("AL1"));
			if (allele0 == null || allele1 == null) {
				return null;
			}

			IAllele result = null;
			if (compound.contains("RST")) {
				result = AlleleManager.alleleRegistry.getAllele(compound.getString("RST"));
			}

			IMutation encoded = null;
			for (IMutation mutation : root.getCombinations(allele0)) {
				if (mutation.isPartner(allele1)) {
					if (result == null
						|| mutation.getTemplate()[0].getUID().equals(result.getUID())) {
						encoded = mutation;
						break;
					}
				}
			}

			return encoded;
		}

		public List<String> getTooltip(CompoundNBT compound) {
			List<String> tooltips = new ArrayList<>();

			if (this == NONE) {
				return tooltips;
			}

			if (this == MUTATION) {
				ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(compound.getString("ROT"));
				if (root == null) {
					return tooltips;
				}

				IMutation encoded = getEncodedMutation(root, compound);
				if (encoded == null) {
					return tooltips;
				}

				String species1 = encoded.getAllele0().getAlleleName();
				String species2 = encoded.getAllele1().getAlleleName();
				String mutationChanceKey = EnumMutateChance.rateChance(encoded.getBaseChance()).toString().toLowerCase(Locale.ENGLISH);
				String mutationChance = Translator.translateToLocal("for.researchNote.chance." + mutationChanceKey);
				String speciesResult = encoded.getTemplate()[root.getSpeciesChromosomeType().ordinal()].getAlleleName();

				tooltips.add(Translator.translateToLocal("for.researchNote.discovery.0"));
				tooltips.add(Translator.translateToLocal("for.researchNote.discovery.1").replace("%SPEC1", species1).replace("%SPEC2", species2));
				tooltips.add(Translator.translateToLocalFormatted("for.researchNote.discovery.2", mutationChance));
				tooltips.add(Translator.translateToLocalFormatted("for.researchNote.discovery.3", speciesResult));

				if (!encoded.getSpecialConditions().isEmpty()) {
					for (String line : encoded.getSpecialConditions()) {
						tooltips.add(TextFormatting.GOLD + line);
					}
				}
			} else if (this == SPECIES) {
				IAlleleSpecies allele0 = (IAlleleSpecies) AlleleManager.alleleRegistry.getAllele(compound.getString("AL0"));
				if (allele0 == null) {
					return tooltips;
				}
				ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(compound.getString("ROT"));
				if (root == null) {
					return tooltips;
				}

				tooltips.add("researchNote.discovered.0");
				tooltips.add(Translator.translateToLocalFormatted("for.researchNote.discovered.1", allele0.getAlleleName(), allele0.getBinomial()));
			}

			return tooltips;
		}

		public boolean registerResults(World world, PlayerEntity player, CompoundNBT compound) {
			if (this == NONE) {
				return false;
			}

			if (this == MUTATION) {
				ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(compound.getString("ROT"));
				if (root == null) {
					return false;
				}

				IMutation encoded = getEncodedMutation(root, compound);
				if (encoded == null) {
					return false;
				}

				IBreedingTracker tracker = encoded.getRoot().getBreedingTracker(world, player.getGameProfile());
				if (tracker.isResearched(encoded)) {
					player.sendMessage(new TranslationTextComponent("for.chat.cannotmemorizeagain"));
					return false;
				}

				IAlleleSpecies species0 = encoded.getAllele0();
				IAlleleSpecies species1 = encoded.getAllele1();
				IAlleleSpecies speciesResult = (IAlleleSpecies) encoded.getTemplate()[root.getSpeciesChromosomeType().ordinal()];

				tracker.registerSpecies(species0);
				tracker.registerSpecies(species1);
				tracker.registerSpecies(speciesResult);

				tracker.researchMutation(encoded);
				player.sendMessage(new TranslationTextComponent("for.chat.memorizednote"));

				player.sendMessage(new TranslationTextComponent("for.chat.memorizednote2",
					TextFormatting.GRAY + species0.getAlleleName(),
					TextFormatting.GRAY + species1.getAlleleName(),
					TextFormatting.GREEN + speciesResult.getAlleleName()));

				return true;
			}

			return false;

		}

		public static ResearchNote createMutationNote(GameProfile researcher, IMutation mutation) {
			CompoundNBT compound = new CompoundNBT();
			compound.setString("ROT", mutation.getRoot().getUID());
			compound.setString("AL0", mutation.getAllele0().getUID());
			compound.setString("AL1", mutation.getAllele1().getUID());
			compound.setString("RST", mutation.getTemplate()[0].getUID());
			return new ResearchNote(researcher, MUTATION, compound);
		}

		public static ItemStack createMutationNoteStack(Item item, GameProfile researcher, IMutation mutation) {
			ResearchNote note = createMutationNote(researcher, mutation);
			CompoundNBT compound = new CompoundNBT();
			note.writeToNBT(compound);
			ItemStack created = new ItemStack(item);
			created.setTagCompound(compound);
			return created;
		}

		public static ResearchNote createSpeciesNote(GameProfile researcher, IAlleleSpecies species) {
			CompoundNBT compound = new CompoundNBT();
			compound.setString("ROT", species.getRoot().getUID());
			compound.setString("AL0", species.getUID());
			return new ResearchNote(researcher, SPECIES, compound);
		}

		public static ItemStack createSpeciesNoteStack(Item item, GameProfile researcher, IAlleleSpecies species) {
			ResearchNote note = createSpeciesNote(researcher, species);
			CompoundNBT compound = new CompoundNBT();
			note.writeToNBT(compound);
			ItemStack created = new ItemStack(item);
			created.setTagCompound(compound);
			return created;
		}

	}

	public static class ResearchNote {
		@Nullable
		private final GameProfile researcher;
		private final EnumNoteType type;
		private final CompoundNBT inner;

		public ResearchNote(GameProfile researcher, EnumNoteType type, CompoundNBT inner) {
			this.researcher = researcher;
			this.type = type;
			this.inner = inner;
		}

		public ResearchNote(@Nullable CompoundNBT compound) {
			if (compound != null) {
				if (compound.hasKey("res")) {
					this.researcher = PlayerUtil.readGameProfileFromNBT(compound.getCompoundNBT("res"));
				} else {
					this.researcher = null;
				}
				this.type = EnumNoteType.VALUES[compound.getByte("TYP")];
				this.inner = compound.getCompoundNBT("INN");
			} else {
				this.type = EnumNoteType.NONE;
				this.researcher = null;
				this.inner = new CompoundNBT();
			}
		}

		public CompoundNBT writeToNBT(CompoundNBT compound) {
			if (this.researcher != null) {
				CompoundNBT nbt = new CompoundNBT();
				PlayerUtil.writeGameProfile(nbt, researcher);
				compound.setTag("res", nbt);
			}
			compound.setByte("TYP", (byte) type.ordinal());
			compound.setTag("INN", inner);
			return compound;
		}

		public void addTooltip(List<String> list) {
			List<String> tooltips = type.getTooltip(inner);
			if (tooltips.size() <= 0) {
				list.add(TextFormatting.ITALIC + TextFormatting.RED.toString() + Translator.translateToLocal("for.researchNote.error.0"));
				list.add(Translator.translateToLocal("for.researchNote.error.1"));
				return;
			}

			list.addAll(tooltips);
		}

		public boolean registerResults(World world, PlayerEntity player) {
			return type.registerResults(world, player, inner);
		}
	}

	public ItemResearchNote() {
		setCreativeTab(null);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		ResearchNote note = new ResearchNote(itemstack.getTag());
		String researcherName;
		if (note.researcher == null) {
			researcherName = "Sengir";
		} else {
			researcherName = note.researcher.getName();
		}
		return Translator.translateToLocalFormatted(getTranslationKey(itemstack) + ".name", researcherName);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack itemstack, @Nullable World world, List<String> list, ITooltipFlag flag) {
		super.addInformation(itemstack, world, list, flag);
		ResearchNote note = new ResearchNote(itemstack.getTag());
		note.addTooltip(list);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack heldItem = playerIn.getHeldItem(handIn);
		if (worldIn.isRemote) {
			return ActionResult.newResult(ActionResultType.PASS, heldItem);
		}

		ResearchNote note = new ResearchNote(heldItem.getTag());
		if (note.registerResults(worldIn, playerIn)) {
			playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
			// Notify player that his inventory has changed.
			NetworkUtil.inventoryChangeNotify(playerIn);
		}

		return ActionResult.newResult(ActionResultType.SUCCESS, heldItem);
	}
}
