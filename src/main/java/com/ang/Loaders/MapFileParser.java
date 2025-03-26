package com.ang.Loaders;

import com.ang.Hittables.*;
import com.ang.Exceptions.MapParseException;
import com.ang.Graphics.Colour;
import com.ang.Maths.Vec2;

public class MapFileParser {
	private String path;
	private String[] lines;

	public MapFileParser(String path) {
		this.path = path;
	}

	public MapData parseMapData(String[] lines) throws MapParseException {
		this.lines = lines;
		ParsingData parsingData = new ParsingData();
		if (!validFile(parsingData)) {
			throw new MapParseException(path + " Is not a valid map file");

		}
		return populateMapData(parsingData);

	}

	private MapData populateMapData(ParsingData pData) throws MapParseException {
		MapData mapData = new MapData();
		// get line numbers from first pass
		int worldLine = pData.worldLineNumber();
		int positionLine = pData.positionLineNumber();
		int facingLine = pData.facingLineNumber();
		int coloursLine = pData.coloursLineNumber();
		// parse all lines
		WorldType worldType = pData.worldType();
		HittableList world;
		if (worldType.isCubeWorld()) {
			world = parseCubeWorld(worldLine, parseColours(coloursLine));	
		} else {
			world = parseSectorWorld(worldLine, parseColours(coloursLine), 
					pData.delimiterLineNumbers(), pData.portalLineNumbers());	
		}
		Vec2 position = vecIsNotArray(parseVec2(positionLine));
		Vec2 facing = vecIsNotArray(parseVec2(facingLine));
		// set map data
		mapData.setWorldType(worldType);
		mapData.setWorld(world);
		mapData.setPosition(position);
		mapData.setFacing(facing);
		return mapData;

	}

	private Vec2 vecIsNotArray(Vec2[] vecs) throws MapParseException {
		if (vecs.length != 1) {
			throw new MapParseException("Invalid array length. Vec2 single expected");
		}
		return vecs[0];

	}

