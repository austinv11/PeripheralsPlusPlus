package com.austinv11.peripheralsplusplus.nei;

import com.austinv11.peripheralsplusplus.init.ModPeripherals;
import com.austinv11.peripheralsplusplus.nei.recipe.RecipePocketUpgrade;
import com.austinv11.peripheralsplusplus.nei.recipe.RecipeTurtleUpgrade;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import java.util.ArrayList;

@JEIPlugin
public class JeiPlugin implements IModPlugin {
    public JeiPlugin() {
        super();
    }

    @Override
    public void register(IModRegistry registry) {
        // Turtle upgrades
        ArrayList<IRecipeWrapper> recipes = new ArrayList<>();
        for (ITurtleUpgrade upgrade : ModPeripherals.TURTLE_UPGRADES) {
            for (int side = 0; side < 2; side++) {
                recipes.add(new RecipeTurtleUpgrade(upgrade, true, side == 0));
                recipes.add(new RecipeTurtleUpgrade(upgrade, false, side == 0));
            }
        }
        for (IPocketUpgrade pocketUpgrade : ModPeripherals.POCKET_UPGRADES) {
            recipes.add(new RecipePocketUpgrade(pocketUpgrade, true));
            recipes.add(new RecipePocketUpgrade(pocketUpgrade, false));
        }
        registry.addRecipes(recipes, VanillaRecipeCategoryUid.CRAFTING);
    }
}
