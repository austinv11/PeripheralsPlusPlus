package miscperipherals.module;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import miscperipherals.api.IEnergyMeterData;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.external.ExtTesseract;
import miscperipherals.safe.Reflector;
import miscperipherals.upgrade.UpgradeTank;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thermalexpansion.api.item.ItemRegistry;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computer.api.ComputerCraftAPI;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.IPeripheralHandler;

public class ModuleThermalExpansion extends Module {
	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {		
		if (MiscPeripherals.instance.enableChargeStation) {
			ItemStack machineFrame = ItemRegistry.getItem("machineFrame", 1);
			ItemStack conduitEnergy = ItemRegistry.getItem("conduitEnergy", 1);
			if (machineFrame != null && conduitEnergy != null) {
				GameRegistry.addShapelessRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 0), conduitEnergy, machineFrame, Item.redstone);
			}
		}
		
		// XP exploit with gray slots!
		//MiscPeripheralsAPI.instance.addFurnaceXP(Reflector.getClass("thermalexpansion.factory.tileentity.TileFurnace"), 1);
		
		IEnergyMeterData.handlers.add(new IEnergyMeterData() {
			@Override
			public boolean canHandle(World world, int x, int y, int z, int side, Object[] arguments) {
				if (!world.blockExists(x, y, z)) return false;
				
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te == null || !te.getClass().getName().equals("thermalexpansion.block.conduit.TileConduitEnergy")) return false;
				
				return true;
			}
			
			@Override
			public void start(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments) {
				if (!world.blockExists(x, y, z)) return;
				
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te == null || !te.getClass().getName().equals("thermalexpansion.block.conduit.TileConduitEnergy")) return;
				
				Reflector.setField(te, "trackEnergy", true);
				Reflector.setField(te, "energySent", new LinkedList());
			}
			
			@Override
			public void cleanup(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments) {
				if (!world.blockExists(x, y, z)) return;
				
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te == null || !te.getClass().getName().equals("thermalexpansion.block.conduit.TileConduitEnergy")) return;
				
				Reflector.setField(te, "trackEnergy", false);
				Reflector.setField(te, "energySent", null);
			}

			@Override
			public Map<String, Object> getData(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments) {
				if (!world.blockExists(x, y, z)) return null;
				
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te == null || !te.getClass().getName().equals("thermalexpansion.block.conduit.TileConduitEnergy")) return null;
				
				Map<String, Object> ret = new HashMap<String, Object>();
				List<Float> transfer = Reflector.getField(te, "energySent", List.class);
				float total = 0.0F;
				for (float tick : transfer) total += tick;
				
				ret.put("total", total);
				ret.put("average", total / (float) transfer.size());
				
				Reflector.setField(te, "trackEnergy", true);
				Reflector.setField(te, "energySent", new LinkedList());
				
				return ret;
			}
		});
		
		try {
			ComputerCraftAPI.registerExternalPeripheral((Class<? extends TileEntity>) Class.forName("thermalexpansion.api.tileentity.ITesseract"), new IPeripheralHandler() {
				@Override
				public IHostedPeripheral getPeripheral(TileEntity tile) {
					return new ExtTesseract(tile);
				}
			});
		} catch (ClassNotFoundException e) {
			MiscPeripherals.log.warning("Thermal Expansion too old for tesseract interface");
		}
		
		OreDictionary.registerOre("MiscPeripherals$energyMeter", ItemRegistry.getItem("multimeter", 1));
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
