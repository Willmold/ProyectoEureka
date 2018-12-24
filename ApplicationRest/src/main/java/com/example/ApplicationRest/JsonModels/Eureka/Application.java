package com.example.ApplicationRest.JsonModels.Eureka;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private String name;
    private List<Instance> instance = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Instance> getInstance() {
        return instance;
    }

    public void setInstance(List<Instance> instance) {
        this.instance = instance;
    }
}
