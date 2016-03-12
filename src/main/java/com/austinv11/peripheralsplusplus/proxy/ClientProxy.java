package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.client.ItemRenderRegister;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

	@SideOnly(Side.CLIENT)
	@Override
	public void setupItemRenderer() {
		ItemRenderRegister.init();
	}
}
