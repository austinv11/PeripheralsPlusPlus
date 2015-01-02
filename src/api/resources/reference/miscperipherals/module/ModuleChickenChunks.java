package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.io.ByteArrayDataInput;

public class ModuleChickenChunks extends Module {
	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		ReflectionStore.initChickenChunks();
		
		if (ReflectionStore.blockChunkLoaderC != null) {
			OreDictionary.registerOre("MiscPeripherals$chunkLoader", new ItemStack(ReflectionStore.blockChunkLoaderC, 1, 0));
			// enable spot loader, but only if the size is 1x1
			if (MiscPeripherals.instance.chunkLoaderRadius == 0) OreDictionary.registerOre("MiscPeripherals$chunkLoader", new ItemStack(ReflectionStore.blockChunkLoaderC, 1, 1));
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
