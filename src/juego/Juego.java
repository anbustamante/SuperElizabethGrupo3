package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;




public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;



	// Variables y métodos propios de cada grupo
	private Personaje elizabeth;
	private Piso[] pisos;
	private Bandera Bandera;
    private BolaDeFuego bolaDeFuego;

    // acá las features de poder pausar y perder el juego si la tipa se cae
    private boolean pausado;
    private boolean perdio;


	public Juego()
	{
		// Inicializa el objeto entorno

		// esto que hace? no está duplicado?
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		this.entorno = new Entorno(this, "Super Elizabet Sis", 800, 600);
		// Inicializar lo que haga falta para el juego
		//esto es para reiniciar el juego
		this.reiniciarJuego();

		this.elizabeth = new Personaje(50, 400);
        // Acá hago que bolaDeFuego inicialice en null, la idea es que cuando arranca el juego no hay bolas de fuego creadas
        this.bolaDeFuego = null;
        // esto es por el pausado y que finalice el juego si me caigo
        this.pausado = false;
        this.perdio = false;
        this.Bandera = new Bandera(5390.0, 425.0);

		this.pisos = new Piso[5];

		this.pisos[0] = new Piso(500, 550, 1000, 100);
		this.pisos[1] = new Piso(1650, 550, 1000, 100);
		this.pisos[2] = new Piso(2750, 550, 800, 100);
		this.pisos[3] = new Piso(3800, 550, 1000, 100);
		this.pisos[4] = new Piso(4900, 550, 1000, 100);

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
	    // MOVERSE LATERALMENTE ---
		// apretando la P ponemos pausa
		if (this.entorno.sePresiono('P') || this.entorno.sePresiono('p')) {
			this.pausado = !this.pausado;
		}

		// si pauso el juego, muestro un textito de pauisa
		// además corto el tick para que el juego no siga
		if (this.pausado) {
			this.dibujarTodo();
			this.entorno.cambiarFont("Arial", 30, java.awt.Color.WHITE);
			this.entorno.escribirTexto("PAUSA", 350, 280);
			return;
		}

		// si la princesa se cae pierde, dibujo la escena y muestro el texto de derrota
		// después corto el tick para que el juego no siga
		if (this.perdio) {
			this.dibujarTodo();

			this.entorno.cambiarFont("Arial", 30, java.awt.Color.RED);
			this.entorno.escribirTexto("PERDISTE", 330, 260);

			this.entorno.cambiarFont("Arial", 18, java.awt.Color.WHITE);
			this.entorno.escribirTexto("Presiona R para reiniciar", 290, 310);

			if (this.entorno.sePresiono('R') || this.entorno.sePresiono('r')) {
				this.reiniciarJuego();
			}

			return;
		}
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








	    // MOVER LOS PISOS ---
	    if (this.entorno.estaPresionada(entorno.TECLA_DERECHA) && this.Bandera.getX() > 300.0) {
	    	this.elizabeth.moverDerecha();

	    	for (int i = 0; i < this.pisos.length; i++) {
	    		this.pisos[i].mover();

	    		}
	    		this.Bandera.mover(this.pisos[0].getVelocidad());

	        }
	    if(this.entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
	    	this.elizabeth.moverIzquierda();
	    }

	    // REVISAR SI TOCA EL PISO ---
	    boolean pisandoSuelo = false;
	    for (int i = 0; i < this.pisos.length; i++) {
	        if (this.pisos[i] != null && this.elizabeth.tocaPiso(this.pisos[i])) {
	            pisandoSuelo = true;
	        }
	    }



	    // LÓGICA DE SALTO ---
	    // Si presiona Arriba y ADEMÁS está pisando el suelo, salta
	    if (this.entorno.sePresiono(this.entorno.TECLA_ARRIBA) && pisandoSuelo) {
	        this.elizabeth.saltar();
	    }

	    //  APLICAR GRAVEDAD O SUBIDA ---
	    // Si NO pisa el suelo, la gravedad tira para abajo
	    if (!pisandoSuelo) {
	        this.elizabeth.caer();
	    }

	    // Siempre procesamos el salto por si está a mitad de vuelo
	    this.elizabeth.procesarSalto();

        // si la princesa se cae, pierdo
        if (this.elizabeth.getY() > this.entorno.alto() + 50) {
            this.perdio = true;
        }
	    // --- 5. DIBUJAR TODO ---
	    for (int i = 0; i < this.pisos.length; i++) {
	        if (this.pisos[i] != null) {
	            this.pisos[i].dibujar(this.entorno);
	        }
	    }


	    if (this.elizabeth.tocarBandera(this.Bandera)) {

	    	this.entorno.cambiarFont("Arial", 40, Color.GREEN);
	    	this.entorno.escribirTexto("¡GANASTE EL JUEGO!", 250, 300);
	    }

	    this.elizabeth.dibujar(this.entorno);
	    this.Bandera.dibujar(this.entorno);

        if (this.bolaDeFuego != null) {
            this.bolaDeFuego.dibujar(this.entorno);
        }

	}
	private void reiniciarJuego() {
		this.elizabeth = new Personaje(50, 400);

		this.bolaDeFuego = null;

		this.pisos = new piso[5];

		this.pisos[0] = new piso(500, 550, 1000, 100);
		this.pisos[1] = new piso(1650, 550, 1000, 100);
		this.pisos[2] = new piso(2750, 550, 800, 100);
		this.pisos[3] = new piso(3800, 550, 1000, 100);
		this.pisos[4] = new piso(4900, 550, 1000, 100);

		this.pausado = false;
		this.perdio = false;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
