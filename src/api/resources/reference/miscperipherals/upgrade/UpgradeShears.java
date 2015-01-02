package miscperipherals.upgrade;

import java.util.Arrays;
import java.util.List;

import miscperipherals.api.IUpgradeToolIcons;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.oredict.OreDictionary;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class UpgradeShears extends UpgradeTool implements IUpgradeToolIcons {
	private static final List<Block> SHEAR_DIG = Arrays.asList(Block.cloth, Block.web);
	private Icon icon;
	
	@Override
	public int getUpgradeID() {
		return 230;
	}

	@Override
	public String getAdjective() {
		return "Shearing";
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Item.shears, 1, OreDictionary.WILDCARD_VALUE);
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return icon;
	}
	
	@Override
	public boolean canAttack(ITurtleAccess turtle, Entity ent) {
		return ent instanceof IShearable && ((IShearable)ent).isShearable(getCraftingItem(), ent.worldObj, (int)ent.posX, (int)ent.posY, (int)ent.posZ);
	}
	
	@Override
	public boolean attack(ITurtleAccess turtle, Entity ent) {
		for (ItemStack is : ((IShearable)ent).onSheared(getCraftingItem(), ent.worldObj, (int)ent.posX, (int)ent.posY, (int)ent.posZ, 0)) {
			Util.storeOrDrop(turtle, is);
		}
		return true;
	}
	
	@Override
	public boolean canDig(ITurtleAccess turtle, int x, int y, int z, int side) {
		Block block = Block.blocksList[turtle.getWorld().getBlockId(x, y, z)];
		return block instanceof IShearable || SHEAR_DIG.contains(block);
	}
	
	@Override
	public Iterable<ItemStack> getBlockDrops(ITurtleAccess turtle, int x, int y, int z, int side) {
		World world = turtle.getWorld();
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		return SHEAR_DIG.contains(block) ? super.getBlockDrops(turtle, x, y, z, side) : ((IShearable)block).onSheared(getCraftingItem(), world, x, y, z, 0);
	}

	@Override
	public void registerIcons(IconRegister reg) {
		icon = reg.registerIcon("shears");
	}
}
