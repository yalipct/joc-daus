package com.itacademy.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

@SpringBootTest(classes = { JugadorControllerTest.class })
class JugadorControllerTest {
	
	@Mock
	JugadorServiceImpl service;
	
	@InjectMocks
	JugadorController controller;
	
	List<Jugador> jugadores;
	Jugador jugador1;
	Jugador jugador2;
	Jugador jugador3;
	
	@BeforeEach
	void setUp() throws Exception {
		
		jugador1 = new Jugador(1, "mar", LocalDate.of(2017, Month.FEBRUARY, 03));
		jugador2 = new Jugador(2, "juan", LocalDate.of(2019, Month.JULY, 10));
		
		jugadores = new ArrayList<Jugador>();
		jugadores.add(jugador1);
		jugadores.add(jugador2);;
	}

	@Test
	@DisplayName("Test getAllJugadors Success")
	void testGetAllJugadors() {
		when(service.getAllJugadors()).thenReturn(jugadores);
		ResponseEntity<List<Jugador>> res = controller.getAllJugadors();
		
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}
	
	@Test
	@DisplayName("Test getAllJugadors Not Found")
	void testGetAllJugadorsNotFound() {
		
		when(service.getAllJugadors()).thenThrow();
		ResponseEntity<List<Jugador>> res = controller.getAllJugadors();
		
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@DisplayName("Test listaPorcentajeMedioExitosJugadores Success")
	void testListaPorcentajeMedioExitosJugadores() {

		Tirada tirada1 = new Tirada(1, 4, 3, true, jugador1);
		Tirada tirada2 = new Tirada(2, 6, 1, true, jugador1);

		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);

		List<Jugador> lista_jugadores = Arrays.asList(jugador1);
		Map<String, Double> listaPorcentajeMedioExitos = new HashMap<String, Double>();

		String key = jugador1.getNombre();
		Double value = jugador1.calculaPorcentajeExitoPorJugador();
		listaPorcentajeMedioExitos.put(key, value);
			
		when(service.getAllJugadors()).thenReturn(lista_jugadores);
		when(service.listaPorcentajeMedioExitos()).thenReturn(listaPorcentajeMedioExitos);
		ResponseEntity<Map<String, Double>> res = controller.listaPorcentajeMedioExitosJugadores();
		
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(1, res.getBody().size());
	}
	
	@Test
	@DisplayName("Test listaPorcentajeMedioExitosJugadores Not Found")
	void testListaPorcentajeMedioExitosJugadoresNotFound() {
				
		when(service.listaPorcentajeMedioExitos()).thenThrow();
		ResponseEntity<Map<String, Double>> res = controller.listaPorcentajeMedioExitosJugadores();
		
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@DisplayName("Test getPorcentajeMedioExitos Success")
	void testGetPorcentajeMedioExitos() {

		Tirada tirada1 = new Tirada(1, 4, 3, true, jugador1);
		Tirada tirada2 = new Tirada(2, 2, 2, false, jugador2);
		Tirada tirada3 = new Tirada(3, 6, 1, true, jugador1);

		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada3);
		jugador2.addTirada(tirada2);

		List<Jugador> jugadores = Arrays.asList(jugador1, jugador2);

		when(service.getAllJugadors()).thenReturn(jugadores);
		
		double porcentajeMedioJugadores = 50.0;
		
		when(service.getJugadoresRanking()).thenReturn(porcentajeMedioJugadores);
		ResponseEntity<Double> res = controller.getPorcentajeMedioExitos();
		
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(service.getJugadoresRanking(), porcentajeMedioJugadores);
	}
	
	@Test
	@DisplayName("Test getPorcentajeMedioExitos Not Found")
	void testGetPorcentajeMedioExitosNotFound() {	
		
		when(service.getJugadoresRanking()).thenThrow();
		ResponseEntity<Double> res = controller.getPorcentajeMedioExitos();
		
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}

	@Test
	@DisplayName("Test findById Success")
	void testGetJugadorDetails() {
		
		int jugadorID = 2;
		
		when(service.getJugadorById(jugadorID)).thenReturn(Optional.of(jugador2));
		ResponseEntity<Jugador> res = controller.getJugadorDetails(jugadorID);
		
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(2, res.getBody().getId());
	}
	
