<p align="center">
    <img src="../resources/header.png">
</p>

# Lab 06 - Escalado y orquestración de microservicios (Reto para los valientes)
<br/>
Llegamos al siguiente nivel... ¡Reto! 
<br/>

## Objetivos y resultados

El objetivo de este laboratorio es montar un cluster con las aplicaciones que se han creado en los laboratorios anteriores, escalarlas y orquestrarlas. 

Ya que hemos aprendido cómo crear contenedores, necesitamos alguna herramienta o plataforma para orquestrar, escalar y planificar esos contenedores, ya que podremos tener algunos que estén corriendo todo el día, otros que sean bajo demanda, otros que tengan mucha disponibilidad...

Para realizar esto, existen varias herramientas: kubernetes, docker swarm...En este laboratorio nos centraremos en Docker Swarm.

Docker Swarm es la solución nativa propia de Docker para clusters de contenedores Docker que tiene la ventaja de estar estrechamente integrada en el ecosistema de Docker y utiliza su propia API. Supervisa la cantidad de contenedores distribuidos en clústeres de servidores y es la forma más conveniente de crear una aplicación acoplada en clúster sin hardware adicional. Proporciona un sistema de orquestación a pequeña escala pero útil para las aplicaciones Dockerizadas.

Pero, **¿qué es un cluster?**

Un cluster es un conjunto de máquinas que pueden trabajar juntas.

Un cluster swarm consta de varios hosts Docker que se ejecutan en modo swarm y actúan como **managers** (para administrar la membresía y la delegación) y **workers** (que ejecutan servicios swarm). Un host Docker determinado puede ser un manager, un worker o desempeñar ambos roles. Cuando creamos un servicio, definimos su estado óptimo (número de réplicas, recursos de red y almacenamiento disponibles, puertos que el servicio expone al mundo exterior y demás). Docker trabaja para mantener ese estado deseado. Por ejemplo, si un nodo de trabajo deja de estar disponible, Docker programa las tareas de ese nodo en otros nodos. Una tarea es un contenedor en ejecución que forma parte de un servicio swarm y es administrado por un swarm manager, en lugar de un contenedor independiente.

Una de las ventajas clave de los servicios swarm sobre los contenedores independientes es que se puede modificar la configuración de un servicio, incluidas las redes y los volúmenes a los que está conectado, sin la necesidad de reiniciarlo manualmente. Docker actualizará la configuración, detendrá las tareas de servicio con la configuración desactualizada y creará nuevas que coincidan con la configuración deseada.


Docker Swarm proporciona alta disponibilidad de nodos managers utilizando un algoritmo Raft, que necesita un número impar de nodos registrados como manager (3, 5, 7 y así sucesivamente). Este algoritmo selecciona un líder entre los distintos nodos manager.
<br/>

## Crear un cluster swarm

> Los siguientes pasos están pensados para hacerlos utilizando el demonio de Docker, instalando en tu sistema operativo de preferencia. En cada paso se darán pistas de cómo poder reproducirlo dentro del entorno Play with Docker 

### Paso 1. Configurando el cluster

Con esta configuración, el swarm estara formado por 3 nodos managers y 3 nodos workers:

    docker-swarm-manager-1
    docker-swarm-manager-2
    docker-swarm-manager-3
    docker-swarm-worker-1
    docker-swarm-worker-2
    docker-swarm-worker-3

Estas máquinas se desplegarán en su propia red:

    192.168.66.1/24

Siendo la primera ip del pool DHCP:

    192.168.66.100

Para crear cada docker machine en VirtualBox, ejecuta el siguiente comando poniendo el nombre correcto  cada vez:	

    docker-machine create --driver virtualbox --virtualbox-cpu-count 1 --virtualbox-memory 1024 --virtualbox-hostonly-cidr "192.168.66.1/24" <docker-machine-name>

Antes de configuar el swarm, debemos hace que el entorno apunte a la primera docker machine.

Si estamos en windows, ejecutar este comando:

    @FOR /f "tokens=*" %i IN ('docker-machine env docker-swarm-manager-1') DO @%i

