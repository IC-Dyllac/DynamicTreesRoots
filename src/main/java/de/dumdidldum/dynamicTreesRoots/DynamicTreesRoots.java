package de.dumdidldum.dynamicTreesRoots;

import org.apache.logging.log4j.Logger;

import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;

import de.dumdidldum.dynamicTreesRoots.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DynamicTreesRoots.MODID, name = DynamicTreesRoots.NAME, version = DynamicTreesRoots.VERSION,  dependencies = DynamicTreesRoots.DEPENDENCIES)
public class DynamicTreesRoots {
	public static final String MODID = "dynamictreesroots";
	public static final String NAME = "Dynamic Trees Roots";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:dynamictrees@[1.12.2-0.7.7e,);required-after:roots";

	private static Logger logger;
	
    @Instance
    public static DynamicTreesRoots instance;

    @SidedProxy(clientSide = "de.dumdidldum.dynamicTreesRoots.proxy.CommonProxy", serverSide = "de.dumdidldum.dynamicTreesRoots.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        proxy.postInit(event);
    }

}