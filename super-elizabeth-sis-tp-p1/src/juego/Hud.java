package juego;

import java.awt.Color;

import entorno.Entorno;

/**
 * Dibuja la informacion de pantalla.
 *
 * Separarlo en una clase propia evita mezclar logica de juego con textos.
 */
public class Hud {

    public void dibujarDuranteJuego(Entorno entorno, int pecesEliminados, int nivel, boolean fuegoPotenciado,
            boolean escudoActivo, long msRestantesHechizo) {

        entorno.cambiarFont("Arial", 16, Color.WHITE);
        entorno.escribirTexto("Peces eliminados: " + pecesEliminados, 15, 25);
        entorno.escribirTexto("Restan: " + Math.max(0, Configuracion.PECES_PARA_GANAR - pecesEliminados), 15, 48);
        entorno.escribirTexto("Nivel: " + nivel, 15, 71);

        String fuego = fuegoPotenciado ? "Fuego potenciado: SI" : "Fuego potenciado: NO";
        entorno.escribirTexto(fuego, 560, 25);

        String escudo = escudoActivo ? "Escudo: activo" : "Escudo: no";
        entorno.escribirTexto(escudo, 560, 48);

        if (Configuracion.OPCIONAL_HECHIZO) {
            if (msRestantesHechizo <= 0) {
                entorno.escribirTexto("Hechizo: listo", 560, 71);
            } else {
                entorno.escribirTexto("Hechizo: " + (msRestantesHechizo / 1000 + 1) + "s", 560, 71);
            }
        }

        entorno.cambiarFont("Arial", 12, Color.WHITE);
        entorno.escribirTexto("Flechas: mover/saltar | Click izq.: fuego | Click der.: hechizo | R: reiniciar", 15, 590);
    }

    public void dibujarPantallaFinal(Entorno entorno, EstadoJuego estado, int pecesEliminados) {
        entorno.cambiarFont("Arial", 34, Color.WHITE, entorno.NEGRITA);

        if (estado == EstadoJuego.GANADO) {
            entorno.escribirTexto("GANASTE", 315, 250);
        } else {
            entorno.escribirTexto("PERDISTE", 305, 250);
        }

        entorno.cambiarFont("Arial", 18, Color.WHITE);
        entorno.escribirTexto("Peces eliminados: " + pecesEliminados, 300, 295);
        entorno.escribirTexto("Presiona R para reiniciar", 292, 325);
    }
}
