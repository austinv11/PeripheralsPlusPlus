package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.collectiveframework.language.translation.TranslationException;
import com.austinv11.collectiveframework.minecraft.utils.MinecraftTranslator;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.RidableTurtlePacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRidableTurtle extends MountedEntity {

	private long lastUpdateTime = 0;
	private ITurtleAccess turtle;

	public EntityRidableTurtle(World world) {
		super(world);
		setSize(1, 1);
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {}

	@Override
	public boolean canBeCollidedWith() {
		return this.world.isRemote && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||
				playerIsHoldingRideEnablingItem());
	}

	private boolean playerIsHoldingRideEnablingItem() {
		if (!this.world.isRemote) {
			return false;
		}
		ItemStack item = Minecraft.getMinecraft().player.getHeldItemMainhand();
		return !item.isEmpty() && (item.isItemEqual(new ItemStack(Items.CARROT_ON_A_STICK)) ||
				item.isItemEqual(new ItemStack(Items.STICK)));
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (!hand.equals(EnumHand.MAIN_HAND))
			return false;
		if (this.isBeingRidden() && this.getControllingPassenger() instanceof EntityPlayer &&
				this.getControllingPassenger() != player)
			return true;
		else if (this.isBeingRidden() && this.getControllingPassenger() != player)
			return false;
		else {
			if (!this.world.isRemote)
				player.startRiding(this);
			return true;
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!isTurtleInWorld()) {
			world.removeEntity(this);
			return;
		}
		checkPlayerMovementRequest();
	}

	public void update(ITurtleAccess turtle) {
		setTurtle(turtle);
		setWorld(turtle.getWorld());
		moveToTurtlePosition();
	}

	/**
	 * Checks if the turtle is in the world
	 * @return false if the check fails
	 */
	private boolean isTurtleInWorld() {
		if (world.isRemote)
			return true;
		if (turtle == null)
			return false;
		TileEntity tileEntity = world.getTileEntity(turtle.getPosition());
		if (tileEntity == null)
			return false;
		try {
			ITurtleAccess worldTurtle = ReflectionHelper.getTurtle(tileEntity);
			if (worldTurtle == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private void moveToTurtlePosition() {
		if (world.isRemote || turtle == null)
			return;
		Vec3d pos = new Vec3d(turtle.getPosition()).addVector(0.5, 0, 0.5);
		posX = pos.x;
		posY = pos.y;
		posZ = pos.z;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}

	public boolean tryMove(MovementCode action) {
		boolean canPerformAction = System.currentTimeMillis() - lastUpdateTime > 500;
		if ((!canPerformAction) || this.world.isRemote || turtle == null)
			return false;
		switch (action) {
			case FORWARD:
			case DESCEND:
			case ASCEND:
				moveTurtle(action, turtle);
				break;
			case TURN_LEFT:
			case TURN_RIGHT:
				turnTurtle(action, turtle);
				break;
		}
		lastUpdateTime = System.currentTimeMillis();
		return true;
	}

	private void turnTurtle(MovementCode turnTo, ITurtleAccess turtle) {
		switch (turnTo) {
			case TURN_LEFT:
				turtle.setDirection(turtle.getDirection().rotateYCCW());
				turtle.playAnimation(TurtleAnimation.TurnLeft);
				break;
			case TURN_RIGHT:
				turtle.setDirection(turtle.getDirection().rotateY());
				turtle.playAnimation(TurtleAnimation.TurnRight);
				break;
		}
	}

	private void moveTurtle(MovementCode direction, ITurtleAccess turtle) {
		int x = turtle.getPosition().getX();
		int y = turtle.getPosition().getY();
		int z = turtle.getPosition().getZ();
		TurtleAnimation animation = TurtleAnimation.None;
		switch (direction) {
			case FORWARD:
				switch (turtle.getDirection()) {
					case NORTH:
						z--;
						break;
					case EAST:
						x++;
						break;
					case SOUTH:
						z++;
						break;
					case WEST:
						x--;
						break;
				}
				animation = TurtleAnimation.MoveForward;
				break;
			case DESCEND:
				y--;
				animation = TurtleAnimation.MoveDown;
				break;
			case ASCEND:
				y++;
				animation = TurtleAnimation.MoveUp;
				break;
		}
		if (this.world.isAirBlock(new BlockPos(x, y, z)) &&
				(turtle.getFuelLevel() >= Config.fuelPerTurtleMovement || !turtle.isFuelNeeded())) {
			turtle.teleportTo(turtle.getWorld(), new BlockPos(x, y, z));
			turtle.playAnimation(animation);
			turtle.consumeFuel(Config.fuelPerTurtleMovement);
		}
	}

	private void checkPlayerMovementRequest() {
		if (this.world.isRemote && this.getControllingPassenger() != null &&
				this.getControllingPassenger() == Minecraft.getMinecraft().player) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) { // TODO Forge keybinds
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(getPersistentID(),
					MovementCode.FORWARD, dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(getPersistentID(),
						MovementCode.TURN_RIGHT, this.dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(getPersistentID(),
						MovementCode.TURN_LEFT, this.dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(getPersistentID(),
						MovementCode.DESCEND, this.dimension));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
				PeripheralsPlusPlus.NETWORK.sendToServer(new RidableTurtlePacket(getPersistentID(),
						MovementCode.ASCEND, this.dimension));
			}
		}
	}

	public void setTurtle(ITurtleAccess turtle) {
		this.turtle = turtle;
	}

	@Nullable
	@Override
	public Entity getControllingPassenger() {
		return getPassengers().size() > 0 ? getPassengers().get(0) : null;
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
	}

	@Override
	public void onKillCommand() {
		// Ignore
	}

	@Override
	public boolean startRiding(Entity entityIn, boolean force) {
		return false;
	}

	@Override
	protected void doBlockCollisions() {
		// Ignore
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		// Ignore
	}

	@Override
	public String getType() {
		return "ridable";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {
				/*
				getEntity()
				Returns an object with details of the current entity riding the turtle
					[name string, type name, UUID]
				 */
				"getEntity",
				/*
				mountNearbyEntity()/mountNearestEntity()/mount()
				Mounts the nearest entity found on top of the turtle
				Returns false if no entity could be mounted
				 */
				"mountNearbyEntity", "mountNearestEntity", "mount",
				/*
				Unmounts the current entity riding the turtle
				 */
				"unmount",
				/*
				Moves the turtle up, ignoring the ridable turtle entity
				 */
				"up"
		};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableRidableTurtle)
			throw new LuaException("Ridable turtles have been disabled.");
		switch (method) {
			case 0:
				return getEntityRidingTurtle();
			case 1:
			case 2:
			case 3:
				return mountNearbyEntity();
			case 4:
				return unmountCurrentEntity();
			case 5:
				return moveTurtleUp();
		}
		return new Object[0];
	}

	private Object[] moveTurtleUp() {
		String failMessage = "";
		try {
			if (!turtle.getWorld().isAirBlock(turtle.getPosition().up()))
				failMessage = MinecraftTranslator.translateToLocal(
						"peripheralsplusplus.message.upgrade.ridable_blocked");
			if (turtle.getFuelLevel() < Config.fuelPerTurtleMovement && turtle.isFuelNeeded())
				failMessage = MinecraftTranslator.translateToLocal(
						"peripheralsplusplus.message.upgrade.ridable_no_fuel");
		}
		catch (TranslationException | IOException e) {
			failMessage = "Unknown error: " + e.getMessage();
		}
		if (failMessage.isEmpty())
			return new Object[]{tryMove(MovementCode.ASCEND)};
		else
			return new Object[]{false, failMessage};
	}

	private Object[] unmountCurrentEntity() {
		Entity passenger = getControllingPassenger();
		if (passenger != null)
			passenger.dismountRidingEntity();
		return new Object[]{true};
	}

	private Object[] mountNearbyEntity() {
		List entities = getNearbyEntities(turtle, 1, 2, Entity.class);
		Entity entity = null;
		for (Object ent : entities) {
			if (((Entity)ent).isEntityAlive() && !(ent instanceof EntityRidableTurtle)) {
				entity = (Entity) ent;
				break;
			}
		}
		if (entity == null || getControllingPassenger() != null)
			return new Object[]{false};
		return new Object[]{entity.startRiding(this, true)};
	}

	public static List getNearbyEntities(ITurtleAccess turtle, int radiusStart, int radiusEnd, Class entityType) {
		World world = turtle.getWorld();
		AxisAlignedBB bb = new AxisAlignedBB(
				turtle.getPosition().getX() - radiusStart, turtle.getPosition().getY() - radiusStart,
				turtle.getPosition().getZ() - radiusStart, turtle.getPosition().getX() + radiusEnd,
				turtle.getPosition().getY() + radiusEnd, turtle.getPosition().getZ() + radiusEnd);
		return world.getEntitiesWithinAABB(entityType, bb);
	}

	private Object[] getEntityRidingTurtle() {
		Entity ridingEntity = getControllingPassenger();
		Map<String, Object> map = new HashMap<>();
		if (ridingEntity != null) {
			map.put("name", ridingEntity instanceof EntityPlayer ?
					((EntityPlayer) ridingEntity).getDisplayNameString() :
					EntityList.getEntityString(ridingEntity));
			map.put("type", ridingEntity.getClass().getSimpleName());
			map.put("uuid", ridingEntity instanceof EntityPlayer ?
					((EntityPlayer) ridingEntity).getGameProfile().getId().toString() :
					ridingEntity.getUniqueID().toString());
		}
		return new Object[]{map};
	}

	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}

	public enum MovementCode {
		FORWARD, TURN_LEFT, TURN_RIGHT, ASCEND, DESCEND
	}
}
