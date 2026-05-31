package juego;

import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

/**
 * Clase principal del juego.
 *
 * Esta clase hereda de InterfaceJuego porque el entorno lo exige.
 * Su metodo mas importante es tick(), que se ejecuta muchas veces por segundo.
 *
 * La idea de diseno es que Juego coordine a las demas clases, pero que no haga
 * todo el trabajo directamente. Por eso existen clases como Elizabeth,
 * AdministradorEscenario, AdministradorPeces y AdministradorItems.
 */
public class Juego extends InterfaceJuego {

    private Entorno entorno;

    private Elizabeth elizabeth;
    private AdministradorEscenario escenario;
    private AdministradorPeces administradorPeces;
    private AdministradorItems administradorItems;
    private Hud hud;

    private BolaFuego bolaFuego;
    private EstadoJuego estado;

    private int pecesEliminados;
    private int nivel;

    private boolean fuegoPotenciado;
    private long momentoUltimoHechizo;
    private long momentoFinEscudo;

    public Juego() {
        this.entorno = new Entorno(this, "Super Elizabeth Sis", Configuracion.ANCHO_VENTANA,
                Configuracion.ALTO_VENTANA);

        inicializarJuego();
        this.entorno.iniciar();
    }

    /**
     * Reinicia todos los objetos importantes.
     * Tambien se usa cuando el jugador presiona R despues de ganar o perder.
     */
    private void inicializarJuego() {
        this.elizabeth = new Elizabeth(55, 500);
        this.escenario = new AdministradorEscenario();
        this.administradorPeces = new AdministradorPeces();
        this.administradorItems = new AdministradorItems();
        this.hud = new Hud();

        this.bolaFuego = null;
        this.estado = EstadoJuego.JUGANDO;

        this.pecesEliminados = 0;
        this.nivel = 1;

        this.fuegoPotenciado = false;
        this.momentoUltimoHechizo = -Configuracion.COOLDOWN_HECHIZO_MS;
        this.momentoFinEscudo = 0;
    }

    /**
     * tick() es el corazon del juego.
     * El entorno lo llama automaticamente en cada instante.
     */
    public void tick() {
        dibujarFondo();

        if (entorno.sePresiono('r') || entorno.sePresiono('R')) {
            inicializarJuego();
        }

        if (estado == EstadoJuego.JUGANDO) {
            actualizarJuego();
            dibujarJuego();
            verificarVictoriaODerrota();
        } else {
            dibujarJuego();
            hud.dibujarPantallaFinal(entorno, estado, pecesEliminados);
        }
    }

    private void actualizarJuego() {
        escenario.actualizar();

        // Primero se actualiza usando el estado de apoyo del tick anterior.
        // Esto permite que el salto se detecte correctamente.
        elizabeth.actualizarMovimiento(entorno);

        // Luego se recalcula si sigue apoyada o si empezo a caer.
        elizabeth.setApoyada(false);
        resolverColisionElizabethConPlataformas();

        manejarDisparoDeElizabeth();
        actualizarBolaDeFuego();

        administradorPeces.actualizar(elizabeth, bolaFuego, nivel, pecesEliminados);

        if (Configuracion.OPCIONAL_ITEMS) {
            administradorItems.actualizar();
            resolverColisionElizabethConItems();
        }

        if (Configuracion.OPCIONAL_HECHIZO) {
            manejarHechizo();
        }

        resolverColisionesConPecesYBombas();
        actualizarNivel();
    }

    private void dibujarJuego() {
        escenario.dibujar(entorno);

        if (Configuracion.OPCIONAL_ITEMS) {
            administradorItems.dibujar(entorno);
        }

        administradorPeces.dibujar(entorno);

        if (bolaFuego != null) {
            bolaFuego.dibujar(entorno);
        }

        elizabeth.dibujar(entorno, escudoActivo());

        if (estado == EstadoJuego.JUGANDO) {
            hud.dibujarDuranteJuego(entorno, pecesEliminados, nivel, fuegoPotenciado, escudoActivo(),
                    msRestantesHechizo());
        }
    }

