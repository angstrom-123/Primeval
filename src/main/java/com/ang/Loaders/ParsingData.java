package com.ang.Loaders;

public class ParsingData {
	private int worldTypeLineNumber;
	private int positionLineNumber;
	private int facingLineNumber;
	private int coloursLineNumber;
	private WorldType worldType;
	private int[] delimiterLineNumbers = new int[1000];
	private int delimitersHead = 0;

	public ParsingData() {}

	public void setWorldType(WorldType type) {
		worldType = type;
	}

	public void setWorldLineNumber(int num) {
		worldTypeLineNumber = num;
	}

	public void setPositionLineNumber(int num) {
		positionLineNumber = num;
	}

	public void setFacingLineNumber(int num) {
		facingLineNumber = num;
	}

	public void setColoursLineNumber(int num) {
		coloursLineNumber = num;
	}

	public void addDelimiterLineNumber(int num) {
		delimiterLineNumbers[delimitersHead++] = num;
	}

	public WorldType worldType() {
		return worldType;

	}

	public int worldLineNumber() {
		return worldTypeLineNumber;

	}

	public int positionLineNumber() {
		return positionLineNumber;

	}

	public int facingLineNumber() {
		return facingLineNumber;

	}

	public int coloursLineNumber() {
		return coloursLineNumber;

	}

	public int[] delimiterLineNumbers() {
		int[] out = new int[delimitersHead];
		for (int i = 0; i < delimitersHead; i++) {
			out[i] = delimiterLineNumbers[i];
		}
		return out;

	}
}
