package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDto(
        VolumeInfoDto volumeInfo
) {
}
