package br.com.fiap.theMovie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI OpenApiConfiguration() {
        return new OpenAPI()
                .info(new Info()
                        .title("TheMovie - API")
                        .version("0.1.0")
                        .description("O Sistema de Gerenciamento de Filmes é uma aplicação web desenvolvida em Java com Spring Boot.\n" +
                                "\t\tEle permite que usuários se cadastrem, façam login e gerenciem um catálogo de filmes.\n" +
                                "\t\tFuncionalidades principais:\n" +
                                "\t\t\tCadastro de Usuários: Registro com e-mail e senha. Login: Autenticação segura para acesso à plataforma.\n" +
                                "\t\t\tGerenciamento de Filmes: Adição de novos filmes com detalhes como nome e descrição. Listagem de filmes cadastrados.\n" +
                                "\t\t\tAtualização e exclusão de filmes.")
                        .contact(new Contact()
                                .name("GitHub TheMovie")
                                .url("https://github.com/RodrigoBatis/API-TheMovie"))
                        .license(new License()));
    }

}
