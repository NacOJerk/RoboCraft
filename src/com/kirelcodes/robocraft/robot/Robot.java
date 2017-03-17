package com.kirelcodes.robocraft.robot;

import static com.kirelcodes.robocraft.utils.NMSClassInteracter.getDeclaredField;
import static com.kirelcodes.robocraft.utils.NMSClassInteracter.getField;
import static com.kirelcodes.robocraft.utils.NMSClassInteracter.getNMS;
import static com.kirelcodes.robocraft.utils.NMSClassInteracter.getVersion;
import static com.kirelcodes.robocraft.utils.NMSClassInteracter.setField;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.kirelcodes.robocraft.RoboCraft;
import com.kirelcodes.robocraft.utils.ItemStackUtils;

public class Robot implements InventoryHolder {

	private final int INV_SIZE = 9 * 3;

	private ArmorData[] bodyParts;
	private int ID;
	private String uuid_Creator;//Creator of the mpet UUID VALUE
	private Inventory inventory;
	private LivingEntity navigator;
	private Object nms_handle;
	private Location targetLocation;
	
	public Robot(Player player, Location loc) {
		Chicken naviPre = (Chicken) player.getWorld().spawnEntity(loc, EntityType.CHICKEN);
		Bukkit.getScheduler().scheduleSyncDelayedTask(RoboCraft.getInstance(), new Runnable() {
			public void run() {
				try 
				{
					clearNavigator(loc);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					naviPre.remove();
					return;
				}
			}
		}, 10L);
		this.uuid_Creator = player.getUniqueId().toString();
		this.inventory = RoboCraft.getInstance().getServer().createInventory(this, INV_SIZE);
		this.navigator = naviPre;
		ID = RoboManager.addRobot(this);
	}

	private void setupBodyParts() throws Exception
	{
		bodyParts = new ArmorData[3];
		////////////////////////////
		//     Universal  Name    //
		////////////////////////////
		String idetifier = ChatColor.MAGIC + "RoboCraft^^NacOJerk^^DrPiggy:"+getID();
		////////////////////////////
		//          Head          //
		////////////////////////////
		ArmorStand head = setUpArmor(idetifier);
		head.setHelmet(ItemStackUtils.getSkullFromURL("http://textures.minecraft.net/texture/36af7a3923c8451bc5b0db753b458ee57275ab562114b6b5c2162507e97372a",ChatColor.MAGIC + "EasterEggInCodeSuperCoolAndPro"));
		RelativeLocation relLocHead = new RelativeLocation(0, 0.055, 0);
		bodyParts[0] = new ArmorData(head, relLocHead);
		///////////////////////////
		//         Belly         //
		///////////////////////////
		ArmorStand belly = setUpArmor(idetifier);
		belly.setHelmet(ItemStackUtils.createItem(Material.IRON_BLOCK, ChatColor.RED + "CoolNacOJerkIsPro", "&aDont u love easter eggs ? "));
		RelativeLocation relLocBelly = new RelativeLocation(0, -0.31, 0);
		bodyParts[1] = new ArmorData(belly, relLocBelly);
		///////////////////////////
		//           Rode        //
		///////////////////////////
		ArmorStand rode = setUpArmor(idetifier);
		rode.setHelmet(ItemStackUtils.createItem(Material.END_ROD, ChatColor.RED + "DeathToAllPigs !(jk <3 Bacon)", "&bAnother one how dare they !"));
		RelativeLocation relLocRode = new RelativeLocation(0, -0.03875, -0.15625);
		bodyParts[2] = new ArmorData(rode, relLocRode);
		///////////////////////////
		// 0 - The head
		// 1 - The belly (Iron Block)
		// 2 - The rode (End Rode)
	}
	
	private ArmorStand setUpArmor(String name)
	{
		ArmorStand stand = (ArmorStand) getLocation().getWorld().spawnEntity(getLocation(), EntityType.ARMOR_STAND);;
		stand.setMarker(true);
		stand.setVisible(false);
		stand.setCustomName(name);
		stand.setCustomNameVisible(false);
		stand.setInvulnerable(true);
		stand.setSmall(true);
		return stand;
	}
	
	public Object getNMSHandle() throws Exception 
	{
		return (nms_handle == null) ? (nms_handle = navigator.getClass().getMethod("getHandle").invoke(navigator))
				: nms_handle;
	}

	public void setSpeed(double speed) throws Exception 
	{
		Object MOVEMENT_SPEED = getNMS("GenericAttributes").getField("MOVEMENT_SPEED").get(null);
		Object genericSpeed = getNMSHandle().getClass().getMethod("getAttributeInstance", getNMS("IAttribute"))
				.invoke(getNMSHandle(), MOVEMENT_SPEED);
		genericSpeed.getClass().getMethod("setValue", double.class).invoke(genericSpeed, speed);
	}

