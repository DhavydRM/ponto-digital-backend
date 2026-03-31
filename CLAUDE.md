# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run

```bash
mvn clean install          # Build the project
mvn spring-boot:run       # Run the application
mvn test                  # Run tests
```

## Environment Configuration

The application uses `java-dotenv` to load environment variables from a `.env` file at the project root. Database credentials are loaded in `LoginApplication.main()` and set as system properties before the application starts.

Required `.env` variables:
```
DB_URL=jdbc:postgresql://localhost:5432/seu_banco
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

## Architecture

**Layered structure**: Controllers → Services → Repositories → Entities

- `controllers/` - REST endpoints, HTTP request/response handling
- `servico/` - Business logic
- `repositorios/` - JPA repositories (Spring Data)
- `entidades/` - JPA entities and enums
- `dto/` - Data transfer objects for API responses
- `auth/` - Authentication (Token class)

**Entities**:
- `Usuario` - User with roles (ADMIN/EMPLOYEE), one-to-many with Registro
- `Registro` - Time clock record with entrada/saida timestamps

**Key design note**: The `Token` class (auth) uses a static repository pattern rather than standard Spring dependency injection. This is non-typical and requires the `setRepository()` method to be called via `@Autowired` on the setter.

## API Endpoints

**Users** (`/usuarios`): CRUD + `POST /usuarios/auth` for login
**Records** (`/registros`): CRUD + `POST /entrada/{usuarioId}` and `POST /saida/{usuarioId}` for clock in/out

## Docker

```bash
docker build -t app .
docker run -p 8080:8080 app
```
