package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.AudioPacket;
import com.austinv11.peripheralsplusplus.network.SynthPacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import com.austinv11.peripheralsplusplus.utils.TranslateUtils;
import com.gtranslate.Language;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		return nbttagcompound;
	}

	public void update() {
		if (turtle != null) {
			this.setWorld(turtle.getWorld());
			this.setPos(turtle.getPosition());
		}
		if (world != null)
			id = world.provider.getDimension();
		if (eventSubticker > 0)
			eventSubticker--;
		if (eventSubticker == 0 && eventTicker != 0)
			eventTicker = 0;
		if (packetInfo[0] != null) {
			PeripheralsPlusPlus.NETWORK.sendToAllAround(
			        new AudioPacket((String)packetInfo[1], (String)packetInfo[2], getPos(), id, side),
                    new NetworkRegistry.TargetPoint(id, getPos().getX(), getPos().getY(), getPos().getZ(),
                            (Double)packetInfo[3]));
			packetInfo[0] = null;
		}
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"speak", "synthesize" /*text, [range, [voice, [pitch, [pitchRange, [pitchShift, [rate, [volume, [wait]]]]]]]]*/};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableSpeaker)
			throw new LuaException("Speakers have been disabled");
		if (method == 0) {
			if (!(arguments.length > 0) || !(arguments[0] instanceof String))
				throw new LuaException("Bad argument #1 (expected string)");
			if (arguments.length > 1 && !(arguments[1] instanceof Double))
				throw new LuaException("Bad argument #2 (expected number)");
			if (arguments.length > 2 && !(arguments[2] instanceof String))
				throw new LuaException("Bad argument #3 (expected string)");
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
		} else if (method == 1) {
			if (!(arguments.length > 0) || !(arguments[0] instanceof String))
				throw new LuaException("Bad argument #1 (expected string)");
			if (arguments.length > 1 && !(arguments[1] instanceof Double))
				throw new LuaException("Bad argument #2 (expected number)");
			if (arguments.length > 2 && !(arguments[2] instanceof String))
				throw new LuaException("Bad argument #3 (expected string)");
			if (arguments.length > 3 && !(arguments[3] instanceof Double))
				throw new LuaException("Bad argument #4 (expected number)");
			if (arguments.length > 4 && !(arguments[4] instanceof Double))
				throw new LuaException("Bad argument #5 (expected number)");
			if (arguments.length > 5 && !(arguments[5] instanceof Double))
				throw new LuaException("Bad argument #6 (expected number)");
			if (arguments.length > 6 && !(arguments[6] instanceof Double))
				throw new LuaException("Bad argument #7 (expected number)");
			if (arguments.length > 7 && !(arguments[7] instanceof Double))
				throw new LuaException("Bad argument #8 (expected number)");
			if (arguments.length > 8 && !(arguments[8] instanceof Boolean))
				throw new LuaException("Bad argument #9 (expected boolean");
			
			String text = (String) arguments[0];
			double range;
			if (Config.speechRange < 0)
				range = Double.MAX_VALUE;
			else
				range = Config.speechRange;
			if (arguments.length > 1)
				range = (Double) arguments[1];
			String voice = arguments.length > 2 ? (String) arguments[2] : "kevin16";
			Float pitch = arguments.length > 3 ? ((Double)arguments[3]).floatValue() : null;
			Float pitchRange = arguments.length > 4 ? ((Double)arguments[4]).floatValue() : null;
			Float pitchShift = arguments.length > 5 ? ((Double)arguments[5]).floatValue() : null;
			Float rate = arguments.length > 6 ? ((Double)arguments[6]).floatValue() : null;
			Float volume = arguments.length > 7 ? ((Double)arguments[7]).floatValue() : null;
			
			PeripheralsPlusPlus.NETWORK.sendToAllAround(
			        new SynthPacket(text, voice, pitch, pitchRange, pitchShift, rate, volume, getPos(), id, side),
                    new NetworkRegistry.TargetPoint(id, getPos().getX(), getPos().getY(), getPos().getZ(), range));
			
			if (arguments.length > 8 && (Boolean) arguments[8])
				context.pullEvent("synthComplete");
			return new Object[]{text};
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
			if (eventTicker == 0 || (lastMessage != null && !lastMessage.equals(text))) {
				if (lang != null)
					computer.queueEvent("speechComplete", new Object[]{text, lang});
				else
					computer.queueEvent("synthComplete", new Object[]{text});
				eventSubticker = TICKER_INTERVAL;
				eventTicker++;
			}
	}
}
