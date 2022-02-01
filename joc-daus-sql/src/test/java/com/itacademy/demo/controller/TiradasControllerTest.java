package com.itacademy.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;
import com.itacademy.demo.service.JugadorServiceImpl;
import com.itacademy.demo.service.TiradaServiceImpl;

@SpringBootTest(classes = { TiradasControllerTest.class })
class TiradasControllerTest {

	@Mock
	TiradaServiceImpl serviceTiradas;

	@Mock
	JugadorServiceImpl serviceJugador;

	@InjectMocks
	TiradaController controller;

	Jugador jugador;

	Tirada tirada;
	Tirada tirada1;
	Tirada tirada2;

	@BeforeEach
	void setUp() throws Exception {

		jugador = new Jugador(1, "jorge", LocalDate.of(2013, Month.AUGUST, 13));

		tirada = new Tirada(1, 4, 3, true, jugador);
		tirada1 = new Tirada(1, 4, 3, true, jugador);
		tirada2 = new Tirada(2, 6, 1, true, jugador);

	}

	@Test
	@DisplayName("Test jugadorTiraDados Success")
	void testJugadorTiraDados() {

		int jugadorID = 1;
		jugador.addTirada(tirada);

		when(serviceJugador.getJugadorById(jugadorID)).thenReturn(Optional.of(jugador));
		when(serviceTiradas.jugadorTiraDados(jugadorID)).thenReturn(tirada);

		ResponseEntity<Tirada> res = controller.addTirada(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(tirada, res.getBody());
	}

	@Test
	@DisplayName("Test jugadorTiraDados Not Found")
	void testJugadorTiraDadosNotFound() {

		int jugadorID = 3;

		when(serviceJugador.getJugadorById(jugadorID)).thenReturn(Optional.empty());
		when(serviceTiradas.jugadorTiraDados(jugadorID)).thenReturn(null);

		ResponseEntity<Tirada> res = controller.addTirada(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	@DisplayName("Test jugadorTiraDados Exception")
	void testJugadorTiraDadosThrowException() {

		int jugadorID = 0;
		when(serviceTiradas.jugadorTiraDados(jugadorID)).thenThrow();

		ResponseEntity<Tirada> res = controller.addTirada(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
	}

	@Test
	@DisplayName("Test getTiradasByJugador Success")
	void testGetTiradasByJugador() {

		int jugadorID = 1;

		jugador.addTirada(tirada1);
		jugador.addTirada(tirada2);

		List<Tirada> listaActual = new ArrayList<Tirada>();
		listaActual.add(tirada1);
		listaActual.add(tirada2);

		when(serviceTiradas.getTiradasByJugador(jugadorID)).thenReturn(listaActual);

		ResponseEntity<List<Tirada>> res = controller.getTiradasByJugador(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(listaActual, res.getBody());
	}

	@Test
	@DisplayName("Test getTiradasByJugador Not Found")
	void testGetTiradasByJugadorNotFound() {

		int jugadorID = 5;

		when(serviceTiradas.getTiradasByJugador(jugadorID)).thenReturn(null);

		ResponseEntity<List<Tirada>> res = controller.getTiradasByJugador(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	@DisplayName("Test getTiradasByJugador Exception")
	void testGetTiradasByJugadorThrowException() {

		int jugadorID = 0;

		when(serviceTiradas.getTiradasByJugador(jugadorID)).thenThrow();

		ResponseEntity<List<Tirada>> res = controller.getTiradasByJugador(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
	}

	@Test
	@DisplayName("Test deleteTiradasJugador Success")
	void testDeleteTiradasJugador() {

		
		 int jugadorID = 1;
		 
		 Jugador jugadorSinTiradas = new Jugador(1, "mar", LocalDate.of(2013, Month.AUGUST, 13));		 
		 jugador.addTirada(tirada);
		 
		 when(serviceJugador.getJugadorById(jugadorID)).thenReturn(Optional.of(jugador)); 
		 when(serviceTiradas.deleteTiradasJugador(jugadorID)).thenReturn(jugadorSinTiradas);
		 
		 ResponseEntity<Jugador> res = controller.deleteTiradasJugador(jugadorID);
		 
		 // Assert the response 
		 assertEquals(HttpStatus.OK, res.getStatusCode());
		 assertEquals(jugadorSinTiradas, res.getBody());
//		assertEquals(jugadorSinTiradas.getMisTiradas().isEmpty(), res.getBody().getMisTiradas());
	}

	@Test
	@DisplayName("Test deleteTiradasJugador Not Found")
	void testDeleteTiradasJugadorNotFound() {

		int jugadorID = 3;

		when(serviceJugador.getJugadorById(jugadorID)).thenReturn(null);

		ResponseEntity<Jugador> res = controller.deleteTiradasJugador(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	@DisplayName("Test deleteTiradasJugador Exception")
	void testDeleteTiradasJugadorThrowException() {

		int jugadorID = 0;

		when(serviceJugador.getJugadorById(jugadorID)).thenThrow();

		ResponseEntity<Jugador> res = controller.deleteTiradasJugador(jugadorID);

		// Assert the response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
	}

}
