package com.austinv11.peripheralsplusplus.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions(value = {"com.austinv11.peripheralsplusplus.asm", "java"})
@IFMLLoadingPlugin.SortingIndex(value = Integer.MAX_VALUE)
public class PeripheralsPlusPlusTransformerPlugin implements IFMLLoadingPlugin {
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"com.austinv11.peripheralsplusplus.asm.Transformer"};
	}
	
	@Override
	public String getModContainerClass() {
		return "com.austinv11.peripheralsplusplus.asm.DummyContainer";
	}
	
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		
	}
	
	@Override
	public String getAccessTransformerClass() {
		return "com.austinv11.peripheralsplusplus.asm.PeripheralsPlusPlusAccessTransformer";
	}
}
