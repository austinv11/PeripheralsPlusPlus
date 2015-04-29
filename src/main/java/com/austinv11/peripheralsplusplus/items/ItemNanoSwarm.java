package com.austinv11.peripheralsplusplus.items;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.entities.EntityNanoBotSwarm;
import com.austinv11.peripheralsplusplus.entities.NanoProperties;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemNanoSwarm extends ItemPPP {
	
	private static HashMap<UUID, List<Entity>> swarmNetwork = new HashMap<UUID, List<Entity>>();
	
	public ItemNanoSwarm() {
		super();
		this.setMaxStackSize(16);
		this.setUnlocalizedName("nanoSwarm");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (NBTHelper.hasTag(stack, "identifier")) {
			if (!world.isRemote) {
				EntityNanoBotSwarm swarm = new EntityNanoBotSwarm(world, player);
				swarm.antennaIdentifier = UUID.fromString(NBTHelper.getString(stack, "identifier"));
				if (NBTHelper.hasTag(stack, "label"))
					swarm.label = NBTHelper.getString(stack, "label");
				world.spawnEntityInWorld(swarm);
			}
			stack.stackSize--;
		}
		return stack;
	}
	
	public static void addSwarmForEntity(EntityNanoBotSwarm swarm, Entity hit) {
		if (TileEntityAntenna.antenna_registry.containsKey(swarm.antennaIdentifier)) {
			TileEntityAntenna antenna = TileEntityAntenna.antenna_registry.get(swarm.antennaIdentifier);
			if (!antenna.swarmNetwork.contains(hit)) {
				antenna.swarmNetwork.add(hit);
			}
			NanoProperties properties = (NanoProperties)hit.getExtendedProperties(NanoProperties.IDENTIFIER);
			properties.numOfBots += Config.numberOfInstructions;
			properties.antenna = swarm.antennaIdentifier;
		}
	}
	
	public static boolean doInstruction(UUID identifier, Entity performer) {
		if (!performer.isDead)
			if (TileEntityAntenna.antenna_registry.containsKey(identifier)) {
				TileEntityAntenna antenna = TileEntityAntenna.antenna_registry.get(identifier);
				if (antenna.swarmNetwork.contains(performer)) {
					NanoProperties properties = (NanoProperties)performer.getExtendedProperties(NanoProperties.IDENTIFIER);
					properties.numOfBots--;
					if (properties.numOfBots == 0)
						antenna.swarmNetwork.remove(performer);
					return true;
				}
			}
		return false; //Do not do the instruction
	}
	
	public static class BehaviorNanoSwarm extends BehaviorDefaultDispenseItem { //Copied mostly from BehaviorProjectileDispense
		
		public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stack) {
			if (NBTHelper.hasTag(stack, "identifier")) {
				World world = blockSource.getWorld();
				IPosition iposition = BlockDispenser.func_149939_a(blockSource);
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				EntityNanoBotSwarm iprojectile = new EntityNanoBotSwarm(world, iposition.getX(), iposition.getY(), iposition.getZ());
				iprojectile.antennaIdentifier = UUID.fromString(NBTHelper.getString(stack, "identifier"));
				if (NBTHelper.hasTag(stack, "label"))
					iprojectile.label = NBTHelper.getString(stack, "label");
				iprojectile.setThrowableHeading((double) enumfacing.getFrontOffsetX(), (double) ((float) enumfacing.getFrontOffsetY()+0.1F), (double) enumfacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
				world.spawnEntityInWorld(iprojectile);
				stack.splitStack(1);
			} else {
				return super.dispenseStack(blockSource, stack);
			}
			return stack;
		}
		
		protected void playDispenseSound(IBlockSource blockSource) {
			blockSource.getWorld().playAuxSFX(1002, blockSource.getXInt(), blockSource.getYInt(), blockSource.getZInt(), 0);
		}
		
		protected float func_82498_a() {
			return 6.0F;
		}
		
		protected float func_82500_b() {
			return 1.1F;
		}
	}
}
