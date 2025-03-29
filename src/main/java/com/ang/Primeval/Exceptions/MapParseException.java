package com.ang.Primeval.Exceptions;

public class MapParseException extends Exception {
	public MapParseException() {}

	public MapParseException(String fileName, int lineNumber, int subLineNumber) {
		super("Syntax error at " + fileName + ":" + lineNumber + ":" + subLineNumber);
		super.printStackTrace();
	}

	public MapParseException(String fileName, int lineNumber) {
		super("Syntax error at " + fileName + ":" + lineNumber);
		super.printStackTrace();
	}

	public MapParseException(String message) {
		super(message);	
		super.printStackTrace();
	}
}
