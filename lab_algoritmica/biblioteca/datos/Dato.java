package biblioteca.datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import net.datastructures.ProbeHashMap;
import net.datastructures.LinkedPositionalList;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Socio;
import biblioteca.modelo.Prestamo;

public class Dato {

    /**
     * Carga los libros desde un archivo de texto.
     * Formato de cada línea: isbn;titulo;autor;genero;anio;ejemplares
     * Ejemplo: 978-0;Cien años de soledad;García Márquez;Novela;1967;3
     *
     * @return mapa indexado por ISBN
     */
    public static ProbeHashMap<String, Libro> cargarLibros(String fileName)
            throws FileNotFoundException {

        ProbeHashMap<String, Libro> libros = new ProbeHashMap<>();
        // lectura del archivo y carga del mapa
        Scanner scanner = new Scanner(new File(fileName));
        // cada línea del archivo representa un libro, con los campos separados por ';'
        while (scanner.hasNextLine()) {
            // linea: isbn;titulo;autor;genero;anioPublicacion;ejemplaresDisponibles
            String linea = scanner.nextLine();
            // campos es un arreglo con los campos del libro
            String[] campos = linea.split(";");
            // asignar a cada campo del libro el valor correspondiente del arreglo campos
            String isbn = campos[0];
            String titulo = campos[1];
            String autor = campos[2];
            String genero = campos[3];
            int anioPublicacion = Integer.parseInt(campos[4]); 
            int ejemplaresDisponibles = Integer.parseInt(campos[5]); 
            // instanciar un objeto Libro con los datos extraidos y agregarlo al mapa
            // con isbn como clave
            Libro libro = new Libro(isbn, titulo, autor, genero, anioPublicacion, ejemplaresDisponibles);
            libros.put(isbn, libro);
        }
        // cerrar scanner
        scanner.close();
        return libros;
    }

    /**
     * Carga los socios desde un archivo de texto.
     * Formato de cada línea: nroSocio;nombre;apellido;email;activo
     * Ejemplo: S001;Juan;Perez;juan@mail.com;true
     *
     * @return mapa indexado por nroSocio
     */
    public static ProbeHashMap<String, Socio> cargarSocios(String fileName)
            throws FileNotFoundException {

        ProbeHashMap<String, Socio> socios = new ProbeHashMap<>();
        // lectura del archivo y carga del mapa
        Scanner scanner = new Scanner(new File(fileName));
        // cada línea del archivo representa un socio, con los campos separados por ';'
        while (scanner.hasNextLine()) {
            // linea: nroSocio;nombre;apellido;email;activo
            String linea = scanner.nextLine();
            // campos es un arreglo con los campos del socio
            String[] campos = linea.split(";");
            // asignar a cada campo del socio el valor correspondiente del arreglo campos
            String nroSocio = campos[0];
            String nombre = campos[1];
            String apellido = campos[2];
            String email = campos[3];
            boolean activo = Boolean.parseBoolean(campos[4]);
            // instanciar un objeto Socio con los datos extraidos y agregarlo al mapa
            // con nroSocio como clave
            Socio socio = new Socio(nroSocio, nombre, apellido, email, activo);
            socios.put(nroSocio, socio);
        }
        // cerrar scanner
        scanner.close();
        return socios;
    }

    /**
     * Carga los préstamos activos desde un archivo de texto.
     * Formato de cada línea: nroSocio;isbn;fechaPrestamo;fechaVencimiento
     * Ejemplo: S001;978-0;01/06/2026;15/06/2026
     *
     * @return mapa indexado por nroSocio con la lista de préstamos de cada socio
     */
    public static ProbeHashMap<String, LinkedPositionalList<Prestamo>> cargarPrestamos(
            String fileName,
            ProbeHashMap<String, Socio> socios,
            ProbeHashMap<String, Libro> libros)
            throws FileNotFoundException {

        ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos = new ProbeHashMap<>();
        // lectura del archivo y carga del mapa
        Scanner scanner = new Scanner(new File(fileName));
        // se considera a todos los prestamos persistidos ya en el archivo "prestamos.txt"
        // como prestamos activos independientemente de si estan vencidos o no
        
        // cada línea del archivo representa un prestamo, con los campos separados por ';'
        while (scanner.hasNextLine()) {
            // linea: nroSocio;isbn;fechaPrestamo;fechaVencimiento
            String linea = scanner.nextLine();
            // campos es un arreglo con los campos del prestamo
            String[] campos = linea.split(";");
            // asignar a cada campo del prestamo el valor correspondiente del arreglo campos
            String nroSocio = campos[0];
            String isbn = campos[1];
            String fechaPrestamoStr = campos[2];
            String fechaVencimientoStr = campos[3];


            // convertir las fechas de String a LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaPrestamo = LocalDate.parse(fechaPrestamoStr, formatter);
            LocalDate fechaVencimiento = LocalDate.parse(fechaVencimientoStr, formatter);

            // obtener el socio y el libro correspondientes a los datos del prestamo
            Socio socio = socios.get(nroSocio);
            Libro libro = libros.get(isbn);
            // instanciar un objeto Prestamo con los datos extraidos
            Prestamo prestamo = new Prestamo(socio, libro, fechaPrestamo, fechaVencimiento);

            // agregar el prestamo a la lista de prestamos del socio en el mapa
            LinkedPositionalList<Prestamo> listaPrestamos = prestamos.get(nroSocio);
            if (listaPrestamos == null) {
                listaPrestamos = new LinkedPositionalList<>();
                prestamos.put(nroSocio, listaPrestamos);
            }
            listaPrestamos.addLast(prestamo);
        }
        // cerrar scanner
        scanner.close();
        return prestamos;
    }
}
