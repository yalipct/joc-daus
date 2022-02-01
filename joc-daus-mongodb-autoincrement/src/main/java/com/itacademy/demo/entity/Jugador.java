package com.itacademy.demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "jugador")
public class Jugador {

	@Id
	private int id;

	//variable para cambiar el id generado por mongo por un número autoincremental 
	@Transient
	public static final String SEQUENCE_NAME = "jugador_sequence";

	@Field(name = "nombre")
	private String nombre;

	@Field(name = "fecha_registro")
	@DateTimeFormat(iso = ISO.DATE)
	private Date fecha_registro;

	@DBRef(lazy = true)
	private List<Tirada> misTiradas;

	@JsonIgnore
	@Transient
	private double porcentaje_exito_tirada;

	public Jugador() {
		this.nombre = "Anonimo";
	}

	public Jugador(String nombre, Date fecha_registro) {
		this.nombre = nombre;
		this.fecha_registro = fecha_registro;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Tirada> getMisTiradas() {
		return misTiradas;
	}

	public void setMisTiradas(List<Tirada> misTiradas) {
		this.misTiradas = misTiradas;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public double getPorcentaje_exito_tirada() {
		return porcentaje_exito_tirada;
	}

	public void setPorcentaje_exito_tirada(double porcentaje_exito_tirada) {
		this.porcentaje_exito_tirada = porcentaje_exito_tirada;
	}

	// calcula el porcentaje de éxito del jugador
	public double calculaPorcentajeExitoPorJugador() {
		double porcentajeExito = 0;
		if (misTiradas != null && misTiradas.size() > 0) {
			int gameListSize = misTiradas.size();
			int totalGanado = 0;
			for (Tirada tirada : misTiradas) {
				if (tirada.isGanado()) {
					totalGanado++;
				}
			}
			porcentajeExito = (totalGanado * 100) / gameListSize;
		}
		return porcentajeExito;
	}

	// añade tiradas a la lista, la utilizo en el método jugadorTiraDados del service
	public void addTirada(Tirada laTirada) {

		if (misTiradas == null) {
			misTiradas = new ArrayList<Tirada>();
		}
		misTiradas.add(laTirada);
		laTirada.setJugador_id(this.getId());
	}

}