	private boolean validFile(ParsingData pData) {
		if (!charsMatch(lines[0], "TOP") || !charsMatch(lines[lines.length - 1], "BOTTOM")) {
			return false;

		}
		boolean worldFound = false;
		boolean positionFound = false;
		boolean facingFound = false;
		boolean coloursFound = false;
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.charAt(line.length() - 1) == '/') {
				pData.addDelimiterLineNumber(i);
			}
			if (line.charAt(0) == 'P') {
				pData.addPortalLineNumber(i);
			}
			if (line.charAt(0) == '-') {
				if (charsMatch(line, "-SECTORWORLD")) {
					worldFound = true;
					pData.setWorldType(WorldType.SECTORWORLD);
					pData.setWorldLineNumber(i);
				}
				if (charsMatch(line, "-CUBEWORLD")) {
					worldFound = true;
					pData.setWorldType(WorldType.CUBEWORLD);
					pData.setWorldLineNumber(i);
				}
				if (charsMatch(line, "-POSITION")) {
					positionFound = true;
					pData.setPositionLineNumber(i);
				}
				if (charsMatch(line, "-FACING")) {
					facingFound = true;
					pData.setFacingLineNumber(i);
				}
				if (charsMatch(line, "-COLOURS")) {
					coloursFound = true;
					pData.setColoursLineNumber(i);
				}
			}
		}
		return worldFound && positionFound && facingFound && coloursFound;

	}
	
	private HittableList parseSectorWorld(int headLineNum, Colour[] colours, 
			int[] delimiterLineNumbers, int[] portalLineNumbers) throws MapParseException {
		final int MAX_SECTOR_CORNERS = 20;
		MapFileDataType type = parseDataType(headLineNum);
		if (type != MapFileDataType.VEC2) {
			throw new MapParseException(path, headLineNum);

		}
		int delimiterPtr = 0;
		int portalPtr = 0;
		int sectorCount = parseSectorCount(headLineNum);
		HittableList world = new HittableList(sectorCount);
		Vec2[] vectors = parseVec2(headLineNum);
		Vec2[] corners = new Vec2[MAX_SECTOR_CORNERS];
		int cornersHead = 0;
		int[] portalCorners = new int[MAX_SECTOR_CORNERS];
		int portalCornersHead = 0;
		for (int i = 0; i < vectors.length; i++) {
			int currentLine = headLineNum + i + 2;
			corners[cornersHead++] = vectors[i];
			if ((portalPtr < portalLineNumbers.length) 
					&& (currentLine == portalLineNumbers[portalPtr])) {
				portalPtr++;
				portalCorners[portalCornersHead++] = i;
			}
			if (currentLine == delimiterLineNumbers[delimiterPtr]) {
				delimiterPtr++;
				Vec2[] cornersToAdd = new Vec2[cornersHead];
				for (int j = 0; j < cornersToAdd.length; j++) {
					cornersToAdd[j] = corners[j];
				}
				Sector toAdd = new Sector(cornersToAdd, portalCorners);
				world.addHittable(toAdd);
				corners = new Vec2[MAX_SECTOR_CORNERS];
				cornersHead = 0;
				portalCorners = new int[MAX_SECTOR_CORNERS];
				portalCornersHead = 0;
			}
		}
		return world;

	}

	private HittableList parseCubeWorld(int headLineNum, Colour[] colours) 
			throws MapParseException {
		MapFileDataType type = parseDataType(headLineNum);
		if (type != MapFileDataType.INTS) {
			throw new MapParseException(path, headLineNum);

		}
		int arrayLength = parseArrayLength(headLineNum);
		String[] arrayLines = extractDataUnderHeader(headLineNum);
		int[][] map = new int[arrayLines.length][arrayLength];
		for (int i = 0; i < arrayLines.length; i++) {
			String line = arrayLines[i];
			boolean didStart = false;
			boolean didEnd = false;
			int[] parsedLine = new int[arrayLength];
			int parsedLineHead = 0;
			for (int j = 0; j < line.length(); j++) {
				char c = line.charAt(j);
				if (!didStart && (c == '<')) {
					didStart = true;
					continue;

				}
				if (didStart && !didEnd && (c == '>')) {
					didEnd = true;
					continue;

				}
				if (didStart && !didEnd && (c != '|')) {
					parsedLine[parsedLineHead++] = c - '0'; 
				}
			}
			if (!didStart || !didEnd) {
				throw new MapParseException(path, headLineNum, i);

			}
			map[i] = parsedLine;
		}
		return new CubeWorld(map, colours); 	

	}

	private Vec2[] parseVec2(int headLineNum) throws MapParseException {
		MapFileDataType type = parseDataType(headLineNum);
		if (type != MapFileDataType.VEC2) {
			throw new MapParseException(path, headLineNum);

		}
		String[] arrayLines = extractDataUnderHeader(headLineNum);
		Vec2[] parsedLines = new Vec2[arrayLines.length];
		for (int i = 0; i < arrayLines.length; i++) {
			String line = arrayLines[i];
			boolean didStart = false;
			boolean didEnd = false;
			Vec2 parsedLine = new Vec2();
			String dataString = "";
			int axisToAdd = 0;
			for (int j = 0; j < line.length(); j++) {
				char c = line.charAt(j);
				if (!didStart && (c == '<')) {
					didStart = true;
					continue;

				}
				if (didStart && (c == '>')) {
					didEnd = true;
					parsedLine.setAxis(axisToAdd++, Double.valueOf(dataString));
					dataString = "";
					continue;

				} 
				if (didStart && !didEnd) {
					if (c != '|') {
						dataString += c;	
					} else {
						parsedLine.setAxis(axisToAdd++, Double.valueOf(dataString));
						dataString = "";
					}
				}
			}
			if (!didStart || !didEnd) {
				throw new MapParseException(path, headLineNum);

			}
			parsedLines[i] = parsedLine;
		}
		return parsedLines;

	}

	private Colour[] parseColours(int headLineNum) throws MapParseException {
		MapFileDataType type = parseDataType(headLineNum);
		if (type != MapFileDataType.COLOUR) {
			throw new MapParseException(path, headLineNum);

		}
		String[] arrayLines = extractDataUnderHeader(headLineNum);
		Colour[] colours = new Colour[arrayLines.length];
		int coloursHead = 0;
		for (int i = 0; i < arrayLines.length; i++) {
			String line = arrayLines[i];
			boolean didStart = false;
			boolean didEnd = false;
			Colour parsedLine = new Colour();
			String dataString = "";
			int componentToAdd = 0;
			for (int j = 0; j < line.length(); j++) {
				char c = line.charAt(j);
				if (!didStart && (c == '<')) {
					didStart = true;
					continue;

				}
				if (didStart && (c == '>')) {
					didEnd = true;
					parsedLine.setComponent(componentToAdd++, Double.valueOf(dataString));
					dataString = "";
					continue;

				}
				if (didStart && !didEnd) {
					if (c != '|') {
						dataString += c;
					} else {
						parsedLine.setComponent(componentToAdd++, Double.valueOf(dataString));
						dataString = "";
					}
				}
			}
			if (!didStart || !didEnd) {
				throw new MapParseException(path, headLineNum, i);

			}
			colours[coloursHead++] = parsedLine;
		}
		return colours;

	}
	
	private int parseIntBetweenChars(int headLineNum, char starter, char ender) 
			throws MapParseException {
		return parseIntBetweenChars(headLineNum, starter, ender, ender);

	}

	private int parseIntBetweenChars(int headLineNum, char starter, char ender, char altEnder) 
			throws MapParseException {
		boolean didStart = false;
		boolean didEnd = false;
		String dataString = "";
		String line = lines[headLineNum];
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (!didStart && (c == starter)) {
				didStart = true;
				continue;

			}
			if (didStart && ((c == ender) || (c == altEnder))) { 
				didEnd = true;
				break;

			}
			if (didStart) {
				dataString += c;	
				continue;

			}
		}
		if (!didStart || !didEnd) {
			throw new MapParseException(path, headLineNum);

		}
		return Integer.valueOf(dataString);

	}

	private int parseSectorCount(int headLineNum) throws MapParseException {
		return parseIntBetweenChars(headLineNum, ':', '#');

		//boolean didStart = false;
		//boolean didEnd = false;
		//String dataString = "";
		//String line = lines[headLineNum];
		//for (int i = 0; i < line.length(); i++) {
		//	char c = line.charAt(i);
		//	if (!didStart && (c == ':')) {
		//		didStart = true;
		//		continue;
		//
		//	}
		//	if (didStart && (c == '#')) { 
		//		didEnd = true;
		//		break;
		//
		//	}
		//	if (didStart && !didEnd) {
		//		dataString += c;	
		//		continue;
		//
		//	}
		//}
		//if (!didStart || !didEnd) {
		//	throw new MapParseException(path, headLineNum);
		//
		//}
		//return Integer.valueOf(dataString);
		//
	}

	private int parseArrayLength(int headLineNum) throws MapParseException {
		return parseIntBetweenChars(headLineNum, '~', '#', ':');

		//boolean didStart = false;
		//boolean didEnd = false;
		//String dataString = "";
		//String line = lines[headLineNum];
		//for (int i = 0; i < line.length(); i++) {
		//	char c = line.charAt(i);
		//	if (!didStart && (c == '~')) {
		//		didStart = true;
		//		continue;
		//
		//	}
		//	if (didStart && ((c == '#') || (c == ':'))) {
		//		didEnd = true;
		//		break;
		//
		//	}
		//	if (didStart && !didEnd) {
		//		dataString += c;	
		//		continue;
		//
		//	}
		//}
		//if (!didStart || !didEnd) {
		//	throw new MapParseException(path, headLineNum);
		//
		//}
		//return Integer.valueOf(dataString);
		//
	}

	private MapFileDataType parseDataType(int headLineNum) throws MapParseException {
		boolean didStart = false;
		boolean didEnd = false;
		String dataString = "";
		String line = lines[headLineNum];
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (!didStart && (c == '#')) {
				didStart = true;
				continue;

			} 
			if (didStart && (c == '~')) {
				didEnd = true;
				continue;

			}
			if (didStart && !didEnd) {
				dataString += c;	
			}
		}
		if (!didStart || !didEnd) {
			throw new MapParseException(path, headLineNum);

		}
		if (charsMatch(dataString, "INT*")) {
			int len = Integer.valueOf(dataString.substring(3));
			MapFileDataType temp = MapFileDataType.INTS;
			temp.setLength(len);
			return temp;

		} else if (charsMatch(dataString, "VEC2")) {
			return MapFileDataType.VEC2;

		} else if (charsMatch(dataString, "INT")) {
			return MapFileDataType.INT;

		} else if (charsMatch(dataString, "COLOUR")) {
			return MapFileDataType.COLOUR;

		} else {
			throw new MapParseException(path, headLineNum);

		}
	}

	private String[] extractDataUnderHeader(int headLineNum) throws MapParseException {
		int arrayLength = parseArrayLength(headLineNum);
		String[] arrayLines = new String[arrayLength];
		int arrayLinesHead = 0;
		boolean didStart = false;
		boolean didEnd = false;
		for (int i = headLineNum + 1; i < headLineNum + arrayLength + 3; i++) {
			String line = lines[i];
			if (!didStart && charsMatch(line, "START")) {
				didStart = true;
				continue;

			}
			if (didStart && !didEnd && charsMatch(line, "END")) {
				didEnd = true;
				continue;

			}
			if (didStart && !didEnd) {
				arrayLines[arrayLinesHead++] = line;
			}
		}
		if (!didStart || !didEnd) {
			throw new MapParseException();

		}
		return arrayLines;

	}

	public boolean charsMatch(String input, String pattern) {
		final char WILD_CARD = '*';
		char[] inp = input.toCharArray();
		char[] pat = pattern.toCharArray();
		int patternPtr = 0;
		char wildEnd = ' ';
		if (pat.length > inp.length) {
			return false;

		}
		for (int i = 0; i < inp.length; i++) {
			if (patternPtr >= pat.length) {
				return true;

			}
			if ((pat[patternPtr] == WILD_CARD) && (wildEnd == ' ')) {
				if (patternPtr >= pat.length - 1) {
					return true;

				}
				wildEnd = pat[patternPtr + 1];
				continue;

			}
			if (inp[i] == wildEnd) {
				wildEnd = ' ';
				patternPtr++;
			}
			if (pat[patternPtr] != WILD_CARD) {
				if (inp[i] != pat[patternPtr]) {
					return false;

				}
				patternPtr++;
			}
		}
		return true;

	}
}
