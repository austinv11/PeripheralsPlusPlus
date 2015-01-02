package miscperipherals.peripheral;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.Positionable;
import miscperipherals.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralChatBox implements IHostedPeripheral {
	private static final int TICKER_INTERVAL = 20;
	
	private final Positionable positionable;
	private Map<IComputerAccess, Boolean> computers = new WeakHashMap<IComputerAccess, Boolean>();
	private int ticker = 0;
	private int subticker = 0;
	
	public PeripheralChatBox(ITurtleAccess turtle) {
		positionable = new Positionable.PositionableTurtle(turtle);
		init();
	}
	
	public PeripheralChatBox(TilePeripheralWrapper tile) {
		positionable = new Positionable.PositionableTile(tile);
		init();
	}
	
	@Override
	public String getType() {
		return "chat";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"say", "tell"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				else if (arguments.length > 1 && !(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (arguments.length > 2 && !(arguments[2] instanceof Boolean)) throw new Exception("bad argument #3 (expected boolean)");
				
				if (++ticker > MiscPeripherals.instance.chatSayRate + 1) throw new Exception("too many messages (over "+MiscPeripherals.instance.chatSayRate+" per tick)");
				
				String message = Util.sanitize((String)arguments[0], MiscPeripherals.instance.chatColorCodes);
				double range = Math.min(arguments.length > 1 ? (Double)arguments[1] : Double.MAX_VALUE, MinecraftServer.getServer().isDedicatedServer() ? (MiscPeripherals.instance.chatSayRange < 0 ? Double.MAX_VALUE : MiscPeripherals.instance.chatSayRange) : Double.MAX_VALUE);
				boolean unlimitedVertical = arguments.length > 2 ? ((Boolean)arguments[2] && MiscPeripherals.instance.chatSayUnlimitedVertical) : false;
				
				Vec3 pos = positionable.getPosition();
				String msg = "[#"+(MiscPeripherals.instance.chatLogCoords ? ":"+(int)Math.floor(pos.xCoord)+","+(int)Math.floor(pos.yCoord)+","+(int)Math.floor(pos.zCoord) : "")+"] "+message;
				for (EntityPlayer player : (Iterable<EntityPlayer>)positionable.getWorld().playerEntities) {
					Vec3 playerpos = Util.getPosition(player, 1.0F);
					if (unlimitedVertical) playerpos.yCoord = pos.yCoord;
					if (pos.distanceTo(playerpos) > range) continue;
					
					player.sendChatToPlayer(msg);
				}
				
				return new Object[] {true};
			}
			case 1: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				else if (!(arguments[1] instanceof String)) throw new Exception("bad argument #2 (expected string)");
				else if (arguments.length > 2 && !(arguments[2] instanceof Double)) throw new Exception("bad argument #3 (expected number)");
				else if (arguments.length > 3 && !(arguments[3] instanceof Boolean)) throw new Exception("bad argument #4 (expected boolean)");
				
				if (++ticker > MiscPeripherals.instance.chatSayRate + 1) throw new Exception("too many messages (over "+MiscPeripherals.instance.chatSayRate+" per tick)");
				
				String to = Util.sanitize((String)arguments[0], false);
				String message = Util.sanitize((String)arguments[1], MiscPeripherals.instance.chatColorCodes);
				double range = Math.min(arguments.length > 2 ? (Double)arguments[2] : Double.MAX_VALUE, MinecraftServer.getServer().isDedicatedServer() ? (MiscPeripherals.instance.chatSayRange < 0 ? Double.MAX_VALUE : MiscPeripherals.instance.chatSayRange) : Double.MAX_VALUE);
				boolean unlimitedVertical = arguments.length > 3 ? ((Boolean)arguments[3] && MiscPeripherals.instance.chatSayUnlimitedVertical) : false;
				
				Vec3 pos = positionable.getPosition();
				String msg = "[#"+(MiscPeripherals.instance.chatLogCoords ? ":"+(int)Math.floor(pos.xCoord)+","+(int)Math.floor(pos.yCoord)+","+(int)Math.floor(pos.zCoord) : "")+"] "+message;
				
				EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(to);
				if (player == null) return new Object[] {false};
				
				Vec3 playerpos = Util.getPosition(player, 1.0F);
				if (unlimitedVertical) playerpos.yCoord = pos.yCoord;
				if (pos.distanceTo(playerpos) > range) return new Object[] {false};
				
				player.sendChatToPlayer(msg);
				
				return new Object[] {true};
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
		
		computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}

	@Override
	public void update() {
		if (++subticker > TICKER_INTERVAL) subticker = ticker = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	private void onChat(EntityPlayer player, String message) {
		if (notInRange(player)) return;
		
		for (IComputerAccess computer : computers.keySet()) {
			computer.queueEvent("chat", new Object[] {player.getEntityName(), message});
		}
	}
	
	private void onDeath(EntityPlayer player, DamageSource source) {
		if (notInRange(player)) return;
		
		String killer = null;
		if (source instanceof EntityDamageSource) {
			Entity ent = ((EntityDamageSource)source).getEntity();
			if (ent != null) killer = ent.getEntityName();
		}
		
		for (IComputerAccess computer : computers.keySet()) {
			computer.queueEvent("chat_death", new Object[] {player.getEntityName(), killer, source.damageType});
		}
	}
	
	private boolean notInRange(EntityPlayer player) {
		Vec3 pos = positionable.getPosition();
		if (pos == null) {
			MiscPeripherals.log.warning("Chat box pos is null!");
			return true;
		}
		if (MinecraftServer.getServer() == null) {
			MiscPeripherals.log.warning("Server is null for chat box at "+pos.xCoord+","+pos.yCoord+","+pos.zCoord+" on "+positionable.getWorld()+"!");
			return true;
		}
		return MinecraftServer.getServer().isDedicatedServer() && pos.distanceTo(Util.getPosition(player, 1.0F)) > (MiscPeripherals.instance.chatReadRange < 0 ? Double.MAX_VALUE : MiscPeripherals.instance.chatReadRange);
	}
	
	private void init() {
		ChatListener.peripherals.put(this, true);
	}
	
	public static class ChatListener {
		private static Map<PeripheralChatBox, Boolean> peripherals = Collections.synchronizedMap(new WeakHashMap<PeripheralChatBox, Boolean>());
		
		@ForgeSubscribe
		public void onServerChat(ServerChatEvent event) {
			for (PeripheralChatBox peripheral : peripherals.keySet()) {
				peripheral.onChat(event.player, event.message);
			}
		}
		
		@ForgeSubscribe
		public void onLivingDeath(LivingDeathEvent event) {
			if (!(event.entity instanceof EntityPlayerMP)) return;
			
			for (PeripheralChatBox peripheral : peripherals.keySet()) {
				peripheral.onDeath((EntityPlayerMP)event.entity, event.source);
			}
		}
	}
	
	static {
		MinecraftForge.EVENT_BUS.register(new ChatListener());
	}
}
