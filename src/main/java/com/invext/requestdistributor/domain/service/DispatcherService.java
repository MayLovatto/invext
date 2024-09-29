package com.invext.requestdistributor.domain.service;

import com.invext.requestdistributor.domain.entity.Request;

public interface DispatcherService {
    void dispatchRequest(Request request);
    void agentCompletedRequest(String agentId);
}
