# Guia para despliegue

Este repositorio es un monorepo con 2 microservicios

- `microservice-banking` (port 8081, WebFlux base path `/micro-banking`)
- `microservice-customer` (port 8080, WebFlux base path `/micro-customer`)

Both services provide a `Dockerfile` in their respective folders and the orchestration is managed with `docker-compose.yml` at repository root.

Prerequisites

- Docker (engine) installed
- Docker Compose (v2) or `docker compose` command

Quick start (build and run everything)

```bash
# from the repository root (where docker-compose.yml and BaseDatos.sql live)
docker compose up --build
```

# Guía de despliegue

Este repositorio es un monorepo que contiene 2 microservicios:

- `microservice-banking` (puerto 8081, base path de WebFlux `/micro-banking`)
- `microservice-customer` (puerto 8080, base path de WebFlux `/micro-customer`)

Ambos servicios incluyen un `Dockerfile` en sus carpetas respectivas y la orquestación se gestiona mediante `docker-compose.yml` en la raíz del repositorio.

Requisitos previos

- Tener instalado Docker (motor)
- Docker Compose (v2) o el comando `docker compose`

Inicio rápido (construir y ejecutar todo)

```bash
# desde la raíz del repositorio (donde están `docker-compose.yml` y `BaseDatos.sql`)
docker compose up --build
```

Esto hará:

- Levantar un contenedor de Postgres e inicializar la base de datos usando `./BaseDatos.sql`.
- Construir y arrancar `microservice-banking` y `microservice-customer` a partir de sus respectivos `Dockerfile`.

Endpoints de los servicios

- Banking: http://localhost:8081/micro-banking
- Customer: http://localhost:8080/micro-customer

Comandos útiles

- Reconstruir y reiniciar:

```bash
docker compose up --build -d
docker compose logs -f
```

- Parar y eliminar contenedores:

```bash
docker compose down
```

- Ver contenedores en ejecución:

```bash
docker ps
```
