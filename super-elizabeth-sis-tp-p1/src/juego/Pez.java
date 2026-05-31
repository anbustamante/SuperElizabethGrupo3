package juego;

import java.awt.Color;

import entorno.Entorno;

/**
 * Pez del infierno.
 *
 * Esta clase concentra varios comportamientos opcionales usando un tipo.
 * Elegimos esta solucion para que sea facil de leer en Programacion I:
 * en vez de tener muchas subclases, cada tipo modifica su movimiento en
 * un metodo bien localizado.
 */
public class Pez extends Entidad {

    public static final int NORMAL = 0;
    public static final int PERSEGUIDOR = 1;
    public static final int ESQUIVADOR = 2;
    public static final int ONDULANTE = 3;
    public static final int RESISTENTE = 4;
    public static final int COLOSAL = 5;

    private int tipo;
    private int vida;
    private double velocidad;
    private double yBase;
    private int ticksVivo;
    private int ticksParaDisparar;

    public Pez(double x, double y, int tipo, int nivel) {
        super(x, y, tipo == COLOSAL ? 120 : 46, tipo == COLOSAL ? 90 : 36);
        this.tipo = tipo;
        this.yBase = y;
        this.ticksVivo = 0;
        this.ticksParaDisparar = 70;

        if (tipo == RESISTENTE) {
            this.vida = 2 + nivel;
        } else if (tipo == COLOSAL) {
            this.vida = 12 + nivel * 2;
        } else {
            this.vida = 1;
        }

        this.velocidad = 2.0 + nivel * 0.35;

        if (tipo == PERSEGUIDOR) {
            this.velocidad += 0.25;
        }
        if (tipo == ESQUIVADOR) {
            this.velocidad += 0.15;
        }
        if (tipo == COLOSAL) {
            this.velocidad = 0.0;
        }
    }

    public int getTipo() {
        return tipo;
    }

    public int getVida() {
        return vida;
    }

    public boolean esColosal() {
        return tipo == COLOSAL;
    }

    public void recibirDano(int dano) {
        vida -= dano;
    }

    public boolean estaMuerto() {
        return vida <= 0;
    }

    /**
     * Devuelve true cuando este pez esta listo para arrojar una bomba.
     * La creacion real de la bomba se hace desde AdministradorPeces.
     */
    public boolean debeDisparar() {
        return ticksParaDisparar <= 0;
    }

    public void reiniciarEsperaDeDisparo() {
        ticksParaDisparar = tipo == COLOSAL ? 45 : 110;
    }

    public void actualizar(Elizabeth elizabeth, BolaFuego bolaFuego) {
        ticksVivo++;
        ticksParaDisparar--;

        if (tipo == COLOSAL) {
            actualizarColosal();
            return;
        }

        x -= velocidad;

        if (tipo == PERSEGUIDOR) {
            perseguirAElizabeth(elizabeth);
        } else if (tipo == ESQUIVADOR) {
            esquivarBolaDeFuego(bolaFuego);
        } else if (tipo == ONDULANTE) {
            y = yBase + Math.sin(ticksVivo * 0.08) * 45;
        }
    }

    private void perseguirAElizabeth(Elizabeth elizabeth) {
        if (elizabeth.getY() < y) {
            y -= 1.2;
        } else if (elizabeth.getY() > y) {
            y += 1.2;
        }
    }

    private void esquivarBolaDeFuego(BolaFuego bolaFuego) {
        if (bolaFuego == null) {
            return;
        }

        double distancia = distanciaA(bolaFuego);
        if (distancia < 130) {
            if (bolaFuego.getY() < y) {
                y += 2.1;
            } else {
                y -= 2.1;
            }
        }

        // No lo dejamos salir completamente por arriba/abajo.
        if (y < 80) {
            y = 80;
        }
        if (y > 500) {
            y = 500;
        }
    }

    private void actualizarColosal() {
        // El jefe final se queda del lado derecho y se mueve verticalmente.
        x = Configuracion.ANCHO_VENTANA - 85;
        y = 250 + Math.sin(ticksVivo * 0.035) * 150;
    }

    public void separarVerticalmente(double distancia) {
        y += distancia;
        if (y < 70) {
            y = 70;
        }
        if (y > 515) {
            y = 515;
        }
    }

    public void dibujar(Entorno entorno) {
        Color cuerpo = colorPorTipo();
        Color panza = new Color(255, 245, 210);
        Color cola = new Color(255, 180, 40);
        Color ojo = Color.BLACK;

        // Cuerpo: varios circulos dan apariencia de pez sin usar imagenes externas.
        entorno.dibujarCirculo(x, y, ancho, cuerpo);
        entorno.dibujarCirculo(x + ancho * 0.15, y + alto * 0.10, ancho * 0.45, panza);

        // Cola. El angulo PI hace que el triangulo apunte hacia la izquierda.
        entorno.dibujarTriangulo(x + ancho * 0.55, y, (int) (alto * 0.9), (int) (ancho * 0.45), Math.PI, cola);

        // Ojo.
        entorno.dibujarCirculo(x - ancho * 0.20, y - alto * 0.15, Math.max(5, ancho * 0.12), ojo);

        // En peces resistentes o jefe se muestra la vida restante para que el jugador entienda
        // por que no mueren de un solo disparo.
        if (vida > 1) {
            entorno.cambiarFont("Arial", 12, Color.WHITE);
            entorno.escribirTexto(String.valueOf(vida), x - 5, y + 5);
        }
    }

    private Color colorPorTipo() {
        if (tipo == PERSEGUIDOR) {
            return new Color(255, 90, 80);
        }
        if (tipo == ESQUIVADOR) {
            return new Color(140, 210, 255);
        }
        if (tipo == ONDULANTE) {
            return new Color(200, 110, 255);
        }
        if (tipo == RESISTENTE) {
            return new Color(255, 160, 40);
        }
        if (tipo == COLOSAL) {
            return new Color(120, 35, 35);
        }
        return new Color(255, 55, 45);
    }
}
