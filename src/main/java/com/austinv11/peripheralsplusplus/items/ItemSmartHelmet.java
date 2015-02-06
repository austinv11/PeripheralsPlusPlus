package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.client.models.ModelSmartHelmet;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

public class ItemSmartHelmet extends ItemArmor implements ISpecialArmor {
	
	public ItemSmartHelmet(ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		this.setCreativeTab(CreativeTabPPP.PPP_TAB);
		this.setUnlocalizedName("smartHelmet");
	}

	public ItemSmartHelmet() {
		this(ArmorMaterial.IRON, RenderingRegistry.addNewArmourRendererPrefix("smartHelmet"), 0);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() instanceof ItemSmartHelmet){
			return Reference.MOD_ID.toLowerCase()+":textures/models/armor/smartHelmet.png";
		}
		return super.getArmorTexture(itemstack, entity, slot, type);
	}

	@Override
	public String getUnlocalizedName(){//Formats the name
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase()+":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	@Override
	public String getUnlocalizedName(ItemStack item){//Formats the name
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase()+":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister){//Sets the icon
		itemIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName){//Removes the "item." from the item name
		return unlocalizedName.substring(unlocalizedName.indexOf(".")+1);
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return new ArmorProperties(0,0,0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		if (itemStack.getItem() instanceof ItemSmartHelmet)
			return new ModelSmartHelmet();
		return super.getArmorModel(entityLiving, itemStack, armorSlot);
	}
}
