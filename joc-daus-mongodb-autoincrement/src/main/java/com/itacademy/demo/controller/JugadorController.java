package com.itacademy.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.service.JugadorServiceImpl;

@RestController
@RequestMapping("players")
public class JugadorController {

	@Autowired
	private JugadorServiceImpl service;

	// GET ALL PLAYERS
	@GetMapping
	public ResponseEntity<List<Jugador>> getAllJugadors() {
		try {
			List<Jugador> jugadores = service.getAllJugadors();
			return new ResponseEntity<>(jugadores, HttpStatus.FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// GET PLAYER BY ID
	@RequestMapping(value = "/{id}")
	public ResponseEntity<Jugador> getJugadorDetails(@PathVariable("id") int id) {

		try {
			Optional<Jugador> jugador = service.getJugadorById(id);
			if (jugador.isPresent()) {
				return new ResponseEntity<>(jugador.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// POST: /players : crea un jugador
	@PostMapping
	public ResponseEntity<Jugador> createJugador(@RequestBody Jugador jugador) {

		try {
			service.saveJugador(jugador);
			return new ResponseEntity<>(jugador, HttpStatus.CREATED);

		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// PUT /players : modifica el nom del jugador
	@PutMapping
	public ResponseEntity<Jugador> updateJugador(@RequestBody Jugador jugador) {

		try {
			Optional<Jugador> optionalJugador = service.getJugadorById(jugador.getId());

			if (optionalJugador.isPresent()) {
				Jugador updateJugador = optionalJugador.get();
				updateJugador.setNombre(jugador.getNombre());
				service.updateJugador(updateJugador);
				return new ResponseEntity<>(updateJugador, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	// GET /players/: retorna el llistat de tots els jugadors del sistema amb el seu
	// percentatge mig d’èxits
	@RequestMapping(value = "/")
	public ResponseEntity<Map<String, Double>> listaPorcentajeMedioExitosJugadores() {

		try {
			Map<String, Double> listaPorcentajeMedioExitos = service.listaPorcentajeMedioExitos();
			return new ResponseEntity<Map<String, Double>>(listaPorcentajeMedioExitos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	// GET /players/ranking: retorna el ranking mig de tots els jugadors del sistema
	// . És a dir, el percentatge mig d’èxits.
	@RequestMapping(value = "/ranking")
	public ResponseEntity<Double> getPorcentajeMedioExitos() {

		try {
			double porcentajeExitoJugadores = service.getJugadoresRanking();
			return new ResponseEntity<>(porcentajeExitoJugadores, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	// GET /players/ranking/loser: retorna el jugador amb pitjor percentatge d’èxit
	@RequestMapping(value = "/ranking/loser")
	public ResponseEntity<Jugador> getjugadorPerdedor() {

		try {
			Jugador jugadorPerdedor = service.getJugadorPerdedor();
			return new ResponseEntity<>(jugadorPerdedor, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// GET /players/ranking/winner: retorna el jugador amb millor percentatge d’èxit
	@RequestMapping(value = "/ranking/winner")
	public ResponseEntity<Jugador> getjugadorGanador() {

		try {
			Jugador jugadorGanador = service.getJugadorGanador();
			return new ResponseEntity<>(jugadorGanador, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}
}
