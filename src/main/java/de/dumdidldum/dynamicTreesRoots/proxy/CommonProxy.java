package de.dumdidldum.dynamicTreesRoots.proxy;

import de.dumdidldum.dynamicTreesRoots.Config;
import de.dumdidldum.dynamicTreesRoots.Content;
import de.dumdidldum.dynamicTreesRoots.DynamicTreesRoots;
import de.dumdidldum.dynamicTreesRoots.ritual.PatchedRitualWildGrowth;
import epicsquid.roots.config.RitualConfig;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualWildGrowth;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
    	Content.preInit();
    	Config.preInit(event);
    }

	public void init(FMLInitializationEvent event) {
		System.out.println("Replacing ritual");
		RitualRegistry.ritualRegistry.remove("ritual_wild_growth");
		RitualRegistry.addRitual(RitualRegistry.ritual_wild_growth = new PatchedRitualWildGrowth("ritual_wild_growth", 300, RitualConfig.disableRitualCategory.disableWildGrowth));
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		//DynamicTreesRoots.Logger.info("Attempting wild wood growth ritual override");
	}
}
