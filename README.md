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

# Link documentacion (Swagger Api)
http://localhost:8080/micro-customer/webjars/swagger-ui/index.html#/
