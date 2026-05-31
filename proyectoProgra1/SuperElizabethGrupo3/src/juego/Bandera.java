package juego;

import java.awt.Color;

import entorno.Entorno;

public class Bandera {

	private double x;
	private double y;
	private double ancho;
	private double alto;

	
	public Bandera(double x, double y) {
		this.x = x;
		this.y = y;
		this.ancho = 20.0;
		this.alto = 150.0;

	}
	
	public void moverDerecha(double velocidadDelPiso) {
		this.x = this.x - velocidadDelPiso; //hacemos que la bandera se mueva a la velocidad del mapa 7 es la velocidad del piso. que aparece en la clase piso
	}
	public void moverIzquierda(double velocidadDelPiso) {
		this.x = this.x + velocidadDelPiso; //hacemos que la bandera se mueva a la velocidad del mapa 7 es la velocidad del piso. que aparece en la clase piso
	}
	
	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.WHITE); //aca dibujamos el mastil o palo
		
		double xTela = this.x - 35.0; //le restamos asi queda a la izquierda 
		double yTela = this.y - 50.0; // y le restamos para que quede mas abajo
		entorno.dibujarRectangulo(xTela, yTela, 50.0, 40.0, 0, Color.RED);
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	                                   // esto es para que la variable privadas, (x, y, ancho y alto) de bandera puedan acceder en las otras clases
	public double getAncho() {
		return this.ancho;
	}
	
	public double getAlto() {
		return this.alto;
	}
	
}