package juego;

/**
 * Clase de configuracion general del juego.
 *
 * Idea importante:
 * Si el docente o el grupo quiere activar/desactivar un opcional,
 * deberia poder hacerlo desde esta clase sin tocar demasiadas partes
 * del proyecto.
 */
public class Configuracion {

    // Medidas de la ventana del entorno.
    public static final int ANCHO_VENTANA = 800;
    public static final int ALTO_VENTANA = 600;

    // Objetivo recomendado por el enunciado: entre 20 y 30 peces.
    public static final int PECES_PARA_GANAR = 25;

    // Velocidad con la que el escenario se mueve de derecha a izquierda.
    public static final double VELOCIDAD_ESCENARIO = 1.6;

    // Fisica simple de la princesa.
    public static final double GRAVEDAD = 0.45;
    public static final double VELOCIDAD_SALTO = -10.5;
    public static final double VELOCIDAD_CAMINAR = 4.2;
    public static final double VELOCIDAD_MAXIMA_CAIDA = 12.0;

    // Capacidad maxima de arreglos.
    public static final int MAX_PLATAFORMAS = 24;
    public static final int MAX_PECES = 18;
    public static final int MAX_BOMBAS = 12;
    public static final int MAX_ITEMS = 6;

    // Intervalos aproximados de aparicion.
    public static final int TICKS_ENTRE_PECES = 70;
    public static final int TICKS_ENTRE_ITEMS = 520;

    // Opcionales: se dejan separados para que sean faciles de quitar.
    public static final boolean OPCIONAL_PECES_VARIADOS = true;
    public static final boolean OPCIONAL_BOMBAS_DE_PECES = true;
    public static final boolean OPCIONAL_HECHIZO = true;
    public static final boolean OPCIONAL_ITEMS = true;
    public static final boolean OPCIONAL_NIVELES = true;
    public static final boolean OPCIONAL_JEFE_COLOSAL = true;

    // Hechizo magico de Elizabeth.
    public static final int COOLDOWN_HECHIZO_MS = 5000;
    public static final double RADIO_HECHIZO = 125;
    public static final int DANO_HECHIZO = 2;

    // Item de escudo.
    public static final int DURACION_ESCUDO_MS = 6000;

    // Constructor privado: esta clase no necesita instanciarse.
    private Configuracion() {
    }
}
