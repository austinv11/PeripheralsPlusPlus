package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockDummyBlock extends BlockPPP {

	private String[] locations = new String[]{"peripheralsplusplus:feederUpgrade", "peripheralsplusplus:tank", "peripheralsplusplus:signUpgrade", "peripheralsplusplus:gardenerUpgrade", "peripheralsplusplus:ridableUpgrade"};
	private IIcon[] icons;

	public BlockDummyBlock() {
		super();
		this.setBlockName("dummyBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){
		icons = new IIcon[locations.length];
		for (int i = 0; i < locations.length; i++)
			icons[i] = iconRegister.registerIcon(locations[i]);
	}
}
