package com.ang.primeval.files.pmap;

import com.ang.primeval.PGlobal;
import com.ang.primeval.files.PFileReader;
import com.ang.primeval.exceptions.*;

import java.io.File;

public class PPMapHandler {
	private String mapDirPath;
	private PPMapSaveData saveData;

	public PPMapHandler(String mapDirPath) {
		this.mapDirPath = mapDirPath;
		saveData = new PPMapSaveData();
	}

	public void setSavedMapData(PPMapData mapData) {
		saveData.savedMapData = mapData;
	}

	public void setEditableMapData(PPMapData mapData) {
		saveData.editableMapData = mapData;
	}

	public PPMapSaveData saveData() {
		return saveData;

	}

	public void saveMap(File file) throws PFileWriteException {
		String fileName = validateMapFile(file);
		if (fileName == null) {
			throw new PFileWriteException("Attempted to save invalid map file at "
					+ mapDirPath + fileName);

		}
		PPMapWriter writer = new PPMapWriter(mapDirPath);
		try {
			writer.writePMap(fileName, saveData.editableMapData);
			System.out.println("saved " + fileName);
		} catch (PPMapWriteException e) {
			throw new PFileWriteException(e.getMessage());

		}
		saveData.savedMapData = saveData.editableMapData.copy();
		saveData.fileName = fileName;
	}

	public void loadMapData(File file) throws PFileLoadException {
		String fileName = validateMapFile(file);
		if (fileName == null) {
			throw new PFileLoadException("Attempted to load invalid map file at "
					+ mapDirPath + file.getName());

		}
		String[] lines;
		PFileReader reader = new PFileReader(mapDirPath);
		try {
			lines = reader.readFile(fileName);
		} catch (PFileReadException e) {
			throw new PFileLoadException(e.getMessage());

		}
		PPMapData mapData;
		PPMapParser parser = new PPMapParser(mapDirPath + fileName);
		try {
			mapData = parser.parseMapData(lines);
		} catch (PPMapParseException e) {
			throw new PFileLoadException(e.getMessage());

		}
		saveData.editableMapData = mapData;
		saveData.savedMapData = mapData.copy();
		saveData.fileName = fileName;
	}

	private String validateMapFile(File file) {
		if ((file == null) || file.isDirectory()) {
			return null;

		}
		String fileName = file.getName();
		if (!validMapFileName(fileName)) {
			fileName += ".pmap";
			if (!validMapFileName(fileName)) {
				return null;

			}
		}
		if (!validMapFile(file)) {
			return null;

		}
		return fileName;

	}

	private boolean validMapFile(File file) {
		if (file.isDirectory()) {
			return false;

		}
		String parentFileName = file.getAbsoluteFile().getParentFile().getName();
		String mapDirPathStripped = mapDirPath.substring(1, mapDirPath.length() - 1);
		if (!parentFileName.equals(mapDirPathStripped)) {
			return false;

		}
		return true;

	}

	private boolean validMapFileName(String fileName) {
		if (fileName == null) {
			return false;

		}
		String[] tokens = fileName.split("[.]");
		if (fileName.charAt(0) == '.') {
			return false;

		}
		if (tokens.length != 2) {
			return false;

		}
		if (!tokens[1].equals("pmap")) {
			return false;

		}
		return true;

	}
}
