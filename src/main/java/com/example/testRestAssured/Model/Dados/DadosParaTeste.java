package com.example.testRestAssured.Model.Dados;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DadosParaTeste {
    public static Stream<Map<String, Object>> camposAusentes() {
        return Stream.of(
            Map.of(
                "email", "usuariosemnome@email.com", 
                "senha", "senha123", 
                "sobrenome", "Sobrenome",
                "cpf", "89304303215", 
                "admin", false, 
                "expectedErrors", List.of("Nome deve ser informado.")
            ),
            Map.of(
                "email", "usuariosemsobrenome@email.com", 
                "senha", "senha123", 
                "nome", "Usuario", 
                "cpf", "89304303216", 
                "admin", false, 
                "expectedErrors", List.of("Sobrenome deve ser informado.")
            ),
            Map.of(
                "senha", "senha123", 
                "nome", "Usuario", 
                "sobrenome", "Sobrenome", 
                "cpf", "89304303217", 
                "admin", false, 
                "expectedErrors", List.of("Email com formato inválido.")
            ),
            Map.of(
                "email", "usuariosemsenha@email.com", 
                "nome", "Usuario", 
                "sobrenome", "Sobrenome", 
                "cpf", "89304303218", 
                "admin", false, 
                "expectedErrors", List.of("Senha deve ser informada.")
            ),
            Map.of(
                "email", "usuariosemcpf@email.com", 
                "senha", "senha123", 
                "nome", "Usuario", 
                "sobrenome", "Sobrenome", 
                "admin", false, 
                "expectedErrors", List.of("Cpf deve conter 11 caracteres numéricos.")
            )
        );
    }
}
