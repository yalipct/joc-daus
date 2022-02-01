package com.itacademy.demo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.NonNull;

@Document(collection = "tiradas")
public class Tirada {

	@Id
	@NonNull
	private String id;
		
	@Field(name = "valortirada1")
	private int valorTirada1;

	@Field(name = "valortirada2")
	private int valorTirada2;

	@Field(name = "ganado")
	private boolean ganado;

	@Field(name = "idjugador")
	private int idjugador;

	public Tirada() {
	}
	
	public Tirada(String id, int jugadorid) {
		this.id = id;
		this.valorTirada1 = generarTirada();
		this.valorTirada2 = generarTirada();
		this.ganado = resultadoTirada();
		this.idjugador = jugadorid;
	}	

	public Tirada(int jugadorid) {
		this.idjugador = jugadorid;
		this.valorTirada1 = generarTirada();
		this.valorTirada2 = generarTirada();
		this.ganado = resultadoTirada();
	}

	public Tirada(String id, int valorTirada1, int valorTirada2, boolean ganado, int jugadorid) {
		this.id = id;
		this.valorTirada1 = valorTirada1;
		this.valorTirada2 = valorTirada2;
		this.ganado = ganado;
		this.idjugador = jugadorid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public int getIdjugador() {
		return idjugador;
	}

	public void setIdjugador(int idjugador) {
		this.idjugador = idjugador;
	}

	public boolean resultadoTirada() {
		if (this.valorTirada1 + this.valorTirada2 == 7) {
			return true;
		} else {
			return false;
		}
	}

	public int generarTirada() {
		int random = (int) Math.floor(Math.random() * 6 + 1);
		return random;
	}

	@Override
	public String toString() {
		return "Tiradas [id=" + id + ", valorTirada1=" + valorTirada1 + ", valorTirada2=" + valorTirada2 + ", ganado="
				+ ganado + ", idjugador=" + idjugador + "]";
	}	
	
}
