package juego;


import entorno.Entorno;
import entorno.InterfaceJuego;



public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;



	// Variables y métodos propios de cada grupo
	private Personaje elizabeth;
	private piso[] pisos;
	private BolaDeFuego bolaDeFuego;

	// este es el constructor
 Juego()
	{
		// Inicializa el objeto entorno

		// esto que hace? no está duplicado?
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		this.entorno = new Entorno(this, "Super Elizabet Sis", 800, 600);
		// Inicializar lo que haga falta para el juego

		this.elizabeth = new Personaje(50, 400);
		// Acá hago que bolaDeFuego inicialice en null, la idea es que cuando arranca el juego no hay bolas de fuego creadas
		this.bolaDeFuego = null;

		this.pisos = new piso[5];

		this.pisos[0] = new piso(500, 550, 1000, 100);
		this.pisos[1] = new piso(1650, 550, 1000, 100);
		this.pisos[2] = new piso(2750, 550, 800, 100);
		this.pisos[3] = new piso(3800, 550, 1000, 100);
		this.pisos[4] = new piso(4900, 550, 1000, 100);

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y
	 * por lo tanto es el método más importante de esta clase. Aquí se debe
	 * actualizar el estado interno del juego para simular el paso del tiempo
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
	    // --- 1. MOVERSE LATERALMENTE ---
	    if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
	        this.elizabeth.moverDerecha();
	    }
	    if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
	        this.elizabeth.moverIzquierda();
	    }

		// con esto trabajo el disparo de la bola de fuego
		// Si se presiona el botón izquierdo del mouse y no hay una bola activa,
		// se crea una nueva bola desde la posición de Elizabeth hacia el mouse.
		if (this.entorno.sePresionoBoton(this.entorno.BOTON_IZQUIERDO) && this.bolaDeFuego == null) {
			this.bolaDeFuego = new BolaDeFuego(
					this.elizabeth.getX(),
					this.elizabeth.getY(),
					this.entorno.mouseX(),
					this.entorno.mouseY()
			);
		}

			// Si la bola existe, la muevo con cada tick
		if (this.bolaDeFuego != null) {
			this.bolaDeFuego.mover();

			// Si la bola sale de la pantalla, la elimino
			if (this.bolaDeFuego.salioDePantalla(this.entorno)) {
				this.bolaDeFuego = null;
			}
		}

	    // Movemos los pisos
	    for (int i = 0; i < this.pisos.length; i++) {
	        if (this.pisos[i] != null) {
	            this.pisos[i].mover();
	        }
	    }

	    // --- 2. REVISAR SI TOCA EL PISO ---
	    boolean pisandoSuelo = false;
	    for (int i = 0; i < this.pisos.length; i++) {
	        if (this.pisos[i] != null && this.elizabeth.tocaPiso(this.pisos[i])) {
	            pisandoSuelo = true;
	        }
	    }

	    // --- 3. LÓGICA DE SALTO ---
	    // Si presiona Arriba y ADEMÁS está pisando el suelo, salta
	    if (this.entorno.sePresiono(this.entorno.TECLA_ARRIBA) && pisandoSuelo) {
	        this.elizabeth.saltar();
	    }

	    // --- 4. APLICAR GRAVEDAD O SUBIDA ---
	    // Si NO pisa el suelo, la gravedad tira para abajo
	    if (!pisandoSuelo) {
	        this.elizabeth.caer();
	    }

	    // Siempre procesamos el salto por si está a mitad de vuelo
	    this.elizabeth.procesarSalto();

	    // --- 5. DIBUJAR TODO ---
	    for (int i = 0; i < this.pisos.length; i++) {
	        if (this.pisos[i] != null) {
	            this.pisos[i].dibujar(this.entorno);
	        }
	    }
	    this.elizabeth.dibujar(this.entorno);

		if (this.bolaDeFuego != null) {
			this.bolaDeFuego.dibujar(this.entorno);
		}
	}

	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
