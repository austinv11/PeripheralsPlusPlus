package gregtechmod.api;

import ic2.api.item.Items;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * This File contains the functions used for Recipes. Please do not include this File AT ALL in your Moddownload as it ruins compatibility
 * This is just the Core of my Recipe System, if you just want to GET the Recipes I add, then you can access this File.
 * Do NOT add Recipes using the Constructors inside this Class, The GregTech_API File calls the correct Functions for these Constructors.
 * 
 * I know this File causes some Errors, because of missing Main Functions, but if you just need to compile Stuff, then remove said erroreous Functions.
 */
public class GT_Recipe {
	
	public static boolean mDebug = true;
	
	/**
	 * If you want to remove Recipes, then set the Index to null, instead of removing the complete Entry!
	 * That's because I have a mapping for quick access, so you should also remove the Mapping of the Recipe.
	 */
	
	public static ArrayList<GT_Recipe> sFusionRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sCentrifugeRecipes	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sElectrolyzerRecipes = new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sGrinderRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sBlastRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sImplosionRecipes	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sSawmillRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sDieselFuels			= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sTurbineFuels		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sHotFuels			= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sDenseLiquidFuels	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sPlasmaFuels			= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sVacuumRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sChemicalRecipes		= new ArrayList<GT_Recipe>();
	
	public static HashMap<Long, Integer> pFusionRecipes			= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pCentrifugeRecipes		= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pElectrolyzerRecipes	= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pGrinderRecipes		= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pBlastRecipes			= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pImplosionRecipes		= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pSawmillRecipes		= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pDieselFuels			= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pTurbineFuels			= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pHotFuels				= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pDenseLiquidFuels		= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pPlasmaFuels			= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pVacuumRecipes			= new HashMap<Long, Integer>();
	public static HashMap<Long, Integer> pChemicalRecipes		= new HashMap<Long, Integer>();
	
	public final ItemStack mInput1, mInput2, mOutput1, mOutput2, mOutput3, mOutput4;
	public final int mDuration, mEUt, mStartEU;
	
	public static int stackToInt(ItemStack aStack) {
		if (aStack == null) return 0;
		return aStack.itemID | (((short)aStack.getItemDamage()) << 16);
	}
	
	public static ItemStack intToStack(int aStack) {
		if (aStack == 0) return null;
		return new ItemStack(aStack & 65535, 1, aStack >> 16);
	}
	
	public static long stacksToLong(ItemStack aStack1, ItemStack aStack2) {
		if (aStack1 == null) return 0;
		return ((long)stackToInt(aStack1)) | (((long)stackToInt(aStack2)) << 32);
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aStartEU, int aType) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = null;
		mOutput1  = aOutput1;
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = 0;
		mEUt      = 0;
		// That's EU per MilliBucket! If there is no Liquid for this Object, then it gets multiplied with 1000!
		mStartEU  = Math.max(1, aStartEU);
		
		if (mInput1 != null) {
			switch (aType) {
			// Diesel Generator
			case 0:
				pDieselFuels.put(stacksToLong(mInput1, mInput2), sDieselFuels.size());
				sDieselFuels.add(this);
				break;
			// Gas Turbine
			case 1:
				pTurbineFuels.put(stacksToLong(mInput1, mInput2), sTurbineFuels.size());
				sTurbineFuels.add(this);
				break;
			// Thermal Generator
			case 2:
				pHotFuels.put(stacksToLong(mInput1, mInput2), sHotFuels.size());
				sHotFuels.add(this);
				break;
			// Fluid Generator
			case 3:
				pDenseLiquidFuels.put(stacksToLong(mInput1, mInput2), sDenseLiquidFuels.size());
				sDenseLiquidFuels.add(this);
				break;
			// Plasma Generator
			case 4:
				pPlasmaFuels.put(stacksToLong(mInput1, mInput2), sPlasmaFuels.size());
				sPlasmaFuels.add(this);
				break;
			}
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, int aStartEU) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = Math.max(aDuration, 1);
		mEUt      = aEUt;
		mStartEU  = Math.max(Math.min(aStartEU, 100000000), 0);
		
