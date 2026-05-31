# Documentacion del proyecto: Super Elizabeth Sis

## 1. Introduccion

Este proyecto implementa un videojuego estilo plataformas inspirado en el enunciado del TP: Elizabeth debe avanzar por un escenario con pisos, huecos e islas flotantes, eliminar peces del infierno con bolas de fuego y evitar caer al precipicio.

La solucion fue pensada para que se pueda explicar en una defensa oral: cada clase tiene una responsabilidad concreta, los opcionales estan separados y la configuracion central se encuentra en `Configuracion.java`.

## 2. Idea general del ciclo de juego

El entorno ejecuta permanentemente el metodo `tick()` de `Juego`. En cada tick se realiza la siguiente secuencia:

1. Se dibuja el fondo.
2. Si el juego esta en estado `JUGANDO`, se actualizan los objetos.
3. Se resuelven colisiones.
4. Se dibujan escenario, items, peces, bombas, fuego, Elizabeth y HUD.
5. Se verifica si el jugador gano o perdio.
6. Si el juego termino, se muestra pantalla final y se permite reiniciar con `R`.

## 3. Decisiones de diseno

### 3.1. Arreglos y objetos nulos

El enunciado pide usar arreglos de objetos para los peces. Por eso `AdministradorPeces` usa:

```java
private Pez[] peces;
private Bomba[] bombas;
```

Cuando un pez desaparece por ser eliminado o por salir de la pantalla, su posicion se vuelve `null`. Esto cumple la aclaracion del TP: no alcanza con ocultar el objeto, debe eliminarse logicamente.

### 3.2. Una clase central de configuracion

La clase `Configuracion` contiene constantes como tamano de pantalla, velocidad, gravedad y banderas opcionales. Esto permite modificar el juego sin buscar valores magicos en muchas clases.

### 3.3. Opcionales atomicos

Los opcionales se activan con booleanos:

```java
public static final boolean OPCIONAL_PECES_VARIADOS = true;
public static final boolean OPCIONAL_BOMBAS_DE_PECES = true;
public static final boolean OPCIONAL_HECHIZO = true;
public static final boolean OPCIONAL_ITEMS = true;
public static final boolean OPCIONAL_NIVELES = true;
public static final boolean OPCIONAL_JEFE_COLOSAL = true;
```

Asi, cada feature queda relativamente encajable/desencajable.

## 4. Descripcion de clases

### 4.1. `Juego`

Es la clase principal. Hereda de `InterfaceJuego`, crea el `Entorno` y contiene el metodo `tick()`.

Responsabilidades:

- Inicializar el juego.
- Coordinar actualizaciones.
- Coordinar dibujos.
- Verificar victoria y derrota.
- Resolver colisiones principales.
- Manejar disparos y hechizo.

Variables importantes:

- `Entorno entorno`: ventana e input.
- `Elizabeth elizabeth`: personaje principal.
- `AdministradorEscenario escenario`: piso e islas.
- `AdministradorPeces administradorPeces`: peces y bombas.
- `AdministradorItems administradorItems`: items.
- `BolaFuego bolaFuego`: disparo actual; es `null` si no hay disparo.
- `EstadoJuego estado`: indica si se esta jugando, ganado o perdido.
- `pecesEliminados`: marcador.
- `nivel`: dificultad actual.

### 4.2. `Configuracion`

Centraliza valores constantes. Evita que el proyecto quede lleno de numeros sueltos.

Ejemplos:

- `ANCHO_VENTANA`
- `ALTO_VENTANA`
- `PECES_PARA_GANAR`
- `VELOCIDAD_ESCENARIO`
- `GRAVEDAD`
- banderas de opcionales

### 4.3. `EstadoJuego`

Enum con tres estados:

- `JUGANDO`
- `GANADO`
- `PERDIDO`

Hace que el flujo del juego sea mas claro que usar Strings o enteros.

### 4.4. `Entidad`

Clase base para objetos con posicion y tamano. Implementa metodos comunes:

- `izquierda()`
- `derecha()`
- `arriba()`
- `abajo()`
- `colisionaCon(Entidad otra)`
- `distanciaA(Entidad otra)`

La colision usada es rectangular, suficiente para un TP introductorio y facil de explicar.

### 4.5. `Elizabeth`

Representa a la princesa.

Responsabilidades:

- Leer flechas del teclado.
- Moverse horizontalmente.
- Saltar si esta apoyada.
- Aplicar gravedad si no esta apoyada.
- Dibujarse.

Decision importante: Elizabeth recuerda `yAnterior` para que `Juego` pueda saber si cayo desde arriba sobre una plataforma.

### 4.6. `Plataforma`

