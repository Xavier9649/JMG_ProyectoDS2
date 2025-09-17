import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
// clase principal que extiende JFrame para crear la interfaz grafica
public class Main extends JFrame {
    // Campos de texto y combo para el formulario de la interfaz
    private final JTextField txtCedula = new JTextField();
    private final JTextField txtNombres = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JComboBox<Curso> comboCurso = new JComboBox<>();
// modelo de tabla para mostrar los datos estudiantes e inscripciones  en la interfaz
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID Inscr.", "ID Est.", "Cédula", "Nombres", "Email", "ID Curso", "Curso", "Créditos", "Fecha"}, 0
    );
    // tabla para listar estudiantes
    private final JTable tabla = new JTable(modelo);

    // Para actualizar/eliminar (variables que guardan los IDs seleccionados)
    private Integer idInscripcionSel = null;
    private Integer idEstudianteSel  = null;
// metodo constructor de la clase Main
    public Main() {
        setTitle("Universidad - Estudiantes e Inscripción");
        setDefaultCloseOperation(EXIT_ON_CLOSE);//cerrar la aplicacion al cerrar la ventana
        setSize(900, 560);//tamaño de la ventana
        setLocationRelativeTo(null);//centrar la ventana
        iniciarInterfaz();//iniciar la interfaz grafica
        cargarCursos();//cargar los cursos en el combo
        cargarTabla();//cargar los datos en la tabla
        mostrarDatosArriba();//mostrar los datos seleccionados en la parte superior
    }
// interfaz grafica
    private void iniciarInterfaz() {
        setLayout(new BorderLayout());
        // DIVIDIMOS en 2 filas el formulario
        JPanel form = new JPanel(new GridLayout(2, 1, 10, 10));
        // primera fila: cedula, nombres, email
        JPanel fila1 = new JPanel(new GridLayout(1, 6, 10, 10));
        fila1.add(new JLabel("Cédula:"));
        fila1.add(txtCedula);
        fila1.add(new JLabel("Nombres:"));
        fila1.add(txtNombres);
        fila1.add(new JLabel("Email:"));
        fila1.add(txtEmail);
        // segunda fila: curso y botones
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        fila2.add(new JLabel("Curso:"));
        comboCurso.setPreferredSize(new Dimension(260, 28));
        fila2.add(comboCurso);
        // botones CRUD
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        fila2.add(btnNuevo);
        fila2.add(btnGuardar);
        fila2.add(btnActualizar);
        fila2.add(btnEliminar);

        // añaidimos filas al formulario
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        form.add(fila1);
        form.add(fila2);
        // agregamos formulario y tabla a la ventana principal centrado
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos básicos de botones
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> {
            try { agregar(); cargarTabla(); limpiarFormulario(); }
            catch (Exception ex) { mostrarError(ex); }
        });
        btnActualizar.addActionListener(e -> {
            try {
                if (idInscripcionSel == null || idEstudianteSel == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un registro."); return;
                }
                actualizar(); cargarTabla(); limpiarFormulario();
            } catch (Exception ex) { mostrarError(ex); }
        });
        btnEliminar.addActionListener(e -> {
            try {
                if (idEstudianteSel == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un registro."); return;
                }
                int c = JOptionPane.showConfirmDialog(this, "¿Eliminar estudiante e inscripción?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) { eliminar(); cargarTabla(); limpiarFormulario(); }
            } catch (Exception ex) { mostrarError(ex); }
        });
    }
// METODOS AUXILIARES DE LA INTERFAZ
    // cuando seleccionas un registro en la tabla, muestra
    // los datos en los campos de texto y combo para editar o eliminar
    private void mostrarDatosArriba() { //Este metodo muestra los datos seleccionados en la parte superior
        // para despues actualizarlos o eliminarlos.
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int row = tabla.getSelectedRow();
                idInscripcionSel = (Integer) tabla.getValueAt(row, 0);
                idEstudianteSel  = (Integer) tabla.getValueAt(row, 1);
                txtCedula.setText(String.valueOf(tabla.getValueAt(row, 2)));
                txtNombres.setText(String.valueOf(tabla.getValueAt(row, 3)));
                txtEmail.setText(String.valueOf(tabla.getValueAt(row, 4)));
                int idCurso = (Integer) tabla.getValueAt(row, 5);
                seleccionarCursoEnCombo(idCurso);
            }
        });
    }

