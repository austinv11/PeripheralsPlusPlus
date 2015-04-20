package com.austinv11.peripheralsplusplus.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityNanoBotSwarm extends EntityThrowable {
	
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
	protected void onImpact(MovingObjectPosition mop) {
		
	}
}
