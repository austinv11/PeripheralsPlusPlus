package miscperipherals.module;

import java.util.HashMap;
import java.util.Map;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import miscperipherals.api.IEnergyMeterData;
import miscperipherals.api.IRTGFuel;
import miscperipherals.api.MiscPeripheralsAPI;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.peripheral.PeripheralRTG;
import miscperipherals.safe.Reflector;
import miscperipherals.tile.TileChargeStation;
import miscperipherals.tile.TileChargeStation.ChargeStationPlugin;
import miscperipherals.upgrade.UpgradeLaser;
import miscperipherals.upgrade.UpgradeOreScanner;
import miscperipherals.upgrade.UpgradeSolar;
import miscperipherals.upgrade.UpgradeTreetap;
import miscperipherals.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;

public class ModuleIC2 extends Module {
	public static boolean enableLaser = true;
	public static boolean enableSolar = true;
	public static boolean enableTreetap = true;
	public static boolean enableODScanner = true;
	public static boolean enableOVScanner = true;
	
	private boolean hasGT;
	private ItemStack refinedUranium = Items.getItem("refinedUranium");
	private ItemStack uraniumBlock = Items.getItem("uraniumBlock");
	private ItemStack reactorUraniumSimple = Items.getItem("reactorUraniumSimple");
	private ItemStack reactorUraniumDual = Items.getItem("reactorUraniumDual");
	private ItemStack reactorUraniumQuad = Items.getItem("reactorUraniumQuad");
	
	@Override
	public void onPreInit() {
		MiscPeripherals instance = MiscPeripherals.instance;
		
		//hasGT = Loader.isModLoaded("GregTech_Addon");
		hasGT = false;
		
		enableLaser = instance.settings.get("features", "enableLaser", enableLaser, "Enable the Laser turtle upgrade").getBoolean(enableLaser);
		enableSolar = instance.settings.get("features", "enableSolar", enableSolar, "Enable the Solar turtle upgrade").getBoolean(enableSolar);
		enableTreetap = instance.settings.get("features", "enableTreetap", enableTreetap, "Enable the Treetap turtle upgrade").getBoolean(enableTreetap);
		enableODScanner = instance.settings.get("features", "enableODScanner", enableODScanner, "Enable the Ore Scanner turtle upgrade").getBoolean(enableODScanner);
		enableOVScanner = instance.settings.get("features", "enableOVScanner", enableOVScanner, "Enable the Advanced Ore Scanner turtle upgrade").getBoolean(enableOVScanner);
	}
	
	@Override
	public void onInit() {
	}
	
