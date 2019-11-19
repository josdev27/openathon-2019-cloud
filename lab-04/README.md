# Lab 04 - PostgreSQL y volúmenes persistentes con docker
<br/>
<p align="center">
<img src="resources/docker-postgresql.png" width="500">
<br/>
¡Persistiendo datos!
</p>
<br/>

## Objetivos y resultados
El objetivo de este laboratorio es crear una imagen de la base de datos **PostgreSQL** y la configuración de un volumen persintente de datos.
Como resultado, obtendremos un contenedor.....
<br/>
## Uso de volúmenes
 
 
<br/>
## ¿Cuál es la ventaja de usar volúmenes?
...
<br/>
## ¿Cómo funciona?

<br/>
**-	TODO:** l sección de y una capa de persistencia.
<br/>

## Desplegar una PosgreSQL

### Paso 1. Generar una nueva imagen e iniciar un contenedor con PostgreSQL.
Para construir nuestra imagen Docker vamos a utilizar como base otras dos imágenes:
- [Imagen de PostgreSQL](https://hub.docker.com/_/postgres): Contiene una instalación de PostgreSQL.

Vamos a usar **docker run** para iniciar un nuevo contenedor desde la imagen oficial de postgres con el nombre de postgres-openathon y exponiendo el puerto 5432 ( por defecto en PostgreSQL). Lo vamos a ejectuar en modo background con el flag **-d**.

También vamos a montar un volumen con el flag **-v**. El volumen lo identificaremos como postgres-data, docker automáticamente lo creará si no existe un volumen en el mismo nombre.

Si es la primera vez que ejecutamos el comando, se hará pull de la imagen. Una vez descargada, iniciará el contenedor y montará el volumen. Tan sólo tenemos que ejecutar el siguiente comando en nuestro terminal:

```sh
docker run -d -p 5432:5432 -v postgres-data:/var/lib/postgresql/data --name postgres-openathon postgres
```
Podemos comprobar que está corriendo el contenedor con  
```sh
docker ps
```
y ver la salida de log
```sh
docker ps
```
docker logs postgres-openathon

Podemos inspeccionar la información sobre el volumen que hemos creado con **docker volume inspect** y observar 
```sh
docker volume inspect
```

### Paso 2. Crear una base de datos.
Vamos a crear una base de datos haciendo uso de **docker exec** para lanzar un shell dentro de nuestro contenedor postgres-openathon
```sh
docker exec -it postgres-openathon sh
```
Dentro del shell creamos una base de datos con el nombre openathon-db
```sh
# createdb -U postgres openathon-db
```
Y por último ejecutamos la utilidad CLI de PostgreSQL **psql** para conectarnos a la base de datos openathon-db
```sh
# psql -U postgres openathon-db
```

### Paso 3. Explorar la base de datos
Dentro de psql, vamos a ejecutar algunos comandos básicos. **\l** lista las bases de datos
```sh
openathon-db=# \l
```
Para obtener la versión de PostgreSQL
```sh
openathon-db=# select version();
```
Vamos a crear la tabla book
```sh
openathon-db=# INSERT INTO book (id,name) VALUES (1, 'Using Dooker');
```
Comprobamos los datos insertados
```sh
openathon-db=# SELECT * FROM book;
``` 
Podemos salir de psql con **\q** y **exit** de nuestro shell
```sh
openathon-db=# \q 
# exit
``` 

### Paso 4. Eliminar el contenedor PostgreSQL
Vamos a parar y eliminar el contenedor **postgres-openathon** en un sólo comando, el flag **-f** fuerza la eliminación de un contenedor en ejecución. 
Podemos eliminar el contenedor con la seguridad de no perder los datos, estos seguirán en el volumen **postgres-data**
```sh
docker rm -f postgres-openathon
``` 
Podemos comprobar que está corriendo el contenedor con  
```sh
docker ps
```

### Paso 5. Adjuntar un volumen a un contedor
Vamos a crear un  nuevo contenedor llamado **postgres-openathon-v** pero adjuntando el volumen que creamos anteriormente **postgres-data** que contiene nuestra base de datos
```sh
docker run -d -p 5432:5432 -v postgres-data:/var/lib/postgresql/data --name postgres-openathon-v postgres
```
Una vez iniciado el contenedor accedemos al shell 
```sh
docker exec -it postgres-openathon-v sh
```
e iniciamos una sesión en psql para comprobar que los datos creados siguen estando presentes
```sh
# psql -U postgres mydb
```
```sh
openathon-db=# SELECT * FROM book;
```
Por último salimos de nuevo
```sh
openathon-db=# \q
# exit
```

### Paso 6. Eliminando recursos (opcional)
Si quieres repetir el laboratorio desde el principio, puedes eliminar todos los recuros creados eliminado tanto el contedor como el volumen creados
```sh
docker rm -f postgres-openathon-v
``` 
```sh
docker volume rm postgres-data
``` 