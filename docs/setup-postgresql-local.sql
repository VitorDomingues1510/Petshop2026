-- Execute no Query Tool do pgAdmin ou no psql, conectado como superusuário postgres.
-- Use este script se quiser os bancos no PostgreSQL 18 instalado no Windows (porta 5432),
-- em vez do container Docker (porta 5433).
-- Troque a senha abaixo pela mesma definida no arquivo .env.

CREATE ROLE petshop_app
    WITH LOGIN
    PASSWORD 'defina-uma-senha-local';

CREATE DATABASE petshop_dev
    WITH OWNER petshop_app;

CREATE DATABASE petshop_test
    WITH OWNER petshop_app;

CREATE DATABASE petshop_diff
    WITH OWNER petshop_app;

CREATE DATABASE petshop_reference
    WITH OWNER petshop_app;
