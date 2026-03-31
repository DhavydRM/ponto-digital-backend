# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run

```bash
mvn clean install          # Build the project (skip tests)
mvn spring-boot:run       # Run the application
mvn test                  # Run tests
```

## Environment Configuration

The application uses `java-dotenv` to load environment variables from a `.env` file at the project root. Database credentials are loaded in `LoginApplication.main()` before Spring Boot starts.

Required `.env` variables:
```
DB_URL=jdbc:postgresql://localhost:5432/seu_banco
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

## Architecture

**Layered structure**: Controllers → Services → Repositories → Entities

- `controllers/` - REST endpoints (HTTP request/response)
- `servico/` - Business logic
- `repositorios/` - Spring Data JPA repositories
- `entidades/` - JPA entities and enums
- `dto/` - Data transfer objects
- `config/` - Configuration (CORS)
- `auth/` - Authentication (Token class)

**Entities**:
- `Usuario` - User entity with roles (ADMIN/EMPLOYEE), one-to-many with Registro
- `Registro` - Time clock record (entrada/saida timestamps), many-to-one with Usuario

**Enums**:
- `Roles` - ADMIN, EMPLOYEE
- `Turnos` - MANHA, TARDE, NOITE
- `StatusRegistro` - ABERTO, FECHADO, UNDEFINED

**Key design notes**:
- `Token` class uses a static repository pattern (non-typical Spring DI) - setter annotated with `@Autowired`
- `LoginApplication.main()` sets DB credentials as system properties from `.env` before Spring starts
- `application.properties` uses `${DB_URL}`, `${DB_USER}`, `${DB_PASSWORD}` placeholders

## API Endpoints

### Users (`/usuarios`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/usuarios` | List all users |
| GET | `/usuarios/{id}` | Get user by ID |
| POST | `/usuarios` | Create user |
| PUT | `/usuarios/{id}` | Update user |
| DELETE | `/usuarios/{id}` | Delete user |
| POST | `/usuarios/auth` | Authenticate (returns Token with idUsuario and funcao) |

### Records (`/registros`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/registros?dataInicial=&dataFinal=&allUsers=` | List records with date filters |
| GET | `/registros/{id}` | Get record by ID |
| GET | `/registros/usuario/{usuarioId}?carregarRegistrosToday=&dataInicial=&dataFinal=` | Get user's records |
| POST | `/registros/entrada/{usuarioId}` | Clock in (max 2 per day) |
| POST | `/registros/saida/{usuarioId}` | Clock out (sets saida on last open record) |
| PUT | `/registros/{id}` | Update record |
| DELETE | `/registros/{id}` | Delete record |

## Docker

```bash
docker build -t app .
docker run -p 8080:8080 app
```
