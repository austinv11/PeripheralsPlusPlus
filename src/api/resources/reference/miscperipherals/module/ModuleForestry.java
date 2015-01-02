package miscperipherals.module;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.tile.TileBeeAnalyzer;
import miscperipherals.tile.TileButterflyAnalyzer;
import miscperipherals.tile.TileTreeAnalyzer;
import miscperipherals.upgrade.UpgradeGrafter;
import miscperipherals.upgrade.UpgradeScoop;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import forestry.api.core.ItemInterface;
import forestry.api.recipes.RecipeManagers;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.EnumBackpackType;
import forestry.api.storage.IBackpackDefinition;

public class ModuleForestry extends Module {
	public static Item backpack;
	public static Item backpackT2;
	
	public static boolean enableBeeAnalyzer = true;
	public static boolean enableTreeAnalyzer = true;
	public static boolean enableButterflyAnalyzer = true;
	public static boolean enableBackpacks = true;
	public static boolean enableGrafter = true;
	public static boolean enableScoop = true;
	
	private int backpackID;
	private int backpackT2ID;
	
	public static class ComputerBackpackDefinition implements IBackpackDefinition {
		private final List<ItemStack> items = new LinkedList(Arrays.asList(
			new ItemStack(MiscPeripherals.instance.blockAlpha, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(MiscPeripherals.instance.blockBeta, 1, OreDictionary.WILDCARD_VALUE),
			new ItemStack(MiscPeripherals.instance.itemAlpha, 1, OreDictionary.WILDCARD_VALUE),
			new ItemStack(MiscPeripherals.instance.itemSmartHelmet, 1, OreDictionary.WILDCARD_VALUE) 
		));
		
		public ComputerBackpackDefinition() {
			addCCItem("ComputerCraft", "CC-Computer");
			addCCItem("ComputerCraft", "CC-Peripheral");
			addCCItem("ComputerCraft", "CC-Cable");
			addCCItem("CCTurtle", "CC-Turtle");
			addCCItem("CCTurtle", "CC-TurtleExpanded");
		}
		
		@Override
		public String getKey() {
			return "computer";
		}

		@Override
		public String getName() {
			return "Computer Engineer's Backpack";
		}

		@Override
		public int getPrimaryColour() {
			return 0xC0C0C0;
		}

		@Override
		public int getSecondaryColour() {
			return 0xFFFFFF;
		}

		@Override
		public void addValidItem(ItemStack validItem) {
			items.add(validItem);
		}

		@Override
		public Collection<ItemStack> getValidItems(EntityPlayer player) {
			return items;
		}
		
		@Override
		public boolean isValidItem(EntityPlayer player, ItemStack itemstack) {
			for (ItemStack entry : items) {
				if (Util.areStacksEqual(entry, itemstack)) return true;
			}
			return false;
		}
		
		private void addCCItem(String mod, String name) {
			ItemStack stack = GameRegistry.findItemStack(mod, name, 1);
			if (stack != null) {
				stack.setItemDamage(OreDictionary.WILDCARD_VALUE);
				items.add(stack);
			}
		}
	}

	@Override
	public void onPreInit() {
		enableBeeAnalyzer = MiscPeripherals.instance.settings.get("features", "enableBeeAnalyzer", enableBeeAnalyzer, "Enable the Bee Analyzer peripheral").getBoolean(enableBeeAnalyzer);
		enableTreeAnalyzer = MiscPeripherals.instance.settings.get("features", "enableTreeAnalyzer", enableTreeAnalyzer, "Enable the Tree Analyzer peripheral").getBoolean(enableTreeAnalyzer);
		enableButterflyAnalyzer = MiscPeripherals.instance.settings.get("features", "enableButterflyAnalyzer", enableButterflyAnalyzer, "Enable the Butterfly Analyzer peripheral").getBoolean(enableButterflyAnalyzer);
		enableBackpacks = MiscPeripherals.instance.settings.get("features", "enableBackpacks", enableBackpacks, "Enable the Computer Engineer's Backpacks").getBoolean(enableBackpacks);
		enableGrafter = MiscPeripherals.instance.settings.get("features", "enableGrafter", enableGrafter, "Enable the Grafter turtle upgrade").getBoolean(enableGrafter);
		enableScoop = MiscPeripherals.instance.settings.get("features", "enableScoop", enableScoop, "Enable the Scoop turtle upgrade").getBoolean(enableScoop);
		
		backpackID = MiscPeripherals.instance.settings.getItem("backpack", 26455, "ID for the Computer Engineer's Backpack").getInt();
		backpackT2ID = MiscPeripherals.instance.settings.getItem("backpackT2", 26456, "ID for the Woven Computer Engineer's Backpack").getInt();
	}

