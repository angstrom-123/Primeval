package com.ang.Primeval.Loaders;

public enum WorldType {
	CUBEWORLD,
	SECTORWORLD;

	public boolean isCubeWorld() {
		if (this == CUBEWORLD) {
			return true;

		}
		return false;

	}

	public boolean isSectorWorld() {
		return !isCubeWorld();

	}
}
