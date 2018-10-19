package forestry.apiculture.genetics_new;

import java.util.Locale;

import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.classification.IBranchDefinition;
import genetics.api.classification.IClassification;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.genetics.EnumBeeChromosome;
import forestry.apiculture.genetics.alleles.AlleleEffects;
import forestry.core.genetics.alleles.EnumAllele;

public enum BeeBranchDefinition implements IBranchDefinition {
	HONEY("Apis"),
	NOBLE("Probapis"),
	INDUSTRIOUS("Industrapis"),
	HEROIC("Herapis"),
	INFERNAL("Diapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_2)
				.set(EnumBeeChromosome.NEVER_SLEEPS, true)
				.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.NETHER)
				.set(EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.AVERAGE);
		}
	},
	AUSTERE("Modapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1)
				.set(EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.DOWN_1)
				.set(EnumBeeChromosome.NEVER_SLEEPS, true)
				.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.CACTI);
		}
	},
	TROPICAL("Caldapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1)
				.set(EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.UP_1)
				.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.JUNGLE)
				.set(EnumBeeChromosome.EFFECT, AlleleEffects.effectMiasmic);
		}
	},
	END("Finapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.LOW)
				.set(EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER)
				.set(EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGER)
				.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1)
				.set(EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGE)
				.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.END)
				.set(EnumBeeChromosome.NEVER_SLEEPS, true)
				.set(EnumBeeChromosome.EFFECT, AlleleEffects.effectMisanthrope);
		}
	},
	FROZEN("Coagapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1)
				.set(EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1)
				.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.SNOW)
				.set(EnumBeeChromosome.EFFECT, AlleleEffects.effectGlacial);
		}
	},
	VENGEFUL("Punapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGEST)
				.set(EnumBeeChromosome.EFFECT, AlleleEffects.effectRadioactive);
		}
	},
	FESTIVE("Festapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER)
				.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_2)
				.set(EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1)
				.set(EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.NORMAL);
		}
	},
	AGRARIAN("Rustapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER)
				.set(EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER)
				.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.WHEAT)
				.set(EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTER);
		}
	},
	BOGGY("Paludapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.MUSHROOMS)
				.set(EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWER)
				.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
		}
	},
	MONASTIC("Monapis") {
		@Override
		protected void setBranchProperties(IAlleleTemplateBuilder template) {
			template.set(EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER)
				.set(EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONG)
				.set(EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.LOW)
				.set(EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTER)
				.set(EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1)
				.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1)
				.set(EnumBeeChromosome.CAVE_DWELLING, true)
				.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.WHEAT);
		}
	};

	private final IClassification branch;

	BeeBranchDefinition(String scientific) {
		branch = BeeManager.beeFactory.createBranch(this.name().toLowerCase(Locale.ENGLISH), scientific);
	}

	protected void setBranchProperties(IAlleleTemplateBuilder template) {

	}

	@Override
	public final IAlleleTemplate getTemplate() {
		return getTemplateBuilder().build();
	}

	@Override
	public final IAlleleTemplateBuilder getTemplateBuilder() {
		IAlleleTemplateBuilder template = BeeHelper.createTemplate();
		setBranchProperties(template);
		return template;
	}

	@Override
	public final IClassification getBranch() {
		return branch;
	}
}
