package com.ang.primeval.files.ttf;

import com.ang.primeval.PGlobal;

public class PTTFParser {
	private String path;
	private int head;

	public PTTFParser(String path) {
		this.path = path;
	}

	public void readTtl(byte[] bytes) {
		head = 0;
		readFontDirectory(bytes);
	}
	
	private void readFontDirectory(byte[] bytes) {
		POffsetSubtable offSub = readOffsetSubtable(bytes);
		PTableDirectory[] tblDir = readTableDirectory(bytes, offSub.tablesNum);
		printTblDir(tblDir);
	}

	private POffsetSubtable readOffsetSubtable(byte[] bytes) {
		POffsetSubtable offSub = new POffsetSubtable();
		offSub.scalerType = read32Move(bytes);
		offSub.tablesNum = read16Move(bytes);
		offSub.searchRange = read16Move(bytes);
		offSub.entrySelector = read16Move(bytes);
		offSub.rangeShift = read16Move(bytes);
		return offSub;

	}

	private PTableDirectory[] readTableDirectory(byte[] bytes, int length) {
		PTableDirectory[] tblDir = new PTableDirectory[length];
		for (int i = 0; i < length; i++) {
			PTableDirectory t = new PTableDirectory();
			int tag = read32Move(bytes);
			t.tag = new byte[]{(byte) ((tag) >>> 24), (byte) ((tag) >>> 16), 
					(byte) ((tag) >>> 8), (byte) (tag)};
			t.checkSum = read32Move(bytes);
			t.offset = read32Move(bytes);
			t.length = read32Move(bytes);
			tblDir[i] = t;
		}
		return tblDir;

	}

	private void printTblDir(PTableDirectory[] tblDir) {
		final int padding = 8;
		System.out.println(PGlobal.padRight("#)", padding)
				+ PGlobal.padRight("tag", padding)
				+ PGlobal.padRight("len", padding)
				+ PGlobal.padRight("offset", padding));
		for (int i = 0; i < tblDir.length; i++) {
			PTableDirectory t = tblDir[i];
			String indexString = String.valueOf(i + 1) + ")";
			System.out.println(PGlobal.padRight(indexString, padding)
					+ PGlobal.padRight(t.tagToString().trim(), padding)
					+ PGlobal.padRight(String.valueOf(t.length).trim(),padding)
					+ PGlobal.padRight(String.valueOf(t.offset).trim(), padding));
		}
	}

	private int read16(byte[] bytes) {
		return ((bytes[head] & 0xFF) << 8) | (bytes[head + 1] & 0xFF);

	}

	private int read32(byte[] bytes) {
		return ((bytes[head] & 0xFF) << 24) | ((bytes[head + 1] & 0xFF) << 16)
				| ((bytes[head + 2] & 0xFF) << 8) | (bytes[head + 3] & 0xFF);

	}

	private int read16Move(byte[] bytes) {
		int out = read16(bytes);
		head += 2;
		return out;

	}

	private int read32Move(byte[] bytes) {
		int out = read32(bytes);
		head += 4;
		return out;

	}
}
