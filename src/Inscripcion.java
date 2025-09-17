import java.time.LocalDate;
// definicion de la clase Inscripcion que es publica
public class Inscripcion {
    // atributos privados de la clase inscripcion
    private int id;
    private Estudiante estudiante;
    private Curso curso;
    private LocalDate fecha;
    // el constructor vacio es util para inicializaciones progresivas
    //o para compatibilidad con frameworks que requieren un constructor sin parametros
    // el constructor con parametros es util para crear objetos completos de una sola vez
// constructor vacio para crear una inscripcion sin inicializar los atributos y solo crear objeto
    public Inscripcion() {}
// constructor con parametros para inicializar los atributos al crear el objeto inscripcion
    public Inscripcion(int id, Estudiante estudiante, Curso curso, LocalDate fecha) {
        this.id = id;
        this.estudiante = estudiante;
        this.curso = curso;
        this.fecha = fecha;
    }
// metodos getter y setter para acceder y modificar los atributos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
// metodo toString para mostrar la inscripcion en el JComboBox
    @Override
    public String toString() {//Metodo usado para mostrar la inscripcion en el JComboBox
        return estudiante + " -> " + curso + " [" + fecha + "]";
    }
}


