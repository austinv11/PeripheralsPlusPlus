package miscperipherals.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.network.GuiHandler;
import miscperipherals.safe.ReflectionStore;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileNuclearReader extends TileInventory implements IPeripheral {
	private static final int SHIPPED_RANGE_UPGRADES = 2;
	/**
	 * 7 by default
	 */
	private static final int MAX_RANGE_UPGRADES = 7;
	/**
	 * Private static final on the info panel class!
	 */
	private static final int BASE_RANGE = 8;
	
	public TileNuclearReader() {
		this(1);
	}
	
	public TileNuclearReader(int slots) {
		super(slots);
	}
	
	@Override
	public boolean isStackValidForSlot(int slot, ItemStack item) {
		return item.getItem() instanceof IPanelDataSource || (ReflectionStore.itemUpgrade != null && ReflectionStore.damageRange != null && item.getItem().getClass() == ReflectionStore.itemUpgrade.getClass() && item.getItemDamage() == ReflectionStore.damageRange);
	}
	
	@Override
	public String getInventoryName() {
		return "Nuclear Information Reader";
	}

	@Override
	public String getType() {
		return "nuclearReader";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"get", "getSlots"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				final int slot = (int)Math.floor((Double)arguments[0]) - 1;

				if (slot < 0 || slot >= inventory.length) throw new Exception("bad slot "+(slot + 1)+" (expected 1-"+inventory.length+")");
				if (inventory[slot] == null) return new Object[] {null, null, null, null};
				
				Future<Object[]> callback = TickHandler.addTickCallback(worldObj, new Callable<Object[]>() {
					@Override
					public Object[] call() {
						int rangeUpgrades = SHIPPED_RANGE_UPGRADES;
						for (int i = 0; i < inventory.length; i++) {
							if (inventory[i] != null && ReflectionStore.itemUpgrade != null && ReflectionStore.damageRange != null && inventory[i].getItem().getClass() == ReflectionStore.itemUpgrade.getClass() && inventory[i].getItemDamage() == ReflectionStore.damageRange) {
								rangeUpgrades += inventory[i].stackSize;
							}
						}
						if (rangeUpgrades > MAX_RANGE_UPGRADES) rangeUpgrades = MAX_RANGE_UPGRADES;
						
						if (inventory[slot] != null && inventory[slot].getItem() instanceof IPanelDataSource) {
							Map<String, Object> map = new HashMap<String, Object>();
							ICardWrapper cw = new CardWrapperCommit(TileNuclearReader.this, slot, map);
							IPanelDataSource ds = (IPanelDataSource)inventory[slot].getItem();
							UUID uuid = ds.getCardType();
							CardState state = ds.update(TileNuclearReader.this, cw, BASE_RANGE * (int)Math.pow(2, rangeUpgrades));
							String title = cw.getTitle();
							return new Object[] {uuid == null ? null : uuid.toString(), state.toString(), title, map};
						} else {
							return new Object[] {null, CardState.INVALID_CARD.toString(), null, null};
						}
					}
				});
				return callback.get();
			}
			case 1: {
				return new Object[] {inventory.length};
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
	
	@Override
	public int getGuiId() {
		return GuiHandler.SINGLE_SLOT;
	}
	
	/**
	 * RG for public source release: There isn't really a native way to intercept panel data, so I made this, with permission from Shedar by the way.
	 */
	public class CardWrapperCommit extends CardWrapperImpl {
		private final TileNuclearReader nr;
		private final int index;
		private final Map<String, Object> map;
		
		public CardWrapperCommit(TileNuclearReader nr, int index, Map<String, Object> map) {
			super(nr.inventory[index], index);
			this.nr = nr;
			this.index = index;
			this.map = map;
		}
		
		@Override
		public void setInt(String name, Integer value) {
			putValue(name, value);
		}
		
		@Override
		public void setLong(String name, Long value) {
			putValue(name, value);
		}
		
		@Override
		public void setString(String name, String value) {
			putValue(name, value);
		}
		
		@Override
		public void setBoolean(String name, Boolean value) {
			putValue(name, value);
		}
		
		@Override
		public void commit(TileEntity panel) {
			
		}
		
		private void putValue(String name, Object value) {
			map.put(name, value);
		}
	}
}
