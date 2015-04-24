package com.austinv11.peripheralsplusplus.commands;

import com.austinv11.collectiveframework.dependencies.DependencyManager;
import com.austinv11.collectiveframework.dependencies.download.FileType;
import com.austinv11.collectiveframework.dependencies.download.NoProviderFoundException;
import com.austinv11.collectiveframework.minecraft.CollectiveFramework;
import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.multithreading.SimpleRunnable;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import java.util.Arrays;
import java.util.List;

public class CommandUpdate implements ICommand {
	
	private static volatile boolean isUpdated = false;
	private static volatile boolean isUpdating = false;
	
	@Override
	public String getCommandName() {
		return "ppp-update";
	}
	
	@Override
	public String getCommandUsage(ICommandSender commandSender) {
		return "ppp-update";
	}
	
	@Override
	public List getCommandAliases() {
		return Arrays.asList(new String[]{getCommandName()});
	}
	
	@Override
	public void processCommand(final ICommandSender commandSender, String[] args) {
		if (CollectiveFramework.IS_DEV_ENVIRONMENT) {
			commandSender.addChatMessage(new ChatComponentText(Colors.RED+"Cannot run this command in a dev environment!"));
			return;
		}
		if (isUpdated || !PeripheralsPlusPlus.VERSION_CHECKER.isUpdateAvailable())
			commandSender.addChatMessage(new ChatComponentTranslation("peripheralsplusplus.chat.updatedAlready"));
		else if (isUpdating)
			commandSender.addChatMessage(new ChatComponentTranslation("peripheralsplusplus.chat.stillUpdating"));
		else if (!isUpdated && !isUpdating && PeripheralsPlusPlus.VERSION_CHECKER.isUpdateAvailable()) {
			commandSender.addChatMessage(new ChatComponentTranslation("peripheralsplusplus.chat.startUpdate"));
			isUpdating = true;
			new SimpleRunnable() {
				
				@Override
				public void run() {
					PeripheralsPlusPlus.LOGGER.info("Starting to update Peripherals++");
					try {
						isUpdating = true;
						DependencyManager.downloadFile(PeripheralsPlusPlus.VERSION_CHECKER.getDownloadUrl(), PeripheralsPlusPlus.currentFile.getParent()+"/"+PeripheralsPlusPlus.VERSION_CHECKER.getUpdateFileName(), FileType.BINARY);
						PeripheralsPlusPlus.currentFile.delete();
						isUpdated = true;
						isUpdating = false;
						PeripheralsPlusPlus.LOGGER.info("Update successful! Please restart Minecraft");
						commandSender.addChatMessage(new ChatComponentTranslation("peripheralsplusplus.chat.updateSucceeded"));
					} catch (NoProviderFoundException e) {
						PeripheralsPlusPlus.LOGGER.error("Update failed!");
						commandSender.addChatMessage(new ChatComponentTranslation("peripheralsplusplus.chat.updateFailed"));
						e.printStackTrace();
					} finally {
						this.disable(true);
					}
				}
				
				@Override
				public String getName() {
					return "Peripherals++ Update Thread";
				}
			}.start();
		}
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
		return true;
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
