package biblioteca.logica;

import java.time.LocalDate;

import net.datastructures.ProbeHashMap;
import net.datastructures.LinkedPositionalList;
import net.datastructures.LinkedQueue;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Socio;
import biblioteca.modelo.Prestamo;
import java.util.ArrayList;
import java.util.Map;
import java.util.AbstractMap;

public class Logica {

    private ProbeHashMap<String, Libro> catalogo;
    private ProbeHashMap<String, Socio> socios;
    // TODO: definir las estructuras adicionales que necesite
    // Pensar: ¿dónde guardar los préstamos activos?
    // Pensar: ¿cómo modelar la lista de espera por libro?
    // Pensar: ¿dónde guardar el historial de préstamos por socio?
    private ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos;
    private ProbeHashMap<String, LinkedQueue<Socio>> colasEspera;

    public Logica(ProbeHashMap<String, Libro> catalogo,
                ProbeHashMap<String, Socio> socios,
                ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos, 
            ProbeHashMap<String, LinkedQueue<Socio>> colasEspera) {
        this.catalogo = catalogo;
        this.socios   = socios;
        //  inicializar las estructuras internas a partir de los datos recibidos
        this.prestamos = prestamos;
        this.colasEspera = colasEspera;
        
    }

    // ── INCREMENTO 1 ──────────────────────────────────────────────

    /**
     * Registra el préstamo de un libro a un socio.
     * La fecha de préstamo es la fecha actual y el vencimiento se calcula
     * automáticamente (14 días).
     * Condiciones: el socio debe estar activo y debe haber ejemplares disponibles.
     * @return true si el préstamo se realizó, false en caso contrario
     */
    public boolean prestar(String nroSocio, String isbn) {
        // implementar
        Socio socio = socios.get(nroSocio);
        Libro libro = catalogo.get(isbn);

        if (libro != null && libro.getEjemplaresDisponibles() == 0 && socio != null && socio.isActivo()) {
            agregarEspera(nroSocio, isbn);
            return false; // No se pudo realizar el préstamo, pero se agregó a la espera
        }

        if (socio != null && socio.isActivo() && libro != null && libro.getEjemplaresDisponibles() > 0) {
            // implementar lógica de préstamo
            libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
            LocalDate fechaPrestamo = LocalDate.now();
            LocalDate fechaVencimiento = fechaPrestamo.plusDays(14);
            Prestamo prestamo = new Prestamo(socio, libro, fechaPrestamo, fechaVencimiento);
            // agregar el préstamo a la lista de préstamos activos
            LinkedPositionalList<Prestamo> listaPrestamos = prestamos.get(nroSocio);
            if (listaPrestamos == null) {
                listaPrestamos = new LinkedPositionalList<>();
                prestamos.put(nroSocio, listaPrestamos);
            }
            listaPrestamos.addLast(prestamo);
            return true;
        }
        return false;
    }

    /**
     * Registra la devolución de un libro.
     * Actualiza el estado del préstamo y la disponibilidad del libro.
     * @return true si la devolución se realizó, false en caso contrario
     */
    public boolean devolver(String nroSocio, String isbn) {
        // implementar
            Socio socio = socios.get(nroSocio);
            Libro libro = catalogo.get(isbn);

            // si hay una devolución, se verifica si el socio tiene un préstamo activo de ese libro
            // si lo tiene, se marca como devuelto, se actualiza la disponibilidad del libro
            //  y se asigna al siguiente en espera (si existe)
            if (libro != null && socio != null && socio.isActivo()) {
                LinkedPositionalList<Prestamo> listaPrestamos = prestamos.get(nroSocio);
                if (listaPrestamos != null) {
                    for (Prestamo prestamo : listaPrestamos) {
                        if (prestamo.getLibro().equals(libro) && prestamo.isActivo()) {
                            prestamo.setActivo(false);
                            libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
                            asignarSiguienteEnEspera(isbn); // Asignar al siguiente en espera si existe
                            return true;
                        }
                    }
                }
            }
            
        return false;
    }

