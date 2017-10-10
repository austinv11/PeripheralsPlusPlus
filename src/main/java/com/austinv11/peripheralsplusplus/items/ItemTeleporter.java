package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.blocks.BlockTeleporter;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemTeleporter extends ItemBlock {
    public ItemTeleporter(BlockTeleporter teleporter) {
        super(teleporter);
        this.setRegistryName(teleporter.getRegistryName());
        this.setUnlocalizedName(teleporter.getUnlocalizedName());
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {
        return String.format("%s.tier.%d", getUnlocalizedName(), item.getMetadata());
    }
}
