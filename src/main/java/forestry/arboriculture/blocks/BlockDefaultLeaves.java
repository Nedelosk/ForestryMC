package forestry.arboriculture.blocks;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.arboriculture.TreeManager;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.core.proxy.Proxies;

/**
 * Genetic leaves with no tile entity, used for worldgen trees.
 * Similar to decorative leaves, but these will drop saplings and can be used for pollination.
 */
public class BlockDefaultLeaves extends BlockAbstractLeaves {
	private final TreeDefinition definition;

	public BlockDefaultLeaves(TreeDefinition definition) {
		super(Block.Properties.create(Material.LEAVES)
				.hardnessAndResistance(0.2f)
				.sound(SoundType.PLANT)
				.tickRandomly());
		this.definition = definition;
	}

	@Nullable
	public TreeDefinition getTreeDefinition(BlockState blockState) {
		if (blockState.getBlock() == this) {
			return this.definition;
		} else {
			return null;
		}
	}

	@Override
	protected void getLeafDrop(NonNullList<ItemStack> drops, World world, @Nullable GameProfile playerProfile, BlockPos pos, float saplingModifier, int fortune) {
		ITree tree = getTree(world, pos);
		if (tree == null) {
			return;
		}

		// Add saplings
		List<ITree> saplings = tree.getSaplings(world, playerProfile, pos, saplingModifier);
		for (ITree sapling : saplings) {
			if (sapling != null) {
				drops.add(TreeManager.treeRoot.getMemberStack(sapling, EnumGermlingType.SAPLING));
			}
		}
	}

	@Override
	protected ITree getTree(IBlockReader world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		TreeDefinition treeDefinition = getTreeDefinition(blockState);
		if (treeDefinition != null) {
			return treeDefinition.getIndividual();
		} else {
			return null;
		}
	}

	/* RENDERING */
	@Override
	public final boolean isOpaqueCube(BlockState state) {
		if (!Proxies.render.fancyGraphicsEnabled()) {
			return !TreeDefinition.Willow.equals(definition);
		}
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int colorMultiplier(BlockState state, @Nullable IBlockReader worldIn, @Nullable BlockPos pos, int tintIndex) {
		TreeDefinition treeDefinition = getTreeDefinition(state);
		if (treeDefinition == null) {
			treeDefinition = TreeDefinition.Oak;
		}
		ITreeGenome genome = treeDefinition.getGenome();

		ILeafSpriteProvider spriteProvider = genome.getPrimary().getLeafSpriteProvider();
		return spriteProvider.getColor(false);
	}
}
