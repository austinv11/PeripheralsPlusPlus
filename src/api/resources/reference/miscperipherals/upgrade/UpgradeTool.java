package miscperipherals.upgrade;

import java.util.ArrayList;

import miscperipherals.core.EventHandler;
import miscperipherals.core.EventHandler.DropConsumer;
import miscperipherals.util.FakePlayer;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public abstract class UpgradeTool implements ITurtleUpgrade {
	@Override
	public abstract int getUpgradeID();

	@Override
	public abstract String getAdjective();

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public abstract ItemStack getCraftingItem();

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public abstract Icon getIcon(ITurtleAccess turtle, TurtleSide side);

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		World world = turtle.getWorld();
		
		switch (verb) {
			case Attack: {
				Vec3 pos = turtle.getPosition();
				FakePlayer player = FakePlayer.get(turtle);
				player.alignToTurtle(turtle, direction);
				player.setHeldItem(getCraftingItem());
				MovingObjectPosition mop = Util.rayTrace(player, 1.5D);
				if (mop == null || mop.typeOfHit != EnumMovingObjectType.ENTITY) return false;
				
				if (!canAttack(turtle, mop.entityHit)) return false;
				
				return attack(turtle, mop.entityHit);
			}
			case Dig: {
				MovingObjectPosition mop = Util.rayTraceBlock(turtle, direction);
				
				int id = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
				int meta = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
				if (Block.blocksList[id] == null || world.isAirBlock(mop.blockX, mop.blockY, mop.blockZ)) return false;
				
				if (!canDig(turtle, mop.blockX, mop.blockY, mop.blockZ, Util.OPPOSITE[direction])) return false;
				
				for (ItemStack stack : getBlockDrops(turtle, mop.blockX, mop.blockY, mop.blockZ, Util.OPPOSITE[direction])) {
					Util.storeOrDrop(turtle, stack);
				}
				
				return dig(turtle, mop.blockX, mop.blockY, mop.blockZ, Util.OPPOSITE[direction]);
			}
		}
		
		return false;
	}
	
	///// OVERRIDABLES /////
	
	public boolean canAttack(ITurtleAccess turtle, Entity ent) {
		return true;
	}
	
	public boolean attack(final ITurtleAccess turtle, Entity ent) {
		EntityLiving living = null;
		if (ent instanceof EntityLiving) living = (EntityLiving)ent;
		
		if (living != null) EventHandler.dropConsumers.put(living, new DropConsumer() {
			@Override
			public void onDrops(DamageSource source, ArrayList<EntityItem> drops, int lootingLevel, boolean recentlyHit, int specialDropValue) {
				for (EntityItem drop : drops) {
					Util.storeOrDrop(turtle, drop.getEntityItem());
				}
				drops.clear();
			}
		});
		boolean ret = ent.attackEntityFrom(DamageSource.causeMobDamage(new EntityLiving(turtle.getWorld()) {
			@Override
			public String getEntityName() {
				return "ComputerCraft";
			}
			
			@Override
			public void entityInit() {
				
			}

			@Override
			public void readEntityFromNBT(NBTTagCompound var1) {
				
			}

			@Override
			public void writeEntityToNBT(NBTTagCompound var1) {
				
			}

			@Override
			public int getMaxHealth() {
				return 0;
			}
		}), getDamage(turtle, ent));
		if (living != null && living.getHealth() > 0) EventHandler.dropConsumers.remove(living);
		return ret;
	}
	
	public int getDamage(ITurtleAccess turtle, Entity ent) {
		return getCraftingItem().getDamageVsEntity(ent);
	}
	
	public boolean canDig(ITurtleAccess turtle, int x, int y, int z, int side) {
		ItemStack item = getCraftingItem();
		if (item != null) {
			int id = turtle.getWorld().getBlockId(x, y, z);
			if (Block.blocksList[id] != null) return ForgeHooks.isToolEffective(item, Block.blocksList[id], turtle.getWorld().getBlockMetadata(x, y, z));
		}
		
		return false;
	}
	
	public Iterable<ItemStack> getBlockDrops(ITurtleAccess turtle, int x, int y, int z, int side) {
		World world = turtle.getWorld();
		int id = world.getBlockId(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		if (Block.blocksList[id] == null || !ForgeHooks.isToolEffective(getCraftingItem(), Block.blocksList[id], meta)) return new ArrayList<ItemStack>(0);
		return Block.blocksList[id].getBlockDropped(world, x, y, z, meta, 0);
	}
	
	public boolean dig(ITurtleAccess turtle, int x, int y, int z, int side) {
		World world = turtle.getWorld();
		int id = world.getBlockId(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		world.playAuxSFX(2001, x, y, z, id + meta * Block.blocksList.length);
		world.setBlock(x, y, z, 0, 0, 2);
		return true;
	}
}
