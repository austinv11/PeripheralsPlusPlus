package miscperipherals.tile;

import java.util.Map;

import net.minecraft.item.ItemStack;
import forestry.api.genetics.IGenome;
import forestry.api.lepidopterology.IButterflyGenome;

public class TileButterflyAnalyzer extends TileAnalyzer {
	@Override
	public String getInventoryName() {
		return "Butterfly Analyzer";
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
	protected void addGenome(ItemStack stack, IGenome origGenome, Map<String, Object> ret) {
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
	}
}
