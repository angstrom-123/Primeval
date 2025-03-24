package com.ang;

public class Main {
    public static void main(String[] args) {
		Game g = new Game(parseArgs(args));
		g.startGame();
    }

	private static String parseArgs(String[] args) {
		if ((args.length == 2) && (args[0].equals("-test"))) {
			switch (args[1]) {
			case "-cubeworld":
			case "-cw":
			case "-c":
				return "testCubeMap.pmap";

			case "-sectorworld":
			case "-sw":
			case "-s":
				return "testSectorMap.pmap";

			default:
				return "testCubeMap.pmap";

			}
		}
		return null;

	}
}
