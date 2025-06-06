/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.vista;

import com.controlador.ControladorAdmin;
import com.modelo.Curso;
import com.modelo.Estudiante;
import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.lang.ClassNotFoundException;

/**
 *
 * @author SOTO PC
 */
public class FormularioEstudiante extends javax.swing.JDialog {

    private ControladorAdmin controlador;

  public FormularioEstudiante(Frame parent, ControladorAdmin controlador) {
    super(parent, "Agregar Estudiante", true); 
    this.controlador = controlador;
    initComponents();
    cargarCursos();
    configurarComponentes();
    this.setLocationRelativeTo(parent); 
}

    private void cargarCursos() {
        cmbCursos.removeAllItems();
        try {
            // Ensure controlador is not null before using, though it should be set in constructor
            if (controlador == null) {
                JOptionPane.showMessageDialog(this, "Error: Controlador no inicializado.", "Error Interno", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ArrayList<Curso> cursos = controlador.getCursos(); // Call can throw
            if (cursos != null && !cursos.isEmpty()) { // cursos can be null if an error occurs before list creation or if DAO returns null (current DAOs return empty list)
                for (Curso curso : cursos) {
                    cmbCursos.addItem(curso); 
                }
            }
            // If cursos is null or empty, the combo box will remain empty.
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar lista de cursos: " + e.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
            // Optionally disable cmbCursos or add a specific "Error" item
        }
    }

    private void configurarComponentes() {
        txtNombre.setToolTipText("Ingrese el nombre completo");
        txtEdad.setToolTipText("Edad en años");
        txtCedula.setToolTipText("Número de cédula");
        txtCodigo.setToolTipText("Código único del estudiante");
        cmbCursos.setToolTipText("Seleccione el curso (opcional)");
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String nombre = txtNombre.getText().trim();
            String edadStr = txtEdad.getText().trim();
            String cedulaStr = txtCedula.getText().trim();
            String codigoStr = txtCodigo.getText().trim();

            if (nombre.isEmpty() || edadStr.isEmpty() || cedulaStr.isEmpty() || codigoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos (Nombre, Edad, Cédula, Código) son obligatorios.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int edad = Integer.parseInt(edadStr);
            int cedula = Integer.parseInt(cedulaStr);
            int codigo = Integer.parseInt(codigoStr);
            Curso cursoSeleccionado = (Curso) cmbCursos.getSelectedItem(); 

            if (edad <= 0 || cedula <= 0 || codigo <= 0) {
                 JOptionPane.showMessageDialog(this, "Edad, cédula y código deben ser números positivos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (controlador.buscarEstudiante(codigo) != null || controlador.buscarProfesor(codigo) != null) {
                JOptionPane.showMessageDialog(this, "El código ingresado ya existe para otra persona.", "Error de Duplicación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Estudiante estudiante = new Estudiante(nombre, edad, cedula, codigo, "Estudiante"); // Create student without course initially
            
            controlador.agregarEstudiante(estudiante); // Persist the student first

            // If a course was selected, now formally assign the student to the course
            if (cursoSeleccionado != null) {
                controlador.asignarEstudianteACurso(estudiante.getCodigo(), cursoSeleccionado.getGrado(), cursoSeleccionado.getGrupo());
            }

            JOptionPane.showMessageDialog(this, "Estudiante registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en campos numéricos (Edad, Cédula, Código). Deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error de persistencia de datos: " + e.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // For debugging
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose(); 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblEdad = new javax.swing.JLabel();
        lblCedula = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        lblCurso = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtEdad = new javax.swing.JTextField();
        txtCedula = new javax.swing.JTextField();
        txtCodigo = new javax.swing.JTextField();
        cmbCursos = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Nuevo Estudiante");

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Agregar Estudiante");

        lblNombre.setText("Nombre:");
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEdad.setText("Edad:");
        lblEdad.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCedula.setText("Cédula:");
        lblCedula.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCodigo.setText("Código:");
        lblCodigo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurso.setText("Curso:");
        lblCurso.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(20, Short.MAX_VALUE) 
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(txtEdad)
                        .addComponent(txtCedula)
                        .addComponent(txtCodigo)
                        .addComponent(cmbCursos, 0, 200, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(20, Short.MAX_VALUE)) 
        );
        
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnGuardar, btnCancelar});


        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE) 
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEdad)
                    .addComponent(txtEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCedula)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCurso)
                    .addComponent(cmbCursos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(20, Short.MAX_VALUE)) 
        );
        pack();
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<Curso> cmbCursos;
    private javax.swing.JLabel lblTitulo;      
    private javax.swing.JLabel lblNombre;      
    private javax.swing.JLabel lblEdad;        
    private javax.swing.JLabel lblCedula;      
    private javax.swing.JLabel lblCodigo;      
    private javax.swing.JLabel lblCurso;       
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration                   
}
