package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VolumeInfoDto(
        String title,
        List<String> authors,
        String publishedDate,
        List<IndustryIdentifierDto> industryIdentifiers,
        List<String> categories,
        String description
) {
}