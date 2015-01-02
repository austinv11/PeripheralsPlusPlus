package gregtechmod.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * This File contains the functions used to get Items and add Recipes. Please do not include this File in your Moddownload as it maybe ruins compatiblity, like with the IC2-API
 * You may just copy those Functions into your Code, or better call them via invocation.
 */
public class GregTech_API {
	private static boolean isGregTechLoaded = false;
	
	/**
	 * Do not use this Function unless you are me!
	 * I use this to easily determine if my Addon is loaded.
	 * I call it in the constructor of my Main-Addon-File.
	 */
	public static void setGregTechLoaded() {isGregTechLoaded = true;}
	
	/**
	 * Gets true in the PreInitPhase of the GregTechAddon
	 */
	public static boolean isGregTechLoaded() {return isGregTechLoaded;}
	
	/**
	 * Gets a Block from my Addon.
	 * @param aIndex Index of my Item:
	 * 0 Standardblock,
	 * 1 Machineblock,
	 * 2 Oreblock,
	 * 3 That glowing thing from my Lighthelmet.
	 * @param aAmount Amount of the Item in the returned Stack
	 * @param aMeta The Metavalue of the Block
	 * @return The ItemStack you ordered, if not then look at the Log.
	 */
	public static ItemStack getGregTechBlock(int aIndex, int aAmount, int aMeta) {
		if (isGregTechLoaded()) {
			try {
				return (ItemStack)Class.forName("gregtechmod.GT_Mod").getMethod("getGregTechBlock", int.class, int.class, int.class).invoke(null, aIndex, aAmount, aMeta);
			} catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * Ever wondered out of how many Tincells an Item consists? Find it out.
	 * You could also check if the targetItem implements ICapsuleCellContainer, to not be relent on the Code in my Addon, but this also outputs values for IC2-Items
	 * @param aStack the Stack of the Item
	 * @return The amount of Tincells in ONE of the Items from the Stack
	 */
	public static int getCapsuleCellContainerCount(ItemStack aStack) {
		if (isGregTechLoaded()) {
			try {
				return (Integer)Class.forName("gregtechmod.GT_Mod").getMethod("getCapsuleCellContainerCount", ItemStack.class).invoke(null, aStack);
			} catch (Exception e) {}
		}
		return 0;
	}
	
	
	
	
	/**
	 * Gets an Item from my Addon. The Indizes are the same, as the ones at the items.png
	 * @param aIndex Index of my Item
	 * @param aAmount Amount of the Item in the returned Stack
	 * @return The ItemStack you ordered, if not then look at the Log.
	 */
	public static ItemStack getGregTechItem(int aIndex, int aAmount, int aMeta) {
		if (isGregTechLoaded()) {
			try {
				return (ItemStack)Class.forName("gregtechmod.GT_Mod").getMethod("getGregTechItem", int.class, int.class, int.class).invoke(null, aIndex, aAmount, aMeta);
			} catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * Adds a FusionreactorRecipe
	 * @param aInput1 = first Input (not null, and respects StackSize)
	 * @param aInput2 = second Input (not null, and respects StackSize)
	 * @param aOutput = Output of the Fusion (can be null, and respects StackSize)
	 * @param aFusionDurationInTicks = How many ticks the Fusion lasts (must be > 0)
	 * @param aFusionEnergyPerTick = The EU generated per Tick (can even be negative!)
	 * @param aEnergyNeededForStartingFusion = EU needed for heating the Reactor up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public static boolean addFusionReactorRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addFusionReactorRecipe", ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class, int.class).invoke(null, aInput1, aInput2, aOutput, aFusionDurationInTicks, aFusionEnergyPerTick, aEnergyNeededForStartingFusion);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * Adds a Centrifuge Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincellcount, < 0 for negative Fuelcancount, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 * @param aOutput4 can be null
	 * @param aDuration must be > 0
	 */
	public static boolean addCentrifugeRecipe(ItemStack aInput1, int aCellInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addCentrifugeRecipe", ItemStack.class, int.class, ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class).invoke(null, aInput1, aCellInput, aOutput1, aOutput2, aOutput3, aOutput4, aDuration);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * Adds a Electrolyzer Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincellcount, < 0 for negative Fuelcancount, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 * @param aOutput4 can be null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 */
	public static boolean addElectrolyzerRecipe(ItemStack aInput1, int aCellInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addElectrolyzerRecipe", ItemStack.class, int.class, ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class).invoke(null, aInput1, aCellInput, aOutput1, aOutput2, aOutput3, aOutput4, aDuration, aEUt);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * Adds a Chemical Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 */
	public static boolean addElectrolyzerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addChemicalRecipe", ItemStack.class, ItemStack.class, ItemStack.class, int.class).invoke(null, aInput1, aInput2, aOutput1, aDuration);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	/**
	 * Adds a Vacuum Freezer Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 */
	public static boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addVacuumFreezerRecipe", ItemStack.class, ItemStack.class, int.class).invoke(null, aInput1, aOutput1, aDuration);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	/**
	 * Adds a Blast Furnace Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 can be null
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 * @param aLevel should be > 0 is the minimum Heat Level needed for this Recipe
	 */
	public static boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addBlastRecipe", ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class, int.class).invoke(null, aInput1, aInput2, aOutput1, aOutput2, aDuration, aEUt, aLevel);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * Adds an Implosion Compressor Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 amount of ITNT, should be > 0
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 */
	public static boolean addImplosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addImplosionRecipe", ItemStack.class, int.class, ItemStack.class, ItemStack.class).invoke(null, aInput1, aInput2, aOutput1, aOutput2);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	/**
	 * Adds a Grinder Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincell count, < 0 for negative Water Cell count, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 * @param aOutput4 can be null
	 */
	public static boolean addGrinderRecipe(ItemStack aInput1, int aCellInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addGrinderRecipe", ItemStack.class, int.class, ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class).invoke(null, aInput1, aCellInput, aOutput1, aOutput2, aOutput3, aOutput4);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	/**
	 * Adds a Sawmill Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincell count, < 0 for negative Water Cell count, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 */
	public static boolean addSawmillRecipe(ItemStack aInput1, int aCellInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addSawmillRecipe", ItemStack.class, int.class, ItemStack.class, ItemStack.class, ItemStack.class).invoke(null, aInput1, aCellInput, aOutput1, aOutput2, aOutput3);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * Adds a Fuel for My Generators
	 * @param aInput1 must be != null
	 * @param aOutput1 can be null
	 * @param aEU EU per MilliBucket. If no Liquid Form of this Container is available, then it will give you EU*1000 per Item.
	 * @param aType 1 = Diesel; 2 = Gas Turbine; 3 = Thermal; 4 = DenseFluid; 5 = Plasma
	 */
	public static boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addFuel", ItemStack.class, ItemStack.class, int.class, int.class).invoke(null, aInput1, aOutput1, aEU, aType);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	/**
	 * Adds a BlockID to the List of the Minable Blocks by the Jackhammer
	 * @param aBlock
	 */
	public static boolean addJackHammerMinableBlock(Block aBlock) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addJackHammerMinableBlock", Block.class).invoke(null, aBlock);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
	
	/**
	 * Adds a Sound to the Sonictron9001
	 * you should NOT call this in the preInit-Phase!
	 * @param aItemStack = The Item you want to display for this Sound
	 * @param aSoundName = The Name of the Sound in the resources/newsound-folder like Vanillasounds
	 * @return true if the Sound got added, otherwise false.
	 */
	public static boolean addSonictronSound(ItemStack aItemStack, String aSoundName) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addSonictronSound", ItemStack.class, String.class).invoke(null, aItemStack, aSoundName);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * Adds a Description to the Computercube
	 * you should call this in the postInit-Phase!
	 * @param aItemStack[] = An Array of 14 ItemStacks not more, not less. The Stacks themself can be null if needed. The last 9 Stacks are showing a Craftingrecipe in the top right Corner if one of them is != null.
	 * @param aText[] = An Array of exactly 17 Lines of Text. 0 = First Line; 16 = Last displayable Line.
	 * @return true if the Descripion got added, otherwise false.
	 */
	
	public static boolean addComputercubeDescriptionSet(ItemStack[] aItemStack, String[] aText) {
		if (isGregTechLoaded()) {
			try {
				Class.forName("gregtechmod.GT_Mod").getMethod("addComputercubeDescriptionSet", ItemStack[].class, String[].class).invoke(null, aItemStack, aText);
				return true;
			} catch (Exception e) {}
		}
		return false;
	}
}
