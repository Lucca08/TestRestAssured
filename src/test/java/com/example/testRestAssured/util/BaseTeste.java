package com.example.testRestAssured.util;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeAll;

public class BaseTeste {

    

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
}
