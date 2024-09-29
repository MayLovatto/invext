package com.invext.requestdistributor.domain.service.impl;

import com.invext.requestdistributor.domain.entity.Agent;
import com.invext.requestdistributor.domain.entity.Request;
import com.invext.requestdistributor.domain.entity.Team;
import com.invext.requestdistributor.domain.service.DispatcherService;
import com.invext.requestdistributor.infrastructure.exception.AgentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DispatcherServiceImpl implements DispatcherService {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServiceImpl.class);

    private Map<Team, Queue<Request>> requestQueues = new ConcurrentHashMap<>();
    private Map<Team, List<Agent>> teamAgents = new ConcurrentHashMap<>();

    public DispatcherServiceImpl() {
        initializeTeams();
        initializeAgents();
    }

    private void initializeTeams() {
        for (Team team : Team.values()) {
            requestQueues.put(team, new ConcurrentLinkedQueue<>());
            teamAgents.put(team, Collections.synchronizedList(new ArrayList<>()));
        }
    }

    private void initializeAgents() {
        teamAgents.get(Team.CARDS).add(new Agent("A1", "Alice", Team.CARDS));
        teamAgents.get(Team.CARDS).add(new Agent("A2", "Alex", Team.CARDS));

        teamAgents.get(Team.LOANS).add(new Agent("B1", "Bob", Team.LOANS));
        teamAgents.get(Team.LOANS).add(new Agent("B2", "Bella", Team.LOANS));

        teamAgents.get(Team.OTHERS).add(new Agent("C1", "Charlie", Team.OTHERS));
        teamAgents.get(Team.OTHERS).add(new Agent("C2", "Cynthia", Team.OTHERS));
    }

    @Override
    public void dispatchRequest(Request request) {
        Team team = request.getRequiredTeam();
        List<Agent> agents = teamAgents.get(team);

        synchronized (agents) {
            Agent availableAgent = agents.stream()
                    .filter(Agent::isAvailable)
                    .findFirst()
                    .orElse(null);

            if (availableAgent != null) {
                assignRequestToAgent(request, availableAgent);
            } else {
                enqueueRequest(request);
            }
        }
    }

    private void assignRequestToAgent(Request request, Agent agent) {
        agent.incrementLoad();
        logger.info("Assigned Request {} to Agent {}", request.getId(), agent.getName());

    }

    private void enqueueRequest(Request request) {
        Queue<Request> queue = requestQueues.get(request.getRequiredTeam());
        queue.add(request);
        logger.info("Enqueued Request {} for Team {}", request.getId(), request.getRequiredTeam());
    }

    @Override
    public void agentCompletedRequest(String agentId) {
        Agent agent = findAgentById(agentId);
        if (agent != null) {
            agent.decrementLoad();
            logger.info("Agent {} completed a request. Current load: {}", agent.getName(), agent.getCurrentLoad());
            assignNextRequest(agent);
        } else {
            throw new AgentNotFoundException("Agent with ID " + agentId + " not found.");
        }
    }

    private Agent findAgentById(String agentId) {
        for (List<Agent> agents : teamAgents.values()) {
            synchronized (agents) {
                for (Agent agent : agents) {
                    if (agent.getId().equals(agentId)) {
                        return agent;
                    }
                }
            }
        }
        return null;
    }

    private void assignNextRequest(Agent agent) {
        Queue<Request> queue = requestQueues.get(agent.getTeam());
        Request nextRequest = queue.poll();
        if (nextRequest != null) {
            assignRequestToAgent(nextRequest, agent);
        }
    }

    public Map<Team, List<Agent>> getTeamAgents() {
        return teamAgents;
    }

    public Map<Team, Queue<Request>> getRequestQueues() {
        return requestQueues;
    }
}
