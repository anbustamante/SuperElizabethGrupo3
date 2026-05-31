package juego;

import java.util.Random;

import entorno.Entorno;

/**
 * Administra los items especiales.
 *
 * Esta clase es opcional. Si se desactiva en Configuracion, Juego no la usa.
 */
public class AdministradorItems {

    private ItemEspecial[] items;
    private Random random;
    private int ticksDesdeUltimoItem;

    public AdministradorItems() {
        this.items = new ItemEspecial[Configuracion.MAX_ITEMS];
        this.random = new Random();
        this.ticksDesdeUltimoItem = 0;
    }

    public ItemEspecial[] getItems() {
        return items;
    }

    public void actualizar() {
        ticksDesdeUltimoItem++;

        if (ticksDesdeUltimoItem >= Configuracion.TICKS_ENTRE_ITEMS) {
            crearItemAleatorio();
            ticksDesdeUltimoItem = 0;
        }

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                items[i].actualizar();
                if (items[i].estaFueraDePantallaPorIzquierda()) {
                    items[i] = null;
                }
            }
        }
    }

    public void dibujar(Entorno entorno) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                items[i].dibujar(entorno);
            }
        }
    }

    public void eliminarItem(int posicion) {
        items[posicion] = null;
    }

    private void crearItemAleatorio() {
        int posicionLibre = buscarPosicionLibre();
        if (posicionLibre == -1) {
            return;
        }

        int tipo = random.nextInt(4);
        double x = Configuracion.ANCHO_VENTANA + 30;
        double y = 170 + random.nextInt(310);
        items[posicionLibre] = new ItemEspecial(x, y, tipo);
    }

    private int buscarPosicionLibre() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
