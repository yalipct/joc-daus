package com.itacademy.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "tiradas")
public class Tirada {


	@Id
	private int id;
	
	//variable para cambiar el id generado por mongo por un número autoincremental
	@Transient
    public static final String SEQUENCE_NAME = "tiradas_sequence";

	@Field(name = "valortirada1")
	private int valorTirada1;

	@Field(name = "valortirada2")
	private int valorTirada2;

	@Field(name = "ganado")
	private boolean ganado;


	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idjugador")
	private int idjugador;

	public Tirada() {
	}
	
	public Tirada(int idjugador) {
		this.valorTirada1 = generarTirada();
		this.valorTirada2 = generarTirada();
		this.ganado = resultadoTirada();
		this.idjugador = idjugador;
	}

	public Tirada(int valorTirada1, int valorTirada2, boolean ganado, int idjugador) {
		this.valorTirada1 = valorTirada1;
		this.valorTirada2 = valorTirada2;
		this.ganado = ganado;
		this.idjugador = idjugador;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValorTirada1() {
		return valorTirada1;
	}

	public void setValorTirada1(int valorTirada1) {
		this.valorTirada1 = valorTirada1;
	}

	public int getValorTirada2() {
		return valorTirada2;
	}

	public void setValorTirada2(int valorTirada2) {
		this.valorTirada2 = valorTirada2;
	}

	public boolean isGanado() {
		return ganado;
	}

	public void setGanado(boolean ganado) {
		this.ganado = ganado;
	}	

	public int getJugador_id() {
		return idjugador;
	}

	public void setJugador_id(int jugador_id) {
		this.idjugador = jugador_id;
	}

	//comprueba si la tirada se ha ganado o no
	public boolean resultadoTirada() {
		if (this.valorTirada1 + this.valorTirada2 == 7) {
			return true;
		} else {
			return false;
		}
	}

	//genera una tirada de dados random
	public int generarTirada() {
		int random = (int) Math.floor(Math.random() * 6 + 1);
		return random;
	}
}
