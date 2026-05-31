package juego;

/**
 * Estados generales del juego.
 *
 * Usar un enum evita comparar Strings y hace mas claro el flujo:
 * - JUGANDO: se actualiza la simulacion.
 * - GANADO: se muestra pantalla de victoria.
 * - PERDIDO: se muestra pantalla de derrota.
 */
public enum EstadoJuego {
    JUGANDO,
    GANADO,
    PERDIDO
}
