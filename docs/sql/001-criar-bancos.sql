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
