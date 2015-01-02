package miscperipherals.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import miscperipherals.api.IMinecartData;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.safe.Reflector;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.io.ByteArrayDataInput;

public class ModuleStevesCarts extends Module {
	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		ReflectionStore.initStevesCarts();
		if (ReflectionStore.modules != null) OreDictionary.registerOre("MiscPeripherals$chunkLoader", new ItemStack(ReflectionStore.modules, 1, 49)); // 49 = chunk loader
		if (ReflectionStore.blockDetector != null) OreDictionary.registerOre("MiscPeripherals$cartDetector", ReflectionStore.blockDetector);
		if (ReflectionStore.blockAdvDetector != null) OreDictionary.registerOre("MiscPeripherals$cartDetectorRail", ReflectionStore.blockAdvDetector);
		
		IMinecartData.handlers.add(new IMinecartData() {
			@Override
			public Map<Object, Object> getMinecartData(EntityMinecart cart) {
				Map<Object, Object> ret = new HashMap<Object, Object>();
				
				if (Reflector.getClass("vswe.stevescarts.Carts.entMCBase").isAssignableFrom(cart.getClass())) {
					ret.put("__CART_TYPE__", "modular");
					
					Map moduleData = Reflector.invoke("vswe.stevescarts.ModuleData.ModuleData", "getList", Map.class);
					assert moduleData != null : "moduleData is null!";
					ArrayList modules = Reflector.invoke(cart, "getModules", ArrayList.class);
					Map<Integer, String> moduleNames = new HashMap<Integer, String>(modules.size());
					int j = 1;
					for (int i = 0; i < modules.size(); i++) {
						Object id = Reflector.invoke(modules.get(i), "getModuleId", Object.class);
						if (id == null) continue;
						Object mod = moduleData.get(id);
						
						String name = Reflector.invoke(mod, "getName", String.class);
						if (ReflectionStore.moduleDataHull != null && ReflectionStore.moduleDataHull.isAssignableFrom(mod.getClass())) ret.put("hull", name);
						else moduleNames.put(j++, name);
					}
					ret.put("modules", moduleNames);
					
					Collection stateList = Reflector.invoke("vswe.stevescarts.ModuleState", "getStateList", Collection.class);
					Map<String, Boolean> states = new HashMap<String, Boolean>();
					for (Object state : stateList) {
						String name = Reflector.invoke(state, "getName", String.class);
						Boolean eval = Reflector.invoke(state, "evaluate", Boolean.class, cart);
						if (eval != null && eval) states.put(name, true);
						else states.put(name, false);
					}
					ret.put("states", states);
				} else return null;
				
				return ret;
			}
		});
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
