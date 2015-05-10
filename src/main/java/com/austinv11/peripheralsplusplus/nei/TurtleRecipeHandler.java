package com.austinv11.peripheralsplusplus.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftNotFoundException;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftRegistry;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurtleRecipeHandler extends TemplateRecipeHandler {
	
	private static final ResourceLocation TWO_INGREDIENTS = new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/gui/turtleNEI.png");
	private static final ResourceLocation THREE_INGREDIENTS = new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/gui/turtleNEI2.png");
	
	public TurtleRecipeHandler() {
		super();
	}
	
	@Override
	public String getGuiTexture() {
		return "null";
	}
	
	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("peripheralsplusplus.nei.turtleRecipeHandler.name");
	}
	
	@Override
	public String getOverlayIdentifier(){
		return "turtle";
	}
	
	@Override
	public int recipiesPerPage() {
		return 2;
	}
	
	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(((CachedTurtleRecipe)arecipes.get(recipe)).hasThreeIngredients ? THREE_INGREDIENTS : TWO_INGREDIENTS);
		GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 65);
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if (isItemStackTurtle(result)) {
			if (NBTHelper.hasTag(result, "leftUpgrade") || NBTHelper.hasTag(result, "rightUpgrade")) {
				Map<Integer, ITurtleUpgrade> upgrades = new HashMap<Integer, ITurtleUpgrade>();
				try {
					upgrades = ComputerCraftRegistry.getTurtleUpgrades();
				} catch (ComputerCraftNotFoundException e) {
					e.printStackTrace();
				}
				if (NBTHelper.hasTag(result, "leftUpgrade") && NBTHelper.hasTag(result, "rightUpgrade")) {
					ITurtleUpgrade upgrade1 = upgrades.get((int)NBTHelper.getShort(result, "leftUpgrade"));
					ITurtleUpgrade upgrade2 = upgrades.get((int)NBTHelper.getShort(result, "rightUpgrade"));
					this.arecipes.add(new CachedTurtleRecipe(upgrade1, upgrade2, isAdvanced(result)));
				} else {
					boolean isLeft = NBTHelper.hasTag(result, "leftUpgrade");
					ITurtleUpgrade upgrade = upgrades.get((int)NBTHelper.getShort(result, isLeft ? "leftUpgrade" : "rightUpgrade"));
					if (upgrade != null)
						this.arecipes.add(new CachedTurtleRecipe(upgrade, isAdvanced(result), isLeft));
				}
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if (isItemStackTurtle(ingredient)) {
			try {
				for (ITurtleUpgrade upgrade : ComputerCraftRegistry.getTurtleUpgrades().values()) {
					if (upgrade != null)
						this.arecipes.add(new CachedTurtleRecipe(upgrade, isAdvanced(ingredient), true));
				}
			} catch (ComputerCraftNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			ITurtleUpgrade upgrade = null;
			try {
				upgrade = getUpgradeForIngredient(ingredient);
			} catch (ComputerCraftNotFoundException e) {
				e.printStackTrace();
			}
			if (upgrade != null) {
				this.arecipes.add(new CachedTurtleRecipe(upgrade, true));
			}
		}
	}
	
	private ITurtleUpgrade getUpgradeForIngredient(ItemStack ingredient) throws ComputerCraftNotFoundException {
		Map<Integer, ITurtleUpgrade> upgrades = ComputerCraftRegistry.getTurtleUpgrades();
		for (ITurtleUpgrade upgrade : upgrades.values())
			if (upgrade.getCraftingItem().isItemEqual(ingredient))
				return upgrade;
		return null;
	}
	
	private boolean isItemStackTurtle(ItemStack stack) {
		return TurtleUtil.getTurtle(true).isItemEqual(stack) || TurtleUtil.getTurtle(false).isItemEqual(stack) ||
				new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-TurtleExpanded")).isItemEqual(stack);
	}
	
	private boolean isAdvanced(ItemStack stack) {
		return stack.isItemEqual(TurtleUtil.getTurtle(true));
	}
	
	public class CachedTurtleRecipe extends CachedRecipe { //FIXME: Item permutations
		
		public boolean hasThreeIngredients = false;
		
		private boolean genPerm = false;
		private List<PositionedStack> inputs = new ArrayList<PositionedStack>();
		private PositionedStack result;
		private final int[] slot_0 = new int[]{19, 12};
		private final int[] slot_1 = new int[]{68, 12};
		private final int[] slot_2 = new int[]{126, 12};
		private final int[] slot2_0 = new int[]{8, 12};
		private final int[] slot2_1 = new int[]{47, 12};
		private final int[] slot2_2 = new int[]{86, 12};
		private final int[] slot2_3 = new int[]{134, 12};
		
		public CachedTurtleRecipe(ITurtleUpgrade upgrade, boolean isLeft) {
			genPerm = true;
			if (!isLeft) {
				inputs.add(new PositionedStack(upgrade.getCraftingItem(), slot_0[0], slot_0[1]));
				inputs.add(new PositionedStack(new ItemStack[]{TurtleUtil.getTurtle(true), TurtleUtil.getTurtle(false)}, slot_1[0], slot_1[1]));
				result = new PositionedStack(new ItemStack[]{TurtleUtil.getTurtle(true, null, upgrade), TurtleUtil.getTurtle(false, null, upgrade)}, slot_2[0], slot_2[1]);
			} else {
				inputs.add(new PositionedStack(upgrade.getCraftingItem(), slot_1[0], slot_1[1]));
				inputs.add(new PositionedStack(new ItemStack[]{TurtleUtil.getTurtle(true), TurtleUtil.getTurtle(false)}, slot_0[0], slot_0[1]));
				result = new PositionedStack(new ItemStack[]{TurtleUtil.getTurtle(true, upgrade), TurtleUtil.getTurtle(false, upgrade)}, slot_2[0], slot_2[1]);
			}
		}
		
		public CachedTurtleRecipe(ITurtleUpgrade upgrade, boolean isAdvanced, boolean isLeft) {
			if (!isLeft) {
				inputs.add(new PositionedStack(upgrade.getCraftingItem(), slot_0[0], slot_0[1]));
				inputs.add(new PositionedStack(TurtleUtil.getTurtle(isAdvanced), slot_1[0], slot_1[1]));
				result = new PositionedStack(TurtleUtil.getTurtle(isAdvanced, null, upgrade), slot_2[0], slot_2[1]);
			} else {
				inputs.add(new PositionedStack(upgrade.getCraftingItem(), slot_1[0], slot_1[1]));
				inputs.add(new PositionedStack(TurtleUtil.getTurtle(isAdvanced), slot_0[0], slot_0[1]));
				result = new PositionedStack(TurtleUtil.getTurtle(isAdvanced, upgrade), slot_2[0], slot_2[1]);
			}
		}
		
		public CachedTurtleRecipe(ITurtleUpgrade upgrade1, ITurtleUpgrade upgrade2, boolean isAdvanced) {
			hasThreeIngredients = true;
			inputs.add(new PositionedStack(upgrade2.getCraftingItem(), slot2_0[0], slot2_0[1]));
			inputs.add(new PositionedStack(TurtleUtil.getTurtle(isAdvanced), slot2_1[0], slot2_1[1]));
			inputs.add(new PositionedStack(upgrade1.getCraftingItem(), slot2_2[0], slot2_2[1]));
			result = new PositionedStack(TurtleUtil.getTurtle(isAdvanced, upgrade1, upgrade2), slot2_3[0], slot2_3[1]);
		}
		
		@Override
		public PositionedStack getResult() {
			if (genPerm)
				result.generatePermutations();
			return this.result;
		}
		
		@Override
		public List<PositionedStack> getIngredients() {
			if (genPerm)
				for (PositionedStack stack : inputs)
					stack.generatePermutations();
			return this.inputs;
		}
	}
}
