package com.kirelcodes.robocraft.robot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RoboManager {
	
	private static final int ID_RANGE_MIN = 100000,
			ID_RANGE_MAX = 20000;
	
	private static Map<Integer, Robot> map = new HashMap<>();
	
	public static boolean contains(int id)
	{
		return map.containsKey(id);
	}
	
	public static int getFreeID()
	{
		int id = new Random().nextInt(ID_RANGE_MAX - ID_RANGE_MIN + 1) + ID_RANGE_MIN;
		if(!contains(id))
			return id;
		return getFreeID();
	}
	
	public static void addRobot(int id, Robot rob)
	{
		map.put(id, rob);
	}
	
	public static int addRobot(Robot rob)
	{
		int id = getFreeID();
		addRobot(id, rob);
		return id;
	}

	public static boolean removeRobot(int id)
	{
		return map.remove(id) != null;
	}

	public static Robot getRobot(int id)
	{
		return map.get(id);
	}
	
	public static boolean hasRobot(int id)
	{
		return getRobot(id) != null;
	}
	
	public static Collection<Robot> getRobots()
	{
		return map.values();
	}
	
	
	
}
