package com.example.testRestAssured.Model;

import com.example.testRestAssured.Model.Enum.TipoDeVoto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class VotoExterno {
    private Integer pautaId;
    private TipoDeVoto tipoDeVoto;
    private String cpf;
    private String senha;
}
