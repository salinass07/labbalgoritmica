package biblioteca.interfaz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import biblioteca.aplicacion.Constante;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;

public class Interfaz {

    private static final Scanner           SC  = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Muestra el menú principal y retorna la opción elegida por el usuario.
     */
    public static int menu() {
        System.out.println("\n===== SISTEMA DE GESTIÓN DE BIBLIOTECA =====");
        System.out.println(Constante.OPCION_PRESTAR       + ". Registrar préstamo");
        System.out.println(Constante.OPCION_DEVOLVER      + ". Registrar devolución");
        System.out.println(Constante.OPCION_BUSCAR_ISBN   + ". Buscar libro por ISBN");
        System.out.println(Constante.OPCION_BUSCAR_TITULO + ". Buscar libro por título");
        System.out.println(Constante.OPCION_BUSCAR_AUTOR  + ". Buscar libro por autor");
        System.out.println(Constante.OPCION_DISPONIBLES   + ". Listar libros disponibles");
        System.out.println(Constante.OPCION_PRESTAMOS_SOCIO + ". Ver préstamos activos de un socio");
        System.out.println("---- Incremento 2 ----");
        System.out.println(Constante.OPCION_HISTORIAL     + ". Ver historial de un socio");
        System.out.println(Constante.OPCION_RANKING       + ". Libros más solicitados");
        System.out.println(Constante.OPCION_VENCIDOS      + ". Préstamos vencidos");
        System.out.println(Constante.OPCION_SALIR         + ". Salir");
        System.out.print("Ingrese una opción: ");

        // validar que la entrada sea un número dentro del rango válido
        try {
            int opcion = Integer.parseInt(SC.nextLine());
            if (opcion >= Constante.OPCION_SALIR && opcion <= Constante.OPCION_VENCIDOS) {
                return opcion;
            } else {
                mostrarError("Opción fuera de rango.");
            }
        } catch (NumberFormatException e) {
            mostrarError("Entrada no válida. Por favor, ingrese un número.");
        }

        return 0;
    }

    public static String pedirIsbn() {
        System.out.print("Ingrese ISBN: ");
        // implementar
        return SC.nextLine();
    }

    public static String pedirNroSocio() {
        System.out.print("Ingrese número de socio: ");
        // implementar
        return SC.nextLine();
    }

    public static String pedirTitulo() {
        System.out.print("Ingrese título (o parte del título): ");
        // implementar
        return SC.nextLine();
    }

    public static String pedirAutor() {
        System.out.print("Ingrese nombre del autor: ");
        // implementar
        return SC.nextLine();
    }

    public static int pedirN() {
        System.out.print("Ingrese cantidad de libros a mostrar: ");
        // implementar
        return Integer.parseInt(SC.nextLine());
    }

    /**
     * Solicita una fecha al usuario en formato dd/MM/yyyy y la retorna
     * como LocalDate. Debe validar el formato antes de retornar.
     */
    public static LocalDate pedirFecha(String etiqueta) {
        System.out.print("Ingrese " + etiqueta + " (dd/MM/yyyy): ");
        // implementar y validar formato usando DateTimeFormatter FMT
        String fechaStr = SC.nextLine();
        try {
            LocalDate fecha = LocalDate.parse(fechaStr, FMT);
            return fecha;
        } catch (DateTimeParseException e) {
            mostrarError("Formato de fecha no válido. Por favor, ingrese la fecha en formato dd/MM/yyyy.");
        }
        return null;
    }

    // ── Métodos de presentación de resultados ──

    public static void mostrarLibro(Libro libro) {
        System.out.println("ISBN: " + libro.getIsbn());
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor());
        System.out.println("Disponible: " + (libro.getEjemplaresDisponibles() > 0 ? "Sí" : "No"));
    }

    public static void mostrarListaLibros(Iterable<Libro> libros) {
        for (Libro libro : libros) {
            mostrarLibro(libro);
            System.out.println("--------------------");
        }
    }

    public static void mostrarListaPrestamos(Iterable<Prestamo> prestamos) {
        for (Prestamo prestamo : prestamos) {
            System.out.println("  ===Socio=== \n" );
                System.out.println("Número de socio: " + prestamo.getSocio().getNroSocio());
                System.out.println("Nombre: " + prestamo.getSocio().getNombre() + " " + prestamo.getSocio().getApellido());
            System.out.println("  ===Libro=== \n");
                System.out.println("ISBN: " + prestamo.getLibro().getIsbn());
                System.out.println("Título: " + prestamo.getLibro().getTitulo());
                System.out.println("Autor: " + prestamo.getLibro().getAutor());
            System.out.println("  ===Prestamo=== \n");
                System.out.println("Fecha de préstamo: " + prestamo.getFechaPrestamo());
                System.out.println("Fecha de vencimiento: " + prestamo.getFechaVencimiento());
            System.out.println("--------------------");
        }
    }

    public static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public static void mostrarError(String mensaje) {
        System.err.println("ERROR: " + mensaje);
    }
}
