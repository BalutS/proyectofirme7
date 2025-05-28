package com.dao;

import com.modelo.Estudiante;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Manages data persistence for Estudiante objects.
 * @author YourName // Replace with actual author or leave as auto-generated
 */
public class EstudianteDAO {
    private String archivo = "estudiantes.dat";

    /**
     * Saves a new Estudiante to the data file.
     * @param est The Estudiante object to save.
     * @throws IOException If an I/O error occurs during writing.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public void guardarEstudiante(Estudiante est) throws IOException, ClassNotFoundException {
        File fileHandle = new File(archivo);
        ArrayList<Estudiante> lista;

        if (fileHandle.exists() && fileHandle.length() > 0) { // Check if file exists and is not empty
            FileInputStream fis = new FileInputStream(fileHandle);
            ObjectInputStream ois = new ObjectInputStream(fis);
            lista = (ArrayList<Estudiante>) ois.readObject();
            ois.close();
            fis.close();
        } else {
            lista = new ArrayList<>();
        }
        
        lista.add(est);
        
        FileOutputStream fos = new FileOutputStream(archivo);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(lista);
        oos.close();
        fos.close();
    }

    /**
     * Retrieves all Estudiante objects from the data file.
     * @return An ArrayList of Estudiante objects.
     * @throws IOException If an I/O error occurs during reading (except FileNotFoundException).
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public ArrayList<Estudiante> listarEstudiantes() throws IOException, ClassNotFoundException {
        File fileHandle = new File(archivo);
        
        if (!fileHandle.exists() || fileHandle.length() == 0) { // Check if file doesn't exist or is empty
            return new ArrayList<>(); // Return empty list if file not found or empty
        }
        
        FileInputStream fis = new FileInputStream(archivo);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Estudiante> lista = (ArrayList<Estudiante>) ois.readObject();
        ois.close();
        fis.close();
        return lista;
    }

    /**
     * Overwrites the existing student data file with the provided list of students.
     * @param estudiantes The new list of students to save.
     * @throws IOException If an I/O error occurs during writing.
     */
    public void actualizarListaEstudiantes(ArrayList<Estudiante> estudiantes) throws IOException {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(archivo))) {
            salida.writeObject(estudiantes);
        }
    }
}
