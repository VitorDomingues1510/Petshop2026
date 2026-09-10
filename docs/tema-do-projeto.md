# Tema do projeto

## Identificação

- Nome: `petshop2026`
- Tema: controle simplificado de estoque de petshop
- Objetivo: cadastrar produtos para cães e gatos e organizá-los por grupos
- Referência das aulas: [jeffersonarpasserini/suporteos2026](https://github.com/jeffersonarpasserini/suporteos2026)

## Entidade de classificação

- Nome no singular: `GrupoProduto`
- Nome no plural: grupos de produtos
- Descrição: classificação utilizada para organizar os itens vendidos no petshop
- Exemplos: Ração, Higiene, Brinquedos e Acessórios
- Status: ativo ou inativo

## Entidade principal

- Nome no singular: `Produto`
- Nome no plural: produtos
- Código único: código de barras
- Descrição: nome comercial do produto
- Medida quantitativa: saldo em estoque
- Valor monetário: valor unitário
- Valor calculado: saldo em estoque multiplicado pelo valor unitário
- Data relevante: data de cadastro
- Status: ativo ou inativo

## Entidade complementar (Aula 06)

- Nome no singular: `Fornecedor`
- Identificador único: CNPJ com 14 dígitos
- Relacionamento: um fornecedor pode atender vários produtos; o vínculo no produto é opcional

## Relacionamento

```text
GrupoProduto 1 ─────── N Produto
Fornecedor   1 ─────── N Produto
```

- Um grupo pode classificar vários produtos.
- Cada produto pertence a um grupo.
- O fornecedor de um produto é opcional.

## Exemplos

| Grupo | Código | Produto | Saldo | Valor unitário |
|---|---|---|---:|---:|
| Ração | `7891234560001` | Ração Golden Adulto 15kg | 20 | 189,90 |
| Higiene | `7891234560002` | Shampoo Neutro 500ml | 15 | 32,50 |
| Brinquedos | `7891234560003` | Bolinha de borracha | 40 | 12,90 |

## Banco local

PostgreSQL sobe pelo Docker na porta `5433`. Cadastro no pgAdmin e credenciais: [banco-postgresql.md](banco-postgresql.md).

## Correspondência com o projeto de referência

```text
GrupoProduto -> entidade de classificação do tema (grupos do petshop)
Produto      -> entidade principal do tema (itens à venda)
Fornecedor   -> fabricante ou distribuidora
```
