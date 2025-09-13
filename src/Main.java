import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class Main extends JFrame {
    private JTextField txtCedula = new JTextField();
    private JTextField txtNombres = new JTextField();
    private JTextField txtEmail = new JTextField();
    private JTextField txtCurso = new JTextField();
    private JTable tabla;
    private DefaultTableModel modelo;

    public Main() {
        setTitle("Universidad - Estudiantes e Inscripción");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Panel superior (formulario) ---
        JPanel form = new JPanel(new GridLayout(2,4,5,5));
        form.add(new JLabel("Cédula:")); form.add(txtCedula);
        form.add(new JLabel("Nombres:")); form.add(txtNombres);
        form.add(new JLabel("Email:")); form.add(txtEmail);
        form.add(new JLabel("Curso:")); form.add(txtCurso);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        JPanel botones = new JPanel();
        botones.add(btnAgregar); botones.add(btnActualizar);
        botones.add(btnEliminar); botones.add(btnRefrescar);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.SOUTH);

        // --- Tabla ---
        modelo = new DefaultTableModel(new String[]{"ID","Cédula","Nombres","Email","Curso"},0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- Eventos ---
        btnAgregar.addActionListener(e -> agregar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargarTabla());

        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try (Connection cn = Database.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT i.id_inscripcion, e.cedula, e.nombres, e.email, c.nombre " +
                             "FROM inscripcion i " +
                             "JOIN estudiante e ON e.id_estudiante=i.id_estudiante " +
                             "JOIN curso c ON c.id_curso=i.id_curso")) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void agregar() {
        try (Connection cn = Database.getConnection()) {
            // Insert estudiante
            PreparedStatement psE = cn.prepareStatement(
                    "INSERT INTO estudiante (cedula,nombres,email) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            psE.setString(1, txtCedula.getText());
            psE.setString(2, txtNombres.getText());
            psE.setString(3, txtEmail.getText());
            psE.executeUpdate();

            int idEst;
            try (ResultSet keys = psE.getGeneratedKeys()) {
                keys.next(); idEst = keys.getInt(1);
            }

            // Buscar curso por nombre
            PreparedStatement psC = cn.prepareStatement("SELECT id_curso FROM curso WHERE nombre=?");
            psC.setString(1, txtCurso.getText());
            ResultSet rsC = psC.executeQuery();
            int idCurso = 0;
            if (rsC.next()) idCurso = rsC.getInt(1);

            // Insert inscripción
            PreparedStatement psI = cn.prepareStatement(
                    "INSERT INTO inscripcion (id_estudiante,id_curso,fecha_inscripcion) VALUES (?,?,?)");
            psI.setInt(1, idEst);
            psI.setInt(2, idCurso);
            psI.setDate(3, Date.valueOf(LocalDate.now()));
            psI.executeUpdate();

            cargarTabla();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        int idIns = (int) modelo.getValueAt(fila, 0);
        try (Connection cn = Database.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(
                    "UPDATE estudiante e " +
                            "JOIN inscripcion i ON e.id_estudiante=i.id_estudiante " +
                            "SET e.cedula=?, e.nombres=?, e.email=? " +
                            "WHERE i.id_inscripcion=?");
            ps.setString(1, txtCedula.getText());
            ps.setString(2, txtNombres.getText());
            ps.setString(3, txtEmail.getText());
            ps.setInt(4, idIns);
            ps.executeUpdate();
            cargarTabla();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        int idIns = (int) modelo.getValueAt(fila, 0);
        try (Connection cn = Database.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("DELETE FROM inscripcion WHERE id_inscripcion=?");
            ps.setInt(1, idIns);
            ps.executeUpdate();
            cargarTabla();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