    private void dibujarFondo() {
        entorno.colorFondo(new Color(126, 135, 255));

        // Nubes decorativas.
        entorno.dibujarCirculo(145, 90, 38, Color.WHITE);
        entorno.dibujarCirculo(175, 85, 46, Color.WHITE);
        entorno.dibujarCirculo(205, 94, 35, Color.WHITE);

        entorno.dibujarCirculo(590, 115, 32, Color.WHITE);
        entorno.dibujarCirculo(620, 105, 42, Color.WHITE);
        entorno.dibujarCirculo(655, 115, 35, Color.WHITE);
    }

    private void resolverColisionElizabethConPlataformas() {
        Plataforma[] plataformas = escenario.getPlataformas();

        for (int i = 0; i < plataformas.length; i++) {
            Plataforma plataforma = plataformas[i];

            if (plataforma != null
                    && (estaCayendoSobrePlataforma(plataforma) || estaParadaSobrePlataforma(plataforma))) {
                elizabeth.apoyarSobre(plataforma.arriba());
                return;
            }
        }
    }

    /**
     * La clave para no atravesar plataformas es mirar la posicion anterior.
     * Elizabeth aterriza solo si antes estaba arriba de la plataforma y ahora la cruzo.
     */
    private boolean estaCayendoSobrePlataforma(Plataforma plataforma) {
        boolean sePisanEnX = elizabeth.derecha() > plataforma.izquierda()
                && elizabeth.izquierda() < plataforma.derecha();

        double piesAntes = elizabeth.getYAnterior() + elizabeth.getAlto() / 2;
        double piesAhora = elizabeth.abajo();
        boolean cruzoDesdeArriba = piesAntes <= plataforma.arriba() && piesAhora >= plataforma.arriba();

        return sePisanEnX && cruzoDesdeArriba && elizabeth.getVelocidadY() >= 0;
    }

    /**
     * Caso complementario: cuando Elizabeth ya estaba parada y el piso se movio
     * con el escenario, no necesariamente "cruza" la plataforma. Por eso se
     * acepta una pequena tolerancia vertical.
     */
    private boolean estaParadaSobrePlataforma(Plataforma plataforma) {
        boolean sePisanEnX = elizabeth.derecha() > plataforma.izquierda()
                && elizabeth.izquierda() < plataforma.derecha();

        double piesAhora = elizabeth.abajo();
        boolean piesCercaDeLaSuperficie = Math.abs(piesAhora - plataforma.arriba()) <= 6;

        return sePisanEnX && piesCercaDeLaSuperficie && elizabeth.getVelocidadY() >= 0;
    }

