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

import java.util.Map;
import java.util.function.Function;

import genetics.api.individual.IIndividual;
import genetics.api.individual.IKaryotype;
import genetics.api.root.IIndividualRoot;
import genetics.api.root.IndividualRoot;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.IRootComponent;

import forestry.api.genetics.IIndividualRootForestry;

public abstract class IndividualRootForestry<I extends IIndividual> extends IndividualRoot<I> implements IIndividualRootForestry<I> {

	public IndividualRootForestry(IKaryotype karyotype, Function<IIndividualRoot<I>, Map<ComponentKey, IRootComponent>> components) {
		super(karyotype, components);
	}
}
