package com.itacademy.demo.model;

public class TiradaModel {

	private String id;

	private int valorTirada1;

	private int valorTirada2;

	private boolean ganado;

	private int idjugador;

	public TiradaModel() {
	}

	public TiradaModel(String id, int jugador_id) {
		this.id = id;
		this.valorTirada1 = generarTirada();
		this.valorTirada2 = generarTirada();
		this.ganado = resultadoTirada();
		this.idjugador = jugador_id;
	}

	public TiradaModel(int jugador_id) {
		this.idjugador = jugador_id;
		this.valorTirada1 = generarTirada();
		this.valorTirada2 = generarTirada();
		this.ganado = resultadoTirada();
	}

	public TiradaModel(String id, int valorTirada1, int valorTirada2, boolean ganado, int jugador_id) {
		this.id = id;
		this.valorTirada1 = valorTirada1;
		this.valorTirada2 = valorTirada2;
		this.ganado = ganado;
		this.idjugador = jugador_id;
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
		return "TiradasModel [id=" + id + ", valorTirada1=" + valorTirada1 + ", valorTirada2=" + valorTirada2
				+ ", ganado=" + ganado + ", idjugador=" + idjugador + "]";
	}

	
}
