package com.itacademy.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itacademy.demo.document.Tirada;
import com.itacademy.demo.model.JugadorModel;
import com.itacademy.demo.service.JocDausService;

@RestController
@RequestMapping("players")
public class JocDausController {

	@Autowired
	JocDausService service;

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info() {
		return "The application is up...";
	}

	// GET /players/
	@GetMapping()
	public List<JugadorModel> getAllPlayers() {
		return service.getAllPlayers();
	}

	// POST: /players
	@PostMapping
	public String createPlayer(@RequestBody JugadorModel jugadorModel) {
		return service.createPlayer(jugadorModel);
	}

	// PUT /players
	@PutMapping("/{id}")
	public String modifyPlayerName(@PathVariable int id, @RequestBody String nombre) {
		return service.updatePlayer(id, nombre);
	}

	// POST /players/{id}/games/ --> jugador tira dados
	@PostMapping("/{id}/tiradas")
	public String playerRollsDice(@PathVariable int id, @RequestBody String tirada_id) {
		return service.addGame(id, tirada_id);
	}

	// DELETE /players/{id}/games
	@DeleteMapping("/{id}/tiradas")
	public String deleteGames(@PathVariable int id) {
		return service.deletePlayerGames(id);
	}

	// GET /players/ --> jugador + porcentaje éxito map
	@RequestMapping(value = "/percentage")
	public ResponseEntity<Map<String, Double>> listPercentages() {
		Map<String, Double> successPercentage = service.playerSuccessPercentage();
		return new ResponseEntity<>(successPercentage, HttpStatus.OK);

	}

	// GET /players/{id}/games --> lista tiradas de un jugador
	@GetMapping("/{id}/tiradas")
	public ResponseEntity<List<Tirada>> rollList(@PathVariable("id") int id) {
		return new ResponseEntity<>(service.getGamesByPlayer(id), HttpStatus.OK);
	}

	// GET /players/ranking
	@RequestMapping(value = "/ranking")
	public ResponseEntity<Double> getPlayersRanking() {
		return new ResponseEntity<>(service.getPlayersRanking(), HttpStatus.OK);
	}

	// GET /players/ranking/loser
	@RequestMapping(value = "/ranking/loser")
	public ResponseEntity<JugadorModel> getjugadorPerdedor() {

		JugadorModel jugadorPerdedor = service.getJugadorPerdedor();
		return new ResponseEntity<>(jugadorPerdedor, HttpStatus.OK);

	}

	// GET /players/ranking/winner
	@RequestMapping(value = "/ranking/winner")
	public ResponseEntity<JugadorModel> getjugadorGanador() {

		JugadorModel jugadorGanador = service.getJugadorGanador();
		return new ResponseEntity<>(jugadorGanador, HttpStatus.OK);

	}
}
