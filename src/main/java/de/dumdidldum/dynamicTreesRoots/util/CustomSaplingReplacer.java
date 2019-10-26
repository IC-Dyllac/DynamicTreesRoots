package de.dumdidldum.dynamicTreesRoots.util;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;

import de.dumdidldum.dynamicTreesRoots.DynamicTreesRoots;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomSaplingReplacer {
	@SubscribeEvent
	public void onPlaceSaplingEvent(PlaceEvent event) {
		IBlockState state = event.getPlacedBlock();
		
		if (state.getBlock() == ModBlocks.wildwood_sapling) {
			Species species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesRoots.MODID, "wildwood"));			
			if (species != null) {
				event.getWorld().setBlockToAir(event.getPos());
				if(!species.plantSapling(event.getWorld(), event.getPos())) {
					double x = event.getPos().getX() + 0.5;
					double y = event.getPos().getY() + 0.5;
					double z = event.getPos().getZ() + 0.5;
					EntityItem itemEntity = new EntityItem(event.getWorld(), x, y, z, species.getSeedStack(1));
					event.getWorld().spawnEntity(itemEntity);
				}
			}
		}
	}
}
