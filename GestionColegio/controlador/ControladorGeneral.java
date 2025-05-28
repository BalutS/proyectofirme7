package com.controlador;

import com.modelo.Colegio;
// import com.modelo.Persona; // Persona is not directly used in the new autenticar
import java.io.IOException;
import java.lang.ClassNotFoundException;

/**
 *
 * @author SOTO PC
 */
public class ControladorGeneral {
    protected Colegio colegio;

    public ControladorGeneral(Colegio colegio) {
        this.colegio = colegio;
    }

    /**
     * Autentica a una persona (Estudiante o Profesor) en el sistema.
     * @param codigo El código de la persona.
     * @param tipo El tipo de persona ("Estudiante" o "Profesor").
     * @return true si la autenticación es exitosa, false en caso contrario.
     * @throws IOException If an I/O error occurs during data retrieval.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public boolean autenticar(int codigo, String tipo) throws IOException, ClassNotFoundException {
        if (colegio == null) {
            return false;
        }
        if ("Estudiante".equalsIgnoreCase(tipo)) {
            return colegio.buscarEstudiante(codigo) != null;
        } else if ("Profesor".equalsIgnoreCase(tipo)) { // "Profesor" was used in Principal.java
            return colegio.buscarProfesor(codigo) != null;
        }
        return false;
    }
}
