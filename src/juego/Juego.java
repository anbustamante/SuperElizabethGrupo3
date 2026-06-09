package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;




public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;



	// Variables y métodos propios de cada grupo
	private Elizabeth elizabeth;
	private Pez[] pez;
	private Plataforma[] plataformas;
	private Bandera bandera;
    private BolaDeFuego bolaDeFuego;

    // acá las features de poder pausar y perder el juego si la tipa se cae
    private boolean pausado;
    private boolean perdio;
	private boolean gano;
	int killCount;


	public Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Super Elizabeth", 800, 600);
		// Inicializar lo que haga falta para el juego
		this.reiniciarJuego();
		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y
	 * por lo tanto es el método más importante de esta clase. Aquí se debe
	 * actualizar el estado interno del juego para simular el paso del tiempo
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick() {
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
			this.entorno.escribirTexto("Cantidad de peces eliminados: " + killCount + "/"+ pez.length, 290, 310);

			this.entorno.cambiarFont("Arial", 18, java.awt.Color.WHITE);
			this.entorno.escribirTexto("Presiona R para reiniciar", 290, 350);

			if (this.entorno.sePresiono('R') || this.entorno.sePresiono('r')) {
				this.reiniciarJuego();
			}
			return;
		}

		if (this.gano) {
			this.dibujarTodo();

			this.entorno.cambiarFont("Arial", 30, Color.GREEN);
			this.entorno.escribirTexto("GANASTE", 330, 260);

			this.entorno.cambiarFont("Arial", 18, java.awt.Color.WHITE);
			this.entorno.escribirTexto("Presiona R para reiniciar", 290, 310);

			if (this.entorno.sePresiono('R') || this.entorno.sePresiono('r')) {
				this.reiniciarJuego();
			}
			return;
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
			// Revisar colisión con peces
			for (int i = 0; i < this.pez.length; i++) {

				if (this.pez[i] != null && this.bolaDeFuego.tocaPez(this.pez[i])) {
					this.pez[i] = null;
					killCount++;
					this.bolaDeFuego = null;
					break;
				}
			}

            // Si la bola sale de la pantalla, la elimino
            if (this.bolaDeFuego != null && this.bolaDeFuego.salioDePantalla(this.entorno)) {
                this.bolaDeFuego = null;
            }
        }

	    // MOVER LOS PISOS ---
        
        //MUEVE EL SUELO Y ISLAS HACIA LA IZQUIERDA
        for (int i = 0; i < this.plataformas.length; i++) {
			this.plataformas[i].moverDerecha();
		}
        
        // MUEVE LOS PECES HACIA LA IZQUIERDA
        for(int i = 0; i < this.pez.length; i++){
			if(pez[i] != null){
				this.pez[i].moverDerecha();
			}
		}
        //MUVE LA BANDERA A LA MISMA VELOCIDAD QUE LAS PLATAFORMAS
		this.bandera.moverDerecha(this.plataformas[0].getVelocidad());
        
		if (this.entorno.estaPresionada(entorno.TECLA_DERECHA)) {
				this.elizabeth.moverDerecha();
					
		}
		if (this.entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {

				this.elizabeth.moverIzquierda();			
		}

	    // REVISAR SI TOCA EL PISO ---
	    boolean pisandoSuelo = false;
	    for (int i = 0; i < this.plataformas.length; i++) {
	        if (this.plataformas[i] != null && this.elizabeth.tocaPiso(this.plataformas[i])) {
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
        if (this.elizabeth.getY() == this.pez.getY()) {
        	this.perdio = true;
        }
        


		if (this.elizabeth.tocarBandera(this.bandera) && killCount == pez.length){
			gano = true;
		}else if(this.elizabeth.tocarBandera(this.bandera)){
			perdio = true;
		}
		
		for(int i = 0; i < this.pez.length; i++){
			if (this.pez[i] != null) {
			}
		}
		this.entorno.cambiarFont("Arial", 30, java.awt.Color.WHITE);
		this.entorno.escribirTexto( killCount + "/"+ pez.length, 0, 50);
		dibujarTodo();

	}

	private void dibujarTodo() {
		for (int i = 0; i < this.plataformas.length; i++) {
			if (this.plataformas[i] != null) {
				this.plataformas[i].dibujar(this.entorno);
			}
		}

		this.elizabeth.dibujar(this.entorno);
		this.bandera.dibujar(this.entorno);
		for(int i = 0; i < this.pez.length; i++){
			if(pez[i] != null){
				this.pez[i].dibujar(this.entorno);
			}
		}
		if (this.bolaDeFuego != null) {
			this.bolaDeFuego.dibujar(this.entorno);
		}
	}

	private void reiniciarJuego() {
		this.elizabeth = new Elizabeth(50, 400);
		this.bandera = new Bandera(4290.0, 425.0);
		this.bolaDeFuego = null;
		this.pez = new Pez[12];
		this.plataformas = new Plataforma[9];
		killCount = 0;
		

		this.plataformas[0] = new Plataforma(500, 550, 1000, 100, Color.GREEN);
		this.plataformas[1] = new Plataforma(1650, 550, 1000, 100, Color.GREEN);
		this.plataformas[2] = new Plataforma(2750, 550, 800, 100, Color.GREEN);
		this.plataformas[3] = new Plataforma(3800, 550, 1000, 100, Color.GREEN);
		this.plataformas[4] = new Plataforma(4900, 550, 1000, 100, Color.GREEN);
		
		this.plataformas[5] = new Plataforma(900, 400, 300, 50, Color.BLUE);
		this.plataformas[6] = new Plataforma(100, 300, 300, 50, Color.BLUE);
		this.plataformas[7] = new Plataforma(1400, 400, 300, 50, Color.BLUE);
		this.plataformas[8] = new Plataforma(1800, 400, 300, 50, Color.BLUE);

		this.pez[0] = new Pez(500, 400);
		this.pez[1] = new Pez(1600, 200);
		this.pez[2] = new Pez(2750, 100);
		this.pez[3] = new Pez(3800, 400);
		this.pez[4] = new Pez(1700, 400);
		this.pez[5] = new Pez(2850, 200);
		this.pez[6] = new Pez(3600, 100);
		this.pez[7] = new Pez(2000, 400);
		this.pez[8] = new Pez(3000, 400);
		this.pez[9] = new Pez(3900, 200);
		this.pez[10] = new Pez(4800, 100);
		this.pez[11] = new Pez(6000, 400);
		this.pausado = false;
		this.perdio = false;
		this.gano = false;
	}
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
