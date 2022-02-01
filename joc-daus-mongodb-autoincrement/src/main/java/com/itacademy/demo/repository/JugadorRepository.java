package com.itacademy.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.itacademy.demo.entity.Jugador;

@Repository
public interface JugadorRepository extends MongoRepository<Jugador, Integer> {
	
	boolean existsByNombre(String nombre);
	
}
