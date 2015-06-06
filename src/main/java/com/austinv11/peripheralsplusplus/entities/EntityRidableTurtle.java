package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.RidableTurtlePacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class EntityRidableTurtle extends Entity {

	private ITurtleAccess turtle = null;
	private int queuedActionCode = -1;
	private int tick = 0;
	private boolean canPerformAction = true;
	private int[] turtleLastPos;

	public EntityRidableTurtle(World world) {
		super(world);
		this.setSize(1, 1);
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		turtleLastPos = nbtTagCompound.getIntArray("turtleLastPos");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		if (turtle != null)
			nbtTagCompound.setIntArray("turtleLastPos", new int[]{turtle.getPosition().posX, turtle.getPosition().posY,
				turtle.getPosition().posZ});
	}

	@Override
	public void setPositionAndRotation2(double x, double y, double z, float pitch, float yaw, int par) {
		this.setPosition(x, y, z);
		this.setRotation(pitch, yaw);
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.worldObj.isRemote && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||
				playerIsHoldingRideEnablingItem());
	}

	private boolean playerIsHoldingRideEnablingItem() {
		if (!this.worldObj.isRemote) {
			return false;
		}
		ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
		return item != null && (item.isItemEqual(new ItemStack(Items.carrot_on_a_stick)) || item.isItemEqual(new ItemStack(Items.stick)));
	}

	@Override
	public boolean interactFirst(EntityPlayer player) {
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer &&
				this.riddenByEntity != player) {
			return true;
		} else if (this.riddenByEntity != null && this.riddenByEntity != player) {
			return false;
		} else {
			if (!this.worldObj.isRemote) {
				player.mountEntity(this);
			}
			return true;
		}
	}

	@Override
	public void onUpdate() {
		checkRemove();
		checkMovementQueue();
		checkPlayerMovementRequest();
		checkLocation();
		updateTick();
	}

	private void checkRemove() {
		if (turtleLastPos != null) {
			try {
				TileEntity turtleTile = this.worldObj.getTileEntity(turtleLastPos[0], turtleLastPos[1], turtleLastPos[2]);
				this.turtle = ReflectionHelper.getTurtle(turtleTile);
			} catch (Exception ignored) {}
			turtleLastPos = null;
		}
		if (!this.worldObj.isRemote && (turtle == null || !isTurleInWorld())) {
			this.worldObj.removeEntity(this);
		}
	}

	private boolean isTurleInWorld() {
		TileEntity turtleTile = this.worldObj.getTileEntity(turtle.getPosition().posX, turtle.getPosition().posY, turtle.getPosition().posZ);
		return turtleTile != null;
	}

	private void updateTick() {
		if (canPerformAction) {
			return;
		}
		if (tick < 10) { // ~.5 second
			tick++;
		} else {
			tick = 0;
			canPerformAction = true;
		}
	}

	private void checkLocation() {
		if (this.worldObj.isRemote || turtle == null || turtle.getPosition().equals(new ChunkCoordinates(
				(int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ)))) {
			return;
		}
		this.setPosition(turtle.getPosition().posX + 0.5, turtle.getPosition().posY, turtle.getPosition().posZ + 0.5);
	}

	private void checkMovementQueue() {
		if ((!canPerformAction) || this.worldObj.isRemote || queuedActionCode < 0
				|| queuedActionCode > RidableTurtlePacket.MovementCode.values().length - 1 || turtle == null) {
			return;
		}
		RidableTurtlePacket.MovementCode which = RidableTurtlePacket.MovementCode.values()[queuedActionCode];
		switch (which) {
			case FORWARD:
				moveTurtle(0);
				break;
			case TURN_LEFT:
				turnTurtle("left");
				break;
			case TURN_RIGHT:
				turnTurtle("right");
				break;
			case DESCEND:
				moveTurtle(1);
				break;
			case ASCEND:
				moveTurtle(2);
				break;
		}
		queuedActionCode = -1;
		canPerformAction = false;
	}

	private void turnTurtle(String turnTo) {
		int[] directions = {2, 5, 3, 4};
		int index = 0;
		for (int i = 0; i < directions.length; i++) {
			if (directions[i] == turtle.getDirection()) {
				index = i;
				break;
			}
		}
		if (turnTo.equalsIgnoreCase("left")) {
			index--;
		} else {
			index++;
		}
		if (index < 0) {
			index = directions.length - 1;
		}
		if (index >= directions.length) {
			index = 0;
		}
		turtle.setDirection(directions[index]);
		if (turnTo.equalsIgnoreCase("left")) {
			turtle.playAnimation(TurtleAnimation.TurnLeft);
		} else {
			turtle.playAnimation(TurtleAnimation.TurnRight);
		}
	}

	private void moveTurtle(int direction) {
		int x = turtle.getPosition().posX;
		int y = turtle.getPosition().posY;
		int z = turtle.getPosition().posZ;
		TurtleAnimation animation = TurtleAnimation.None;
		switch (direction) {
			case 0: // Forward
				switch (turtle.getDirection()) {
					case 2: // North
						z--;
						break;
					case 5: // East
						x++;
						break;
					case 3: // South
						z++;
						break;
					case 4: // West
						x--;
						break;
				}
				animation = TurtleAnimation.MoveForward;
				break;
			case 1: // Descend
				y--;
				animation = TurtleAnimation.MoveDown;
				break;
			case 2: // Ascend
				y++;
				animation = TurtleAnimation.MoveUp;
				break;
		}
		if (this.worldObj.isAirBlock(x, y, z) && (turtle.getFuelLevel() >= Config.fuelPerTurtleMovement || !turtle.isFuelNeeded())) {
			turtle.playAnimation(animation);
			turtle.teleportTo(turtle.getWorld(), x, y, z);
			turtle.consumeFuel(Config.fuelPerTurtleMovement);
		}
	}

	private void checkPlayerMovementRequest() {
		if (this.worldObj.isRemote && this.riddenByEntity != null &&
				this.riddenByEntity == Minecraft.getMinecraft().thePlayer) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) { // TODO Forge keybinds
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(this.getEntityId(),
						RidableTurtlePacket.MovementCode.FORWARD.code, this.dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(this.getEntityId(),
						RidableTurtlePacket.MovementCode.TURN_RIGHT.code, this.dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(this.getEntityId(),
						RidableTurtlePacket.MovementCode.TURN_LEFT.code, this.dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(this.getEntityId(),
						RidableTurtlePacket.MovementCode.DESCEND.code, this.dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(this.getEntityId(),
						RidableTurtlePacket.MovementCode.ASCEND.code, this.dimension));
			}
		}
	}

	public void setTurtle(ITurtleAccess turtle) {
		this.turtle = turtle;
	}

	public void queueAction(int movementCode) {
		if (canPerformAction) {
			this.queuedActionCode = movementCode;
		}
	}
}
