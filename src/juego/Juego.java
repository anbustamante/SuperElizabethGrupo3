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
	
	
	Juego()
	{
		// Inicializa el objeto entorno
		
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		this.entorno = new Entorno(this, "Super Elizabet Sis", 800, 600);
		// Inicializar lo que haga falta para el juego
		
		this.elizabeth = new Personaje(50, 400);

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
		// Procesamiento de un instante de tiempo
		this.elizabeth.dibujar(this.entorno);
		
		// --- 1. LÓGICA DE MOVIMIENTO ---
        // Si aprieta la derecha, le decimos al personaje que se mueva a la derecha
        if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
            this.elizabeth.moverDerecha();
        }
        
        // Si aprieta la izquierda, le decimos al personaje que se mueva a la izquierda
        if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
            this.elizabeth.moverIzquierda();
        }

        // --- 2. DIBUJAR TODO ---
        // Al final de todo el procesamiento del tick, dibujamos
        this.elizabeth.dibujar(this.entorno);
	}
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
