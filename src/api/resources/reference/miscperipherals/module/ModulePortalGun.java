package miscperipherals.module;

import miscperipherals.core.Module;
import miscperipherals.external.ExtAFP;
import miscperipherals.external.ExtPortalSpawner;
import miscperipherals.safe.Reflector;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import dan200.computer.api.ComputerCraftAPI;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.IPeripheralHandler;

public class ModulePortalGun extends Module {
	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		Class<? extends TileEntity> cls = Reflector.getClass("portalgun.common.tileentity.TileEntityAFP");
		if (cls != null) ComputerCraftAPI.registerExternalPeripheral(cls, new IPeripheralHandler() {
			@Override
			public IHostedPeripheral getPeripheral(TileEntity tile) {
				return new ExtAFP(tile);
			}
		});
		cls = Reflector.getClass("portalgun.common.tileentity.TileEntityPortalMod");
		if (cls != null) ComputerCraftAPI.registerExternalPeripheral(cls, new IPeripheralHandler() {
			@Override
			public IHostedPeripheral getPeripheral(TileEntity tile) {
				if (Reflector.getField(tile, "isSpawner", Boolean.class)) return new ExtPortalSpawner(tile);
				else return null;
			}
		});
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
	
	public static void setValueWithPair(Object instance, String fieldName, Object value) {
		setValueWithPair(instance, fieldName, value, "pair");
	}
	
	public static void setValueWithPair(Object instance, String fieldName, Object value, String pairName) {
		Reflector.setField(instance, fieldName, value);
		Object pair = Reflector.getField(instance, pairName, Object.class);
		if (pair != null) Reflector.setField(pair, fieldName, value);
	}
}
