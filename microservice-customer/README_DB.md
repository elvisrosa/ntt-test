Creación de la base de datos y esquema (PostgreSQL)

Archivos:
- `sql/create_person_schema.sql` : Script SQL que crea la tabla `person`, índices y trigger `updated_at`.
- `scripts/init_db.sh` : Script bash para crear la base de datos (si no existe) y aplicar el schema. Usa `psql`.
- `scripts/init_db.bat` : Script Windows (cmd) equivalente.

Requisitos:
- PostgreSQL con cliente `psql` instalado y en PATH.

Ejecutar en Linux/macOS:

```bash
chmod +x scripts/init_db.sh
./scripts/init_db.sh [DB_NAME] [DB_USER] [DB_PASS] [DB_HOST] [DB_PORT]
```

Ejecutar en Windows (cmd):

```
cd scripts
init_db.bat [DB_NAME] [DB_USER] [DB_PASS] [DB_HOST] [DB_PORT]
```

Valores por defecto si no se pasan parámetros:
- DB_NAME = microservice_customer
- DB_USER = postgres
- DB_PASS = postgres
- DB_HOST = localhost
- DB_PORT = 5432

Notas:
- El script crea la base de datos si no existe y luego aplica el SQL del schema.
- Si usas Docker, puedes ejecutar los scripts apuntando al host/puerto del contenedor.
- Alternativamente puedes integrar estos SQL en Flyway copiando `sql/create_person_schema.sql` a `src/main/resources/db/migration/V1__create_person.sql`.
