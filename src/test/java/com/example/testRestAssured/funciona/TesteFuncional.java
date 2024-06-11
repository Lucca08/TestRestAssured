package com.example.testRestAssured.funciona;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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



public class TesteFuncional extends BaseTeste {

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
    @DisplayName("Deve retornar 401 ao tentar fazer login com credenciais inválidas")
    public void deveRetornar401AoFazerLoginComCredenciaisInvalidas() {
        String loginPayload = "{ \"email\": \"invalido@email.com\", \"senha\": \"senhaerrada\" }";

        Response response = given()
            .body(loginPayload)
            .contentType(ContentType.JSON)
        .when()
            .post(ENDPOINT_LOGIN);

        System.out.println("Corpo da Resposta: " + response.getBody().asString());

        response.then()
            .log().all()
            .statusCode(401);
    }


    @Test
    @DisplayName("Deve criar um novo usuário e validar os campos da resposta")
    public void deveRetornar201aoCriarUmUsuarioAdmin() {
        Login autenticacaoDados = Login.builder()
            .email("novousuario02@email.com")
            .senha("senha123")
            .build();
    
        Usuario novoUsuario = Usuario.builder()
            .autenticacaoDto(autenticacaoDados)
            .nome("Novo")
            .sobrenome("Usuario")
            .cpf("89304303333")
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
            .body("email", equalTo("novousuario02@email.com")) 
            .body("nome", equalTo("Novo")) 
            .body("sobrenome", equalTo("Usuario")) 
            .body("cpf", equalTo("89304303333")) 
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
            .cpf("89304303223")
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
            .body("cpf", equalTo("89304303223")) 
            .body("admin", equalTo(false)); 
    }

    @Test
    @DisplayName("Deve fazer login e validar os campos da resposta")
    public void DeveRetornar200AoFazerLoginEValidarCamposDaResposta() {
        String loginEmail = "novousuario02@email.com";
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
    @DisplayName("Deve retornar 400 ao tentar criar um usuário com campos obrigatórios ausentes")
    public void deveRetornar400AoCriarUsuarioComCamposAusentes() {
        Login autenticacaoDados = Login.builder()
            .email("usuariosemnome4@email.com")
            .senha("senha123")
            .build();

        Usuario novoUsuario = Usuario.builder()
            .autenticacaoDto(autenticacaoDados)
            .cpf("89304303216")
            .admin(false)
            .sobrenome("Sobrenome")
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
            .statusCode(400)
            .body("erro", containsString("Nome deve ser informado."));
    }


    @ParameterizedTest
    @MethodSource("com.example.testRestAssured.Model.Dados.DadosParaTeste#camposAusentes")
    @DisplayName("Deve retornar 400 ao tentar criar um usuário com campos obrigatórios ausentes")
    public void deveRetornar400AoCriarUsuarioComCamposAusentes(Map<String, Object> dadosParaTeste) {
        Login.LoginBuilder loginBuilder = Login.builder();
        if (dadosParaTeste.containsKey("email")) {
            loginBuilder.email((String) dadosParaTeste.get("email"));
        }
        if (dadosParaTeste.containsKey("senha")) {
            loginBuilder.senha((String) dadosParaTeste.get("senha"));
        }
        Login autenticacaoDados = loginBuilder.build();

        Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder()
            .autenticacaoDto(autenticacaoDados)
            .admin((Boolean) dadosParaTeste.get("admin"));

        if (dadosParaTeste.containsKey("cpf")) {
            usuarioBuilder.cpf((String) dadosParaTeste.get("cpf"));
        }
        if (dadosParaTeste.containsKey("nome")) {
            usuarioBuilder.nome((String) dadosParaTeste.get("nome"));
        }
        if (dadosParaTeste.containsKey("sobrenome")) {
            usuarioBuilder.sobrenome((String) dadosParaTeste.get("sobrenome"));
        }

        Usuario novoUsuario = usuarioBuilder.build();

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
            .statusCode(400);

        List<String> expectedErrors = (List<String>) dadosParaTeste.get("expectedErrors");
        for (String expectedError : expectedErrors) {
            response.then().body("erro", containsString(expectedError));
        }
    }

    @Test
    @DisplayName("Deve criar uma pauta e validar os campos da resposta")
    public void deveRetornar201aoCriarUmaPauta() {
        Pauta novaPauta = Pauta.builder()
            .assunto("Assunto da Pauta9")
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
            .body("assunto", equalTo("Assunto da Pauta9"))
            .body("categoria", equalTo("TRANSPORTE"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar uma pauta com dados inválidos")
    public void deveRetornar400AoCriarPautaComDadosInvalidos() {
        Pauta pautaInvalida = Pauta.builder()
            .assunto("") 
            .categoria("TRANSPORTE") 
            .build();

        System.out.println("Corpo da Solicitação: " + pautaInvalida.toString());

        Response response = given()
            .header(HEADER_AUTHORIZATION, tokenAdmin)
            .body(pautaInvalida)
            .contentType(ContentType.JSON)
        .when() 
            .post(ENDPOINT_CREATE_PAUTA);

        System.out.println("Corpo da Resposta: " + response.getBody().asString());

        response.then()
            .log().all()
            .statusCode(400)
            .body("erro", containsString("Assunto deve ser informado"));
    }


    @Test
    @DisplayName("Deve abrir a votação de uma pauta e validar os campos da resposta")
    public void deveRetornar200aoAbrirVotacaoDeUmaPauta() {
        AbrirSessao abrirSessao = AbrirSessao.builder()
            .minutos(2L)
            .pautaId(25)
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
            .body("pautaId", equalTo(25))
            .body("votosPositivos", equalTo(0))
            .body("votosNegativos", equalTo(0))
            .body("dataAbertura", notNullValue())
            .body("dataFechamento", notNullValue())
            .body("sessaoAtiva", equalTo(true));
        }

        @Test
        @DisplayName("Deve retornar erro ao criador da pauta tentar votar")
        public void deveRetornarErroAoCriadorDaPautaVotar() {
            VotoInterno votoInterno = VotoInterno.builder()
                .pautaId(25)
                .tipoDeVoto(TipoDeVoto.VOTO_POSITIVO)
                .build();
        
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
                .statusCode(400) 
                .body("erro", equalTo("O criador não pode votar na pauta criada.")); 
        }
        

    @Test
    @DisplayName("Deve votar positivamente na pauta voto externo")
    public void deveRetornar200aoFazerVotoExternoPositivo() {
        VotoExterno votoExterno = VotoExterno.builder()
            .pautaId(25)
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
            .body("pautaId", equalTo(25))
            .body("votosPositivos", equalTo(1))
            .body("votosNegativos", equalTo(0))
            .body("dataAbertura", notNullValue())
            .body("dataFechamento", notNullValue())
            .body("sessaoAtiva", equalTo(true));
    }

    @Test
    @DisplayName("Deve votar negativamente na pauta voto externo")
    public void deveRetornar200aoFazerVotoExternoNegativo() {
        VotoExterno votoExterno = VotoExterno.builder()
            .pautaId(25)
            .tipoDeVoto(TipoDeVoto.VOTO_NEGATIVO)
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
            .statusCode(400)
            .body("erro", equalTo("Não é possível votar duas vezes."));
        }


}