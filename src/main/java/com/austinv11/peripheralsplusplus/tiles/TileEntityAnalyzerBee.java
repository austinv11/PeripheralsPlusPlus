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
		ret.put("type", root.getType(stack).name());
		ret.put("speciesPrimary", genome.getPrimary().getName());
		ret.put("speciesSecondary", genome.getSecondary().getName());
		ret.put("speed", genome.getSpeed());
		ret.put("lifespan", genome.getLifespan());
		ret.put("fertility", genome.getFertility());
		ret.put("nocturnal", genome.getNocturnal());
		ret.put("tolerantFlyer", genome.getTolerantFlyer());
		ret.put("caveDwelling", genome.getCaveDwelling());
		ret.put("flower", genome.getFlowerProvider().getDescription());
		ret.put("territory", Util.arrayToMap(genome.getTerritory()));
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
