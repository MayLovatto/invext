package com.invext.requestdistributor.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Agent {
    private String id;
    private String name;
    private Team team;
    private int currentLoad = 0;
    private static final int MAX_LOAD = 3;

    public Agent(String id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public boolean isAvailable() {
        return currentLoad < MAX_LOAD;
    }

    public void incrementLoad() {
        if (isAvailable()) {
            currentLoad++;
        }
    }

    public void decrementLoad() {
        if (currentLoad > 0) {
            currentLoad--;
        }
    }
}
