package com.dias.services.mnemo.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchemeDTO {
    private Long id;
    private String title;
    private String schemaType;
    private String createdBy;
    private JsonNode content;
}
