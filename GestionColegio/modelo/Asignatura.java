/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.modelo;

import java.util.ArrayList;

/**
 *
 * @author river
 */
public class Asignatura implements Descriptible {
    private String nombre;
    private ArrayList<Calificacion> calificaciones;

    public Asignatura(String nombre) {
        this.nombre = nombre;
        calificaciones = new ArrayList<>();
    }
    
    public void agregarCalificacion(Calificacion cal){
        getCalificaciones().add(cal);
    }
    
    public String listarCalificaiones () {
        StringBuilder lis = new StringBuilder();
        if (getCalificaciones().isEmpty()) {
            lis.append("  - Sin calificaciones registradas.\n");
        } else {
            for (Calificacion calificacion : getCalificaciones()) {
                lis.append("  - ").append(calificacion.toString()).append("\n");
            }
        }
        return lis.toString();
    }
    
    public float promedio(){
        float sum = 0;
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0; 
        }
        for (Calificacion calificacion : calificaciones) {
            sum += calificacion.getNota();
        }
        return sum/calificaciones.size();
    }

    @Override
    public String toString() {
        return nombre; 
    }
    
    

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the calificaciones
     */
    public ArrayList<Calificacion> getCalificaciones() {
        if (this.calificaciones == null) {
            this.calificaciones = new ArrayList<>(); 
        }
        return calificaciones;
    }

    /**
     * @param calificaciones the calificaciones to set
     */
    public void setCalificaciones(ArrayList<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    @Override
    public String obtenerDescripcionCompleta() {
        return "Asignatura: " + getNombre() + "\n" +
               "Número de calificaciones registradas: " + (getCalificaciones() != null ? getCalificaciones().size() : 0) + "\n" +
               "Promedio actual: " + String.format("%.2f", promedio());
    }

    @Override
    public String obtenerResumen() {
        return getNombre(); 
    }
}
