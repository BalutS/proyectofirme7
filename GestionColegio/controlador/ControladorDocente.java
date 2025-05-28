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
            // Si el profesor y su curso asignado existen, actualizar el curso a su estado más reciente
            if (this.profesor.getCurso() != null) {
                Curso cursoOriginalObsoleto = this.profesor.getCurso();
                // Obtener la ÚLTIMA versión de este curso desde la fuente central de verdad (cursos.dat a través de Colegio)
                Curso cursoActualizado = this.colegio.buscarCurso(cursoOriginalObsoleto.getGrado(), cursoOriginalObsoleto.getGrupo());
                
                if (cursoActualizado != null) {
                    // Reemplazar el objeto Curso (potencialmente) obsoleto en la instancia cargada del Profesor
                    // con el actualizado.
                    this.profesor.setCurso(cursoActualizado); 
                } else {
                    // El curso asignado al profesor no se encontró en cursos.dat. Esto es una inconsistencia.
                    System.err.println("Advertencia: El curso " + cursoOriginalObsoleto.getGrado() + "-" + cursoOriginalObsoleto.getGrupo() + 
                                       " asignado al profesor " + codigoProfesor + 
                                       " no fue encontrado en la base de datos de cursos. El profesor podría estar mostrando información de curso desactualizada o incorrecta.");
                    // No se establece this.profesor.setCurso(null) aquí para permitir la visualización de datos potencialmente obsoletos si el curso fue eliminado,
                    // pero las operaciones que requieren consistencia del curso podrían fallar más tarde. El registro de errores es importante.
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
            // Pasar el objeto Estudiante real al método del profesor
            this.profesor.calificarEstudiante(estudianteAcalificar, nombreAsignatura, nombreCalificacion, nota, periodo, fecha);
            
            // Persistir los cambios realizados en el estudiante
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
