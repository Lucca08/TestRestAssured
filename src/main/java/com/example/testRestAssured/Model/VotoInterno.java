package com.example.testRestAssured.Model;

import com.example.testRestAssured.Model.Enum.TipoDeVoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VotoInterno {
    private Integer pautaId;
    private TipoDeVoto tipoDeVoto;
    
}
