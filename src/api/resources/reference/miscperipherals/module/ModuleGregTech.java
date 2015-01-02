package miscperipherals.module;

import gregtechmod.api.GregTech_API;
import ic2.api.item.Items;
import ic2.api.recipe.Recipes;
import miscperipherals.api.IRTGFuel;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.peripheral.PeripheralRTG;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleGregTech extends Module {	
	private ItemStack thorium;
	private ItemStack dualThorium;
	private ItemStack quadThorium;
	private ItemStack plutonium;
	private ItemStack dualPlutonium;
	private ItemStack quadPlutonium;

	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		if (MiscPeripherals.instance.enableChargeStationT4) {
			Recipes.advRecipes.addRecipe(new ItemStack(MiscPeripherals.instance.blockBeta, 1, 3), " # ", "C@C", " $ ", 'C', Items.getItem("trippleInsulatedIronCableItem"), '#', "circuitTier07", '$', "10kkEUStore", '@', MiscPeripherals.instance.enableChargeStationT3 ? new ItemStack(MiscPeripherals.instance.blockBeta, 1, 2) : (MiscPeripherals.instance.enableChargeStationT2 ? new ItemStack(MiscPeripherals.instance.blockBeta, 1, 1) : (MiscPeripherals.instance.enableChargeStation ? new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 0) : "rawMachineTier04")));
		}
		
		thorium = GregTech_API.getGregTechItem(48, 1, 0);
		dualThorium = GregTech_API.getGregTechItem(49, 1, 0);
		quadThorium = GregTech_API.getGregTechItem(50, 1, 0);
		plutonium = GregTech_API.getGregTechItem(51, 1, 0);
		dualPlutonium = GregTech_API.getGregTechItem(52, 1, 0);
		quadPlutonium = GregTech_API.getGregTechItem(53, 1, 0);
		
		IRTGFuel.handlers.add(new IRTGFuel() {
			@Override
			public int get(ItemStack stack) {
				if (stack.itemID == thorium.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * PeripheralRTG.THORIUM_MOD * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				} else if (stack.itemID == dualThorium.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * PeripheralRTG.THORIUM_MOD * 2.0F * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				} else if (stack.itemID == quadThorium.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * PeripheralRTG.THORIUM_MOD * 4.0F * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				} else if (stack.itemID == plutonium.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * PeripheralRTG.PLUTONIUM_MOD * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				} else if (stack.itemID == dualPlutonium.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * PeripheralRTG.PLUTONIUM_MOD * 2.0F * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				} else if (stack.itemID == quadPlutonium.itemID) {
					return (int)(PeripheralRTG.URANIUM_TOTAL * PeripheralRTG.PLUTONIUM_MOD * 4.0F * (1.0F - ((float)stack.getItemDamage() / (float)stack.getMaxDamage())));
				}
				
				return 0;
			}
		});
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
