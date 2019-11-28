<p align="center">
    <img src="../../resources/header.png">
</p>

# Lab 03 - Hello World en Spring Boot

<br/>

<p align="center">
<img src="./resources/spring-boot-logo.png" width="500">
<br/>
Welcome Spring!
</p>
<br/>

## Objetivos y resultados
El objetivo de este laboratorio es crear y levantar con Docker  un microservicio desarrollado en Java usando [Spring Boot](https://spring.io/projects/spring-boot). Al final de este laboratorio, tendremos una API con un endpoint **Get “/hello?name={name}”**. En los siguientes laboratorios, este microservicio será consumido por una aplicación Angular, y se conectará a una base de datos dockerizada para obtener saludos aleatoriamente.

## Prerrequisitos

El único prerrequesito es tener una cuenta en Docker :).

## Levantar tu primera aplicación Spring Boot

### Introducción

El microservicio tiene un controlador con un único endpoint:

```sh
GET /demo/hello?name={name}
```

Este endpoint recibe un parámetro “name” y devuelve un JSON con el atributo "saludo" y 200 OK como código HTTP:

```sh
GET /demo/hello?name=Docker
```

Y la respuesta:

```sh
{"saludo":"Hello, Docker"}
```

### Clonar el repositorio

Partiendo del directorio home (~), tienes que clonar el proyecto desde [spring_boot_app](https://github.com/josdev27/spring_boot_app). Añadir una nueva instancia en play-with-docker y ejecutar:

```sh
[node1] (local) root@192.168.0.23 ~
$ git clone https://github.com/josdev27/spring_boot_app.git
Cloning into 'spring_boot_app'...
remote: Enumerating objects: 47, done.
remote: Counting objects: 100% (47/47), done.
remote: Compressing objects: 100% (31/31), done.
remote: Total 47 (delta 5), reused 39 (delta 1), pack-reused 0
Unpacking objects: 100% (47/47), done.
```

En este momento, hemos generado el directorio **spring_boot_app** con el código fuente. El próximo paso es cambiarnos con el comando *cd*:

```sh
[node1] (local) root@192.168.0.23 ~
$ cd spring_boot_app/
[node1] (local) root@192.168.0.23 ~/spring_boot_app
$ ls
Dockerfile  README.md   pom.xml     src
```

El contenido de la aplicación es:

1. **src**: contiene todo el código fuente de la aplicación (controlador, servicio, etc).
2. **pom.xml**: permite a maven saber como construir la aplicación.
3. **README.md**: es el readme del proyecto.
4. **Dockerfile**: este fichero nos permite indicarle a Docker como construir la imagen de la aplicación.

Si nos centramos en el *Dockerfile*, podemos ver que contiene dos fases. Una de ellas es usar maven para construir el jar de nuestra aplicación. La otra es usar openjdk-8 para poder levantarla. Notar que usamos el comando **AS** para indicarle a Docker que del resultado de la primera fase (que es la que parte de maven), lo vamos a usar en la segunda (que es la parte de openjdk). 
Para más información mirar, https://docs.docker.com/develop/develop-images/multistage-build/. 

```dockerfile
### Partimos de una imagen de maven para contruir el jar de la aplicación
FROM maven:3-jdk-8 AS builder

### Copiamos el pom.xml y el src/ en el directorio build del contenedor
COPY pom.xml /build/
COPY src /build/src/

### Cambiamos al directorio build/ para construir el jar
WORKDIR /build/
RUN mvn package

### Partimos de una imagen de openjdk-8 para poder ejecutar el jar
FROM openjdk:8-jdk-alpine

### Cambiamos al directorio app
WORKDIR /app/

### Partiendo de la fase anterior, copiamos el jar a este contenedor y lo llamamos demo.jar
### Nota: maven por defecto, guarda el jar en target
COPY --from=builder /build/target/*.jar demo.jar

### Indicamos a Docker que al levantar nuestra imagen ejecute el comando java -jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]
```

### Construir y ejecutar el microservicio

El siguiente paso es ejecutar la aplicación para verificar que los pasos son correctos.. En la misma instancia ejecutamos:

```sh
docker build -t josdev27/spring_boot_app .
```

La opción **-t** nos permite darle un nombre a nuestra imagen Docker, mientras que **.** indicamos que el **Dockerfile** está en el directorio actual.
Si todo a ido bien, en la salida veremos:

```sh
Successfully tagged josdev27/spring_boot_app:latest
```

Una forma de comprobarlo es que al ejecutar el siguiente comando (*docker images*), tenemos que tenerla imagen **josdev27/spring_boot_app**:

```sh
[node1] (local) root@192.168.0.23 ~/spring_boot_app
$ docker images
REPOSITORY                 TAG                 IMAGE ID            CREATED             SIZE
josdev27/spring_boot_app   latest              b150c2fb08de        8 seconds ago       122MB
<none>                     <none>              366efaa5317f        16 seconds ago      567MB
maven                      3-jdk-8             9b5dcb455379        27 hours ago        499MB
openjdk                    8-jdk-alpine        a3562aa0b991        6 months ago        105MB
```

Para arrancar la imagen de Docker, usamos el mandato *docker run*:

```sh
docker run -d -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=local" -t josdev27/spring_boot_app
```

1. **run**: permite lanzar una imagen de docker. En este caso,  maven:3.3-jdk-8 https://hub.docker.com/_/maven, que nos permite ejecutar maven para contruir y levantar nuestro microservicio.
2. **-d**: permite lanza el contenedor en background.
3. **-p**: el formato es host_port:container_port. En este caso, el puerto 8080 de la máquina lo redirijimos al puerto 8080 del contenedor (por el que está escuchando el microservicio).
4. **-e**: nos permite pasar variables de entorno. En este caso, para que el microservicio se ejecute con el perfil *local*. El perfil *db* es que nos permite usar la base de datos que se verá en el [lab-04](../../lab-04/README.md).
3. **-p**: el formato es host_port:container_port. En este caso, el puerto 8080 de la máquina lo redirijimos al puerto 8080 del contenedor (por el que está escuchando el microservicio).
3. **-p**: el formato es host_port:container_port. En este caso, el puerto 8080 de la máquina lo redirijimos al puerto 8080 del contenedor (por el que está escuchando el microservicio).
5. **-t**: para indicar qué imagen queremos ejecutar

Para más información, mirar https://docs.docker.com/engine/reference/commandline/run/.

El proceso puede ser lento ya que tiene que descargar la imagen base (Take easy! ;))

