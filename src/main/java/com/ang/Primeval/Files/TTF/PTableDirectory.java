package com.ang.Primeval.Files.TTF;

public class PTableDirectory {
	public byte[] tag;
	public int checkSum;
	public int offset;
	public int length;

	public PTableDirectory() {}

	public String tagToString() {
		String out = "";
		for (byte b : tag) {
			out += (char) b;
		}
		return out;

	}
}
