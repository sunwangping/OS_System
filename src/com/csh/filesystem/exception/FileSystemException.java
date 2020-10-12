package com.csh.filesystem.exception;

public class FileSystemException extends RuntimeException {

	public FileSystemException() {
		super();
	}

	public FileSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileSystemException(String message) {
		super(message);
	}

	public FileSystemException(Throwable cause) {
		super(cause);
	}

}
