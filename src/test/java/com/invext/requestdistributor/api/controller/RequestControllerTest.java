package com.invext.requestdistributor.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invext.requestdistributor.api.dto.RequestDTO;
import com.invext.requestdistributor.domain.service.DispatcherService;
import com.invext.requestdistributor.infrastructure.exception.GlobalExceptionHandler;
import com.invext.requestdistributor.infrastructure.exception.AgentNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.doThrow;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DispatcherService dispatcherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void submitRequest_ReturnsSuccess() throws Exception {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setId("R1");
        requestDTO.setSubject("Problemas com cartão");

        mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Request submitted successfully."));
    }

    @Test
    public void submitRequest_InvalidInput_ReturnsBadRequest() throws Exception {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setId("");
        requestDTO.setSubject("");

        MvcResult result = mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("Response: " + result.getResponse().getContentAsString());

        mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value("ID cannot be blank"))
                .andExpect(jsonPath("$.subject").value("Subject cannot be blank"));
    }

    @Test
    public void completeRequest_AgentNotFound_ReturnsNotFound() throws Exception {
        String agentId = "InvalidAgentId";
        doThrow(new AgentNotFoundException("Agent with ID " + agentId + " not found."))
                .when(dispatcherService).agentCompletedRequest(agentId);

        mockMvc.perform(post("/api/requests/complete")
                        .param("agentId", agentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Agent with ID " + agentId + " not found."));
    }

    @Test
    public void completeRequest_ReturnsSuccess() throws Exception {
        String agentId = "A1";

        mockMvc.perform(post("/api/requests/complete")
                        .param("agentId", agentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Agent " + agentId + " has completed a request."));
    }
}
