package com.kirelcodes.robocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;

/**
 * 
 * @author NacOJerk
 *
 */
public class MajorCommand extends BukkitCommand {
	
	private CommandManager cm;
	private String prefix, needToAddArgs, noSuchCommand, noPermissions;
	private SimpleCommandMap scm;

	public MajorCommand(String name) {
		super(name);
		this.cm = new CommandManager(this);
		try {
			SimpleCommandMap smp = (SimpleCommandMap) getOBC("CraftServer").getMethod("getCommandMap")
					.invoke(Bukkit.getServer());
			smp.register(name, this);
			scm = smp;
			register(smp);
		} catch (Exception e) {

		}
	}

	/////////////////////////////////////////////////////////////
	private String getVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName();
		version = version.split("\\.")[3];
		return version;// Getting Version
	}

	private Class<?> getClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;// Getting class

		}
	}

	private Class<?> getOBC(String OBC) {
		return getClass("org.bukkit.craftbukkit." + getVersion() + "." + OBC);// Getting
																				// OBC
																				// class
	}

	////////////////////////////////////////////////////////////////
	// Full Support//
	/**
	 * 
	 * @return the command manager of this command
	 */
	public CommandManager getCommandManager() {
		return cm;
	}

	/**
	 * Sets the command's prefix
	 * 
	 * @param prefix
	 * @return
	 */
	public MajorCommand setPrefix(String prefix) {
		this.prefix = prefix.replaceAll("&", "§");
		return this;
	}

	/**
	 * 
	 * @return the prefix (with a space after it to save a bit of time :P)
	 */
	public String getPrefix() {
		return prefix + " " + ChatColor.RESET;
	}

	/**
	 * Sets the message that would be sent when the player just enters the
	 * command (With nothing after it) (Already adds prefix auto)
	 * 
	 * @param argsMessage
	 * @return
	 */
	public MajorCommand setArgsMessage(String argsMessage) {
		this.needToAddArgs = argsMessage.replaceAll("&", "§");
		return this;
	}

	/**
	 * Gets the message that would be sent when the player just enters the
	 * command (With nothing after it) (Already adds prefix auto)
	 * 
	 * @return
	 */
	public String getArgsMessage() {
		return getPrefix() + needToAddArgs;
	}

	/**
	 * Sets the message that would be sent when the player just enters a command
	 * that doesnt exists (Already adds prefix auto)
	 * 
	 * @param argsMessage
	 * @return
	 */
	public MajorCommand setNoSuchCommandMessage(String noSuchCommand) {
		this.noSuchCommand = noSuchCommand.replaceAll("&", "§");
		return this;
	}

	/**
	 * Gets the message that would be sent when the player just enters a command
	 * that doesnt exists (Already adds prefix auto)
	 * 
	 * @param argsMessage
	 * @return
	 */
	public String getNoSuchCommandMessage() {
		return getPrefix() + noSuchCommand;
	}

	/**
	 * Sets the message that would be sent when a player tries to use a command
	 * that he dont have permission for it (Already adds prefix auto)
	 * 
	 * @param noPerms
	 * @return
	 */
	public MajorCommand setNoPermission(String noPerms) {
		this.noPermissions = noPerms.replaceAll("&", "§");
		return this;
	}

	/**
	 * Gets the message that would be sent when a player tries to use a command
	 * that he dont have permission for it (Already adds prefix auto)
	 * 
	 * @return
	 */
	public String getNoPermission() {
		return getPrefix() + noPermissions;
	}

	@Override
	public boolean execute(CommandSender sender, String cmd, String[] args) {
		if (args.length == 0 && !getCommandManager().hasDefaultCommand()) {
			sender.sendMessage(getArgsMessage());
			return true;
		}
		String command = "";
		if (args.length != 0) {
			command = args[0];
			if (!getCommandManager().containsCommand(command)) {
				sender.sendMessage(getNoSuchCommandMessage());
				return true;
			}
		} else {
			command = getName();
		}
		ExtendedCommandBase ecb;
		if (args.length != 0) {
			ecb = getCommandManager().getCommand(command);
		} else {
			ecb = getCommandManager().getDefaultCommand();
		}
		if (ecb.hasPermmision()) {
			if (!sender.hasPermission(ecb.getPermission())) {
				sender.sendMessage(getNoPermission());
				return true;
			}
		}
		String[] newArgs;
		if (args.length == 1) {
			String[] empty = {};
			newArgs = empty;
		} else {
			ArrayList<String> arrays = new ArrayList<>();
			for (int i = 1; i < args.length; i++) {
				arrays.add(args[i]);
			}
			String[] mid = new String[arrays.size()];
			arrays.toArray(mid);
			newArgs = mid;
		}
		if (args.length != 0)
			return getCommandManager().executeCommand(command, sender, newArgs);
		else
			return getCommandManager().executeCommandDefault(command, sender, newArgs);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		if (args.length == 1) {
			ArrayList<String> array = new ArrayList<>();
			for (ExtendedCommandBase ecb : getCommandManager().getCommands()) {
				if (ecb.hasPermmision()) {
					if (!sender.hasPermission(ecb.getPermission()))
						continue;
				}
				if (ecb.getCommand().toLowerCase().startsWith(args[0].toLowerCase()))
					array.add(ecb.getCommand());
			}
			return array;
		}
		if (args.length > 1) {
			if (!getCommandManager().containsCommand(args[0])) {
				return super.tabComplete(sender, alias, args);
			}
			String[] newArgs;
			ArrayList<String> arrays = new ArrayList<>();
			for (int i = 1; i < args.length; i++) {
				arrays.add(args[i]);
			}
			String[] mid = new String[arrays.size()];
			arrays.toArray(mid);
			newArgs = mid;
			return getCommandManager().tabComplete(args[0], sender, alias, newArgs);
		}
		return super.tabComplete(sender, alias, args);
	}

	public void unregister() {
		unregister(scm);
	}
}
