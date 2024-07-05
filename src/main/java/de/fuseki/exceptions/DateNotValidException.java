package de.fuseki.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DateNotValidException extends RuntimeException{
    public DateNotValidException(String message){
        super(message);
    }
}
