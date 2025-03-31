package com.ang.Primeval.Util.Loaders;

import com.ang.Primeval.Exceptions.FileReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReader {
	private String mapDirPath;

	public FileReader(String mapDirPath) {
		this.mapDirPath = mapDirPath;	
	}

	public String[] readFile(String fileName) throws FileReadException {
		String filePath = mapDirPath + fileName;
		if ((fileName == null) || (fileName == "")) {
			throw new FileReadException("File name is not valid");

		}
		int lineCount = countLines(filePath);
		return readLines(filePath, lineCount);

	}

	public char[] readFileAsChars(String fileName) throws FileReadException {
		String filePath = mapDirPath + fileName;
		if ((fileName == null) || (fileName == "")) {
			throw new FileReadException("File name is not valid");

		}
		int lineCount = countLines(filePath);
		return readChars(filePath, lineCount);

	}

	private int countLines(String filePath) throws FileReadException {
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
			throw new FileReadException("IOException in file reader");

		}
	}

	private String[] readLines(String filePath, int lineCount) throws FileReadException {
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
			throw new FileReadException("IOException in file reader");

		}
		return lines;

	}

	private char[] readChars(String filePath, int lineCount) throws FileReadException {
		final int MAX_LINE_LEN = 1000;
		char[] chars = new char[lineCount * MAX_LINE_LEN];
		int head = 0;
		InputStream s = this.getClass().getResourceAsStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(s));
		try {
			for (int i = 0; i < lineCount; i++) {
				String line;
				boolean lineFound = false;
				while (!lineFound) {
					line = br.readLine();
					if (line == null) {
						return chars;

					}
					String trimmed = line.trim();
					int lineLength = trimmed.length();
					if (lineLength > 0) {
						lineFound = true;
					}
					for (int j = 0; j < lineLength; j++) {
						chars[head++] = trimmed.charAt(j);
					}
				}
			}
		} catch (IOException e) {
			throw new FileReadException("IOException in file reader");

		}
		return chars;

	}
}
