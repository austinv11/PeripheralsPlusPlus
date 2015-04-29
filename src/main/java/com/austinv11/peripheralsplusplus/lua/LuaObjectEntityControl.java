package com.austinv11.peripheralsplusplus.lua;

import com.austinv11.collectiveframework.minecraft.utils.Location;
import com.austinv11.collectiveframework.minecraft.utils.WorldUtils;
import com.austinv11.collectiveframework.utils.math.MathUtils;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.entities.NanoProperties;
import com.austinv11.peripheralsplusplus.items.ItemNanoSwarm;
import com.austinv11.peripheralsplusplus.network.RobotEventPacket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;

import java.util.UUID;

public class LuaObjectEntityControl implements ILuaObject {
	
	private UUID id;
	
	private boolean isPlayer;
	private EntityPlayer player;
	private EntityLiving entity;
	
	public LuaObjectEntityControl(UUID id, Entity entity) {
		this.id = id;
		if (entity instanceof EntityPlayer) {
			isPlayer = true;
			player = (EntityPlayer) entity;
		} else {
			isPlayer = false;
			this.entity = (EntityLiving) entity;
		}
	}
	
	@Override
	public String[] getMethodNames() {
		if (isPlayer)
			return new String[]{"isPlayer", "hurt", "heal", "getHealth", "getMaxHealth", "isDead", "getRemainingBots", "getDisplayName",
					"getPlayerName", "getUUID", "getHunger", "click", "clickRelease", "keyPress", "keyRelease", "mouseMove", "whisper"};
		else
			return new String[]{"isPlayer", "hurt", "heal", "getHealth", "getMaxHealth", "isDead", "getRemainingBots", "getDisplayName",
					"getEntityName", "setTarget", "setAttackTarget", "setMovementTarget", "setTurnAngle", "toggleJumping"};
	}
	
	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (method < 8) {
			switch (method) {
				case 0:
					return new Object[]{isPlayer};
				
				case 1:
					if (isPlayer) {
						if (!ItemNanoSwarm.doInstruction(id, player)) 
							return new Object[]{false};
						player.attackEntityFrom(new DamageSource(Reference.MOD_ID.toLowerCase()+".nanobot").setDamageBypassesArmor(), 1.0F);
					} else {
						if (!ItemNanoSwarm.doInstruction(id, entity)) 
							return new Object[]{false};
						entity.attackEntityFrom(new DamageSource(Reference.MOD_ID.toLowerCase()+".nanobot").setDamageBypassesArmor(), 1.0F);
					}
					break;
					
				case 2:
					if (isPlayer) {
						if (!ItemNanoSwarm.doInstruction(id, player))
							return new Object[]{false};
						player.heal(1.0F);
					} else {
						if (!ItemNanoSwarm.doInstruction(id, entity))
							return new Object[]{false};
						player.heal(1.0F);
					}
					break;
					
				case 3:
					if (isPlayer) {
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{player.getHealth()};
					} else {
						if (!ItemNanoSwarm.doInstruction(id, entity))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{entity.getHealth()};
					}
					
				case 4:
					if (isPlayer) {
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{player.getMaxHealth()};
					} else {
						if (!ItemNanoSwarm.doInstruction(id, entity))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{entity.getMaxHealth()};
					}
					
				case 5:
					if (isPlayer) {
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{player.isDead};
					} else {
						if (!ItemNanoSwarm.doInstruction(id, entity))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{entity.isDead};
					}
					
				case 6:
					if (isPlayer) {
						return new Object[]{((NanoProperties)player.getExtendedProperties(NanoProperties.IDENTIFIER)).numOfBots};
					} else {
						return new Object[]{((NanoProperties)entity.getExtendedProperties(NanoProperties.IDENTIFIER)).numOfBots};
					}
					
				case 7:
					if (isPlayer) {
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{player.getDisplayName()};
					} else {
						if (!ItemNanoSwarm.doInstruction(id, entity))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{entity.getCustomNameTag()};
					}
			}
		} else {
			if (isPlayer) {
				switch (method) {
					case 8:
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{player.getGameProfile().getName()};
					
					case 9:
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{player.getGameProfile().getId().toString()};
					
					case 10:
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{player.getFoodStats().getFoodLevel()};
					
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						if (arguments.length < 1 || (method == 15 && arguments.length < 2))
							throw new LuaException("Too few arguments");
						if (!(arguments[0] instanceof Double && method < 13) &&
								!(arguments[0] instanceof String && method < 15) && !(arguments[0] instanceof Double && method == 15))
							throw new LuaException("Bad argument #1 (expected string or number)");
						if (arguments.length > 1 && !(arguments[1] instanceof Double))
							throw new LuaException("Bad argument #2 (expected number)");
						if (!ItemNanoSwarm.doInstruction(id, player))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						RobotEventPacket.PressType type = MathUtils.isEvenNumber(method) ? RobotEventPacket.PressType.RELEASE : RobotEventPacket.PressType.PRESS;
						RobotEventPacket.ActionType action = method < 13 ? RobotEventPacket.ActionType.MOUSE_CLICK : method < 15 ? RobotEventPacket.ActionType.KEYBOARD : RobotEventPacket.ActionType.MOUSE_MOVE;
						Object args = arguments.length == 1 && arguments[0] instanceof Double ? (Integer)(int)(double)(Double)arguments[0] : 
								arguments[0] instanceof String ? (String)arguments[0] : new int[]{(int)(double)(Double)arguments[0], (int)(double)(Double)arguments[1]};
						PeripheralsPlusPlus.NETWORK.sendTo(new RobotEventPacket(action, type, args), (EntityPlayerMP) player);
						break;
					
					case 16:
						if (arguments.length < 1)
							throw new LuaException("Too few arguments");
						if (!(arguments[0] instanceof String))
							throw new LuaException("Bad argument #1 (expected string)");
						if (arguments.length > 1 && !(arguments[1] instanceof String))
							throw new LuaException("Bad argument #2 (expected string)");
						if (!ItemNanoSwarm.doInstruction(id, player))
							return new Object[]{false};
						String sender = arguments.length > 1 ? "<"+arguments[1]+"> " : "";
						player.addChatComponentMessage(new ChatComponentText(sender+arguments[0]));
						break;
				}
				
			} else {
				switch (method) {
					case 8:
						if (!ItemNanoSwarm.doInstruction(id, entity))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						return new Object[]{entity.getClass().getSimpleName()};
					
					case 9:
						if (arguments.length < 1)
							throw new LuaException("Too few arguments");
						if (!(arguments[0] instanceof String || arguments[0] instanceof Double))
							throw new LuaException("Bad argument #1 (expected string or number)");
						if (arguments[0] instanceof Double && !(arguments.length > 1 && arguments[1] instanceof Double))
							throw new LuaException("Bad argument #2 (expected number)");
						if (arguments[0] instanceof Double && !(arguments.length > 2 && arguments[2] instanceof Double))
							throw new LuaException("Bad argument #3 (expected number)");
						if (!ItemNanoSwarm.doInstruction(id, entity))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						Entity target;
						if (arguments[0] instanceof String)
							target = WorldUtils.getPlayerForWorld((String)arguments[0], entity.worldObj);
						else
							target = WorldUtils.getNearestEntityToLocation(new Location((Double)arguments[0],
									(Double)arguments[1], (Double)arguments[2], entity.worldObj));
						entity.currentTarget = target;
						return new Object[]{target != null};
					
					case 10:
						if (arguments.length < 1)
							throw new LuaException("Too few arguments");
						if (!(arguments[0] instanceof String || arguments[0] instanceof Double))
							throw new LuaException("Bad argument #1 (expected string or number)");
						if (arguments[0] instanceof Double && !(arguments.length > 1 && arguments[1] instanceof Double))
							throw new LuaException("Bad argument #2 (expected number)");
						if (arguments[0] instanceof Double && !(arguments.length > 2 && arguments[2] instanceof Double))
							throw new LuaException("Bad argument #3 (expected number)");
						if (!ItemNanoSwarm.doInstruction(id, entity))
							throw new LuaException("Entity with id "+id+" cannot be interacted with");
						Entity attackTarget;
						if (arguments[0] instanceof String)
							attackTarget = WorldUtils.getPlayerForWorld((String)arguments[0], entity.worldObj);
						else
							attackTarget = WorldUtils.getNearestEntityToLocation(new Location((Double)arguments[0],
									(Double)arguments[1], (Double)arguments[2], entity.worldObj));
						entity.setAttackTarget((EntityLivingBase) attackTarget);
						return new Object[]{attackTarget != null};
					
					case 11:
						if (arguments.length < 3)
							throw new LuaException("Too few arguments");
						if (!(arguments[0] instanceof Double))
							throw new LuaException("Bad argument #1 (expected number)");
						if (!(arguments[1] instanceof Double))
							throw new LuaException("Bad argument #2 (expected number)");
						if (!(arguments[2] instanceof Double))
							throw new LuaException("Bad argument #3 (expected number)");
						if (!ItemNanoSwarm.doInstruction(id, entity))
							return new Object[]{false};
						entity.navigator.setPath(entity.navigator.getPathToXYZ((Double)arguments[0], (Double)arguments[1],
								(Double)arguments[2]), entity.getAIMoveSpeed());
						break;
						
					case 12:
						if (arguments.length < 1)
							throw new LuaException("Too few arguments");
						if (!(arguments[0] instanceof Double))
							throw new LuaException("Bad argument #1 (expected number)");
						if (!ItemNanoSwarm.doInstruction(id, entity))
							return new Object[]{false};
						entity.rotationYaw = (float)(double)(Double)arguments[0];
						entity.rotationYawHead = (float)(double)(Double)arguments[0];
						break;
						
					case 13:
						if (!ItemNanoSwarm.doInstruction(id, entity))
							return new Object[]{false};
						entity.setJumping(!entity.isJumping);
						break;
				}
			}
		}
		return new Object[]{true};
	}
}
