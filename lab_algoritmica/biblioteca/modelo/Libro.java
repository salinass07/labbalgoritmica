package biblioteca.modelo;

public class Libro {

    private String isbn;
    private String titulo;
    private String autor;
    private String genero;
    private int anioPublicacion;
    private int ejemplaresDisponibles;

    public Libro(String isbn, String titulo, String autor, String genero,
                int anioPublicacion, int ejemplaresDisponibles) {
        // inicializar atributos
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    // getters y setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void setEjemplaresDisponibles(int ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    // toString
    @Override
    public String toString() {
        return "Libro{" +
                "isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", genero='" + genero + '\'' +
                ", anioPublicacion=" + anioPublicacion +
                ", ejemplaresDisponibles=" + ejemplaresDisponibles +
                '}';
    }

    // equals
    @Override
    public boolean equals(Object obj) {
        // dos libros son iguales si tienen el mismo ISBN
        // Casteo explicito del objeto a Libro para comparar el ISBN
        Libro libro = (Libro) obj;
        return java.util.Objects.equals(isbn, libro.isbn);
    }
}
