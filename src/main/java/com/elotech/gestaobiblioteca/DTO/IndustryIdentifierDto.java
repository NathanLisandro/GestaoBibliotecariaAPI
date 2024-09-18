package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IndustryIdentifierDto(
        String type,
        String identifier
) {
}
