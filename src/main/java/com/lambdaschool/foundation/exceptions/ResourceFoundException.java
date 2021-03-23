package com.lambdaschool.foundation.exceptions;


public class ResourceFoundException
    extends RuntimeException
{
    public ResourceFoundException(String message)
    {
        super(String.format("Error %s",
            message));
        ;
    }
}