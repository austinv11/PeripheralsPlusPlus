package miscperipherals.tile;

import java.util.Map;

import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.IGenome;

public class TileBeeAnalyzer extends TileAnalyzer {
	@Override
	public String getInventoryName() {
		return "Bee Analyzer";
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
	protected void addGenome(ItemStack stack, IGenome origGenome, Map<String, Object> ret) {
		IBeeRoot root = (IBeeRoot) getRoot();
		IBeeGenome genome = (IBeeGenome) origGenome;
		
		ret.put("type", Util.camelCase(root.getType(stack).name()));
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
}
