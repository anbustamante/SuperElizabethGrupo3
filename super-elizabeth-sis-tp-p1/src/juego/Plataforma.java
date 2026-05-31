package juego;

import java.awt.Color;

import entorno.Entorno;

/**
 * Representa una parte del piso o una isla flotante.
 *
 * Responsabilidad:
 * - Guardar su posicion y tamano.
 * - Moverse con el escenario.
 * - Dibujarse.
 */
public class Plataforma extends Entidad {

    private boolean esPiso;

    public Plataforma(double x, double y, double ancho, double alto, boolean esPiso) {
        super(x, y, ancho, alto);
        this.esPiso = esPiso;
    }

    public boolean esPiso() {
        return esPiso;
    }

    public void moverConEscenario(double velocidadEscenario) {
        this.x -= velocidadEscenario;
    }

    public void dibujar(Entorno entorno) {
        if (esPiso) {
            dibujarPiso(entorno);
        } else {
            dibujarIsla(entorno);
        }
    }

    private void dibujarPiso(Entorno entorno) {
        Color tierra = new Color(116, 72, 38);
        Color borde = new Color(78, 48, 26);
        Color piedra = new Color(205, 178, 132);

        entorno.dibujarRectangulo(x, y, ancho, alto, 0, tierra);
        entorno.dibujarRectangulo(x, arriba() + 4, ancho, 8, 0, borde);

        // Piedras decorativas. No afectan la logica, solo ayudan a leer el escenario.
        for (int i = 0; i < ancho / 35; i++) {
            double px = izquierda() + 20 + i * 35;
            entorno.dibujarCirculo(px, y, 13, piedra);
        }
    }

    private void dibujarIsla(Entorno entorno) {
        Color base = new Color(67, 176, 71);
        Color borde = new Color(33, 120, 40);
        Color luz = new Color(251, 222, 72);

        entorno.dibujarRectangulo(x, y, ancho, alto, 0, base);
        entorno.dibujarRectangulo(x, arriba() + 5, ancho, 10, 0, borde);

        for (int i = 0; i < ancho / 30; i++) {
            double px = izquierda() + 18 + i * 30;
            entorno.dibujarCirculo(px, y, 12, luz);
        }
    }
}
