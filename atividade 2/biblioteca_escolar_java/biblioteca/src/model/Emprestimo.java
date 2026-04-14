package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Emprestimo {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int id;
    private Aluno aluno;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;   // null enquanto não devolvido
    private boolean devolvido;

    public Emprestimo(int id, Aluno aluno, Livro livro, LocalDate dataEmprestimo) {
        this.id = id;
        this.aluno = aluno;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.devolvido = false;
    }

    public int getId()                  { return id; }
    public Aluno getAluno()             { return aluno; }
    public Livro getLivro()             { return livro; }
    public LocalDate getDataEmprestimo(){ return dataEmprestimo; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public boolean isDevolvido()        { return devolvido; }

    public void registrarDevolucao() {
        if (devolvido) {
            throw new IllegalStateException("Este empréstimo já foi encerrado.");
        }
        this.devolvido = true;
        this.dataDevolucao = LocalDate.now();
        livro.devolver();
    }

    private String situacao() {
        return devolvido
                ? "Devolvido em " + dataDevolucao.format(FMT)
                : "EM ABERTO";
    }

    @Override
    public String toString() {
        return String.format(
                "[%d] Aluno: %-25s | Livro: %-35s | Saída: %s | %s",
                id,
                aluno.getNome() + " (" + aluno.getTurma() + ")",
                livro.getTitulo(),
                dataEmprestimo.format(FMT),
                situacao());
    }
}
