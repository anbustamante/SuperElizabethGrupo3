package juego;

import java.awt.Color;
import entorno.Entorno;


public class Plataforma {

	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double velocidad;
	private Color color;
	
	
	public Plataforma(double x, double y, double ancho, double alto, Color color) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = 8;
		this.color = color;
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, color);

	}

	public void moverDerecha() {
		this.x = this.x - this.velocidad;
	}

	public void moverIzquierda() {
		this.x = this.x + this.velocidad;
	}

	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double getAncho() { return this.ancho; }
	public double getAlto() { return this.alto; }
	public double getVelocidad() { return this.velocidad; }
}