	@Override
	public void onPostInit() {
		if (enableLaser) {
			MiscPeripherals.registerUpgrade(new UpgradeLaser());
		}
		
		if (enableSolar) {
			MiscPeripherals.registerUpgrade(new UpgradeSolar());
		}
		
		if (enableTreetap) {
			MiscPeripherals.registerUpgrade(new UpgradeTreetap());
		}
		
		if (enableODScanner) {
			MiscPeripherals.registerUpgrade(new UpgradeOreScanner(false));
		}
		
		if (enableOVScanner) {
			MiscPeripherals.registerUpgrade(new UpgradeOreScanner(true));
		}
		
		if (MiscPeripherals.instance.enableChargeStation) {
			Recipes.advRecipes.addShapelessRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 0), hasGT ? "crafting10kEUStore" : Items.getItem("reBattery"), hasGT ? "craftingRawMachineTier01" : Items.getItem("machine"), Item.redstone);
		}
		
		if (MiscPeripherals.instance.enableChargeStationT2) {
			Recipes.advRecipes.addRecipe(new ItemStack(MiscPeripherals.instance.blockBeta, 1, 1), " # ", "C@C", " $ ", 'C', Items.getItem("doubleInsulatedGoldCableItem"), '#', hasGT ? "craftingCircuitTier02" : Items.getItem("electronicCircuit"), '$', hasGT ? "crafting100kEUStore" : Items.getItem("energyCrystal"), '@', MiscPeripherals.instance.enableChargeStation ? new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 0) : (hasGT ? "craftingRawMachineTier01" : Items.getItem("machine")));
		}
		
		if (MiscPeripherals.instance.enableChargeStationT3) {
			Recipes.advRecipes.addRecipe(new ItemStack(MiscPeripherals.instance.blockBeta, 1, 2), " # ", "C@C", " $ ", 'C', Items.getItem("glassFiberCableItem"), '#', hasGT ? "craftingCircuitTier04" : Items.getItem("advancedCircuit"), '$', hasGT ? "crafting1kkEUStore" : Items.getItem("lapotronCrystal"), '@', MiscPeripherals.instance.enableChargeStationT2 ? new ItemStack(MiscPeripherals.instance.blockBeta, 1, 1) : (MiscPeripherals.instance.enableChargeStation ? new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 0) : (hasGT ? "craftingRawMachineTier02" : Items.getItem("advancedMachine"))));
		}
		
		MinecraftForge.EVENT_BUS.register(this);
		
		IRTGFuel.handlers.add(new IRTGFuel() {
			@Override
			public int get(ItemStack stack) {
				if (Util.areStacksEqual(stack, refinedUranium)) {
					return (int)PeripheralRTG.URANIUM_TOTAL;
				} else if (Util.areStacksEqual(stack, uraniumBlock)) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * 9);
				} else if (stack.itemID == reactorUraniumSimple.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				} else if (stack.itemID == reactorUraniumDual.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * 2.0F * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				} else if (stack.itemID == reactorUraniumQuad.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * 4.0F * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				}
				
				return 0;
			}
		});
		TileChargeStation.PLUGINS.add(new ChargeStationPlugin() {
			@Override
			public void update(TileChargeStation station) {
				if (!station.addedToEnergyNet) {
					MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(station));
					station.addedToEnergyNet = true;
				}
				
				ItemStack battery = station.getStackInSlot(0);
				if (battery != null && battery.getItem() instanceof IElectricItem) {
					IElectricItem ee = (IElectricItem)battery.getItem();
					if (ee.canProvideEnergy(battery) && ee.getTier(battery) <= station.tier) station.energy += ElectricItem.discharge(battery, (int)Math.ceil(station.getMaxCharge() - station.energy), station.tier, false, false);
				}
			}
			
			@Override
			public boolean isBattery(TileChargeStation station, ItemStack item) {
				if (item.getItem() instanceof IElectricItem) {
					IElectricItem iee = (IElectricItem)item.getItem();
					return iee.canProvideEnergy(item) && iee.getTier(item) <= station.tier;
				}
				
				return false;
			}

			@Override
			public void unload(TileChargeStation station) {
				if (!station.worldObj.isRemote) {
					MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(station));
					station.addedToEnergyNet = false;
				}
			}

			@Override
			public boolean onBlockActivated(TileChargeStation station, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
				return false;
			}

			@Override
			public void readFromNBT(TileChargeStation station, NBTTagCompound compound) {
				
			}

			@Override
			public void writeToNBT(TileChargeStation station, NBTTagCompound compound) {
				
			}

			@Override
			public void initialize(TileChargeStation station) {
				
			}

			@Override
			public boolean isDisabled(TileChargeStation station) {
				return false;
			}
		});
		IEnergyMeterData.handlers.add(new IEnergyMeterData() {
			@Override
			public boolean canHandle(World world, int x, int y, int z, int side, Object[] arguments) {
				if (!world.blockExists(x, y, z)) return false;
				
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (!(te instanceof IEnergyTile)) return false;
				
				return true;
			}
			
			@Override
			public void start(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments) {
				if (!world.blockExists(x, y, z)) return;
				
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (!(te instanceof IEnergyTile)) return;
				
				EnergyNet enet = EnergyNet.getForWorld(world);
				long emitted = enet.getTotalEnergyEmitted(te);
				long sunken = enet.getTotalEnergySunken(te);
				data.setLong("lastTotalEnergyEmitted", emitted);
				data.setLong("lastTotalEnergySunken", sunken);
				data.setLong("firstTotalEnergy", emitted + sunken);
				data.setLong("lastMeasureTime", world.getTotalWorldTime());
			}
			
			@Override
			public void cleanup(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments) {
				
			}

			@Override
			public Map<String, Object> getData(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments) {
				if (!world.blockExists(x, y, z)) return null;
				
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (!(te instanceof IEnergyTile)) return null;
				
				long measurePeriod = data.getLong("lastMeasureTime");
				EnergyNet enet = EnergyNet.getForWorld(world);
				long emitted = enet.getTotalEnergyEmitted(te);
				long sunken = enet.getTotalEnergySunken(te);
				double deltaEmitted = (double)(emitted - data.getLong("lastTotalEnergyEmitted")) / measurePeriod;
				double deltaSunken = (double)(sunken - data.getLong("lastTotalEnergySunken")) / measurePeriod;
				data.setLong("lastTotalEnergyEmitted", enet.getTotalEnergyEmitted(te));
				data.setLong("lastTotalEnergySunken", enet.getTotalEnergySunken(te));
				data.setLong("lastMeasureTime", world.getTotalWorldTime());
				
				Map<String, Object> ret = new HashMap<String, Object>();
				ret.put("total", emitted + sunken - data.getLong("firstTotalEnergy"));
				ret.put("pass", deltaEmitted + deltaSunken);
				ret.put("in", deltaSunken);
				ret.put("out", deltaEmitted);
				return ret;
			}
		});
		
		MiscPeripheralsAPI.instance.addFurnaceXP(Reflector.getClass("ic2.core.block.machine.tileentity.TileEntityElecFurnace"), 2);
		MiscPeripheralsAPI.instance.addFurnaceXP(Reflector.getClass("ic2.core.block.machine.tileentity.TileEntityInduction"), 3, 4);
		
		ItemStack uranium = Items.getItem("reactorUraniumSimple");
		if (uranium != null && uranium.getItem() != null) {
			int euOutput = ic2.api.reactor.IC2Reactor.getEUOutput();
			PeripheralRTG.URANIUM_TOTAL = uranium.getMaxDamage() * PeripheralRTG.OUTPUT * euOutput * 20;
		} else {
			MiscPeripherals.log.warning("!!! IC2 URANIUM IS NULL !!! GregTech?");
		}
		
		OreDictionary.registerOre("MiscPeripherals$rtgChamber", Items.getItem("reactorChamber"));
		OreDictionary.registerOre("MiscPeripherals$rtgGenerator", Items.getItem("generator"));
		OreDictionary.registerOre("MiscPeripherals$rtgCircuit", Items.getItem("advancedCircuit"));
		OreDictionary.registerOre("MiscPeripherals$energyMeter", Items.getItem("ecMeter"));
	}
	
	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
	
	/*@ForgeSubscribe
	public void onTurtleRefuel(TurtleRefuel event) {
		if (event.itemstack == null) return;
		
		ItemStack suBattery = Items.getItem("suBattery");
		if (event.itemstack.getItem() instanceof IElectricItem) {
			event.refuelAmount = (int)Math.floor(ElectricItem.discharge(event.itemstack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false) / MiscPeripherals.instance.fuelEU);
			
			event.setHandled();
		} else if (suBattery != null && event.itemstack.itemID == suBattery.itemID) {
			event.refuelAmount = (int)Math.floor(1000 / MiscPeripherals.instance.fuelEU);
		}
	}*/
}
