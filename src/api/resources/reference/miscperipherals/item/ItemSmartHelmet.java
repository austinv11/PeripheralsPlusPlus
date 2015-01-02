package miscperipherals.item;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import miscperipherals.api.ISmEntity;
import miscperipherals.api.ISmEntityFactory;
import miscperipherals.api.ISmSender;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.safe.Reflector;
import miscperipherals.util.SmallNetHelper;
import miscperipherals.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;

public class ItemSmartHelmet extends ItemArmor implements ISpecialArmor, ISmEntityFactory<EntityPlayer> {
	public static final int LINES = 5;
	
	public static String[] clientLines = new String[LINES];
	
	private static Object fwFontRenderer;
	
	private Icon icon;
	private Map<EntityPlayer, SmartHelmetLogic> logicCache = new WeakHashMap<EntityPlayer, SmartHelmetLogic>();
	
	public ItemSmartHelmet(int id) {
		super(id, EnumArmorMaterial.IRON, 0, 0);
		setMaxDamage(0);
		setCreativeTab(MiscPeripherals.instance.tabMiscPeripherals);
		
		fwFontRenderer = Reflector.invoke("dan200.ComputerCraft", "getFixedWidthFontRenderer", Object.class);
		
		SmallNetHelper.bindEntity(EntityPlayerMP.class, this);
	}
	
	@Override
	public boolean isValidArmor(ItemStack stack, int armorType) {
		return armorType == 0;
	}
	
	@Override
	public void getSubItems(int id, CreativeTabs tab, List list) {
		list.add(new ItemStack(this));
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		return icon;
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icon = reg.registerIcon("MiscPeripherals:smartHelmet");
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return "Smart Helmet";
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return "/mods/MiscPeripherals/textures/armor/smartHelmet.png";
	}

	@Override
	public ArmorProperties getProperties(EntityLiving player, ItemStack armor, DamageSource source, double damage, int slot) {
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack, DamageSource source, int damage, int slot) {
		
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
		return false;
	}
	
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack) {
		
	}

	@Override
	public ISmEntity getEntity(EntityPlayer entity) {		
		SmartHelmetLogic logic = logicCache.get(entity);
		if (logic == null) logicCache.put(entity, logic = new SmartHelmetLogic(entity));
		return logic;
	}
	
	@Override
	public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (!mc.gameSettings.showDebugInfo) {			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			Gui.drawRect(0, 0, resolution.getScaledWidth(), 6 + ItemSmartHelmet.clientLines.length * 10, 0x7F7F7F7F);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			for (int i = 0; i < ItemSmartHelmet.clientLines.length; i++) {
				drawFixedWidthString(mc, ItemSmartHelmet.clientLines[i], 3, 3 + i * 10);
			}
		}
	}
	
	public static void drawFixedWidthString(Minecraft mc, String text, int x, int y) {
		if (text == null) text = "";
		
		if (fwFontRenderer == null) {
			mc.fontRenderer.drawString(text, x, y, 0xFFFFFF);
		} else {
			Reflector.invoke(fwFontRenderer, "drawString", Object.class, text, x, y, Strings.repeat("f", text.length()), 2.0F, false);
		}
	}
	
	public static void sendToPlayer(EntityPlayer player, String line) {
		if (!(player instanceof EntityPlayerMP)) return;
		
		ByteArrayDataOutput os = ByteStreams.newDataOutput();
		os.writeUTF(Util.sanitize(line.length() > 255 ? line.substring(0, 255) : line, false).replaceAll("\n|\r|\t|\b", ""));
		
		((EntityPlayerMP) player).playerNetServerHandler.sendPacketToPlayer(PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)7, os.toByteArray()));
	}
	
	public static class SmartHelmetLogic implements ISmEntity {
		public final EntityPlayer player;
		
		public SmartHelmetLogic(EntityPlayer player) {
			this.player = player;
		}

		@Override
		public Vec3 getPosition() {
			return Util.getPosition(player, 1.0F);
		}

		@Override
		public World getWorld() {
			return player.worldObj;
		}

		@Override
		public double getPower() {
			return 0.0D;
		}

		@Override
		public void receive(ISmSender from, String type, String payload) {
			if (type.equalsIgnoreCase("smartHelmet")) {
				sendToPlayer(player, payload);
			}
		}
	}
}
