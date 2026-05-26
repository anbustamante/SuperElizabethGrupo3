package juego;

import java.awt.Color;
import entorno.Entorno;

public class BolaDeFuego {

    private double x;
    private double y;
    private double diametro;

    private double velocidad;
    private double direccionX;
    private double direccionY;

    public BolaDeFuego(double xInicial, double yInicial, double mouseX, double mouseY) {
        this.x = xInicial;
        this.y = yInicial;
        this.diametro = 12;
        this.velocidad = 8;

        // Con esto calculo hacia donde tiene que ir la bola
        // la bola aparece en donde está la princesa y apunta en la dirección del mouse.
        double distanciaX = mouseX - xInicial;
        double distanciaY = mouseY - yInicial;
        // Calculo la distancia total entre la princesa y el mouse

        // En google dice que esto es para que la bola no vaya más rápido en diagonal
        double distanciaTotal = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

        // si el mouse está arriba de la princesa, no divido por 0

        if (distanciaTotal == 0) {
            this.direccionX = 1;
            this.direccionY = 0;
        } else {
            this.direccionX = distanciaX / distanciaTotal;
            this.direccionY = distanciaY / distanciaTotal;
        }
    }

    public void mover() {
        this.x = this.x + this.direccionX * this.velocidad;
        this.y = this.y + this.direccionY * this.velocidad;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarCirculo(this.x, this.y, this.diametro, Color.ORANGE);
    }

    public boolean salioDePantalla(Entorno entorno) {
        return this.x < 0 || this.x > entorno.ancho() || this.y < 0 || this.y > entorno.alto();
    }
}