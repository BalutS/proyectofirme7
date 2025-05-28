package com.modelo;

import java.io.Serializable;

/**
 *
 * @author SOTO PC
 */
import java.util.ArrayList;

public class Estudiante extends Persona implements Serializable {
    private Curso curso;
    private ArrayList<Asignatura> asignaturas;

    public Estudiante(ArrayList<Asignatura> asignaturas, String nombre, int edad, int cedula, int codigo, String tipo) {
        super(nombre, edad, cedula, codigo, tipo);
        this.asignaturas = (asignaturas != null) ? asignaturas : new ArrayList<>();
    }
    
    public Estudiante(String nombre, int edad, int cedula, int codigo, String tipo) {
        super(nombre, edad, cedula, codigo, tipo);
        this.asignaturas = new ArrayList<>();
    }
    
    public Asignatura buscarAsignatura(String nombre){
        if (this.asignaturas == null || nombre == null) return null;
        for (Asignatura asignatura : getAsignaturas()) {
            if (asignatura.getNombre().equalsIgnoreCase(nombre)) {
                return asignatura;
            }
        }
        return null;
    }
    
    public String reporteAcademico(){
        return toString() + "\n\n--- ASIGNATURAS ---\n" +  promedioAsignaturas() + "\nPromedio General: " + String.format("%.2f", promedioGeneral());
    }
    
    private String promedioAsignaturas(){
        StringBuilder sb = new StringBuilder();
        if (this.asignaturas == null || this.asignaturas.isEmpty()) {
            return "No hay asignaturas matriculadas.\n";
        }
        for (Asignatura asignatura : asignaturas) {
            sb.append("  - ").append(asignatura.getNombre()).append(": Promedio ").append(String.format("%.2f", asignatura.promedio())).append("\n");
        }
        return sb.toString();
    }
    
    private float promedioGeneral() {
        if (this.asignaturas == null || this.asignaturas.isEmpty()) {
            return 0.0f;
        }
        float sum = 0;
        int count = 0;
        for (Asignatura asignatura : asignaturas) {
            sum += asignatura.promedio();
            count++;
        }
        return (count == 0) ? 0.0f : sum / count; 
    }

    @Override
    public String toString() {
        return getNombre() + " (Cód: " + getCodigo() + ")";
    }
    
    public String getInfoCompleta() {
         String cursoInfo = (curso != null && curso.getGrado() != 0) ? " (Curso: " + curso.getGrado() + "-" + curso.getGrupo() + ")" : " (Sin curso asignado)";
         return "Estudiante: " + getNombre() + ", Código: " + getCodigo() + ", Cédula: " + getCedula() + ", Edad: " + getEdad() + cursoInfo;
    }

    public ArrayList<Asignatura> getAsignaturas() {
        if (this.asignaturas == null) {
            this.asignaturas = new ArrayList<>(); 
        }
        return asignaturas;
    }

    public void setAsignaturas(ArrayList<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getDetalleCompletoCalificaciones() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- DETALLE COMPLETO DE CALIFICACIONES ---\n\n");

        if (this.asignaturas == null || this.asignaturas.isEmpty()) {
            sb.append("No hay asignaturas matriculadas.\n");
        } else {
            for (Asignatura asignatura : this.asignaturas) {
                sb.append("Asignatura: ").append(asignatura.getNombre()).append("\n");
                sb.append(asignatura.listarCalificaiones()); 
                sb.append("  Promedio Asignatura: ").append(String.format("%.2f", asignatura.promedio())).append("\n\n");
            }
        }
        sb.append("--------------------------------\n");
        sb.append("Promedio General: ").append(String.format("%.2f", promedioGeneral())).append("\n");
        return sb.toString();
    }

    @Override
    public String obtenerDescripcionCompleta() {
        return super.obtenerDescripcionCompleta() + (getCurso() != null ? ", Curso: " + getCurso().toString() : ", Sin curso asignado");
    }

    @Override
    public String obtenerResumen() {
        return super.obtenerResumen() + (getCurso() != null ? " - " + getCurso().toString() : "");
    }
}
