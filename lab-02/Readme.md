## ¿Qué es un DockerFile?

Un Dockerfile es un archivo de texto plano que contiene una serie de instrucciones necesarias para crear una imagen que, posteriormente, se convertirá en una sola aplicación utilizada para un determinado propósito.

Docker puede construir imágenes automáticamente leyendo las instrucciones de un Dockerfile.

Ejemplo de Dockerfile:

```sh
# Descarga la imagen de Ubuntu 18.04
FROM ubuntu:18.04

# Actualiza la imagen base de Ubuntu 18.04
RUN apt-get update


# Instalar Git
RUN apt-get -qqy install git
```

### Imágenes a medida con Dockerfile


Docker puede construir imágenes automáticamente, leyendo las instrucciones indicadas en un fichero Dockerfile. 
Los pasos principales para crear una imagen a partir de un fichero Dockerfile son:
  - Crear un nuevo directorio que contenga el fichero y otros ficheros que fuesen necesarios para crear la imagen.
  - Crear el contenido.
  - Construir la imagen mediante el comando docker build.

La sintaxis del comando es:
```sh
docker build [opciones] RUTA | URL | -
```
Las opciones más comunes son:

-   -t, nombre [:etiqueta]. Crea una imagen con el nombre y la etiqueta especificada a partir de las instrucciones indicadas en el fichero. Es recomendable asignar siempre un nombre a las imágenes que creamos.
-   –no-cache. Establecida por defecto, Docker guarda en memoria caché las acciones realizadas recientemente. Si se diese el caso de que ejecutamos un docker build varias veces, Docker comprobará si el fichero contiene las mismas instrucciones y, en caso afirmativo, no generará una nueva imagen. Para generar una nueva imagen omitiendo la memoria caché utilizaremos siempre esta opción.
-   –pull. También por defecto. Docker solo descargará la imagen especificada en la expresión FROM si no existe en el repositorio local. Para forzar que descargue la nueva versión de la imagen utilizaremos esta opción.
-   –quiet. Por defecto, se muestra todo el proceso de creación, los comandos ejecutados y su salida. Utilizando esta opción solo mostrará el identificador de la imagen creada.
##### Vamos a crear nuestro primera imagen con Dockerfile
El primer paso es crear un directorio
```sh
mkdir laboratorio2
```
Accedemos al nuevo directorio
```sh
cd laboratorio2
```
Creamos un nuevo fichero Dockerfile(podemos hacer uso del editor o bien con el comando vi)
```sh
vi Dockerfile
```
Escribimos el siguiente contenido en el fichero:

```sh
FROM ubuntu:latest
RUN apt-get -y update; 
RUN apt-get -y install git;
CMD ["bash"]
```

Una vez creado el fichero y editado, lo guardamos. Ahora realizamos la construcción de la imagen:

```sh
docker build  -t "laboratorio2:dockerfile" .
```
Si todo ha ido bien, aparecerá un mensaje como el siguiente:
```sh
Successfully tagged laboratorio2:dockerfile
```
Comprobaremos que la imagen está disponible ejecutando docker images
```sh
[node1] (local) root@192.168.0.23 ~/laboratorio2
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED              SIZE
laboratorio2        dockerfile          d6def67745f8        About a minute ago 186MB
```

Ahora vamos a crear un contendor a partir de la imagen. Para ello ejecutaremos lo siguiente:

```sh
docker run -dti --name containerlaboratorio2 d6def67745f8
```
Siendo d6def67745f8 el IMAGE ID de nuestra imagen.

Ejecutamos docker ps para verificar que el contenedor está levantado:
```sh
[node1] (local) root@192.168.0.23 ~/laboratorio2
$ docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED              STATUS              PORTS               NAMES
5d8723797fd0        d6def67745f8        "bash"              About a minute ago   Up About a minute                       containerlaboratorio2
```
Por último, accedemos al contenedor para verificar que tiene git instalado.
```sh
[node1] (local) root@192.168.0.23 ~/laboratorio2
$ docker exec -i -t containerlaboratorio2 /bin/bash
root@5d8723797fd0:/# git --version
git version 2.17.1
```

### Instrucciones
Aunque en esta [URL](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/) disponemos de detalle sobre las distintas instrucciones y mejores prácticas para escribir Dockerfiles aquí mostramos un resumen con las más importantes.

