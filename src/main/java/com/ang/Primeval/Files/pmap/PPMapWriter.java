package com.ang.primeval.files.pmap;

import java.io.FileWriter;

import com.ang.primeval.hittables.PSector;
import com.ang.primeval.maths.PVec2;
import com.ang.primeval.exceptions.PFileWriteException;
import com.ang.primeval.exceptions.PPMapWriteException;
import com.ang.primeval.files.PFileWriter;

public class PPMapWriter {
	private String mapDirPath;

	public PPMapWriter(String mapDirPath) {
		this.mapDirPath = mapDirPath;
	}

	public void writePMap(String fileName, PPMapData mapData) 
			throws PPMapWriteException {
		PFileWriter writer = new PFileWriter(mapDirPath);
		try {
			writer.newFile(fileName);
			writer.writeToFile(fileName, toPMap(mapData));
		} catch (PFileWriteException e) {
			throw new PPMapWriteException(e.getMessage());

		}
	}

	private String[] toPMap(PPMapData mapData) {
		String[] lines = new String[calculateFileLength(mapData)];
		int head = 0;
		lines[head++] = "!PMAPv1.0.0";
		lines[head++] = "!CORNER";
		for (PSector sec : mapData.world().sectors()) {
			for (PVec2 vec : sec.corners()) {
				lines[head++] = String.valueOf(vec.x()) + " " + String.valueOf(vec.y());
			}
		}
		lines[head++] = "!SECTOR";
		lines[head++] = "0";
		int sectorEnd = 0;
		for (PSector sec : mapData.world().sectors()) {
			lines[head++] = String.valueOf(sectorEnd + sec.corners().length);
		}
		lines[head++] = "!HEIGHT";
		for (PSector sec : mapData.world().sectors()) {
			lines[head++] = String.valueOf(sec.floorHeight()) 
					+ " " + String.valueOf(sec.ceilingHeight());
		}
		lines[head++] = "!PORTAL";
		for (PSector sec : mapData.world().sectors()) {
			for (int i = 0; i < sec.portalIndices().length - 1; i++) {
				lines[head++] = String.valueOf(sec.portalIndices()[i])
						+ " " + String.valueOf(sec.portalIndices()[i + 1]);
			}
		}
		lines[head++] = "!POSITION";
		lines[head++] = String.valueOf(mapData.position().x())
				+ " " + String.valueOf(mapData.position().y());
		lines[head++] = "!FACING";
		lines[head++] = String.valueOf(mapData.facing().x())
				+ " " + String.valueOf(mapData.facing().y());
		lines[head++] = "!COLOUR";
		lines[head++] = "1.0 1.0 1.0"; // TODO: implement for real
		return lines;

	}

	private int calculateFileLength(PPMapData mapData) {
		int count = 8;
		count += 2;
		for (PSector sec : mapData.world().sectors()) {
			count += 2;
			count += sec.portalIndices().length / 2;
			count += sec.corners().length;
		}
		count += 1; // TODO: temporarily adding 1 colour, implement this later
		return count;

	}
}
