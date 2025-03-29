package com.ang.Primeval.Loaders;

import com.ang.Primeval.Exceptions.MapReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapFileReader {
	private String mapDirPath;

	public MapFileReader(String mapDirPath) {
		this.mapDirPath = mapDirPath;	
	}

	public String[] readFile(String fileName) throws MapReadException {
		String filePath = mapDirPath + fileName;
		if ((fileName == null) || (fileName == "")) {
			throw new MapReadException("Map file name is not valid");

		}
		System.out.println("dir = " + filePath);
		int lineCount = countLines(filePath);
		return readLines(filePath, lineCount);

	}

	private int countLines(String filePath) throws MapReadException {
		int count = 0;
		InputStream s = this.getClass().getResourceAsStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(s));
		try {
			while (true) {
				String line = br.readLine();	
				if (line == null) {
					return count;

				}
				if (line.trim().length() > 0) {
					count++;
				}
			}
		} catch (IOException e) {
			throw new MapReadException("IOException in map reader");

		}
	}

	private String[] readLines(String filePath, int lineCount) throws MapReadException {
		String[] lines = new String[lineCount];
		InputStream s = this.getClass().getResourceAsStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(s));
		try {
			for (int i = 0; i < lineCount; i++) {
				String line;
				boolean lineFound = false;
				while (!lineFound) {
					line = br.readLine();
					if (line == null) {
						return lines;

					}
					if (line.trim().length() > 0) {
						lines[i] = line;
						lineFound = true;
					}
				}
			}
		} catch (IOException e) {
			throw new MapReadException("IOException in map reader");

		}
		return lines;

	}
}
