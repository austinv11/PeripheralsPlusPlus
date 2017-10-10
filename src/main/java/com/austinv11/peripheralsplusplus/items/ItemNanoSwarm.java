package com.austinv11.peripheralsplusplus.items;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.capabilities.nano.CapabilityNanoBot;
import com.austinv11.peripheralsplusplus.capabilities.nano.NanoBotHolder;
import com.austinv11.peripheralsplusplus.entities.EntityNanoBotSwarm;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.UUID;

public class ItemNanoSwarm extends ItemPPP {
	
	public ItemNanoSwarm() {
		super();
		this.setMaxStackSize(16);
		this.setRegistryName("nano_swarm");
		this.setUnlocalizedName("nano_swarm");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (NBTHelper.hasTag(player.getHeldItem(hand), "identifier")) {
			if (!world.isRemote) {
				EntityNanoBotSwarm swarm = new EntityNanoBotSwarm(world, player);
				swarm.antennaIdentifier = UUID.fromString(NBTHelper.getString(player.getHeldItem(hand),
						"identifier"));
				if (NBTHelper.hasTag(player.getHeldItem(hand), "label"))
					swarm.label = NBTHelper.getString(player.getHeldItem(hand), "label");
				swarm.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw,
						0, 1.1f, 6);
				world.spawnEntity(swarm);
			}
			player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
			return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
	}
	
	public static void addSwarmForEntity(EntityNanoBotSwarm swarm, Entity hit) {
		if (TileEntityAntenna.ANTENNA_REGISTRY.containsKey(swarm.antennaIdentifier)) {
			TileEntityAntenna antenna = TileEntityAntenna.ANTENNA_REGISTRY.get(swarm.antennaIdentifier);
			antenna.registerEntity(hit);
			NanoBotHolder properties = hit.getCapability(CapabilityNanoBot.INSTANCE, null);
			if (properties == null)
				return;
			properties.setBots(properties.getBots() + Config.numberOfInstructions);
			properties.setAntenna(swarm.antennaIdentifier);
		}
	}
	
	public static boolean doInstruction(UUID identifier, Entity performer) {
		if (!performer.isDead)
			if (TileEntityAntenna.ANTENNA_REGISTRY.containsKey(identifier)) {
				TileEntityAntenna antenna = TileEntityAntenna.ANTENNA_REGISTRY.get(identifier);
				if (antenna.isEntityRegistered(performer)) {
					NanoBotHolder properties = performer.getCapability(CapabilityNanoBot.INSTANCE, null);
					if (properties == null)
						return false;
					properties.setBots(properties.getBots() - 1);
					if (properties.getBots() <= 0)
						antenna.removeEntity(performer);
					return true;
				}
			}
		return false; //Do not do the instruction
	}
	
	public static class BehaviorNanoSwarm extends BehaviorDefaultDispenseItem { //Copied mostly from BehaviorProjectileDispense

		@Override
		public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stack) {
			if (NBTHelper.hasTag(stack, "identifier")) {
				World world = blockSource.getWorld();
				IPosition iposition = BlockDispenser.getDispensePosition(blockSource);
				EnumFacing enumfacing = blockSource.getBlockState().getValue(BlockDispenser.FACING);
				EntityNanoBotSwarm iprojectile = new EntityNanoBotSwarm(world, iposition.getX(), iposition.getY(), iposition.getZ());
				iprojectile.antennaIdentifier = UUID.fromString(NBTHelper.getString(stack, "identifier"));
				if (NBTHelper.hasTag(stack, "label"))
					iprojectile.label = NBTHelper.getString(stack, "label");
				iprojectile.setThrowableHeading(
						enumfacing.getFrontOffsetX(),
						enumfacing.getFrontOffsetY() + 0.1f,
						enumfacing.getFrontOffsetZ(),
						1.1f,
						6);
				world.spawnEntity(iprojectile);
				stack.splitStack(1);
			} else {
				return super.dispenseStack(blockSource, stack);
			}
			return stack;
		}
	}
}
