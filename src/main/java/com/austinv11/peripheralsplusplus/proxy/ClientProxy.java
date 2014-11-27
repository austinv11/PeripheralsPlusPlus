package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.client.models.RenderRocket;
import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.turtles.TurtleCompass;
import com.austinv11.peripheralsplusplus.utils.IconManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void iconManagerInit() {
		IconManager.upgrades.add(new TurtleCompass());
		MinecraftForge.EVENT_BUS.register(new IconManager());
	}

	@Override
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, new RenderRocket());
	}
}
