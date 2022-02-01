package com.itacademy.demo.controllerIT;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
class TiradasControllerIT {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@Order(9)
	void testAddTiradaIntegrationTest() throws JSONException {

		Jugador jugador = new Jugador(2, "frank", LocalDate.of(2022, Month.APRIL, 01));
		Tirada tirada = new Tirada(3, 1, 6, true, jugador);

//		String expected = "{\r\n" + "    \"id\": 3,\r\n" + "    \"valorTirada1\": 1,\r\n"
//				+ "    \"valorTirada2\": 6,\r\n" + "    \"ganado\": true\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// response body
		HttpEntity<Tirada> request = new HttpEntity<Tirada>(tirada, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/players/2/tiradas", request,
				String.class);

		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		assertEquals(HttpStatus.OK, response.getStatusCode());
//		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(10)
	void testGetTiradasByJugadorIntegrationTest() throws JSONException {

		String expected = "[\r\n" + "    {\r\n" + "        \"id\": 1,\r\n" + "        \"valorTirada1\": 5,\r\n"
				+ "        \"valorTirada2\": 6,\r\n" + "        \"ganado\": false\r\n" + "    },\r\n" + "    {\r\n"
				+ "        \"id\": 2,\r\n" + "        \"valorTirada1\": 3,\r\n" + "        \"valorTirada2\": 4,\r\n"
				+ "        \"ganado\": true\r\n" + "    }\r\n" + "]";

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/players/1/tiradas",
				String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(11)
	void testDeleteTiradasJugadorIntegrationTest() throws JSONException {

		Jugador jugador = new Jugador(2, "jorge", LocalDate.of(2022, Month.APRIL, 01));
		Tirada tirada = new Tirada(3, 1, 6, true, jugador);
		jugador.addTirada(tirada);

		String expected = "{\r\n" + "    \"id\": 2,\r\n" + "    \"nombre\": \"jorge\",\r\n"
				+ "    \"fecha_registro\": \"2022-04-01\",\r\n" + "    \"misTiradas\": []\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// response body
		HttpEntity<Tirada> request = new HttpEntity<Tirada>(tirada, headers);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/players/2/tiradas",
				HttpMethod.DELETE, request, String.class);

		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		// Borra el registro de la bbdd pero como no retorna nada no se puede comprobar
		// con asserts
		// Por ello se implementa todo el código de arriba pasando HttpMethod.DELETE en
		// el response
		// restTemplate.delete("http://localhost:8080/players/2/tiradas");
	}

}
