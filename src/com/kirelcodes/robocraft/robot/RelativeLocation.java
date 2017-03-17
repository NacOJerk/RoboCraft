package com.kirelcodes.robocraft.robot;

public class RelativeLocation {
	private double x,y,z;
	public RelativeLocation(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
}
