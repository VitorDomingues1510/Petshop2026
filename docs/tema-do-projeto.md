# Tema do projeto

## Identificação

- Nome: `petshop`
- Tema: controle simplificado de itens de estoque de um pet shop
- Objetivo: cadastrar produtos de pet shop e organizá-los por categorias

## Entidade de classificação

- Nome no singular: `CategoriaProduto`
- Nome no plural: categorias de produtos
- Descrição: classificação utilizada para organizar produtos do pet shop
- Exemplos: Ração, Higiene, Brinquedos e Farmácia
- Status: ativo ou inativo

## Entidade principal

- Nome no singular: `Produto`
- Nome no plural: produtos
- Código único: código de barras
- Descrição: nome ou descrição comercial do produto
- Medida quantitativa: saldo em estoque
- Valor monetário: valor unitário
- Valor calculado: saldo em estoque multiplicado pelo valor unitário
- Data relevante: data de cadastro
- Status: ativo ou inativo

## Relacionamentos

- Uma categoria de produto pode classificar vários produtos.
- Cada produto pertence a uma categoria de produto.
- Um fornecedor pode fornecer vários produtos.
- O fornecedor de um produto é opcional.

```text
CategoriaProduto 1 ─────── N Produto
Fornecedor       1 ─────── N Produto
```

## Exemplos

| Categoria | Código | Produto | Saldo | Valor unitário |
|---|---|---|---:|---:|
| Ração | `7890000000001` | Ração Premium Cães 15kg | 20 | 189,90 |
| Higiene | `7890000000002` | Shampoo Neutro 500ml | 15 | 32,50 |
| Brinquedos | `7890000000003` | Bolinha de borracha | 40 | 12,90 |

## Correspondência com o projeto de referência

```text
GrupoProduto -> CategoriaProduto
Produto      -> Produto
Fornecedor   -> Fornecedor
```
