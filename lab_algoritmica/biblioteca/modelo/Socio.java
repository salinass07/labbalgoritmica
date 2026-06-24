package biblioteca.modelo;

public class Socio {

    private String nroSocio;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo;

    public Socio(String nroSocio, String nombre, String apellido,
                String email, boolean activo) {
        // inicializar atributos
        this.nroSocio = nroSocio;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.activo = activo;
    }

    // getters y setters
    public String getNroSocio() {
        return nroSocio;
    }

    public void setNroSocio(String nroSocio) {
        this.nroSocio = nroSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // toString
    @Override
    public String toString() {
        return "Socio{" +
                "nroSocio='" + nroSocio + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                '}';

    }

    // equals
    @Override
    public boolean equals(Object obj) {
        // dos socios son iguales si tienen el mismo nroSocio
        // Casteo explicito del objeto a Socio para comparar el nroSocio
        Socio socio = (Socio) obj;
        return java.util.Objects.equals(nroSocio, socio.nroSocio);
    }
}
