package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.pocket.PocketMotionDetector;
import com.austinv11.peripheralsplusplus.pocket.PocketPeripheralContainer;
import com.austinv11.peripheralsplusplus.turtles.*;
import com.austinv11.peripheralsplusplus.utils.IPlusPlusPeripheral;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class ModPeripherals {
    public static final List<ITurtleUpgrade> TURTLE_UPGRADES = new ArrayList<>();
    public static final List<IPocketUpgrade> POCKET_UPGRADES = new ArrayList<>();

    public static void registerInternally() {
        TURTLE_UPGRADES.add(new TurtleChatBox());
        TURTLE_UPGRADES.add(new TurtlePlayerSensor());
        TURTLE_UPGRADES.add(new TurtleCompass());
        TURTLE_UPGRADES.add(new TurtleXP());
        TURTLE_UPGRADES.add(new TurtleBarrel());
		TURTLE_UPGRADES.add(new TurtleOreDictionary());
		TURTLE_UPGRADES.add(new TurtleEnvironmentScanner());
		TURTLE_UPGRADES.add(new TurtleFeeder());
		TURTLE_UPGRADES.add(new TurtleShear());
		TURTLE_UPGRADES.add(new TurtleSignReader());
		TURTLE_UPGRADES.add(new TurtleGarden());
		TURTLE_UPGRADES.add(new TurtleSpeaker());
		TURTLE_UPGRADES.add(new TurtleTank());
        TURTLE_UPGRADES.add(new TurtleRidable());
		TURTLE_UPGRADES.add(new TurtleDispenser());
		TURTLE_UPGRADES.add(new TurtleResupply());
		TURTLE_UPGRADES.add(new TurtleChunkLoader());
		// TODO re-implement project red/blue turtles with a dynamic turtle registration for any tools/weapons
        POCKET_UPGRADES.add(new PocketMotionDetector());
        POCKET_UPGRADES.add(new PocketPeripheralContainer());
    }

    public static void registerWithComputerCraft() {
        PeripheralsPlusPlus.LOGGER.info("Registering peripherals...");
        ComputerCraftAPI.registerPeripheralProvider(new IPlusPlusPeripheral.Provider());
        PeripheralsPlusPlus.LOGGER.info("Registering turtle upgrades...");
		for (ITurtleUpgrade upgrade : TURTLE_UPGRADES) {
            ComputerCraftAPI.registerTurtleUpgrade(upgrade);
            if (upgrade instanceof TurtleDropCollector)
                MinecraftForge.EVENT_BUS.register(((TurtleDropCollector) upgrade).newInstanceOfListener());
        }
        PeripheralsPlusPlus.LOGGER.info("Registering pocket computer upgrades...");
		for (IPocketUpgrade pocketUpgrade : POCKET_UPGRADES)
		    ComputerCraftAPI.registerPocketUpgrade(pocketUpgrade);
    }
}
