import java.time.LocalDate;

public class Inscripcion {
    private int id;
    private Estudiante estudiante;
    private Curso curso;
    private LocalDate fecha;

    public Inscripcion() {}

    public Inscripcion(int id, Estudiante estudiante, Curso curso, LocalDate fecha) {
        this.id = id;
        this.estudiante = estudiante;
        this.curso = curso;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    @Override
    public String toString() {//Metodo usado para mostrar la inscripcion en el JComboBox
        return estudiante + " -> " + curso + " [" + fecha + "]";
    }
}


