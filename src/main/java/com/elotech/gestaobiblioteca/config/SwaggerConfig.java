package com.elotech.gestaobiblioteca.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API de Gestão de Biblioteca")
                        .version("1.0")
                        .description("API para gestão de biblioteca, incluindo funcionalidades de cadastro, empréstimos e recomendações de livros.")
                        .contact(new Contact().name("Nathan Lisandro Toppa").email("nathanlisandro@outlook.com")))
                .components(new Components()
                        .addSchemas("Erro400", new Schema<>()
                                .type("object")
                                .description("Erro de validação de parâmetros")
                                .addProperty("dataHora", new StringSchema().format("date-time").example("2024-09-16T15:57:14.602Z"))
                                .addProperty("codigoStatus", new Schema<Integer>().type("integer").example(400))
                                .addProperty("textoStatus", new StringSchema().example("Bad Request"))
                                .addProperty("mensagem", new StringSchema().example("Dados inválidos fornecidos"))
                                .addProperty("detalhes", new ArraySchema().items(new StringSchema()).example("[\"[Campo] não pode ser nulo.\"]")))
                        .addSchemas("Erro404", new Schema<>()
                                .type("object")
                                .description("Recurso não encontrado")
                                .addProperty("dataHora", new StringSchema().format("date-time").example("2024-09-16T15:57:14.603Z"))
                                .addProperty("codigoStatus", new Schema<Integer>().type("integer").example(404))
                                .addProperty("textoStatus", new StringSchema().example("Not Found"))
                                .addProperty("mensagem", new StringSchema().example("Recurso não encontrado"))
                                .addProperty("detalhes", new ArraySchema().items(new StringSchema()).example("[\"Item com ID 1 não foi encontrado.\"]")))
                        .addSchemas("Erro409", new Schema<>()
                                .type("object")
                                .description("Violação de integridade de dados")
                                .addProperty("dataHora", new StringSchema().format("date-time").example("2024-09-16T15:57:14.604Z"))
                                .addProperty("codigoStatus", new Schema<Integer>().type("integer").example(409))
                                .addProperty("textoStatus", new StringSchema().example("Conflict"))
                                .addProperty("mensagem", new StringSchema().example("Violação de integridade de dados"))
                                .addProperty("detalhes", new ArraySchema().items(new StringSchema()).example("[\" Esse item já existe no banco de dados \"]")))
                        .addSchemas("Erro500", new Schema<>()
                                .type("object")
                                .description("Erro interno do servidor")
                                .addProperty("dataHora", new StringSchema().format("date-time").example("2024-09-16T15:57:14.605Z"))
                                .addProperty("codigoStatus", new Schema<Integer>().type("integer").example(500))
                                .addProperty("textoStatus", new StringSchema().example("Internal Server Error"))
                                .addProperty("mensagem", new StringSchema().example("Erro interno do servidor"))
                                .addProperty("detalhes", new ArraySchema().items(new StringSchema()).example("[\"Erro inesperado.\"]")))
                );
    }
}