Representa piso o isla flotante.

Variables:

- `esPiso`: distingue piso de isla.

Metodos:

- `moverConEscenario()`
- `dibujar()`

### 4.7. `AdministradorEscenario`

Administra el arreglo de plataformas.

Responsabilidades:

- Crear escenario inicial con piso, huecos e islas.
- Mover plataformas de derecha a izquierda.
- Eliminar plataformas que salen por izquierda.
- Generar nuevos tramos de piso e islas.

### 4.8. `BolaFuego`

Disparo de Elizabeth.

Responsabilidades:

- Moverse hacia el mouse al momento del disparo.
- Guardar dano.
- Dibujarse.

Regla obligatoria: solo puede existir una a la vez. Esto se controla en `Juego` usando `bolaFuego == null`.

### 4.9. `Pez`

Representa a los peces enemigos.

Tipos implementados:

- `NORMAL`
- `PERSEGUIDOR`
- `ESQUIVADOR`
- `ONDULANTE`
- `RESISTENTE`
- `COLOSAL`

Cada tipo modifica su movimiento o resistencia.

### 4.10. `AdministradorPeces`

Administra peces y bombas.

Responsabilidades:

- Guardar peces en arreglo.
- Crear peces aleatorios.
- Evitar superposicion inicial.
- Mantener separacion vertical minima.
- Eliminar peces que salen por izquierda.
- Crear bombas.
- Crear jefe colosal cuando corresponde.

### 4.11. `Bomba`

Proyectil de los peces.

Responsabilidades:

- Moverse hacia Elizabeth.
- Colisionar con Elizabeth o con la bola de fuego.
- Dibujarse.

### 4.12. `ItemEspecial`

Items opcionales.

Tipos:

- `PUNTOS`: suma un punto.
- `TRAMPA`: resta un punto si se puede.
- `FUEGO_POTENCIADO`: mejora la proxima bola de fuego.
- `ESCUDO`: protege temporalmente.

### 4.13. `AdministradorItems`

Crea, mueve, dibuja y elimina items especiales.

### 4.14. `Hud`

Dibuja textos de pantalla:

- peces eliminados
- peces restantes
- nivel
- fuego potenciado
- escudo
- cooldown del hechizo
- pantalla final

## 5. Colisiones

### 5.1. Elizabeth con plataformas

Se usa una logica de aterrizaje:

1. Se verifica que Elizabeth se pise horizontalmente con la plataforma.
2. Se verifica que los pies hayan cruzado la parte superior de la plataforma desde arriba.
3. Si se cumple, se apoya a Elizabeth sobre la plataforma.

Tambien hay una condicion complementaria para cuando Elizabeth ya estaba parada y el piso se desplaza con el escenario.

### 5.2. Fuego con peces

Si la bola de fuego colisiona con un pez:

1. El pez recibe dano.
2. La bola desaparece.
3. Si la vida del pez llega a cero, se suma al contador y el pez se pone en `null`.

### 5.3. Fuego con piso o islas

Si la bola toca piso o isla, solo desaparece la bola de fuego.

### 5.4. Elizabeth con peces o bombas

Si Elizabeth tiene escudo, destruye o ignora el peligro. Si no tiene escudo, pierde.

## 6. Como explicar el proyecto en una defensa oral

Una forma clara de explicarlo seria:

1. El juego se organiza alrededor de `Juego.tick()`.
2. `tick()` actualiza, resuelve colisiones y dibuja.
3. Los objetos del juego heredan datos basicos de `Entidad`.
4. Los peces se guardan en un arreglo, como pide el enunciado.
5. Los opcionales se activan desde `Configuracion`.
6. El movimiento del escenario se logra moviendo plataformas hacia la izquierda.
7. Elizabeth tambien se desplaza con el escenario si no se presiona ninguna tecla.
8. La bola de fuego se crea solo si no existe otra.
9. Ganar/perder depende del contador, colision con enemigos o caida.

## 7. Posibles mejoras futuras

- Reemplazar dibujos geometricos por imagenes usando `entorno.dibujarImagen`.
- Agregar musica o sonidos si el entorno/proyecto base lo permite.
- Agregar pantalla de inicio.
- Agregar seleccion de dificultad.
- Guardar puntaje maximo.
- Separar cada tipo de pez en subclases si se desea una solucion mas orientada a objetos.

## 8. Conclusiones

El proyecto cumple los requerimientos obligatorios y agrega opcionales sin mezclar todo dentro de una unica clase. La estructura esta pensada para que una persona que esta empezando pueda leer el codigo, explicar cada responsabilidad y modificar features concretas sin romper el resto del juego.
