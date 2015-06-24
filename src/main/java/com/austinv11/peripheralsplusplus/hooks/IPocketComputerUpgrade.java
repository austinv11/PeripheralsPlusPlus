package com.austinv11.peripheralsplusplus.hooks;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This is like {@link dan200.computercraft.api.turtle.ITurtleUpgrade} but for Pocket Computers
 */
public interface IPocketComputerUpgrade {
	
	/**
	 * The upgrade id <b>cannot</b> be 1! That is reserved by ComputerCraft
	 * @return The id
	 */
	public int getUpgradeID();
	
	/**
	 * Same as {@link ITurtleUpgrade#getUnlocalisedAdjective()}
	 */
	public String getUnlocalisedAdjective();
	
	/**
	 * Same as {@link ITurtleUpgrade#getUnlocalisedAdjective()}
	 */
	public ItemStack getCraftingItem();
	
	/**
	 * Called to instantiate a peripheral
	 * @param entity The entity holding the pocket computer <b>NOTE:</b> This can be null!
	 * @param stack The stack representing the computer
	 * @return The peripheral
	 */
	public IPeripheral createPeripheral(Entity entity, ItemStack stack);
	
	/**
	 * Called when the pocket computer itemstack updates
	 * @param entity The entity holding the itemstack
	 * @param stack The pocket computer itemstack
	 * @param peripheral The peripheral on the pocket computer
	 */
	public void update(Entity entity, ItemStack stack, IPeripheral peripheral);
	
	/**
	 * Called when the pocket computer is right clicked on something
	 * @param world The world the computer is in
	 * @param player The player right clicking
	 * @param stack The stack representing the computer
	 * @param peripheral The peripheral on the computer
	 * @return True to stop the gui from opening or vice versa
	 */
	public boolean onRightClick(World world, EntityPlayer player, ItemStack stack, IPeripheral peripheral);
}
