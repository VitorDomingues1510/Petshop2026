# PostgreSQL com Docker e pgAdmin

O pgAdmin desktop **não pede senha ao abrir**. Isso é normal. A senha só é pedida (ou gravada) quando o **servidor PostgreSQL** é cadastrado.

Sem esse cadastro, o menu de criar database fica indisponível. Neste projeto o banco é criado pelo Docker; o pgAdmin só serve para visualizar e executar consultas.

A porta **5433** é usada de propósito: a 5432 costuma estar ocupada por outro PostgreSQL na mesma máquina.

## Credenciais locais

| Campo | Valor |
|---|---|
| Host | `localhost` |
| Porta | `5433` |
| Usuário | `petshop_app` |
| Senha | `petshop123` |
| Banco de manutenção | `petshop2026_dev` |

Bancos criados na primeira subida do container:

- `petshop2026_dev`
- `petshop2026_test`
- `petshop2026_diff`
- `petshop2026_reference`

O arquivo `.env` da raiz já aponta para essa porta e senha. Não versionar `.env` no Git.

## 1. Subir o PostgreSQL

Com o Docker Desktop aberto, na pasta do projeto:

```powershell
docker compose up -d
```

Para conferir:

```powershell
docker compose ps
```

O serviço `postgres` deve aparecer como `running` / `healthy`.

Para parar:

```powershell
docker compose down
```

`docker compose down` não apaga os dados. Para zerar o volume (apaga os bancos):

```powershell
docker compose down -v
```

## 2. Cadastrar o servidor no pgAdmin

1. Clique com o botão direito em **Servers**.
2. Escolha **Register → Server**.
3. Aba **General**, Name: `Petshop`.
4. Aba **Connection**, preencha:

| Campo | Valor |
|---|---|
| Host name/address | `localhost` |
| Port | `5433` |
| Maintenance database | `petshop2026_dev` |
| Username | `petshop_app` |
| Password | `petshop123` |
| Save password | marcado |

5. Clique em **Save**.

Se a conexão falhar:

- o Docker Desktop está fechado;
- o container ainda não ficou healthy;
- a porta foi preenchida como `5432` em vez de `5433`.

## 3. Conferir os bancos

No pgAdmin, abra **Servers → Petshop → Databases** e use **Refresh**. Devem aparecer os quatro bancos.

Não é necessário criar database pelo menu **Create**. O `docker-compose.yml` e o script `docs/sql/docker-init.sql` já fazem isso.

## 4. SQL avulso (só se o Docker não for usado)

O arquivo `docs/sql/001-criar-bancos.sql` é o caminho da Aula 04 quando o PostgreSQL já existe e o usuário tem permissão de `CREATE DATABASE`. Conecte no banco `postgres` (ou no de manutenção do superusuário) e execute o script no **Query Tool**.

## Relação com a Aula 04

No material do curso, o Docker Desktop é pré-requisito da persistência. Os comandos SQL de `CREATE ROLE` e `CREATE DATABASE` continuam válidos; neste projeto eles foram automatizados no container para evitar cadastro manual quando o pgAdmin ainda não tem servidor.
