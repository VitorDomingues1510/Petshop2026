package com.curso.petshop.cliente;

import com.curso.petshop.comum.RecursoDuplicadoException;
import com.curso.petshop.comum.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        if (repository.existsByCpf(cliente.getCpf())) {
            throw new RecursoDuplicadoException("CPF já cadastrado");
        }
        return repository.save(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return repository.findAll();
    }

    @Transactional
    public Cliente alterar(Long id, Cliente dados) {
        Cliente cliente = buscarPorId(id);
        if (repository.existsByCpfAndIdNot(dados.getCpf(), id)) {
            throw new RecursoDuplicadoException("CPF já cadastrado");
        }
        cliente.alterarDados(
                dados.getNome(),
                dados.getCpf(),
                dados.getEmail(),
                dados.getTelefone());
        return cliente;
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);
        repository.delete(cliente);
        repository.flush();
    }
}
