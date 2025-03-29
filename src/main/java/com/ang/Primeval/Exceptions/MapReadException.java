package com.ang.Primeval.Exceptions;

public class MapReadException extends Exception {
	public MapReadException(String message) {
		super(message);	
		super.printStackTrace();
	}
}
