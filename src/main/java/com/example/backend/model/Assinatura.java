package com.example.backend.model;

import java.time.LocalDate;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Assinatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @ManyToOne
    @JoinColumn(name = "cod_App")
    private Aplicativo aplicativo;

    @ManyToOne
    @JoinColumn(name = "cod_Cli")
    private Cliente cliente;

     
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;
}
