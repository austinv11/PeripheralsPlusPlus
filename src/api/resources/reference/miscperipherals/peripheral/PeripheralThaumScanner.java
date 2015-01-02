package miscperipherals.peripheral;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import miscperipherals.core.LuaManager;
import miscperipherals.safe.Reflector;
import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.AuraNode;
import thaumcraft.api.EnumTag;
import thaumcraft.api.IAspectSource;
import thaumcraft.api.ObjectTags;
import thaumcraft.api.ThaumcraftApi;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralThaumScanner implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	
	public PeripheralThaumScanner(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "thaumScanner";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getAspects", "getAspectsUp", "getAspectsDown", "getItemAspects", "getAuraNodeDistance", "getAuraNode"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 1:
			case 2: {
				MovingObjectPosition mop = Util.rayTraceBlock(turtle, method == 0 ? turtle.getFacingDir() : (method == 1 ? 1 : 0));
				TileEntity tile = turtle.getWorld().getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
				if (!(tile instanceof IAspectSource)) return new Object[] {null};
				
				IAspectSource source = (IAspectSource)tile;
				ObjectTags tags = source.getSourceTags();
				Map<String, Integer> aspects = new HashMap<String, Integer>(tags.size());
				for (Entry<EnumTag, Integer> aspect : tags.tags.entrySet()) {
					if (aspect.getValue() > 0) aspects.put(aspect.getKey().name, aspect.getValue());
				}
				
				return new Object[] {aspects};
			}
			case 3: {
				ItemStack selstack = turtle.getSlotContents(turtle.getSelectedSlot());
				if (selstack == null) return new Object[] {null};

				Map<String, Integer> aspects = new HashMap<String, Integer>();
				ObjectTags tags = ThaumcraftApi.objectTags.get(Arrays.asList(selstack.itemID, selstack.getItemDamage()));
				if (tags == null) return new Object[] {aspects};
				
				for (EnumTag aspect : (Set<EnumTag>)tags.tags.keySet()) {
					aspects.put(aspect.name, (Integer)tags.tags.get(aspect));
				}
				
				return new Object[] {aspects};
			}
			case 4: 
			case 5: {
				World world = turtle.getWorld();
				Vec3 pos = turtle.getPosition();
				double closestDistance = Double.MAX_VALUE;
				Map<Integer, AuraNode> auraNodes = (Map<Integer, AuraNode>)Reflector.getField("thaumcraft.common.AuraManager", "auraNodes", Map.class);
				AuraNode closest = null;
				for (Integer key : (Iterable<Integer>)Reflector.invoke("thaumcraft.common.AuraManager", "getAurasWithin", Iterable.class, world, pos.xCoord, pos.yCoord, pos.zCoord)) {
					AuraNode node = auraNodes.get(key);
					if (node == null) continue;
					double distance = pos.distanceTo(world.getWorldVec3Pool().getVecFromPool(node.xPos, node.yPos, node.zPos));
					if (distance < closestDistance) {
						closestDistance = distance;
						closest = node;
					}
				}
				
				if (closest == null) return new Object[] {null, null, null, null, null};
				
				int flux = 0;
				for (Integer fluxamount : closest.flux.tags.values()) {
					flux += fluxamount;
				}
				return new Object[] {auraNodeHashCode(closest), closestDistance, closest.level, flux, Util.camelCase(closest.type.name())};
			}
		}
		
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side)  {
		return false;
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

	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	private int auraNodeHashCode(AuraNode node) {
		return node.key;
	}
}
