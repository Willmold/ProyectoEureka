package com.example.ApplicationRest.JsonModels.Eureka.InstanceClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Port {
    private int $;

    public int get$() {
        return $;
    }

    public void set$(int $) {
        this.$ = $;
    }
}
