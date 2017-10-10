package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.capabilities.nano.NanoBotHolder;
import com.austinv11.peripheralsplusplus.capabilities.nano.NanoBotHolderDefault;
import com.austinv11.peripheralsplusplus.capabilities.nano.NanoBotHolderStorage;
import com.austinv11.peripheralsplusplus.client.gui.GuiHandler;
import com.austinv11.peripheralsplusplus.entities.EntityNanoBotSwarm;
import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.event.handler.CapabilitiesHandler;
import com.austinv11.peripheralsplusplus.event.handler.PeripheralContainerHandler;
import com.austinv11.peripheralsplusplus.network.*;
import com.austinv11.peripheralsplusplus.pocket.PocketMotionDetector;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.*;
import com.austinv11.peripheralsplusplus.villagers.VillagerProfessionPPP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static com.austinv11.peripheralsplusplus.PeripheralsPlusPlus.NETWORK;

public class CommonProxy {

	public void setupVillagers() {
		VillagerRegistry.VillagerProfession profession = new VillagerProfessionPPP(
				Reference.MOD_ID + ":ccvillager",
				Reference.MOD_ID + ":textures/models/ccvillager.png",
				"minecraft:textures/entity/zombie_villager/zombie_villager.png");
		ForgeRegistries.VILLAGER_PROFESSIONS.register(profession);
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityChatBox.class, TileEntityChatBox.publicName);
		GameRegistry.registerTileEntity(TileEntityAIChatBox.class, TileEntityAIChatBox.publicName);
		GameRegistry.registerTileEntity(TileEntityPlayerSensor.class, TileEntityPlayerSensor.publicName);
		GameRegistry.registerTileEntity(TileEntityRFCharger.class, TileEntityRFCharger.publicName);
		GameRegistry.registerTileEntity(TileEntityOreDictionary.class, TileEntityOreDictionary.publicName);
		GameRegistry.registerTileEntity(TileEntityAnalyzerBee.class, TileEntityAnalyzerBee.publicName);
		GameRegistry.registerTileEntity(TileEntityAnalyzerButterfly.class, TileEntityAnalyzerButterfly.publicName);
		GameRegistry.registerTileEntity(TileEntityAnalyzerTree.class, TileEntityAnalyzerTree.publicName);
		GameRegistry.registerTileEntity(TileEntityTeleporter.class, TileEntityTeleporter.publicName);
		GameRegistry.registerTileEntity(TileEntityEnvironmentScanner.class, TileEntityEnvironmentScanner.publicName);
		GameRegistry.registerTileEntity(TileEntitySpeaker.class, TileEntitySpeaker.publicName);
		GameRegistry.registerTileEntity(TileEntityAntenna.class, TileEntityAntenna.publicName);
		GameRegistry.registerTileEntity(TileEntityPeripheralContainer.class, TileEntityPeripheralContainer.publicName);
		GameRegistry.registerTileEntity(TileEntityMEBridge.class, TileEntityMEBridge.publicName);
		GameRegistry.registerTileEntity(TileEntityTurtle.class, TileEntityTurtle.publicName);
		GameRegistry.registerTileEntity(TileEntityTimeSensor.class, TileEntityTimeSensor.publicName);
		GameRegistry.registerTileEntity(TileEntityInteractiveSorter.class, TileEntityInteractiveSorter.publicName);
        GameRegistry.registerTileEntity(TileEntityPlayerInterface.class, TileEntityPlayerInterface.publicName);
		GameRegistry.registerTileEntity(TileEntityResupplyStation.class, TileEntityResupplyStation.publicName);
    }

	public void textureAndModelInit() {}

	public void registerRenderers() {}

	public void prepareGuis() {
		NetworkRegistry.INSTANCE.registerGuiHandler(PeripheralsPlusPlus.instance, new GuiHandler());
	}

	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new TileEntityChatBox.ChatListener());
		MinecraftForge.EVENT_BUS.register(new PeripheralContainerHandler());
		MinecraftForge.EVENT_BUS.register(new TileEntityAntenna());
		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new PocketMotionDetector());
	}

	public void registerEntities() {
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "ridable_turtle"),
				EntityRidableTurtle.class, "ridable_turtle", 1, PeripheralsPlusPlus.instance,
				64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "nano_bot_swarm"),
				EntityNanoBotSwarm.class, "nano_bot_swarm", 2, PeripheralsPlusPlus.instance,
				64, 20, true);
	}

	public void registerNetwork() {
		NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("ppp");
		NETWORK.registerMessage(AudioPacket.AudioPacketHandler.class, AudioPacket.class, 0, Side.CLIENT);
		NETWORK.registerMessage(AudioResponsePacket.AudioResponsePacketHandler.class, AudioResponsePacket.class, 1, Side.SERVER);
		NETWORK.registerMessage(ChatPacket.ChatPacketHandler.class, ChatPacket.class, 4, Side.CLIENT);
		NETWORK.registerMessage(ScaleRequestPacket.ScaleRequestPacketHandler.class, ScaleRequestPacket.class, 5, Side.CLIENT);
		NETWORK.registerMessage(ScaleRequestResponsePacket.ScaleRequestResponsePacketHandler.class, ScaleRequestResponsePacket.class, 6, Side.SERVER);
		NETWORK.registerMessage(CommandPacket.CommandPacketHandler.class, CommandPacket.class, 7, Side.CLIENT);
		NETWORK.registerMessage(ParticlePacket.ParticlePacketHandler.class, ParticlePacket.class, 8, Side.CLIENT);
		NETWORK.registerMessage(InputEventPacket.InputEventPacketHandler.class, InputEventPacket.class, 9, Side.SERVER);
		NETWORK.registerMessage(GuiPacket.GuiPacketHandler.class, GuiPacket.class, 10, Side.CLIENT);
		NETWORK.registerMessage(TextFieldInputEventPacket.TextFieldInputEventPacketHandler.class, TextFieldInputEventPacket.class, 11, Side.SERVER);
		NETWORK.registerMessage(RidableTurtlePacket.RidableTurtlePacketHandler.class, RidableTurtlePacket.class, 12, Side.SERVER);
		NETWORK.registerMessage(RobotEventPacket.RobotEventPacketHandler.class, RobotEventPacket.class, 13, Side.CLIENT);
        NETWORK.registerMessage(PermCardChangePacket.PermCardChangePacketHandler.class, PermCardChangePacket.class, 14, Side.SERVER);
		NETWORK.registerMessage(SynthPacket.SynthPacketHandler.class, SynthPacket.class, 15, Side.CLIENT);
		NETWORK.registerMessage(SynthResponsePacket.SynthResponsePacketHandler.class, SynthResponsePacket.class, 16, Side.SERVER);
	}

	public void registerCapabilities() {
		CapabilityManager.INSTANCE.register(NanoBotHolder.class, new NanoBotHolderStorage(),
				NanoBotHolderDefault.class);
	}
}
