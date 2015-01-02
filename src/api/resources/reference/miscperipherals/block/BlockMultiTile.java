package miscperipherals.block;

import java.util.ArrayList;
import java.util.List;

import miscperipherals.block.BlockMultiTile.TileData.FacingMode;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.tile.Tile;
import miscperipherals.util.Util;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.base.Objects;

public class BlockMultiTile extends BlockContainer {
	private static final int META_VALUES = 16;
	private static final int FACING_VALUES = 6;
	/**
	 * 0 = y- down
	 * 1 = y+ up
	 * 2 = z- east
	 * 3 = z+ west (DEFAULT)
	 * 4 = x- north
	 * 5 = x+ south
	 */
	private static final int[][] SIDE_OFFSETS = {{3,2,0,1,5,4},{2,3,0,1,5,4},{0,1,3,2,5,4},{0,1,2,3,4,5},{0,1,4,5,3,2},{0,1,5,4,2,3}};
	public static Icon GENERIC = null;
	
	public TileData[] data = new TileData[META_VALUES];
	
	public BlockMultiTile(int id) {
		super(id, Material.rock);
		setHardness(3.0F);
		setResistance(10.0F);
		
		setCreativeTab(MiscPeripherals.instance.tabMiscPeripherals);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		if (data[meta] == null || data[meta].clazz == null) return null;
		
		try {
			return (TileEntity)data[meta].clazz.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		if (data[meta] == null) return GENERIC;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te instanceof Tile)) return data[meta].sprites[SIDE_OFFSETS[3][side]];
		
