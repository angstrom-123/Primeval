package com.ang.Primeval.Core;

import com.ang.Primeval.Editor.MapEditor;
import com.ang.Primeval.Exceptions.*;
import com.ang.Primeval.Core.Hittables.*;
import com.ang.Primeval.Core.Threads.*;
import com.ang.Primeval.Inputs.*;
import com.ang.Primeval.Util.Loaders.FileReader;
import com.ang.Primeval.Util.Loaders.Pmap.*;

public class Game implements ThreadInterface, MovementInputInterface {
	private final String 	MAP_DIR_PATH	= "/mapData/";
	private final int 		IMAGE_WIDTH 	= 600;
	private final int 		FRAME_MS 		= 1000 / 60;
	private boolean[] 		keyInputs 		= new boolean[256];
	private MovementInputListener listener 	= new MovementInputListener(this);
	private Camera 			cam 			= new Camera(IMAGE_WIDTH);
	private CameraMover		controller  	= new CameraMover(cam);
	private SectorWorld		world;

	public Game() {}

	public void start(Mode[] modes) {
		boolean test = false;
		boolean edit = false;
		boolean game = false;
		for (Mode mode : modes) {
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
		MapEditor editor = new MapEditor(MAP_DIR_PATH);
		editor.test();
	}

	private void testGame() {
		if (!loadMapFile("testMap.pmap")) {
			System.err.println("Failed to load test level");
			return;

		}
		System.out.println("Test level loaded successfully");
		cam.init(listener);
		Global.uw = new UpdateWorker(FRAME_MS);
		Global.uw.setInterface(this);
		Global.uw.run();
	}

	private boolean loadMapFile(String fileName) {
		FileReader mapReader = new FileReader(MAP_DIR_PATH);
		String[] fileData;
		try {
			fileData = mapReader.readFile(fileName);
		} catch (FileReadException e) {
			e.printStackTrace();
			return false; 

		}
		PmapParser mapParser = new PmapParser(MAP_DIR_PATH + fileName);
		MapData mapData;
		try {
			mapData = mapParser.parseMapData(fileData);
		} catch (MapParseException e) {
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
