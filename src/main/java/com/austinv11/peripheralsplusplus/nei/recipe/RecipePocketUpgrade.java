package com.austinv11.peripheralsplusplus.nei.recipe;

import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipePocketUpgrade implements IRecipeWrapper {
    private final IPocketUpgrade pocketUpgrade;
    private final boolean advanced;

    public RecipePocketUpgrade(IPocketUpgrade pocketUpgrade, boolean advanced) {
        this.pocketUpgrade = pocketUpgrade;
        this.advanced = advanced;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(ItemStack.EMPTY);
        inputs.add(pocketUpgrade.getCraftingItem());
        for (int emptySlot = 0; emptySlot < 2; emptySlot++)
            inputs.add(ItemStack.EMPTY);
        inputs.add(TurtleUtil.getPocket(advanced));
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, TurtleUtil.getPocket(advanced, pocketUpgrade));
    }
}
