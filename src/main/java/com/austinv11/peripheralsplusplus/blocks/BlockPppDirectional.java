package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPppDirectional extends BlockDirectional implements BlockPpp {
    public BlockPppDirectional(Material material) {
        super(material);
        this.setHardness(4.0F);
        if (this.getTab() != null)
            this.setCreativeTab(this.getTab());

    }

    public BlockPppDirectional() {
        this(Material.ROCK);
    }

    public String getUnlocalizedName() {
        return String.format("tile.%s%s", this.getModId().toLowerCase() + ":",
                this.getUnwrappedUnlocalizedName(this.getUnwrappedUnlocalizedName(super.getUnlocalizedName())));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public CreativeTabs getTab() {
        return CreativeTabPPP.PPP_TAB;
    }

    @Override
    public String getModId() {
        return Reference.MOD_ID;
    }
}
