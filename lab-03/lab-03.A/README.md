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

<br/>

## Prerequisites

Before start to coding, we need install some tools:

1. [**Java 8** or mayor](#how-to-install-java-8)

2. [**Maven 3**]()

3. [**Eclipse Java Enterprise**]()
   1. [**Spring Tools 4 - for Spring Boot**]()

<br/>

### How to install Java 8

You can use Oracle JDK, in that case, you have register in Oracle, or use OpenJDK, the open version of JDK (that we will use).


#### Check if we already have Java

We can check if we have installed java with the next command:

```sh
java -version
```

If you see a message similar to *version "1.x"*, where *x* is mayor or equal to 8, skip this section and go to [Install maven 3](#how-to-install-maven-3).

If we see a message similar to *Command not found*, we need install java, so go to [Install Java 8](#install-java-8).


<p align="center">
    <img src="./resources/java-version-out.png">
    In my case, I use OpenJDK 1.8.0_222
</p>

#### Install Java 8

#### Windows

1. Download OpenJDK 8 from next link: https://adoptopenjdk.net/
2. Extract the zip in the folder you want.
3. Set the value for **JAVA_HOME**. For that, open cmd and execute next command:

    ```cmd
    setx /m JAVA_HOME "<full_path_your_folder>"
    ```

4. Execute *java -version* to verify the installation.

### Mac

1. Download OpenJDK 8 from next link: https://adoptopenjdk.net/
2. Extract the zip in the folder you want.
3. Set the value for **JAVA_HOME**. For that, open terminal and execute next command:

    ```sh
     export JAVA_HOME="<full_path_your_folder>"
    ```

4. Execute *java -version* to verify the installation.

### Linux

1. Open a terminal.
2. Execute the next command:

   ```sh
    sudo apt-get install openjdk-8-jdk
   ```

3. Execute *java -version* to verify the installation.

<br/>

### How to install maven 3

You can check if you have already maven 3 installed

```sh
mvn --version
```

<p align="center">
    <img src="./resources/mvn-version-out.png">
    In my case, I have Maven 3.6.1
</p>


### How to install Eclipse Enterprise

#### Install Eclipse

#### Install Spring Boot Plugin


[< Lab 03 - Creando un HelloWorld ](../../lab-03)>

<p align="center">
    <img src="../../resources/header.png">
</p>

