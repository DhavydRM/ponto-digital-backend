# 📌 Assinatura de Ponto Digital API

Esta API é o backend robusto para um sistema de assinatura de ponto digital, gerenciando todo o ciclo de vida (CRUD) de usuários e o registro de entradas e saídas.  
Ela oferece funcionalidades completas para marcar pontos, buscar registros de forma flexível e manter os dados de usuários atualizados.

**Status do Projeto:** 🚧 Em construção – atualmente estou focado em sua finalização.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem de Programação:** Java 21  
- **Framework:** Spring Boot  
- **Banco de Dados:** MySQL  

---

## 🛠️ Instalação e Execução

### ✅ Pré-requisitos

Antes de começar, certifique-se de ter os seguintes softwares instalados:

- JDK 21  
- Apache Maven  
- MySQL Server  
- Git  

### 📥 Instalação

Clone este repositório:

```bash
git clone https://github.com/DhavydRM/assinatura-de-ponto-digital.git
```
Acesse a pasta do projeto:

```bash
cd assinatura-de-ponto-digital
```
Instale as dependências:

```bash
mvn clean install
```
---
⚙️ Configuração do Banco de Dados

Edite o arquivo src/main/resources/application.properties (ou application.yml) e configure as credenciais do banco MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_seu_banco_de_dados?serverTimezone=America/Sao_Paulo&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=seu_usuario_mysql
spring.datasource.password=sua_senha_mysql
spring.jpa.hibernate.ddl.auto=update
spring.jpa.show-sql=true
```

Substitua ```nome_do_seu_banco_de_dados```, ```seu_usuario_mysql``` e ```sua_senha_mysql``` pelas suas credenciais.

---

▶️ Executando a Aplicação

Via Maven:

```bash
mvn spring-boot:run
```

Ou via IDE (IntelliJ IDEA, Eclipse, etc.):
Basta executar a classe principal com ```@SpringBootApplication```.

A API estará disponível em:
👉 http://localhost:8080

---

📋 Endpoints da API

A API oferece endpoints para **Usuários** e **Registros de Ponto.**

| Método | URL              | Descrição                   | Exemplo de Body                                                               | Exemplo de Resposta                                                                     |
| ------ | ---------------- | --------------------------- | ----------------------------------------------------------------------------- | --------------------------------------------------------------------------------------- |
| GET    | `/usuarios`      | Lista todos os usuários.    | N/A                                                                           | `[{"id":1,"nome":"João","email":"joao@exemplo.com","senha":"senha"}]`                   |
| GET    | `/usuarios/{id}` | Busca usuário pelo ID.      | N/A                                                                           | `{"id":1,"nome":"João","email":"joao@exemplo.com","senha":"senha"}`                     |
| POST   | `/usuarios`      | Cadastra novo usuário.      | `{"nome":"Novo Usuário","email":"novo@exemplo.com","senha":"senha123"}`       | `{"id":2,"nome":"Novo Usuário","email":"novo@exemplo.com","senha":"senha"}`             |
| PUT    | `/usuarios/{id}` | Atualiza usuário existente. | `{"nome":"Atualizado","email":"atualizado@exemplo.com","senha":"nova_senha"}` | `{"id":1,"nome":"Atualizado","email":"atualizado@exemplo.com","senha":"senha"}`         |
| DELETE | `/usuarios/{id}` | Remove usuário pelo ID.     | N/A                                                                           | `204 No Content`                                                                        |

---

| Método | URL                                                     | Descrição                                | Exemplo de Body                                                   | Exemplo de Resposta                                                                                 |
| ------ | ------------------------------------------------------- | ---------------------------------------- | ----------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| GET    | `/registros?date=YYYY-MM-DD`                            | Lista registros (pode filtrar por data). | N/A                                                               | `[{"id":101,"usuario":{"id":1,...},"entrada":"2024-01-01T08:00:00","saida":"2024-01-01T17:00:00"}]` |
| GET    | `/registros/{id}`                                       | Busca registro por ID.                   | N/A                                                               | `{"id":101,"usuario":{"id":1,...},"entrada":"2024-01-01T08:00:00","saida":"2024-01-01T17:00:00"}`   |
| GET    | `/registros/usuario/{usuarioId}?carregarRegistros=true` | Lista registros de um usuário.           | N/A                                                               | `[{"id":102,"usuario":{"id":2,...},"entrada":"2024-01-02T09:00:00","saida":null}]`                  |
| POST   | `/registros/entrada/{usuarioId}`                        | Marca entrada.                           | N/A                                                               | `{"id":103,"usuario":{"id":3,...},"entrada":"2024-01-03T07:30:00","saida":null}`                    |
| POST   | `/registros/saida/{usuarioId}`                          | Marca saída.                             | N/A                                                               | `{"id":104,"usuario":{"id":4,...},"entrada":"2024-01-04T08:00:00","saida":"2024-01-04T18:00:00"}`   |
| PUT    | `/registros/{id}`                                       | Atualiza registro existente.             | `{"entrada":"2024-01-01T08:05:00","saida":"2024-01-01T17:55:00"}` | `{"id":101,"usuario":{"id":1,...},"entrada":"2024-01-01T08:05:00","saida":"2024-01-01T17:55:00"}`   |
| DELETE | `/registros/{id}`                                       | Remove registro pelo ID.                 | N/A                                                               | `204 No Content`                                                                                    |

---

**📬 Contato**

Se tiver dúvidas ou sugestões, sinta-se à vontade para entrar em contato:

**👤 Dhavyd Romano**

[🔗Meu LinkedIn](https://www.linkedin.com/in/dhavyd-romano-002b55347/)
