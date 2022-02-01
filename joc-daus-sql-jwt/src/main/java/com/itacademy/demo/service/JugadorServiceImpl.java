package com.itacademy.demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;
import com.itacademy.demo.repository.JugadorRepository;
import com.itacademy.demo.repository.TiradaRepository;

@Service
public class JugadorServiceImpl implements IJugadorService {

	private static int contador = 1; 
	
	@Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private TiradaRepository tiradaRepository;

	@Override
	public List<Jugador> getAllJugadors() {
		return jugadorRepository.findAll();
	}

	@Override
	public Map<String, Double> listaPorcentajeMedioExitos() {

		Map<String, Double> listaPorcentajeMedioExitos = new HashMap<>();
		List<Jugador> allPlayers = jugadorRepository.findAll();

		if (!allPlayers.isEmpty()) {

			List<Tirada> tiradasJugadorActual;

			for (Jugador jugador : allPlayers) {
				tiradasJugadorActual = tiradaRepository.getTiradasByJugadorId(jugador.getId());

				if (!tiradasJugadorActual.isEmpty()) {
					String key = jugador.getNombre();
					Double value = jugador.calculaPorcentajeExitoPorJugador();
					listaPorcentajeMedioExitos.put(key, value);
				} else {
					listaPorcentajeMedioExitos.put(jugador.getNombre(), (double) 0);
				}
			}
		}
		return listaPorcentajeMedioExitos;

	}

	@Override
	public double getJugadoresRanking() {
		List<Jugador> litado_jugadores = jugadorRepository.findAll();

		double media = litado_jugadores.stream().mapToDouble(Jugador::calculaPorcentajeExitoPorJugador).average()
				.orElse(0.0);
		return Math.round(media * 100.0) / 100.0;
	}

	// devuelve una lista del ranking de todos los jugadores, la utilizo para
		// devolver el jugador ganador y perdedor
		public List<Jugador> getListJugadoresRanking(List<Jugador> lista_jugadores) {

			List<Jugador> listActualPlayer = new ArrayList<>();

			for (Jugador jugador : lista_jugadores) {				
				Double porcentajeExito = jugador.calculaPorcentajeExitoPorJugador();
				jugador.setPorcentaje_exito_tirada(porcentajeExito);
				listActualPlayer.add(jugador);
			}
			return listActualPlayer;
		}

	@Override
	public Optional<Jugador> getJugadorById(int id) {
		return jugadorRepository.findById(id);
	}

	@Override
	public Jugador saveJugador(Jugador jugador) {

		if (jugador.getNombre() == null || "".equals(jugador.getNombre())) {
			jugador.setNombre("Anonimo");
		}
		if (jugadorRepository.existsByNombre(jugador.getNombre())) {
			throw new NoSuchElementException("Jugador con nombre: " + jugador.getNombre() + " ya existe!!!");
		} 
		if (jugador.getNombre().equalsIgnoreCase("anonimo")) {			
				jugador.setNombre(jugador.getNombre() + contador);
				contador++;
		}
		return jugadorRepository.save(jugador);
	}

	@Override
	public boolean existeNombreJugador(Jugador jugador) {
		return jugadorRepository.existsByNombre(jugador.getNombre());
	}

	@Override
	public Jugador updateJugador(Jugador jugador) {
		return jugadorRepository.save(jugador);
	}

	@Override
	public Jugador getJugadorPerdedor() {
		List<Jugador> lista_jugadores = jugadorRepository.findAll();
		List<Jugador> allPlayers = getListJugadoresRanking(lista_jugadores);
		allPlayers.sort(Comparator.comparing(Jugador::calculaPorcentajeExitoPorJugador));
		return allPlayers.get(0);
	}

	@Override
	public Jugador getJugadorGanador() {
		List<Jugador> lista_jugadores = jugadorRepository.findAll();
		List<Jugador> allPlayers = getListJugadoresRanking(lista_jugadores);
		allPlayers.sort(Comparator.comparing(Jugador::calculaPorcentajeExitoPorJugador));
		return allPlayers.get(allPlayers.size() - 1);
	}

}
