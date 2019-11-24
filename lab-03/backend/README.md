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
El objetivo de este laboratorio es crear y levantar con Docker  un microservicio desarrollado in Java usando [Spring Boot](https://spring.io/projects/spring-boot). Al final de este laboratorio, tendremos una API con un endpoint **Get “/hello?name={name}”**. En los siguientes laboratorios, este microservicio será consumido por una aplicación Angular.

## Prerrequisitos

El único prerrequesito es tener una cuenta en Docker :).

## Levantar tu primera aplicación Spring Boot

### Introducción

El microservicio tiene un controlador con un único endpoint:

```sh
GET /demo/hello?name={name}
```

Este endopint recibe un parámetro “name” y devuelve el String Hello, {name}  con 200 OK como código HTTP:

```sh
GET /demo/hello?name=Docker
```

Y la respuesta:

```sh
Hello, Docker
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

Si nos centramos en el *Dockerfile*, podemos ver que contiene dos fases. Una de ellas es usar maven para contruir el jar de nuestra aplicación. La otra es usar openjdk-8 para poder levantarla. Notar que usamos el comando **AS** para indicarle a Docker que del resultado de la primera fase (que es la que parte de maven), lo vamos a usar en la segunda (que es la parte de openjdk). Para más información mirar, https://docs.docker.com/develop/develop-images/multistage-build/. 

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
ENTRYPOINT ["java","-jar","demo.jar"]
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
docker run -p 8080:8080 -t josdev27/spring-boot-app
```

1. **run**: permite lanzar una imagen de docker. En este caso,  maven:3.3-jdk-8 https://hub.docker.com/_/maven, que nos permite ejecutar maven para contruir y levantar nuestro microservicio.
2. **-p**: el formato es host_port:container_port. En este caso, el puerto 8080 de la máquina lo rederijimos al puerto 8080 del contenedor (por el que está escuchando el microservicio).
3. **-t**: para indicar que imagen queremos ejecutar

Para más información, mirar https://docs.docker.com/engine/reference/commandline/run/.

El proceso puede ser lento ya que tiene que descargar la imagen base (Take easy! ;))

Si todo es correcto, deberias de ver el siguiente mensaje en la terminal:

```sh
[node1] (local) root@192.168.0.23 ~/spring_boot_app
$ docker run -p 8080:8080 -t josdev27/spring_boot_app

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.1.RELEASE)

2019-11-24 18:29:55.884  INFO 1 --- [           main] com.josdev27.sample.DemoApplication      : Starting DemoApplication v0.0.1-SNAPSHOT on 6cd796d80e88 with PID 1 (/app/demo.jar started by root in /app)
2019-11-24 18:29:55.894  INFO 1 --- [           main] com.josdev27.sample.DemoApplication      : No active profile set, falling back to default profiles: default
2019-11-24 18:30:00.740  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2019-11-24 18:30:00.778  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2019-11-24 18:30:00.784  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.27]
2019-11-24 18:30:00.943  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2019-11-24 18:30:00.943  INFO 1 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 4796 ms
2019-11-24 18:30:01.750  INFO 1 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2019-11-24 18:30:02.843  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-11-24 18:30:02.858  INFO 1 --- [           main] com.josdev27.sample.DemoApplication      : Started DemoApplication in 9.137 seconds (JVM running for 11.171)
```

### Verificar que la aplicación está escuchando

Lo primero es añadir una nueva instancia, ya que en la otra tenemos levantada la consola del microservicio.  Para hacer la petición, vamos a utilizar el comando curl (the *ip-other-instance* es la IP de la otra instancia, en mi caso *192.168.0.23*). Este comando nos permite hacer peticiones HTTP desde la terminal:

```sh
curl -X GET http://<ip-other-instance>:8080/demo/hello\?name\=Jos
```

```sh
[node2] (local) root@192.168.0.22 ~
$ curl -X GET http://192.168.0.23:8080/demo/hello\?name\=Jos
curl: (7) Failed to connect to 192.168.0.23 port 8080: Connection refused
[node2] (local) root@192.168.0.22 ~
$ curl -X GET http://192.168.0.23:8080/demo/hello\?name\=Jos
Hello, Jos[node2] (local) root@192.168.0.22 ~
```

Si todo va bien, veremos por la salida:

```sh
Hello, Jos
```

## Resumen
Hemos clonado una aplicación Spring Boot y la hemos dockerizado. Luego, hemos sido capaces de probar que nuestra aplicación está funcionando. Todo sin escribir una linea de código ;)

El siguiente paso será conectar está aplicación al frontend en Angular y a la capa de datos en PostgreSQL, todo ello en contenedores docker.



< [Lab 02](../lab-02/Readme.md) | [Lab 03 - Frontend - Haciendo el despliegue de un HelloWorld con Docker y Nginx ](../../lab-03)>

<p align="center">
    <img src="../../resources/header.png">
</p>

