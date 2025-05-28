package com.controlador;

import com.modelo.*;
import java.time.LocalDate; 
import java.util.List; 

/**
 *
 * @author SOTO PC
 */
public class ControladorDocente extends ControladorGeneral {
    private Profesor profesor;

    public ControladorDocente(Colegio colegio, int codigoProfesor) {
        super(colegio);
        this.profesor = colegio.buscarProfesor(codigoProfesor);
        if (this.profesor == null) {
            System.err.println("Advertencia: Profesor con código " + codigoProfesor + " no encontrado.");
        }
    }

    public void calificarEstudiante(int codEst, String nombreAsignatura, String nombreCalificacion, float nota, int periodo, LocalDate fecha) {
        if (profesor != null) {
            try {
                profesor.calificarEstudiante(codEst, nombreAsignatura, nombreCalificacion, nota, periodo, fecha);
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.err.println("Error al calificar: " + e.getMessage());
            }
        } else {
            System.err.println("Error: Profesor no inicializado en ControladorDocente.");
        }
    }

    public String listarEstudiantesEnCurso() {
        if (profesor == null) {
            return "Profesor no asignado o no encontrado.";
        }
        if (profesor.getCurso() == null) {
            return "El profesor no tiene un curso asignado.";
        }
        return profesor.listarEstudiantes(); 
    }

    public String verInfoCurso() {
        if (profesor == null) {
            return "Profesor no asignado o no encontrado.";
        }
        if (profesor.getCurso() == null) {
            return "El profesor no tiene un curso asignado.";
        }
        return profesor.getCurso().infoCurso(); 
    }

    public Profesor getProfesor() {
        return profesor;
    }
}