// cargamos los cursos desde la base de datos al combo
    private void cargarCursos() {
        comboCurso.removeAllItems();
        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT id_curso, nombre, creditos FROM curso ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboCurso.addItem(new Curso(
                        rs.getInt("id_curso"),
                        rs.getString("nombre"),
                        rs.getInt("creditos")
                ));
            }
        } catch (Exception e) { mostrarError(e); }
    }

    //CRUD
    // cargar estudiantes e inscripciones en la tabla
    private void cargarTabla() {
        modelo.setRowCount(0);// limpiar la tabla
        String sql = """
        SELECT 
            COALESCE(i.id_inscripcion, 0)   AS id_inscripcion,
            e.id_estudiante,
            e.cedula,
            e.nombres,
            e.email,
            c.id_curso,
            c.nombre,
            c.creditos,
            i.fecha_inscripcion
        FROM estudiante e
        LEFT JOIN inscripcion i ON e.id_estudiante = i.id_estudiante
        LEFT JOIN curso c ON c.id_curso = i.id_curso
        ORDER BY e.nombres
    """;
        try (var cn = Database.getConnection();
             var ps = cn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[] {
                        rs.getInt("id_inscripcion"),              // 0 si no tiene inscripción
                        rs.getInt("id_estudiante"),
                        rs.getString("cedula"),
                        rs.getString("nombres"),
                        rs.getString("email"),
                        rs.getObject("id_curso") == null ? 0 : rs.getInt("id_curso"),
                        rs.getString("nombre"),                   // puede ser null
                        rs.getObject("creditos") == null ? 0 : rs.getInt("creditos"),
                        rs.getObject("fecha_inscripcion") == null ? "" : rs.getDate("fecha_inscripcion").toString()
                });
            }
        } catch (Exception e) { mostrarError(e); }
    }

// agregar nuevo estudiante e inscripción
    private void agregar() throws Exception {
        String ced = txtCedula.getText().trim();
        String nom = txtNombres.getText().trim();
        String ema = txtEmail.getText().trim();
        Curso cur = (Curso) comboCurso.getSelectedItem();

        if (ced.isEmpty() || nom.isEmpty() || ema.isEmpty() || cur == null) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos y selecciona un curso."); return;
        }

        try (Connection cn = Database.getConnection()) {
            cn.setAutoCommit(false);
            try {
                // Estudiante insertar
                PreparedStatement psE = cn.prepareStatement(
                        "INSERT INTO estudiante (cedula,nombres,email) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                psE.setString(1, ced);
                psE.setString(2, nom);
                psE.setString(3, ema);
                psE.executeUpdate();
                int idEst;
                try (ResultSet keys = psE.getGeneratedKeys()) { keys.next(); idEst = keys.getInt(1); }

                // Inscripción insertar
                PreparedStatement psI = cn.prepareStatement(
                        "INSERT INTO inscripcion (id_estudiante,id_curso,fecha_inscripcion) VALUES (?,?,?)");
                psI.setInt(1, idEst);
                psI.setInt(2, cur.getId());
                psI.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                psI.executeUpdate();

                cn.commit();
            } catch (Exception ex) { cn.rollback(); throw ex; }
            finally { cn.setAutoCommit(true); }
        }
    }
// actualizar estudiante e inscripción
    private void actualizar() throws Exception {
        String ced = txtCedula.getText().trim();
        String nom = txtNombres.getText().trim();
        String ema = txtEmail.getText().trim();
        Curso cur = (Curso) comboCurso.getSelectedItem();

        if (idInscripcionSel == null || idEstudianteSel == null) return;

        try (Connection cn = Database.getConnection()) {
            cn.setAutoCommit(false);
            try { // actualizar estudiante
                PreparedStatement psE = cn.prepareStatement(
                        "UPDATE estudiante SET cedula=?, nombres=?, email=? WHERE id_estudiante=?");
                psE.setString(1, ced);
                psE.setString(2, nom);
                psE.setString(3, ema);
                psE.setInt(4, idEstudianteSel);
                psE.executeUpdate();
                // actualizar inscripción
                PreparedStatement psI = cn.prepareStatement(
                        "UPDATE inscripcion SET id_curso=?, fecha_inscripcion=? WHERE id_inscripcion=?");
                psI.setInt(1, cur.getId());
                psI.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                psI.setInt(3, idInscripcionSel);
                psI.executeUpdate();

                cn.commit();
            } catch (Exception ex) { cn.rollback(); throw ex; }
            finally { cn.setAutoCommit(true); }
        }
    }
    //eliminar estudiante e inscripción en base de datos
    private void eliminar() throws Exception {
        if (idEstudianteSel == null) return;
        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM estudiante WHERE id_estudiante=?")) {
            ps.setInt(1, idEstudianteSel);
            ps.executeUpdate(); // ON DELETE CASCADE borra inscripción
        }
    }

    //Metodos auxiliares

    private void limpiarFormulario() {
        idInscripcionSel = null;
        idEstudianteSel = null;
        txtCedula.setText(""); txtNombres.setText(""); txtEmail.setText("");
        if (comboCurso.getItemCount() > 0) comboCurso.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void seleccionarCursoEnCombo(int idCurso) {
        for (int i = 0; i < comboCurso.getItemCount(); i++) {
            Curso c = comboCurso.getItemAt(i);
            if (c.getId() == idCurso) { comboCurso.setSelectedIndex(i); break; }
        }
    }

    private void mostrarError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    //Método main

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}

