<p align="center">
    <img src="../../resources/header.png">
</p>

# Lab 03.A - Hello World in Spring Boot

<br/>

<p align="center">
<img src="./resources/spring-boot-logo.png" width="500">
<br/>
Welcome Spring!
</p>
<br/>

## Goals and results
The goal of this laboratory is create a microservice in Java using [Spring Boot](https://spring.io/projects/spring-boot). Finally, we will have an API with a endpoint **Get /hello?name={name}**  

## Prerrequisites

The only prerrequisite is having an account in Docker :)

## Set up your first Spring Boot application

### Introduction

The microservice has a controller with one endpoint:

```sh
GET /demo/hello?name={name}
```

This endpoint receive a request param “name” and return the message *Hello, {name}* with 200 OK as Http Code. 
I mean, the request would be:

```sh
GET /demo/hello?name=Docker
```

And the response:

```sh
Hello, Docker
```

### Clone the repository

You have to clone the project from [spring_boot_app](https://github.com/josdev27/spring_boot_app). Add a new instance in [play-with-docker](https://labs.play-with-docker.com/) and execute:

```sh
git clone https://github.com/josdev27/spring_boot_app.git
```

In this moment, you have a new folder **spring_boot_app**. Change on it:

```sh
cd spring_boot_app
```

<p align="center">
    <img src="./resources/git_clone.png">
</p>


### Build and execute the application

In this time, we will run the application to verify it. In the same instance, execute:

```sh
docker run -p 8080:8080 -it --rm --name spring_boot_app -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.3-jdk-8 mvn spring-boot:run
```

It has to download an image, so the process could be slow (Take easy! ;))

You would have to see the next log message in terminal:

```sh
INFO 67 --- [           main] com.josdev27.sample.DemoApplication      : Started DemoApplication in 1.915 seconds (JVM running for 2.452)
```

<p align="center">
    <img src="./resources/execute_ms.png">
</p>

### Check if application is listening

Add a new instance and execute the next (the *ip-other-instance* in my case was *192.168.0.48*):

```sh
curl -X GET http://<ip-other-instance>:8080/demo/hello\?name\=Jos
```

<p align="center">
    <img src="./resources/result.png">
</p>

If everything is OK, the stdout is:

```sh
Hello, Jos
```


[< Lab 03 - Creando un HelloWorld ](../../lab-03)>

<p align="center">
    <img src="../../resources/header.png">
</p>

