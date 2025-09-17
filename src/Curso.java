public class Curso {
    // definicion de la clase publica y los atributos privados
    private int id;
    private String nombre;
    private int creditos;
    // el constructor vacio es util para inicializaciones progresivas
    //o para compatibilidad con frameworks que requieren un constructor sin parametros
    // el constructor con parametros es util para crear objetos completos de una sola vez
// constructor vacio para crear un curso sin inicializar los atributos
    public Curso() {}
// constructor con parametros para inicializar los atributos al crear el objeto curso
    public Curso(int id, String nombre, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
    }
// metodos getter y setter para acceder y modificar los atributos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
// metodo toString para mostrar el curso en el JComboBox
    @Override
    public String toString() { //Este método se usa para mostrar el curso en el JComboBox
        return nombre + " (" + creditos + ")";
    }
}


