package miscperipherals.block;

import miscperipherals.block.BlockLanCable.CableType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockLanCable extends ItemBlock {
	public ItemBlockLanCable(int id) {
		super(id);
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		int meta = stack.getItemDamage();
		CableType type = BlockLanCable.types.get(meta);
		if (type == null) return "[Invalid Cable Type]";
		else return "miscperipherals.lanCable."+type.name;
	}
}
