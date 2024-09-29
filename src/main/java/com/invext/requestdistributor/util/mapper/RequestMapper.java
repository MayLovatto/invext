package com.invext.requestdistributor.util.mapper;

import com.invext.requestdistributor.api.dto.RequestDTO;
import com.invext.requestdistributor.domain.entity.Request;

public class RequestMapper {

    public static Request toRequest(RequestDTO dto) {
        return new Request(dto.getId(), dto.getSubject());
    }

    public static RequestDTO toRequestDTO(Request request) {
        RequestDTO dto = new RequestDTO();
        dto.setId(request.getId());
        dto.setSubject(request.getSubject());
        return dto;
    }
}
