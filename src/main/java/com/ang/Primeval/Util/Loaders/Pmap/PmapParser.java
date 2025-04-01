package com.ang.Primeval.Util.Loaders.Pmap;

import com.ang.Primeval.Util.Global;
import com.ang.Primeval.Exceptions.*;
import com.ang.Primeval.Graphics.Colour;
import com.ang.Primeval.Core.Hittables.*;
import com.ang.Primeval.Core.Maths.*;

public class PmapParser {
	private String path;

	public PmapParser(String path) {
		this.path = path;
	}

	public MapData parseMapData(String[] lines) throws MapParseException {
		MapData mapData = new MapData();
		mapData.setPosition(parseSingleVector(lines, "!POSITION"));
		mapData.setFacing(parseSingleVector(lines, "!FACING"));
		if (lines[0].equals("!PMAPv1.0.0")) {
			mapData.setWorld(parseWorld(lines));
		} else {
			throw new MapParseException(path, 0);

		}
		return mapData;

	}

	private Vec2 parseSingleVector(String[] lines, String match) throws MapParseException {
		Vec2[] extracted = new Vec2[0];
		int lineNum = 0;
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.equals(match)) {
				extracted = extractVec2s(i + 1, lines);	
				lineNum = i + 1;
				break;

			}
		}
		if (extracted.length != 1) {
			throw new MapParseException(path, lineNum);

		}
		return extracted[0];

	}

	private SectorWorld parseWorld(String[] lines) throws MapParseException {
		Vec2[] corners = new Vec2[0];
		int[] sectors = new int[0];
		Vec2[] heights = new Vec2[0];
		Vec2[] portals = new Vec2[0];
		Colour[] colours = new Colour[0];
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.equals("!CORNER")) {
				corners = extractVec2s(i + 1, lines);
			} else if (line.equals("!SECTOR")) {
				sectors = extractInts(i + 1, lines);	
			} else if (line.equals("!HEIGHT")) {
				heights = extractVec2s(i + 1, lines);	
			} else if (line.equals("!PORTAL")) {
				portals = extractVec2s(i + 1, lines);
			} else if (line.equals("!COLOUR")) {
				colours = extractColours(i + 1, lines);	
			}
		}
		if ((corners.length == 0) || (sectors.length == 0) || (colours.length == 0)) {
			throw new MapParseException("Must specify corners and sectors and colours");

		}
		return consructWorld(corners, sectors, heights, portals, colours);

	}

	private SectorWorld consructWorld(Vec2[] corners, int[] sectors, 
			Vec2[] heights, Vec2[] portals, Colour[] colours) throws MapParseException {
		SectorWorld world = new SectorWorld(1000);
		for (int i = 0; i < sectors.length; i++) {
			// get sector limits
			int limit = (i == sectors.length - 1)
			? corners.length
			: sectors[i + 1];
			// read corners within limits
			Vec2[] sectorCorners = new Vec2[limit - sectors[i]];
			for (int j = sectors[i]; j < limit; j++) {
				sectorCorners[j - sectors[i]] = corners[j];	
			}
			// read portals within limits
			int[] sectorPortals = new int[sectorCorners.length];
			int head = 0;
			for (int j = 0; j < portals.length; j++) {
				if ((portals[j].x() < limit) && (portals[j].y() < limit)) {
					sectorPortals[head++] = (int) portals[j].x() - sectors[i];
					sectorPortals[head++] = (int) portals[j].y() - sectors[i];
				}
			}
			sectorPortals = Global.reduceArray(sectorPortals, head);
			// create sector
			Sector sec = new Sector(sectorCorners, sectorPortals);	
			sec.setHeight(heights[i].x(), heights[i].y());
			world.addSector(sec);
		}
		return world;

	}

	private int[] extractInts(int startLine, String[] lines) 
			throws MapParseException {
		int[] array = new int[lines.length - startLine];
		int head = 0;
		for (int i = startLine; i < lines.length; i++) {
			String line = lines[i];
			if (line.charAt(0) == '!') {
				if (head == 0) {
					throw new MapParseException(path, i);

				}
				break;

			}
			String[] nums = line.split("\\s+");
			if (nums.length != 1) {
				throw new MapParseException(path, i);

			}
			int num = Integer.valueOf(nums[0]);
			array[head++] = num;
		}
		return Global.reduceArray(array, head);

	}

	private Vec2[] extractVec2s(int startLine, String[] lines) 
			throws MapParseException {
		Vec2[] array = new Vec2[lines.length - startLine];
		int head = 0;
		for (int i = startLine; i < lines.length; i++) {
			String line = lines[i];
			if (line.charAt(0) == '!') {
				if (head == 0) {
					throw new MapParseException(path, i);

				}
				break;

			}
			String[] nums = line.split("\\s+");
			if (nums.length != 2) {
				throw new MapParseException(path, i);

			}
			double x = Double.valueOf(nums[0]);
			double y = Double.valueOf(nums[1]);
			array[head++] = new Vec2(x, y);
		}
		return Global.reduceArray(array, head);

	}

	private Colour[] extractColours(int startLine, String[] lines) 
			throws MapParseException {
		Colour[] array = new Colour[lines.length - startLine];
		int head = 0;
		for (int i = startLine; i < lines.length; i++) {
			String line = lines[i];
			if (line.charAt(0) == '!') {
				if (head == 0) {
					throw new MapParseException(path, i);

				}
				return new Colour[0];	

			}
			String[] nums = line.split("\\s+");
			if (nums.length != 3) {
				throw new MapParseException(path, i);

			}
			double r = Double.valueOf(nums[0]);
			double g = Double.valueOf(nums[1]);
			double b = Double.valueOf(nums[2]);
			array[head++] = new Colour(r, g, b);
		}
		return Global.reduceArray(array, head);

	}
}
