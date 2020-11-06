package com.pidev.minesweeperapi.exception;

/**
 * Exception that represents an attempt to find an invalid cell from a map.
 */
public class InvalidCellException extends Exception {

    public InvalidCellException(String message) {
        super(message);
    }
}
