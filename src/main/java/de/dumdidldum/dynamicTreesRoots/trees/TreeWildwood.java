package de.dumdidldum.dynamicTreesRoots.trees;

import java.util.List;
import java.util.function.BiFunction;

import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.Species.LogsAndSticks;

import de.dumdidldum.dynamicTreesRoots.Config;
import de.dumdidldum.dynamicTreesRoots.Content;
import de.dumdidldum.dynamicTreesRoots.DynamicTreesRoots;
import de.dumdidldum.dynamicTreesRoots.util.CustomDropCreator;
import epicsquid.mysticallib.block.BlockLeavesBase;
import epicsquid.mysticallib.block.BlockLogBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeWildwood extends TreeFamily {

	public class SpeciesWildWood extends Species {
		
		SpeciesWildWood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, Content.wildwoodLeavesProperties);
			
			setBasicGrowingParameters(0.5f, 17.0f, 4, 4, 0.9f);
						
			envFactor(Type.COLD, 0.5f);
			envFactor(Type.DRY, 0.5f);
			envFactor(Type.HOT, 0.75f);
			envFactor(Type.FOREST, 1.05f);
			
			generateSeed();

			if(Config.wildwoodDropSeeds) {				
				setupStandardSeedDropping();
			}
			
			addDropCreator(new CustomDropCreator(ModItems.wildroot, 20));
			addDropCreator(new CustomDropCreator(epicsquid.mysticalworld.init.ModItems.silkworm_egg, 30));
			
			//Add species features
			addGenFeature(new FeatureGenClearVolume(6));//Clear a spot for the thick tree trunk
			addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
			addGenFeature(new FeatureGenMound(5));//Establish mounds
			addGenFeature(new FeatureGenRoots(12).setScaler(getRootScaler()));//Finally Generate Roots
		}
		
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 12 ? (trunkRadius / 20f) : 0, 0, 1);
				return (int) (inRadius * scale);
			};
		}
		
		@Override
		public List<ItemStack> getLogsDrops(World world, BlockPos breakPos, List<ItemStack> dropList, float volume) {
			volume *= Config.wildwoodLogDropMultiplier; //all for the balance!
			return super.getLogsDrops(world, breakPos, dropList, volume);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.FOREST);
		}
		
		@Override
		public boolean isThick() {
			return true;
		}
		
	}
	
	
	BlockSurfaceRoot surfaceRootBlock;
	
	public TreeWildwood() {
		super(new ResourceLocation(DynamicTreesRoots.MODID, "wildwood"));
		
		IBlockState primLog = ModBlocks.wildwood_log.getDefaultState();//.withProperty(BlockLogRustic.VARIANT, BlockPlanksRustic.EnumType.IRONWOOD);
		setPrimitiveLog(primLog, new ItemStack(((BlockLogBase) ModBlocks.wildwood_log).getItemBlock(), 1));
		
		Content.wildwoodLeavesProperties.setTree(this);
		
		surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");
		
		this.addConnectableVanillaLeaves((state) -> {
			// TODO find better check if leaves are really roots leaves
			return state.getBlock() instanceof BlockLeavesBase;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesWildWood(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(surfaceRootBlock);
		return super.getRegisterableBlocks(blockList);
	}
	
	@Override
	public boolean isThick() {
		return true;
	}
	
	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return surfaceRootBlock;
	}
}