    /**
     * Busca un libro por su ISBN.
     * @return el Libro encontrado, o null si no existe
     */
    public Libro buscarPorIsbn(String isbn) {
        //implementar
        Libro libro =  catalogo.get(isbn);
        if (libro != null) {
            return libro;
        }
        return null;
    }

    /**
     * Busca libros cuyo título contenga la cadena indicada (sin distinguir mayúsculas).
     */
    public LinkedPositionalList<Libro> buscarPorTitulo(String titulo) {
        // implementar
        LinkedPositionalList<Libro> librosEncontrados = new LinkedPositionalList<>();
        for (Libro libro : catalogo.values()) {
            if (libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                librosEncontrados.addLast(libro);
            }
        }
        return librosEncontrados;
    }

    /**
     * Busca libros de un autor dado (sin distinguir mayúsculas).
     */
    public LinkedPositionalList<Libro> buscarPorAutor(String autor) {
        // implementar
        LinkedPositionalList<Libro> librosEncontrados = new LinkedPositionalList<>();
        for (Libro libro : catalogo.values()) {
            if (libro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                librosEncontrados.addLast(libro);
            }
        }
        return librosEncontrados;
    }

    /**
     * Retorna todos los libros con al menos un ejemplar disponible.
     */
    public LinkedPositionalList<Libro> listarDisponibles() {
        // implementar
        LinkedPositionalList<Libro> librosDisponibles = new LinkedPositionalList<>();
        for (Libro libro : catalogo.values()) {
            if (libro.getEjemplaresDisponibles() > 0) {
                librosDisponibles.addLast(libro);
            }
        }
        return librosDisponibles;
    }

    /**
     * Retorna los préstamos activos de un socio.
     */
    public LinkedPositionalList<Prestamo> prestamosActivosDeSocio(String nroSocio) {
        // implementar
        LinkedPositionalList<Prestamo> listaPrestamos = prestamos.get(nroSocio);
        if (listaPrestamos != null) {
            return listaPrestamos;
        }
        return new LinkedPositionalList<>();
    }

    // ── INCREMENTO 2 ──────────────────────────────────────────────

    /**
     * Agrega un socio a la cola de espera de un libro.
     * Se invoca cuando no hay ejemplares disponibles al momento del pedido.
     */
    public void agregarEspera(String nroSocio, String isbn) {
        // implementar
        Socio socio = socios.get(nroSocio);
        Libro libro = catalogo.get(isbn);
        if (socio != null && libro != null) {
            LinkedQueue<Socio> colaEspera = colasEspera.get(isbn);
            if (colaEspera == null) {
                colaEspera = new LinkedQueue<>();
                colasEspera.put(isbn, colaEspera);
            }
            colaEspera.enqueue(socio);
        }
        System.out.println("El libro '" + libro.getTitulo() + "' no tiene ejemplares disponibles. " +
        "El socio " + socio.getNombre() + " " + socio.getApellido() + " (Nro Socio: " +
        socio.getNroSocio() + ") ha sido agregado a la lista de espera.");
    }

    /**
     * Al devolver un libro, si hay socios en espera, asigna el ejemplar
     * automáticamente al primero en la cola y lo notifica.
     */
    public void asignarSiguienteEnEspera(String isbn) {
        // implementar
        Libro libro = catalogo.get(isbn);
        if (libro != null) {
            LinkedQueue<Socio> colaEspera = colasEspera.get(isbn);
            if (colaEspera != null && !colaEspera.isEmpty()) {
                Socio socio = colaEspera.dequeue();
                prestar(socio.getNroSocio(), isbn);
                System.out.println("El libro '" + libro.getTitulo() + "' ha sido asignado al socio " +
                socio.getNombre() + " " + socio.getApellido() + " (Nro Socio: " +
                socio.getNroSocio() + ") desde la lista de espera.");
            }
        }
    }

