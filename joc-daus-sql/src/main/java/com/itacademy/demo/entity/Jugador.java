package com.itacademy.demo.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jugador")
public class Jugador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "fecha_registro", columnDefinition = "DATE")
	private LocalDate fecha_registro;

	@OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Tirada> misTiradas = new ArrayList<>();
	
	@JsonIgnore
	@Transient
	private double porcentaje_exito_tirada;

	public Jugador() {
		this.nombre = "Anonimo";
	}

	public Jugador(int id, String nombre, LocalDate fecha_registro) {
		this.id = id;
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

	public double getPorcentaje_exito_tirada() {
		return porcentaje_exito_tirada;
	}

	public void setPorcentaje_exito_tirada(double porcentaje_exito_tirada) {
		this.porcentaje_exito_tirada = porcentaje_exito_tirada;
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


	//calcula el porcentaje de éxito del jugador
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
		laTirada.setJugador(this);
	}

	@Override
	public String toString() {
		return "Jugador [id=" + id + ", nombre=" + nombre + ", misTiradas=" + getMisTiradas()
				+ ", porcentaje_exito_tirada=" + calculaPorcentajeExitoPorJugador() + "]";
	}

}
