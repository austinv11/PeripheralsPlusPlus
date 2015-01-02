package miscperipherals.upgrade;

import java.util.HashMap;
import java.util.Map;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.peripheral.PeripheralTank;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.ChunkCoordIntPair;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeTank implements ITurtleUpgrade, IUpgradeIcons {
	/**
	 * BuildCraft, Railcraft
	 */
	public static final ItemStack[] CRAFTING_ITEM = new ItemStack[2];
	public static final int GAUGE_MAXIMUM = 10;
	public static final Map<ChunkCoordIntPair, String> LIQUIDS = new HashMap<ChunkCoordIntPair, String>();
	public static final Map<String, Integer> KNOWN_LIQUIDS = new HashMap<String, Integer>();
	
	private Icon[] icons = new Icon[11];
	private Icon[] gauges = new Icon[11];
	
	@Override
	public int getUpgradeID() {
		return 217;
	}

	@Override
	public String getAdjective() {
		return "Tank";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		ItemStack item = Util.getFirstElement(CRAFTING_ITEM);
		return item == null ? new ItemStack(Block.glass) : item;
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		if (turtle != null) {
			IHostedPeripheral peripheral = turtle.getPeripheral(side);
			if (peripheral instanceof PeripheralTank) {
				PeripheralTank tank = (PeripheralTank)peripheral;
				if (tank.liquid != null) {
					return icons[getGauge(tank)];
				}
			}
		}
		return icons[0];
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralTank(turtle);
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		for (int i = 0; i <= 10; i++) {
			icons[i] = reg.registerIcon("MiscPeripherals:tank"+i);
			gauges[i] = reg.registerIcon("MiscPeripherals:tank_gauge"+i);
		}
	}
	
	private int getGauge(PeripheralTank tank) {
		return (int)Math.ceil((tank.liquid.amount / (double)tank.CAPACITY) * (double)GAUGE_MAXIMUM);
	}
	
	/**
	 * RG for public source release: This was used in the coremod days to color the tank, now unused. Dan, y u no add multipass rendering?
	 */
	static {
		// Shortcuts
		String[] names;
		int[] colors;
		
		// Vanilla liquids
		LIQUIDS.put(new ChunkCoordIntPair(Block.waterStill.blockID, 0), "water");
		LIQUIDS.put(new ChunkCoordIntPair(Block.lavaStill.blockID, 0), "lava");
		KNOWN_LIQUIDS.put("water", 0x265CFF);
		KNOWN_LIQUIDS.put("lava", 0xFF6A00);
		
		// BuildCraft
		KNOWN_LIQUIDS.put("oil", 0x000000);
		KNOWN_LIQUIDS.put("fuel", 0xC4C00E);
		
		// Forestry
		KNOWN_LIQUIDS.put("biomass", 0x58C136);
		KNOWN_LIQUIDS.put("biofuel", 0xFF9D00);
		KNOWN_LIQUIDS.put("honey", 0xFFD000);
		KNOWN_LIQUIDS.put("seedoil", 0xFFFD9B);
		KNOWN_LIQUIDS.put("juice", 0xC3FF00);
		KNOWN_LIQUIDS.put("mead", 0xD0B26D);
		KNOWN_LIQUIDS.put("ice", 0xA3FFF7);
		KNOWN_LIQUIDS.put("milk", 0xFFFFFF);
		KNOWN_LIQUIDS.put("liquidglass", 0xD2D2D2);
		
		// Railcraft
		KNOWN_LIQUIDS.put("creosote oil", 0x6B6B00);
		KNOWN_LIQUIDS.put("steam", 0x404040);
		
		// LiquidUU
		KNOWN_LIQUIDS.put("liquiduu", 0xB84FB8);
		
		// Thermal Expansion
		KNOWN_LIQUIDS.put("redstone", 0x1F0000);
		KNOWN_LIQUIDS.put("ender", 0x052C25);
		KNOWN_LIQUIDS.put("glowstone", 0xD2D200);
		
		// IC2
		KNOWN_LIQUIDS.put("coolant", 0x42E2FF);
		
		// XyCraft
		KNOWN_LIQUIDS.put("primer", 0x7D7D7D);
		names = new String[] {"liquid nitrogen", "nitrogen gas", "liquidnitrogen"};
		for (int i = 0; i < names.length; i++) {
			KNOWN_LIQUIDS.put(names[i], 0x80FCE3);
		}
		
		names = new String[] {"black", "red", "green", "brown", "blue", "purple", "cyan", "light gray", "gray", "pink", "lime", "yellow", "light blue", "magenta", "orange", "white"};
		colors = new int[] {0x191919, 0x993333, 0x667F33, 0x664C33, 0x334CB2, 0x7F3FB2, 0x4C7F99, 0x999999, 0x4C4C4C, 0xF27FA5, 0x7FCC19, 0xE5E533, 0x6699D8, 0xB24CD8, 0xD87F33, 0xFFFFFF};
		for (int i = 0; i < names.length; i++) {
			KNOWN_LIQUIDS.put("liquid "+names[i]+" dye", colors[i]);
			KNOWN_LIQUIDS.put("liquid "+names[i]+"dye", colors[i]); // FIXME xycraft does a double registration (new proper name above and old name here), map can't stand it
			KNOWN_LIQUIDS.put("dye"+names[i].replaceAll(" ", ""), colors[i]);
		}
		
		// Liquid Metals/ExtraBees
		names = new String[] {"iron", "gold", "copper", "tin", "silver", "lead", "diamond", "emerald", "obsidian", "bronze"};
		colors = new int[] {0x525252, 0xE5CE37, 0x743306, 0x7A7A8E, 0xA9A9A9, 0x151515, 0x79D8CF, 0x05CB50, 0x251D3D, 0xBD9526};
		for (int i = 0; i < names.length; i++) {
			KNOWN_LIQUIDS.put("molten "+names[i], colors[i]); // Liquid Metals
			KNOWN_LIQUIDS.put("molten"+names[i], colors[i]); // ExtraBees
		}
		
		// ExtraBees
		KNOWN_LIQUIDS.put("liquiddna", 0xFA3BF8);
		KNOWN_LIQUIDS.put("acid", 0xA1DD0B);
		KNOWN_LIQUIDS.put("poison", 0xDA03DA);
		
		// Plugins for Forestry
		KNOWN_LIQUIDS.put("pumpkin juice", 0xFFB62D);
		KNOWN_LIQUIDS.put("melon juice", 0xFF4E54);
		KNOWN_LIQUIDS.put("veggie juice", 0xED4201);
		KNOWN_LIQUIDS.put("mushroom soup", 0xD1B071);
		KNOWN_LIQUIDS.put("living seedoil", 0xDCDC93);
		KNOWN_LIQUIDS.put("heavy water", 0x3C4BCD);
		KNOWN_LIQUIDS.put("liquid peat", 0x2F1F15);
		KNOWN_LIQUIDS.put("radioactive waste", 0xC4FA32);
		KNOWN_LIQUIDS.put("sulfuric acid", 0xFFE803);
		KNOWN_LIQUIDS.put("sugar syrup", 0xBC815D);
		
		// GregTech
		names = new String[] {"wolframium", "lithium", "silicon", "berylium", "calcium", "sodium", "chlorite", "potassium", "mercury", "sodiumpersulfate", "calciumcarbonate"};
		colors = new int[] {0x222028, 0x91BFFF, 0x3F240C, 0x002E00, 0xEA4F61, 0x2B48A4, 0x19645F, 0xA4B5B5, 0xCAAAA3, 0x006743, 0x5B1012};
		for (int i = 0; i < names.length; i++) {
			KNOWN_LIQUIDS.put("fluid"+names[i], colors[i]);
		}
		
		names = new String[] {"hydrogen", "deuterium", "tritium", "helium", "helium-3", "methane", "nitrogen"};
		colors = new int[] {0x000086, 0xFFFF00, 0xC00000, 0xCDD900, 0xFFFFDD, 0xEE1E3D, 0x2D9B9B};
		for (int i = 0; i < names.length; i++) {
			KNOWN_LIQUIDS.put("gas"+names[i], colors[i]);
		}
		
		// Tropicraft
		KNOWN_LIQUIDS.put("tropics water", 0x77B3E5);
		
		// MineFactory Reloaded
		KNOWN_LIQUIDS.put("sludge", 0x141420);
		KNOWN_LIQUIDS.put("sewage", 0x6A342D);
		KNOWN_LIQUIDS.put("mobessence", 0x006400);
	}
}
