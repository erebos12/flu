package com.erebos.flu.exceptions;

public class DateParsingException extends RuntimeException {
    public DateParsingException(final String errorMessage) {
        super(errorMessage);
    }
}
