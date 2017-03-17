package com.kirelcodes.robocraft.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 * 
 * @author NacOJerk
 *
 */
public class CommandManager {
	private HashMap<String, ExtendedCommandBase> nameCommand;//HashMap containing all the commands
	private String majorCommand; //The base command also known as the "Major Command"
	private MajorCommand mjco; //Stands for MajorCommandObject
	private ExtendedCommandBase defaultCommand;
	/**
	 * 
	 * @param mjco stands for MajorCommandObject the major command that this manages
	 */
	public CommandManager(MajorCommand mjco) {
		nameCommand = new HashMap<>();
		this.majorCommand = mjco.getName();
		this.mjco = mjco;
	}
	/**
	 * 
	 * @return the major command object
	 */
	public MajorCommand getMajorCommand(){
		return mjco;
	}
	/**
	 * Gets the main command name 
	 * @return
	 */
	public String getMajorCommandName() {
		return majorCommand;
	}
	/**
	 * Adds a command to the HashMap
	 * @param ecx
	 */
	public void addCommand(ExtendedCommandBase ecx) {
		/*if (nameCommand.containsKey(ecx.getCommand())) {
			System.out.println("Would soon be added");
			return;
		}*/
		nameCommand.put(ecx.getCommand().toLowerCase(), ecx);
		ecx.setCommandManager(this);
	}
	/**
	 * Removes a command from the HashMap
	 * @param name
	 * @return
	 */
	public ExtendedCommandBase removeCommand(String name) {
		return nameCommand.remove(name.toLowerCase());
	}
	/**
	 * Removes a command from the HashMap
	 * @param ecx
	 * @return
	 */
	public boolean removeCommand(ExtendedCommandBase ecx) {
		return nameCommand.remove(ecx.getCommand().toLowerCase(), ecx);
	}
	/**
	 * Called when a player execute a command
	 * @param command
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean executeCommand(String command, CommandSender sender,
			String[] args) {
		/*if (!nameCommand.containsKey(command)) {
			System.out.println("Would soon be added");
			return true;
		}*/
		return nameCommand.get(command.toLowerCase()).executeCommand(sender, command, args,
				(sender instanceof Player));
	}
	/**
	 * Called when a player execute a command
	 * @param command
	 * @param sender
	 * @param args
	 * @return
	 */
	public boolean executeCommandDefault(String command, CommandSender sender,
			String[] args) {
		/*if (!nameCommand.containsKey(command)) {
			System.out.println("Would soon be added");
			return true;
		}*/
		return getDefaultCommand().executeCommand(sender, command, args,
				(sender instanceof Player));
	}
	/**
	 * Called when a player presses TAB
	 * @param command
	 * @param sender
	 * @param alias
	 * @param args
	 * @return
	 */
	public List<String> tabComplete(String command, CommandSender sender,
			String alias, String[] args) {
		if (!nameCommand.containsKey(command.toLowerCase())) 
			return new ArrayList<>();
		if(nameCommand.get(command.toLowerCase()).tabComplete(sender, alias, args) == null)
			return new ArrayList<>();
		return nameCommand.get(command.toLowerCase()).tabComplete(sender, alias, args);
	}
	/**
	 * Clears the HashMap
	 */
	public void clear(){
		nameCommand = new HashMap<>();
	}
	/**
	 * Returns a list of all commands added
	 * @return
	 */
	public ArrayList<String> getCommandNames(){
		return new ArrayList<>(nameCommand.keySet());
	}
	/**
	 * Returns a list of all commands added
	 * @return
	 */
	public ArrayList<ExtendedCommandBase> getCommands(){
		return new ArrayList<>(nameCommand.values());
	}
	/**
	 * Returns if the manager contains a certain command
	 * @param command
	 * @return
	 */
	public boolean containsCommand(String command){
		return getCommandNames().contains(command.toLowerCase());
	}
	/**
	 * Returns the Extended Command
	 * @param command
	 * @return
	 */
	public ExtendedCommandBase getCommand(String command){
		return nameCommand.get(command.toLowerCase());
	}
	
	/**
	 * Setts the default command to execute when the command is called empty
	 * @param defaultCommand the command
	 */
	public void addDefaultCommand(ExtendedCommandBase defaultCommand)
	{
		this.defaultCommand = defaultCommand;
	}
	
	/**
	 * Does the major command have a default command 
	 * @return if it has a default command
	 */
	public boolean hasDefaultCommand(){
		return this.defaultCommand != null;
	}
	
	/**
	 * The default command to execute when args is empty
	 * @return the default command
	 */
	public ExtendedCommandBase getDefaultCommand(){
		return defaultCommand;
	}
}
