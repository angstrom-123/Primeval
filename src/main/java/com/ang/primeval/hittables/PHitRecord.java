package com.ang.primeval.hittables;

import com.ang.primeval.graphics.PColour;

public class PHitRecord {
	private double t;
	private PColour colour;
	private double sectorFloor;
	private double sectorCeiling;

	public PHitRecord() {}

	public PHitRecord copy() {
		PHitRecord temp = new PHitRecord();
		temp.setT(t);
		temp.setColour(colour);
		temp.setFloor(sectorFloor);
		temp.setCeiling(sectorCeiling);
		return temp;

	}

	public double t() {
		return t;

	}

	public PColour colour() {
		return colour;

	}

	public double sectorFloor() {
		return sectorFloor;

	}

	public double sectorCeiling() {
		return sectorCeiling;

	}

	public void setT(double t) {
		this.t = t;
	}

	public void setColour(PColour colour) {
		this.colour = colour;
	}

	public void setFloor(double floorHeight) {
		this.sectorFloor = floorHeight;
	}

	public void setCeiling(double ceilingHeight) {
		this.sectorCeiling = ceilingHeight;
	}
}
