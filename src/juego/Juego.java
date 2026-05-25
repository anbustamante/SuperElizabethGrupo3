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
	
	
	public Juego()
	{
		// Inicializa el objeto entorno
		
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		this.entorno = new Entorno(this, "Super Elizabet Sis", 800, 600);
		// Inicializar lo que haga falta para el juego
		
		this.elizabeth = new Personaje(50, 400);
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
	    if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)) {
	        this.elizabeth.moverDerecha();
	    }
	    if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)) {
	        this.elizabeth.moverIzquierda();
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
	
	
	}
	
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
