package juego;

import java.awt.Color;

import entorno.Entorno;

/**
 * Disparo de Elizabeth.
 *
 * Regla obligatoria del TP:
 * solo puede existir una bola de fuego a la vez. Esa regla se controla
 * en Juego: si bolaFuego es null, se puede disparar; si no es null, no.
 */
public class BolaFuego extends Entidad {

    private double velocidadX;
    private double velocidadY;
    private int dano;

    public BolaFuego(double x, double y, double destinoX, double destinoY, boolean potenciada) {
        super(x, y, potenciada ? 20 : 14, potenciada ? 20 : 14);

        double dx = destinoX - x;
        double dy = destinoY - y;
        double modulo = Math.sqrt(dx * dx + dy * dy);

        if (modulo == 0) {
            dx = 1;
            dy = 0;
            modulo = 1;
        }

        double velocidad = potenciada ? 10.5 : 8.0;
        this.velocidadX = dx / modulo * velocidad;
        this.velocidadY = dy / modulo * velocidad;
        this.dano = potenciada ? 2 : 1;
    }

    public int getDano() {
        return dano;
    }

    public void actualizar() {
        x += velocidadX;
        y += velocidadY;
    }

    public void dibujar(Entorno entorno) {
        Color borde = new Color(255, 65, 0);
        Color centro = new Color(255, 210, 45);
        entorno.dibujarCirculo(x, y, ancho, borde);
        entorno.dibujarCirculo(x, y, ancho * 0.6, centro);
    }
}
