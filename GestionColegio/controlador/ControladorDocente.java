package com.controlador;

import com.modelo.*;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.time.LocalDate; 
import java.util.List; 

/**
 *
 * @author SOTO PC
 */
public class ControladorDocente extends ControladorGeneral {
    private Profesor profesor;

    public ControladorDocente(Colegio colegio, int codigoProfesor) throws IOException, ClassNotFoundException {
        super(colegio);
        this.profesor = colegio.buscarProfesor(codigoProfesor); 

        if (this.profesor == null) {
            System.err.println("Advertencia: Profesor con código " + codigoProfesor + " no encontrado.");
        } else {
            // If professor and their assigned course exist, refresh the course to its latest state
            if (this.profesor.getCurso() != null) {
                Curso cursoOriginalStale = this.profesor.getCurso();
                // Fetch the LATEST version of this course from the central source of truth (cursos.dat via Colegio)
                Curso cursoActualizado = this.colegio.buscarCurso(cursoOriginalStale.getGrado(), cursoOriginalStale.getGrupo());
                
                if (cursoActualizado != null) {
                    // Replace the (potentially) stale Curso object in the loaded Profesor instance
                    // with the fresh one.
                    this.profesor.setCurso(cursoActualizado); 
                } else {
                    // The course assigned to the professor was not found in cursos.dat. This is an inconsistency.
                    System.err.println("Advertencia: El curso " + cursoOriginalStale.getGrado() + "-" + cursoOriginalStale.getGrupo() + 
                                       " asignado al profesor " + codigoProfesor + 
                                       " no fue encontrado en la base de datos de cursos. El profesor podría estar mostrando información de curso desactualizada o incorrecta.");
                    // Not setting this.profesor.setCurso(null) here to allow viewing of potentially stale data if course was deleted,
                    // but operations requiring course consistency might fail later. Error log is important.
                }
            }
        }
    }

    public void calificarEstudiante(int codEst, String nombreAsignatura, String nombreCalificacion, float nota, int periodo, LocalDate fecha) throws IOException, ClassNotFoundException {
        if (this.profesor == null) {
            System.err.println("Error: Profesor no inicializado en ControladorDocente.");
            return;
        }

        Estudiante estudianteAcalificar = colegio.buscarEstudiante(codEst); // Throws IOException, CNFE
        if (estudianteAcalificar == null) {
            System.err.println("Error: Estudiante con código " + codEst + " no encontrado.");
            return;
        }

        try {
            // Pass the actual Estudiante object to the professor's method
            this.profesor.calificarEstudiante(estudianteAcalificar, nombreAsignatura, nombreCalificacion, nota, periodo, fecha);
            
            // Persist the changes made to the student
            colegio.guardarCambiosEstudiante(estudianteAcalificar); // Throws IOException, CNFE

        } catch (IllegalStateException | IllegalArgumentException e) {
            System.err.println("Error al calificar: " + e.getMessage());
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
