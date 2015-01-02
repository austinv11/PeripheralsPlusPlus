package miscperipherals.util;

import miscperipherals.tile.Tile;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import dan200.turtle.api.ITurtleAccess;

public interface Positionable {
	public Vec3 getPosition();
	public int getFacing();
	public World getWorld();
	public IInventory getInventory();
	
	public static class PositionableTile implements Positionable {
		private final TileEntity tile;
		
		public PositionableTile(TileEntity tile) {
			this.tile = tile;
		}
		
		@Override
		public Vec3 getPosition() {
			return tile == null ? null : Vec3.createVectorHelper(tile.xCoord, tile.yCoord, tile.zCoord);
		}
		
		@Override
		public int getFacing() {
			return tile instanceof Tile ? ((Tile) tile).getFacing() : 0;
		}

		@Override
		public World getWorld() {
			return tile == null ? null : tile.worldObj;
		}

		@Override
		public IInventory getInventory() {
			return tile instanceof IInventory ? (IInventory)tile : null;
		}		
	}

	public static class PositionableTurtle implements Positionable {
		private final ITurtleAccess turtle;
		
		public PositionableTurtle(ITurtleAccess turtle) {
			this.turtle = turtle;
		}
		
		@Override
		public Vec3 getPosition() {
			return turtle == null ? null : turtle.getVisualPosition(1.0F);
		}
		
		@Override
		public int getFacing() {
			return turtle.getFacingDir();
		}

		@Override
		public World getWorld() {
			return turtle == null ? null : turtle.getWorld();
		}

		@Override
		public IInventory getInventory() {
			return turtle instanceof IInventory ? (IInventory)turtle : null;
		}
	}
}
