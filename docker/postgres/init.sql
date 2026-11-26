CREATE ROLE petshop_app
    WITH LOGIN
    PASSWORD 'petshop_local';

CREATE DATABASE petshop_dev
    WITH OWNER petshop_app;

CREATE DATABASE petshop_test
    WITH OWNER petshop_app;

CREATE DATABASE petshop_diff
    WITH OWNER petshop_app;

CREATE DATABASE petshop_reference
    WITH OWNER petshop_app;
