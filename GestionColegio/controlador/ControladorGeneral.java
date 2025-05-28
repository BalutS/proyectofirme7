package com.controlador;

import com.modelo.Colegio;
import com.modelo.Persona; 

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
     */
    public boolean autenticar(int codigo, String tipo) {
        if (colegio == null || colegio.getPersonas() == null) {
            return false; 
        }
        for (Persona p : colegio.getPersonas()) {
            if (p.getCodigo() == codigo && p.getTipo().equalsIgnoreCase(tipo)) {
                return true;
            }
        }
        return false;
    }
}