	private void clearNavigator(Location loc) throws Exception 
	{
		Object goalSelector = getField(getNMSHandle(), "goalSelector");
		if (getVersion().contains("8")) 
		{
			List<?> goalSelectorA = (List<?>) getDeclaredField(goalSelector, "b");
			List<?> goalSelectorB = (List<?>) getDeclaredField(goalSelector, "c");
			goalSelectorA.clear();
			goalSelectorB.clear();
		} else 
		{
			LinkedHashSet<?> goalSelectorA = (LinkedHashSet<?>) getDeclaredField(goalSelector, "b");
			LinkedHashSet<?> goalSelectorB = (LinkedHashSet<?>) getDeclaredField(goalSelector, "c");
			goalSelectorA.clear();
			goalSelectorB.clear();
		}
		getNMSHandle().getClass().getMethod("setInvisible", boolean.class).invoke(getNMSHandle(), true);
		Object nbtTAG = getNMS("NBTTagCompound").getConstructor().newInstance();
		nbtTAG.getClass().getDeclaredMethod("setString", String.class, String.class).invoke(nbtTAG, "id", "Chicken");
		nbtTAG.getClass().getDeclaredMethod("setBoolean", String.class, boolean.class).invoke(nbtTAG, "Silent", true);
		getNMSHandle().getClass().getMethod("f", getNMS("NBTTagCompound")).invoke(getNMSHandle(), nbtTAG);
		getNavigator().setCustomName(ChatColor.MAGIC + "" + getID());
		getNavigator().setCustomNameVisible(false);
		getNavigator().teleport(loc);
		try 
		{
			setField(getNMSHandle(), "P", 1.0F);
		}
		catch (Exception e)
		{

		}
	}

	private int getID()
	{
		return ID;
	}

	

	@Override
	public Inventory getInventory()
	{
		return inventory;
	}

	public Location getTargetLocation()
	{
		return targetLocation;
	}

	/**
	 * Gets the speed of the chicken (Navigator)
	 * 
	 * @throws Exception
	 */
	public double getSpeed() throws Exception
	{
		Object MOVEMENT_SPEED = getNMS("GenericAttributes").getField("MOVEMENT_SPEED").get(null);
		Object genericSpeed = getNMSHandle().getClass().getMethod("getAttributeInstance", getNMS("IAttribute"))
				.invoke(getNMSHandle(), MOVEMENT_SPEED);
		return (double) genericSpeed.getClass().getMethod("getValue").invoke(genericSpeed);
	}

	public Location getLocation()
	{
		return getNavigator().getLocation();
	}
	
	public boolean onTargetLocation()
	{
		if (targetLocation == null)
			return false;
		if (targetLocation.distanceSquared(getLocation()) < 0.1)
		{
			targetLocation = null;
			return true;
		}
		return false;
	}

	public LivingEntity getNavigator()
	{
		return navigator;
	}

	/**
	 * Sets the location the robot should move to
	 * 
	 * @param loc
	 * @return did it start or not
	 * @throws Exception
	 */
	public boolean setTargetLocation(Location loc) throws Exception
	{
		Object navagation = getNMSHandle().getClass().getMethod("getNavigation").invoke(getNMSHandle());
		Object path = navagation.getClass().getMethod("a", double.class, double.class, double.class).invoke(navagation,
				loc.getX(), loc.getY(), loc.getZ());
		if (path == null)
		{
			return false;
		}
		navagation.getClass().getMethod("a", getNMS("PathEntity"), double.class).invoke(navagation, path, getSpeed());
		navagation.getClass().getMethod("a", double.class).invoke(navagation, 2.0D);
		targetLocation = loc;
		return true;
	}

	public void stopPathfinding() throws Exception
	{
		Object navagation = getNMSHandle().getClass().getMethod("getNavigation").invoke(getNMSHandle());
		navagation.getClass().getMethod("a", double.class).invoke(navagation, 0D);
	}
	
	public String getCreatorUUID()
	{
		return uuid_Creator;
	}
	
	public OfflinePlayer getCreator()
	{
		return Bukkit.getOfflinePlayer(UUID.fromString(uuid_Creator));
	}
	
	public void remove()
	{
		for(ArmorData stand : bodyParts)
		{
			stand.getArmorStand().remove();
		}
		navigator.remove();
		RoboManager.removeRobot(getID());
	}
	
}
