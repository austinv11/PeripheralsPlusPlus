package miscperipherals.module;

import miscperipherals.api.IInteractiveSorterOutput;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import buildcraft.api.transport.IPipeEntry;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleBuildCraftTransport extends Module {	
	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {		
		if (MiscPeripherals.instance.enableChargeStation) {
			ItemStack pipePowerGold = ModuleBuildCraftCore.getBCItem("Transport", "pipePowerGold");
			ItemStack ironGearItem = ModuleBuildCraftCore.getBCItem("Core", "ironGearItem");
			if (pipePowerGold != null && ironGearItem != null) {
				GameRegistry.addShapelessRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 0), pipePowerGold, ironGearItem, Item.redstone);
			}
		}
		
		for (String name : new String[] {"pipeGate", "pipeGateAutarchic"}) {
			ItemStack gate = ModuleBuildCraftCore.getBCItem("Transport", name);
			if (gate != null) {
				gate = gate.copy();
				gate.setItemDamage(OreDictionary.WILDCARD_VALUE);
				OreDictionary.registerOre("MiscPeripherals$bcGate", gate);
			}
		}
		
		for (String name : new String[] {"redPipeWire", "bluePipeWire", "greenPipeWire", "yellowPipeWire"}) {
			ItemStack wire = ModuleBuildCraftCore.getBCItem("Transport", name);
			if (wire != null) {
				wire = wire.copy();
				wire.setItemDamage(OreDictionary.WILDCARD_VALUE);
				OreDictionary.registerOre("MiscPeripherals$bcPipeWire", wire);
			}
		}
		
		IInteractiveSorterOutput.handlers.add(new IInteractiveSorterOutput() {
			@Override
			public void output(ItemStack stack, World world, int posX, int posY, int posZ, int direction) {
				TileEntity te = world.getBlockTileEntity(posX + Facing.offsetsXForSide[direction], posY + Facing.offsetsYForSide[direction], posZ + Facing.offsetsZForSide[direction]);
				if (te instanceof IPipeEntry) { // try pipe
					IPipeEntry pipe = (IPipeEntry)te;
					
					if (pipe.acceptItems()) {
						pipe.entityEntering(stack.copy(), ForgeDirection.getOrientation(direction));
						stack.stackSize = 0;
					}
				}
			}
		});
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
