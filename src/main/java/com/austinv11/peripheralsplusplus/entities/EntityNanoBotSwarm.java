package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.items.ItemNanoSwarm;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityNanoBotSwarm extends EntityThrowable {
	
	public UUID antennaIdentifier;
	public String label;
	
	public EntityNanoBotSwarm(World world) {
		super(world);
	}
	
	public EntityNanoBotSwarm(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}
	
	public EntityNanoBotSwarm(World world, double x, double y, double z) {
		super(world, x, y, z);
	}
	
	@Override
	protected void onImpact(RayTraceResult mop) {
		if (!world.isRemote) {
			if (mop.typeOfHit == RayTraceResult.Type.ENTITY) {
				mop.entityHit.attackEntityFrom(new DamageSource(Reference.MOD_ID.toLowerCase()+".nanobots"), 0);
				ItemNanoSwarm.addSwarmForEntity(this, mop.entityHit);
			} else if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
				ItemStack stack = new ItemStack(ModItems.NANO_SWARM);
				NBTHelper.setString(stack, "identifier", antennaIdentifier.toString());
				List<String> info = new ArrayList<String>();
				
				if (label == null) {
					info.add(Colors.RESET.toString()+Colors.GRAY+antennaIdentifier.toString());
				} else {
					info.add(Colors.RESET.toString()+Colors.GRAY+label);
					NBTHelper.setString(stack, "label", label);
				}
				
				NBTHelper.setInfo(stack, info);
				EnumFacing direction = mop.sideHit;
				EntityItem entityItem = new EntityItem(this.world,
						this.posX+direction.getFrontOffsetX(), this.posY+direction.getFrontOffsetY(),
								this.posZ+direction.getFrontOffsetZ(), stack);
				world.spawnEntity(entityItem);
			}
			this.setDead();
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setString("antennaIdentifier", antennaIdentifier.toString());
		tag.setBoolean("hasLabel", label != null);
		if (label != null)
			tag.setString("label", label);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		antennaIdentifier = UUID.fromString(tag.getString("antennaIdentifier"));
		if (tag.getBoolean("hashLabel"))
			label = tag.getString("label");
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0.0075F;
	}
}
