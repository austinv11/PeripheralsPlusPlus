package shedar.mods.ic2.nuclearcontrol.panel;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;

public class CardWrapperImpl implements ICardWrapper {
	public CardWrapperImpl(ItemStack stack, int slot) {
		
	}

	@Override
	public void setTarget(int x, int y, int z) {

	}

	@Override
	public ChunkCoordinates getTarget() {
		return null;
	}

	@Override
	public void setInt(String name, Integer value) {

	}

	@Override
	public Integer getInt(String name) {
		return null;
	}

	@Override
	public void setLong(String name, Long value) {

	}

	@Override
	public Long getLong(String name) {
		return null;
	}

	@Override
	public void setString(String name, String value) {

	}

	@Override
	public String getString(String name) {
		return null;
	}

	@Override
	public void setBoolean(String name, Boolean value) {

	}

	@Override
	public Boolean getBoolean(String name) {
		return null;
	}

	@Override
	public void setTitle(String title) {

	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public CardState getState() {
		return null;
	}

	@Override
	public void setState(CardState state) {

	}

	@Override
	public ItemStack getItemStack() {
		return null;
	}

	@Override
	public boolean hasField(String field) {
		return false;
	}

	@Override
	public void commit(TileEntity panel) {
		
	}
}