	@Test
	@DisplayName("Test findById Not Found")
	void testGetJugadorDetailsNotFound() {
		
		int jugadorID = 1;
		
		when(service.getJugadorById(jugadorID)).thenReturn(Optional.empty());
		ResponseEntity<Jugador> res = controller.getJugadorDetails(jugadorID);
		
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
	}
	
	@Test
	@DisplayName("Test findById Throw Exception")
	void testGetJugadorDetailsThrowException() {
		
		int jugadorID = 5;
		
		when(service.getJugadorById(jugadorID)).thenThrow();
		ResponseEntity<Jugador> res = controller.getJugadorDetails(jugadorID);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
	}

	@Test
	@DisplayName("Test createJugador Success")
	void testCreateJugador() {
		
		when(service.saveJugador(jugador1)).thenReturn(jugador1);
		ResponseEntity<Jugador> res = controller.createJugador(jugador1);
		
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(jugador1, res.getBody());
		
	}
	
	@Test
	@DisplayName("Test createJugador Conflict")
	void testCreateJugadorThrowConflict() {
		
		when(service.saveJugador(jugador3)).thenThrow(NoSuchElementException.class);
		ResponseEntity<Jugador> res = controller.createJugador(jugador3);
		
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
		
	}

	@Test
	@DisplayName("Test updateJugador Success")
	void testUpdateJugador() {
		
		jugador3 = new Jugador(3, "joan", LocalDate.of(2016, Month.MARCH, 15));
		int jugadorID = 3;
				
		when(service.getJugadorById(jugadorID)).thenReturn(Optional.of(jugador3));		
		when(service.updateJugador(jugador3)).thenReturn(jugador3);
		
		ResponseEntity<Jugador> res = controller.updateJugador(jugador3);
		
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(3, res.getBody().getId());
		assertEquals("joan", res.getBody().getNombre());		
	}	
	
	
	@Test
	@DisplayName("Test updateJugador Conflict")
	void testUpdateJugadorThrowConflict() {
		
		int jugadorID = 3;
				
		when(service.getJugadorById(jugadorID)).thenReturn(Optional.empty());	
		ResponseEntity<Jugador> res = controller.updateJugador(jugador3);
		
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());		
	}

	@Test
	@DisplayName("Test getjugadorPerdedor Success")
	void testGetjugadorPerdedor() {
		
		Tirada tirada1 = new Tirada(1, 4, 3, true, jugador1);
		Tirada tirada2 = new Tirada(2, 2, 2, false, jugador2);
		Tirada tirada3 = new Tirada(3, 6, 1, true, jugador1);

		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada3);
		jugador2.addTirada(tirada2);

		List<Jugador> listado = Arrays.asList(jugador1, jugador2);

		when(service.getAllJugadors()).thenReturn(listado);
		when(service.getJugadorPerdedor()).thenReturn(jugador2);
		ResponseEntity<Jugador> res = controller.getjugadorPerdedor();
		
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(jugador2.getNombre(), res.getBody().getNombre());
	}

	@Test
	@DisplayName("Test getjugadorPerdedor Conflict")
	void testGetjugadorPerdedorConflict() {
		
		when(service.getJugadorPerdedor()).thenThrow();
		ResponseEntity<Jugador> res = controller.getjugadorPerdedor();
		
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
	}
	
	@Test
	@DisplayName("Test getjugadorGanador Success")
	void testGetjugadorGanador() {
		
		Tirada tirada1 = new Tirada(1, 4, 3, true, jugador1);
		Tirada tirada2 = new Tirada(2, 2, 2, false, jugador2);
		Tirada tirada3 = new Tirada(3, 6, 1, true, jugador1);

		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada3);
		jugador2.addTirada(tirada2);

		List<Jugador> jugadores = Arrays.asList(jugador1, jugador2);

		when(service.getAllJugadors()).thenReturn(jugadores);
		when(service.getJugadorGanador()).thenReturn(jugador1);
		ResponseEntity<Jugador> res = controller.getjugadorGanador();
		
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(jugador1.getNombre(), res.getBody().getNombre());
	}
	
	@Test
	@DisplayName("Test getjugadorGanador Conflict")
	void testGetjugadorGanadorConflict() {
		
		when(service.getJugadorGanador()).thenThrow();
		ResponseEntity<Jugador> res = controller.getjugadorGanador();
		
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
	}

}
