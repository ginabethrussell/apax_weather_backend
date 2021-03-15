package com.lambdaschool.foundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "locations")
public class Location extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long locationid;

    @NotNull
    @Column(nullable = false)
    private String zipcode;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = "locations",
        allowSetters = true)
    private User user;

    public Location()
    {
    }

    public Location(
        @NotNull String zipcode,
        @NotNull User user)
    {
        this.zipcode = zipcode;
        this.user = user;
    }


    public long getLocationid()
    {
        return locationid;
    }

    public void setLocationid(long locationid)
    {
        this.locationid = locationid;
    }


    public String getZipcode()
    {
        return zipcode;
    }

    public void setZipcode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
