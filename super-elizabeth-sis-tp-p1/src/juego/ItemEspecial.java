package juego;

import java.awt.Color;

import entorno.Entorno;

/**
 * Item opcional que aparece en el escenario.
 *
 * Tipos:
 * - PUNTOS: suma un pez eliminado al marcador.
 * - TRAMPA: resta un punto si es posible.
 * - FUEGO_POTENCIADO: la proxima bola de fuego hace mas dano.
 * - ESCUDO: protege temporalmente de peces y bombas.
 */
public class ItemEspecial extends Entidad {

    public static final int PUNTOS = 0;
    public static final int TRAMPA = 1;
    public static final int FUEGO_POTENCIADO = 2;
    public static final int ESCUDO = 3;

    private int tipo;

    public ItemEspecial(double x, double y, int tipo) {
        super(x, y, 28, 28);
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void actualizar() {
        x -= Configuracion.VELOCIDAD_ESCENARIO;
    }

    public void dibujar(Entorno entorno) {
        Color color = Color.WHITE;
        String texto = "?";

        if (tipo == PUNTOS) {
            color = new Color(120, 230, 120);
            texto = "+";
        } else if (tipo == TRAMPA) {
            color = new Color(120, 80, 160);
            texto = "-";
        } else if (tipo == FUEGO_POTENCIADO) {
            color = new Color(255, 130, 20);
            texto = "F";
        } else if (tipo == ESCUDO) {
            color = new Color(90, 170, 255);
            texto = "E";
        }

        entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
        entorno.cambiarFont("Arial", 16, Color.BLACK);
        entorno.escribirTexto(texto, x - 5, y + 6);
    }
}
