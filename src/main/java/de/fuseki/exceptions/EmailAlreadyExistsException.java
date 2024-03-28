package de.fuseki.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException (String message){
        super(message);
    }
}
