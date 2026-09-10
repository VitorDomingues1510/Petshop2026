# Coleção Postman — Aula 07

Importe `Petshop-Aula07.postman_collection.json` no Postman:

1. Abra o Postman.
2. **Import > File** e selecione este JSON.
3. Confira a variável `baseUrl` = `http://localhost:8080`.
4. Inicie a API com o profile `dev`.
5. Na coleção, clique em **Run collection**.

A ordem importa: os cadastros gravam `grupoId`, `fornecedorId` e `produtoId` para as consultas seguintes.

Se executar de novo no mesmo banco, altere o nome do grupo, o CNPJ e o código de barras, porque esses valores são únicos.
