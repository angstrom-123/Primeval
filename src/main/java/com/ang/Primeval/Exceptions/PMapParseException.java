package com.ang.Primeval.Exceptions;

public class PMapParseException extends Exception {
	public PMapParseException() {}

	public PMapParseException(String fileName, int lineNumber, int subLineNumber) {
		super("Syntax error at " + fileName + ":" + lineNumber + ":" + subLineNumber);
	}

	public PMapParseException(String fileName, int lineNumber) {
		super("Syntax error at " + fileName + ":" + lineNumber);
	}

	public PMapParseException(String message) {
		super(message);	
	}
}
