# Super Elizabeth Sis contra el ataque de los peces del infierno

Proyecto Java para el TP de Programacion I.

## Como usar este proyecto en Eclipse

1. Crear o importar un proyecto Java en Eclipse.
2. Copiar el archivo `entorno.jar` entregado por la catedra dentro de la carpeta `lib/`.
3. Verificar que `lib/entorno.jar` este en el Build Path.
4. Ejecutar `src/juego/Juego.java`.

## Controles

- Flecha izquierda: retroceder.
- Flecha derecha: avanzar.
- Flecha arriba: saltar, solo si Elizabeth esta apoyada.
- Click izquierdo: lanzar bola de fuego.
- Click derecho: lanzar hechizo, si esta disponible y se clickea un pez.
- R: reiniciar la partida.

## Reglas principales

- Elizabeth pierde si toca un pez o una bomba sin escudo.
- Elizabeth pierde si cae al precipicio.
- Se gana al llegar a la cantidad de peces eliminados indicada en `Configuracion.PECES_PARA_GANAR`.
- Solo puede existir una bola de fuego a la vez.
- Los peces se guardan en un arreglo de objetos y se eliminan poniendo la posicion en `null`.

## Opcionales implementados

Todos los opcionales estan aislados en clases/metodos y pueden activarse o desactivarse desde `Configuracion.java`.

- Peces variados: normal, perseguidor, esquivador, ondulante, resistente y colosal.
- Bombas de peces.
- Hechizo magico con enfriamiento.
- Items especiales: puntos, trampa, fuego potenciado y escudo.
- Sistema de niveles.
- Jefe colosal.

## Estructura

```text
src/juego/
  Juego.java
  Configuracion.java
  EstadoJuego.java
  Entidad.java
  Elizabeth.java
  Plataforma.java
  BolaFuego.java
  Pez.java
  Bomba.java
  ItemEspecial.java
  AdministradorEscenario.java
  AdministradorPeces.java
  AdministradorItems.java
  Hud.java
```
