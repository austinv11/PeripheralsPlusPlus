package com.austinv11.peripheralsplusplus.items;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemBlockTurtle extends ItemBlock {
	
	private Random rng = new Random();
	
	public ItemBlockTurtle(Block block) {
		super(block);
		setRegistryName(ModBlocks.TURTLE.getRegistryName());
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (NBTHelper.hasTag(item, "desc")) {
            String description = I18n.translateToLocal("peripheralsplusplus.description.turtle." +
                    NBTHelper.getInt(item, "desc"));
            tooltip.add(description);
        } else {
            NBTHelper.setInteger(item, "desc", MathHelper.getInt(rng, 1, 10));
            addInformation(item, world, tooltip, flag);
        }
    }
}
