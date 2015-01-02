package miscperipherals.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import miscperipherals.core.MiscPeripherals;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;

public class ItemMulti extends Item {
	private List<Integer> metas = new ArrayList<Integer>();
	private Map<Integer, String> spriteName = new HashMap<Integer, String>();
	private Map<Integer, Icon> sprite = new HashMap<Integer, Icon>();
	private Map<Integer, String> name = new HashMap<Integer, String>();
	
	public ItemMulti(int id) {
		super(id);
		
		setCreativeTab(MiscPeripherals.instance.tabMiscPeripherals);
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		Icon icon = sprite.get(meta);
		return icon != null ? icon : Item.brick.getIconFromDamage(0);
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		return StatCollector.translateToLocal("miscperipherals."+name.get(stack.getItemDamage())+".name");
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list) {
		for (int i = 0; i < 16; i++) {
			if (metas.contains(i)) list.add(new ItemStack(id, 1, i));
		}
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		for (Entry<Integer, String> entry : spriteName.entrySet()) {
			sprite.put(entry.getKey(), reg.registerIcon("MiscPeripherals:"+entry.getValue()));
		}
	}
	
	public void registerItem(int meta, String sprite, String name) {
		if (metas.contains(meta)) throw new RuntimeException("Metadata value "+meta+" already used by "+this.name.get(meta)+" when adding "+name);
		
		metas.add(meta);
		this.spriteName.put(meta, sprite);
		this.name.put(meta, name);
	}

	public int findFirstUsedMeta() {
		if (metas.isEmpty()) return -1;
		else return metas.get(0);
	}
}
