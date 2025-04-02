package com.ang.Primeval.Core;

import com.ang.Primeval.Editor.PMapEditor;
import com.ang.Primeval.Exceptions.*;
import com.ang.Primeval.Hittables.*;
import com.ang.Primeval.Threads.*;
import com.ang.Primeval.Inputs.*;
import com.ang.Primeval.PGlobal;
import com.ang.Primeval.Files.PFileReader;
import com.ang.Primeval.Files.PMAP.*;

public class PGame implements PThreadInterface, PMovementInputInterface {
	private final String 	MAP_DIR_PATH	= "/mapData/";
	private final String 	FONT_DIR_PATH	= "/fontData/";
	private final int 		IMAGE_WIDTH 	= 600;
	private final int 		FRAME_MS 		= 1000 / 60;
	private boolean[] 		keyInputs 		= new boolean[256];
	private PMovementInputListener listener 	= new PMovementInputListener(this);
	private PCamera 			cam 			= new PCamera(IMAGE_WIDTH);
	private PCameraMover		controller  	= new PCameraMover(cam);
	private PSectorWorld		world;

	public PGame() {}

	public void start(PMode[] modes) {
		boolean test = false;
		boolean edit = false;
		boolean game = false;
		for (PMode mode : modes) {
			switch (mode) {
			case TEST:
				test = true;
				break;
			
			case EDIT:
				edit = true;
				break;

			case GAME:
				game = true;
				break;

			}
		}
		if ((edit && game) || (test && !edit && !game)) {
			System.err.println("Invalid arguments");
			return;

		}
		if (game) {
			if (test) {
				testGame();
			} else {
				runGame();
			}
		} else if (edit) {
			if (test) {
				testEditor();
			} else {
				runEditor();
			}
		}
	}

	// TODO:
	private void runEditor() {
		System.out.println("Regular editor entry point goes here");
	}

	// TODO:
	private void runGame() {
		System.out.println("Regular game entry point goes here");
	}

	private void testEditor() {
		PMapEditor editor = new PMapEditor(MAP_DIR_PATH, FONT_DIR_PATH);
		editor.test();
	}

	private void testGame() {
		if (!loadMapFile("testMap.pmap")) {
			System.err.println("Failed to load test level");
			return;

		}
		System.out.println("Test level loaded successfully");
		cam.init(listener);
		PGlobal.uw = new PUpdateWorker(FRAME_MS);
		PGlobal.uw.setInterface(this);
		PGlobal.uw.run();
	}

	private boolean loadMapFile(String fileName) {
		PFileReader mapReader = new PFileReader(MAP_DIR_PATH);
		String[] fileData;
		try {
			fileData = mapReader.readFile(fileName);
		} catch (PFileReadException e) {
			e.printStackTrace();
			return false; 

		}
		PMapParser mapParser = new PMapParser(MAP_DIR_PATH + fileName);
		PMapData mapData;
		try {
			mapData = mapParser.parseMapData(fileData);
		} catch (PMapParseException e) {
			e.printStackTrace();
			return false;

		}
		world = mapData.world();
		cam.setTransform(mapData.position(), mapData.facing());	
		return true;

	}

	@Override
	public void update() {
		controller.update(keyInputs);
		cam.update();
		cam.draw(world);
	}

	@Override
	public void pressed(int key) {
		keyInputs[key] = true;
	}

	@Override
	public void released(int key) {
		keyInputs[key] = false;
	}

}
