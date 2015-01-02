package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.Reflector;
import miscperipherals.tile.TileChargeStation;
import miscperipherals.tile.TileChargeStation.ChargeStationPlugin;
import miscperipherals.tile.TileGateReader;
import miscperipherals.upgrade.UpgradeGateReader;
import miscperipherals.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.NetHandler;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.tools.IToolWrench;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModuleBuildCraftCore extends Module {
	public static boolean enableGateReader = true;

	@Override
	public void onPreInit() {
		enableGateReader = MiscPeripherals.instance.settings.get("features", "enableGateReader", enableGateReader, "Enable the Gate Reader peripheral and turtle upgrade").getBoolean(enableGateReader);
	}

	@Override
	public void onInit() {
		if (enableGateReader) {
			MiscPeripherals.instance.blockAlpha.registerTile(6).setClass(TileGateReader.class).setSprites("gate").setName("gateReader").setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileGateReader.class, "MiscPeripherals Gate Reader");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.gateReader.name", "Gate Reader");
			GameRegistry.registerCustomItemStack("gateReader", new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 6));
			
			MiscPeripherals.registerUpgrade(new UpgradeGateReader());
		}
	}

	@Override
	public void onPostInit() {		
		if (enableGateReader) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 6), "RWR", "WGW", "RWR", 'R', Item.redstone, 'G', "MiscPeripherals$bcGate", 'W', "MiscPeripherals$bcPipeWire"));
		}
		
		TileChargeStation.PLUGINS.add(new ChargeStationPlugin() {
			@Override
			public void writeToNBT(TileChargeStation station, NBTTagCompound compound) {
				((IPowerProvider)station.bcProvider).writeToNBT(compound);
			}
			
			@Override
			public void update(TileChargeStation station) {
				((IPowerProvider)station.bcProvider).update(station);
			}
			
			@Override
			public void unload(TileChargeStation station) {
				
			}
			
			@Override
			public void readFromNBT(TileChargeStation station, NBTTagCompound compound) {
				((IPowerProvider)station.bcProvider).readFromNBT(compound);
			}
			
			@Override
			public boolean onBlockActivated(TileChargeStation station, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
				ItemStack held = player.getCurrentEquippedItem();
				if (held != null && held.getItem() instanceof IToolWrench) {
					station.setFacing(side);
					return true;
				}
				
				return false;
			}
			
			@Override
			public boolean isBattery(TileChargeStation station, ItemStack item) {
				return false;
			}

			@Override
			public void initialize(TileChargeStation station) {
				station.bcProvider = PowerFramework.currentFramework.createPowerProvider();
				((IPowerProvider)station.bcProvider).configure(0, 0, station.powerRequest(ForgeDirection.UNKNOWN), 0, station.powerRequest(ForgeDirection.UNKNOWN));
			}

			@Override
			public boolean isDisabled(TileChargeStation station) {
				return false;
			}
		});
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}

	public static ItemStack getBCItem(String module, String item) {
		ItemStack ret = GameRegistry.findItemStack("BuildCraft|" + module, item, 1);
		if (ret == null) ret = Util.createItemStackNC(Reflector.getField("buildcraft.BuildCraft" + module, item, Object.class), 1, 0);
		return ret;
	}
}
