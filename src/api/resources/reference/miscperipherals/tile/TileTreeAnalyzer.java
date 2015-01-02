package miscperipherals.tile;

import java.util.Map;

import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.genetics.IGenome;

public class TileTreeAnalyzer extends TileAnalyzer {
	@Override
	public String getInventoryName() {
		return "Tree Analyzer";
	}

	@Override
	public String getType() {
		return "treeAnalyzer";
	}
	
	@Override
	protected String getRootType() {
		return "rootTrees";
	}
	
	@Override
	protected void addGenome(ItemStack stack, IGenome origGenome, Map<String, Object> ret) {
		ITreeGenome genome = (ITreeGenome) origGenome;
		
		ret.put("speciesPrimary", genome.getPrimary().getName());
		ret.put("speciesSecondary", genome.getSecondary().getName());
		ret.put("height", genome.getHeight());
		ret.put("fertility", genome.getFertility());
		ret.put("yield", genome.getYield());
		ret.put("sappiness", genome.getSappiness());
		ret.put("matures", genome.getMaturationTime());
		ret.put("fruit", genome.getFruitProvider().getDescription());
		ret.put("growth", genome.getGrowthProvider().getDescription());
		ret.put("girth", genome.getGirth());
		ret.put("plant", Util.iterableToMap(genome.getPlantTypes()));
	}
}
