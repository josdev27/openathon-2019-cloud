<p align="center">
    <img src="../../resources/header.png">
</p>

# Lab 03.B - Haciendo el despliegue del HelloWorld a producción con Docker y Nginx

<br/>

<p align="center">
<img src="./resources/logoNginxDocker.png" width="500">
<br/>
¡Despleguemos con Docker y Nginx!
</p>
<br/>

## Objetivos y resultados
El objetivo de este laboratorio es **crear una imagen Docker** que contenga todo lo necesario para que al construir un contenedor con ella tengamos nuestra aplicación **HelloWorld desplegada en un servidor Nginx**. Esta imagen estaría **lista para ser desplegada** en nuestro entorno de producción.

<br/>

## Generar una nueva imagen y un contenedor para desplegar nuestra aplicación.

Para comenzar este laboratorio vamos a partir de la aplicación HelloWorld creada en el laboratiorio [anterior](../lab-03.B).



### Paso 1. Hacer pull de las imágenes Docker que usaremos como base.

Para construir nuestra imagen Docker vamos a utilizar como base otras dos imágenes:

- [Imagen de Node.js](https://hub.docker.com/_/node): Contiene una instalación de Node.js que vamos a necesitar para generar el entregable de nuestro HelloWorld.
- [Imagen de Nginx](https://hub.docker.com/_/nginx): Contiene una instalación de Nginx que usaremos como servidor web para publicar nuestra aplicación.

Para hacer el pull de ambas imágenes tan solo tenemos que ejecutar los siguientes comandos en nuestro terminal:

```sh
docker pull node
docker pull nginx
```

Si todo va bien, ambos deben aparecer en nuestra lista de imágenes tras ejecutar el siguiente comando:

```sh
docker images
```

<img src="./resources/docker_images.1.png" width="180" style="max-width:100%;">

### Paso 2. Crear un fichero de configuración para Nginx.

Para que nuestra aplicación Angular se despliegue y funcione correctamente necesitaremos crear un fichero de configuración básico para el servidor web Nginx. En este fichero se especifican cosas como el puerto en el que se publicará, la ruta del directorio raiz, las rutas permitidas, etc.

Crea un fichero llamado **nginx.conf** en la raiz del proyecto y copia en el lo siguiente:

```conf
worker_processes  1;
 
events {
  worker_connections  1024;
}
 
http {
  server {
    listen 80;
    server_name  localhost;
 
    root   /usr/share/nginx/html;
    index  index.html index.htm;
    include /etc/nginx/mime.types;
 
    gzip on;
    gzip_min_length 1000;
    gzip_proxied expired no-cache no-store private auth;
    gzip_types text/plain text/css application/json application/javascript application/x-javascript text/xml application/xml application/xml+rss text/javascript;
 
    location / {
      try_files $uri $uri/ /index.html;
    }

    location /assets/img/ {
    }
  }
}
```

### Paso 3. Crear el fichero Dockerfile.

El siguiente paso será crear un fichero **Dockerfile** para indicar a Docker cual será el contenido de la nueva imagen que vamos a crear. Crea un fichero con el nombre *Dockerfile* en la raiz del proyecto y copia lo siguiente en su interior:

```dockerfile
# Utilizamos la imagen de node como base y la denominamos build
FROM node as build

# Copiamos el fichero package.json a una nueva carpeta de trabajo
COPY ./package.json /usr/angular-workdir/
WORKDIR /usr/angular-workdir

# Lanzamos el comando npm install para que se descargue todas las dependencias
# definidas en nuestro fichero package.json
RUN npm install

# Copiamos todo el código del proyecto a la carpeta de trabajo
COPY ./ /usr/angular-workdir

# Ahora que tenemos todas las dependencias y todo el código podemos generar 
# nuestro entregable tal y como hacíamos en el laboratorio anterior.
RUN npm run build --prod

# Llega el momento de preparar el servidor web, para ello usaremos la imágen base
# de Nginx
FROM nginx

# Copiamos el fichero nginx.conf a la ruta adecuada en la imagen nginx
COPY ./nginx.conf /etc/nginx/nginx.conf

# Borramos todos los ficheros que pudieran existir en la ruta donde desplegaremos 
# el entregable que hemos generado antes 
RUN rm -rf /usr/share/nginx/html/*

# Finalmente copiamos nuestro entregable desde la imagen de node a la ruta de despliegue
# en la imagen de Nginx 
COPY --from=build /usr/angular-workdir/dist/HelloWorld /usr/share/nginx/html
```

En este fichero Dockerfile se especifican los pasos que hay que dar para generar la imagen tal y como la queremos. Los comentarios incluidos en el fichero te indican en que consiste cada paso y cual es su finalidad. 

### Paso 4. Crear la nueva imagen a partir del fichero Dockerfile.

En este punto vamos a crear la imagen Docker que hemos descrito en el punto anterior. Para ello simplemente ejecutaremos el siguiente comando en la ruta donde está el fichero Dockerfile:

```sh
docker build -t helloworld .
```

Si todo va bien, nuestra nueva imágen aparecera, junto a las de node y nginx, en nuestra lista de imágenes si ejecutamos el siguiente comando:
```sh
docker images
```

<img src="./resources/docker_images.2.png" width="180" style="max-width:100%;">

### Paso 5. Ejecutar nuestra nueva imagen para generar un nuevo contenedor.

Ahora que tenemos lista nuestra nueva imagen docker tan solo nos queda ejecutarla y crear así nuestro contenedor que arrancará el servidor web Nginx con nuestro HelloWorld.

Para ellos vamos a ejecutar el siguiente comando:
```sh
docker run -d -p 8080:80 helloworld
```

Si todo va bien, ya tendremos nuestro HelloWorld desplegado en el servidor Nginx, y podemos comprobarlo con solo acceder a [http://localhost:8080](http://localhost:8080).

Podemos ver nuestro nuevo contenedor ejecutando el siguiente comando:
```sh
docker ps
```

<img src="./resources/docker_ps.png" width="180" style="max-width:100%;">


<br/>

[< Lab 3.B XXXXXXXXXXX ](../lab-03.B) | [Lab 04 - XXXXXXXXXX >](../lab-04)

<p align="center">
    <img src="../../resources/header.png">
</p>

