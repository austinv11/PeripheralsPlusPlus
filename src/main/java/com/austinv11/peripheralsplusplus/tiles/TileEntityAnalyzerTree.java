package com.austinv11.peripheralsplusplus.tiles;

import dan200.computercraft.api.peripheral.IPeripheral;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.genetics.IGenome;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class TileEntityAnalyzerTree extends TileEntityAnalyzer {

	public static String publicName = "treeAnalyzer";

	public TileEntityAnalyzerTree() {
		super();
	}

	@Override
	public String getName(){
		return "tileEntityTreeAnalyzer";
	}

	@Override
	public String getPublicName() {
		return publicName;
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
	protected void addGenome(ItemStack stack, IGenome origGenome, HashMap<String, Object> ret) {
		ITreeGenome genome = (ITreeGenome) origGenome;
		ret.put("speciesPrimary", genome.getPrimary().getName());
		ret.put("speciesSecondary", genome.getSecondary().getName());
		ret.put("height", genome.getHeight());
		ret.put("fertility", genome.getFertility());
		ret.put("yield", genome.getYield());
		ret.put("sappiness", genome.getSappiness());
		ret.put("matures", genome.getMaturationTime());
		ret.put("fruit", genome.getFruitProvider().getDescription());
		ret.put("girth", genome.getGirth());
		ret.put("effect", genome.getEffect().getUID());
		ret.put("decorativeLeaves", genome.getDecorativeLeaves().getDisplayName());
		ret.put("matchesTemplateGenome", genome.matchesTemplateGenome());
	}

	@Override
	protected IPeripheral getInstance() {
		return this;
	}
}
