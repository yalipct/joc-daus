package com.itacademy.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@SpringBootTest(classes = { JugadorServiceImplTest.class })
class JugadorServiceImplTest {

	@Mock
	private JugadorRepository jugadorRepository;

	@Mock
	private TiradaRepository tiradaRepository;

	@InjectMocks
	private JugadorServiceImpl service;

	public List<Jugador> lista_jugadores;

	Jugador jugador;
	Jugador jugador1;
	Jugador jugador2;
	Jugador jugadorAnonimo;

	Tirada tirada1;
	Tirada tirada2;
	Tirada tirada3;

	@BeforeEach
	void setUp() throws Exception {

		jugador1 = new Jugador(1, "mar", LocalDate.of(2015, Month.DECEMBER, 31));
		jugador2 = new Jugador(2, "juan", LocalDate.of(2007, Month.MAY, 13));
		jugadorAnonimo = new Jugador(3, "", LocalDate.of(2021, Month.OCTOBER, 06));

		lista_jugadores = new ArrayList<Jugador>();
		lista_jugadores.add(jugador1);
		lista_jugadores.add(jugador2);

		tirada1 = new Tirada(1, 4, 3, true, jugador1);
		tirada2 = new Tirada(2, 2, 2, false, jugador2);
		tirada3 = new Tirada(2, 6, 1, true, jugador1);

	}

	@Test
	@DisplayName("Test getAllJugadors Success")
	void testGetAllJugadors() {

		// Setup our mock repository
		when(jugadorRepository.findAll()).thenReturn(lista_jugadores);

		// Execute the service call
		List<Jugador> jugadores = service.getAllJugadors();

		// Assert the response
		assertEquals(2, jugadores.size());
	}

	@Test
	@DisplayName("Test porcentajeMedioExitos Success")
	void testPorcentajeMedioExitos() {

		// Setup our mock repository
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);

		List<Jugador> lista_jugadores = Arrays.asList(jugador1);
		Map<String, Double> listaPorcentajeMedioExitos = new HashMap<String, Double>();

		String key = jugador1.getNombre();
		Double value = jugador1.calculaPorcentajeExitoPorJugador();
		listaPorcentajeMedioExitos.put(key, value);

		when(jugadorRepository.findAll()).thenReturn(lista_jugadores);
		when(tiradaRepository.getTiradasByJugadorId(jugador1.getId())).thenReturn(Arrays.asList(tirada1, tirada2));

		// Execute the service call
		Map<String, Double> mapExpected = service.listaPorcentajeMedioExitos();

