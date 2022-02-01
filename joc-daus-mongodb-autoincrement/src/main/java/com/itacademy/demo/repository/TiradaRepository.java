package com.itacademy.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.itacademy.demo.entity.Tirada;

@Repository
public interface TiradaRepository extends MongoRepository<Tirada, Integer> {

	List<Tirada> getTiradasByIdjugador(int player_id);
}
