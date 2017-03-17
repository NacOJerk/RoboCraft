package com.kirelcodes.robocraft.robot;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class ArmorData {
	private ArmorStand armor;
	private RelativeLocation relLoc;
	
	public ArmorData(ArmorStand armor , RelativeLocation relLoc)
	{
		this.armor = armor;
		this.relLoc = relLoc;
	}
	
	public ArmorStand getArmorStand()
	{
		return armor;
	}
	
	public RelativeLocation getRelativeLocation()
	{
		return relLoc;
	}
	
	public void teleport(Location loc)
	{
		armor.teleport(loc.add(getRelativeLocation().getX() , getRelativeLocation().getY(), getRelativeLocation().getZ()));
	}
	
}
