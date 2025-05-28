package com.vista;

import com.controlador.ControladorAdmin;
import com.modelo.Curso;
import com.modelo.Asignatura; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List; 

/**
 *
 * @author SOTO PC
 */
public class FormularioAsignatura extends JDialog {

    private ControladorAdmin controlador;
    private JTextField txtNombreAsignatura;
    private JComboBox<Curso> cmbCursos;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public FormularioAsignatura(Frame parent, ControladorAdmin controlador) {
        super(parent, "Crear Asignatura", true);
        this.controlador = controlador;

        initComponents();
        populateCursos();
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        txtNombreAsignatura = new JTextField(20);
        cmbCursos = new JComboBox<>();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarAsignatura();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void populateCursos() {
        List<Curso> cursos = controlador.getCursos(); 
        if (cursos != null) {
            for (Curso curso : cursos) {
                cmbCursos.addItem(curso);
            }
        }
    }

    private void setupLayout() {
        JLabel lblDialogTitle = new JLabel("Crear Asignatura");
        lblDialogTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblDialogTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNombreAsignaturaText = new JLabel("Nombre Asignatura:");
        lblNombreAsignaturaText.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel lblCursoText = new JLabel("Seleccionar Curso:");
        lblCursoText.setHorizontalAlignment(SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addComponent(lblDialogTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombreAsignaturaText, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCursoText, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNombreAsignatura, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(cmbCursos, 0, 200, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) 
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)) 
        );
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnGuardar, btnCancelar});


        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(lblDialogTitle)
            .addGap(18) 
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblNombreAsignaturaText)
                .addComponent(txtNombreAsignatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) 
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblCursoText)
                .addComponent(cmbCursos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(25) 
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnGuardar)
                .addComponent(btnCancelar))
        );
    }

    private void guardarAsignatura() {
        String nombreAsignatura = txtNombreAsignatura.getText().trim();
        Curso cursoSeleccionado = (Curso) cmbCursos.getSelectedItem();

        if (nombreAsignatura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la asignatura no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cursoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un curso.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (controlador.existeAsignaturaGlobalmente(nombreAsignatura)) {
            Asignatura asigExistenteEnCurso = cursoSeleccionado.buscarAsignatura(nombreAsignatura);
            if (asigExistenteEnCurso != null) {
                 JOptionPane.showMessageDialog(this, "Una asignatura con este nombre ya existe en el curso seleccionado.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                 return;
            } else {
                 JOptionPane.showMessageDialog(this, "Una asignatura con este nombre ya existe globalmente en el sistema. No se puede crear una nueva con el mismo nombre.", "Error de Duplicación", JOptionPane.ERROR_MESSAGE);
                 return;
            }
        }

        controlador.crearAsignatura(nombreAsignatura, cursoSeleccionado);

        JOptionPane.showMessageDialog(this, "Asignatura creada y asociada al curso exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
