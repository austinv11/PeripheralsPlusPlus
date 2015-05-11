package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.AudioPacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import com.austinv11.peripheralsplusplus.utils.TranslateUtils;
import com.gtranslate.Language;
import cpw.mods.fml.common.network.NetworkRegistry;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public class TileEntitySpeaker extends MountedTileEntity {

	public static String publicName = "speaker";
	private String name = "tileEntitySpeaker";
	private ITurtleAccess turtle;
	private TurtleSide side = null;
	private int id;
	private static final int TICKER_INTERVAL = 20;
	private int eventTicker = 0;
	private int eventSubticker = 0;
	private HashMap<IComputerAccess, Boolean> computers = new HashMap<IComputerAccess,Boolean>();
	private String lastMessage;
	private Object[] packetInfo = new Object[4];

	public TileEntitySpeaker() {
		super();
		packetInfo[0] = null;
	}

	public TileEntitySpeaker(ITurtleAccess turtle, TurtleSide side) {
		this();
		this.turtle = turtle;
		this.side = side;
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
	}

	@Override
	public void updateEntity() {
		if (turtle != null) {
			this.setWorldObj(turtle.getWorld());
			this.xCoord = turtle.getPosition().posX;
			this.yCoord = turtle.getPosition().posY;
			this.zCoord = turtle.getPosition().posZ;
		}
		if (worldObj != null)
			id = worldObj.provider.dimensionId;
		if (eventSubticker > 0)
			eventSubticker--;
		if (eventSubticker == 0 && eventTicker != 0)
			eventTicker = 0;
		if (packetInfo[0] != null) {
			PeripheralsPlusPlus.NETWORK.sendToAllAround(new AudioPacket((String)packetInfo[1], (String)packetInfo[2], xCoord, yCoord, zCoord, id, side), new NetworkRegistry.TargetPoint(id, xCoord, yCoord, zCoord, (Double)packetInfo[3]));
			packetInfo[0] = null;
		}
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"speak"/*text, [range, [lang]]*/};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableSpeaker)
			throw new LuaException("Speakers have been disabled");
		if (method == 0) {
//			try {
			if (!(arguments.length > 0) || !(arguments[0] instanceof String))
				throw new LuaException("Bad argument #1 (expected string)");
			if (arguments.length > 1 && !(arguments[1] instanceof Double))
				throw new LuaException("Bad argument #2 (expected string)");
			if (arguments.length > 2 && !(arguments[2] instanceof String))
				throw new LuaException("Bad argument #3 (expected boolean)");
			if (arguments.length > 3 && !(arguments[3] instanceof Boolean))
				throw new LuaException("Bad argument #4 (expected boolean");
			String lang = null;
			if (arguments.length > 2)
				if (TranslateUtils.isPrefix((String) arguments[2]))
					lang = (String) arguments[2];
				else {
					try {
						lang = ReflectionHelper.getLangFromWord((String) arguments[2]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (lang == null || lang.length() < 2)
						throw new LuaException("Language "+(String)arguments[2]+" is unknown");
				}
			else
				lang = Language.ENGLISH;//TranslateUtils.detectLangPrefix((String) arguments[0]);
			double range;
			if (Config.speechRange < 0)
				range = Double.MAX_VALUE;
			else
				range = Config.speechRange;
			if (arguments.length > 1)
				range = (Double) arguments[1];
			packetInfo[0] = "something";
			packetInfo[1] = lang;
			packetInfo[2] = arguments[0];
			packetInfo[3] = range;
			lastMessage = (String)arguments[0];
			if (arguments.length > 3 && (Boolean) arguments[3])
				context.pullEvent("speechComplete");
			return new Object[]{lastMessage, lang};
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.put(computer, true);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
		super.detach(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	public void onSpeechCompletion(String text, String lang) {
		for (IComputerAccess computer : computers.keySet())
			if (eventTicker == 0 || !lastMessage.equals(text)) {
				computer.queueEvent("speechComplete", new Object[]{text, lang});
				eventSubticker = TICKER_INTERVAL;
				eventTicker++;
			}
	}
}
