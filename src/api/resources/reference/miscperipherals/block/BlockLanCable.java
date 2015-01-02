package miscperipherals.block;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.tile.TileLanCable;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockLanCable extends BlockContainer {
	public static Map<Integer, CableType> types = new TreeMap<Integer, CableType>(new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 < o2 ? -1 : (o1 > o2 ? 1 : 0);
		}
	});
	
	public BlockLanCable(int id) {
		super(id, Material.rock);
		setLightOpacity(0);
		setCreativeTab(MiscPeripherals.instance.tabMiscPeripherals);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileLanCable();
	}
	
	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te.getClass() == TileLanCable.class)) return;
		
		((TileLanCable)te).setType(meta);
		world.markBlockForRenderUpdate(x, y, z);
	}
	
	@Override
	public void getSubBlocks(int id, CreativeTabs tabs, List list) {
		for (Integer type : types.keySet()) {
			list.add(new ItemStack(this, 1, type));
		}
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return MiscPeripherals.proxy.getRenderID("lanCable");
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te.getClass() == TileLanCable.class)) return types.get(0).sprite;
		return types.get(((TileLanCable)te).getType()).sprite;
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z, int meta) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te.getClass() == TileLanCable.class)) return null;
		
    	double halfThickness = ((TileLanCable)te).getThickness();

    	return AxisAlignedBB.getBoundingBox(x + 0.5d - halfThickness, y + 0.5d - halfThickness, z + 0.5d - halfThickness, x + 0.5d + halfThickness, y + 0.5d + halfThickness, z + 0.5d + halfThickness);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return getCommonBoundingBoxFromPool(world, x, y, z, false);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return getCommonBoundingBoxFromPool(world, x, y, z, true);
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te == null || !(te.getClass() == TileLanCable.class)) return new ArrayList<ItemStack>(0);
		
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>(1);
		ret.add(new ItemStack(this, 1, ((TileLanCable)te).getType()));
		return ret;
	}
	
	public AxisAlignedBB getCommonBoundingBoxFromPool(World world, int x, int y, int z, boolean selectionBoundingBox) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te == null || !(te.getClass() == TileLanCable.class)) return null;
		TileLanCable cable = (TileLanCable)te;
		
		double halfThickness = cable.getThickness() / 2d;
		
		double minX = x + 0.5d - halfThickness;
		double minY = y + 0.5d - halfThickness;
		double minZ = z + 0.5d - halfThickness;
		double maxX = x + 0.5d + halfThickness;
		double maxY = y + 0.5d + halfThickness;
		double maxZ = z + 0.5d + halfThickness;
		
		if (cable.canInteractWith(world.getBlockTileEntity(x-1, y, z))) minX = x;
		if (cable.canInteractWith(world.getBlockTileEntity(x, y-1, z))) minY = y;
		if (cable.canInteractWith(world.getBlockTileEntity(x, y, z-1))) minZ = z;
		if (cable.canInteractWith(world.getBlockTileEntity(x+1, y, z))) maxX = x + 1;
		if (cable.canInteractWith(world.getBlockTileEntity(x, y+1, z))) maxY = y + 1;
		if (cable.canInteractWith(world.getBlockTileEntity(x, y, z+1))) maxZ = z + 1;

		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public static CableType registerType(int id, String name, Block sprite, double cps, boolean state, boolean redstone) {
		if (types.containsKey(id)) throw new RuntimeException("Cable type "+id+" already taken by "+types.get(id).name);
		
		CableType type = new CableType(id, name, MiscPeripherals.proxy.getIcon(sprite), cps, state, redstone);
		types.put(id, type);
		return type;
	}
	
	public static class CableType {
		public final int id;
		public final String name;
		public Icon sprite;
		
		public final double cps;
		public final boolean state;
		public final boolean redstone;
		
		public CableType(int id, String name, Icon sprite, double cps, boolean state, boolean redstone) {
			this.id = id;
			String localName = Util.camelCase(name);
			LanguageRegistry.instance().addStringLocalization("miscperipherals.lanCable."+localName+".name", name+" LAN Cable");
			this.name = localName;
			this.sprite = sprite;
			
			this.cps = cps;
			this.state = state;
			this.redstone = redstone;
		}
		
		public CableType addRecipe(String mat) {
			Object arg = mat;
			if (mat.equals("ingotIron")) arg = Item.ingotIron;
			else if (mat.equals("ingotGold")) arg = Item.ingotGold;
			
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MiscPeripherals.instance.blockLanCable, 8, id), " R ", "*R*", " R ", 'R', Item.redstone, '*', arg));
			return this;
		}
		
		public CableType setOreDictTexture(String ore, ItemStack fallback) {
			for (ItemStack stack : OreDictionary.getOres(ore)) {
				if (stack.itemID > 0 && stack.itemID < Block.blocksList.length) {
					sprite = Block.blocksList[stack.itemID].getIcon(3, stack.getItemDamage());
					return this;
				}
			}
			
			sprite = Block.blocksList[fallback.itemID].getIcon(3, fallback.getItemDamage());
			return this;
		}
	}
}
