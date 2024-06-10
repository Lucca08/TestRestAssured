package com.example.testRestAssured.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class AbrirSessao {
    private Long minutos;
    private Integer pautaId;
}
