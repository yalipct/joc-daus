package com.itacademy.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tiradas")
public class Tirada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="valortirada1")
	private int valorTirada1;
	
	@Column(name="valortirada2")
	private int valorTirada2;
	
	@Column(name="ganado")
	private boolean ganado;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "jugador_id")
	private Jugador jugador;
	
	public Tirada() {}
	
	public Tirada(Jugador jugador) {
		this.valorTirada1 = generarTirada();
		this.valorTirada2 = generarTirada();
		this.ganado = resultadoTirada();
		this.jugador = jugador;
	}

	public Tirada(int id, int valorTirada1, int valorTirada2, boolean ganado, Jugador jugador) {
		this.id = id;
		this.valorTirada1 = valorTirada1;
		this.valorTirada2 = valorTirada2;
		this.ganado = ganado;
		this.jugador = jugador;
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

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}	
	
	//comprueba si la tirada se ha ganado o no
	public boolean resultadoTirada() {
		if(this.valorTirada1 + this.valorTirada2 == 7) {
			return true;
		}else {
			return false;
		}
	}
	
	//genera una tirada de dados random
	public int generarTirada() {
		int random = (int) Math.floor(Math.random() * 6 + 1);
		return random;		
	}

	@Override
	public String toString() {
		return "Tiradas [id=" + id + ", valorTirada1=" + valorTirada1 + ", valorTirada2=" + valorTirada2 + ", ganado="
				+ ganado + ", jugador=" + jugador.getNombre() + "]";
	}
	
	
}
