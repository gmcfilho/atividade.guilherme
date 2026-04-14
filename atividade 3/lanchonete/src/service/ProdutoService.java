package service;

import model.Produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProdutoService {

    private List<Produto> produtos = new ArrayList<>();

    public Produto cadastrar(String nome, String descricao, double preco) {
        Produto p = new Produto(nome, descricao, preco);
        produtos.add(p);
        return p;
    }

    public Optional<Produto> buscarPorId(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Produto> listarTodos() {
        return Collections.unmodifiableList(produtos);
    }

    public boolean remover(int id) {
        return produtos.removeIf(p -> p.getId() == id);
    }
}
