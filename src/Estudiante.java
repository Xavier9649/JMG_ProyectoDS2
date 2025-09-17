public class Estudiante {
    // atributos privados de la clase estudiante- private significa que solo
    // pueden ser accedidos desde dentro de la clase
    private int id;
    private String cedula;
    private String nombres;
    private String email;
    // el constructor vacio es util para inicializaciones progresivas
    //o para compatibilidad con frameworks que requieren un constructor sin parametros
    // el constructor con parametros es util para crear objetos completos de una sola vez
//Metodo constructor vacio ya que no queremos inicializar los atributos
    public Estudiante() {}
// Metodo constructor con parametros para inicializar los atributos
    //permite crear un objeto estudiante con los atributos especificados
    public Estudiante(int id, String cedula, String nombres, String email) {
        this.id = id;
        this.cedula = cedula;
        this.nombres = nombres;
        this.email = email;
    }
    // metodos get y set para acceder y modificar los atributos
    // getter devuelve el valor del atributo
    // setter asigna un valor al atributo

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
// metodos getter y setter para cedula, nombres y email
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
// sobreescritura del metodo toString para mostrar el estudiante en el JComboBox
    @Override
    public String toString() {//Metodo usado para mostrar el estudiante en el JComboBox
        return nombres + " (" + cedula + ")";
    }
}
//retorna el nombre y la cedula del estudiante en el formato "nombres (cedula)"


