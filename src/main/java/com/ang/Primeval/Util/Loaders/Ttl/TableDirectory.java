package com.ang.Primeval.Util.Loaders.Ttl;

public class TableDirectory {
	public byte[] tag;
	public int checkSum;
	public int offset;
	public int length;

	public TableDirectory() {}

	public String tagToString() {
		String out = "";
		for (byte b : tag) {
			out += (char) b;
		}
		return out;

	}
}
