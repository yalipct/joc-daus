package com.itacademy.demo.controllerIT;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.itacademy.demo.entity.Jugador;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
class JugadorControllerIT {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@Order(1)
	void testGetAllJugadorsIntegrationTest() throws JSONException {

		String expected = "[\r\n" + "    {\r\n" + "        \"id\": 1,\r\n" + "        \"nombre\": \"jugador1\",\r\n"
				+ "        \"fecha_registro\": \"2020-02-20\",\r\n" + "        \"misTiradas\": [\r\n"
				+ "            {\r\n" + "                \"id\": 1,\r\n" + "                \"valorTirada1\": 5,\r\n"
				+ "                \"valorTirada2\": 6,\r\n" + "                \"ganado\": false\r\n"
				+ "            },\r\n" + "            {\r\n" + "                \"id\": 2,\r\n"
				+ "                \"valorTirada1\": 3,\r\n" + "                \"valorTirada2\": 4,\r\n"
				+ "                \"ganado\": true\r\n" + "            }\r\n" + "        ]\r\n" + "    },\r\n"
				+ "    {\r\n" + "        \"id\": 2,\r\n" + "        \"nombre\": \"jorge\",\r\n"
				+ "        \"fecha_registro\": \"2022-04-01\",\r\n" + "        \"misTiradas\": []\r\n" + "    }\r\n"
				+ "]";

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/players", String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(2)
	void testListaPorcentajeMedioExitosJugadoresIntegrationTest() throws JSONException {

		String expected = "{\r\n" + "    \"jugador1\": 50.0,\r\n" + "    \"jorge\": 0.0\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/players/", String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(3)
	void testGetPorcentajeMedioExitosIntegrationTest() throws JSONException {

		String expected = "25.0";

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/players/ranking",
				String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(4)
	void testGetJugadorDetailsIntegrationTest() throws JSONException {

		String expected = "{\r\n" + "    \"id\": 1,\r\n" + "    \"nombre\": \"jugador1\",\r\n"
				+ "    \"fecha_registro\": \"2020-02-20\",\r\n" + "    \"misTiradas\": [\r\n" + "        {\r\n"
				+ "            \"id\": 1,\r\n" + "            \"valorTirada1\": 5,\r\n"
				+ "            \"valorTirada2\": 6,\r\n" + "            \"ganado\": false\r\n" + "        },\r\n"
				+ "        {\r\n" + "            \"id\": 2,\r\n" + "            \"valorTirada1\": 3,\r\n"
				+ "            \"valorTirada2\": 4,\r\n" + "            \"ganado\": true\r\n" + "        }\r\n"
				+ "    ]\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/players/1", String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(5)
	void testCreateJugadorIntegrationTest() throws JSONException {

		Jugador jugador = new Jugador(3, "maria", LocalDate.of(2021, Month.MAY, 13));

		String expected = "{\r\n" + "    \"id\": 3,\r\n" + "    \"nombre\": \"maria\",\r\n"
				+ "    \"fecha_registro\": \"2021-05-13\",\r\n" + "    \"misTiradas\": []\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// response body
		HttpEntity<Jugador> request = new HttpEntity<Jugador>(jugador, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/players", request,
				String.class);

		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(6)
	void testUpdateJugadorIntegrationTest() throws JSONException {

		Jugador jugador = new Jugador(2, "frank", LocalDate.of(2022, Month.APRIL, 01));

		String expected = "{\r\n" + "    \"id\": 2,\r\n" + "    \"nombre\": \"frank\",\r\n"
				+ "    \"fecha_registro\": \"2022-04-01\",\r\n" + "    \"misTiradas\": []\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// response body
		HttpEntity<Jugador> request = new HttpEntity<Jugador>(jugador, headers);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/players", HttpMethod.PUT,
				request, String.class);

		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(7)
	void testGetjugadorPerdedorIntegrationTest() throws JSONException {

		String expected = "{\r\n" + "    \"id\": 2,\r\n" + "    \"nombre\": \"frank\",\r\n"
				+ "    \"fecha_registro\": \"2022-04-01\",\r\n" + "    \"misTiradas\": []\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/players/ranking/loser",
				String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Order(8)
	void testGetjugadorGanadorIntegrationTest() throws JSONException {

		String expected = "{\r\n" + "    \"id\": 1,\r\n" + "    \"nombre\": \"jugador1\",\r\n"
				+ "    \"fecha_registro\": \"2020-02-20\",\r\n" + "    \"misTiradas\": [\r\n" + "        {\r\n"
				+ "            \"id\": 1,\r\n" + "            \"valorTirada1\": 5,\r\n"
				+ "            \"valorTirada2\": 6,\r\n" + "            \"ganado\": false\r\n" + "        },\r\n"
				+ "        {\r\n" + "            \"id\": 2,\r\n" + "            \"valorTirada1\": 3,\r\n"
				+ "            \"valorTirada2\": 4,\r\n" + "            \"ganado\": true\r\n" + "        }\r\n"
				+ "    ]\r\n" + "}";

		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/players/ranking/winner",
				String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

}
