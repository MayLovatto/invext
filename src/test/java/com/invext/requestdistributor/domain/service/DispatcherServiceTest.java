package com.invext.requestdistributor.domain.service;

import com.invext.requestdistributor.domain.entity.Agent;
import com.invext.requestdistributor.domain.entity.Request;
import com.invext.requestdistributor.domain.entity.Team;
import com.invext.requestdistributor.domain.service.impl.DispatcherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class DispatcherServiceTest {

    private DispatcherServiceImpl dispatcherService;

    @BeforeEach
    public void setUp() {
        dispatcherService = new DispatcherServiceImpl();
    }

    @Test
    public void testDispatchRequest_AssignsToAvailableAgent() {
        // Arrange
        Request request = new Request("R1", "Problemas com cartão");
        Team team = request.getRequiredTeam();
        List<Agent> agents = dispatcherService.getTeamAgents().get(team);

        // Ensure all agents are available
        agents.forEach(agent -> assertTrue(agent.isAvailable()));

        // Act
        dispatcherService.dispatchRequest(request);

        // Assert
        // Find the agent who received the request
        Agent assignedAgent = agents.stream()
                .filter(agent -> agent.getCurrentLoad() == 1)
                .findFirst()
                .orElse(null);

        assertNotNull(assignedAgent, "Request should be assigned to an available agent");
        assertEquals(1, assignedAgent.getCurrentLoad(), "Agent's load should have increased to 1");
    }

    @Test
    public void testDispatchRequest_EnqueuesWhenNoAgentsAvailable() {
        // Arrange
        Request request1 = new Request("R1", "Problemas com cartão");
        Request request2 = new Request("R2", "Problemas com cartão");
        Request request3 = new Request("R3", "Problemas com cartão");
        Request request4 = new Request("R4", "Problemas com cartão"); // This should be enqueued

        Team team = request1.getRequiredTeam();
        List<Agent> agents = dispatcherService.getTeamAgents().get(team);

        // Manually set agents to max load
        agents.forEach(agent -> agent.setCurrentLoad(agent.MAX_LOAD));

        // Act
        dispatcherService.dispatchRequest(request4);

        // Assert
        Queue<Request> queue = dispatcherService.getRequestQueues().get(team);
        assertEquals(1, queue.size(), "Request should be enqueued when no agents are available");
        assertEquals("R4", queue.peek().getId(), "Enqueued request should be R4");
    }

    @Test
    public void testAgentCompletedRequest_AssignsNextRequest() {
        // Arrange
        Team team = Team.CARDS;
        List<Agent> agents = dispatcherService.getTeamAgents().get(team);
        Agent agent = agents.get(0);

        // Simulate agent at max load
        agent.setCurrentLoad(Agent.MAX_LOAD);

        // Simulate other agents also at max load
        agents.forEach(a -> a.setCurrentLoad(Agent.MAX_LOAD));

        // Enqueue a request
        Request queuedRequest = new Request("R5", "Problemas com cartão");
        dispatcherService.dispatchRequest(queuedRequest);


        // Verify the request is in the queue
        Queue<Request> queue = dispatcherService.getRequestQueues().get(team);
        assertEquals(1, queue.size(), "Request queue should have one request");

        // Act
        dispatcherService.agentCompletedRequest(agent.getId());

        // Assert
        assertEquals(Agent.MAX_LOAD, agent.getCurrentLoad(),
                "Agent's load should remain the same after completing a request and taking a new one");

        // Verify the queue is empty
        assertEquals(0, queue.size(), "Request queue should be empty after assigning to agent");

    }
}
