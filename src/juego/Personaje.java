package juego;

import java.awt.Color;
import entorno.Entorno;

public class Personaje {
	
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double velocidad;

	public Personaje(double x, double y) {
		this.x = x;
		this.y = y;
		this.ancho = 30;
		this.alto = 50;
		this.velocidad = 3.5;
	}
	
	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.PINK);
	}
	
	// 3. Método para avanzar (suma a la x)
    public void moverDerecha() {
        this.x = this.x + this.velocidad;
    }

    // 4. Método para retroceder (resta a la x)
    public void moverIzquierda() {
        this.x = this.x - this.velocidad;
    }


// Esto lo agrego para obtener la posición del personaje

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

}

