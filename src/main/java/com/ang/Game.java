package com.ang;

import com.ang.Exceptions.*;
import com.ang.Hittables.*;
import com.ang.Loaders.*;
import com.ang.Threads.*;
import com.ang.Inputs.*;

public class Game implements ThreadInterface, InputInterface {
	private final String 	MAP_DIR_PATH	= "/mapData/";
	private final int 		IMAGE_WIDTH 	= 600;
	private final int 		FRAME_MS 		= 1000 / 60;
	private boolean[] 		keyInputs 		= new boolean[256];
	private InputListener 	il 				= new InputListener(this);
	private Camera 			cam 			= new Camera(IMAGE_WIDTH);
	private CameraMover		controller  	= new CameraMover(cam);
	private HittableList	world;

	public Game(String mapFileName) {
		if (mapFileName == null) {
			System.out.println("Regular game entry point goes here");
			return;

		}
		boolean didLoad = loadMapFile(mapFileName);
		if (didLoad) {
			cam.init(il);
			Global.uw = new UpdateWorker(FRAME_MS);
			Global.uw.setInterface(this);
			System.out.println("Level loaded successfully");
		} else {
			System.out.println("Failed to load level");
		}
	}

	public boolean loadMapFile(String fileName) {
		MapFileReader mapReader = new MapFileReader(MAP_DIR_PATH);
		String[] fileData;
		try {
			fileData = mapReader.readFile(fileName);
		} catch (MapReadException e) {
			e.printStackTrace();
			return false; 

		}
		MapFileParser mapParser = new MapFileParser(MAP_DIR_PATH + fileName);
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

	public void startGame() {
		if (world != null) {
			Global.uw.run();
		}
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
