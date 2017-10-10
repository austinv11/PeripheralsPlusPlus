package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.peripheral.IPeripheral;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.IGenome;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class TileEntityAnalyzerBee extends TileEntityAnalyzer {

	public static String publicName = "beeAnalyzer";

	public TileEntityAnalyzerBee() {
		super();
	}

	@Override
	public String getName(){
		return "tileEntityBeeAnalyzer";
	}

	@Override
	public String getPublicName() {
		return publicName;
	}

	@Override
	public String getType() {
		return "beeAnalyzer";
	}

	@Override
	protected String getRootType() {
		return "rootBees";
	}

	@Override
	protected void addGenome(ItemStack stack, IGenome origGenome, HashMap<String,Object> ret) {
		IBeeRoot root = (IBeeRoot) getRoot();
		IBeeGenome genome = (IBeeGenome) origGenome;
		ret.put("type", root.getType(stack) == null ? null : root.getType(stack).name());
		ret.put("speciesPrimary", genome.getPrimary().getAlleleName());
		ret.put("speciesSecondary", genome.getSecondary().getAlleleName());
		ret.put("speed", genome.getSpeed());
		ret.put("lifespan", genome.getLifespan());
		ret.put("fertility", genome.getFertility());
		ret.put("neverSleeps", genome.getNeverSleeps());
		ret.put("toleratesRain", genome.getToleratesRain());
		ret.put("caveDwelling", genome.getCaveDwelling());
		ret.put("flower", genome.getFlowerProvider().getDescription());
		ret.put("flowering", genome.getFlowering());
		ret.put("territory", Util.arrayToMap(new int[]{genome.getTerritory().getX(), genome.getTerritory().getZ(),
                genome.getTerritory().getZ()}));
		ret.put("effect", genome.getEffect().getUID());
		ret.put("temperature", genome.getPrimary().getTemperature().toString());
		ret.put("toleranceTemperature", genome.getToleranceTemp().toString());
		ret.put("humidity", genome.getPrimary().getHumidity().toString());
		ret.put("toleranceHumidity", genome.getToleranceHumid().toString());
	}

	@Override
	protected IPeripheral getInstance() {
		return this;
	}
}