FROM: Indica la imagen base sobre la que se construirá la aplicación dentro del contenedor. Todos los Dockerfiles comienzan con un FROM.
```sh
FROM  <imagen>
FROM  <imagen>:<tag>
```

RUN: Nos permite ejecutar comandos en el contenedor.
```sh
RUN  <comando>
```
ENV: Establece variables de entorno dentro del contenedor.
```sh
ENV  <clave> <valor>
```
ADD: Esta instrucción se encarga de copiar los ficheros y directorios desde una ubicación especificada y los agrega al sistema de ficheros del contenedor.

```sh
ADD <fuente> <destino>
```

MAINTAINER: Nos permite configurar datos del autor del Dockerfile, principalmente su nombre y su dirección de correo electrónico.
```sh
MAINTANER <nombre> <"correo">
```
LABEL: Nos permite añadir metadatos a nuestra imagen.
```sh
LABEL <clave> <valor>
```


COPY: La instrucción copia ficheros y directorios de un path origen y los añade a un path destino dentro del contenedor.
```sh
COPY <origen> <destino>
```

EXPOSE: Indica los puertos en los que va a escuchar el contenedor. Con ello, los puertos no serán accesibles desde el host, para esto será necesario utilizar la exposición de puertos mediante la opción -p de docker run.
```sh
EXPOSE <puerto>
```
VOLUME: Esta instrucción crea un volumen como punto de montaje dentro del contenedor y es visible desde el host anfitrión.
```sh
VOLUME <path>
```
WORKDIR: Establece el directorio por defecto para la ejecución de las instrucciones RUN, CMD, ENTRYPOINT, COPY y ADD siguientes en el Dockerfile.
```sh
WORKDIR <path>
```
USER: Por defecto, todas las acciones son realizadas por el usuario root. Aquí podemos indicar un usuario diferente.
```sh
USER <usuario>
```
## Crear imagen con contenido estático
Es posible crear una imagen que muestre contenido estático. Para ello podemos hacer uso de nginx para que nos proveea del enrutado.

Vamos a realizar un ejemplo de ello. Lo primero es crear un nuevo directorio para realizar la práctica:

```sh
mkdir laboratorio2_2
```
Accedemos al nuevo directorio:

```sh
cd laboratorio2_2
```

Vamos a proceder a crear el contenido estático que queremos mostrar. En este caso una etiqueta simple HTML y lo guardaremos en un fichero index.html.
Para ello, podemos hacer uso de vi o bien usar el editor.

```sh
vi index.html
```

Guardamos el fichero. En nuestro caso con contenido:  
`
<h1>Hello World</h1>
`

Ahora vamos a crear un Dockerfile con el mismo procedimiento. El contenido será el siguiente:

```sh
  FROM nginx:alpine
  COPY . /usr/share/nginx/html
```
Se parte de una imagen de nginx y copiamos nuestro contenido.

El siguiente paso es crear una imagen para este Dockerfile. 

```sh
  docker build -t practicanginx:v1 .
```

¿Sabrías como comprobar que se ha creado la imagen?

```sh
 [node1] (local) root@192.168.0.33 ~/laboratorio2_2
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
practicanginx       v1                  0f6dbc295378        21 minutes ago      21.4MB
nginx               alpine              b6753551581f        2 weeks ago         21.4MB
```
Ahora vamos a levantar el contenedor:
```sh
  docker run -d -p 80:80 practicanginx:v1 
```

¿Sabías comprobar que el contendor está arrancado?
```sh
[node1] (local) root@192.168.0.33 ~/laboratorio2_2
$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                NAMES
e62f948c8f89        practicanginx:v1    "nginx -g 'daemon of…"   25 minutes ago      Up 25 minutes       0.0.0.0:80->80/tcp   distracted_vaughan
```
No solo eso, ahora podemos ver que un nuevo link ha aparecido en nuestra ventana, informando que el puerto 80 está a la escucha. 
<br/>
<p align="center">
<img src="../resources/Lab2_Puerto.JPG">
<br/>
</p>
<br/>
Si abrimos el link podremos ver el contenido de nuestro HTML en una nueva ventana:
<br/>
<p align="center">
<img src="../resources/Lab2_HTML.JPG">
<br/>
</p>
<br/>



