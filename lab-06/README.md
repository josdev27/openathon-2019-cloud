<p align="center">
    <img src="../../resources/header.png">
</p>

# Lab 06 - Escalado y orquestración de microservicios (Reto para los valientes)
<br/>
<p align="center">
<img src="./resources/logoAngular.png" width="500">
<br/>
¡Reto! 
</p>
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

### Paso 1. Configurando el cluster

Con esta configuración, el swarm estara formado por 3 nodos managers y 3 nodos wrokers:

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
	
### Paso 2. Inicilizar el swarm

Para inicializar el swarm, se usa el siguiente comando:

    docker swarm init --advertise-addr 192.168.66.100

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

### Paso 3. Manejar el swarm	

Una vez esta inicializado el swarm, podemos pararlo con el siguiente comando:	

    docker-machine stop docker-swarm-manager-1 docker-swarm-manager-2 docker-swarm-manager-3 docker-swarm-worker-1 docker-swarm-worker-2 docker-swarm-worker-3

Y para arrancarlo de nuevo, usaremos este comando:

    docker-machine start docker-swarm-manager-1 docker-swarm-manager-2 docker-swarm-manager-3 docker-swarm-worker-1 docker-swarm-worker-2 docker-swarm-worker-3

<br/>

## Reto! 




[< Lab 5 ](../lab-05) | [ Lab 7 -  >](../lab-07)

<p align="center">
    <img src="../../resources/header.png">
</p>

