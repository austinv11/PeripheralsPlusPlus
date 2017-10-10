package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.client.models.ModelSmartHelmet;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemSmartHelmet extends ItemArmor implements ISpecialArmor {
	
	public ItemSmartHelmet(ArmorMaterial material, int renderIndex, EntityEquipmentSlot armorType) {
		super(material, renderIndex, armorType);
		this.setCreativeTab(CreativeTabPPP.PPP_TAB);
		this.setUnlocalizedName("smart_helmet");
		this.setRegistryName(Reference.MOD_ID, "smart_helmet");
	}

	public ItemSmartHelmet() {
	    this(ArmorMaterial.IRON, 0, EntityEquipmentSlot.HEAD);
	}

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (stack.getItem() instanceof ItemSmartHelmet){
			return Reference.MOD_ID.toLowerCase()+":textures/models/armor/smart_helmet.png";
		}
		return super.getArmorTexture(stack, entity, slot, type);
	}

	@Override
	public String getUnlocalizedName(){//Formats the name
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase()+":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	@Override
	public String getUnlocalizedName(ItemStack item){//Formats the name
		return String.format("item.%s%s", Reference.MOD_ID.toLowerCase()+":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
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
	@Nullable
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (itemStack.getItem() instanceof ItemSmartHelmet)
			return new ModelSmartHelmet();
		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}
}
