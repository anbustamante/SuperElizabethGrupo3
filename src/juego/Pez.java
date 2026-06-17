package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.*;

public class Pez {
    private double x;
    private double y;
    private double ancho;
    private double alto;
    private double velocidad;
    private Image img;

    public Pez(double x, double y) {
        this.x = x;
        this.y = y;
        this.ancho = 30;
        this.alto = 50;
        this.velocidad = 8;

        this.img = Herramientas.cargarImagen("pez.png");
    }
    public void dibujar(Entorno entorno) {
        entorno.dibujarImagen(this.img, this.x, this.y, 0, 1);
    }
 // En Pez.java, usá este método para la lógica de movimiento
    public void moverDerecha() {
        this.x -= this.velocidad;
        
    }
    public boolean salioDePantalla(Entorno entorno) {
        return this.x < 0 || this.x > entorno.ancho() || this.y < 0 || this.y > entorno.alto();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public double getAncho() {
        return ancho;
    }
    
    public double getAlto() {
        return alto;
    }
    
    
}
