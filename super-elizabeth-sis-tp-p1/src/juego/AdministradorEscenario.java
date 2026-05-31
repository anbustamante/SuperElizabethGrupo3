package juego;

import java.util.Random;

import entorno.Entorno;

/**
 * Administra piso e islas flotantes.
 *
 * Esta clase ayuda a que Juego no tenga demasiadas responsabilidades.
 * Juego pregunta por el arreglo de plataformas para resolver colisiones,
 * pero la creacion, movimiento y dibujo del escenario quedan aca.
 */
public class AdministradorEscenario {

    private Plataforma[] plataformas;
    private Random random;

    public AdministradorEscenario() {
        this.plataformas = new Plataforma[Configuracion.MAX_PLATAFORMAS];
        this.random = new Random();
        crearEscenarioInicial();
    }

    public Plataforma[] getPlataformas() {
        return plataformas;
    }

    private void crearEscenarioInicial() {
        // Piso inicial con al menos 2 huecos visibles.
        agregarPlataforma(new Plataforma(120, 570, 240, 60, true));
        agregarPlataforma(new Plataforma(420, 570, 160, 60, true));
        agregarPlataforma(new Plataforma(700, 570, 280, 60, true));

        // Al menos 3 islas flotantes, como pide el enunciado.
        agregarPlataforma(new Plataforma(250, 405, 145, 28, false));
        agregarPlataforma(new Plataforma(520, 310, 155, 28, false));
        agregarPlataforma(new Plataforma(710, 430, 130, 28, false));
    }

    public void actualizar() {
        for (int i = 0; i < plataformas.length; i++) {
            if (plataformas[i] != null) {
                plataformas[i].moverConEscenario(Configuracion.VELOCIDAD_ESCENARIO);
                if (plataformas[i].estaFueraDePantallaPorIzquierda()) {
                    plataformas[i] = null;
                }
            }
        }

        generarPisoSiHaceFalta();
        generarIslasSiHaceFalta();
    }

    public void dibujar(Entorno entorno) {
        for (int i = 0; i < plataformas.length; i++) {
            if (plataformas[i] != null) {
                plataformas[i].dibujar(entorno);
            }
        }
    }

    private void agregarPlataforma(Plataforma plataforma) {
        for (int i = 0; i < plataformas.length; i++) {
            if (plataformas[i] == null) {
                plataformas[i] = plataforma;
                return;
            }
        }
    }

    private void generarPisoSiHaceFalta() {
        double bordeDerechoMasLejano = obtenerBordeDerechoMasLejanoDelPiso();

        if (bordeDerechoMasLejano < Configuracion.ANCHO_VENTANA + 250) {
            double ancho = 210 + random.nextInt(130);
            double hueco = 120 + random.nextInt(100);
            double x = bordeDerechoMasLejano + hueco + ancho / 2;
            agregarPlataforma(new Plataforma(x, 570, ancho, 60, true));
        }
    }

    private void generarIslasSiHaceFalta() {
        int cantidadIslas = contarIslas();
        if (cantidadIslas >= 4) {
            return;
        }

        double x = Configuracion.ANCHO_VENTANA + 150 + random.nextInt(250);
        double y = 260 + random.nextInt(190);
        double ancho = 120 + random.nextInt(90);
        agregarPlataforma(new Plataforma(x, y, ancho, 28, false));
    }

    private double obtenerBordeDerechoMasLejanoDelPiso() {
        double maximo = Configuracion.ANCHO_VENTANA;
        for (int i = 0; i < plataformas.length; i++) {
            if (plataformas[i] != null && plataformas[i].esPiso()) {
                if (plataformas[i].derecha() > maximo) {
                    maximo = plataformas[i].derecha();
                }
            }
        }
        return maximo;
    }

    private int contarIslas() {
        int cantidad = 0;
        for (int i = 0; i < plataformas.length; i++) {
            if (plataformas[i] != null && !plataformas[i].esPiso()) {
                cantidad++;
            }
        }
        return cantidad;
    }
}
