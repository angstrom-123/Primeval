package com.ang.primeval.core;

import com.ang.primeval.editor.PEditor;
import com.ang.primeval.exceptions.*;
import com.ang.primeval.hittables.*;
import com.ang.primeval.threads.*;
import com.ang.primeval.inputs.*;
import com.ang.primeval.PGlobal;
import com.ang.primeval.files.PFileReader;
import com.ang.primeval.files.pmap.*;

public class PGame implements PThreadInterface, PMovementInputInterface {
	private final String 	MAP_DIR_PATH	= "/mapData/";
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
		PEditor editor = new PEditor(MAP_DIR_PATH);
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
		PPMapParser mapParser = new PPMapParser(MAP_DIR_PATH + fileName);
		PPMapData mapData;
		try {
			mapData = mapParser.parseMapData(fileData);
		} catch (PPMapParseException e) {
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
