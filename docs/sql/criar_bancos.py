"""Cria usuario e bancos do petshop no PostgreSQL local."""

from __future__ import annotations

import sqlite3
import subprocess
import sys

BANCOS = (
    "petshop2026_dev",
    "petshop2026_test",
    "petshop2026_diff",
    "petshop2026_reference",
)
ROLE = "petshop_app"
SENHA = "petshop123"
PGADMIN_DB = r"C:\Users\user\AppData\Roaming\pgAdmin\pgadmin4.db"


def instalar_psycopg() -> None:
    try:
        import psycopg  # noqa: F401
    except ImportError:
        subprocess.check_call([sys.executable, "-m", "pip", "install", "psycopg[binary]"])


def candidatos() -> list[dict[str, object]]:
    lista: list[dict[str, object]] = [
        {"host": "localhost", "port": 5432, "user": "cybhor", "password": "cybhor", "dbname": "cybhor_risk"},
        {"host": "localhost", "port": 5432, "user": "postgres", "password": "postgres", "dbname": "postgres"},
        {"host": "localhost", "port": 5432, "user": "postgres", "password": "admin", "dbname": "postgres"},
        {"host": "localhost", "port": 5432, "user": "postgres", "password": "123", "dbname": "postgres"},
        {"host": "localhost", "port": 5432, "user": "postgres", "password": "1234", "dbname": "postgres"},
        {"host": "localhost", "port": 5432, "user": "postgres", "password": "root", "dbname": "postgres"},
        {"host": "127.0.0.1", "port": 5432, "user": "postgres", "password": "", "dbname": "postgres"},
    ]
    try:
        conexao = sqlite3.connect(PGADMIN_DB)
        colunas = {
            row[1]
            for row in conexao.execute("PRAGMA table_info(server)")
        }
        campos = ["name", "host", "port", "username"]
        if "maintenance_db" in colunas:
            campos.append("maintenance_db")
        rows = conexao.execute(
            f"SELECT {', '.join(campos)} FROM server"
        ).fetchall()
        conexao.close()
        for row in rows:
            dados = dict(zip(campos, row))
            lista.insert(
                0,
                {
                    "host": dados.get("host") or "localhost",
                    "port": int(dados.get("port") or 5432),
                    "user": dados.get("username") or "postgres",
                    "password": "postgres",
                    "dbname": dados.get("maintenance_db") or "postgres",
                },
            )
            print(f"pgAdmin servidor: {dados}")
    except Exception as erro:
        print(f"Nao li o pgAdmin: {erro}")
    return lista


def conectar(psycopg, candidatos_conexao):
    ultimo_erro = None
    for dados in candidatos_conexao:
        try:
            conexao = psycopg.connect(
                host=str(dados["host"]),
                port=int(dados["port"]),
                user=str(dados["user"]),
                password=str(dados["password"]),
                dbname=str(dados["dbname"]),
                connect_timeout=5,
            )
            conexao.autocommit = True
            print(f"Conectado como {dados['user']} em {dados['host']}:{dados['port']}/{dados['dbname']}")
            return conexao
        except Exception as erro:
            ultimo_erro = erro
    raise RuntimeError(f"Nao conectou no PostgreSQL: {ultimo_erro}")


def executar(conexao) -> None:
    with conexao.cursor() as cursor:
        cursor.execute("SELECT 1 FROM pg_roles WHERE rolname = %s", (ROLE,))
        if cursor.fetchone() is None:
            cursor.execute(
                f"CREATE ROLE {ROLE} WITH LOGIN PASSWORD %s",
                (SENHA,),
            )
            print(f"Role {ROLE} criada")
        else:
            cursor.execute(f"ALTER ROLE {ROLE} WITH LOGIN PASSWORD %s", (SENHA,))
            print(f"Role {ROLE} ja existia; senha atualizada")

        cursor.execute("SELECT datname FROM pg_database")
        existentes = {row[0] for row in cursor.fetchall()}
        for banco in BANCOS:
            if banco in existentes:
                print(f"Banco {banco} ja existe")
                continue
            cursor.execute(f'CREATE DATABASE {banco} WITH OWNER {ROLE}')
            print(f"Banco {banco} criado")

        cursor.execute("SELECT rolname FROM pg_roles WHERE rolname = %s", (ROLE,))
        print("usuario:", cursor.fetchone())
        cursor.execute(
            "SELECT datname FROM pg_database WHERE datname = ANY(%s)",
            (list(BANCOS),),
        )
        print("bancos:", [row[0] for row in cursor.fetchall()])


def main() -> int:
    instalar_psycopg()
    import psycopg

    conexao = conectar(psycopg, candidatos())
    try:
        executar(conexao)
    finally:
        conexao.close()
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
