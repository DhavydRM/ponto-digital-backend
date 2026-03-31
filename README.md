# Assinatura de Ponto Digital API

API REST para um sistema de registro de ponto digital, permitindo o controle de entrada e saída de funcionários.

---

## Tecnologias

- **Java 21**
- **Spring Boot 3.x**
- **PostgreSQL**
- **Maven**

---

## Como Executar

### Pré-requisitos

- JDK 21
- Maven
- PostgreSQL

### Configuração

Crie um arquivo `.env` na raiz do projeto com as variáveis de ambiente:

```env
DB_URL=jdbc:postgresql://localhost:5432/seu_banco
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

### Executando

```bash
mvn clean install
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## Endpoints

### Usuários

| Método | Endpoint              | Descrição                |
| ------ | --------------------- | ------------------------ |
| GET    | `/usuarios`           | Listar todos os usuários |
| GET    | `/usuarios/{id}`      | Buscar usuário por ID    |
| POST   | `/usuarios`           | Criar novo usuário       |
| PUT    | `/usuarios/{id}`      | Atualizar usuário       |
| DELETE | `/usuarios/{id}`      | Remover usuário         |
| POST   | `/usuarios/auth`      | Autenticar usuário      |

### Registros de Ponto

| Método | Endpoint                            | Descrição                          |
| ------ | ----------------------------------- | ---------------------------------- |
| GET    | `/registros`                        | Listar registros (com filtros)     |
| GET    | `/registros/{id}`                   | Buscar registro por ID             |
| GET    | `/registros/usuario/{usuarioId}`    | Listar registros de um usuário    |
| POST   | `/registros/entrada/{usuarioId}`   | Marcar entrada                     |
| POST   | `/registros/saida/{usuarioId}`     | Marcar saída                       |
| PUT    | `/registros/{id}`                   | Atualizar registro                 |
| DELETE | `/registros/{id}`                   | Remover registro                   |

### Filtros para GET /registros

- `dataInicial` - Data inicial (formato: YYYY-MM-DD)
- `dataFinal` - Data final (formato: YYYY-MM-DD)
- `allUsers` - Buscar de todos os usuários (true/false)

---

## Autor

[Dhavyd Romano](https://www.linkedin.com/in/dhavyd-romano-002b55347/)
