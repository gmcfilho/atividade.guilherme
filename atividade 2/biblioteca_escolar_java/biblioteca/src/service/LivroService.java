package service;

import exception.DadosInvalidosException;
import model.Livro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LivroService {

    private final List<Livro> livros = new ArrayList<>();
    private int proximoId = 1;

    public Livro cadastrar(String titulo, String autor, String genero, int quantidade)
            throws DadosInvalidosException {

        if (titulo == null || titulo.isBlank()) {
            throw new DadosInvalidosException("O título do livro não pode ser vazio.");
        }
        if (quantidade < 1) {
            throw new DadosInvalidosException(
                    "A quantidade deve ser maior que zero. Informado: " + quantidade);
        }

        String tituloNorm = titulo.trim();
        String autorNorm  = (autor  == null || autor.isBlank())  ? "Desconhecido" : autor.trim();
        String generoNorm = (genero == null || genero.isBlank()) ? "—"            : genero.trim();

        Livro livro = new Livro(proximoId++, tituloNorm, autorNorm, generoNorm, quantidade);
        livros.add(livro);
        return livro;
    }

    public List<Livro> listarTodos() {
        return Collections.unmodifiableList(livros);
    }

    public List<Livro> listarDisponiveis() {
        return livros.stream()
                     .filter(Livro::isDisponivel)
                     .toList();
    }

    public Optional<Livro> buscarPorId(int id) {
        return livros.stream().filter(l -> l.getId() == id).findFirst();
    }

    public Optional<Livro> buscarPorTitulo(String titulo) {
        return livros.stream()
                     .filter(l -> l.getTitulo().equalsIgnoreCase(titulo.trim()))
                     .findFirst();
    }

    public int totalCadastrados() {
        return livros.size();
    }
}
