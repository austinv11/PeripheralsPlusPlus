package com.austinv11.peripheralsplusplus.tiles;

import dan200.computercraft.api.peripheral.IPeripheral;
import forestry.api.genetics.IGenome;
import forestry.api.lepidopterology.IButterflyGenome;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class TileEntityAnalyzerButterfly extends TileEntityAnalyzer {

	public static String publicName = "butterflyAnalyzer";

	public TileEntityAnalyzerButterfly() {
		super();
	}

	@Override
	public String getName(){
		return "tileEntityButterflyAnalyzer";
	}

	@Override
	public String getPublicName() {
		return publicName;
	}

	@Override
	public String getType() {
		return "butterflyAnalyzer";
	}

	@Override
	protected String getRootType() {
		return "rootButterflies";
	}

	@Override
	protected void addGenome(ItemStack stack, IGenome origGenome, HashMap<String, Object> ret) {
		IButterflyGenome genome = (IButterflyGenome) origGenome;
		ret.put("speciesPrimary", genome.getPrimary().getName());
		ret.put("speciesSecondary", genome.getSecondary().getName());
		ret.put("speed", genome.getSpeed());
		ret.put("lifespan", genome.getLifespan());
		ret.put("metabolism", genome.getMetabolism());
		ret.put("fertility", genome.getFertility());
		ret.put("nocturnal", genome.getNocturnal());
		ret.put("tolerantFlyer", genome.getTolerantFlyer());
		ret.put("fireResistant", genome.getFireResist());
		ret.put("flower", genome.getFlowerProvider().getDescription());
		ret.put("effect", genome.getEffect().getUID());
		ret.put("temperature", genome.getPrimary().getTemperature().toString());
		ret.put("toleranceTemperature", genome.getToleranceTemp().toString());
		ret.put("humidity", genome.getPrimary().getHumidity().toString());
		ret.put("toleranceHumidity", genome.getToleranceHumid().toString());
		ret.put("cocoon", genome.getCocoon().toString());
	}

	@Override
	protected IPeripheral getInstance() {
		return this;
	}
}
