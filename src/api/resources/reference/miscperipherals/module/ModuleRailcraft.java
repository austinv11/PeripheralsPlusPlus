package miscperipherals.module;

import java.util.HashMap;
import java.util.Map;

import miscperipherals.api.IMinecartData;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.external.ExtTrack;
import miscperipherals.external.ExtTrackLauncher;
import miscperipherals.external.ExtTrackLimiter;
import miscperipherals.external.ExtTrackLocomotive;
import miscperipherals.external.ExtTrackPriming;
import miscperipherals.safe.Reflector;
import miscperipherals.tile.TileSignalController;
import miscperipherals.upgrade.UpgradeTank;
import miscperipherals.util.Util;
import mods.railcraft.api.carts.IRoutableCart;
import mods.railcraft.api.tracks.ITrackInstance;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computer.api.ComputerCraftAPI;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.IPeripheralHandler;

public class ModuleRailcraft extends Module {
	public static boolean enableSignalController = true;

	@Override
	public void onPreInit() {
		enableSignalController = MiscPeripherals.instance.settings.get("features", "enableSignalController", enableSignalController, "Enable the Electronic Signal Controller peripheral").getBoolean(enableSignalController);
	}

	@Override
	public void onInit() {
		if (enableSignalController) {
			MiscPeripherals.instance.blockAlpha.registerTile(14).setClass(TileSignalController.class).setSprites("signalBox").setName("signalController").setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileSignalController.class, "MiscPeripherals Signal Controller");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.signalController.name", "Electronic Signal Controller");
			
			ItemStack controller = GameRegistry.findItemStack("Railcraft", "signal.box.controller", 1);
			if (controller == null) controller = GameRegistry.findItemStack("Railcraft", "part.circuit.signal", 1);
			if (controller == null) controller = new ItemStack(Item.redstoneRepeater);
			ItemStack receiver = GameRegistry.findItemStack("Railcraft", "signal.box.receiver", 1);
			if (receiver == null) receiver = GameRegistry.findItemStack("Railcraft", "part.circuit.signal", 1);
			if (receiver == null) receiver = new ItemStack(Item.redstoneRepeater);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 14), true, "GRG", "@D#", "GRG", 'G', Item.ingotGold, 'R', Item.redstone, 'D', Item.diamond, '@', controller, '#', receiver));
			GameRegistry.registerCustomItemStack("signalController", new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 14));
		}
	}

	@Override
	public void onPostInit() {
		ItemStack worldAnchor = GameRegistry.findItemStack("Railcraft", "machine.alpha.world.anchor", 1);
		if (worldAnchor != null) {
			OreDictionary.registerOre("MiscPeripherals$chunkLoader", worldAnchor);
		}
		
		ItemStack ironTankGauge = GameRegistry.findItemStack("Railcraft", "machine.beta.tank.iron.gauge", 1);
		if (ironTankGauge != null) {
			UpgradeTank.CRAFTING_ITEM[0] = ironTankGauge;
		}
		
		final Map<String, Class<? extends ExtTrack>> tracks = new HashMap<String, Class<? extends ExtTrack>>();
		tracks.put("mods.railcraft.common.blocks.tracks.TrackLimiter", ExtTrackLimiter.class);
		tracks.put("mods.railcraft.common.blocks.tracks.TrackLocomotive", ExtTrackLocomotive.class);
		tracks.put("mods.railcraft.common.blocks.tracks.TrackLauncher", ExtTrackLauncher.class);
		tracks.put("mods.railcraft.common.blocks.tracks.TrackPriming", ExtTrackPriming.class);
		Class<? extends TileEntity> cls = Reflector.getClass("mods.railcraft.common.blocks.tracks.TileTrack");
		if (cls != null) ComputerCraftAPI.registerExternalPeripheral(cls, new IPeripheralHandler() {			
			@Override
			public IHostedPeripheral getPeripheral(TileEntity tile) {
				ITrackInstance track = Reflector.getField(tile, "track", ITrackInstance.class);
				Class extClass = tracks.get(track.getClass().getName());
				if (extClass != null) {
					try {
						return Reflector.construct(extClass, ExtTrack.class, track);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
		
		IMinecartData.handlers.add(new IMinecartData() {
			@Override
			public Map<Object, Object> getMinecartData(EntityMinecart cart) {
				Map<Object, Object> ret = new HashMap<Object, Object>();
				
				String className = cart.getClass().getName();
				if (className.equals("mods.railcraft.common.carts.EntityCartAnchor") || className.equals("mods.railcraft.common.carts.EntityCartAnchorPersonal") || className.equals("mods.railcraft.common.carts.EntityCartAnchorAdmin")) {
					ret.put("__CART_TYPE__", "anchor");
					
					if (className.equals("mods.railcraft.common.carts.EntityCartAnchor")) ret.put("type", "anchor");
					else if (className.equals("mods.railcraft.common.carts.EntityCartAnchorPersonal")) ret.put("type", "personal");
					else if (className.equals("mods.railcraft.common.carts.EntityCartAnchorAdmin")) ret.put("type", "admin");
					
					Integer refuel = Reflector.invoke("mods.railcraft.common.core.RailcraftConfig", "anchorRefuel", Integer.class);
					if (refuel != null && refuel > 0) {
						Long fuel = Reflector.invoke(cart, "getAnchorFuel", Long.class);
						if (fuel != null) ret.put("fuel", (double)fuel / (72000.0D * (double)refuel));
					}
				} else if (className.equals("mods.railcraft.common.carts.EntityCartBasic")) {
					ret.put("__CART_TYPE__", "basic");
					ret.put("occupied", cart.riddenByEntity != null);
					if (cart.riddenByEntity != null) ret.put("username", cart.riddenByEntity.getEntityName());
				} else if (className.equals("mods.railcraft.common.carts.EntityCartChest")) {
					ret.put("__CART_TYPE__", "storage");
				} else if (className.equals("mods.railcraft.common.carts.EntityCartEnergy")) {
					ret.put("__CART_TYPE__", "energy");
					ret.put("type", Util.camelCase(((IInventory)cart).getInvName().replace(" Cart", "")));
					ret.put("energy", cart.getDataWatcher().getWatchableObjectInt(20));
					Integer capacity = Reflector.invoke(cart, "getCapacity", Integer.class);
					if (capacity != null) ret.put("capacity", capacity);
					Integer transferLimit = Reflector.invoke(cart, "getTransferLimit", Integer.class);
					if (transferLimit != null) ret.put("transferRate", transferLimit);
				} else if (className.equals("mods.railcraft.common.carts.EntityCartFurnace")) {
					ret.put("__CART_TYPE__", "furnace");
					NBTTagCompound workaround = new NBTTagCompound();
					cart.writeToNBT(workaround);
					ret.put("fuel", workaround.getInteger("Fuel"));
				} else if (className.equals("mods.railcraft.common.carts.EntityCartPumpkin")) {
					ret.put("__CART_TYPE__", "pumpkin");
				} else if (className.equals("mods.railcraft.common.carts.EntityCartGift")) {
					ret.put("__CART_TYPE__", "gift");
				} else if (className.equals("mods.railcraft.common.carts.EntityCartTNT")) {
					ret.put("__CART_TYPE__", "tntWood");
					ret.put("fuse", (int)cart.getDataWatcher().getWatchableObjectShort(20));
				} else if (className.equals("mods.railcraft.common.carts.EntityCartTank")) {
					ret.put("__CART_TYPE__", "tank");
					DataWatcher dw = cart.getDataWatcher();
					if (dw.getWatchableObjectInt(20) != 0) {
						ret.put("liquid", Util.getUUID(new ItemStack(dw.getWatchableObjectInt(20), dw.getWatchableObjectInt(22), dw.getWatchableObjectInt(21))));
						ret.put("amount", dw.getWatchableObjectInt(22));
					}
				} else if (className.equals("mods.railcraft.common.carts.EntityCartTrackRelayer")) {
					ret.put("__CART_TYPE__", "trackRelayer");
				} else if (className.equals("mods.railcraft.common.carts.EntityCartUndercutter")) {
					ret.put("__CART_TYPE__", "undercutter");
				} else if (className.equals("mods.railcraft.common.carts.EntityCartWork")) {
					ret.put("__CART_TYPE__", "work");
				} else if (className.equals("mods.railcraft.common.carts.EntityTunnelBore")) {
					ret.put("__CART_TYPE__", "tunnelBore");
					ItemStack head = ((IInventory) cart).getStackInSlot(0);
					if (head != null) {
						ItemStack ndHead = head.copy();
						ndHead.setItemDamage(0);
						ret.put("boreHead", Util.getUUID(ndHead));
						ret.put("boreHeadHealth", 1.0D - ((double)head.getItemDamage() / (double)head.getMaxDamage()));
					}
					
					ret.put("fuel", Reflector.getField(cart, "fuel", Integer.class));
				} else if (className.equals("mods.railcraft.common.carts.EntityLocomotiveSteam")) {
					ret.put("__CART_TYPE__", "locomotiveSteam");
					DataWatcher dw = cart.getDataWatcher();
					ret.put("mode", Util.camelCase(ExtTrackLocomotive.locoModes[dw.getWatchableObjectByte(27)].toString()));
					byte speed = dw.getWatchableObjectByte(28);
					ret.put("speed", speed == ExtTrackLimiter.locoSpeeds - 1 ? -1 : speed);
					ret.put("color", Util.arrayToMap(new int[] {dw.getWatchableObjectByte(25), dw.getWatchableObjectByte(26)}));
					ret.put("fuel", Reflector.getField(cart, "fuel", Integer.class));
				}
				
				if (cart instanceof IRoutableCart) {
					ret.put("destination", ((IRoutableCart) cart).getDestination());
				}
				
				return ret;
			}
		});
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
	
	public static void updateTrack(ITrackInstance track) {
		Reflector.invoke(track, "sendUpdateToClient", Object.class);
	}
}
