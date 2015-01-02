package miscperipherals.util;

import java.util.WeakHashMap;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.tile.Tile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import dan200.turtle.api.ITurtleAccess;

/**
 * RG for public source release: This code started crashing randomly around 1.6 but I never bothered with fixing it.
 */
public class FakePlayer extends EntityPlayer {
	private static WeakHashMap<Object, FakePlayer> players = new WeakHashMap<Object, FakePlayer>();
	private int selected = 0;
	
	public FakePlayer(World world) {
		super(world);
		
		username = "ComputerCraft";
	}

	@Override
	public void sendChatToPlayer(String var1) {
		
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates((int)Math.floor(posX), (int)Math.floor(posY), (int)Math.floor(posZ));
	}
	
	@Override
	public ItemStack getCurrentEquippedItem() {
		return inventory.mainInventory[selected];
	}
	
	/**
	 * AT
	 */
	@Override
	public boolean getFlag(int flag) {
		return super.getFlag(flag);
	}
	
	/**
	 * AT
	 */
	@Override
	public void setFlag(int flag, boolean value) {
		super.setFlag(flag, value);
	}
	
	/**
	 * Nope.
	 */
	@Override
	public void renderBrokenItemStack(ItemStack stack) {
		
	}
	
	public void alignToTurtle(ITurtleAccess turtle, int direction) {
		if (turtle.getWorld() != worldObj) MiscPeripherals.log.warning("Turtle "+turtle+" requested a fakeplayer but the world mismatches, unexpected things may happen! (t="+turtle.getWorld()+" p="+worldObj+")");
		
		//username = "ComputerCraft$"+(turtle.toString().split("@")[1]);
		setFlag(1, false);
		
		Vec3 pos = turtle.getPosition();
		if (pos != null) {
			// RG for public source release: If you tweak these values and firing a mining laser turtle breaks the turtle, you're doing it wrong.
			
			// ??
			//setPositionAndRotation(pos.xCoord + 0.3D + (1.0D * Facing.offsetsXForSide[direction]), pos.yCoord, pos.zCoord + 0.3D + (1.0D * Facing.offsetsZForSide[direction]), Util.YAW[direction], Util.PITCH[direction]);
			
			// 2.3
			//setPositionAndRotation(pos.xCoord + 0.1D + (1.0D * Facing.offsetsXForSide[direction]), pos.yCoord + 0.5D + (1.0D * Facing.offsetsYForSide[direction]), pos.zCoord + 0.1D + (1.0D * Facing.offsetsZForSide[direction]), Util.YAW[direction], Util.PITCH[direction]);
			
			setPositionAndRotation(pos.xCoord + 0.5D + (0.48D * Facing.offsetsXForSide[direction]), pos.yCoord + 0.5D + (0.48D * Facing.offsetsYForSide[direction]), pos.zCoord + 0.5D + (0.48D * Facing.offsetsZForSide[direction]), Util.YAW[direction], Util.PITCH[direction]);
		} else MiscPeripherals.log.warning("Turtle "+turtle+" requested a fakeplayer but its position is null!");
		
		selected = turtle.getSelectedSlot();
		inventory = new InventoryPlayer(this);
		inventory.mainInventory[0] = new ItemStack(Item.stick);
		for (int i = 1; i <= turtle.getInventorySize(); i++) {
			inventory.mainInventory[i] = turtle.getSlotContents(i - 1);
		}
		for (int i = turtle.getInventorySize() + 1; i < inventory.mainInventory.length; i++) {
			inventory.mainInventory[i] = new ItemStack(Item.stick);
		}
	}
	
	public void alignToTurtle(ITurtleAccess turtle) {
		alignToTurtle(turtle, turtle.getFacingDir());
	}
	
	public void postUse(ITurtleAccess turtle) {
		for (int i = 1; i < inventory.mainInventory.length; i++) {
			if (i <= turtle.getInventorySize()) {
				turtle.setSlotContents(i - 1, inventory.mainInventory[i]);
			} else {
				Util.storeOrDrop(turtle, inventory.mainInventory[i]);
			}
		}
		inventory = new InventoryPlayer(this);
	}
	
	public void setHeldItem(ItemStack stack) {
		selected = 0;
		inventory.mainInventory[0] = stack;
	}
	
	public void alignToTile(Tile tile) {
		setPositionAndRotation(tile.xCoord + 0.5D, tile.yCoord + 0.5D, tile.zCoord + 0.5D, Util.YAW[tile.getFacing()], Util.PITCH[tile.getFacing()]);
	}
	
	public void alignToInventory(IInventory inv) {
		inventory = new InventoryPlayer(this);
		inventory.mainInventory[0] = new ItemStack(Item.stick);
		for (int i = 1; i <= inv.getSizeInventory(); i++) {
			inventory.mainInventory[i] = inv.getStackInSlot(i - 1);
		}
		for (int i = inv.getSizeInventory() + 1; i < inventory.mainInventory.length; i++) {
			inventory.mainInventory[i] = new ItemStack(Item.stick);
		}
	}
	
	public void realignInventory(IInventory inv) {
		for (int i = 1; i <= inv.getSizeInventory(); i++) {
			inv.setInventorySlotContents(i - 1, inventory.mainInventory[i]);
		}
	}
	
	public static FakePlayer get(Object world) {
		if (players.containsKey(world)) {
			return players.get(world);
		} else {
			FakePlayer ret = new FakePlayer(world instanceof World ? (World)world : (world instanceof ITurtleAccess ? ((ITurtleAccess)world).getWorld() : DimensionManager.getWorld(0)));
			players.put(world, ret);
			return ret;
		}
	}
}
