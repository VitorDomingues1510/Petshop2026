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

- `GET /api/health`
- `http://localhost:8080/api/categorias-produtos`
- `http://localhost:8080/api/fornecedores`
- `http://localhost:8080/api/produtos`

Os contratos usam DTOs, validação de entrada e respostas de erro padronizadas.

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
