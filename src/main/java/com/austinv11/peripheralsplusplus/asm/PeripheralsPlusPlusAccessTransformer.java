package com.austinv11.peripheralsplusplus.asm;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class PeripheralsPlusPlusAccessTransformer extends AccessTransformer {
	
	public PeripheralsPlusPlusAccessTransformer() throws IOException {
		super("META-INF/ppp_at.cfg");
	}
}
