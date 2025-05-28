package com.vista;

import com.controlador.ControladorDocente;
import com.modelo.Estudiante;
import com.modelo.Asignatura;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author SOTO PC
 */
public class FormularioCalificacion extends JDialog {
    private ControladorDocente controladorDocente;
    private Estudiante estudiante;
    private Asignatura asignatura;

    private JTextField txtNombreCalificacion;
    private JTextField txtNota;
    private JTextField txtPeriodo;
    private JTextField txtFecha; 
    private JButton btnGuardar;
    private JButton btnCancelar;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public FormularioCalificacion(Frame parent, ControladorDocente controladorDocente, Estudiante estudiante, Asignatura asignatura) {
        super(parent, "Agregar Calificación - " + estudiante.getNombre() + " - " + asignatura.getNombre(), true);
        this.controladorDocente = controladorDocente;
        this.estudiante = estudiante;
        this.asignatura = asignatura;

        initComponents();
        configurarEventos();
        pack();
        setLocationRelativeTo(parent);
        setResizable(false); 
    }

    private void initComponents() {
        txtNombreCalificacion = new JTextField(20);
        txtNota = new JTextField(5);
        txtPeriodo = new JTextField(5);
        txtFecha = new JTextField(10);
        txtFecha.setText(LocalDate.now().format(DATE_FORMATTER)); 
        
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        JLabel lblNombreActividad = new JLabel("Nombre Actividad:");
        lblNombreActividad.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel lblNota = new JLabel("Nota (0.0 - 5.0):");
        lblNota.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel lblPeriodo = new JLabel("Periodo (1-4):");
        lblPeriodo.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel lblFecha = new JLabel("Fecha (dd/MM/yyyy):");
        lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(lblNombreActividad)
                .addComponent(lblNota)
                .addComponent(lblPeriodo)
                .addComponent(lblFecha))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(txtNombreCalificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnGuardar, btnCancelar});

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblNombreActividad)
                .addComponent(txtNombreCalificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblNota)
                .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblPeriodo)
                .addComponent(txtPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblFecha)
                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(25) 
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnGuardar)
                .addComponent(btnCancelar))
        );
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCalificacion();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void guardarCalificacion() {
        String nombreActividad = txtNombreCalificacion.getText().trim();
        String notaStr = txtNota.getText().trim();
        String periodoStr = txtPeriodo.getText().trim();
        String fechaStr = txtFecha.getText().trim();

        if (nombreActividad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la actividad no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            txtNombreCalificacion.requestFocus();
            return;
        }

        float nota;
        try {
            nota = Float.parseFloat(notaStr);
            if (nota < 0.0f || nota > 5.0f) {
                JOptionPane.showMessageDialog(this, "La nota debe estar entre 0.0 y 5.0.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                txtNota.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato de nota inválido. Ingrese un número decimal (e.g., 3.5).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            txtNota.requestFocus();
            return;
        }

        int periodo;
        try {
            periodo = Integer.parseInt(periodoStr);
            if (periodo < 1 || periodo > 4) { 
                JOptionPane.showMessageDialog(this, "El periodo debe ser un número entre 1 y 4.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                txtPeriodo.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato de periodo inválido. Ingrese un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            txtPeriodo.requestFocus();
            return;
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy (e.g., 25/12/2023).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            txtFecha.requestFocus();
            return;
        }

        try {
            controladorDocente.calificarEstudiante(
                estudiante.getCodigo(), 
                asignatura.getNombre(), 
                nombreActividad, 
                nota, 
                periodo, 
                fecha
            );

            JOptionPane.showMessageDialog(this, "Calificación agregada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IllegalStateException | IllegalArgumentException ex) {
             JOptionPane.showMessageDialog(this, "Error al guardar calificación: " + ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Un error inesperado ocurrió: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); 
        }
    }
}
