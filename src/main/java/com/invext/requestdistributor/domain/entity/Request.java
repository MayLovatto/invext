package com.invext.requestdistributor.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private String id;
    private String subject;
    private Team requiredTeam;

    public Request(String id, String subject) {
        this.id = id;
        this.subject = subject;
        this.requiredTeam = determineTeam(subject);
    }

    private Team determineTeam(String subject) {
        if ("Problemas com cartão".equalsIgnoreCase(subject)) {
            return Team.CARDS;
        } else if ("contratação de empréstimo".equalsIgnoreCase(subject)) {
            return Team.LOANS;
        } else {
            return Team.OTHERS;
        }
    }
}
