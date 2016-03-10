package com.austinv11.peripheralsplusplus;

import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class PeripheralsPlusPlus {

	@Mod.Instance(Reference.MOD_ID)
	public static PeripheralsPlusPlus instance;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

	}
}
