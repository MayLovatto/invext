package com.invext.requestdistributor.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {

    @NotBlank(message = "ID cannot be blank")
    private String id;

    @NotBlank(message = "Subject cannot be blank")
    private String subject;
}
