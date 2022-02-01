package com.itacademy.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itacademy.demo.entity.Jugador;
import com.itacademy.demo.entity.Tirada;
import com.itacademy.demo.repository.JugadorRepository;
import com.itacademy.demo.repository.TiradaRepository;

@Service
public class TiradaServiceImpl implements ITiradaService {

	@Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private TiradaRepository tiradaRepository;

	@Override
	public Tirada jugadorTiraDados(int player_id) {

		Optional<Jugador> jugador = jugadorRepository.findById(player_id);

		if (jugador.isPresent()) {
			Jugador jugador1 = jugador.get();
			Tirada tirada = new Tirada(jugador1);
			jugador1.addTirada(tirada);
			tiradaRepository.save(tirada);
			return tirada;
		}
		return null;
	}

	@Override
	public List<Tirada> getTiradasByJugador(int player_id) {
		return tiradaRepository.getTiradasByJugadorId(player_id);
	}

	@Override
	public Jugador deleteTiradasJugador(int player_id) {
		Optional<Jugador> optionalJugador = jugadorRepository.findById(player_id);
		if (optionalJugador.isPresent()) {
			Jugador jugador = optionalJugador.get();
			tiradaRepository.deleteAll(jugador.getMisTiradas());
			jugador.getMisTiradas().clear();
			return jugador;
		}
		return null;
	}

}
