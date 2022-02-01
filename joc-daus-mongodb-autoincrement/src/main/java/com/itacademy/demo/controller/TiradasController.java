package com.itacademy.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;
import com.itacademy.demo.service.TiradaServiceImpl;

@RestController
@RequestMapping("players")
public class TiradasController {

	@Autowired
	private TiradaServiceImpl service;

	//POST /players/{id}/games/ : un jugador específic realitza una tirada  dels daus.
	@PostMapping("/{id}/tiradas")
	public ResponseEntity<Tirada> addTirada(@PathVariable("id") int id) {

		try {
			Tirada tirada = service.jugadorTiraDados(id);
			if (tirada != null) {
				return new ResponseEntity<>(tirada, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//GET /players/{id}/games: retorna el llistat de jugades per un jugador. 
	@GetMapping("/{id}/tiradas")
	public ResponseEntity<List<Tirada>> getTiradasByJugador(@PathVariable("id") int id) {

		try {
			if (service.getTiradasByJugador(id) != null) {
				return new ResponseEntity<>(service.getTiradasByJugador(id), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//DELETE /players/{id}/games: elimina les tirades del jugador 
//	@Transactional
	@DeleteMapping("/{id}/tiradas")
	public ResponseEntity<String> deleteTiradasJugador(@PathVariable("id") int id) {

		try {
			service.borraTiradas(id);
			return new ResponseEntity<>("Tiradas borradas correctamente", HttpStatus.OK);

		} catch (Exception ex) {
			return new  ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
