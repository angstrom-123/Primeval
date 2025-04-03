package com.ang.primeval.files;

import com.ang.primeval.exceptions.PFileWriteException;

import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
		List<String> linesList = Arrays.asList(lines);
		try {
			Files.write(Paths.get(fileName), linesList, StandardCharsets.UTF_8,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new PFileWriteException("Could not write to " + fileDir + fileName);

		}
	}
}
