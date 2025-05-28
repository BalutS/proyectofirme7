package com.vista;

import com.controlador.ControladorEstudiante;
import com.modelo.Estudiante; 
import com.modelo.Asignatura; 
import com.modelo.Calificacion; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author SOTO PC
 */
public class MenuEstudiante extends JFrame {
    private ControladorEstudiante controlador;
    private JTextArea areaReporte;
    private JButton btnVerMisNotas; 
    private JButton btnCerrarSesion;

    public MenuEstudiante(ControladorEstudiante controlador) {
        this.controlador = controlador;
        initComponents();
        configurarVentana();
        mostrarReporteAcademico(); 
        configurarEventos();
    }

    private void initComponents() {
        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPaneReporte = new JScrollPane(areaReporte);

        btnVerMisNotas = new JButton("Ver Todas Mis Notas Detalladas");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addComponent(scrollPaneReporte, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE) 
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) 
                .addComponent(btnVerMisNotas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerrarSesion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)) 
        );
        layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {btnVerMisNotas, btnCerrarSesion});


        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(scrollPaneReporte, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE) 
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) 
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnVerMisNotas)
                .addComponent(btnCerrarSesion))
        );
        
        pack(); 
    }

    private void configurarVentana() {
        if (controlador != null && controlador.getEstudiante() != null) {
            setTitle("Menú Estudiante - " + controlador.getEstudiante().getNombre());
        } else {
            setTitle("Menú Estudiante - Estudiante no encontrado");
        }
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void mostrarReporteAcademico() {
        if (controlador != null) {
            String reporte = controlador.verReporteAcademico();
            areaReporte.setText(reporte);
            areaReporte.setCaretPosition(0); 
        } else {
            areaReporte.setText("Error: Controlador no disponible.");
        }
    }
    
    private void mostrarTodasLasNotas() {
        if (controlador == null || controlador.getEstudiante() == null) {
            areaReporte.setText("No se pueden mostrar las notas: Estudiante no disponible.");
            return;
        }
        
        String detalleCompleto = controlador.getEstudiante().getDetalleCompletoCalificaciones();
        areaReporte.setText(detalleCompleto);
        areaReporte.setCaretPosition(0); 
    }


    private void configurarEventos() {
        btnVerMisNotas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTodasLasNotas(); 
            }
        });

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuEstudiante.this.dispose();
                new Principal().setVisible(true); 
            }
        });
    }
}
