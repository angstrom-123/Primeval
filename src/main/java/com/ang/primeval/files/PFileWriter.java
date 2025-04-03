package com.ang.primeval.files;

import com.ang.primeval.exceptions.PFileWriteException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PFileWriter {
	private String fileDir;

	public PFileWriter(String fileDir) {
		this.fileDir = fileDir;
	}

	public void newFile(String fileName) throws PFileWriteException {
		File f = new File(fileName);	
		try {
			if (!f.createNewFile()) {
				throw new PFileWriteException(fileDir + fileName + " already exists");

			}
		} catch (IOException e) {
			throw new PFileWriteException("IOException in write file");

		}
	}

	public void writeToFile(String fileName, String line) throws PFileWriteException {
		writeToFile(fileName, new String[]{line});
	}

	public void writeToFile(String fileName, String[] lines) throws PFileWriteException {
		try {
			for (String line : lines) {
				Files.write(Paths.get(fileName), line.getBytes(StandardCharsets.UTF_8));
			}
		} catch (IOException e) {
			throw new PFileWriteException("Could not write to " + fileDir + fileName);

		}
	}
}
