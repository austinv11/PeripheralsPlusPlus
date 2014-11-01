package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Logger;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.util.List;
import java.util.Random;

public class PeripheralXP implements IPeripheral {//Beware, a lot of the math was taken from the mind of RichardG (meaning, I don't really understand all of it)

	private static final int MAX_LEVEL = 30;
	private static final double COLLECT_RANGE = 2.0D;
	private final BetterRandom random = new BetterRandom();
	private int experience = 0;
	private int experienceRemainder = 0;
	private int experienceLevel = 0;
	private boolean autoCollect = false;
	private int ticker = random.nextInt(20);
	public boolean changed = false;
	private ITurtleAccess turtle;
	private TurtleSide side;

	public PeripheralXP(ITurtleAccess turtleAccess, TurtleSide side) {
		turtle = turtleAccess;
		this.side = side;
		NBTTagCompound tag = turtle.getUpgradeNBTData(side);
		experience = tag.getInteger("experience");
		experienceRemainder = tag.getInteger("experienceRemainder");
		experienceLevel = tag.getInteger("experienceLevel");
		random.setSeed(tag.getLong("rndSeed"));
	}

	public void update() {
		if (autoCollect && ++ticker >= 20) {
			ticker = 0;
			addExperience(collect());
			changed = true;
		}
		if (changed) {
			NBTTagCompound tag = turtle.getUpgradeNBTData(side);
			tag.setInteger("experience", experience);
			tag.setInteger("experienceRemainder", experienceRemainder);
			tag.setInteger("experienceLevel", experienceLevel);
			tag.setLong("rndSeed", random.getSeed());
			turtle.updateUpgradeNBTData(side);
		}
	}

	public void addExperience(int amount) {
		int var = Integer.MAX_VALUE - this.experience;
		if (amount > var)
			amount = var;
		this.experienceRemainder += amount;
		for (this.experience += amount; this.experienceRemainder < 0 || this.experienceRemainder >= levelXP(experienceLevel); this.experienceRemainder -= levelXP(experienceLevel) * (this.experienceRemainder < 0 ? -1 : 1)){
			this.addLevels(this.experienceRemainder < 0 ? -1 : 1, false);
		}
	}

	public void addLevels(int par1, boolean updateXP) {
		this.experienceLevel += par1;
		if (this.experienceLevel < 0)
			this.experienceLevel = 0;
		if (updateXP)
			experience = calculateLevelXP(experienceLevel) + experienceRemainder;
	}

	public int levelXP(int level) {
		return level >= 30 ? 62 + (level - 30) * 7 : (level >= 15 ? 17 + (level - 15) * 3 : 17);
	}

	public int calculateLevelXP(int level) {
		int levelXP = 0;
		for (int currentLevel = 1;currentLevel <= level;currentLevel++) {
			levelXP += levelXP(currentLevel);
		}
		return levelXP;
	}

	private int collect() {
		int ret = 0;
		Vec3 pos = Vec3.createVectorHelper(turtle.getPosition().posX,turtle.getPosition().posY,turtle.getPosition().posZ);
		for (EntityXPOrb orb : (List<EntityXPOrb>)turtle.getWorld().getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getBoundingBox(pos.xCoord - COLLECT_RANGE, pos.yCoord - COLLECT_RANGE, pos.zCoord - COLLECT_RANGE, pos.xCoord + 1.0D + COLLECT_RANGE, pos.yCoord + 1.0D + COLLECT_RANGE, pos.zCoord + 1.0D + COLLECT_RANGE))) {
			ret += orb.getXpValue();
			orb.setDead();
		}
		return ret;
	}

	@Override
	public String getType() {
		return "xp";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"add", "getXP", "getLevels", "collect", "setAutoCollect", "enchant"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableXPTurtle)
			throw new LuaException("XP Turtles have been disabled");
		ItemStack slot;
		switch (method) {
			case 0:
				slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
				int amount = Integer.MAX_VALUE;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double))
						throw new LuaException("Bad argument #1 (expected number)");
					amount = (int)Math.floor((Double)arguments[0]);
				}
				if (slot == null)
					return new Object[] {0};
				amount = Math.min(amount, slot.stackSize);
				int recharge = 0;
				if (slot.isItemEqual(new ItemStack(Items.experience_bottle))) {
					recharge = 3 + random.nextInt(5) + random.nextInt(5);
				}
				recharge *= amount;
				addExperience(recharge);
				if (recharge > 0) {
					slot.stackSize -= amount;
					if (slot.stackSize <= 0)
						slot = null;
					turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), slot);
				}
				changed = true;
				return new Object[] {recharge};
			case 1:
				return new Object[] {experience};
			case 2:
				return new Object[] {experienceLevel};
			case 3:
				int collected = collect();
				addExperience(collected);
				changed = true;
				return new Object[] {collected};
			case 4:
				boolean ac = !autoCollect;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Boolean))
						throw new LuaException("Bad argument #1 (expected boolean)");
					ac = (Boolean)arguments[0];
				}
				autoCollect = ac;
				return new Object[] {autoCollect};
			case 5:
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				int levels = (int)Math.floor((Double)arguments[0]);
				if (levels < 1 || levels > MAX_LEVEL)
					throw new LuaException("invalid level count "+levels+" (expected 1-"+MAX_LEVEL+")");
				slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
				if (!slot.isItemEnchantable())
					return new Object[] {false};
				if (experienceLevel < levels)
					return new Object[] {false};
				List enchants = EnchantmentHelper.buildEnchantmentList(random, slot, levels);
				if (enchants == null || enchants.isEmpty())
					return new Object[] {false};
				ItemStack enchanted = slot.copy();
				if (enchanted.isItemEqual(new ItemStack(Items.enchanted_book))) {
					enchanted = new ItemStack(Items.enchanted_book);
					enchanted.stackTagCompound = new NBTTagCompound();
					NBTTagList storedEnchantments = new NBTTagList();
					NBTTagCompound enchantment = new NBTTagCompound();
					EnchantmentData data = (EnchantmentData)enchants.get(0);
					enchantment.setShort("id", (short)data.enchantmentobj.effectId);
					enchantment.setShort("lvl", (short)data.enchantmentLevel);
					storedEnchantments.appendTag(enchantment);
					enchanted.stackTagCompound.setTag("StoredEnchantments", storedEnchantments);
				} else {
					for (EnchantmentData data : (List<EnchantmentData>)enchants) {
						enchanted.addEnchantment(data.enchantmentobj, data.enchantmentLevel);
					}
				}
				addLevels(-levels, true);
				turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), enchanted);
				changed = true;
				return new Object[] {true};
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {}

	@Override
	public void detach(IComputerAccess computer) {}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}

	private class BetterRandom extends Random {
		private long seed;

		@Override
		public void setSeed(long seed) {
			super.setSeed(seed);
			this.seed = seed;
		}

		public long getSeed() {
			return seed;
		}
	}
}