		Tile tile = (Tile)te;
		int sideOff = SIDE_OFFSETS[tile.getFacing()][side];
		return tile.isActive() ? data[meta].activeSprites[sideOff] : data[meta].sprites[sideOff];
	}
	
	@Override
	public Icon getIcon(int side, int meta) {
		return meta >= 0 && meta < data.length && data[meta] != null ? data[meta].sprites[SIDE_OFFSETS[3][side >= 0 && side < 6 ? side : 0]] : GENERIC;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack stack) {
		if (!world.isRemote && entity != null) {
			TileEntity te = world.getBlockTileEntity(x, y, z);
			
			if (te instanceof Tile) {
				Tile tile = (Tile) te;
				
				int meta = world.getBlockMetadata(x, y, z);
				if (data[meta] != null && data[meta].facingMode != FacingMode.None) {
					int yaw = MathHelper.floor_double(((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
					int pitch = data[meta].facingMode == FacingMode.All ? Math.round(entity.rotationPitch) : 0;
					
					if (pitch >= 65) tile.setFacing(1);
					else if (pitch <= -65) tile.setFacing(0);
					else if (yaw == 0) tile.setFacing(2);
					else if (yaw == 1) tile.setFacing(5);
					else if (yaw == 2) tile.setFacing(3);
				}
				
				tile.onPlaced(world, x, y, z, entity, stack);
			}
		}
	}
	
	@Override
	public void getSubBlocks(int id, CreativeTabs tabs, List list) {
		for (int meta = 0; meta < data.length; meta++) {
			if (data[meta] != null) list.add(new ItemStack(this, 1, meta));
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		if (player.isSneaking()) return false;
		
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te instanceof Tile)) return false;
		
		Tile tile = (Tile)te;
		return tile.onBlockActivated(player, side, hitX, hitY, hitZ);
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList();
		ItemStack stack = new ItemStack(this, 1, metadata);
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof Tile) {
			Tile tile = (Tile) te;
			if (tile.displayName != null) stack.setItemName(tile.displayName);
		}
		ret.add(stack);
		return ret;
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
	public void breakBlock(World world, int x, int y, int z, int a, int b) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof IInventory) {
			IInventory inv = (IInventory)te;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				
				if (stack != null) {
					float var10 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float var11 = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem var14;

                    for (float var12 = world.rand.nextFloat() * 0.8F + 0.1F; stack.stackSize > 0; world.spawnEntityInWorld(var14)) {
                        int var13 = world.rand.nextInt(21) + 10;

                        if (var13 > stack.stackSize) var13 = stack.stackSize;

                        stack.stackSize -= var13;
                        var14 = new EntityItem(world, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(stack.itemID, var13, stack.getItemDamage()));
                        float var15 = 0.05F;
                        var14.motionX = (double)((float)world.rand.nextGaussian() * var15);
                        var14.motionY = (double)((float)world.rand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)world.rand.nextGaussian() * var15);

                        if (stack.hasTagCompound()) {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                        }
                    }
				}
			}
		}
		
		world.removeBlockTileEntity(x, y, z);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
		world.removeBlockTileEntity(x, y, z);
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		GENERIC = reg.registerIcon("MiscPeripherals:generic");
		
		for (int i = 0; i < META_VALUES; i++) {
			if (data[i] == null) continue;
			
			for (int j = 0; j < data[i].spriteNames.length; j++) {
				data[i].sprites[j] = reg.registerIcon("MiscPeripherals:"+data[i].spriteNames[j]);
			}
			
			for (int j = 0; j < data[i].activeSpriteNames.length; j++) {
				data[i].activeSprites[j] = reg.registerIcon("MiscPeripherals:"+data[i].activeSpriteNames[j]);
			}
		}
	}
	
	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
		if (axis == ForgeDirection.UNKNOWN) return false;
		
		int meta = worldObj.getBlockMetadata(x, y, z);
		if (data[meta] == null) return false;
		
		switch (data[meta].facingMode) {
			case None: {
				return false;
			}
			case Horizontal: {
				if (axis.offsetY != 0) return false;
				break;
			}
			// shut up Eclipse
			case All: {}
		}
		
		TileEntity te = worldObj.getBlockTileEntity(x, y, z);
		if (!(te instanceof Tile)) return false;
		
		((Tile) te).setFacing(axis.ordinal());
		
		return true;
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te instanceof Tile)) return 0;
		else return ((Tile) te).getRedstone(Util.OPPOSITE[side]);
	}
	
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te instanceof Tile)) return 0;
		else return ((Tile) te).getComparator(side);
	}

	public TileData registerTile(int meta) {
		if (data[meta] != null) throw new RuntimeException("Metadata value "+meta+" already used by "+data[meta]);
		
		TileData td = new TileData(meta);
		data[meta] = td;
		return td;
	}
	
	public int findFirstUsedMeta() {
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) return i;
		}
		
		return -1;
	}
	
	public static class TileData {
		public final int meta;
		public Class<? extends Tile> clazz;
		public Icon[] sprites = new Icon[6];
		public Icon[] activeSprites = new Icon[6];
		public String name;
		public FacingMode facingMode = FacingMode.None;
		public String[] infoText = new String[0];
		
		private String[] spriteNames = new String[6];
		private String[] activeSpriteNames = new String[6];
		
		private TileData(int meta) {
			this.meta = meta;
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("name", name).add("meta", meta).add("clazz", clazz).toString();
		}
		
		public TileData setClass(Class<? extends Tile> clazz) {
			this.clazz = clazz;
			return this;
		}
		
		public TileData setSprites(String... sprites) {
			switch (sprites.length) {
				case 1: {
					activeSpriteNames = spriteNames = new String[] {sprites[0], sprites[0], sprites[0], sprites[0], sprites[0], sprites[0]};
					break;
				}
				case 2: {
					spriteNames = new String[] {sprites[0], sprites[0], sprites[0], sprites[0], sprites[0], sprites[0]};
					activeSpriteNames = new String[] {sprites[1], sprites[1], sprites[1], sprites[1], sprites[1], sprites[1]};
					break;
				}
				case 6: {
					activeSpriteNames = spriteNames = sprites;
					break;
				}
				case 12: {
					System.arraycopy(sprites, 0, spriteNames, 0, 6);
					System.arraycopy(sprites, 6, activeSpriteNames, 0, 6);
					break;
				}
				default: throw new IllegalArgumentException("Unknown sprite array size "+sprites.length);
			}
			return this;
		}
		
		public TileData setName(String name) {
			this.name = name;
			return this;
		}
		
		public TileData setFacingMode(FacingMode facingMode) {
			this.facingMode = facingMode;
			return this;
		}
		
		public TileData setInfoText(String... infoText) {
			if (infoText.length > 0 && infoText[0] == null) return this;
			this.infoText = infoText;
			return this;
		}
		
		public enum FacingMode { All, Horizontal, None }
	}
}
