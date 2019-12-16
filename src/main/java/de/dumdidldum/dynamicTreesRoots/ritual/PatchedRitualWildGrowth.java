package de.dumdidldum.dynamicTreesRoots.ritual;

import de.dumdidldum.dynamicTreesRoots.DynamicTreesRoots;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.ritual.RitualWildGrowth;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PatchedRitualWildGrowth extends RitualWildGrowth {

	public PatchedRitualWildGrowth(String name, int duration, boolean disabled) {
		super(name, duration, disabled);
	}
	
	@Override
	public EntityRitualBase doEffect(World world, BlockPos pos) {
		System.out.println("Attempting spawning stuff");
		return this.spawnEntity(world, pos, PatchedEntityRitualWildGrowth.class);
	}
}
