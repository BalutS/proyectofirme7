package com.modelo;

import com.dao.AsignaturaDAO;
import com.dao.CursoDAO;
import com.dao.EstudianteDAO;
import com.dao.ProfesorDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator; 

/**
 *
 * @author river
 */
public class Colegio {
    private static Colegio instancia;
    private String nombre;
    private final EstudianteDAO estudianteDAO;
    private final ProfesorDAO profesorDAO;
    private final CursoDAO cursoDAO;
    private final AsignaturaDAO asignaturaDAO;

    private Colegio(String nombre) {
        this.nombre = nombre;
        this.estudianteDAO = new EstudianteDAO();
        this.profesorDAO = new ProfesorDAO();
        this.cursoDAO = new CursoDAO();
        this.asignaturaDAO = new AsignaturaDAO();
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
    
    public void agregarEstudiante(Estudiante est) throws IOException, ClassNotFoundException {
        if (est != null) { // Basic null check, DAO handles actual persistence
            estudianteDAO.guardarEstudiante(est);
        }
    }
    
    public void agregarProfesor(Profesor prof) throws IOException, ClassNotFoundException {
        if (prof != null) { // Basic null check
            profesorDAO.guardarProfesor(prof);
        }
    }
    
    public void agregarCurso(Curso curso) throws IOException, ClassNotFoundException {
        if (curso != null) { // Basic null check
            cursoDAO.guardarCurso(curso);
        }
    }
    
    public void agregarAsignatura(Asignatura asig) throws IOException, ClassNotFoundException {
        if (asig != null) { // Basic null check
            asignaturaDAO.guardarAsignatura(asig);
        }
    }
    
    public void agregarCursoAProfesor(int codigoProfesor, int gradoCurso, int grupoCurso) throws IOException, ClassNotFoundException {
        Profesor prof = buscarProfesor(codigoProfesor); // Already uses DAO
        Curso curso = buscarCurso(gradoCurso, grupoCurso);   // Already uses DAO

        if (prof != null && curso != null) {
            // Set the relationship
            prof.setCurso(curso);
            curso.setProfesor(prof);

            // Save the updated Profesor
            ArrayList<Profesor> todosProfesores = profesorDAO.listarProfesores();
            boolean profesorActualizado = false;
            for (int i = 0; i < todosProfesores.size(); i++) {
                if (todosProfesores.get(i).getCodigo() == prof.getCodigo()) {
                    todosProfesores.set(i, prof); // prof is the modified object
                    profesorActualizado = true;
                    break;
                }
            }
            if (profesorActualizado) {
                profesorDAO.actualizarListaProfesores(todosProfesores);
            }

            // Save the updated Curso
            ArrayList<Curso> todosCursos = cursoDAO.listarCursos();
            boolean cursoActualizado = false;
            for (int i = 0; i < todosCursos.size(); i++) {
                if (todosCursos.get(i).getGrado() == curso.getGrado() && todosCursos.get(i).getGrupo() == curso.getGrupo()) {
                    todosCursos.set(i, curso); // curso is the modified object
                    cursoActualizado = true;
                    break;
                }
            }
            if (cursoActualizado) {
                cursoDAO.actualizarListaCursos(todosCursos);
            }
        }
    }
    
    public void agregarEstudianteACurso(int codigoEstudiante, int gradoCurso, int grupoCurso) throws IOException, ClassNotFoundException {
        Estudiante est = buscarEstudiante(codigoEstudiante);
        Curso curso = buscarCurso(gradoCurso, grupoCurso);

        if (est != null && curso != null) {
            boolean courseNeedsSave = false;
            if (curso.getEstudiantes() == null) {
                curso.setEstudiantes(new ArrayList<>());
                // courseNeedsSave = true; // Initializing to empty list doesn't change persisted state yet
            }
            
            boolean alreadyEnrolled = false;
            for (Estudiante e : curso.getEstudiantes()) {
                if (e.getCodigo() == est.getCodigo()) {
                    alreadyEnrolled = true;
                    break;
                }
            }
            
            if (!alreadyEnrolled) {
                curso.getEstudiantes().add(est);
                courseNeedsSave = true; // Course's student list has changed
            }

            boolean studentNeedsSave = false;
            // Check if the student's course reference needs updating or is being set for the first time
            if (est.getCurso() == null || 
                !(est.getCurso().getGrado() == curso.getGrado() && est.getCurso().getGrupo() == curso.getGrupo()) ||
                !alreadyEnrolled) { // If added to course, ensure student's side is also updated/saved
                est.setCurso(curso);
                studentNeedsSave = true;
            }
            
            // Only save if changes were made that affect persistence
            if (studentNeedsSave) {
                this.guardarCambiosEstudiante(est);
            }
            
            if (courseNeedsSave) {
                // If the student was added to the course, the course object changed.
                // Also, if the student's course reference was updated TO THIS COURSE,
                // and this course object itself didn't have its student list changed (because student was already in it),
                // we might not need to save the course.
                // However, if 'est.setCurso(curso)' happened, and 'est' is now part of 'curso.getEstudiantes()',
                // then 'curso' object (which holds references) might be considered changed.
                // The original code saved both if est and curso were not null.
                // Let's ensure course is saved if its student list changed.
                this.guardarCambiosCurso(curso);
            } else if (studentNeedsSave && est.getCurso().equals(curso)) {
                // This case: student's course was set to this 'curso', but 'curso's student list didn't change
                // (e.g. student was already in the list). We might not need to save 'curso' again.
                // But, if 'curso.getEstudiantes().add(est)' did not happen,
                // and only 'est.setCurso(curso)' happened, then only student needs saving.
                // The current logic for courseNeedsSave only true if student added to list.
                // This is fine. If only est.setCurso() changed, only student is saved.
                // If student added to curso.getEstudiantes(), curso is saved.
            }
        }
    }
    
    public void agregarAsignaturaAEstudiante(int codigoEstudiante, String nombreAsignatura) throws IOException, ClassNotFoundException {
        Estudiante est = buscarEstudiante(codigoEstudiante); // Already uses DAO
        Asignatura asig = buscarAsignatura(nombreAsignatura); // Already uses DAO

        if (est != null && asig != null) {
            // Initialize asignatura list in estudiante if null
            if (est.getAsignaturas() == null) {
                est.setAsignaturas(new ArrayList<>());
            }
            // Add asignatura to student's list, avoid duplicates
            boolean alreadyHasAsignatura = false;
            for(Asignatura currentAsig : est.getAsignaturas()){
                if(currentAsig.getNombre().equalsIgnoreCase(asig.getNombre())){
                    alreadyHasAsignatura = true;
                    break;
                }
            }
            if(!alreadyHasAsignatura){
                 est.getAsignaturas().add(asig);
            }

            // Save the updated Estudiante
            ArrayList<Estudiante> todosEstudiantes = estudianteDAO.listarEstudiantes();
            boolean estudianteActualizado = false;
            for (int i = 0; i < todosEstudiantes.size(); i++) {
                if (todosEstudiantes.get(i).getCodigo() == est.getCodigo()) {
                    todosEstudiantes.set(i, est); // est is the modified object
                    estudianteActualizado = true;
                    break;
                }
            }
            if (estudianteActualizado) {
                estudianteDAO.actualizarListaEstudiantes(todosEstudiantes);
            }
            // Asignatura object itself is not changed by this operation in terms of its own persisted state.
        }
    }

    /**
     * Updates an existing Estudiante object in the persisted list.
     * If the student is found in the current list, it's replaced with the provided 'est' object.
     * Then, the entire list is saved back to the data file.
     * @param est The Estudiante object with updated information.
     * @throws IOException If an I/O error occurs during reading or writing.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found during reading.
     */
    public void guardarCambiosEstudiante(Estudiante est) throws IOException, ClassNotFoundException {
        if (est == null) {
            System.err.println("Error: Estudiante a guardar no puede ser null.");
            return;
        }
        ArrayList<Estudiante> todosEstudiantes = estudianteDAO.listarEstudiantes();
        boolean found = false;
        for (int i = 0; i < todosEstudiantes.size(); i++) {
            if (todosEstudiantes.get(i).getCodigo() == est.getCodigo()) {
                todosEstudiantes.set(i, est); // Replace with the modified student object
                found = true;
                break;
            }
        }
        if (!found) {
            // This case implies 'est' was not from the original list.
            // For 'guardarCambios', a warning if not found is appropriate.
            System.err.println("Advertencia: Estudiante con código " + est.getCodigo() + " no encontrado en la lista para actualizar. Los cambios no se guardarán para este estudiante si era nuevo.");
            // If this method were an "addOrUpdate", then 'todosEstudiantes.add(est);' might be here.
            // But given the name, it's an update.
        }
        // Always save the list; if not found, original list is saved. If found, updated list is saved.
        estudianteDAO.actualizarListaEstudiantes(todosEstudiantes);
    }

    /**
     * Updates an existing Curso object in the persisted list.
     * If the curso is found in the current list, it's replaced with the provided 'curso' object.
     * Then, the entire list is saved back to the data file.
     * @param curso The Curso object with updated information.
     * @throws IOException If an I/O error occurs during reading or writing.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found during reading.
     */
    public void guardarCambiosCurso(Curso curso) throws IOException, ClassNotFoundException {
        if (curso == null) {
            System.err.println("Error: Curso a guardar no puede ser null.");
            return;
        }
        ArrayList<Curso> todosCursos = cursoDAO.listarCursos();
        boolean found = false;
        for (int i = 0; i < todosCursos.size(); i++) {
            Curso currentCurso = todosCursos.get(i);
            if (currentCurso.getGrado() == curso.getGrado() && currentCurso.getGrupo() == curso.getGrupo()) {
                todosCursos.set(i, curso); // Replace with the modified curso object
                found = true;
                break;
            }
        }
        if (!found) {
            System.err.println("Advertencia: Curso " + curso.getGrado() + "-" + curso.getGrupo() + " no encontrado en la lista para actualizar.");
        }
        cursoDAO.actualizarListaCursos(todosCursos);
    }
    
    // Methods to retrieve lists of entities directly from DAOs
    public ArrayList<Curso> listarCursos() throws IOException, ClassNotFoundException {
        return cursoDAO.listarCursos();
    }

    public ArrayList<Asignatura> listarAsignaturas() throws IOException, ClassNotFoundException {
        return asignaturaDAO.listarAsignaturas();
    }

    public ArrayList<Estudiante> listarEstudiantes() throws IOException, ClassNotFoundException {
        return estudianteDAO.listarEstudiantes();
    }

    public ArrayList<Profesor> listarProfesores() throws IOException, ClassNotFoundException {
        return profesorDAO.listarProfesores();
    }
    
    public Profesor buscarProfesor(int cod) throws IOException, ClassNotFoundException {
        ArrayList<Profesor> profesores = profesorDAO.listarProfesores();
        for (Profesor profesor : profesores) {
            if (profesor.getCodigo() == cod) {
                return profesor;
            }
        }
        return null;
    }
    
    public Estudiante buscarEstudiante(int cod) throws IOException, ClassNotFoundException {
        ArrayList<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getCodigo() == cod) {
                return estudiante;
            }
        }
        return null;
    }
    
    public Curso buscarCurso(int grado, int grupo) throws IOException, ClassNotFoundException {
        ArrayList<Curso> cursosList = cursoDAO.listarCursos();
        for (Curso curso : cursosList) {
            if (curso.getGrado() == grado && curso.getGrupo() == grupo) {
                return curso;
            }
        }
        return null;
    }
    
    public Asignatura buscarAsignatura(String nombre) throws IOException, ClassNotFoundException {
        ArrayList<Asignatura> asignaturasList = asignaturaDAO.listarAsignaturas();
        for (Asignatura asignatura : asignaturasList) {
            if (asignatura.getNombre().equalsIgnoreCase(nombre)) {
                return asignatura;
            }
        }
        return null;
    }
    
    public String reporteEstudiante(int codigo) throws IOException, ClassNotFoundException {
        Estudiante est = buscarEstudiante(codigo);
        if (est != null) {
            return est.reporteAcademico();
        }
        return "Estudiante no encontrado.";
    }
    
    public String infoCurso(int grado, int grupo) throws IOException, ClassNotFoundException {
        Curso curso = buscarCurso(grado, grupo);
        if (curso != null) {
            return curso.infoCurso();
        }
        return "Curso no encontrado.";
    }
    
    public String listarTodosLosCursos() throws IOException, ClassNotFoundException {
        ArrayList<Curso> cursosList = cursoDAO.listarCursos();
        StringBuilder sb = new StringBuilder();
        if (cursosList.isEmpty()) {
            return "No hay cursos registrados.";
        }
        for (Curso curso : cursosList) {
            sb.append(curso.infoCurso()).append("\n\n");
        }
        return sb.toString();
    }

    public String listarTodosLosProfesores() throws IOException, ClassNotFoundException {
        ArrayList<Profesor> profesoresList = profesorDAO.listarProfesores();
        StringBuilder sb = new StringBuilder();
        sb.append("--- LISTA DE DOCENTES ---\n");
        if (profesoresList.isEmpty()) {
            sb.append("No hay docentes registrados.\n");
        } else {
            for (Profesor prof : profesoresList) {
                sb.append(prof.toString()).append("\n\n"); 
            }
        }
        return sb.toString();
    }

    public String listarTodosLosEstudiantes() throws IOException, ClassNotFoundException {
        ArrayList<Estudiante> estudiantesList = estudianteDAO.listarEstudiantes();
        StringBuilder sb = new StringBuilder();
        sb.append("--- LISTA DE ESTUDIANTES ---\n");
        if (estudiantesList.isEmpty()) {
            sb.append("No hay estudiantes registrados.\n");
        } else {
            for (Estudiante est : estudiantesList) {
                sb.append(est.toString()).append("\n\n"); 
            }
        }
        return sb.toString();
    }

    public boolean eliminarEstudiante(int codigo) throws IOException, ClassNotFoundException {
        ArrayList<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
        Estudiante estudianteAEliminar = null;
        int indexEstudianteAEliminar = -1;

        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getCodigo() == codigo) {
                estudianteAEliminar = estudiantes.get(i);
                indexEstudianteAEliminar = i;
                break;
            }
        }

        if (estudianteAEliminar != null) {
            // Handle relationship with Curso
            Curso cursoDelEstudiante = estudianteAEliminar.getCurso();
            if (cursoDelEstudiante != null) {
                ArrayList<Curso> todosLosCursos = cursoDAO.listarCursos();
                boolean cursoModificado = false;
                for (Curso curso : todosLosCursos) {
                    if (curso.getGrado() == cursoDelEstudiante.getGrado() && curso.getGrupo() == cursoDelEstudiante.getGrupo()) {
                        // Remove student from this specific course's list
                        if (curso.getEstudiantes() != null) {
                            Iterator<Estudiante> it = curso.getEstudiantes().iterator();
                            while(it.hasNext()){
                                if(it.next().getCodigo() == codigo){
                                    it.remove();
                                    cursoModificado = true;
                                    break; 
                                }
                            }
                        }
                        break; // Found the course, no need to check others
                    }
                }
                if (cursoModificado) {
                    cursoDAO.actualizarListaCursos(todosLosCursos);
                }
            }

            estudiantes.remove(indexEstudianteAEliminar);
            estudianteDAO.actualizarListaEstudiantes(estudiantes);
            return true;
        }
        return false;
    }

    public boolean eliminarProfesor(int codigo) throws IOException, ClassNotFoundException {
        ArrayList<Profesor> profesores = profesorDAO.listarProfesores();
        Profesor profesorAEliminar = null;
        int indexProfesorAEliminar = -1;

        for (int i = 0; i < profesores.size(); i++) {
            if (profesores.get(i).getCodigo() == codigo) {
                profesorAEliminar = profesores.get(i);
                indexProfesorAEliminar = i;
                break;
            }
        }

        if (profesorAEliminar != null) {
            // Handle relationship with Curso
            Curso cursoDelProfesor = profesorAEliminar.getCurso();
            if (cursoDelProfesor != null) {
                ArrayList<Curso> todosLosCursos = cursoDAO.listarCursos();
                boolean cursoModificado = false;
                for (Curso curso : todosLosCursos) {
                    // Assuming Curso's equals method or direct comparison is reliable,
                    // or compare by unique properties like grado and grupo.
                    if (curso.getGrado() == cursoDelProfesor.getGrado() && curso.getGrupo() == cursoDelProfesor.getGrupo()) {
                        if (curso.getProfesor() != null && curso.getProfesor().getCodigo() == codigo) {
                            curso.setProfesor(null);
                            cursoModificado = true;
                            break; 
                        }
                    }
                }
                if (cursoModificado) {
                    cursoDAO.actualizarListaCursos(todosLosCursos);
                }
            }

            profesores.remove(indexProfesorAEliminar);
            profesorDAO.actualizarListaProfesores(profesores);
            return true;
        }
        return false;
    }

    public boolean eliminarCurso(int grado, int grupo) throws IOException, ClassNotFoundException {
        ArrayList<Curso> cursos = cursoDAO.listarCursos();
        Curso cursoAEliminar = null;
        int indexCursoAEliminar = -1;

        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).getGrado() == grado && cursos.get(i).getGrupo() == grupo) {
                cursoAEliminar = cursos.get(i);
                indexCursoAEliminar = i;
                break;
            }
        }

        if (cursoAEliminar != null) {
            // Update students who were in this course
            ArrayList<Estudiante> todosLosEstudiantes = estudianteDAO.listarEstudiantes();
            boolean estudiantesModificados = false;
            for (Estudiante estudiante : todosLosEstudiantes) {
                if (estudiante.getCurso() != null && 
                    estudiante.getCurso().getGrado() == grado && 
                    estudiante.getCurso().getGrupo() == grupo) {
                    estudiante.setCurso(null);
                    estudiantesModificados = true;
                }
            }
            if (estudiantesModificados) {
                estudianteDAO.actualizarListaEstudiantes(todosLosEstudiantes);
            }

            // Update professor who was assigned to this course
            if (cursoAEliminar.getProfesor() != null) {
                Profesor profesorDelCurso = cursoAEliminar.getProfesor();
                ArrayList<Profesor> todosLosProfesores = profesorDAO.listarProfesores();
                boolean profesorModificado = false;
                for (Profesor profesor : todosLosProfesores) {
                    if (profesor.getCodigo() == profesorDelCurso.getCodigo()) {
                         if (profesor.getCurso() != null && 
                             profesor.getCurso().getGrado() == grado && 
                             profesor.getCurso().getGrupo() == grupo) {
                            profesor.setCurso(null);
                            profesorModificado = true;
                            break; 
                        }
                    }
                }
                if (profesorModificado) {
                    profesorDAO.actualizarListaProfesores(todosLosProfesores);
                }
            }

            cursos.remove(indexCursoAEliminar);
            cursoDAO.actualizarListaCursos(cursos);
            return true;
        }
        return false;
    }

    public boolean eliminarAsignatura(String nombreAsignatura) throws IOException, ClassNotFoundException {
        ArrayList<Asignatura> asignaturas = asignaturaDAO.listarAsignaturas();
        Asignatura asignaturaAEliminar = null;
        int indexAsignaturaAEliminar = -1;

        for (int i = 0; i < asignaturas.size(); i++) {
            if (asignaturas.get(i).getNombre().equalsIgnoreCase(nombreAsignatura)) {
                asignaturaAEliminar = asignaturas.get(i);
                indexAsignaturaAEliminar = i;
                break;
            }
        }

        if (asignaturaAEliminar != null) {
            // Update Courses
            ArrayList<Curso> todosLosCursos = cursoDAO.listarCursos();
            boolean cursosModificados = false;
            for (Curso curso : todosLosCursos) {
                if (curso.getAsignaturas() != null) {
                    if (curso.getAsignaturas().removeIf(asig -> asig.getNombre().equalsIgnoreCase(nombreAsignatura))) {
                        cursosModificados = true;
                    }
                }
            }
            if (cursosModificados) {
                cursoDAO.actualizarListaCursos(todosLosCursos);
            }

            // Update Students
            ArrayList<Estudiante> todosLosEstudiantes = estudianteDAO.listarEstudiantes();
            boolean estudiantesModificados = false;
            for (Estudiante estudiante : todosLosEstudiantes) {
                if (estudiante.getAsignaturas() != null) {
                    if (estudiante.getAsignaturas().removeIf(asig -> asig.getNombre().equalsIgnoreCase(nombreAsignatura))) {
                        estudiantesModificados = true;
                    }
                }
            }
            if (estudiantesModificados) {
                estudianteDAO.actualizarListaEstudiantes(todosLosEstudiantes);
            }

            asignaturas.remove(indexAsignaturaAEliminar);
            asignaturaDAO.actualizarListaAsignaturas(asignaturas);
            return true;
        }
        return false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
