package miscperipherals.entity;

import miscperipherals.api.IDataCart;
import miscperipherals.core.MiscPeripherals;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

public class EntityMinecartData extends EntityMinecart implements IDataCart {
	private String data = "";
	
	public EntityMinecartData(World world) {
		super(world);
	}

	@Override
	public int getMinecartType() {
		return 0;
	}
	
	@Override
	public boolean interact(EntityPlayer player) {
		if (!worldObj.isRemote) {
			ItemStack stack = player.getHeldItem();
			if (stack != null && (stack.itemID == Item.writtenBook.itemID || stack.itemID == Item.writableBook.itemID)) {
				String toSet = "";
				
				if (stack.hasTagCompound()) {
					NBTTagList pages = stack.stackTagCompound.getTagList("pages");
					if (pages.tagCount() > 0) {
						NBTTagString string = (NBTTagString) pages.tagAt(0);
						toSet = string.data;
					}
				}
				
				data = toSet;
				player.sendChatToPlayer("Read data from book page 1");
			}
		}
		
		return super.interact(player);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		
		data = compound.getString("data");
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		
		compound.setString("data", data);
	}

	@Override
	public void setData(Object[] data) throws Throwable {
		if (data.length < 1) throw new Exception("too few arguments");
		else if (!(data[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
		
		String toSet = (String) data[0];
		if (toSet.length() > 1024) toSet.substring(0, 1024);
		
		this.data = toSet;
	}

	@Override
	public Object[] getData() {
		return new Object[] {data};
	}
	
	@Override
	public Block getDefaultDisplayTile() {
		return MiscPeripherals.instance.blockAlpha;
	}
	
	@Override
	public int getDefaultDisplayTileData() {
		return -1;
	}
	
	@Override
	public ItemStack getCartItem() {
		return new ItemStack(MiscPeripherals.instance.itemCart, 1, 0);
	}
}
