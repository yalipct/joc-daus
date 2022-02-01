package com.itacademy.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.itacademy.demo.entity.Jugador;

public interface IJugadorService {

	// GET ALL PLAYERS
	List<Jugador> getAllJugadors();

	// GET PLAYER BY ID
	Optional<Jugador> getJugadorById(int id);

	// player name exists?
	boolean existeNombreJugador(Jugador jugador);

	// POST: /players : crea un jugador
	Jugador saveJugador(Jugador jugador);

	// PUT /players : modifica el nom del jugador
	Jugador updateJugador(Jugador jugador);

	// GET /players/: retorna el llistat de tots els jugadors del sistema amb el seu
	// percentatge mig d’èxits
	Map<String, Double> listaPorcentajeMedioExitos();

	// GET /players/ranking: retorna el ranking mig de tots els jugadors del sistema
	// . És a dir, el percentatge mig d’èxits.
	double getJugadoresRanking();

	// GET /players/ranking/loser: retorna el jugador amb pitjor percentatge d’èxit
	Jugador getJugadorPerdedor();

	// GET /players/ranking/winner: retorna el jugador amb millor percentatge d’èxit
	Jugador getJugadorGanador();
}
