package com.ang.Primeval.Files.PMAP;

import com.ang.Primeval.Hittables.PSectorWorld;
import com.ang.Primeval.Maths.PVec2;

public class PMapData {
	private PSectorWorld world;
	private PVec2 position;
	private PVec2 facing;

	public PMapData() {}

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
