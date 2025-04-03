package com.ang.primeval.files.pmap;

import com.ang.primeval.PGlobal;
import com.ang.primeval.exceptions.*;
import com.ang.primeval.graphics.PColour;
import com.ang.primeval.utils.PArrays;
import com.ang.primeval.hittables.*;
import com.ang.primeval.maths.*;

public class PPMapParser {
	private String path;

	public PPMapParser(String path) {
		this.path = path;
	}

	public PPMapData parseMapData(String[] lines) throws PPMapParseException {
		PPMapData mapData = new PPMapData();
		mapData.setPosition(parseSingleVector(lines, "!POSITION"));
		mapData.setFacing(parseSingleVector(lines, "!FACING"));
		if (lines[0].equals("!PMAPv1.0.0")) {
			mapData.setWorld(parseWorld(lines));
		} else {
			throw new PPMapParseException(path, 0);

		}
		return mapData;

	}

	private PVec2 parseSingleVector(String[] lines, String match) throws PPMapParseException {
		PVec2[] extracted = new PVec2[0];
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
			throw new PPMapParseException(path, lineNum);

		}
		return extracted[0];

	}

	private PSectorWorld parseWorld(String[] lines) throws PPMapParseException {
		PVec2[] corners = new PVec2[0];
		int[] sectors = new int[0];
		PVec2[] heights = new PVec2[0];
		PVec2[] portals = new PVec2[0];
		PColour[] colours = new PColour[0];
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
			throw new PPMapParseException("Must specify corners and sectors and colours");

		}
		return consructWorld(corners, sectors, heights, portals, colours);

	}

	private PSectorWorld consructWorld(PVec2[] corners, int[] sectors, PVec2[] heights, 
			PVec2[] portals, PColour[] colours) throws PPMapParseException {
		PSectorWorld world = new PSectorWorld(1000);
		for (int i = 0; i < sectors.length; i++) {
			// get sector limits
			int limit = (i == sectors.length - 1)
			? corners.length
			: sectors[i + 1];
			// read corners within limits
			PVec2[] sectorCorners = new PVec2[limit - sectors[i]];
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
			sectorPortals = PArrays.reduceArray(sectorPortals, head);
			// create sector
			PSector sec = new PSector(sectorCorners, sectorPortals);	
			sec.setHeight(heights[i].x(), heights[i].y());
			world.addSector(sec);
		}
		return world;

	}

	private int[] extractInts(int startLine, String[] lines) 
			throws PPMapParseException {
		int[] array = new int[lines.length - startLine];
		int head = 0;
		for (int i = startLine; i < lines.length; i++) {
			String line = lines[i];
			if (line.charAt(0) == '!') {
				if (head == 0) {
					throw new PPMapParseException(path, i);

				}
				break;

			}
			String[] nums = line.split("\\s+");
			if (nums.length != 1) {
				throw new PPMapParseException(path, i);

			}
			int num = Integer.valueOf(nums[0]);
			array[head++] = num;
		}
		return PArrays.reduceArray(array, head);

	}

	private PVec2[] extractVec2s(int startLine, String[] lines) 
			throws PPMapParseException {
		PVec2[] array = new PVec2[lines.length - startLine];
		int head = 0;
		for (int i = startLine; i < lines.length; i++) {
			String line = lines[i];
			if (line.charAt(0) == '!') {
				if (head == 0) {
					throw new PPMapParseException(path, i);

				}
				break;

			}
			String[] nums = line.split("\\s+");
			if (nums.length != 2) {
				throw new PPMapParseException(path, i);

			}
			double x = Double.valueOf(nums[0]);
			double y = Double.valueOf(nums[1]);
			array[head++] = new PVec2(x, y);
		}
		return PArrays.reduceArray(array, head);

	}

	private PColour[] extractColours(int startLine, String[] lines) 
			throws PPMapParseException {
		PColour[] array = new PColour[lines.length - startLine];
		int head = 0;
		for (int i = startLine; i < lines.length; i++) {
			String line = lines[i];
			if (line.charAt(0) == '!') {
				if (head == 0) {
					throw new PPMapParseException(path, i);

				}
				return new PColour[0];	

			}
			String[] nums = line.split("\\s+");
			if (nums.length != 3) {
				throw new PPMapParseException(path, i);

			}
			double r = Double.valueOf(nums[0]);
			double g = Double.valueOf(nums[1]);
			double b = Double.valueOf(nums[2]);
			array[head++] = new PColour(r, g, b);
		}
		return PArrays.reduceArray(array, head);

	}
}
