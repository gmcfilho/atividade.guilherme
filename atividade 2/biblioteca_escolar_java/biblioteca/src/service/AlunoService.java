package service;

import exception.DadosInvalidosException;
import model.Aluno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AlunoService {

    private final List<Aluno> alunos = new ArrayList<>();
    private int proximoId = 1;

    public Aluno cadastrar(String nome, String turma, String matricula)
            throws DadosInvalidosException {

        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("O nome do aluno não pode ser vazio.");
        }

        String nomeNorm      = nome.trim();
        String turmaNorm     = (turma     == null || turma.isBlank())     ? "—" : turma.trim();
        String matriculaNorm = (matricula == null || matricula.isBlank()) ? "—" : matricula.trim();

        Aluno aluno = new Aluno(proximoId++, nomeNorm, turmaNorm, matriculaNorm);
        alunos.add(aluno);
        return aluno;
    }

    public List<Aluno> listarTodos() {
        return Collections.unmodifiableList(alunos);
    }

    public Optional<Aluno> buscarPorId(int id) {
        return alunos.stream().filter(a -> a.getId() == id).findFirst();
    }

    public Optional<Aluno> buscarPorNome(String nome) {
        return alunos.stream()
                     .filter(a -> a.getNome().equalsIgnoreCase(nome.trim()))
                     .findFirst();
    }

    public int totalCadastrados() {
        return alunos.size();
    }
}
