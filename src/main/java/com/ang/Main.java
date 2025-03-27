package com.ang;

public class Main {
    public static void main(String[] args) {
		Game g = new Game(parseArgs(args));
		g.startGame();
    }

	private static String parseArgs(String[] args) {
		if (args.length != 0) {
			switch (args[0]) {
			case "-test":
			case "-t":
				return "testMap.pmap";

			}
		}
		return null;

	}
}
