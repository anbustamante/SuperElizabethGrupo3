package juego;

import java.awt.Color;

import entorno.Entorno;

/**
 * Personaje principal controlado por el jugador.
 *
 * Responsabilidad:
 * - Leer teclas de movimiento.
 * - Aplicar una fisica simple: gravedad, salto y velocidad horizontal.
 * - Dibujarse.
 *
 * La colision contra plataformas se resuelve desde Juego, porque Juego conoce
 * el arreglo de plataformas completo.
 */
public class Elizabeth extends Entidad {

    private double velocidadX;
    private double velocidadY;
    private boolean apoyada;
    private boolean mirandoDerecha;
    private double yAnterior;

    public Elizabeth(double x, double y) {
        super(x, y, 34, 58);
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.apoyada = false;
        this.mirandoDerecha = true;
        this.yAnterior = y;
    }

    public boolean estaApoyada() {
        return apoyada;
    }

    public void setApoyada(boolean apoyada) {
        this.apoyada = apoyada;
    }

    public double getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    public double getYAnterior() {
        return yAnterior;
    }

    public boolean miraDerecha() {
        return mirandoDerecha;
    }

    /**
     * Actualiza el movimiento de Elizabeth segun las teclas.
     *
     * Regla del TP:
     * - Si no se presiona nada, la princesa se mueve con el escenario.
     * - Si se presiona derecha, avanza.
     * - Si se presiona izquierda, retrocede.
     * - Si se presiona arriba y esta apoyada, salta.
     */
    public void actualizarMovimiento(Entorno entorno) {
        yAnterior = y;

        velocidadX = -Configuracion.VELOCIDAD_ESCENARIO;

        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            velocidadX -= Configuracion.VELOCIDAD_CAMINAR;
            mirandoDerecha = false;
        }

        if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            velocidadX += Configuracion.VELOCIDAD_CAMINAR;
            mirandoDerecha = true;
        }

        if (entorno.sePresiono(entorno.TECLA_ARRIBA) && apoyada) {
            velocidadY = Configuracion.VELOCIDAD_SALTO;
            apoyada = false;
        }

        if (!apoyada) {
            velocidadY += Configuracion.GRAVEDAD;
            if (velocidadY > Configuracion.VELOCIDAD_MAXIMA_CAIDA) {
                velocidadY = Configuracion.VELOCIDAD_MAXIMA_CAIDA;
            }
        }

        x += velocidadX;
        y += velocidadY;

        // No se permite salir por los laterales.
        if (izquierda() < 0) {
            x = ancho / 2;
        }
        if (derecha() > Configuracion.ANCHO_VENTANA) {
            x = Configuracion.ANCHO_VENTANA - ancho / 2;
        }
    }

    /**
     * Se llama cuando Elizabeth cae sobre una plataforma.
     */
    public void apoyarSobre(double coordenadaSuperiorDePlataforma) {
        y = coordenadaSuperiorDePlataforma - alto / 2;
        velocidadY = 0;
        apoyada = true;
    }

    public void dibujar(Entorno entorno, boolean tieneEscudo) {
        Color vestido = new Color(245, 93, 169);
        Color piel = new Color(245, 210, 165);
        Color pelo = new Color(245, 220, 40);
        Color corona = new Color(255, 215, 0);
        Color ojo = Color.BLACK;

        // Cuerpo y cabeza.
        entorno.dibujarRectangulo(x, y + 8, 28, 38, 0, vestido);
        entorno.dibujarCirculo(x, y - 20, 24, piel);

        // Pelo y corona.
        entorno.dibujarCirculo(x - 6, y - 25, 12, pelo);
        entorno.dibujarCirculo(x + 5, y - 26, 12, pelo);
        entorno.dibujarTriangulo(x, y - 42, 14, 18, 0, corona);

        // Ojo mirando hacia la direccion actual.
        double ojoX = mirandoDerecha ? x + 6 : x - 6;
        entorno.dibujarCirculo(ojoX, y - 21, 4, ojo);

        // Piernas.
        entorno.dibujarRectangulo(x - 8, y + 34, 8, 20, 0, new Color(90, 55, 35));
        entorno.dibujarRectangulo(x + 8, y + 34, 8, 20, 0, new Color(90, 55, 35));

        // Escudo opcional.
        if (tieneEscudo) {
            entorno.dibujarCirculo(x, y, 75, new Color(90, 170, 255, 90));
            entorno.dibujarCirculo(x, y, 68, new Color(145, 210, 255, 70));
        }
    }
}
