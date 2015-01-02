package miscperipherals.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Objects;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.entity.EntityMinecartData;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemCart extends Item {
	private List<Integer> metas = new ArrayList<Integer>();
	private Map<Integer, CartData> data = new HashMap<Integer, CartData>();
	
	public ItemCart(int id) {
		super(id);
		
		setCreativeTab(MiscPeripherals.instance.tabMiscPeripherals);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (BlockRailBase.isRailBlock(world.getBlockId(x, y, z))) {
			if (!world.isRemote) {
				EntityMinecart cart = getMinecart(stack, player, world);
				cart.setPosition(x + 0.5D, y + 0.5D, z + 0.5D);
				
				if (stack.hasDisplayName()) {
					cart.func_96094_a(stack.getDisplayName());
				}

				world.spawnEntityInWorld(cart);
				stack.stackSize--;
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		CartData cd = data.get(meta);
		return cd != null && cd.sprite != null ? cd.sprite : Item.brick.getIconFromDamage(0);
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		int meta = stack.getItemDamage();
		if (data.get(meta) == null) return "[Invalid Item]";
		else return StatCollector.translateToLocal("miscperipherals." + data.get(meta).name + ".name");
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list) {
		for (int i = 0; i < 16; i++) {
			if (metas.contains(i)) list.add(new ItemStack(id, 1, i));
		}
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		for (Entry<Integer, CartData> entry : data.entrySet()) {
			entry.getValue().sprite = reg.registerIcon("MiscPeripherals:" + entry.getValue().spriteName);
		}
	}
	
	protected EntityMinecart getMinecart(ItemStack stack, EntityPlayer player, World world) {
		switch (stack.getItemDamage()) {
			case 0: return new EntityMinecartData(world);
		}
		
		return null;
	}
	
	public CartData registerCart(int meta) {
		if (metas.contains(meta)) throw new RuntimeException("Metadata value " +meta + " already used by " + data.get(meta));
		
		CartData cd = new CartData(meta);
		data.put(meta, cd);
		metas.add(meta);
		return cd;
	}
	
	public static class CartData {
		public final int meta;
		public Class<? extends EntityMinecart> clazz;
		public Icon sprite;
		public String name;
		
		private String spriteName;
		
		public CartData(int meta) {
			this.meta = meta;
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("name", name).add("meta", meta).add("clazz", clazz).toString();
		}
		
		public CartData setClass(Class<? extends EntityMinecart> clazz) {
			this.clazz = clazz;
			return this;
		}
		
		public CartData setSprite(String sprite) {
			spriteName = sprite;
			return this;
		}
		
		public CartData setName(String name) {
			this.name = name;
			return this;
		}
	}
}
