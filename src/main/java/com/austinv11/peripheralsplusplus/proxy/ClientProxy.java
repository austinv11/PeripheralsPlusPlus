package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.turtles.TurtleCompass;
import com.austinv11.peripheralsplusplus.utils.IconManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void iconManagerInit() {
		IconManager.upgrades.add(new TurtleCompass());
		MinecraftForge.EVENT_BUS.register(new IconManager());
	}
}
