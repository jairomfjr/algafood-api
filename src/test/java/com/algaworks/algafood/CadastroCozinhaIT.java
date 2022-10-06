package com.algaworks.algafood;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CadastroCozinhaIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private Flyway flyway;
	
	@Before
	public void setUp() {
		enableLoggingOfRequestAndResponseIfValidationFails();
		basePath = "/cozinhas";
		RestAssured.port = port;
		flyway.migrate();
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConter4Cozinhas_QuandoConsultarCozinhas() {		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(4))
			.body("nome", hasItems("Indiana", "Tailandesa"));
	}
	
	@Test
	public void testRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body("{\"nome\": \"Chinesa\"}")
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
}