	@Override
	public void onInit() {
		LanguageRegistry lang = LanguageRegistry.instance();
		
		if (enableBeeAnalyzer) {
			MiscPeripherals.instance.blockAlpha.registerTile(4).setClass(TileBeeAnalyzer.class).setSprites("analyzer_bee").setName("beeAnalyzer").setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileBeeAnalyzer.class, "MiscPeripherals Bee Analyzer");
			lang.addStringLocalization("miscperipherals.beeAnalyzer.name", "Bee Analyzer");
			GameRegistry.registerCustomItemStack("beeAnalyzer", new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 4));
		}
		
		if (enableTreeAnalyzer) {
			MiscPeripherals.instance.blockAlpha.registerTile(11).setClass(TileTreeAnalyzer.class).setSprites("analyzer_tree").setName("treeAnalyzer").setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileTreeAnalyzer.class, "MiscPeripherals Tree Analyzer");
			lang.addStringLocalization("miscperipherals.treeAnalyzer.name", "Tree Analyzer");
			GameRegistry.registerCustomItemStack("treeAnalyzer", new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 11));
		}
		
		if (enableButterflyAnalyzer) {
			MiscPeripherals.instance.blockBeta.registerTile(9).setClass(TileButterflyAnalyzer.class).setSprites("analyzer_butterfly").setName("butterflyAnalyzer").setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileButterflyAnalyzer.class, "MiscPeripherals Butterfly Analyzer");
			lang.addStringLocalization("miscperipherals.butterflyAnalyzer.name", "Butterfly Analyzer");
			GameRegistry.registerCustomItemStack("butterflyAnalyzer", new ItemStack(MiscPeripherals.instance.blockBeta, 1, 9));
		}
	}

	@Override
	public void onPostInit() {
		if (enableBeeAnalyzer) {
			GameRegistry.addRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 4), "GRG", "R@R", "GRG", 'G', Item.ingotGold, 'R', Item.redstone, '@', ItemInterface.getItem("beealyzer"));
		}
		
		if (enableTreeAnalyzer) {
			GameRegistry.addRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 11), "GRG", "R@R", "GRG", 'G', Item.ingotGold, 'R', Item.redstone, '@', ItemInterface.getItem("treealyzer"));
		}
		
		if (enableButterflyAnalyzer) {
			GameRegistry.addRecipe(new ItemStack(MiscPeripherals.instance.blockBeta, 1, 9), "GRG", "R@R", "GRG", 'G', Item.ingotGold, 'R', Item.redstone, '@', ItemInterface.getItem("flutterlyzer"));
		}
		
		if (enableBackpacks) {
			ComputerBackpackDefinition def = new ComputerBackpackDefinition();
			
			backpack = BackpackManager.backpackInterface.addBackpack(backpackID, def, EnumBackpackType.T1).setCreativeTab(MiscPeripherals.instance.tabMiscPeripherals);
			GameRegistry.registerCustomItemStack("backpack", new ItemStack(backpack, 1, 0));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(backpack), "SWS", "D@D", "SWS", 'S', Item.silk, 'W', new ItemStack(Block.cloth, 1, Short.MAX_VALUE), '@', Block.chest, 'D', "MiscPeripherals$disk"));
			
			backpackT2 = BackpackManager.backpackInterface.addBackpack(backpackT2ID, def, EnumBackpackType.T2).setCreativeTab(MiscPeripherals.instance.tabMiscPeripherals);
			GameRegistry.registerCustomItemStack("backpackT2", new ItemStack(backpackT2, 1, 0));
			ItemStack wovenSilk = ItemInterface.getItem("craftingMaterial");
			if (wovenSilk != null) {
				wovenSilk.setItemDamage(3);
				RecipeManagers.carpenterManager.addRecipe(200, new LiquidStack(Block.waterStill.blockID, 1000), null, new ItemStack(backpackT2), new Object[] {"SDS", "S@S", "SSS", 'D', Item.diamond, '@', backpack, 'S', wovenSilk});
			}
		}
		
		if (enableGrafter) {
			MiscPeripherals.registerUpgrade(new UpgradeGrafter());
		}
		
		if (enableScoop) {
			MiscPeripherals.registerUpgrade(new UpgradeScoop());
		}
		
		UpgradeGrafter.registerGrafter(ItemInterface.getItem("grafter"));
		UpgradeGrafter.registerGrafter(ItemInterface.getItem("grafterProven"));
		UpgradeScoop.registerScoop(ItemInterface.getItem("scoop"));
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