		// Assert the response
		assertEquals(listaPorcentajeMedioExitos, mapExpected);
	}

	@Test
	@DisplayName("Test porcentajeMedioExitos Empty")
	void testPorcentajeMedioExitosIsEmpty() {

		// Setup our mock repository
		Map<String, Double> listaPorcentajeMedioExitos = new HashMap<String, Double>();

		when(jugadorRepository.findAll()).thenReturn(Collections.emptyList());

		// Execute the service call
		Map<String, Double> mapExpected = service.listaPorcentajeMedioExitos();

		// Assert the response
		assertEquals(listaPorcentajeMedioExitos, mapExpected);

	}

	@Test
	@DisplayName("Test porcentajeMedioExitos Return Default Value")
	void testPorcentajeMedioExitosDefaultValue() {

		// Setup our mock repository
		List<Jugador> lista_jugadores = Arrays.asList(jugador1);
		Map<String, Double> listaPorcentajeMedioExitos = new HashMap<String, Double>();

		String key = jugador1.getNombre();
		Double value = jugador1.calculaPorcentajeExitoPorJugador();
		listaPorcentajeMedioExitos.put(key, value);

		when(jugadorRepository.findAll()).thenReturn(lista_jugadores);
		when(tiradaRepository.getTiradasByJugadorId(jugador1.getId())).thenReturn(Collections.emptyList());

		// Execute the service call
		Map<String, Double> mapExpected = service.listaPorcentajeMedioExitos();

		// Assert the response
		assertEquals(listaPorcentajeMedioExitos.get(key), mapExpected.get(key));

	}

	@Test
	@DisplayName("Test Ranking Medio Jugadores Success")
	void testGetJugadoresRanking() {

		// Setup our mock repository
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada3);
		jugador2.addTirada(tirada2);

		when(jugadorRepository.findAll()).thenReturn(lista_jugadores);

		double mediaExpected = 50;

		// Execute the service call
		double mediaActual = service.getJugadoresRanking();

		// Assert the response
		assertEquals(mediaActual, mediaExpected);
	}

	@Test
	@DisplayName("Test Lista Ranking Jugadores Success")
	void testGetListJugadoresRanking() {

		// Setup our mock repository
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador1.addTirada(tirada3);

		when(jugadorRepository.findAll()).thenReturn(lista_jugadores);

		List<Jugador> lista_Actual = lista_jugadores;

		// Execute the service call
		List<Jugador> lista_Esperada = service.getListJugadoresRanking(lista_jugadores);

		// Assert the response
		assertEquals(lista_Actual, lista_Esperada);
	}

	@Test
	@DisplayName("Test findById Success")
	void testGetJugadorById() {

		// Setup our mock repository

		when(jugadorRepository.findById(1)).thenReturn(Optional.of(jugador1));

		// Execute the service call
		Optional<Jugador> returnedJugador = service.getJugadorById(1);

		// Assert the response
		assertTrue(returnedJugador.isPresent());
		assertSame(returnedJugador.get(), jugador1);
		assertEquals(1, returnedJugador.get().getId());
	}

	@Test
	@DisplayName("Test findById Not Found")
	void testGetJugadorByIdNotFound() {

		// Setup our mock repository
		when(jugadorRepository.findById(3)).thenReturn(Optional.empty());

		// Execute the service call
		Optional<Jugador> returnedJugador = service.getJugadorById(1);

		// Assert the response
		assertFalse(returnedJugador.isPresent(), "Jugador no encontrado");
	}

	@Test
	@DisplayName("Test save jugador")
	void testSaveJugador() {

		// Setup our mock repository
		when(jugadorRepository.save(jugador1)).thenReturn(jugador1);

		// Execute the service call
		Jugador returnedJugador = service.saveJugador(jugador1);

		// Assert the response
		assertEquals(jugador1, returnedJugador);
		assertNotNull(returnedJugador, "El jugador guardado no debe ser null");

	}

	@Test
	@DisplayName("Test save jugador Anonimo")
	void testSaveJugadorAnonimo() throws Exception {

		// Setup our mock repository
		when(jugadorRepository.existsByNombre(jugadorAnonimo.getNombre())).thenReturn(false);

		String nombre = "Anonimo1";

		when(jugadorRepository.save(jugadorAnonimo)).thenReturn(jugadorAnonimo);

		// Execute the service call
		Jugador returnedJugador = service.saveJugador(jugadorAnonimo);

		// Assert the response
		assertEquals(nombre, returnedJugador.getNombre());
	}

	@Test
	@DisplayName("Test save jugador null")
	void testSaveJugadorNull() throws Exception {

		// Setup our mock repository
		jugador = new Jugador(1, null, LocalDate.of(2018, Month.NOVEMBER, 05));
		when(jugadorRepository.existsByNombre(jugador.getNombre())).thenReturn(false);

		String nombre = "Anonimo2";

		when(jugadorRepository.save(jugador)).thenReturn(jugador);

		// Execute the service call
		Jugador returnedJugador = service.saveJugador(jugador);

		// Assert the response
		assertEquals(returnedJugador, jugador);
		assertEquals(nombre, returnedJugador.getNombre());
	}

	
	@Test
	@DisplayName("Test jugador name exists")
	void testExisteNombreJugador() {

		// Setup our mock repository
		when(jugadorRepository.existsByNombre(jugador1.getNombre())).thenReturn(true);

		// Execute the service call
		Boolean returnedJugador = service.existeNombreJugador(jugador1);

		// Assert the response
		assertTrue(returnedJugador);
	}

	@Test
	void testUpdateJugador() {

		// Setup our mock repository
		jugador = new Jugador(2, "jorge", LocalDate.of(2012, Month.DECEMBER, 20));
		when(jugadorRepository.save(jugador)).thenReturn(jugador);

		// Execute the service call
		Jugador returnedJugador = service.updateJugador(jugador);

		// Assert the response
		assertEquals(returnedJugador, jugador);

	}

	@Test
	void testGetJugadorPerdedor() {

		// Setup our mock repository
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada3);
		jugador2.addTirada(tirada2);

		when(jugadorRepository.findAll()).thenReturn(lista_jugadores);

		// Execute the service call
		Jugador jugadorPerdedor = service.getJugadorPerdedor();

		// Assert the response
		assertEquals(jugador2.getNombre(), jugadorPerdedor.getNombre());
	}

	@Test
	void testGetJugadorGanador() {

		// Setup our mock repository
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada3);
		jugador2.addTirada(tirada2);

		when(jugadorRepository.findAll()).thenReturn(lista_jugadores);

		// Execute the service call
		Jugador jugadorGanador = service.getJugadorGanador();

		// Assert the response
		assertEquals(jugador1.getNombre(), jugadorGanador.getNombre());
	}

}
