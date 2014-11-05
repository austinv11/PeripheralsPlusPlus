package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public abstract class TileEntityAnalyzer extends TileEntityInv implements IPeripheral{

	private String name = "tileEntityAnalyzer";

	public TileEntityAnalyzer() {
		super();
		this.size = 1;
		this.invName = "Analyzer";
	}

	public String getName() {
		return name;
	}

	public abstract String getPublicName();

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
	}

	@Override
	public String getType() {
		return getPublicName();
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"analyze","isMember"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableAnalyzers)
			throw new LuaException("Analyzers have been disabled");
		switch (method) {
			case 0:
				ISpeciesRoot root = getRoot();
				ItemStack stack = getStackInSlot(0);
				if (stack == null || !root.isMember(stack))
					return new Object[] {false};
				IIndividual individual = root.getMember(stack);
				if (!individual.isAnalyzed())
					return new Object[] {null};
				HashMap<String, Object> ret = new HashMap<String, Object>();
				addGenome(stack, individual.getGenome(), ret);
				return new Object[] {ret};
			case 1:
				ItemStack specimen = getStackInSlot(0);
				if (specimen == null || !getRoot().isMember(specimen))
					return new Object[] {false};
				return new Object[] {true};
		}
		return new Object[]{};
	}

	@Override
	public void attach(IComputerAccess computer) {}

	@Override
	public void detach(IComputerAccess computer) {}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	protected ISpeciesRoot getRoot() {
		return AlleleManager.alleleRegistry.getSpeciesRoot(getRootType());
	}

	protected abstract String getRootType();

	protected abstract void addGenome(ItemStack stack, IGenome origGenome, HashMap<String, Object> ret);
}
