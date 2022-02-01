package com.itacademy.demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import com.itacademy.demo.document.Tirada;
import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.model.JugadorModel;
import com.itacademy.demo.model.TiradaModel;
import com.itacademy.demo.repository.JugadorRepository;
import com.itacademy.demo.repository.TiradaRepository;

@Service
public class JocDausService {

	private static int contador = 1;

	@Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private TiradaRepository tiradaRepository;

	// devuelve el listado de todos los jugadores
	public List<JugadorModel> getAllPlayers() {

		List<JugadorModel> jugadores = new ArrayList<>();
		List<Jugador> jugadoresList = jugadorRepository.findAll();
		try {
			jugadoresList = jugadorRepository.findAll(); // Fetch all the players from the database.
		} catch (Exception e) {
			throw e;
		}

		if (jugadoresList.size() > 0) { // If the above list is not empty then return the list after unwrapping all the
										// records
			jugadoresList.stream().forEach(j -> { // Traverse through the records
				JugadorModel jugadorModel = new JugadorModel();
				jugadorModel.setId(j.getId());
				jugadorModel.setNombre(j.getNombre());
				jugadorModel.setFecha_registro(j.getFecha_registro());
				jugadorModel.calculaPorcentajeExitoPorJugador();
				List<Tirada> tiradasList = new ArrayList<>();
				List<TiradaModel> tiradas = new ArrayList<>();
				try {
					tiradasList = tiradaRepository.findTiradaByIdjugador(jugadorModel.getId()); // Fetch all the games
																								// by player ID.
				} catch (Exception e) {
					throw e;
				}
				if (tiradasList.size() > 0) {
					tiradasList.stream().forEach(t -> {
						TiradaModel tiradaModel = new TiradaModel();
						BeanUtils.copyProperties(t, tiradaModel);
						tiradas.add(tiradaModel);
					});
				}
				jugadorModel.setTiradas(tiradas);
				jugadores.add(jugadorModel);
			});
		}
		return jugadores;
	}

	// crea un jugador
	@Transactional
	public String createPlayer(JugadorModel jugadorModel) {
		if (!jugadorRepository.existsByNombre(jugadorModel.getNombre())) {

			if (jugadorModel.getNombre() == null || "".equals(jugadorModel.getNombre())) {
				jugadorModel.setNombre("Anonimo");
			}
			if (jugadorModel.getNombre().equalsIgnoreCase("anonimo")) {
				jugadorModel.setNombre(jugadorModel.getNombre() + contador);
				contador++;
			}

			Jugador jugador = new Jugador();
			BeanUtils.copyProperties(jugadorModel, jugador);
			try {
				jugadorRepository.save(jugador);
				jugadorModel.getTiradas().stream().forEach(t -> {
					Tirada tirada = new Tirada(jugadorModel.getId());
					t.setIdjugador(jugadorModel.getId());
					BeanUtils.copyProperties(t, tirada);
					try {
						tiradaRepository.save(tirada);
					} catch (Exception e) {
						throw e;
					}

				});
			} catch (Exception e) {
				throw e;
			}

			return "Jugador creado correctamente!";
		} else {
			return "Jugador con nombre " + jugadorModel.getNombre() + " ya existe!";
		}
	}

	// modifica el nombre de un jugador
	@Transactional
	public String updatePlayer(int id, String nombre) {

		Optional<Jugador> optionalJugador = jugadorRepository.findById(id);

		if (!optionalJugador.isPresent()) {
			return "Jugador con id:" + id + " no existe!";
		}

		Jugador jugador = optionalJugador.get();

		if (jugadorRepository.existsByNombre(nombre)) {
			return "Jugador con nombre " + nombre + " ya existe!";
		}
		jugador.setNombre(nombre);
		jugadorRepository.save(jugador);

		return "Nombre modificado correctamente!";
	}

