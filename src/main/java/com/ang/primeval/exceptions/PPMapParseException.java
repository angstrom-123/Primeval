package com.ang.primeval.exceptions;

public class PPMapParseException extends Exception {
	public PPMapParseException() {}

	public PPMapParseException(String fileName, int lineNumber, int subLineNumber) {
		super("Syntax error at " + fileName + ":" + lineNumber + ":" + subLineNumber);
	}

	public PPMapParseException(String fileName, int lineNumber) {
		super("Syntax error at " + fileName + ":" + lineNumber);
	}

	public PPMapParseException(String message) {
		super(message);	
	}
}
