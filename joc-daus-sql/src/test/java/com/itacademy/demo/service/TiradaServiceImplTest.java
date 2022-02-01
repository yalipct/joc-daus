package com.itacademy.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;
import com.itacademy.demo.repository.JugadorRepository;
import com.itacademy.demo.repository.TiradaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TiradaServiceImplTest.class })
class TiradaServiceImplTest {

	@Mock
	private JugadorRepository jugadorRepository;

	@Mock
	private TiradaRepository tiradaRepository;

	@InjectMocks
	private TiradaServiceImpl service;

	Jugador jugador;
	Tirada tirada1;
	Tirada tirada2;
	
	@BeforeEach
	void setUp() throws Exception {
		jugador = new Jugador(1, "jorge", LocalDate.of(2017, Month.FEBRUARY, 03));
		tirada1 = new Tirada(1, 4, 3, true, jugador);
		tirada2 = new Tirada(2, 6, 1, true, jugador);
	}

	@Test
	void testJugadorTiraDados() {

		// Setup our mock repository
		when(jugadorRepository.findById(1)).thenReturn(Optional.of(jugador));
		when(tiradaRepository.save(tirada1)).thenReturn(tirada1);

		// Execute the service call
		Tirada tiradasExpected = service.jugadorTiraDados(jugador.getId());

		// Assert the response
		assertEquals(tirada1.getJugador(), tiradasExpected.getJugador());
	}

	@Test
	void testJugadorTiraDadosNull() {

		// Setup our mock repository
		int jugadorID = 4;

		when(jugadorRepository.findById(jugadorID)).thenReturn(Optional.empty());

		// Execute the service call
		Tirada tirada = service.jugadorTiraDados(jugadorID);

		// Assert the response
		assertEquals(null, tirada);
	}

	@Test
	void testGetTiradasByJugador() {
		
		// Setup our mock repository
		jugador.addTirada(tirada1);
		jugador.addTirada(tirada2);

		List<Tirada> listaActual = new ArrayList<Tirada>();
		listaActual.add(tirada1);
		listaActual.add(tirada2);

		when(tiradaRepository.getTiradasByJugadorId(1)).thenReturn(listaActual);

		// Execute the service call
		List<Tirada> tiradasExpected = service.getTiradasByJugador(jugador.getId());

		// Assert the response
		assertEquals(listaActual, tiradasExpected);

	}

	@Test
	void testDeleteTiradasJugador() {

		// Setup our mock repository
		jugador.addTirada(tirada2);

		when(jugadorRepository.findById(1)).thenReturn(Optional.of(jugador));

		// Execute the service call
		Jugador jugador2 = service.deleteTiradasJugador(jugador.getId());

		// Assert the response
		assertEquals(jugador.getMisTiradas(), jugador2.getMisTiradas());
	}

	@Test
	void testDeleteTiradasJugadorNull() {

		// Setup our mock repository
		int jugadorID = 4;

		when(jugadorRepository.findById(jugadorID)).thenReturn(Optional.empty());

		// Execute the service call
		Jugador jugador2 = service.deleteTiradasJugador(jugadorID);

		// Assert the response
		assertEquals(null, jugador2);
	}
}
