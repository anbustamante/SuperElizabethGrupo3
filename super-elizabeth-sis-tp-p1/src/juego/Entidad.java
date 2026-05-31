package juego;

/**
 * Clase base para objetos con posicion y tamano.
 *
 * No dibuja nada por si misma. Solo guarda datos comunes y provee
 * metodos simples de colision rectangular.
 */
public class Entidad {

    protected double x;
    protected double y;
    protected double ancho;
    protected double alto;

    public Entidad(double x, double y, double ancho, double alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public double izquierda() {
        return x - ancho / 2;
    }

    public double derecha() {
        return x + ancho / 2;
    }

    public double arriba() {
        return y - alto / 2;
    }

    public double abajo() {
        return y + alto / 2;
    }

    /**
     * Colision rectangular simple.
     * Sirve muy bien para un TP introductorio porque es facil de explicar:
     * dos rectangulos colisionan si se pisan en X y tambien en Y.
     */
    public boolean colisionaCon(Entidad otra) {
        if (otra == null) {
            return false;
        }

        boolean sePisanEnX = this.izquierda() < otra.derecha() && this.derecha() > otra.izquierda();
        boolean sePisanEnY = this.arriba() < otra.abajo() && this.abajo() > otra.arriba();

        return sePisanEnX && sePisanEnY;
    }

    public double distanciaA(Entidad otra) {
        double dx = this.x - otra.x;
        double dy = this.y - otra.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean estaFueraDePantallaPorIzquierda() {
        return derecha() < 0;
    }

    public boolean estaFueraDePantalla() {
        return derecha() < 0 || izquierda() > Configuracion.ANCHO_VENTANA
                || abajo() < 0 || arriba() > Configuracion.ALTO_VENTANA;
    }
}
