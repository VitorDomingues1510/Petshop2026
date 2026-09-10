-- Alternativa da Aula 04, se o Docker deste projeto nao for usado.
-- Caminho preferido: docker compose up -d (veja docs/banco-postgresql.md).
-- Se for executar aqui: Query Tool no banco postgres, com permissao de CREATE DATABASE.

CREATE ROLE petshop_app
    WITH LOGIN
    PASSWORD 'petshop123';

CREATE DATABASE petshop2026_dev
    WITH OWNER petshop_app;

CREATE DATABASE petshop2026_test
    WITH OWNER petshop_app;

CREATE DATABASE petshop2026_diff
    WITH OWNER petshop_app;

CREATE DATABASE petshop2026_reference
    WITH OWNER petshop_app;

SELECT rolname FROM pg_roles WHERE rolname = 'petshop_app';

SELECT datname FROM pg_database
WHERE datname IN (
    'petshop2026_dev',
    'petshop2026_test',
    'petshop2026_diff',
    'petshop2026_reference'
);
