package juego;

import java.awt.Color;

import entorno.Entorno;

/**
 * Bomba lanzada por los peces.
 *
 * Opcional: las bombas pueden destruirse con la bola de fuego.
 */
public class Bomba extends Entidad {

    private double velocidadX;
    private double velocidadY;

    public Bomba(double x, double y, double destinoX, double destinoY) {
        super(x, y, 16, 16);

        double dx = destinoX - x;
        double dy = destinoY - y;
        double modulo = Math.sqrt(dx * dx + dy * dy);

        if (modulo == 0) {
            dx = -1;
            dy = 0;
            modulo = 1;
        }

        double velocidad = 4.3;
        this.velocidadX = dx / modulo * velocidad;
        this.velocidadY = dy / modulo * velocidad;
    }

    public void actualizar() {
        x += velocidadX;
        y += velocidadY;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarCirculo(x, y, ancho, Color.DARK_GRAY);
        entorno.dibujarCirculo(x - 3, y - 3, ancho * 0.35, Color.LIGHT_GRAY);
    }
}
