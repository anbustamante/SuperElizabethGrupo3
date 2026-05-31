package juego;

import java.util.Random;

import entorno.Entorno;

/**
 * Administra los peces y sus bombas.
 *
 * Importante para el enunciado:
 * - Los peces se guardan en un arreglo de objetos.
 * - Cuando un pez desaparece, su posicion del arreglo vuelve a null.
 */
public class AdministradorPeces {

    private Pez[] peces;
    private Bomba[] bombas;
    private Random random;
    private int ticksDesdeUltimoPez;
    private boolean jefeCreado;

    public AdministradorPeces() {
        this.peces = new Pez[Configuracion.MAX_PECES];
        this.bombas = new Bomba[Configuracion.MAX_BOMBAS];
        this.random = new Random();
        this.ticksDesdeUltimoPez = 0;
        this.jefeCreado = false;
    }

    public Pez[] getPeces() {
        return peces;
    }

    public Bomba[] getBombas() {
        return bombas;
    }

    public void actualizar(Elizabeth elizabeth, BolaFuego bolaFuego, int nivel, int pecesEliminados) {
        ticksDesdeUltimoPez++;

        if (ticksDesdeUltimoPez >= Configuracion.TICKS_ENTRE_PECES) {
            crearPezAleatorio(nivel);
            ticksDesdeUltimoPez = 0;
        }

        crearJefeSiCorresponde(nivel, pecesEliminados);

        for (int i = 0; i < peces.length; i++) {
            if (peces[i] != null) {
                peces[i].actualizar(elizabeth, bolaFuego);

                if (Configuracion.OPCIONAL_BOMBAS_DE_PECES && peces[i].debeDisparar()) {
                    crearBombaDesdePez(peces[i], elizabeth);
                    peces[i].reiniciarEsperaDeDisparo();
                }

                if (!peces[i].esColosal() && peces[i].estaFueraDePantallaPorIzquierda()) {
                    peces[i] = null;
                }
            }
        }

        mantenerPecesSeparados();
        actualizarBombas();
    }

    public void dibujar(Entorno entorno) {
        for (int i = 0; i < peces.length; i++) {
            if (peces[i] != null) {
                peces[i].dibujar(entorno);
            }
        }

        for (int i = 0; i < bombas.length; i++) {
            if (bombas[i] != null) {
                bombas[i].dibujar(entorno);
            }
        }
    }

    public boolean hayJefeVivo() {
        for (int i = 0; i < peces.length; i++) {
            if (peces[i] != null && peces[i].esColosal()) {
                return true;
            }
        }
        return false;
    }

    public void eliminarPez(int posicion) {
        peces[posicion] = null;
    }

    public void eliminarBomba(int posicion) {
        bombas[posicion] = null;
    }

    private void crearPezAleatorio(int nivel) {
        int posicionLibre = buscarPosicionLibrePez();
        if (posicionLibre == -1) {
            return;
        }

        int tipo = Pez.NORMAL;
        if (Configuracion.OPCIONAL_PECES_VARIADOS) {
            tipo = elegirTipoSegunNivel(nivel);
        }

        for (int intento = 0; intento < 10; intento++) {
            double x = Configuracion.ANCHO_VENTANA + 35;
            double y = 90 + random.nextInt(400);
            Pez candidato = new Pez(x, y, tipo, nivel);

            if (!seSuperponeConOtroPez(candidato)) {
                peces[posicionLibre] = candidato;
                return;
            }
        }
    }

    private int elegirTipoSegunNivel(int nivel) {
        int azar = random.nextInt(100);

        if (nivel >= 3 && azar < 20) {
            return Pez.RESISTENTE;
        }
        if (nivel >= 2 && azar < 45) {
            return Pez.ESQUIVADOR;
        }
        if (nivel >= 1 && azar < 65) {
            return Pez.PERSEGUIDOR;
        }
        if (azar < 80) {
            return Pez.ONDULANTE;
        }
        return Pez.NORMAL;
    }

    private void crearJefeSiCorresponde(int nivel, int pecesEliminados) {
        if (!Configuracion.OPCIONAL_JEFE_COLOSAL) {
            return;
        }
        if (jefeCreado) {
            return;
        }
        if (pecesEliminados < Configuracion.PECES_PARA_GANAR - 6) {
            return;
        }

        int posicionLibre = buscarPosicionLibrePez();
        if (posicionLibre != -1) {
            peces[posicionLibre] = new Pez(Configuracion.ANCHO_VENTANA - 85, 260, Pez.COLOSAL, nivel);
            jefeCreado = true;
        }
    }

    private void crearBombaDesdePez(Pez pez, Elizabeth elizabeth) {
        int posicionLibre = buscarPosicionLibreBomba();
        if (posicionLibre == -1) {
            return;
        }

        bombas[posicionLibre] = new Bomba(pez.getX() - pez.getAncho() / 2, pez.getY(), elizabeth.getX(), elizabeth.getY());
    }

    private void actualizarBombas() {
        for (int i = 0; i < bombas.length; i++) {
            if (bombas[i] != null) {
                bombas[i].actualizar();
                if (bombas[i].estaFueraDePantalla()) {
                    bombas[i] = null;
                }
            }
        }
    }

    private void mantenerPecesSeparados() {
        for (int i = 0; i < peces.length; i++) {
            for (int j = i + 1; j < peces.length; j++) {
                if (peces[i] != null && peces[j] != null) {
                    double distancia = peces[i].distanciaA(peces[j]);
                    if (distancia < 48) {
                        peces[i].separarVerticalmente(-1.2);
                        peces[j].separarVerticalmente(1.2);
                    }
                }
            }
        }
    }

    private boolean seSuperponeConOtroPez(Pez candidato) {
        for (int i = 0; i < peces.length; i++) {
            if (peces[i] != null && peces[i].distanciaA(candidato) < 62) {
                return true;
            }
        }
        return false;
    }

    private int buscarPosicionLibrePez() {
        for (int i = 0; i < peces.length; i++) {
            if (peces[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int buscarPosicionLibreBomba() {
        for (int i = 0; i < bombas.length; i++) {
            if (bombas[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
