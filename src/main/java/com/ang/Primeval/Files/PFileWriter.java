package com.ang.Primeval.Files;

import java.io.File;
import java.io.IOException;

import com.ang.Primeval.Exceptions.PFileWriteException;

public class PFileWriter {
	private String fileDir;

	public PFileWriter(String fileDir) {
		this.fileDir = fileDir;
	}

	public void newFile(String fileName) throws PFileWriteException {
		File f = new File(fileName);	
		try {
			if (f.createNewFile()) {

			}
		} catch (IOException e) {
			throw new PFileWriteException("IOException in write file");

		}
	}

	//public void 
}
