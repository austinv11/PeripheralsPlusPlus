package miscperipherals.peripheral;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import miscperipherals.api.ISmItem;
import miscperipherals.api.ISmSender;
import miscperipherals.core.LuaManager;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.Positionable;
import miscperipherals.util.SmallNetHelper;
import miscperipherals.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralSmSender implements ISmSender, IHostedPeripheral {
	public final Positionable positionable;
	private List<UUID> links = new LinkedList<UUID>();
	
	public PeripheralSmSender(TilePeripheralWrapper tile) {
		positionable = new Positionable.PositionableTile(tile);
	}
	
	public PeripheralSmSender(ITurtleAccess turtle) {
		positionable = new Positionable.PositionableTurtle(turtle);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList list = tag.getTagList("links");
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound link = (NBTTagCompound) list.tagAt(i);
			links.add(new UUID(link.getLong("msb"), link.getLong("lsb")));
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList list = new NBTTagList();
		for (UUID uuid : links) {
			NBTTagCompound link = new NBTTagCompound();
			link.setLong("lsb", uuid.getLeastSignificantBits());
			link.setLong("msb", uuid.getMostSignificantBits());
			list.appendTag(link);
		}
		tag.setTag("links", list);
	}
	
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem();
		if (stack != null && stack.getItem() instanceof ISmItem) {
			UUID uuid = ((ISmItem) stack.getItem()).getUUID(player, stack);
			if (links.contains(uuid)) {
				links.remove(uuid);
				player.sendChatToPlayer("Unlinked from item " + uuid.toString());
			} else {
				links.add(uuid);
				player.sendChatToPlayer("Linked to item " + uuid.toString());
			}
		}
		
		UUID uuid = SmallNetHelper.getUUID(player);
		if (links.contains(uuid)) {
			links.remove(uuid);
			player.sendChatToPlayer("Unlinked from player " + uuid.toString());
		} else {
			links.add(uuid);
			player.sendChatToPlayer("Linked to player " + uuid.toString());
		}
		
		return true;
	}

	@Override
	public Vec3 getPosition() {
		return positionable.getPosition().addVector(0.5D, 0.5D, 0.5D);
	}
	
	@Override
	public World getWorld() {
		return positionable.getWorld();
	}

	@Override
	public Iterable<UUID> getLinks() {
		return links;
	}

	@Override
	public void addLink(UUID uuid) throws UnsupportedOperationException {
		links.add(uuid);
	}

	@Override
	public void removeLink(UUID uuid) throws UnsupportedOperationException {
		links.remove(uuid);
	}

	@Override
	public double getPower() {
		return 64.0D;
	}

	@Override
	public String getType() {
		return "smNetSender";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"send"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				else if (!(arguments[1] instanceof String)) throw new Exception("bad argument #2 (expected string)");
				
				String type = (String) arguments[0];
				if (type.length() > 255) type = type.substring(0, 255);
				
				String payload = (String) arguments[1];
				if (payload.length() > 255) payload = payload.substring(0, 255);
				
				for (UUID uuid : links) {
					SmallNetHelper.send(this, uuid, Util.sanitize(type, false), Util.sanitize(payload, false));
				}
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
		
	}
}
