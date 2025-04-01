package com.ang.Primeval.Util.Loaders.Ttf;

import com.ang.Primeval.Util.Global;

public class TtfParser {
	private String path;
	private int head;

	public TtfParser(String path) {
		this.path = path;
	}

	public void readTtl(byte[] bytes) {
		head = 0;
		readFontDirectory(bytes);
	}
	
	private void readFontDirectory(byte[] bytes) {
		OffsetSubtable offSub = readOffsetSubtable(bytes);
		TableDirectory[] tblDir = readTableDirectory(bytes, offSub.tablesNum);
		printTblDir(tblDir);
	}

	private OffsetSubtable readOffsetSubtable(byte[] bytes) {
		OffsetSubtable offSub = new OffsetSubtable();
		offSub.scalerType = read32Move(bytes);
		offSub.tablesNum = read16Move(bytes);
		offSub.searchRange = read16Move(bytes);
		offSub.entrySelector = read16Move(bytes);
		offSub.rangeShift = read16Move(bytes);
		return offSub;

	}

	private TableDirectory[] readTableDirectory(byte[] bytes, int length) {
		TableDirectory[] tblDir = new TableDirectory[length];
		for (int i = 0; i < length; i++) {
			TableDirectory t = new TableDirectory();
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

	private void printTblDir(TableDirectory[] tblDir) {
		final int padding = 8;
		System.out.println(Global.padRight("#)", padding)
				+ Global.padRight("tag", padding)
				+ Global.padRight("len", padding)
				+ Global.padRight("offset", padding));
		for (int i = 0; i < tblDir.length; i++) {
			TableDirectory t = tblDir[i];
			String indexString = String.valueOf(i + 1) + ")";
			System.out.println(Global.padRight(indexString, padding)
					+ Global.padRight(t.tagToString().trim(), padding)
					+ Global.padRight(String.valueOf(t.length).trim(),padding)
					+ Global.padRight(String.valueOf(t.offset).trim(), padding));
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
