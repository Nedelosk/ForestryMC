package forestry.apiculture.genetics_new;

import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.individual.IKaryotype;

import forestry.api.apiculture.genetics.EnumBeeChromosome;
import forestry.api.apiculture.genetics.IBeeRoot;
import forestry.apiculture.genetics.alleles.AlleleEffects;
import forestry.core.genetics.alleles.EnumAllele;

public class BeeHelper {

	private BeeHelper() {
	}

	public static IBeeRoot getRoot() {
		return BeePlugin.ROOT.get();
	}

	public static IKaryotype getKaryotype() {
		return getRoot().getKaryotype();
	}

	public static IAlleleTemplateBuilder createTemplate() {
		return getKaryotype().createTemplate();
	}

	public static IAlleleTemplate createDefaultTemplate(IAlleleTemplateBuilder templateBuilder) {
		return templateBuilder.set(EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST)
			.set(EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER)
			.set(EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.NORMAL)
			.set(EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE)
			.set(EnumBeeChromosome.NEVER_SLEEPS, false)
			.set(EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE)
			.set(EnumBeeChromosome.TOLERATES_RAIN, false)
			.set(EnumBeeChromosome.CAVE_DWELLING, false)
			.set(EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA)
			.set(EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWEST)
			.set(EnumBeeChromosome.TERRITORY, EnumAllele.Territory.AVERAGE)
			.set(EnumBeeChromosome.EFFECT, AlleleEffects.effectNone).build();
	}
}
