package juego;

import java.awt.Color;
import entorno.Entorno;


public class Piso {

	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double velocidad;
	
	
	public Piso(double x, double y, double ancho, double alto) {
	this.x = x;
	this.y = y;
	this.ancho = ancho;
	this.alto = alto;
	this.velocidad = 7;
}

public void dibujar(Entorno entorno) {
	entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GREEN);
	
}

public void mover() {
	this.x = this.x - this.velocidad;
}

//Métodos para compartir las medidas (Getters)
public double getX() { return this.x; }
public double getY() { return this.y; }
public double getAncho() { return this.ancho; }
public double getAlto() { return this.alto; }
public double getVelocidad() { return this.velocidad; }
	
}
