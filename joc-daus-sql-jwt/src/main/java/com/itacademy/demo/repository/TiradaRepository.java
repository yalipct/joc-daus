package com.itacademy.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itacademy.demo.entity.Tirada;

@Repository
public interface TiradaRepository extends JpaRepository<Tirada, Integer> {
	
	List<Tirada> getTiradasByJugadorId(int player_id);
}
