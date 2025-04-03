package com.ang.primeval.files.pmap;

import com.ang.primeval.hittables.PSectorWorld;
import com.ang.primeval.maths.PVec2;

public class PPMapData {
	private PSectorWorld world;
	private PVec2 position;
	private PVec2 facing;

	public PPMapData() {}

	public PPMapData copy() {
		PPMapData temp = new PPMapData();
		temp.setWorld(world.copy());
		temp.setPosition(position.copy());
		temp.setFacing(facing.copy());
		return temp;

	}

	public void setWorld(PSectorWorld world) {
		this.world = world;
	}

	public void setPosition(PVec2 position) {
		this.position = position;
	}

	public void setFacing(PVec2 facing) {
		this.facing = facing;
	}

	public PSectorWorld world() {
		return world;
	
	}

	public PVec2 position() {
		return position;

	}

	public PVec2 facing() {
		return facing;

	}

	public boolean isPopulated() {
		if ((world != null) && (position != null) && (facing != null)) {
			return true;

		}
		return false;

	}
}