	// un jugador realiza una tirada
	@Transactional
	public String addGame(int jugador_id, String tirada_id) {

		Optional<Jugador> optionalJugador = jugadorRepository.findById(jugador_id);

		if (optionalJugador.isPresent()) {
			Tirada tirada = new Tirada(tirada_id, jugador_id);
			tiradaRepository.save(tirada);
			return "Tirada añadida correctamente";
		} else {
			return "Jugador con id:" + jugador_id + " no existe!";
		}
	}

	// borrar las tiradas de un jugador
	@Transactional
	public String deletePlayerGames(int jugadorID) {
		Optional<Jugador> optionalJugador = jugadorRepository.findById(jugadorID);

		if (optionalJugador.isPresent()) {
			List<Tirada> listaTiradas = tiradaRepository.findTiradaByIdjugador(jugadorID);
			if (!listaTiradas.isEmpty()) {
				tiradaRepository.deleteAllByIdjugador(jugadorID);
			} else {
				return "El jugador con id:" + jugadorID + " no tiene tiradas para borrar";
			}
			return "Tiradas borradas correctamente";
		}
		return "Jugador con id:" + jugadorID + " no existe!";
	}

	// lista el nombre y el porcentaje de éxito de todos los jugadores
	public Map<String, Double> playerSuccessPercentage() {

		List<JugadorModel> jugadores = getAllPlayers();
		Map<String, Double> listPlayerSuccessPercentage = new HashMap<String, Double>();

		List<Tirada> currentPlayerRoll;

		if (jugadores.size() > 0) {
			for (JugadorModel jugador : jugadores) {
				currentPlayerRoll = tiradaRepository.findTiradaByIdjugador(jugador.getId());
				if (!currentPlayerRoll.isEmpty()) {
					String key = jugador.getNombre();
					Double value = jugador.calculaPorcentajeExitoPorJugador();
					listPlayerSuccessPercentage.put(key, value);
				} else {
					listPlayerSuccessPercentage.put(jugador.getNombre(), (double) 0);
				}
			}
		}
		return listPlayerSuccessPercentage;
	}

	// devuelve lista de tiradas de un jugador
	public List<Tirada> getGamesByPlayer(int player_id) {
		return tiradaRepository.findTiradaByIdjugador(player_id);
	}

	// porcentaje medio de éxito de todos los jugadores
	public double getPlayersRanking() {
		List<JugadorModel> listado_jugadores = getAllPlayers();
		Double porcentajeExito = 0.0;
		Double suma = 0.0;
		Double media = 0.0;

		if (listado_jugadores != null && listado_jugadores.size() > 0) {

			for (JugadorModel jugador : listado_jugadores) {
				porcentajeExito = jugador.calculaPorcentajeExitoPorJugador();
				suma += porcentajeExito;
			}
			media = suma / listado_jugadores.size();
		}
		return Math.round(media * 100.0) / 100.0;
	}

	// devuelve una lista de todos los jugadores con su porcentaje actualizado
	public List<JugadorModel> getListJugadoresRanking(List<JugadorModel> lista_jugadores) {

		List<JugadorModel> listActualPlayer = new ArrayList<>();

		for (JugadorModel jugador : lista_jugadores) {				
			jugador.calculaPorcentajeExitoPorJugador();
			listActualPlayer.add(jugador);
		}
		return listActualPlayer;
	}

	// devuelve el jugador con peor porcentaje de éxito
	public JugadorModel getJugadorPerdedor() {
		List<JugadorModel> lista_jugadores = getAllPlayers();
		List<JugadorModel> allPlayers = getListJugadoresRanking(lista_jugadores);
		allPlayers.sort(Comparator.comparing(JugadorModel::calculaPorcentajeExitoPorJugador));
		return allPlayers.get(0);
	}

	// devuelve el jugador con mejor porcentaje de éxito
	public JugadorModel getJugadorGanador() {
		List<JugadorModel> lista_jugadores = getAllPlayers();
		List<JugadorModel> allPlayers = getListJugadoresRanking(lista_jugadores);
		allPlayers.sort(Comparator.comparing(JugadorModel::calculaPorcentajeExitoPorJugador));
		return allPlayers.get(allPlayers.size() - 1);
	}

}
