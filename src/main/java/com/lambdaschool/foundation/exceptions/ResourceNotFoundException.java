package com.lambdaschool.foundation.exceptions;


public class ResourceNotFoundException
    extends RuntimeException
{
    public ResourceNotFoundException(String message)
    {
        super(String.format("Error %s",
            message));
    }
}