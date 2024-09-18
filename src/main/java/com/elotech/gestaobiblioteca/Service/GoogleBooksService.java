package com.elotech.gestaobiblioteca.Service;

import com.elotech.gestaobiblioteca.DTO.GoogleBookResponseDto;
import com.elotech.gestaobiblioteca.DTO.ItemDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class GoogleBooksService {

    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=intitle:";

    private final RestTemplate restTemplate;

    public GoogleBooksService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ItemDto> buscarLivrosPorTitulo(String titulo) {
        String url = GOOGLE_BOOKS_API_URL + titulo;
        GoogleBookResponseDto response = restTemplate.getForObject(url, GoogleBookResponseDto.class);
        return Optional.ofNullable(response)
                .map(GoogleBookResponseDto::items)
                .orElse(List.of());
    }
}
