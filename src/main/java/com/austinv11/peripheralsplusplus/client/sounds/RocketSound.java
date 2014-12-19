package com.austinv11.peripheralsplusplus.client.sounds;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RocketSound extends MovingSound {

	private EntityRocket theRocket;
	private double startY;
	public static float MAX_VOLUME = 1.5F;

	protected RocketSound(ResourceLocation p_i45104_1_) {
		super(p_i45104_1_);
	}

	public RocketSound(EntityRocket rocket) {
		this(new ResourceLocation(Reference.MOD_ID.toLowerCase()+":Rocket"));
		theRocket = rocket;
		this.field_147666_i = AttenuationType.NONE;
		this.volume = 0.000001F;
		this.field_147663_c = 0.0F; //Pitch
		this.repeat = true;
		this.field_147665_h = 0; //Repeat delay
		this.updateSoundLocation(rocket);
		startY = rocket.posY;
	}

	@Override
	public void update() {
		if (!theRocket.isDead) {
			if (theRocket.getIsActive()) {
				if (theRocket.countDown >= 0) {
					if (field_147663_c < 1.0F)
						field_147663_c += 0.005F;
					else
						field_147663_c = 1.0F;
				}
			} else {
				donePlaying = true;
			}
			float newVolume = theRocket.countDown > 0 && theRocket.countDown <= 7 ? MAX_VOLUME/theRocket.countDown : (float)((4*startY) / theRocket.posY);
			if (theRocket.countDown > 7)
				newVolume = 0.000001F;
			volume = newVolume > MAX_VOLUME ? MAX_VOLUME : newVolume;
			updateSoundLocation(theRocket);
		} else
			donePlaying = true;
	}

	private void updateSoundLocation(Entity ent) {
		this.xPosF = (float)ent.posX;
		this.yPosF = (float)ent.posY;
		this.zPosF = (float)ent.posZ;
	}
}
