# JSON para o Postman

Arquivos da Aula 07, com dados do petshop. A coleção importável está em `docs/postman/Petshop-Aula07.postman_collection.json`.

## Como usar os arquivos avulsos

1. Inicie a API com o profile `dev`.
2. No Postman, crie a variável `baseUrl` = `http://localhost:8080`.
3. Em cada `POST`, escolha **Body > raw > JSON** e cole o conteúdo do arquivo correspondente.

| Arquivo | Método | URL | Status esperado |
|---|---|---|---|
| `01-cadastrar-grupo.json` | `POST` | `{{baseUrl}}/api/grupos-produtos` | `201` |
| `02-cadastrar-fornecedor.json` | `POST` | `{{baseUrl}}/api/fornecedores` | `201` |
| `03-cadastrar-produto.json` | `POST` | `{{baseUrl}}/api/produtos` | `201` |
| `04-produto-invalido.json` | `POST` | `{{baseUrl}}/api/produtos` | `400` |

No `03-cadastrar-produto.json`, troque `grupoId` e `fornecedorId` pelos IDs devolvidos nos cadastros anteriores. Na coleção importável isso é feito automaticamente pelas variáveis `grupoId` e `fornecedorId`.

Consultas sem corpo:

- `GET {{baseUrl}}/api/health`
- `GET {{baseUrl}}/api/produtos/{{produtoId}}`
- `GET {{baseUrl}}/api/produtos`
- `GET {{baseUrl}}/api/produtos/999999999` (espera `404`)
