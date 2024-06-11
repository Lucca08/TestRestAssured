
package com.example.testRestAssured.contrato;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.testRestAssured.Model.Login;
import com.example.testRestAssured.Model.Usuario;
import com.example.testRestAssured.util.BaseTeste;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class TesteContratoUsuario extends BaseTeste {

    private static String tokenAdmin;
    private static final String ENDPOINT_LOGIN = "/auth/login";
    private static final String ENDPOINT_CREATE_USER = "/usuario";
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    
     @BeforeAll
    @DisplayName("Deve fazer login como administrador")
    public static void beforeAll() {
        String loginPayload = "{ \"email\": \"" + ADMIN_EMAIL + "\", \"senha\": \"" + ADMIN_PASSWORD + "\" }";

        tokenAdmin = given()
            .body(loginPayload)
            .contentType(ContentType.JSON)
            .when()
            .post(ENDPOINT_LOGIN)
            .then()
            .statusCode(200)
            .extract()
            .path("token");
    }


    @Test
    @DisplayName("Deve retornar 201 ao criar um novo usuário e validar os campos da resposta")
    public void deveRetornar201AoCriarUmUsuario() {

        Login autenticacaoDados = Login.builder()
            .email("novousuarioDacasa20@email.com")
            .senha("senha123")
            .build();
    
        Usuario novoUsuario = Usuario.builder()
            .autenticacaoDto(autenticacaoDados)
            .nome("Lucca")
            .sobrenome("Garcia")
            .cpf("89304303112")
            .admin(true)
            .build();
    

        Response response = given()
            .header(HEADER_AUTHORIZATION, tokenAdmin)
            .body(novoUsuario)
            .contentType(ContentType.JSON)
        .when()
            .post(ENDPOINT_CREATE_USER);

            response.then()
            .log().all() 
            .statusCode(201)
            .body(matchesJsonSchemaInClasspath("schemas/schema-usuario.json"))
            .body("id", notNullValue()) 
            .body("email", equalTo("novousuarioDacasa20@email.com")) 
            .body("nome", equalTo("Lucca")) 
            .body("sobrenome", equalTo("Garcia")) 
            .body("cpf", equalTo("89304303112")) 
            .body("admin", equalTo(true)); 
       
    }
}
