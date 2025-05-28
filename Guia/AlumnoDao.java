/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import modelo.Alumno;

/**
 *
 * @author Unimagdalena
 */
public class AlumnoDao {
    private String archivo="alumnos.dat";
    
    public void guardarAlumo(Alumno al) throws FileNotFoundException, IOException, ClassNotFoundException{
        File ojo=new File(archivo);
        ArrayList<Alumno> lista=null;
        if(ojo.exists())
          lista= listarAlumnos();
        else
          lista= new ArrayList<>();
        
        lista.add(al);
        
        FileOutputStream file=new FileOutputStream(archivo);
        ObjectOutputStream salida= new ObjectOutputStream(file);
        salida.writeObject(lista);
        salida.close();
        file.close();
    }

    public ArrayList<Alumno> listarAlumnos() throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream file= new FileInputStream(archivo);
        ObjectInputStream entrada=new ObjectInputStream(file);
        ArrayList<Alumno> lista=(ArrayList<Alumno>) entrada.readObject();
        file.close();
        entrada.close();
        return lista;
        
    }
}

