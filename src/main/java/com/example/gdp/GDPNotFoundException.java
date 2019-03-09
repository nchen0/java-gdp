package com.example.gdp;

public class GDPNotFoundException extends RuntimeException{
    public GDPNotFoundException (Long id) {
        super("Could not find country");
    }
}
