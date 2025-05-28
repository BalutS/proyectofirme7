package com.controlador;

import com.modelo.*;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.util.ArrayList; 
// Note: java.util.List might not be needed if methods return ArrayList directly

/**
 *
 * @author SOTO PC
 */
public class ControladorAdmin extends ControladorGeneral {

    public ControladorAdmin(Colegio colegio) {
        super(colegio);
    }

    public void agregarEstudiante(Estudiante estudiante) throws IOException, ClassNotFoundException {
        colegio.agregarEstudiante(estudiante);
    }

    public void agregarProfesor(Profesor profesor) throws IOException, ClassNotFoundException {
        colegio.agregarProfesor(profesor);
    }

    public Estudiante buscarEstudiante(int codigo) throws IOException, ClassNotFoundException {
        return colegio.buscarEstudiante(codigo);
    }

    public Profesor buscarProfesor(int codigo) throws IOException, ClassNotFoundException {
        return colegio.buscarProfesor(codigo);
    }

    public void agregarCurso(Curso curso) throws IOException, ClassNotFoundException {
        colegio.agregarCurso(curso);
    }

    public void asignarCursoAProfesor(int codProf, int grado, int grupo) throws IOException, ClassNotFoundException {
        colegio.agregarCursoAProfesor(codProf, grado, grupo);
    }
    
    public boolean existeCurso(int grado, int grupo) throws IOException, ClassNotFoundException {
        return colegio.listarCursos().stream()
            .anyMatch(c -> c.getGrado() == grado && c.getGrupo() == grupo);
    }
    
    public void asignarEstudianteACurso(int codEst, int grado, int grupo) throws IOException, ClassNotFoundException {
        colegio.agregarEstudianteACurso(codEst, grado, grupo);
    }

    public void agregarAsignatura(Asignatura asignatura) throws IOException, ClassNotFoundException { 
        colegio.agregarAsignatura(asignatura);
    }

    public void asignarAsignaturaAEstudiante(int codEst, String nombreAsig) throws IOException, ClassNotFoundException {
        colegio.agregarAsignaturaAEstudiante(codEst, nombreAsig);
    }

    public String generarReporteEstudiante(int codigo) throws IOException, ClassNotFoundException {
        return colegio.reporteEstudiante(codigo);
    }

    public String listarTodosLosCursos() throws IOException, ClassNotFoundException {
        return colegio.listarTodosLosCursos();
    }
    
    public String listarTodosLosProfesores() throws IOException, ClassNotFoundException {
        return colegio.listarTodosLosProfesores(); 
    }
    
    public String listarTodosLosEstudiantes() throws IOException, ClassNotFoundException {
        return colegio.listarTodosLosEstudiantes(); 
    }
    
    public ArrayList<Curso> getCursos() throws IOException, ClassNotFoundException { // Return type changed
        return colegio.listarCursos();
    }

    public ArrayList<Asignatura> getAsignaturas() throws IOException, ClassNotFoundException { // Return type changed
        return colegio.listarAsignaturas();
    }

    public boolean existeCodigoEstudiante(int codigo) throws IOException, ClassNotFoundException {
        return colegio.listarEstudiantes().stream()
            .anyMatch(p -> p.getCodigo() == codigo);
    }

    public void crearAsignatura(String nombreAsignatura, Curso cursoSeleccionadoOriginal) throws IOException, ClassNotFoundException {
        Asignatura nuevaAsignatura = new Asignatura(nombreAsignatura);
        // Persist the new asignatura first
        colegio.agregarAsignatura(nuevaAsignatura); 
    
        if (cursoSeleccionadoOriginal != null) {
            // Fetch the definitive, persisted version of the course to ensure we're working with up-to-date data
            Curso cursoAactualizar = colegio.buscarCurso(cursoSeleccionadoOriginal.getGrado(), cursoSeleccionadoOriginal.getGrupo());
            
            if (cursoAactualizar == null) {
                System.err.println("Error: El curso (" + cursoSeleccionadoOriginal.getGrado() + "-" + cursoSeleccionadoOriginal.getGrupo() + ") seleccionado originalmente ya no existe. La asignatura fue creada pero no asignada al curso.");
                return;
            }
    
            // Add asignatura to the fetched course object
            // The Asignatura class's agregarAsignatura method should handle null checks for its internal list
            // Assuming Curso.agregarAsignatura handles initialization of its Asignatura list
            cursoAactualizar.agregarAsignatura(nuevaAsignatura); 
            
            // Persist changes to the course (which now includes the new asignatura in its list)
            colegio.guardarCambiosCurso(cursoAactualizar);
    
            // Update all students in that course to include the new asignatura
            if (cursoAactualizar.getEstudiantes() != null && !cursoAactualizar.getEstudiantes().isEmpty()) {
                // Create a list of student IDs to avoid issues if the underlying student list of the course object is modified during iteration elsewhere.
                // For this loop, direct iteration should be fine as we fetch each student individually.
                ArrayList<Estudiante> estudiantesDelCurso = new ArrayList<>(cursoAactualizar.getEstudiantes()); // Iterate over a copy
    
                for (Estudiante estEnCurso : estudiantesDelCurso) {
                    // Fetch the definitive version of the student
                    Estudiante estudianteParaActualizar = colegio.buscarEstudiante(estEnCurso.getCodigo());
                    
                    if (estudianteParaActualizar != null) {
                        if (estudianteParaActualizar.getAsignaturas() == null) {
                            estudianteParaActualizar.setAsignaturas(new ArrayList<>());
                        }
                        // Add asignatura to student if not already present
                        boolean alreadyHasAsignatura = false;
                        for(Asignatura asig : estudianteParaActualizar.getAsignaturas()){
                            if(asig.getNombre().equalsIgnoreCase(nuevaAsignatura.getNombre())){
                                alreadyHasAsignatura = true;
                                break;
                            }
                        }
                        if(!alreadyHasAsignatura){
                             estudianteParaActualizar.getAsignaturas().add(nuevaAsignatura);
                             colegio.guardarCambiosEstudiante(estudianteParaActualizar); // Persist changes to student
                        }
                    } else {
                         System.err.println("Advertencia: Estudiante con código " + estEnCurso.getCodigo() + 
                                            " (listado en curso " + cursoAactualizar.getGrado() + "-" + cursoAactualizar.getGrupo() + 
                                            ") no fue encontrado. No se pudo agregar la asignatura '" + 
                                            nuevaAsignatura.getNombre() + "' a este estudiante.");
                    }
                }
            }
        }
    }

    public boolean existeAsignaturaGlobalmente(String nombreAsignatura) throws IOException, ClassNotFoundException {
        return colegio.listarAsignaturas().stream()
            .anyMatch(asig -> asig.getNombre().equalsIgnoreCase(nombreAsignatura));
    }

    public boolean eliminarEstudiante(int codigo) throws IOException, ClassNotFoundException {
        return colegio.eliminarEstudiante(codigo);
    }

    public boolean eliminarProfesor(int codigo) throws IOException, ClassNotFoundException {
        return colegio.eliminarProfesor(codigo);
    }

    public boolean eliminarCurso(int grado, int grupo) throws IOException, ClassNotFoundException {
        return colegio.eliminarCurso(grado, grupo);
    }

    public boolean eliminarAsignatura(String nombreAsignatura) throws IOException, ClassNotFoundException {
        return colegio.eliminarAsignatura(nombreAsignatura);
    }
}