		if (mInput1 != null && mInput2 != null && findEqualFusionRecipeIndex(mInput1, mInput2) == -1) {
			pFusionRecipes.put(stacksToLong(mInput1, mInput2), sFusionRecipes.size());
			sFusionRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = aOutput4==null?null:aOutput4.copy();
		mDuration = Math.max(aDuration, 1);
		mEUt      = 0;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualCentrifugeRecipeIndex(mInput1, mInput2) == -1) {
			pCentrifugeRecipes.put(stacksToLong(mInput1, mInput2), sCentrifugeRecipes.size());
			sCentrifugeRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = aOutput4==null?null:aOutput4.copy();
		mDuration = Math.max(aDuration, 1);
		mEUt      = aEUt>0?aEUt:1;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualElectrolyzerRecipeIndex(mInput1, mInput2) == -1) {
			pElectrolyzerRecipes.put(stacksToLong(mInput1, mInput2), sElectrolyzerRecipes.size());
			sElectrolyzerRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = null;
		mDuration = 200*(mInput1!=null?mInput1.stackSize:1);
		mEUt      = 32;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualSawmillRecipeIndex(mInput1, mInput2) == -1) {
			pSawmillRecipes.put(stacksToLong(mInput1, mInput2), sSawmillRecipes.size());
			sSawmillRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = aOutput4==null?null:aOutput4.copy();
		mDuration = 100*(mInput1!=null?mInput1.stackSize:1);
		mEUt      = 128;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualGrinderRecipeIndex(mInput1, mInput2) == -1) {
			pGrinderRecipes.put(stacksToLong(mInput1, mInput2), sGrinderRecipes.size());
			sGrinderRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = null;
		mOutput4  = null;
		mDuration = Math.max(aDuration, 1);
		mEUt      = aEUt>0?aEUt:1;
		mStartEU  = aLevel>0?aLevel:100;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualBlastRecipeIndex(mInput1, mInput2) == -1) {
			pBlastRecipes.put(stacksToLong(mInput1, mInput2), sBlastRecipes.size());
			sBlastRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
		mInput1   = aInput1==null?null:aInput1.copy();
		ItemStack tStack = Items.getItem("industrialTnt");
		if (tStack == null) tStack = new ItemStack(Block.tnt, 1);
		tStack.stackSize = (aInput2>0?aInput2<64?aInput2:64:1);
		mInput2   = tStack;
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = null;
		mOutput4  = null;
		mDuration = 20;
		mEUt      = 32;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualImplosionRecipeIndex(mInput1, mInput2) == -1) {
			pImplosionRecipes.put(stacksToLong(mInput1, mInput2), sImplosionRecipes.size());
			sImplosionRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = null;
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = 128;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualVacuumRecipeIndex(mInput1, mInput2) == -1) {
			pVacuumRecipes.put(stacksToLong(mInput1, mInput2), sVacuumRecipes.size());
			sVacuumRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = 32;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualChemicalRecipeIndex(mInput1, mInput2) == -1) {
			pChemicalRecipes.put(stacksToLong(mInput1, mInput2), sChemicalRecipes.size());
			sChemicalRecipes.add(this);
		}
	}
	
	public static int findEqualRecipeIndex(ItemStack aInput1, ItemStack aInput2, boolean aShapeless, ArrayList<GT_Recipe> aList, HashMap<Long, Integer> aHash) {
		if (aInput1 == null) return -1;
		long k = stacksToLong(aInput1, aInput2);
		int i = -1;
		if (aHash.containsKey(k))
			i = aHash.get(k);
		else
			if (aShapeless && aHash.containsKey(k = stacksToLong(aInput2, aInput1)))
				i = aHash.get(k);
			else
				if (aHash.containsKey(k = stacksToLong(aInput1, null)))
					i = aHash.get(k);
				else
					if (aShapeless && aHash.containsKey(k = stacksToLong(aInput2, null)))
						i = aHash.get(k);
		
		if (i >= 0 && i < aList.size() && isRecipeInputEqual(aShapeless, false, aInput1, aInput2, aList.get(i))) return i;
		
		boolean temp = false;
		for (i = 0; i < aList.size(); i++) {
			if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, aList.get(i))) {
				temp = true;
				break;
			}
		}
		
		if (temp && mDebug) {
			//GT_Log.out.println("Didn't find Recipe via Hashcode, did another Mod attempt to remove a Recipe improperly? Hash = " + k + " / " + aInput1.getItemName() + " / " + (aInput2==null?"NULL":aInput2.getItemName()));
			return i;
		}
		return -1;
	}
	
	public static int findEqualFusionRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sFusionRecipes, pFusionRecipes);
	}

	public static int findEqualCentrifugeRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sCentrifugeRecipes, pCentrifugeRecipes);
	}

	public static int findEqualElectrolyzerRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sElectrolyzerRecipes, pElectrolyzerRecipes);
	}

	public static int findEqualSawmillRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sSawmillRecipes, pSawmillRecipes);
	}
	
	public static int findEqualGrinderRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sGrinderRecipes, pGrinderRecipes);
	}
	
	public static int findEqualBlastRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sBlastRecipes, pBlastRecipes);
	}

	public static int findEqualImplosionRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sImplosionRecipes, pImplosionRecipes);
	}

	public static int findEqualVacuumRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sVacuumRecipes, pVacuumRecipes);
	}
	
	public static int findEqualChemicalRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sChemicalRecipes, pChemicalRecipes);
	}
	
	public static boolean isRecipeInputEqual(boolean aShapeless, boolean aDecreaseStacksizeBySuccess, ItemStack aInput1, ItemStack aInput2, GT_Recipe aRecipe) {
		if (aRecipe == null) return false;
		if (aShapeless) if (isRecipeInputEqual(false, aDecreaseStacksizeBySuccess, aInput2, aInput1, aRecipe)) return true;
		if (aInput1 == null)
			return false;
		else {
			if (aInput1.getItem() == aRecipe.mInput1.getItem() && (aInput1.getItemDamage() == aRecipe.mInput1.getItemDamage() || aRecipe.mInput1.getItemDamage() == -1) && aInput1.stackSize >= aRecipe.mInput1.stackSize) {
				if (aRecipe.mInput2 != null && (aInput2 == null || !(aInput2.getItem() == aRecipe.mInput2.getItem() && (aInput2.getItemDamage() == aRecipe.mInput2.getItemDamage() || aRecipe.mInput2.getItemDamage() == -1)) || aInput2.stackSize < aRecipe.mInput2.stackSize)) return false;
				if (aDecreaseStacksizeBySuccess) {
					aInput1.stackSize -= aRecipe.mInput1.stackSize;
					if (aRecipe.mInput2 != null) aInput2.stackSize -= aRecipe.mInput2.stackSize;
				}
				return true;
			}
		}
		return false;
	}
	
	public void checkCellBalance() {
		if (mInput1 == null) return;
		//int tInputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInput1) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInput2);
		//int tOutputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput1) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput2) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput3) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput4);
		
		/*if (tInputAmount < tOutputAmount) {
			GT_Log.out.println("You get more Cells, than you put in? There must be something wrong. " + mInput1.getItemName() + " / " + (mInput2==null?"NULL":mInput2.getItemName()));
		} else if (tInputAmount > tOutputAmount && !mInput1.isItemEqual(GT_ModHandler.getIC2Item("lavaCell", 1))) {
			GT_Log.out.println("You get less Cells, than you put in? My Machines usually don't destroy Cells. " + mInput1.getItemName() + " / " + (mInput2==null?"NULL":mInput2.getItemName()));
		}*/
	}
}