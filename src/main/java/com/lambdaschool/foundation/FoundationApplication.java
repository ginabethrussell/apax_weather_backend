package com.lambdaschool.foundation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
@PropertySource(value = "file:/Users/ginabethrussell/apaxweather.properties", ignoreResourceNotFound = true)
public class FoundationApplication
{

    @Autowired
    private static Environment env;

    private static boolean stop = false;

    private static void checkEnvironmentVariable(String envvar)
    {
        if (System.getenv(envvar) == null)
        {
            stop = true;
        }
    }

    public static void main(String[] args)
    {
        // Check to see if the environment variables exists. If they do not, stop execution of application.
        checkEnvironmentVariable("OAUTHCLIENTID");
        checkEnvironmentVariable("OAUTHCLIENTSECRET");

        if (!stop)
        {
            SpringApplication.run(FoundationApplication.class,
                args);
        } else
        {
            System.out.println("Environment Variables NOT SET: OAUTHCLIENTID and / or OAUTHCLIENTSECRET");
        }
    }
}
