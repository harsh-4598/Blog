package com.project.blog.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationException extends RuntimeException{
    String message;

    public ValidationException(String message) {
        super(message);
        this.message = message;
    }
}
