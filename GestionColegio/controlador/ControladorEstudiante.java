package com.controlador;

import com.modelo.*;

/**
 *
 * @author SOTO PC
 */
public class ControladorEstudiante extends ControladorGeneral {
    private Estudiante estudiante;

    public ControladorEstudiante(Colegio colegio, int codigoEstudiante) {
        super(colegio);
        this.estudiante = colegio.buscarEstudiante(codigoEstudiante);
        if (this.estudiante == null) {
            System.err.println("Advertencia: Estudiante con código " + codigoEstudiante + " no encontrado.");
        }
    }

    public String verReporteAcademico() {
        if (estudiante == null) {
            return "Estudiante no encontrado o no inicializado.";
        }
        return estudiante.reporteAcademico();
    }

    public String verInfoCurso() {
        if (estudiante == null) {
            return "Estudiante no encontrado o no inicializado.";
        }
        if (estudiante.getCurso() == null) {
            return "El estudiante no está asignado a ningún curso.";
        }
        return estudiante.getCurso().infoCurso();
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }
}
