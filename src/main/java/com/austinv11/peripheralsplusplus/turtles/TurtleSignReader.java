package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralSignReader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleSignReader implements ITurtleUpgrade
{
    @Override
    public int getUpgradeID() {
		return Reference.SIGN_READER_UPGRADE;
	}

    @Override
    public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.signReader";
	}

    @Override
    public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

    @Override
    public ItemStack getCraftingItem() {
		return new ItemStack(Items.sign);
	}

    @Override
    public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new PeripheralSignReader(turtle);
    }

    @Override
    public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
        return ModBlocks.dummyBlock.getIcon(0,2);
    }

    @Override
    public void update(ITurtleAccess turtle, TurtleSide side) {}
}
