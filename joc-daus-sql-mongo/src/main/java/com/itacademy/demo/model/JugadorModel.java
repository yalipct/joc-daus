package com.itacademy.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class JugadorModel {

	private int id;

	private String nombre;

	private LocalDate fecha_registro;

	private List<TiradaModel> listaTiradas;

	public JugadorModel() {
	}

	public JugadorModel(int id, String nombre, LocalDate fecha_registro) {
		this.id = id;
		this.nombre = nombre;
		this.fecha_registro = fecha_registro;
	}
	
	public JugadorModel(int id, String nombre, LocalDate fecha_registro, List<TiradaModel> listaTiradas) {
		this.id = id;
		this.nombre = nombre;
		this.fecha_registro = fecha_registro;
		this.listaTiradas = listaTiradas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(LocalDate fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public List<TiradaModel> getTiradas() {
		return listaTiradas;
	}

	public void setTiradas(List<TiradaModel> listaTiradas) {
		this.listaTiradas = listaTiradas;
	}	

	// calcula el porcentaje de éxito del jugador
	public Double calculaPorcentajeExitoPorJugador() {
		double porcentajeExito = 0;
		if (listaTiradas != null && listaTiradas.size() > 0) {
			int gameListSize = listaTiradas.size();
			int totalGanado = 0;
			for (TiradaModel tirada : listaTiradas) {
				if (tirada.isGanado()) {
					totalGanado++;
				}
			}
			porcentajeExito = (totalGanado * 100) / gameListSize;
		}
		return porcentajeExito;
	}

	// añade tiradas a la lista, la necesito en jugadorTiraDados del service
	public void addTirada(TiradaModel laTirada) {

		if (listaTiradas == null) {
			listaTiradas = new ArrayList<>();
		}
		listaTiradas.add(laTirada);
		laTirada.setIdjugador(this.id);
	}

	@Override
	public String toString() {
		return "JugadorModel [id=" + id + ", nombre=" + nombre + ", fecha_registro=" + fecha_registro
				+ ", listaTiradas=" + listaTiradas + "]";
	}

}
