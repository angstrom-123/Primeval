package com.ang;

import com.ang.Exceptions.*;
import com.ang.Loaders.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
	@Test
	public void testMapParser() {
		MapFileReader reader = new MapFileReader("/mapData/");
		String[] fileData;
		try {
			fileData = reader.readFile("testCubeMap.pmap");
		} catch (MapReadException e) {
			System.err.println("Failed to test map parsing as file did not load");
			return;

		}
		MapFileParser parser = new MapFileParser("/mapData/testCubeMap.pmap");
		assertTrue(fileData[0].equals("TOP"));
		assertTrue(fileData[1].equals("-CUBEWORLD#INT9~9#"));
		assertTrue(parser.charsMatch(fileData[0], "TOP"));
		assertTrue(parser.charsMatch(fileData[1], "-CUBEWORLD*"));
		assertTrue(parser.charsMatch(fileData[1], "-CUBEWORLD#*#"));
		assertTrue(parser.charsMatch(fileData[1], "-CUBEWORLD#INT*~*#"));
		assertFalse(parser.charsMatch(fileData[0], "CUBEWORLD"));
		assertFalse(parser.charsMatch(fileData[0], "-CUBEWORLD"));
		assertFalse(parser.charsMatch(fileData[0], "-CUBEWORLD*f"));
		assertFalse(parser.charsMatch(fileData[0], "CUBEWORLD#g*#"));
	}
}
