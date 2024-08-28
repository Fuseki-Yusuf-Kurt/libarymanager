package de.fuseki.exceptions;

import org.springframework.expression.AccessException;

public class NoAccesException extends NullPointerException {
    public NoAccesException(String message) {super(message);}
}
