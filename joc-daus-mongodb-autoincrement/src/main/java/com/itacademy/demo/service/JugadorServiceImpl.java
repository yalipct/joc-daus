package com.itacademy.demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;
import com.itacademy.demo.repository.JugadorRepository;
import com.itacademy.demo.repository.TiradaRepository;

@Service
public class JugadorServiceImpl implements IJugadorService {

	//esta variable la utilizo para no tener nombres anónimos repetidos y poder tener varios
	private static int contador = 1; 
	
	@Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private TiradaRepository tiradaRepository;

	@Autowired
	private SequenceGeneratorService generatorJugadorService;

	@Override
	public List<Jugador> getAllJugadors() {
		List<Jugador> lista = jugadorRepository.findAll();
		return lista;
	}

	@Override
	public Optional<Jugador> getJugadorById(int id) {
		return jugadorRepository.findById(id);
	}

	@Override
	public boolean existeNombreJugador(Jugador jugador) {
		return jugadorRepository.existsByNombre(jugador.getNombre());
	}

	@Override
	public Jugador saveJugador(Jugador jugador) throws Exception {

		if (jugador.getNombre() == null || "".equals(jugador.getNombre())) {
			jugador.setNombre("Anonimo");
		}
		if (jugadorRepository.existsByNombre(jugador.getNombre())) {
			throw new Exception("Jugador con nombre: " + jugador.getNombre() + " ya existe!!!");
		}
		if (jugador.getNombre().equalsIgnoreCase("anonimo")) {
			jugador.setNombre(jugador.getNombre() + contador);
			contador++;
		}
		jugador.setId(generatorJugadorService.getSequenceNumber(Jugador.SEQUENCE_NAME));
		return jugadorRepository.save(jugador);
	}

	@Override
	public Jugador updateJugador(Jugador jugador) {
		return jugadorRepository.save(jugador);
	}

	@Override
	public Map<String, Double> listaPorcentajeMedioExitos() {

		Map<String, Double> listaPorcentajeMedioExitos = new HashMap<String, Double>();
		List<Tirada> tiradasJugadorActual = new ArrayList<Tirada>();
		List<Jugador> allPlayers = jugadorRepository.findAll();

		if (allPlayers != null && allPlayers.size() > 0) {

			for (Jugador jugador : allPlayers) {
				tiradasJugadorActual = tiradaRepository.getTiradasByIdjugador(jugador.getId());

				if (tiradasJugadorActual.size() > 0) {
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

		double media = litado_jugadores.stream().mapToDouble(Jugador::calculaPorcentajeExitoPorJugador).average().orElse(0.0);
		return Math.round(media * 100.0) / 100.0;
	}

	// devuelve una lista de todos los jugadores con su porcentaje de éxito, la utilizo para  devolver el jugador ganador y perdedor
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
	public Jugador getJugadorPerdedor() {
		List<Jugador> listado_jugadores = jugadorRepository.findAll();
		List<Jugador> allPlayers = getListJugadoresRanking(listado_jugadores);
		allPlayers.sort(Comparator.comparing(Jugador::calculaPorcentajeExitoPorJugador)); 
		return allPlayers.get(0);
	}

	@Override
	public Jugador getJugadorGanador() {
		List<Jugador> listado_jugadores = jugadorRepository.findAll();
		List<Jugador> allPlayers = getListJugadoresRanking(listado_jugadores);
		allPlayers.sort(Comparator.comparing(Jugador::calculaPorcentajeExitoPorJugador).reversed()); 
		return allPlayers.get(0);
	}

}
