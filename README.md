# PetShop 2026

API didática da disciplina de Programação, adaptada do projeto de referência [suporteos2026](https://github.com/jeffersonarpasserini/suporteos2026) para o tema **Pet Shop**.

## Domínio

- Categoria de produto: classificação dos itens (ração, higiene, brinquedos, farmácia).
- Produto: item identificado por código de barras, com saldo e valor unitário.
- Fornecedor: empresa opcional associada ao produto.

Detalhes em [docs/tema-do-projeto.md](docs/tema-do-projeto.md).

## Requisitos

- Java 21
- Git
- Docker Desktop (PostgreSQL)
- Maven Wrapper (`mvnw` / `mvnw.cmd`)

## Banco de dados

O projeto usa **PostgreSQL** (driver `org.postgresql`, sem H2), como na aula.

Há duas opções:

1. **Container Docker (já configurado):** `localhost:5433`, usuário `petshop_app`, bancos `petshop_dev` e `petshop_test`.

```powershell
docker compose up -d
```

2. **PostgreSQL 18 instalado no Windows (porta 5432):** execute [docs/setup-postgresql-local.sql](docs/setup-postgresql-local.sql) no pgAdmin e aponte as URLs do `.env` para a porta `5432`.

Copie `.env.example` para `.env` e coloque a senha só nesse arquivo local (não vai para o Git).

## Executando o projeto

No Windows:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Com a aplicação iniciada, acesse <http://localhost:8080/api/health>. A resposta esperada é `OK`.

## API

Cada cadastro tem as operações cadastrar (`POST`), listar (`GET`), buscar por ID (`GET /{id}`), alterar (`PUT /{id}`) e excluir (`DELETE /{id}`).

- `GET /api/health`
- `/api/categorias-produtos`
- `/api/fornecedores`
- `/api/produtos`

Categorias e produtos também têm alterar status (`PUT /{id}/status`). Produtos têm entrada (`POST /{id}/estoque/entrada`) e saída de estoque (`POST /{id}/estoque/saida`); uma saída maior que o saldo retorna `400`.

Os contratos usam DTOs, validação de entrada e respostas de erro padronizadas. A exclusão de um registro que ainda está em uso retorna `409` com a mensagem do motivo, por exemplo "Não é possível excluir: a categoria possui produtos vinculados".

A coleção do Postman está em [docs/postman/PetShop.postman_collection.json](docs/postman/PetShop.postman_collection.json). Ela tem uma pasta para cada cadastro. Execute as pastas em ordem, com o banco vazio. Cada pasta cadastra um registro próprio para testar a exclusão, e as requisições marcadas como "(bloqueado)" mostram exclusões recusadas porque o registro está em uso.

## Organização do código

Cada parte do sistema fica em uma pasta própria dentro de `src/main/java/com/curso/petshop`:

- `categoria`: categorias de produtos
- `fornecedor`: fornecedores
- `produto`: produtos
- `comum`: tratamento de erros, status e health check

Cada pasta tem a entidade, o repository, o service, o controller, os DTOs de entrada e saída e o mapper.

## Testes

Os testes de persistência acessam `petshop_test` e leem `DB_TEST_PASSWORD` do `.env`.

```powershell
.\mvnw.cmd test
```

## Modelo de domínio

```text
CategoriaProduto 1 ─────── N Produto
Fornecedor       1 ─────── N Produto
```

O Liquibase cria e versiona o esquema; o Hibernate apenas o valida. Os profiles `dev`, `test` e `prod` usam PostgreSQL, sem H2.