    /**
     * Retorna el historial completo de préstamos de un socio
     * (activos e históricos), en orden cronológico.
     */
    public LinkedPositionalList<Prestamo> historialDeSocio(String nroSocio) {
        LinkedPositionalList<Prestamo> listaPrestamos = prestamos.get(nroSocio);
        if (listaPrestamos == null || listaPrestamos.isEmpty()) {
            return new LinkedPositionalList<>();
        }

        // Copiar a ArrayList para ordenar
        ArrayList<Prestamo> prestamosList = new ArrayList<>();
        for (Prestamo p : listaPrestamos) {
            prestamosList.add(p);
        }

        // Ordenar por fecha de préstamo (ascendente)
        prestamosList.sort(java.util.Comparator.comparing(Prestamo::getFechaPrestamo));

        // Volcar a LinkedPositionalList
        LinkedPositionalList<Prestamo> historialOrdenado = new LinkedPositionalList<>();
        for (Prestamo p : prestamosList) {
            historialOrdenado.addLast(p);
        }
        return historialOrdenado;
    }

    /**
     * Retorna los N libros más solicitados (préstamos activos + históricos).
     * @param n cantidad de libros a retornar
     */
    public LinkedPositionalList<Libro> librosMasSolicitados(int n) {
        if (n <= 0) {
            return new LinkedPositionalList<>();
        }

        // Contar préstamos por ISBN
        ProbeHashMap<String, Integer> conteoPrestamos = new ProbeHashMap<>();
        for (LinkedPositionalList<Prestamo> prestamos : prestamos.values()) {
            for (Prestamo prestamo : prestamos) {
                String isbn = prestamo.getLibro().getIsbn();
                Integer count = conteoPrestamos.get(isbn);
                if (count == null) {
                    conteoPrestamos.put(isbn, 1);
                } else {
                    conteoPrestamos.put(isbn, count + 1);
                }
            }
        }

        // Pasar a ArrayList para ordenar
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>();
        for (String isbn : conteoPrestamos.keySet()) {
            entries.add(new AbstractMap.SimpleEntry<>(isbn, conteoPrestamos.get(isbn)));
        }

        // Ordenar por frecuencia descendente (y por ISBN si hay empate)
        entries.sort((e1, e2) -> {
            int cmp = e2.getValue().compareTo(e1.getValue()); // descendente
            if (cmp == 0) {
                return e1.getKey().compareTo(e2.getKey()); // ascendente por ISBN
            }
            return cmp;
        });

        // Tomar los primeros n (o todos si hay menos)
        LinkedPositionalList<Libro> librosMasSolicitados = new LinkedPositionalList<>();
        int limit = Math.min(n, entries.size());
        for (int i = 0; i < limit; i++) {
            String isbn = entries.get(i).getKey();
            Libro libro = catalogo.get(isbn);
            if (libro != null) {
                librosMasSolicitados.addLast(libro);
            }
        }
        return librosMasSolicitados;
    }

    /**
     * Retorna todos los préstamos cuya fecha de vencimiento expiró
     * y que aún no fueron devueltos.
     * // ejemplo: si hoy es 15/06/2026, se deben retornar los préstamos con fecha de vencimiento anterior a 15/06/2026
     * @param hoy fecha actual
     */
    public LinkedPositionalList<Prestamo> prestamosVencidos(LocalDate hoy) {
        //implementar
            LinkedPositionalList<Prestamo> prestamosVencidos = new LinkedPositionalList<>();
                for (LinkedPositionalList<Prestamo> prestamos : prestamos.values()) {
                    for (Prestamo prestamo : prestamos) {
                        if (prestamo.isActivo() && prestamo.getFechaVencimiento().isBefore(hoy)) {
                            prestamosVencidos.addLast(prestamo);
                        }
                    }
                }
        return prestamosVencidos;
    }
}
