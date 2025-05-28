/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import dao.AlumnoDao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import modelo.Alumno;
import modelo.EdadException;

/**
 *
 * @author Unimagdalena
 */
public class ControlAlumno {
    private AlumnoDao aldao=new AlumnoDao();
    
    public void addAlumno(Alumno al) throws IOException, FileNotFoundException, ClassNotFoundException{
        aldao.guardarAlumo(al);
    }

    public void guardarAlumno(int codigo, String nombre, int edad, float prom) throws IOException, FileNotFoundException, ClassNotFoundException, EdadException {
        addAlumno(new Alumno(codigo, nombre, edad, prom));
    }
    
    public String listar() throws IOException, FileNotFoundException, ClassNotFoundException{
        ArrayList<Alumno> lista=aldao.listarAlumnos();
        String lis="";
        for (Alumno alumno : lista) {
            lis+=" "+alumno.toString()+"\n";
        }
        return lis;
    }
}

