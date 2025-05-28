package com.modelo;

import java.util.ArrayList;
import java.util.Iterator; 

/**
 *
 * @author river
 */
public class Colegio {
    private static Colegio instancia;
    private String nombre;
    private ArrayList<Persona> personas;
    private ArrayList<Curso> cursos;
    private ArrayList<Asignatura> asignaturas;

    private Colegio(String nombre) {
        this.nombre = nombre;
        personas = new ArrayList<>();
        cursos = new ArrayList<>();
        asignaturas = new ArrayList<>();
    }
  
    public static Colegio getInstance(String nombreColegio) {
        if (instancia == null) {
            instancia = new Colegio(nombreColegio);
        }
        return instancia;
    }
    
    public static Colegio getInstance() {
        if (instancia == null) {
            instancia = new Colegio("Nombre de Colegio por Defecto"); 
        }
        return instancia;
    }
    
    public void agregarEstudiante(Estudiante est){
        if (est != null && !personas.contains(est)) {
            personas.add(est);
        }
    }
    
    public void agregarProfesor(Profesor prof){
        if (prof != null && !personas.contains(prof)) {
            personas.add(prof);
        }
    }
    
    public void agregarCurso(Curso curso){
        if (curso != null && !cursos.contains(curso)) {
            cursos.add(curso);
        }
    }
    
    public void agregarAsignatura(Asignatura asig){
        if (asig != null && !asignaturas.contains(asig)) {
            asignaturas.add(asig);
        }
    }
    
    public void agregarCursoAProfesor(int codigo, int grado, int grupo){
        Profesor prof = buscarProfesor(codigo);
        Curso curso = buscarCurso(grado, grupo);
        if (prof != null && curso != null) {
            prof.setCurso(curso);
            curso.setProfesor(prof);
        }
    }
    
    public void agregarEstudianteACurso(int codigo, int grado, int grupo){
        Estudiante est = buscarEstudiante(codigo);
        Curso curso = buscarCurso(grado, grupo);
        if (est != null && curso != null) {
            if (curso.getEstudiantes() == null) {
                curso.setEstudiantes(new ArrayList<>());
            }
            curso.getEstudiantes().add(est);
            est.setCurso(curso);
        }
    }
    
    public void agregarAsignaturaAEstudiante(int codigo, String nombre){
        Estudiante est = buscarEstudiante(codigo);
        Asignatura asig = buscarAsignatura(nombre);
        if (est != null && asig != null) {
            if (est.getAsignaturas() == null) {
                est.setAsignaturas(new ArrayList<>());
            }
            est.getAsignaturas().add(asig);
        }
    }
    
    public Profesor buscarProfesor(int cod){
        for (Persona persona : personas) {
            if (persona instanceof Profesor && persona.getCodigo() == cod) {
                return (Profesor) persona;
            }
        }
        return null;
    }
    
    public Estudiante buscarEstudiante(int cod){
        for (Persona persona : personas) {
            if (persona instanceof Estudiante && persona.getCodigo() == cod) {
                return (Estudiante) persona;
            }
        }
        return null;
    }
    
    public Curso buscarCurso(int grado, int grupo){
        for (Curso curso : cursos) {
            if (curso.getGrado() == grado && curso.getGrupo() == grupo) {
                return curso;
            }
        }
        return null;
    }
    
    public Asignatura buscarAsignatura(String nombre){
        for (Asignatura asignatura : asignaturas) {
            if (asignatura.getNombre().equalsIgnoreCase(nombre)) {
                return asignatura;
            }
        }
        return null;
    }
    
    public String reporteEstudiante(int codigo){
        Estudiante est = buscarEstudiante(codigo);
        if (est != null) {
            return est.reporteAcademico();
        }
        return "Estudiante no encontrado.";
    }
    
    public String infoCurso(int grado, int grupo){
        Curso curso = buscarCurso(grado, grupo);
        if (curso != null) {
            return curso.infoCurso();
        }
        return "Curso no encontrado.";
    }
    
    public String listarTodosLosCursos(){
        StringBuilder sb = new StringBuilder();
        if (cursos.isEmpty()) {
            return "No hay cursos registrados.";
        }
        for (Curso curso : cursos) {
            sb.append(curso.infoCurso()).append("\n\n");
        }
        return sb.toString();
    }

