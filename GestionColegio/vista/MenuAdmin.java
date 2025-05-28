package com.vista;

import com.controlador.ControladorAdmin;
import javax.swing.*; 
import java.awt.*;    
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

/**
 *
 * @author SOTO PC
 */
public class MenuAdmin extends javax.swing.JFrame {
    private ControladorAdmin controlador;

    private JLabel jLabel1; 
    private JLabel titulo;  
    private JButton jButton1; 
    private JButton jButton2; 
    private JButton btnListarDocentes;
    private JButton btnListarEstudiantes;
    private JButton btnCerrarSesion;
    private JButton btnCrearCurso;
    private JButton btnCrearAsignatura;
    private JButton btnEliminarEstudiante;
    private JButton btnEliminarProfesor; 
    private JButton btnEliminarCurso;
    private JButton btnEliminarAsignatura;
    private JButton btnGenerarReporteEstudiante;

    public MenuAdmin() {
        initComponents();
        configurarEventos(); 
    }

    public MenuAdmin(ControladorAdmin controlador) {
        this.controlador = controlador;
        initComponents();
        configurarEventos(); 
        setLocationRelativeTo(null); 
    }

    private void configurarEventos() {
        if (jButton1 != null) { 
            jButton1.addActionListener(e -> {
                if (this.controlador == null) { JOptionPane.showMessageDialog(this, "Controlador no inicializado.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                FormularioDocente formDocente = new FormularioDocente(this, controlador);
                formDocente.setVisible(true);
            });
        }
        if (jButton2 != null) { 
            jButton2.addActionListener(e -> {
                if (this.controlador == null) { JOptionPane.showMessageDialog(this, "Controlador no inicializado.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                FormularioEstudiante formEstudiante = new FormularioEstudiante(this, controlador);
                formEstudiante.setVisible(true);
            });
        }
        if (btnCerrarSesion != null) {
            btnCerrarSesion.addActionListener(e -> {
                Principal principal = new Principal();
                principal.setVisible(true);
                this.dispose();
            });
        }
        if (btnCrearCurso != null) {
            btnCrearCurso.addActionListener(e -> {
                if (this.controlador == null) { JOptionPane.showMessageDialog(this, "Controlador no inicializado.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                FormularioCurso formCurso = new FormularioCurso(this, controlador);
                formCurso.setVisible(true);
            });
        }
        if (btnListarDocentes != null) {
            btnListarDocentes.addActionListener(e -> {
                if (this.controlador == null) { return; }
                String listaDocentes = controlador.listarTodosLosProfesores();
                JTextArea textArea = new JTextArea(listaDocentes);
                textArea.setEditable(false);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Lista de Docentes", JOptionPane.INFORMATION_MESSAGE);
            });
        }
        if (btnListarEstudiantes != null) {
            btnListarEstudiantes.addActionListener(e -> {
                if (this.controlador == null) { return; }
                String listaEstudiantes = controlador.listarTodosLosEstudiantes();
                JTextArea textArea = new JTextArea(listaEstudiantes);
                textArea.setEditable(false);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Lista de Estudiantes", JOptionPane.INFORMATION_MESSAGE);
            });
        }
        if (btnCrearAsignatura != null) {
            btnCrearAsignatura.addActionListener(e -> {
                if (this.controlador == null) { JOptionPane.showMessageDialog(this, "Controlador no inicializado.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                FormularioAsignatura formAsignatura = new FormularioAsignatura(this, controlador);
                formAsignatura.setVisible(true);
            });
        }
        if (btnEliminarEstudiante != null) {
            btnEliminarEstudiante.addActionListener(e -> {
                if (this.controlador == null) { return; }
                String codigoStr = JOptionPane.showInputDialog(this, "Ingrese el código del estudiante a eliminar:", "Eliminar Estudiante", JOptionPane.QUESTION_MESSAGE);
                if (codigoStr != null && !codigoStr.trim().isEmpty()) {
                    try {
                        int codigo = Integer.parseInt(codigoStr.trim());
                        if (controlador.eliminarEstudiante(codigo)) {
                            JOptionPane.showMessageDialog(this, "Estudiante eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "No se encontró o no se pudo eliminar al estudiante con el código " + codigo + ".", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "El código ingresado no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (codigoStr != null) {
                    JOptionPane.showMessageDialog(this, "El código del estudiante no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        if (btnEliminarProfesor != null) { 
            btnEliminarProfesor.addActionListener(e -> {
                 if (this.controlador == null) { return; }
                String codigoStr = JOptionPane.showInputDialog(this, "Ingrese el código del docente a eliminar:", "Eliminar Docente", JOptionPane.QUESTION_MESSAGE);
                if (codigoStr != null && !codigoStr.trim().isEmpty()) {
                    try {
                        int codigo = Integer.parseInt(codigoStr.trim());
                        if (controlador.eliminarProfesor(codigo)) {
                            JOptionPane.showMessageDialog(this, "Docente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "No se encontró o no se pudo eliminar al docente con el código " + codigo + ".", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "El código ingresado no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (codigoStr != null) {
                    JOptionPane.showMessageDialog(this, "El código del docente no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        if (btnEliminarCurso != null) {
            btnEliminarCurso.addActionListener(e -> {
                if (this.controlador == null) { return; }
                String gradoStr = JOptionPane.showInputDialog(this, "Ingrese el grado del curso a eliminar:", "Eliminar Curso - Grado", JOptionPane.QUESTION_MESSAGE);
                if (gradoStr == null) return;
                if (gradoStr.trim().isEmpty()) { JOptionPane.showMessageDialog(this, "El grado no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE); return; }
                String grupoStr = JOptionPane.showInputDialog(this, "Ingrese el grupo del curso a eliminar:", "Eliminar Curso - Grupo", JOptionPane.QUESTION_MESSAGE);
                if (grupoStr == null) return;
                if (grupoStr.trim().isEmpty()) { JOptionPane.showMessageDialog(this, "El grupo no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE); return; }
                try {
                    int grado = Integer.parseInt(gradoStr.trim());
                    int grupo = Integer.parseInt(grupoStr.trim());
                    if (controlador.eliminarCurso(grado, grupo)) {
                        JOptionPane.showMessageDialog(this, "Curso " + grado + "-" + grupo + " eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró o no se pudo eliminar el curso " + grado + "-" + grupo + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Grado y/o Grupo ingresados no son números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        if (btnEliminarAsignatura != null) {
            btnEliminarAsignatura.addActionListener(e -> {
                if (this.controlador == null) { return; }
                String nombreAsignatura = JOptionPane.showInputDialog(this, "Ingrese el nombre de la asignatura a eliminar:", "Eliminar Asignatura", JOptionPane.QUESTION_MESSAGE);
                if (nombreAsignatura != null && !nombreAsignatura.trim().isEmpty()) {
                    if (controlador.eliminarAsignatura(nombreAsignatura.trim())) {
                        JOptionPane.showMessageDialog(this, "Asignatura \"" + nombreAsignatura.trim() + "\" eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró o no se pudo eliminar la asignatura \"" + nombreAsignatura.trim() + "\".", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (nombreAsignatura != null) {
                    JOptionPane.showMessageDialog(this, "El nombre de la asignatura no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        if (btnGenerarReporteEstudiante != null) {
            btnGenerarReporteEstudiante.addActionListener(e -> {
                if (this.controlador == null) { return; }
                String codigoStr = JOptionPane.showInputDialog(this, "Ingrese el código del estudiante para generar el reporte:", "Generar Reporte Académico", JOptionPane.QUESTION_MESSAGE);
                if (codigoStr != null && !codigoStr.trim().isEmpty()) {
                    try {
                        int codigo = Integer.parseInt(codigoStr.trim());
                        if (controlador.buscarEstudiante(codigo) != null) {
                            String reporte = controlador.generarReporteEstudiante(codigo);
                            JTextArea textArea = new JTextArea(reporte);
                            textArea.setEditable(false);
                            textArea.setWrapStyleWord(true);
                            textArea.setLineWrap(true);
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            scrollPane.setPreferredSize(new Dimension(500, 400));
                            JOptionPane.showMessageDialog(this, scrollPane, "Reporte Académico - Estudiante " + codigo, JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Estudiante no encontrado con el código " + codigo + ".", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "El código ingresado no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (codigoStr != null) {
                    JOptionPane.showMessageDialog(this, "El código del estudiante no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
    
    private void initComponents() {
        titulo = new JLabel();
        jLabel1 = new JLabel(); 
        jButton1 = new JButton(); 
        jButton2 = new JButton(); 
        btnListarDocentes = new JButton();
        btnListarEstudiantes = new JButton();
        btnCerrarSesion = new JButton();
        btnCrearCurso = new JButton();
        btnCrearAsignatura = new JButton();
        btnEliminarEstudiante = new JButton();
        btnEliminarProfesor = new JButton(); 
        btnEliminarCurso = new JButton();
        btnEliminarAsignatura = new JButton();
        btnGenerarReporteEstudiante = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menú Administrador");

        titulo.setText("GESTION COLEGIO");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        jLabel1.setText("MENÚ ADMIN"); 
        jLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 20)); 
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        jButton1.setText("Agregar Docente");
        jButton2.setText("Agregar Estudiante");
        btnListarDocentes.setText("Listar Docentes");
        btnListarEstudiantes.setText("Listar Estudiantes");
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCrearCurso.setText("Crear Curso");
        btnCrearAsignatura.setText("Crear Asignatura");
        btnEliminarEstudiante.setText("Eliminar Estudiante");
        btnEliminarProfesor.setText("Eliminar Docente");
        btnEliminarCurso.setText("Eliminar Curso");
        btnEliminarAsignatura.setText("Eliminar Asignatura");
        btnGenerarReporteEstudiante.setText("Reporte Estudiante");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addComponent(titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(btnListarDocentes)
                    .addComponent(btnEliminarProfesor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(btnListarEstudiantes)
                    .addComponent(btnEliminarEstudiante))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCrearCurso)
                    .addComponent(btnGenerarReporteEstudiante)
                    .addComponent(btnEliminarCurso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCrearAsignatura)
                    .addComponent(btnEliminarAsignatura)))
            .addComponent(btnCerrarSesion, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        
        layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {
            jButton1, jButton2, btnCrearCurso, btnCrearAsignatura,
            btnListarDocentes, btnListarEstudiantes, btnGenerarReporteEstudiante, 
            btnEliminarProfesor, btnEliminarEstudiante, btnEliminarCurso, btnEliminarAsignatura
        });


        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(titulo)
            .addComponent(jLabel1)
            .addGap(20) 
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1)
                .addComponent(jButton2)
                .addComponent(btnCrearCurso)
                .addComponent(btnCrearAsignatura))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnListarDocentes)
                .addComponent(btnListarEstudiantes)
                .addComponent(btnGenerarReporteEstudiante)
                .addComponent(btnEliminarAsignatura)) 
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnEliminarProfesor)
                .addComponent(btnEliminarEstudiante)
                .addComponent(btnEliminarCurso))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE) 
            .addComponent(btnCerrarSesion)
        );

        pack();
    }
}