Si todo es correcto, deberias de ver el siguiente mensaje en la terminal:

```sh
[node1] (local) root@192.168.0.8 ~/spring_boot_app
$ docker run -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=local" -t josdev27/spring_boot_app
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.1.RELEASE)

2019-11-25 12:03:59.941  INFO 1 --- [           main] com.josdev27.sample.DemoApplication      : Starting DemoApplication v0.0.1-SNAPSHOT on 62ba03bc412d with PID 1 (/app/demo.jar started by root in /app)
2019-11-25 12:03:59.946  INFO 1 --- [           main] com.josdev27.sample.DemoApplication      : The following profiles are active: local
2019-11-25 12:04:03.344  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2019-11-25 12:04:03.376  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2019-11-25 12:04:03.377  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.27]
2019-11-25 12:04:03.538  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2019-11-25 12:04:03.539  INFO 1 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 3402 ms
2019-11-25 12:04:04.231  INFO 1 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2019-11-25 12:04:04.833  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-11-25 12:04:04.839  INFO 1 --- [           main] com.josdev27.sample.DemoApplication      : Started DemoApplication in 6.191 seconds (JVM running for 7.4)
```

### Verificar que la aplicación está escuchando

Para hacer la petición, vamos a utilizar el comando curl. Este comando nos permite hacer peticiones HTTP desde la terminal:

```sh
curl -X GET http://localhost:8080/demo/hello\?name\=Jos
```

```sh
[node2] (local) root@192.168.0.22 ~
$ curl -X GET http://localhost:8080/demo/hello\?name\=Jos
{"saludo":"Hello, Jos"}[node2] (local) root@192.168.0.22 ~
```

Si todo va bien, veremos por la salida:

```sh
{"saludo":"Hello, Jos"}
```

## Resumen
Hemos clonado una aplicación Spring Boot y la hemos dockerizado. Luego, hemos sido capaces de probar que nuestra aplicación está funcionando. Todo sin escribir una linea de código ;)

El siguiente paso será conectar está aplicación al frontend en Angular y a la capa de datos en PostgreSQL, todo ello en contenedores docker.



< [Lab 03 - Frontend - Haciendo el despliegue de un HelloWorld con Docker y Nginx ](../frontend) | [Lab 04 - PostgreSQL y volúmenes persistentes con docker](../../lab-04/)>

<p align="center">
    <img src="../../resources/header.png">
</p>

