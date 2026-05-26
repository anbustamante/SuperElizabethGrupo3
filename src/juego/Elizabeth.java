package juego;

import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;


public class Elizabeth {

    private double x;
    private double y;
    private double ancho;
    private double alto;
    private double velocidad;
    private boolean saltando;
    private int impulso;
    private Image img;



	public Elizabeth(double x, double y) {
		this.x = x;
		this.y = y;
		this.ancho = 30;
		this.alto = 50;
		this.velocidad = 3;
		this.saltando = false;
		this.impulso = 0;
		
		this.img = Herramientas.cargarImagen("princesa.png");
	}
	
	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(this.img, this.x, this.y, 0, 3);
	}
	

	
	// 3. Método para avanzar (suma a la x)
    public void moverDerecha() {
    	if (this.x +(this.ancho /2) < 800) { // limite para parte derecha
    	this.x = this.x + this.velocidad;
    	}
    }

    // 4. Método para retroceder (resta a la x)
    public void moverIzquierda() {
    	if (this.x - (this.ancho /2) > 0) { //limite parte izquierda
        this.x = this.x - this.velocidad;
    	}
    }
    
    // Método para que la gravedad haga efecto (suma a la Y para ir hacia abajo)
    public void caer() {
        this.y = this.y + 4.5; // Podés ajustar este número para que caiga más rápido o más lento
    }

    // Método para detectar si choca contra un piso
    public boolean tocaPiso(Plataforma plataforma) {
        // Calculamos dónde están los pies de Elizabeth y sus costados
        double miAbajo = this.y + (this.alto / 2);
        double miIzquierda = this.x - (this.ancho / 2);
        double miDerecha = this.x + (this.ancho / 2);

        // Calculamos dónde está el techo del piso y sus bordes
        double pisoArriba = plataforma.getY() - (plataforma.getAlto() / 2);
        double pisoIzquierda = plataforma.getX() - (plataforma.getAncho() / 2);
        double pisoDerecha = plataforma.getX() + (plataforma.getAncho() / 2);

        // Si los pies tocan el techo Y el personaje está dentro del ancho del bloque:
        if (miAbajo >= pisoArriba && miAbajo <= pisoArriba + 10 && miDerecha > pisoIzquierda && miIzquierda < pisoDerecha) {
            // La acomodamos justo arriba para que no se hunda
            this.y = pisoArriba - (this.alto / 2); 
            return true; // Sí, está pisando
        }
        return false; // No, está en el aire
    }

    // Activa el salto
    public void saltar() {
        this.saltando = true;
        this.impulso = 20;
    }

    // Procesa la subida ganándole a la gravedad
    public void procesarSalto() {
        if (this.saltando && this.impulso > 0) {
            this.y = this.y - 11; // Resta a la Y para ir hacia arriba
            this.impulso--; // Gasta un poquito de impulso
        } else {
            this.saltando = false; // Se quedó sin nafta, empieza a caer
        }
    }
    // Al tocar la bandera se termina el juego
    public boolean tocarBandera(Bandera b) {
    	boolean colisionX = Math.abs(this.x - b.getX()) < (this.ancho / 2 + b.getAncho() /2 );
    	boolean colisionY = Math.abs(this.y - b.getY()) < (this.alto / 2 + b.getAlto() / 2);
    	return colisionX && colisionY;
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
}