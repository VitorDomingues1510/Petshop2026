package com.curso.petshop.venda;

import com.curso.petshop.cliente.Cliente;
import com.curso.petshop.cliente.ClienteRepository;
import com.curso.petshop.comum.RecursoNaoEncontradoException;
import com.curso.petshop.produto.Produto;
import com.curso.petshop.produto.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Venda abrir(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado"));
        return vendaRepository.save(new Venda(cliente, LocalDate.now()));
    }

    @Transactional(readOnly = true)
    public Venda buscarPorId(Long id) {
        return vendaRepository.buscarPorIdComItens(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Venda não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Venda> listar() {
        return vendaRepository.buscarTodasComItens();
    }

    @Transactional
    public Venda adicionarItem(Long id, Long produtoId, BigDecimal quantidade) {
        Venda venda = buscarPorId(id);
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto não encontrado"));
        venda.adicionarItem(produto, quantidade);
        return vendaRepository.saveAndFlush(venda);
    }

    @Transactional
    public Venda removerItem(Long id, Long itemId) {
        Venda venda = buscarPorId(id);
        venda.removerItem(itemId);
        return vendaRepository.saveAndFlush(venda);
    }

    @Transactional
    public Venda finalizar(Long id) {
        Venda venda = buscarPorId(id);
        venda.finalizar();
        return venda;
    }

    @Transactional
    public Venda cancelar(Long id) {
        Venda venda = buscarPorId(id);
        venda.cancelar();
        return venda;
    }

    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarPorId(id);
        venda.validarExclusao();
        vendaRepository.delete(venda);
        vendaRepository.flush();
    }
}
