package com.vista;

import com.controlador.ControladorDocente;
import com.modelo.Curso;
import com.modelo.Estudiante;
import com.modelo.Asignatura;
import com.modelo.Profesor;
import com.modelo.Colegio; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; 
import java.util.ArrayList;

/**
 *
 * @author SOTO PC
 */
public class MenuDocente extends javax.swing.JFrame {
    private ControladorDocente controlador;
    private JTextArea areaInfoCursoEstudiantes;
    private JComboBox<Estudiante> cmbEstudiantes;
    private JComboBox<Asignatura> cmbAsignaturas;
    private JButton btnAgregarCalificacion;
    private JButton btnCerrarSesion;


    public MenuDocente(ControladorDocente controlador) {
        this.controlador = controlador;
        initComponents(); 
        configurarVentana(); 
        cargarDatosProfesor(); 
        configurarEventos(); 
    }

    private void initComponents() {
        areaInfoCursoEstudiantes = new JTextArea();
        areaInfoCursoEstudiantes.setEditable(false);
        areaInfoCursoEstudiantes.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPaneInfo = new JScrollPane(areaInfoCursoEstudiantes);

        cmbEstudiantes = new JComboBox<>();
        cmbAsignaturas = new JComboBox<>();
        btnAgregarCalificacion = new JButton("Agregar Calificación");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        JLabel lblEstudiante = new JLabel("Estudiante:");
        JLabel lblAsignatura = new JLabel("Asignatura:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Horizontal Group
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblEstudiante)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18)
                .addComponent(lblAsignatura)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbAsignaturas, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18)
                .addComponent(btnAgregarCalificacion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)) 
            .addComponent(scrollPaneInfo) 
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup() 
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrarSesion))
        );
        


        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblEstudiante)
                .addComponent(cmbEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblAsignatura)
                .addComponent(cmbAsignaturas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnAgregarCalificacion))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) 
            .addComponent(scrollPaneInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE) 
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) 
            .addComponent(btnCerrarSesion)
        );
        
        pack(); 
    }

    private void configurarVentana() {
        if (controlador.getProfesor() != null) {
            setTitle("Menú Docente - " + controlador.getProfesor().getNombre());
        } else {
            setTitle("Menú Docente - Profesor no encontrado");
        }
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
    }

    private void cargarDatosProfesor() {
        Profesor profesor = controlador.getProfesor();

        if (profesor == null) {
            areaInfoCursoEstudiantes.setText("Error: No se pudo cargar la información del profesor.");
            actualizarEstadoControlesAsignatura();
            return;
        }

        Curso curso = profesor.getCurso();
        areaInfoCursoEstudiantes.setText("Profesor: " + profesor.getInfoCompleta() + "\n\n");

        if (curso == null) {
            areaInfoCursoEstudiantes.append("No tiene un curso asignado actualmente.");
            actualizarEstadoControlesAsignatura();
        } else {
            areaInfoCursoEstudiantes.append("Curso Asignado: " + curso.toString() + "\n\n");
            areaInfoCursoEstudiantes.append("--- Estudiantes del Curso ---\n" + controlador.listarEstudiantesEnCurso());

            cmbEstudiantes.removeAllItems();
            if (curso.getEstudiantes() != null && !curso.getEstudiantes().isEmpty()) {
                for (Estudiante est : curso.getEstudiantes()) {
                    cmbEstudiantes.addItem(est);
                }
                if (cmbEstudiantes.getItemCount() > 0) {
                    cmbEstudiantes.setSelectedIndex(0);
                }
            } else {
                 areaInfoCursoEstudiantes.append("\nNo hay estudiantes matriculados en este curso.");
            }
            // Populate subjects for the teacher's course
            cmbAsignaturas.removeAllItems();
            if (profesor.getCurso() != null && profesor.getCurso().getASignaturas() != null && !profesor.getCurso().getASignaturas().isEmpty()) {
                for (Asignatura asig : profesor.getCurso().getASignaturas()) {
                    cmbAsignaturas.addItem(asig);
                }
            }
            actualizarEstadoControlesAsignatura();
        }
    }

    private void actualizarEstadoControlesAsignatura() {
        Profesor profesor = controlador.getProfesor();
        Curso curso = (profesor != null) ? profesor.getCurso() : null;
        Estudiante estudianteSeleccionado = (Estudiante) cmbEstudiantes.getSelectedItem();

        boolean habilitar = estudianteSeleccionado != null &&
                            curso != null &&
                            curso.getASignaturas() != null &&
                            !curso.getASignaturas().isEmpty();

        cmbAsignaturas.setEnabled(habilitar);
        btnAgregarCalificacion.setEnabled(habilitar);

        if (!habilitar) {
            cmbAsignaturas.removeAllItems();
        } else {
            // Ensure cmbAsignaturas is populated if it was cleared but should be enabled
            if (cmbAsignaturas.getItemCount() == 0 && curso != null && curso.getASignaturas() != null && !curso.getASignaturas().isEmpty()) {
                for (Asignatura asig : curso.getASignaturas()) {
                    cmbAsignaturas.addItem(asig);
                }
            }
        }
        
        // Disable student combobox if no students
        cmbEstudiantes.setEnabled(profesor != null && curso != null && curso.getEstudiantes() != null && !curso.getEstudiantes().isEmpty());

        // If no students, also disable subject related controls
        if (cmbEstudiantes.getItemCount() == 0) {
            cmbAsignaturas.setEnabled(false);
            btnAgregarCalificacion.setEnabled(false);
            cmbAsignaturas.removeAllItems();
        }
    }


    private void configurarEventos() {
        cmbEstudiantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEstadoControlesAsignatura();
            }
        });

        btnAgregarCalificacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Estudiante estSeleccionado = (Estudiante) cmbEstudiantes.getSelectedItem();
                Asignatura asigSeleccionada = (Asignatura) cmbAsignaturas.getSelectedItem();

                if (estSeleccionado == null || asigSeleccionada == null) {
                    JOptionPane.showMessageDialog(MenuDocente.this, 
                        "Debe seleccionar un estudiante y una asignatura.", 
                        "Selección Requerida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                FormularioCalificacion formCalificacion = new FormularioCalificacion(
                    MenuDocente.this, 
                    controlador,      
                    estSeleccionado,  
                    asigSeleccionada  
                );
                formCalificacion.setVisible(true);

            }
        });

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuDocente.this.dispose();
                Principal principal = new Principal(); 
                principal.setVisible(true);
            }
        });
        
        // Initial call to set component states correctly after everything is loaded.
        actualizarEstadoControlesAsignatura(); 
    }
}