Si estamos en linux, ejecutar este otro comando:

    eval $(docker-machine env docker-swarm-manager-1)

En Play with Docker sólo podemos utilizar cinco instancias, por lo que no podremos tener 3 nodos managers y 3 workers. Como el algoritmo Raft necesita un número impar de nodos, tendremos 3 managers y 2 workers. Abriremos cinco instancias diferenciadas utilizando el botón "Add new instance" en la izquierda de la interfaz.
	
### Paso 2. Inicilizar el swarm

Para inicializar el swarm, se usa el siguiente comando:

    docker swarm init --advertise-addr <IP de la instancia o de la primera ip del pool DHCP> 

En windows/linux utilizaríamos la IP 192.168.66.100 de ejemplo. En Play With Docker, usaremos la IP de la instancia donde ejecutemos el comando, podemos ver la IP de cada instancia en la parte izquierda de la pantalla.

Una vez inicializado, el swarm expone dos tokens: Uno para añadir nodos manager y otro para añadir nodos workers
El comando para obtener los tokens es:

    docker swarm join-token manager -q
    docker swarm join-token worker -q

Una vez tengamos los tokens, cambia el entorno para que apunte a cada docker machine, cada nodo manager y cada nodo worker.
	
Si estamos en windows, ejecutar este comando:

    @FOR /f "tokens=*" %i IN ('docker-machine env <docker-machine-name>') DO @%i

Si estamos en linux, ejecutar este otro comando:

    eval $(docker-machine env <docker-machine-name>)

Usamos el comando swarm join en cada nodo ya sea manager o worker:

    docker swarm join --token <manager-or-worker-token> 192.168.66.100:2377

En Play with Docker, tendremos que ir instancia a instancia ejecutando el siguiente comando (como la instancia que inicializó el swarm es un manager, el join tendrá que ejecutarse en dos instancias utilizando el token de manager, y en otras dos utilizando el token de worker):

    docker swarm join --token <manager-or-worker-token> <IP de la instancia dónde se inicializó  el swarm>:2377

Se puede obtener este comando resolviendo ya los token y la IP si ejecutamos el comando `docker swarm join-token manager` (para managers) o `docker swarm join-token worker` (para workers) en la instancia que inició el swarm. Copiando la salida del primer comando en dos instancias y la salida del segundo en otras dos, ya tendremos el swarm con los cinco nodos añadidos.

Ejecutando el comando

    docker node ls

veremos si se ha creado el swarm correctamente, con todos sus nodos asociados, y podremos reconocer el rol de cada nodo dentro del swarm por el contenido de la columna MANAGER STATUS.

<br/>

## Paso 3. Escalar los servicios ##

Para escalar los servicios en el swarm, desde cualquiera de los nodos manager, se puede ejecutar el siguiente comando:

	docker service scale <SERVICE-ID>=<NUMBER-OF-TASKS>

<br/>

### Paso 4. Manejar el swarm	

Una vez esta inicializado el swarm, podemos pararlo con el siguiente comando:	

    docker-machine stop docker-swarm-manager-1 docker-swarm-manager-2 docker-swarm-manager-3 docker-swarm-worker-1 docker-swarm-worker-2 docker-swarm-worker-3

Y para arrancarlo de nuevo, usaremos este comando:

    docker-machine start docker-swarm-manager-1 docker-swarm-manager-2 docker-swarm-manager-3 docker-swarm-worker-1 docker-swarm-worker-2 docker-swarm-worker-3
<br/>

En Play with Docker no tenemos instalada la funcionalidad `docker-machine`, por lo que no podemos parar y arrancar el swarm. Para abandonar el swarm, podemos utilizar el siguiente comando nodo a nodo en todos ellos:

    docker swarm leave --force

## ¡Reto! 

El reto consiste en crear un cluster con docker swarm y orquestrar y escalar los servicios que se han creado en los laboratorios 03 y 04.



[< Lab 05 - Creando un stack de servicios con docker-compose](../lab-05) | [Lab 07 - Dockerización de la aplicación Event-UI >](../lab-07)

<p align="center">
    <img src="../resources/header.png">
</p>

