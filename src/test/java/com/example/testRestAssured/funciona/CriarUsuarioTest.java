package com.example.testRestAssured.funciona;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.testRestAssured.Model.AbrirSessao;
import com.example.testRestAssured.Model.Login;
import com.example.testRestAssured.Model.Pauta;
import com.example.testRestAssured.Model.Usuario;
import com.example.testRestAssured.Model.VotoExterno;
import com.example.testRestAssured.Model.VotoInterno;
import com.example.testRestAssured.Model.Enum.TipoDeVoto;
import com.example.testRestAssured.util.BaseTeste;

import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class CriarUsuarioTest extends BaseTeste {

    private static String tokenAdmin;
    private static final String ENDPOINT_LOGIN = "/auth/login";
    private static final String ENDPOINT_CREATE_USER = "/usuario";
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String ENDPOINT_CREATE_PAUTA = "/pauta";
    private static final String ENDPOINT_ABRIR_SESSAO_VOTACAO = "votacao/abrir";
    private static final String ENDPOINT_VOTO_INTERNO = "votacao/votoInterno";
    private static final String ENDPOINT_VOTO_EXTERNO = "votacao/votoExterno";


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
    @DisplayName("Deve fazer login e validar os campos da resposta")
    public void deveFazerLoginEValidarCamposDaResposta() {
        String loginEmail = "novousuario07@email.com";
        String loginSenha = "senha123";

        String loginPayload = "{ \"email\": \"" + loginEmail + "\", \"senha\": \"" + loginSenha + "\" }";

        Response response = given()
            .body(loginPayload)
            .contentType(ContentType.JSON)
            .when()
            .post(ENDPOINT_LOGIN);

        System.out.println("Corpo da Resposta: " + response.getBody().asString());

        response.then()
            .log().all()
            .statusCode(200)
            .body("token", notNullValue())  
            .body("admin", equalTo(true)); 
    }


    @Test
    @DisplayName("Deve criar um novo usuário e validar os campos da resposta")
    public void deveRetornar201aoCriarUmUsuarioAdmin() {
        Login autenticacaoDados = Login.builder()
            .email("novousuario07@email.com")
            .senha("senha123")
            .build();
    
        Usuario novoUsuario = Usuario.builder()
            .autenticacaoDto(autenticacaoDados)
            .nome("Novo")
            .sobrenome("Usuario")
            .cpf("89304303207")
            .admin(true)
            .build();
    
        System.out.println("Corpo da Solicitação: " + novoUsuario.toString()); 
    
        Response response = given()
            .header(HEADER_AUTHORIZATION, tokenAdmin)
            .body(novoUsuario)
            .contentType(ContentType.JSON)
        .when()
            .post(ENDPOINT_CREATE_USER);
    
        System.out.println("Corpo da Resposta: " + response.getBody().asString());
    
        response.then()
            .log().all() 
            .statusCode(201)
            .body("id", notNullValue()) 
            .body("email", equalTo("novousuario07@email.com")) 
            .body("nome", equalTo("Novo")) 
            .body("sobrenome", equalTo("Usuario")) 
            .body("cpf", equalTo("89304303207")) 
            .body("admin", equalTo(true)); 
    }

 

    @Test
    @DisplayName("Deve criar um novo usuário e validar os campos da resposta")
    public void deveRetornar201aoCriarUmUsuarioNormal() {
        Login autenticacaoDados = Login.builder()
            .email("normal03@email.com")
            .senha("senha123")
            .build();
    
        Usuario novoUsuario = Usuario.builder()
            .autenticacaoDto(autenticacaoDados)
            .nome("usuario")
            .sobrenome("padrao")
            .cpf("89304303213")
            .admin(false)
            .build();
    
        System.out.println("Corpo da Solicitação: " + novoUsuario.toString()); 
    
        Response response = given()
            .header(HEADER_AUTHORIZATION, tokenAdmin)
            .body(novoUsuario)
            .contentType(ContentType.JSON)
        .when()
            .post(ENDPOINT_CREATE_USER);
    
        System.out.println("Corpo da Resposta: " + response.getBody().asString());
    
        response.then()
            .log().all() 
            .statusCode(201)
            .body("id", notNullValue()) 
            .body("email", equalTo("normal03@email.com")) 
            .body("nome", equalTo("usuario")) 
            .body("sobrenome", equalTo("padrao")) 
            .body("cpf", equalTo("89304303213")) 
            .body("admin", equalTo(false)); 
    }

    @Test
    @DisplayName("Deve criar uma pauta e validar os campos da resposta")
    public void deveRetornar201aoCriarUmaPauta() {
        Pauta novaPauta = Pauta.builder()
            .assunto("Assunto da Pauta2")
            .categoria("TRANSPORTE")
            .build();

        System.out.println("Corpo da Solicitação: " + novaPauta.toString());

        Response response = given()
            .header(HEADER_AUTHORIZATION, tokenAdmin)
            .body(novaPauta)
            .contentType(ContentType.JSON)
        .when() 
            .post(ENDPOINT_CREATE_PAUTA);

        System.out.println("Corpo da Resposta: " + response.getBody().asString());

        response.then()
            .log().all()
            .statusCode(201)
            .body("id", notNullValue())
            .body("assunto", equalTo("Assunto da Pauta2"))
            .body("categoria", equalTo("TRANSPORTE"));
    }

    @Test
    @DisplayName("Deve abrir a votação de uma pauta e validar os campos da resposta")
    public void deveRetornar200aoAbrirVotacaoDeUmaPauta() {
        AbrirSessao abrirSessao = AbrirSessao.builder()
            .minutos(1L)
            .pautaId(10)
            .build();

        System.out.println("Corpo da Solicitação: " + abrirSessao.toString());

        Response response = given()
            .header(HEADER_AUTHORIZATION, tokenAdmin)
            .body(abrirSessao)
            .contentType(ContentType.JSON)

        .when()
            .post(ENDPOINT_ABRIR_SESSAO_VOTACAO);


        System.out.println("Corpo da Resposta: " + response.getBody().asString());

        response.then()
            .log().all()
            .statusCode(200)
            .body("id", notNullValue())
            .body("pautaId", equalTo(10))
            .body("votosPositivos", equalTo(0))
            .body("votosNegativos", equalTo(0))
            .body("dataAbertura", notNullValue())
            .body("dataFechamento", notNullValue())
            .body("sessaoAtiva", equalTo(true));
        }

        @Test
        @DisplayName("Deve votar positivamente na pauta voto interno")
        public void deveRetornar200aoFazerVotoInternoPositivo() {
            VotoInterno votoInterno = VotoInterno.builder()
                .pautaId(10)
                .tipoDeVoto(TipoDeVoto.VOTO_POSITIVO)
                .build();
    
            System.out.println("Corpo da Solicitação: " + votoInterno.toString());
    
            Response response = given()
                .header(HEADER_AUTHORIZATION, tokenAdmin)
                .body(votoInterno)
                .contentType(ContentType.JSON)
            .when()
                .patch(ENDPOINT_VOTO_INTERNO);
    
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Corpo da Resposta: " + response.getBody().asString());
    
            response.then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .body("pautaId", equalTo(1))
                .body("votosPositivos", equalTo(1))
                .body("votosNegativos", equalTo(0))
                .body("dataAbertura", notNullValue())
                .body("dataFechamento", notNullValue())
                .body("sessaoAtiva", equalTo(true));
        }


        @Test
        @DisplayName("Deve votar negativamente na pauta voto interno")
        public void deveRetornar200aoFazerVotoInternoNegativo() {
            VotoInterno votoInterno = VotoInterno.builder()
                .pautaId(10)
                .tipoDeVoto(TipoDeVoto.VOTO_NEGATIVO)
                .build();
    
            System.out.println("Corpo da Solicitação: " + votoInterno.toString());
    
            Response response = given()
                .header(HEADER_AUTHORIZATION, tokenAdmin)
                .body(votoInterno)
                .contentType(ContentType.JSON)
            .when()
                .patch(ENDPOINT_VOTO_INTERNO);
    
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Corpo da Resposta: " + response.getBody().asString());
    
            response.then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .body("pautaId", equalTo(1))
                .body("votosPositivos", equalTo(1))
                .body("votosNegativos", equalTo(1))
                .body("dataAbertura", notNullValue())
                .body("dataFechamento", notNullValue())
                .body("sessaoAtiva", equalTo(true));
    }

    @Test
    @DisplayName("Deve votar positivamente na pauta voto externo")
    public void deveRetornar200aoFazerVotoExternoPositivo() {
        VotoExterno votoExterno = VotoExterno.builder()
            .pautaId(10)
            .tipoDeVoto(TipoDeVoto.VOTO_POSITIVO)
            .cpf("89304303207")
            .senha("senha123")
            .build();

        System.out.println("Corpo da Solicitação: " + votoExterno.toString());

        Response response = given()
            .header(HEADER_AUTHORIZATION, tokenAdmin)
            .body(votoExterno)
            .contentType(ContentType.JSON)
        .when()
            .patch(ENDPOINT_VOTO_EXTERNO);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Corpo da Resposta: " + response.getBody().asString());

        response.then()
            .log().all()
            .statusCode(200)
            .body("id", notNullValue())
            .body("pautaId", equalTo(1))
            .body("votosPositivos", equalTo(1))
            .body("votosNegativos", equalTo(0))
            .body("dataAbertura", notNullValue())
            .body("dataFechamento", notNullValue())
            .body("sessaoAtiva", equalTo(true));
    }

}