package com.itacademy.demo.service;

import java.util.List;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;

public interface ITiradaService {

	//POST /players/{id}/games/ : un jugador específic realitza una tirada  dels daus.  
	Tirada jugadorTiraDados(int player_id);
	
	//GET /players/{id}/games: retorna el llistat de jugades per un jugador.  
	List<Tirada> getTiradasByJugador(int player_id);
	
	//DELETE /players/{id}/games: elimina les tirades del jugador 
	Jugador deleteTiradasJugador(int player_id);

	
}
