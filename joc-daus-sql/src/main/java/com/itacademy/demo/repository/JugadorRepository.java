package com.itacademy.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itacademy.demo.entity.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Integer> {

	//comprueba si el nombre del jugador existe
	boolean existsByNombre(String nombre);
	
}
