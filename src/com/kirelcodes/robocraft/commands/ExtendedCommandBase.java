package com.kirelcodes.robocraft.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * 
 * @author NacOJerk
 *
 */
public abstract class ExtendedCommandBase {
	
	private String command, //The command's name
					permission, //The command's permission
					help;  //The command's help / how to use / description
	private boolean hasPerms = false,//States if the command has a permission
					 hasHelp = false;//States if the command has a help / how to use / description
	private CommandManager cm;
	/**
	 * 
	 * @param command the command's name
	 * @param permission the command' permission
	 * @param help the command's help / description / how to use
	 */
	public ExtendedCommandBase(String command, String permission, String help) {
		this.command = command;
		setPermission(permission);
		setHelp(help);
	}
	/**
	 * 
	 * @param command the command's name
	 */
	public ExtendedCommandBase(String command) {
		this.command = command;
	}

	/**
	 * Sets the command's permission;
	 * 
	 * @param permission
	 * @return
	 */
	public ExtendedCommandBase setPermission(String permission) {
		this.permission = permission.toLowerCase();
		hasPerms = true;
		return this;
	}

	/**
	 * Sets the command's displayed help
	 * 
	 * @param help
	 * @return
	 */
	public ExtendedCommandBase setHelp(String help) {
		this.help = help;
		hasHelp = true;
		return this;
	}
	/**
	 * Called to execute the command
	 * @param sender
	 * @param command
	 * @param args
	 * @param sentViaPlayer
	 * @return
	 */
	public abstract boolean executeCommand(CommandSender sender,
			String command, String[] args, boolean sentViaPlayer);
	/**
	 * Called when a players presses tab
	 * @param sender
	 * @param alias
	 * @param args
	 * @return
	 */
	public abstract List<String> tabComplete(CommandSender sender,
			String alias, String[] args);
	/**
	 * 
	 * @return the command's name
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * 
	 * @return the command's permission
	 */
	public String getPermission() {
		return permission;
	}
	/**
	 * 
	 * @return the command's help
	 */
	public String getHelp() {
		return help;
	}
	/**
	 * 
	 * @return if the command has a permission
	 */
	public boolean hasPermmision() {
		return hasPerms;
	}
	/**
	 * 
	 * @return if the command has a help / how to use / description
	 */
	public boolean hasHelp() {
		return hasHelp;
	}
	/**
	 * Sets the command manager
	 * @param cm
	 */
	protected void setCommandManager(CommandManager cm){
		this.cm = cm;
	}
	/**
	 * 
	 * @return the command manger that manages this extended command
	 */
	protected CommandManager getCommandManager(){
		return cm;
	}
	@Override
	public String toString() {
		return getCommand();
	}
}