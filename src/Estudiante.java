public class Estudiante {
    private int id;
    private String cedula;
    private String nombres;
    private String email;

    public Estudiante() {}

    public Estudiante(int id, String cedula, String nombres, String email) {
        this.id = id;
        this.cedula = cedula;
        this.nombres = nombres;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nombres + " (" + cedula + ")";
    }
}

