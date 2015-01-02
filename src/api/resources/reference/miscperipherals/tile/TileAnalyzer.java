package miscperipherals.tile;

import java.util.HashMap;
import java.util.Map;

import miscperipherals.core.LuaManager;
import miscperipherals.network.GuiHandler;
import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;

public abstract class TileAnalyzer extends TileInventory implements IPeripheral {
	public TileAnalyzer() {
		super(1);
	}
	
	@Override
	public int getGuiId() {
		return GuiHandler.SINGLE_SLOT;
	}
	
	@Override
	public abstract String getInventoryName();

	@Override
	public abstract String getType();

	@Override
	public String[] getMethodNames() {
		return new String[] {"analyze","isMember","isBee","isTree"};
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				ISpeciesRoot root = getRoot();
				
				ItemStack stack = getStackInSlot(0);
				if (stack == null || !root.isMember(stack)) return new Object[] {false};
				
				IIndividual individual = root.getMember(stack);
				if (!individual.isAnalyzed()) return new Object[] {null};
				
				Map<String, Object> ret = new HashMap<String, Object>();
				addGenome(stack, individual.getGenome(), ret);
				
				return new Object[] {ret};
			}
			case 1:
			case 2: 
			case 3: {
				ItemStack stack = getStackInSlot(0);
				if (stack == null || !getRoot().isMember(stack)) return new Object[] {false};
				
				return new Object[] {true};
			}
		}
		
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		LuaManager.mount(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}
	
	protected ISpeciesRoot getRoot() {
		return AlleleManager.alleleRegistry.getSpeciesRoot(getRootType());
	}
	
	protected abstract String getRootType();
	
	protected abstract void addGenome(ItemStack stack, IGenome origGenome, Map<String, Object> ret);
}
