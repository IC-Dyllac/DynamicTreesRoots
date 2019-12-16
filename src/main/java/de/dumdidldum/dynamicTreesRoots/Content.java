package de.dumdidldum.dynamicTreesRoots;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import de.dumdidldum.dynamicTreesRoots.ritual.PatchedEntityRitualWildGrowth;
import de.dumdidldum.dynamicTreesRoots.trees.TreeWildwood;
import de.dumdidldum.dynamicTreesRoots.util.CustomSaplingReplacer;
import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesRoots.MODID)
@GameRegistry.ObjectHolder(DynamicTreesRoots.MODID)
public class Content {
	public static ILeavesProperties wildwoodLeavesProperties;
	public static TreeFamily wildwoodTree;

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		// not needed
	}
	
	@SubscribeEvent
	public static void init(@Nonnull RegisterContentEvent event) {		  
		LibRegistry.setActiveMod(DynamicTreesRoots.MODID, DynamicTreesRoots.CONTAINER);
	    LibRegistry.registerEntity(PatchedEntityRitualWildGrowth.class);
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		wildwoodLeavesProperties = new LeavesProperties(
				ModBlocks.wildwood_leaves.getDefaultState(), //.withProperty(BlockLeavesRustic.VARIANT, BlockPlanksRustic.EnumType.OLIVE),
				new ItemStack(Item.getItemFromBlock(ModBlocks.wildwood_leaves), 1));// BlockPlanksRustic.EnumType.OLIVE.getMetadata()));
		
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesRoots.MODID, 0, wildwoodLeavesProperties);
		
		wildwoodTree = new TreeWildwood();
		
		wildwoodTree.registerSpecies(Species.REGISTRY);
		
		IForgeRegistry<Block> registry = event.getRegistry();

		ArrayList<Block> treeBlocks = new ArrayList<>();
		wildwoodTree.getRegisterableBlocks(treeBlocks);
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesRoots.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		ArrayList<Item> treeItems = new ArrayList<>();
		wildwoodTree.getRegisterableItems(treeItems);
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}
	
	public static void preInit() {		
		
		if (ModConfigs.replaceVanillaSapling) {
			MinecraftForge.EVENT_BUS.register(new CustomSaplingReplacer());
		}
	}
	
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    	
        ModelHelper.regModel(wildwoodTree.getDynamicBranch());
        ModelHelper.regModel(wildwoodTree.getCommonSpecies().getSeed());
        ModelHelper.regModel(wildwoodTree);
            
        ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesRoots.MODID, "wildwood")).getSeed()); 
        LeavesPaging.getLeavesMapForModId(DynamicTreesRoots.MODID).forEach((key,leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
   }
	
	public static Item getWildwoodSeed() {
		return wildwoodTree.getCommonSpecies().getSeed();
	}
	
	public static Item getAppleSeed() {
		return TreeRegistry.findSpecies(new ResourceLocation("dynamictrees", "apple")).getSeed();
	}
	
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ItemStack wildwoodSeeds = wildwoodTree.getCommonSpecies().getSeedStack(1);
		
		ItemStack transformationPotion = new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex());
		
		ModRecipes.createDirtBucketExchangeRecipes(new ItemStack(ModBlocks.wildwood_sapling, 1), wildwoodSeeds, true);
		
		BrewingRecipeRegistry.addRecipe(transformationPotion, wildwoodSeeds, ModItems.dendroPotion.setTargetTree(transformationPotion.copy(), wildwoodTree));
	}
	
	public static boolean replaceWorldGen() {
		return WorldGenRegistry.isWorldGenEnabled();
	}
}
