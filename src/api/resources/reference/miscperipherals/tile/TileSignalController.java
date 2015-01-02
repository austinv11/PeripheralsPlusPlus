package miscperipherals.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.TickHandler;
import miscperipherals.util.Util;
import mods.railcraft.api.core.WorldCoordinate;
import mods.railcraft.api.signals.IControllerTile;
import mods.railcraft.api.signals.IReceiverTile;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.api.signals.SignalTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileSignalController extends Tile implements IControllerTile, IReceiverTile, IPeripheral {
	private static final String NAME = "Electronic Signal Controller";
	private static final SignalAspect[] ASPECTS = SignalAspect.values();
	
	private MultiSignalReceiver receiver = new MultiSignalReceiver(NAME, this, 256);
	private MultiSignalController controller = new MultiSignalController(NAME, this, 256);
	private final Map<IComputerAccess, Boolean> computers = new WeakHashMap<IComputerAccess, Boolean>();
	
	@Override
	public World getWorld() {
		return worldObj;
	}

	@Override
	public SignalReceiver getReceiver() {
		return receiver;
	}

	@Override
	public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
		for (IComputerAccess computer : computers.keySet()) {
			WorldCoordinate coords = con.getCoords();
			computer.queueEvent("signal", new Object[] {coords.dimension, coords.x, coords.y, coords.z, aspectToString(aspect)});
		}
	}

	@Override
	public SignalController getController() {
		return controller;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		receiver.readFromNBT(compound);
		controller.readFromNBT(compound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		receiver.writeToNBT(compound);
		controller.writeToNBT(compound);
	}

	@Override
	public String getType() {
		return "signalController";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"get", "set"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, final Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length > 0 && arguments.length < 3) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (arguments.length > 1 && !(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (arguments.length > 2 && !(arguments[2] instanceof Double)) throw new Exception("bad argument #3 (expected number)");
				else if (arguments.length > 3 && !(arguments[3] instanceof Double)) throw new Exception("bad argument #4 (expected number)");
				
				Future<SignalAspect> callback = TickHandler.addTickCallback(worldObj, new Callable<SignalAspect>() {
					@Override
					public SignalAspect call() {
						if (arguments.length > 1) {
							int x = (int)Math.floor((Double)arguments[0]);
							int y = (int)Math.floor((Double)arguments[1]);
							int z = (int)Math.floor((Double)arguments[2]);
							int dim = arguments.length > 3 ? (int)Math.floor((Double)arguments[3]) : worldObj.provider.dimensionId;
							
							return receiver.getAspectFor(new WorldCoordinate(dim, x, y, z));
						} else {
							return receiver.getDefaultAspect();
						}
					}
				});
				
				SignalAspect aspect = callback.get();
				return new Object[] {aspect == null ? null : aspectToString(aspect)};
			}
			case 1: {
				if (arguments.length < 1 || (arguments.length > 1 && arguments.length < 4)) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				else if (arguments.length > 1 && !(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (arguments.length > 2 && !(arguments[2] instanceof Double)) throw new Exception("bad argument #3 (expected number)");
				else if (arguments.length > 3 && !(arguments[3] instanceof Double)) throw new Exception("bad argument #4 (expected number)");
				else if (arguments.length > 4 && !(arguments[4] instanceof Double)) throw new Exception("bad argument #5 (expected number)");
				
				Future<String> callback = TickHandler.addTickCallback(worldObj, new Callable<String>() {
					@Override
					public String call() {
						String aspectName = (String)arguments[0];
						
						SignalAspect aspect = getAspect(aspectName);
						if (arguments.length > 1) {
							int x = (int)Math.floor((Double)arguments[1]);
							int y = (int)Math.floor((Double)arguments[2]);
							int z = (int)Math.floor((Double)arguments[3]);
							int dim = arguments.length > 4 ? (int)Math.floor((Double)arguments[4]) : worldObj.provider.dimensionId;
							
							if (!controller.setAspectFor(new WorldCoordinate(dim, x, y, z), aspect)) return null;
						} else {
							controller.setDefaultAspect(aspect);
						}
						return aspectToString(aspect);
					}
				});
				
				return new Object[] {callback.get()};
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
		computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side.isServer()) {
			receiver.tickServer();
			controller.tickServer();
		} else if (side.isClient()) {
			receiver.tickClient();
			controller.tickClient();
		}
	}
	
	private SignalAspect getAspect(String name) {
		for (SignalAspect aspect : ASPECTS) {
			if (name.equalsIgnoreCase(aspectToString(aspect))) return aspect;
		}
		
		return SignalAspect.BLINK_RED;
	}
	
	private String aspectToString(SignalAspect aspect) {
		return Util.camelCase(aspect.name().replaceAll("_", " "));
	}
	
	private WorldCoordinate getWorldCoordinate() {
		return new WorldCoordinate(worldObj.provider.dimensionId, xCoord, yCoord, zCoord);
	}
	
	private class MultiSignalController extends SignalController {
		private Map<WorldCoordinate, SignalAspect> aspects = new HashMap<WorldCoordinate, SignalAspect>();
		
		public MultiSignalController(String name, TileEntity tile, int maxPairings) {
			super(name, tile, maxPairings);
		}
		
		@Override
		public void loadNBT(NBTTagCompound compound) {
			NBTTagList aspects = compound.getTagList("aspects");
			for (int i = 0; i < aspects.tagCount(); i++) {
				NBTTagCompound aspect = (NBTTagCompound)aspects.tagAt(i);
				int[] coords = aspect.getIntArray("coords");
				this.aspects.put(new WorldCoordinate(coords[0], coords[1], coords[2], coords[3]), SignalAspect.values()[aspect.getByte("aspect")]);
			}
		}
		
		@Override
		public void saveNBT(NBTTagCompound compound) {
			NBTTagList aspects = new NBTTagList();
			for (Entry<WorldCoordinate, SignalAspect> entry : this.aspects.entrySet()) {
				NBTTagCompound aspect = new NBTTagCompound();
				WorldCoordinate coords = entry.getKey();
				aspect.setIntArray("coords", new int[] {coords.dimension, coords.x, coords.y, coords.z});
				aspect.setByte("aspect", (byte)entry.getValue().ordinal());
				aspects.appendTag(aspect);
			}
			compound.setTag("aspects", aspects);
		}
		
		@Override
		public SignalAspect getAspectFor(WorldCoordinate receiver) {
			return aspects.get(receiver);
		}
		
		@Override
		public void clearPairing(WorldCoordinate other) {
			super.clearPairing(other);
			aspects.remove(other);
			
			for (IComputerAccess computer : computers.keySet()) {
				computer.queueEvent("signal_receiver_remove", new Object[] {other.dimension, other.x, other.y, other.z});
			}
		}
		
		@Override
		public void addPairing(WorldCoordinate other) {
			// modified super code
			pairings.add(other);
	        while (pairings.size() > getMaxPairings()) {
	            WorldCoordinate coord = pairings.remove();
	            for (IComputerAccess computer : computers.keySet()) {
					computer.queueEvent("signal_receiver_remove", new Object[] {coord.dimension, coord.x, coord.y, coord.z});
				}
	        }
	        SignalTools.packetBuilder.sendPairPacketUpdate(this);			
			
			aspects.put(other, SignalAspect.BLINK_RED);
			
			for (IComputerAccess computer : computers.keySet()) {
				computer.queueEvent("signal_receiver_add", new Object[] {other.dimension, other.x, other.y, other.z});
			}
		}
		
		public boolean setAspectFor(WorldCoordinate receiver, SignalAspect aspect) {
			if (!pairings.contains(receiver)) return false;
			
			if (sendAspectTo(receiver, aspect)) {
				aspects.put(receiver, aspect);
				return true;
			} else return false;
		}
		
		public void setDefaultAspect(SignalAspect aspect) {
			for (WorldCoordinate coord : getPairs()) {
				setAspectFor(coord, aspect);
			}
		}
	}
	
	private class MultiSignalReceiver extends SignalReceiver {
		private Map<WorldCoordinate, SignalAspect> aspects = new HashMap<WorldCoordinate, SignalAspect>();
		
		public MultiSignalReceiver(String name, TileEntity tile, int maxPairings) {
			super(name, tile, maxPairings);
		}
		
		@Override
		public void loadNBT(NBTTagCompound compound) {
			super.loadNBT(compound);
			
			NBTTagList aspects = compound.getTagList("aspects");
			for (int i = 0; i < aspects.tagCount(); i++) {
				NBTTagCompound aspect = (NBTTagCompound)aspects.tagAt(i);
				int[] coords = aspect.getIntArray("coords");
				this.aspects.put(new WorldCoordinate(coords[0], coords[1], coords[2], coords[3]), SignalAspect.values()[aspect.getByte("aspect")]);
			}
		}
		
		@Override
		public void saveNBT(NBTTagCompound compound) {
			super.saveNBT(compound);
			
			NBTTagList aspects = new NBTTagList();
			for (Entry<WorldCoordinate, SignalAspect> entry : this.aspects.entrySet()) {
				NBTTagCompound aspect = new NBTTagCompound();
				WorldCoordinate coords = entry.getKey();
				aspect.setIntArray("coords", new int[] {coords.dimension, coords.x, coords.y, coords.z});
				aspect.setByte("aspect", (byte)entry.getValue().ordinal());
				aspects.appendTag(aspect);
			}
			compound.setTag("aspects", aspects);
		}
		
		@Override
		public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
			super.onControllerAspectChange(con, aspect);
			
			aspects.put(con.getCoords(), aspect);
		}
		
		public SignalAspect getAspectFor(WorldCoordinate coord) {
			SignalController controller = getControllerAt(coord);
			if (controller == null) return SignalAspect.BLINK_RED;
			return controller.getAspectFor(getCoords());
		}
		
		public SignalAspect getDefaultAspect() {
			SignalAspect ret = null;
			for (WorldCoordinate coord : getPairs()) {
				ret = SignalAspect.mostRestrictive(ret, getAspectFor(coord));
			}
			return ret;
		}
	}
}
