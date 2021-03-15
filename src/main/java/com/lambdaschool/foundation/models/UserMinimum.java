package com.lambdaschool.foundation.models;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserMinimum
{

    @NotNull
    @Column(nullable = false)
    private String username;

    @NotNull
    private String password;

    @Email
    @NotNull
    private String primaryemail;


    public String getUsername()
    {
        return username;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public String getPassword()
    {
        return password;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public String getPrimaryemail()
    {
        return primaryemail;
    }


    public void setPrimaryemail(String primaryemail)
    {
        this.primaryemail = primaryemail;
    }
}
