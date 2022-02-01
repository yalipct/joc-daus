package com.itacademy.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.itacademy.demo.document.Tirada;

@Repository
public interface TiradaRepository extends MongoRepository<Tirada, String> {

	public List<Tirada> findTiradaByIdjugador( int id_jugador);
	
	public void deleteAllByIdjugador(int id_jugador);
}
