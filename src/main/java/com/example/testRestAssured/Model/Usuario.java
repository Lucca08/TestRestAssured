package com.example.testRestAssured.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Usuario {
    private Login autenticacaoDto;
    
    private String nome;

    private String sobrenome;

    private String cpf;

    private boolean admin;
}
