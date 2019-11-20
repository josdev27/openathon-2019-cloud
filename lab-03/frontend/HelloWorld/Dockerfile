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
# el desplegable que hemos generado antes 
RUN rm -rf /usr/share/nginx/html/*

# Finalmente copiamos nuestro entregable desde la imagen de node a la ruta de despliegue
# en la imagen de Nginx 
COPY --from=build /usr/angular-workdir/dist/HelloWorld /usr/share/nginx/html
