package miscperipherals.upgrade;

import java.util.ArrayList;

import miscperipherals.safe.Reflector;
import miscperipherals.util.FakePlayer;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class UpgradeWand extends UpgradeTool {
	@Override
	public int getUpgradeID() {
		return 244;
	}

	@Override
	public String getAdjective() {
		return "Wand";
	}

	@Override
	public ItemStack getCraftingItem() {
		return ItemApi.getItem("itemWandCastingAdept", OreDictionary.WILDCARD_VALUE);
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return getCraftingItem().getIconIndex();
	}
	
	@Override
	public boolean attack(ITurtleAccess turtle, Entity ent) {
		if (!(ent instanceof EntityLiving)) return false;
		
		World world = turtle.getWorld();
		FakePlayer player = FakePlayer.get(world);
		player.alignToTurtle(turtle);
		
		ItemStack item = getCraftingItem();
		Integer maxvisr = Reflector.invoke(item.getItem(), "getMaxVis", Integer.class);
		if (maxvisr == null) return false;
		int maxvis = maxvisr;
		item.stackTagCompound = new NBTTagCompound();
		item.stackTagCompound.setShort("vis", (short)maxvis);
		
		player.setHeldItem(item);
		
		Vec3 pos = turtle.getPosition();
		if (ThaumcraftApi.decreaseClosestAura(world, pos.xCoord + 0.5D, pos.yCoord + 0.5D, pos.zCoord + 0.5D, 1, false)) {
			boolean ret = item.getItem().itemInteractionForEntity(item, (EntityLiving)ent);
			
			int dec = maxvis - item.stackTagCompound.getShort("vis");
			if (dec > 0) ThaumcraftApi.decreaseClosestAura(turtle.getWorld(), pos.xCoord + 0.5D, pos.yCoord + 0.5D, pos.zCoord + 0.5D, dec, true);
			
			if (player.getHeldItem() == null) {
				for (TurtleSide tside : Util.TURTLE_SIDES) {
					if (turtle.getUpgrade(tside) == this) {
						Util.setTurtleUpgrade(turtle, tside, null);
						break;
					}
				}
			}
			return ret;
		} else return false;
	}
	
	@Override
	public boolean canDig(ITurtleAccess turtle, int x, int y, int z, int side) {
		return true;
	}
	
	@Override
	public Iterable<ItemStack> getBlockDrops(ITurtleAccess turtle, int x, int y, int z, int side) {
		return new ArrayList<ItemStack>(0);
	}
	
	@Override
	public boolean dig(ITurtleAccess turtle, int x, int y, int z, int side) {
		World world = turtle.getWorld();
		FakePlayer player = FakePlayer.get(world);
		ItemStack item = getCraftingItem();
		
		Integer maxvisr = Reflector.invoke(item.getItem(), "getMaxVis", Integer.class);
		if (maxvisr == null) return false;
		int maxvis = maxvisr;
		item.stackTagCompound = new NBTTagCompound();
		item.stackTagCompound.setShort("vis", (short)maxvis);
		
		player.setHeldItem(item);
		player.alignToTurtle(turtle, Util.OPPOSITE[side]);
		int id = world.getBlockId(x, y, z);
		if (Block.blocksList[id] != null && ThaumcraftApi.decreaseClosestAura(world, x + 0.5D, y + 0.5D, z + 0.5D, 1, false)) {
			boolean ret = item.getItem().onItemUseFirst(item, player, world, x, y, z, side, 0.0F, 0.0F, 0.0F) && ThaumcraftApi.decreaseClosestAura(world, x + 0.5D, y + 0.5D, z + 0.5D, 1, true);
			
			int dec = maxvis - item.stackTagCompound.getShort("vis");
			if (dec > 0) ThaumcraftApi.decreaseClosestAura(world, x + 0.5D, y + 0.5D, z + 0.5D, dec, true);
			
			if (player.getHeldItem() == null) {
				for (TurtleSide tside : Util.TURTLE_SIDES) {
					if (turtle.getUpgrade(tside) == this) {
						Util.setTurtleUpgrade(turtle, tside, null);
						break;
					}
				}
			}
			return ret;
		} else return false;
	}
}
