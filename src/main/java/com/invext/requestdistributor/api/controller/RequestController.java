package com.invext.requestdistributor.api.controller;

import com.invext.requestdistributor.api.dto.RequestDTO;
import com.invext.requestdistributor.api.dto.ResponseDTO;
import com.invext.requestdistributor.domain.entity.Request;
import com.invext.requestdistributor.domain.service.DispatcherService;
import com.invext.requestdistributor.util.mapper.RequestMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private DispatcherService dispatcherService;

    @PostMapping
    public ResponseEntity<ResponseDTO> submitRequest(@RequestBody @Valid RequestDTO requestDTO) {
        Request request = RequestMapper.toRequest(requestDTO);
        dispatcherService.dispatchRequest(request);
        return ResponseEntity.ok(new ResponseDTO("Request submitted successfully."));
    }

    @PostMapping("/complete")
    public ResponseEntity<ResponseDTO> completeRequest(@RequestParam String agentId) {
        dispatcherService.agentCompletedRequest(agentId);
        return ResponseEntity.ok(new ResponseDTO("Agent " + agentId + " has completed a request."));
    }
}
