package mods.railcraft.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ICokeOvenRecipe
{

    public int getCookTime();

    public ItemStack getInput();

    public LiquidStack getLiquidOutput();

    public ItemStack getOutput();
}
