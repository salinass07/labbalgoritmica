¿Dónde guardar los préstamos activos?

Los préstamos activos se guardan en la estructura `ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos`, donde la clave es el número de socio y el valor es una lista posicional enlazada con todos sus préstamos. Para distinguir cuáles están activos, cada objeto `Prestamo` posee un atributo booleano `activo` que se consulta mediante `isActivo()`. Al registrar un préstamo se inicializa en `true`, y al devolver el libro se actualiza a `false`, de modo que el préstamo permanece en la lista pero queda marcado como inactivo.

¿Cómo modelar la lista de espera por libro?

La lista de espera se modela con la estructura `ProbeHashMap<String, LinkedQueue<Socio>> colasEspera`, donde la clave es el ISBN del libro y el valor es una cola enlazada de socios. Se eligió una cola porque respeta el orden de llegada (FIFO): el primer socio en solicitar el libro es el primero en recibirlo cuando haya un ejemplar disponible. Cuando un socio pide un libro sin ejemplares disponibles se lo agrega al final de la cola con `enqueue()`, y cuando se produce una devolución se extrae al primero con `dequeue()` y se le asigna el préstamo automáticamente.

¿Dónde guardar el historial de préstamos por socio?

El historial se guarda en la misma estructura que los préstamos activos, es decir, `ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos`. No se necesita una estructura separada porque los préstamos nunca se eliminan de la lista: al devolver un libro el préstamo se marca como inactivo pero permanece registrado. De esta forma la lista de cada socio acumula tanto los préstamos actuales como los históricos, y es posible recorrerla completa para obtener el historial ordenado cronológicamente.