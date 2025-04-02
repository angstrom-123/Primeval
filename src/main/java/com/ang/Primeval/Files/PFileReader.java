package com.ang.primeval.files;

import com.ang.primeval.exceptions.PFileReadException;
import com.ang.primeval.utils.PArrays;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PFileReader {
	private String mapDirPath;

	public PFileReader(String mapDirPath) {
		this.mapDirPath = mapDirPath;	
	}

	public String[] readFile(String fileName) throws PFileReadException {
		int MAX_LINE_COUNT = 2000;
		String filePath = mapDirPath + fileName;
		if ((fileName == null) || (fileName == "")) {
			throw new PFileReadException("File name is not valid");

		}
		InputStream s = this.getClass().getResourceAsStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(s));
		String[] lines = new String[MAX_LINE_COUNT];
		int head = 0;
		try {
			for (int i = 0; i < MAX_LINE_COUNT; i++) {
				String line;
				boolean lineFound = false;
				while (!lineFound) {
					line = br.readLine();
					if (line == null) {
						break;

					}
					if (line.trim().length() > 0) {
						lines[i] = line;
						head = i;
						lineFound = true;
					}
				}
			}
		} catch (IOException e) {
			throw new PFileReadException("IOException in file reader");

		}
		return PArrays.reduceArray(lines, head);

	}

	public byte[] readFileAsBytes(String fileName) throws PFileReadException {
		String filePath = mapDirPath + fileName;
		if ((fileName == null) || (fileName == "")) {
			throw new PFileReadException("File name is not valid");

		}
		InputStream s = this.getClass().getResourceAsStream(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		try {
			while (true) {
				int read = s.read(buffer);
				if (read <= 0) {
					break;

				}
				baos.write(buffer, 0, read);
			}
		} catch (IOException e) {
			throw new PFileReadException("IOException in binary file reader");

		}
		return baos.toByteArray();

	}
}
