package miscperipherals.peripheral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.api.IXPSource;
import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.util.RandomExt;
import miscperipherals.util.Util;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralXP implements IHostedPeripheral {
	private static final int TICK_RATE = 20;
	private static final double COLLECT_RANGE = 2.0D;
	private static final int MAX_LEVEL = 30;
	private final ITurtleAccess turtle;
	private final RandomExt random = new RandomExt();
	private int experience = 0;
	private int experienceRemainder = 0;
	private int experienceLevel = 0;
	private boolean autoCollect = false;
	private int ticker = random.nextInt(TICK_RATE);
	
	public PeripheralXP(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "xp";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"add","getXP","getLevels","collect","setAutoCollect","enchant","get","getUp","getDown","fixBook"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				ItemStack slot = turtle.getSlotContents(turtle.getSelectedSlot());
				int amount = Integer.MAX_VALUE;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
					amount = (int)Math.floor((Double)arguments[0]);
				}
				
				if (slot == null) return new Object[] {0};
				amount = Math.min(amount, slot.stackSize);
				
				int recharge = 0;
				if (slot.itemID == Item.expBottle.itemID) {
					recharge = 3 + random.nextInt(5) + random.nextInt(5);
				} else if (slot.itemID == Item.monsterPlacer.itemID) {
					Entity ent = EntityList.createEntityByID(slot.getItemDamage(), turtle.getWorld());
					if (ent instanceof EntityLiving) {
						recharge = ((EntityLiving)ent).experienceValue;
					}
				}
				
				recharge *= amount;
				addExperience(recharge);
				
				if (recharge > 0) {
					slot.stackSize -= amount;
					if (slot.stackSize <= 0) slot = null;
					turtle.setSlotContents(turtle.getSelectedSlot(), slot);
				}
				
				return new Object[] {recharge};
			}
			case 1: {
				return new Object[] {experience};
			}
			case 2: {
				return new Object[] {experienceLevel};
			}
			case 3: {
				int collected = collect();
				addExperience(collected);
				return new Object[] {collected};
			}
			case 4: {
				boolean ac = !autoCollect;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Boolean)) throw new Exception("bad argument #1 (expected boolean)");
					ac = (Boolean)arguments[0];
				}
				
				autoCollect = ac;
				return new Object[] {autoCollect};
			}
			case 5: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int levels = (int)Math.floor((Double)arguments[0]);				
				if (levels < 1 || levels > MAX_LEVEL) throw new Exception("invalid level count "+levels+" (expected 1-"+MAX_LEVEL+")");
				
				ItemStack slot = turtle.getSlotContents(turtle.getSelectedSlot());
				if (!slot.isItemEnchantable()) return new Object[] {false};
				
				if (experienceLevel < levels) return new Object[] {false};
				
				List enchants = EnchantmentHelper.buildEnchantmentList(random, slot, levels);
				if (enchants == null || enchants.isEmpty()) return new Object[] {false}; // no valid enchants
				
				ItemStack enchanted = slot.copy();
				if (isBook(enchanted)) {
					//enchanted = Item.field_92053_bW.func_92057_a((EnchantmentData)enchants.get(0));
					// fixed code for BuildCraft and barrel NBT comparing behavior
					enchanted = new ItemStack(Item.enchantedBook);
					enchanted.stackTagCompound = new NBTTagCompound("tag");
					NBTTagList storedEnchantments = new NBTTagList("StoredEnchantments");
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
				turtle.setSlotContents(turtle.getSelectedSlot(), enchanted);
				
				return new Object[] {true};
			}
			case 6:
			case 7:
			case 8: {
				final int m = method;
				Future<Integer> callback = TickHandler.addTickCallback(turtle.getWorld(), new Callable<Integer>() {
					@Override
					public Integer call() {
						int toAdd = 0;
						MovingObjectPosition mop = Util.rayTraceBlock(turtle, m == 6 ? turtle.getFacingDir() : (m == 7 ? 1 : 0));
						
						for (IXPSource xpsource : IXPSource.handlers) {
							try {
								toAdd += xpsource.get(turtle, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
						
						addExperience(toAdd);
						return toAdd;
					}
				});
				return new Object[] {callback.get()};
			}
			case 9: {
				ItemStack stack = turtle.getSlotContents(turtle.getSelectedSlot());
				if (stack != null && stack.itemID == Item.enchantedBook.itemID && stack.stackTagCompound != null && stack.stackTagCompound.hasKey("StoredEnchantments")) {
					NBTTagList list = stack.stackTagCompound.getTagList("StoredEnchantments");
					if (list.tagCount() > 1) {
						for (int i = 0; i < list.tagCount(); i++) {
							ItemStack book = new ItemStack(Item.enchantedBook);
							book.stackTagCompound = new NBTTagCompound("tag");
							NBTTagList storedEnchantments = new NBTTagList("StoredEnchantments");
							storedEnchantments.appendTag(list.tagAt(i).copy());
							book.stackTagCompound.setTag("StoredEnchantments", storedEnchantments);
							Util.storeOrDrop(turtle, book);
						}
						
						turtle.setSlotContents(turtle.getSelectedSlot(), null);
					}
				}
				
				throw new Exception("This is a debug function used by me to fix glitched enchanted books. Those were only possible to obtain in a internal ForgeCraft testing version.");
			}
		}
		
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		LuaManager.mount(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}

	@Override
	public void update() {
		if (autoCollect && ++ticker >= TICK_RATE) {
			ticker = 0;
			addExperience(collect());
		}
	}
	
	private int collect() {
		int ret = 0;
		Vec3 pos = turtle.getPosition();
		for (EntityXPOrb orb : (List<EntityXPOrb>)turtle.getWorld().getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getAABBPool().getAABB(pos.xCoord - COLLECT_RANGE, pos.yCoord - COLLECT_RANGE, pos.zCoord - COLLECT_RANGE, pos.xCoord + 1.0D + COLLECT_RANGE, pos.yCoord + 1.0D + COLLECT_RANGE, pos.zCoord + 1.0D + COLLECT_RANGE))) {
			ret += orb.getXpValue();
			orb.setDead();
		}
		
		return ret;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		experience = nbttagcompound.getInteger("experience");
		experienceRemainder = nbttagcompound.getInteger("experienceRemainder");
		experienceLevel = nbttagcompound.getInteger("experienceLevel");
		random.setSeed(nbttagcompound.getLong("rndSeed"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("experience", experience);
		nbttagcompound.setInteger("experienceRemainder", experienceRemainder);
		nbttagcompound.setInteger("experienceLevel", experienceLevel);
		nbttagcompound.setLong("rndSeed", random.getSeed());
	}
	
    public void addExperience(int par1) {
        int var2 = Integer.MAX_VALUE - this.experience;

        if (par1 > var2)
        {
            par1 = var2;
        }

        this.experienceRemainder += par1;

        for (this.experience += par1; this.experienceRemainder < 0 || this.experienceRemainder >= thisLevelXP(); this.experienceRemainder -= thisLevelXP() * (this.experienceRemainder < 0 ? -1 : 1))
        {
            this.addLevels(this.experienceRemainder < 0 ? -1 : 1, false);
        }
    }

    public void addLevels(int par1, boolean updateXP) {
        this.experienceLevel += par1;

        if (this.experienceLevel < 0)
        {
            this.experienceLevel = 0;
        }
        if (updateXP) {experience = calculateLevelXP(experienceLevel) + experienceRemainder;}
    }

    public int thisLevelXP() {
        return levelXP(experienceLevel);
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
    
    public static int calculateFurnaceXP(ItemStack stack) {
    	int stackSize = stack.stackSize;
    	float xp = FurnaceRecipes.smelting().getExperience(stack);
    	
    	if (xp == 0.0F) return 0;
		else if (xp < 1.0F) {
			int result = (int)Math.floor((float)stackSize * xp);
			if (result < (int)Math.ceil((float)stackSize * xp) && (float)Math.random() < (float)stackSize * xp - (float)result) {
				result++;
			}
			stackSize = result;
		}
		
		return stackSize;
    }
    
    private static boolean isBook(ItemStack stack) {
    	return stack.itemID == Item.book.itemID;
    }
    
    public static class FurnaceXPSource implements IXPSource {
    	public static final Map<Class, int[]> classes = new HashMap<Class, int[]>();
    	
		@Override
		public int get(ITurtleAccess turtle, int x, int y, int z, int side) {
			TileEntity te = turtle.getWorld().getBlockTileEntity(x, y, z);
			if (te == null) return 0;
			int[] slots = null;
			for (Entry<Class, int[]> entry : classes.entrySet()) {
				if (entry.getKey().isAssignableFrom(te.getClass())) {
					slots = entry.getValue();
					break;
				}
			}
			if (slots == null) return 0;
			
			int ret = 0;
			IInventory inv = (IInventory)te;
			for (int slot : slots) {
				ItemStack stack = inv.getStackInSlotOnClosing(slot);
				if (stack != null) {
					Util.storeOrDrop(turtle, stack);
					ret += calculateFurnaceXP(stack);
				}
			}
			return ret;
		}
		
		static {
			classes.put(TileEntityFurnace.class, new int[] {2});
		}
    }
    
    public static class TurtleXPSource implements IXPSource {
		@Override
		public int get(ITurtleAccess turtle, int x, int y, int z, int side) {
			TileEntity te = turtle.getWorld().getBlockTileEntity(x, y, z);
			if (!(te instanceof ITurtleAccess)) return 0;
			
			PeripheralXP xp = Util.getPeripheral(turtle, PeripheralXP.class);
			if (xp != null) {
				int levels = xp.experienceLevel;
				if (levels > 0) {
					xp.addLevels(-levels, true);
					return xp.calculateLevelXP(levels);
				}
			}
			return 0;
		}
    }
    
    public static class LiquidXPSource implements IXPSource {
    	public static final Map<LiquidStack, Integer> liquids = new HashMap<LiquidStack, Integer>();
    	
		@Override
		public int get(ITurtleAccess turtle, int x, int y, int z, int side) {
			TileEntity te = turtle.getWorld().getBlockTileEntity(x, y, z);
			if (!(te instanceof ITankContainer)) return 0;
			ITankContainer tc = (ITankContainer)te;
			ForgeDirection dir = ForgeDirection.getOrientation(side);
			
			List<ILiquidTank> tanks = new ArrayList<ILiquidTank>();
			tanks.addAll(Arrays.asList(tc.getTanks(dir)));
			for (Entry<LiquidStack, Integer> entry : liquids.entrySet()) {
				ILiquidTank tank = tc.getTank(dir, entry.getKey());
				if (tank != null) tanks.add(tank);
			}
			
			int ret = 0;
			for (ILiquidTank tank : tanks) {
				for (Entry<LiquidStack, Integer> entry : liquids.entrySet()) {
					LiquidStack wanted = entry.getKey();
					if (wanted.isLiquidEqual(tank.getLiquid())) {
						LiquidStack drained;
						while ((drained = tank.drain(wanted.amount, true)) != null) {
							ret += (int)Math.floor((float)entry.getValue() * ((float)drained.amount / (float)wanted.amount));
						}
					}
				}
			}
			return ret;
		}
    }
    
    static {
    	IXPSource.handlers.add(new FurnaceXPSource());
    	IXPSource.handlers.add(new TurtleXPSource());
    	IXPSource.handlers.add(new LiquidXPSource());
    }
}
