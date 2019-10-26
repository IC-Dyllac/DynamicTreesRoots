package de.dumdidldum.dynamicTreesRoots;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {

	public static File configDirectory;
	
	public static float wildwoodLogDropMultiplier;
	public static boolean wildwoodDropSeeds;
	
	public static void preInit(FMLPreInitializationEvent event) {
		
		configDirectory = event.getModConfigurationDirectory();
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		wildwoodLogDropMultiplier = config.getFloat("logDropMultiplier", "wildwood", 1, 0, Float.MAX_VALUE, "A multiplier for the wildwood dropped logs (for balancing)");
		wildwoodDropSeeds = config.getBoolean("dropSeeds", "wildwood", false, "Set to true to have wildwood trees drop seeds.");
		
		config.save();
	}
}
