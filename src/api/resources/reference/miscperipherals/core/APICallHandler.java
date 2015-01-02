package miscperipherals.core;

import java.util.UUID;

import miscperipherals.api.ISmEntityFactory;
import miscperipherals.api.ISmSender;
import miscperipherals.api.MiscPeripheralsAPI;
import miscperipherals.peripheral.PeripheralXP;
import miscperipherals.render.Icons;
import miscperipherals.util.SmallNetHelper;
import miscperipherals.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.liquids.LiquidStack;
import dan200.turtle.api.ITurtleUpgrade;

public class APICallHandler extends MiscPeripheralsAPI {	
	@Override
	public boolean isSideSensitive() {
		return MiscPeripherals.instance.sideSensitive;
	}
	
	@Override
	public boolean isDescriptive() {
		return MiscPeripherals.instance.descriptive;
	}

	@Override
	public int getFuelEU() {
		return MiscPeripherals.instance.fuelEU;
	}

	@Override
	public int getFuelMJ() {
		return MiscPeripherals.instance.fuelMJ;
	}

	@Override
	public void addFurnaceXP(Class<? extends TileEntity> clazz, int... slots) {
		if (PeripheralXP.FurnaceXPSource.classes.containsKey(clazz)) {
			int[] existing = PeripheralXP.FurnaceXPSource.classes.get(clazz);
			int[] nslots = new int[existing.length + slots.length];
			System.arraycopy(existing, 0, nslots, 0, existing.length);
			System.arraycopy(slots, 0, nslots, existing.length, slots.length);
			PeripheralXP.FurnaceXPSource.classes.put(clazz, nslots);
		} else PeripheralXP.FurnaceXPSource.classes.put(clazz, slots);
	}

	@Override
	public void addLiquidXP(LiquidStack liquid, int xp) {
		PeripheralXP.LiquidXPSource.liquids.put(liquid, xp);
	}

	@Override
	public int getUUID(ItemStack stack) {
		return Util.getUUID(stack);
	}

	@Override
	public int getUUID(LiquidStack stack) {
		return Util.getUUID(stack);
	}

	@Override
	public void registerUpgradeIcons(ITurtleUpgrade icons) {
		Icons.registerUpgrade(icons);
	}

	@Override
	public Vec3 getEntityPosition(Entity ent) {
		return Util.getPosition(ent, 1.0F);
	}

	@Override
	public UUID getEntityUUID(Entity ent) {
		return SmallNetHelper.getUUID(ent);
	}

	@Override
	public void setEntityUUID(Entity ent, UUID uuid) {
		SmallNetHelper.setUUID(ent, uuid);
	}

	@Override
	public UUID getRandomUUID() {
		return SmallNetHelper.randomUUID();
	}

	@Override
	public <T> void bindSmEntity(Class<T> clazz, ISmEntityFactory<T> factory) {
		if (!Entity.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Attempted to register a ISmEntityFactory for a non-entity type");
		
		SmallNetHelper.bindEntity((Class<? extends Entity>) clazz, (ISmEntityFactory<? extends Entity>) factory);
	}
	
	@Override
	public void sendSm(ISmSender from, UUID uuid, String type, String payload) {
		SmallNetHelper.send(from, uuid, type, payload);
	}
}
