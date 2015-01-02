package miscperipherals.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import miscperipherals.render.Icons;
import miscperipherals.speech.SpeechManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProxyClient extends Proxy {
	private final Minecraft mc;
	private final Map<String, Integer> renders = new HashMap<String, Integer>();
	
	public ProxyClient() {
		super();
		mc = Minecraft.getMinecraft();
		
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
	}
	
	@ForgeSubscribe
	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		Icons.addTextures(event.map);
	}
	
	@Override
	public World getClientWorld() {
		return mc.theWorld;
	}
	
	@Override
	public Entity getClientEntityById(int id) {
		return mc.theWorld.getEntityByID(id);
	}
	
	@Override
	public void spawnHeartParticles(Entity ent) {
		for (int i = 0; i < 7; i++) {
            double x = mc.theWorld.rand.nextGaussian() * 0.02D;
            double y = mc.theWorld.rand.nextGaussian() * 0.02D;
            double z = mc.theWorld.rand.nextGaussian() * 0.02D;
            mc.theWorld.spawnParticle("heart", ent.posX + (double)(mc.theWorld.rand.nextFloat() * ent.width * 2.0F) - (double)ent.width, ent.posY + 0.5D + (double)(mc.theWorld.rand.nextFloat() * ent.height), ent.posZ + (double)(mc.theWorld.rand.nextFloat() * ent.width * 2.0F) - (double)ent.width, x, y, z);
        }
	}
	
	@Override
	public File getMinecraftFolder() {
		return mc.getMinecraftDir();
	}
	
	@Override
	public boolean isServer() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}
	
	@Override
	public void spawnSmoke(double x, double y, double z, double motionX, double motionY, double motionZ) {
		EntitySmokeFX fx = new EntitySmokeFX(mc.theWorld, x, y, z, motionX, motionY, motionZ);
		mc.effectRenderer.addEffect(fx);
	}
	
	@Override
	public int getRenderID(String key) {
		return renders.get(key);
	}
	
	@Override
	public Icon getIcon(Object o) {
		if (o instanceof Block) return ((Block)o).getBlockTextureFromSide(0);
		else if (o instanceof Item) return ((Item)o).getIconFromDamage(0);
		else return null;
	}
	
	@Override
	public void onPostInit() {
		SpeechManager.init();
		MinecraftForge.EVENT_BUS.register(this);
		
		//ClientRegistry.bindTileEntitySpecialRenderer(TilePlayerDetector.class, new RenderTilePlayerDetector());
	}
	
	@Override
	public void speak(String text, double speed, double x, double y, double z) {
		SpeechManager.speak(text, speed, x, y, z);
	}
}
