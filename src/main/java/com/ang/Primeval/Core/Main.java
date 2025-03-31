package com.ang.Primeval.Core;

public class Main {
    public static void main(String[] args) {
		Game g = new Game();
		g.start(parseArgs(args));
    }

	private static Mode[] parseArgs(String[] args) {
		Mode[] out = new Mode[args.length];
		int head = 0;
		for (String arg : args) {
			switch (arg) {
			case "-test":
			case "-t":
				out[head++] = Mode.TEST;
				break;

			case "-editor":
			case "-edit":
			case "-e":
				out[head++] = Mode.EDIT;
				break;

			case "-game":
			case "-g":
				out[head++] = Mode.GAME;
				break;

			default:
				System.err.println("Invalid arguments");
				return new Mode[0];

			}
		}
		return out;

	}
}
