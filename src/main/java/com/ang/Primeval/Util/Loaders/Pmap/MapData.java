package com.ang.Primeval.Util.Loaders.Pmap;

import com.ang.Primeval.Core.Hittables.SectorWorld;
import com.ang.Primeval.Core.Maths.Vec2;

public class MapData {
	private SectorWorld world;
	private Vec2 position;
	private Vec2 facing;

	public MapData() {}

	public void setWorld(SectorWorld world) {
		this.world = world;
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public void setFacing(Vec2 facing) {
		this.facing = facing;
	}

	public SectorWorld world() {
		return world;
	
	}

	public Vec2 position() {
		return position;

	}

	public Vec2 facing() {
		return facing;

	}

	public boolean isPopulated() {
		if ((world != null) && (position != null) && (facing != null)) {
			return true;

		}
		return false;

	}
}
