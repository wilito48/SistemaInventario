# Sistema de Gestion de Inventario

Sistema integral para el control de inventarios desarrollado con Java (Spring Boot) para el backend y Angular 21 para el frontend.

## Requisitos Previos

* Java 17 o superior
* Node.js v18 o superior
* MySQL Server

## Configuracion de la Base de Datos

1. Crear la base de datos en MySQL:
   ```sql
   CREATE DATABASE sistema_inventario;
   ```
2. La estructura de tablas se genera automaticamente al iniciar el backend gracias a Flyway. No es necesario importar ningun archivo .sql manualmente.

## Ejecucion del Backend

1. Navegar a la carpeta: `Backend/inventario-system`
2. Configurar el acceso a la base de datos en `src/main/resources/application.yaml`.
3. Iniciar la aplicacion:
   ```bash
   mvn spring-boot:run
   ```

## Ejecucion del Frontend

1. Navegar a la carpeta: `Frontend/inventario-frontend`
2. Instalar dependencias:
   ```bash
   npm install
   ```
3. Iniciar el servidor de desarrollo:
   ```bash
   npm start
   ```
4. Acceder a: http://localhost:4200

## Credenciales de Acceso Inicial

El sistema crea automaticamente los siguientes usuarios al arrancar:

* Administrador:
  - Usuario: admin
  - Password: admin123
* Almacenero:
  - Usuario: almacenero
  - Password: almacen123

## Funcionalidades Principales

* Autenticacion basada en JWT.
* Gestion de categorias, productos y movimientos.
* Dashboard con resumen de stock.
* Control de usuarios y roles (ADMIN / ALMACENERO).
* Interfaz responsiva para dispositivos moviles.
