package biblioteca.aplicacion;

import java.io.IOException;
import java.time.LocalDate;

import net.datastructures.LinkedPositionalList;
import net.datastructures.LinkedQueue;
import net.datastructures.ProbeHashMap;
import biblioteca.datos.CargarParametros;
import biblioteca.datos.Dato;
import biblioteca.interfaz.Interfaz;
import biblioteca.logica.Logica;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Socio;

public class Aplicacion {

    public static void main(String[] args) {

        // 1. Cargar parámetros de configuración
        try {
            CargarParametros.parametros();
        } catch (IOException e) {
            System.err.println("Error al cargar config.properties");
            System.exit(-1);
        }

        // 2. Cargar datos desde archivos
        ProbeHashMap<String, Libro>   catalogo  = null;
        ProbeHashMap<String, Socio>   socios    = null;
        ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos = null;
        ProbeHashMap<String, LinkedQueue<Socio>> colasEspera = null;

        try {
            catalogo  = Dato.cargarLibros(CargarParametros.getArchivoLibros());
            socios    = Dato.cargarSocios(CargarParametros.getArchivoSocios());
            prestamos = Dato.cargarPrestamos(CargarParametros.getArchivoPrestamos(),
                                            socios, catalogo);
        } catch (Exception e) {
            System.err.println("Error al cargar archivos de datos: " + e.getMessage());
            System.exit(-1);
        }

        // 3. Inicializar capa lógica
        Logica logica = new Logica(catalogo, socios, prestamos, colasEspera);

        // 4. Ciclo principal de la aplicación
        int opcion;
        do {
            opcion = Interfaz.menu();

            switch (opcion) {
                case Constante.OPCION_PRESTAR: {
                    // pedir datos al usuario y llamar a logica.prestar(...)
                    String isbn = Interfaz.pedirIsbn();
                    String nroSocio = Interfaz.pedirNroSocio();
                    if (logica.prestar(nroSocio, isbn)) {
                        Interfaz.mostrarMensaje("Préstamo realizado con éxito.");
                    } else {
                        Interfaz.mostrarError("No se pudo realizar el préstamo.");
                    }
                    break;
                }
                case Constante.OPCION_DEVOLVER:{
                    // pedir datos al usuario y llamar a logica.devolver(...)
                    String isbn = Interfaz.pedirIsbn();
                    String nroSocio = Interfaz.pedirNroSocio();
                    if (logica.devolver(nroSocio, isbn)) {
                        Interfaz.mostrarMensaje("Devolución realizada con éxito.");
                    } else {
                        Interfaz.mostrarError("No se pudo realizar la devolución.");
                    }
                    break;
                }
                case Constante.OPCION_BUSCAR_ISBN:{
                    // pedir ISBN y mostrar resultado de logica.buscarPorIsbn(...)
                    String isbn = Interfaz.pedirIsbn();
                    Libro libro = logica.buscarPorIsbn(isbn);
                    if (libro != null) {
                        Interfaz.mostrarLibro(libro);
                    } else {
                        Interfaz.mostrarError("No se encontró un libro con ese ISBN.");
                    }
                    break;
                }

                case Constante.OPCION_BUSCAR_TITULO:{
                    // pedir título y mostrar resultados de logica.buscarPorTitulo(...)
                    String titulo = Interfaz.pedirTitulo();
                    LinkedPositionalList<Libro> resultados = logica.buscarPorTitulo(titulo);
                    if (resultados.isEmpty()) {
                        Interfaz.mostrarError("No se encontraron libros con ese título.");
                    } else {
                        Interfaz.mostrarListaLibros(logica.buscarPorTitulo(titulo));
                    }
                    break;
                    }
                case Constante.OPCION_BUSCAR_AUTOR:{
                    // pedir autor y mostrar resultados de logica.buscarPorAutor(...)
                    String autor = Interfaz.pedirAutor();
                    LinkedPositionalList<Libro> resultados = logica.buscarPorAutor(autor);
                    if (resultados.isEmpty()) {
                        Interfaz.mostrarError("No se encontraron libros de ese autor.");
                    } else {
                        Interfaz.mostrarListaLibros(logica.buscarPorAutor(autor));
                    }
                    break;
                }
                case Constante.OPCION_DISPONIBLES:{
                    // mostrar resultado de logica.listarDisponibles()
                    LinkedPositionalList<Libro> disponibles = logica.listarDisponibles();
                    if (disponibles.isEmpty()) {
                        Interfaz.mostrarMensaje("No hay libros disponibles en este momento.");
                    } else {
                        Interfaz.mostrarListaLibros(logica.listarDisponibles());
                    }
                    break;
                }
                case Constante.OPCION_PRESTAMOS_SOCIO:{
                    // pedir nroSocio y mostrar logica.prestamosActivosDeSocio(...)
                    String nroSocio = Interfaz.pedirNroSocio();
                    LinkedPositionalList<Prestamo> socioPrestamos = logica.prestamosActivosDeSocio(nroSocio);
                    if (socioPrestamos.isEmpty()) {
                        Interfaz.mostrarMensaje("El socio no tiene préstamos activos.");
                    } else {
                        Interfaz.mostrarListaPrestamos(socioPrestamos);
                    }
                    break;
                }
                case Constante.OPCION_HISTORIAL:
                    //pedir nroSocio y mostrar logica.historialDeSocio(...)
                    String nroSocio = Interfaz.pedirNroSocio();
                    LinkedPositionalList<Prestamo> historial = logica.historialDeSocio(nroSocio);
                    if (historial.isEmpty()) {
                        Interfaz.mostrarMensaje("El socio no tiene historial de préstamos.");
                    } else {
                        Interfaz.mostrarListaPrestamos(historial);
                    }
                    break;

                case Constante.OPCION_RANKING:
                    //pedir N y mostrar logica.librosMasSolicitados(N)
                    int n = Interfaz.pedirN();
                    LinkedPositionalList<Libro> ranking = logica.librosMasSolicitados(n);
                    if (ranking.isEmpty()) {
                        Interfaz.mostrarMensaje("No hay libros disponibles para mostrar en el ranking.");
                    } else {
                        Interfaz.mostrarListaLibros(ranking);
                    }
                    break;

                case Constante.OPCION_VENCIDOS:
                    // pedir fecha con Interfaz.pedirFecha(...) y mostrar
                    //       logica.prestamosVencidos(LocalDate)
                    LocalDate fecha = Interfaz.pedirFecha("Ingrese la fecha para verificar préstamos vencidos (formato: dd/MM/yyyy):");
                    LinkedPositionalList<Prestamo> vencidos = logica.prestamosVencidos(fecha);
                    if (vencidos.isEmpty()) {
                        Interfaz.mostrarMensaje("No hay préstamos vencidos para la fecha ingresada.");
                    } else {
                        Interfaz.mostrarListaPrestamos(vencidos);
                    }
                    break;

                case Constante.OPCION_SALIR:
                    Interfaz.mostrarMensaje("Hasta luego.");
                    break;

                default:
                    Interfaz.mostrarError("Opción no válida.");
            }

        } while (opcion != Constante.OPCION_SALIR);
    }
}
