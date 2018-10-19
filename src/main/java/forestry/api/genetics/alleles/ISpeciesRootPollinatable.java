/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics.alleles;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.ICheckPollinatable;
import forestry.api.genetics.IIndividualForestry;
import forestry.api.genetics.IIndividualRootForestry;
import forestry.api.genetics.IPollinatable;

/**
 * @author Nedelosk
 * @since 5.12.16
 */
public interface ISpeciesRootPollinatable extends IIndividualRootForestry {

	ICheckPollinatable createPollinatable(IIndividualForestry individual);

	@Nullable
	IPollinatable tryConvertToPollinatable(@Nullable GameProfile owner, World world, final BlockPos pos, final IIndividualForestry pollen);

}
