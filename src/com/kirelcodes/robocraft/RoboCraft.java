package com.kirelcodes.robocraft;

import org.bukkit.plugin.java.JavaPlugin;

public class RoboCraft extends JavaPlugin{
	private static RoboCraft instance = null;
	
	@Override
	public void onEnable()
	{
		instance = this;
	}
	
	public static RoboCraft getInstance()
	{
		return instance;
	}
	
}
