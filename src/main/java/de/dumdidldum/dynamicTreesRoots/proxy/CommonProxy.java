package de.dumdidldum.dynamicTreesRoots.proxy;

import de.dumdidldum.dynamicTreesRoots.Config;
import de.dumdidldum.dynamicTreesRoots.Content;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
    	Content.preInit();
    	Config.preInit(event);
    }

	public void init(FMLInitializationEvent event) {
	}
	
	public void postInit(FMLPostInitializationEvent event) {
	}

}
