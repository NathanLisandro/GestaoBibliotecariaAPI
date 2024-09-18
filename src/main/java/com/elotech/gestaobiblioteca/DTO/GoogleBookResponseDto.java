package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleBookResponseDto(
        List<ItemDto> items
) {
}