    public String listarTodosLosProfesores() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- LISTA DE DOCENTES ---\n");
        boolean hayProfesores = false;
        for (Persona p : personas) {
            if (p instanceof Profesor) { 
                Profesor prof = (Profesor) p;
                sb.append(prof.toString()).append("\n\n"); 
                hayProfesores = true;
            }
        }
        if (!hayProfesores) {
            sb.append("No hay docentes registrados.\n");
        }
        return sb.toString();
    }

    public String listarTodosLosEstudiantes() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- LISTA DE ESTUDIANTES ---\n");
        boolean hayEstudiantes = false;
        for (Persona p : personas) {
            if (p instanceof Estudiante) { 
                Estudiante est = (Estudiante) p;
                sb.append(est.toString()).append("\n\n"); 
                hayEstudiantes = true;
            }
        }
        if (!hayEstudiantes) {
            sb.append("No hay estudiantes registrados.\n");
        }
        return sb.toString();
    }

    public boolean eliminarEstudiante(int codigo) {
        Iterator<Persona> iterator = personas.iterator();
        while (iterator.hasNext()) {
            Persona persona = iterator.next();
            if (persona instanceof Estudiante && persona.getCodigo() == codigo) {
                Estudiante estudiante = (Estudiante) persona;
                Curso cursoDelEstudiante = estudiante.getCurso();
                if (cursoDelEstudiante != null && cursoDelEstudiante.getEstudiantes() != null) {
                    cursoDelEstudiante.getEstudiantes().remove(estudiante);
                }
                iterator.remove(); 
                return true;
            }
        }
        return false;
    }

    public boolean eliminarProfesor(int codigo) {
        Iterator<Persona> iterator = personas.iterator();
        while (iterator.hasNext()) {
            Persona persona = iterator.next();
            if (persona instanceof Profesor && persona.getCodigo() == codigo) {
                Profesor profesor = (Profesor) persona;
                Curso cursoDelProfesor = profesor.getCurso();
                if (cursoDelProfesor != null) {
                    cursoDelProfesor.setProfesor(null);
                }
                iterator.remove(); 
                return true;
            }
        }
        return false;
    }

    public boolean eliminarCurso(int grado, int grupo) {
        Iterator<Curso> iterator = cursos.iterator();
        while (iterator.hasNext()) {
            Curso curso = iterator.next();
            if (curso.getGrado() == grado && curso.getGrupo() == grupo) {
                // Unassign students from this course
                if (curso.getEstudiantes() != null) {
                    for (Estudiante estudiante : curso.getEstudiantes()) {
                        estudiante.setCurso(null);
                    }
                    curso.getEstudiantes().clear(); 
                }
                // Unassign professor from this course
                if (curso.getProfesor() != null) {
                    curso.getProfesor().setCurso(null);
                    curso.setProfesor(null);
                }
                iterator.remove(); 
                return true;
            }
        }
        return false;
    }

    public boolean eliminarAsignatura(String nombreAsignatura) {
        Asignatura asignaturaAEliminar = null;
        Iterator<Asignatura> iteratorAsignaturas = asignaturas.iterator();
        while (iteratorAsignaturas.hasNext()) {
            Asignatura currentAsignatura = iteratorAsignaturas.next();
            if (currentAsignatura.getNombre().equalsIgnoreCase(nombreAsignatura)) {
                asignaturaAEliminar = currentAsignatura;
                iteratorAsignaturas.remove(); 
                break; 
            }
        }

        if (asignaturaAEliminar == null) {
            return false; 
        }

        // Remove from all Courses
        for (Curso curso : cursos) {
            if (curso.getAsignaturas() != null) {
                curso.getAsignaturas().removeIf(asig -> asig.getNombre().equalsIgnoreCase(nombreAsignatura));
            }
        }

        // Remove from all Students
        for (Persona persona : personas) {
            if (persona instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) persona;
                if (estudiante.getAsignaturas() != null) {
                    estudiante.getAsignaturas().removeIf(asig -> asig.getNombre().equalsIgnoreCase(nombreAsignatura));
                }
            }
        }
        return true; 
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(ArrayList<Persona> personas) {
        this.personas = personas;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList<Curso> cursos) {
        this.cursos = cursos;
    }

    public ArrayList<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(ArrayList<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