    private void manejarDisparoDeElizabeth() {
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && bolaFuego == null) {
            bolaFuego = new BolaFuego(elizabeth.getX(), elizabeth.getY() - 8, entorno.mouseX(), entorno.mouseY(),
                    fuegoPotenciado);
            fuegoPotenciado = false;
        }
    }

    private void actualizarBolaDeFuego() {
        if (bolaFuego == null) {
            return;
        }

        bolaFuego.actualizar();

        if (bolaFuego.estaFueraDePantalla()) {
            bolaFuego = null;
            return;
        }

        // Si pega contra piso o isla, desaparece.
        Plataforma[] plataformas = escenario.getPlataformas();
        for (int i = 0; i < plataformas.length; i++) {
            if (plataformas[i] != null && bolaFuego.colisionaCon(plataformas[i])) {
                bolaFuego = null;
                return;
            }
        }
    }

    private void resolverColisionesConPecesYBombas() {
        Pez[] peces = administradorPeces.getPeces();
        Bomba[] bombas = administradorPeces.getBombas();

        for (int i = 0; i < peces.length; i++) {
            Pez pez = peces[i];

            if (pez != null) {
                if (bolaFuego != null && bolaFuego.colisionaCon(pez)) {
                    pez.recibirDano(bolaFuego.getDano());
                    bolaFuego = null;

                    if (pez.estaMuerto()) {
                        if (pez.esColosal()) {
                            pecesEliminados += 5;
                        } else {
                            pecesEliminados++;
                        }
                        administradorPeces.eliminarPez(i);
                    }
                    continue;
                }

                if (elizabeth.colisionaCon(pez)) {
                    if (escudoActivo()) {
                        administradorPeces.eliminarPez(i);
                    } else {
                        estado = EstadoJuego.PERDIDO;
                    }
                }
            }
        }

        for (int i = 0; i < bombas.length; i++) {
            Bomba bomba = bombas[i];

            if (bomba != null) {
                if (bolaFuego != null && bolaFuego.colisionaCon(bomba)) {
                    administradorPeces.eliminarBomba(i);
                    bolaFuego = null;
                    continue;
                }

                if (elizabeth.colisionaCon(bomba)) {
                    administradorPeces.eliminarBomba(i);
                    if (!escudoActivo()) {
                        estado = EstadoJuego.PERDIDO;
                    }
                }
            }
        }
    }

    private void resolverColisionElizabethConItems() {
        ItemEspecial[] items = administradorItems.getItems();

        for (int i = 0; i < items.length; i++) {
            ItemEspecial item = items[i];

            if (item != null && elizabeth.colisionaCon(item)) {
                aplicarEfectoItem(item);
                administradorItems.eliminarItem(i);
            }
        }
    }

    private void aplicarEfectoItem(ItemEspecial item) {
        if (item.getTipo() == ItemEspecial.PUNTOS) {
            pecesEliminados++;
        } else if (item.getTipo() == ItemEspecial.TRAMPA) {
            if (pecesEliminados > 0) {
                pecesEliminados--;
            }
        } else if (item.getTipo() == ItemEspecial.FUEGO_POTENCIADO) {
            fuegoPotenciado = true;
        } else if (item.getTipo() == ItemEspecial.ESCUDO) {
            momentoFinEscudo = entorno.tiempo() + Configuracion.DURACION_ESCUDO_MS;
        }
    }

    private void manejarHechizo() {
        if (!entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {
            return;
        }

        if (msRestantesHechizo() > 0) {
            return;
        }

        Pez pezClickeado = buscarPezClickeado(entorno.mouseX(), entorno.mouseY());
        if (pezClickeado == null) {
            return;
        }

        Pez[] peces = administradorPeces.getPeces();
        Entidad centroHechizo = new Entidad(pezClickeado.getX(), pezClickeado.getY(), 1, 1);

        for (int i = 0; i < peces.length; i++) {
            if (peces[i] != null && peces[i].distanciaA(centroHechizo) <= Configuracion.RADIO_HECHIZO) {
                peces[i].recibirDano(Configuracion.DANO_HECHIZO);
                if (peces[i].estaMuerto()) {
                    if (peces[i].esColosal()) {
                        pecesEliminados += 5;
                    } else {
                        pecesEliminados++;
                    }
                    administradorPeces.eliminarPez(i);
                }
            }
        }

        momentoUltimoHechizo = entorno.tiempo();
    }

    private Pez buscarPezClickeado(int mouseX, int mouseY) {
        Pez[] peces = administradorPeces.getPeces();
        Entidad puntoMouse = new Entidad(mouseX, mouseY, 2, 2);

        for (int i = 0; i < peces.length; i++) {
            if (peces[i] != null && peces[i].colisionaCon(puntoMouse)) {
                return peces[i];
            }
        }
        return null;
    }

    private void actualizarNivel() {
        if (!Configuracion.OPCIONAL_NIVELES) {
            nivel = 1;
            return;
        }

        if (pecesEliminados >= 16) {
            nivel = 3;
        } else if (pecesEliminados >= 8) {
            nivel = 2;
        } else {
            nivel = 1;
        }
    }

    private void verificarVictoriaODerrota() {
        if (pecesEliminados >= Configuracion.PECES_PARA_GANAR) {
            estado = EstadoJuego.GANADO;
        }

        // Perder por caer al precipicio.
        if (elizabeth.arriba() > Configuracion.ALTO_VENTANA) {
            estado = EstadoJuego.PERDIDO;
        }
    }

    private boolean escudoActivo() {
        return entorno.tiempo() < momentoFinEscudo;
    }

    private long msRestantesHechizo() {
        long transcurrido = entorno.tiempo() - momentoUltimoHechizo;
        long restante = Configuracion.COOLDOWN_HECHIZO_MS - transcurrido;
        return Math.max(0, restante);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}
