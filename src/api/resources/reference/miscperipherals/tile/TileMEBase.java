package miscperipherals.tile;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import appeng.api.WorldCoord;
import appeng.api.events.GridTileLoadEvent;
import appeng.api.events.GridTileUnloadEvent;
import appeng.api.me.tiles.IGridMachine;
import appeng.api.me.util.IGridInterface;

public class TileMEBase extends Tile implements IGridMachine {
	protected IGridInterface grid;
	protected boolean powered = false;
	
	@Override
	public WorldCoord getLocation() {
		return new WorldCoord(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean isValid() {
		return loaded;
	}

	@Override
	public void setPowerStatus(boolean hasPower) {
		powered = hasPower;
	}

	@Override
	public boolean isPowered() {
		return powered;
	}

	@Override
	public IGridInterface getGrid() {
		return grid;
	}

	@Override
	public void setGrid(IGridInterface gi) {
		grid = gi;
	}

	@Override
	public World getWorld() {
		return worldObj;
	}

	@Override
	public float getPowerDrainPerTick() {
		return 1.0F;
	}
	
	@Override
	public void onLoaded() {
		super.onLoaded();
		MinecraftForge.EVENT_BUS.post(new GridTileLoadEvent(this, worldObj, getLocation()));
	}
	
	@Override
	public void onUnloaded() {
		super.onUnloaded();
		MinecraftForge.EVENT_BUS.post(new GridTileUnloadEvent(this, worldObj, getLocation()));
	}
}
