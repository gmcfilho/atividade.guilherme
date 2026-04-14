import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EmprestimoService {

    private final List<Emprestimo> emprestimos = new ArrayList<>();
    private int proximoId = 1;

    public Emprestimo registrarEmprestimo(Aluno aluno, Livro livro)
            throws LivroIndisponivelException {

        if (!livro.isDisponivel()) {
            throw new LivroIndisponivelException(livro.getTitulo());
        }

        boolean duplicado = emprestimos.stream().anyMatch(e ->
                !e.isDevolvido()
                && e.getAluno().getId() == aluno.getId()
                && e.getLivro().getId() == livro.getId());

        if (duplicado) {
            throw new IllegalStateException(
                    "O aluno \"" + aluno.getNome() + "\" já está com este livro em aberto.");
        }

        livro.emprestar();
        Emprestimo emp = new Emprestimo(proximoId++, aluno, livro, LocalDate.now());
        emprestimos.add(emp);
        return emp;
    }

    public Emprestimo registrarDevolucao(int idEmprestimo) {
        Emprestimo emp = buscarPorId(idEmprestimo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Empréstimo de ID " + idEmprestimo + " não encontrado."));

        emp.registrarDevolucao();
        return emp;
    }

    public List<Emprestimo> listarTodos() {
        return Collections.unmodifiableList(emprestimos);
    }

    public List<Emprestimo> listarEmAberto() {
        return emprestimos.stream()
                          .filter(e -> !e.isDevolvido())
                          .toList();
    }

    public List<Emprestimo> listarDevolvidos() {
        return emprestimos.stream()
                          .filter(Emprestimo::isDevolvido)
                          .toList();
    }

    public List<Emprestimo> listarPorAluno(int alunoId) {
        return emprestimos.stream()
                          .filter(e -> e.getAluno().getId() == alunoId)
                          .toList();
    }

    public List<Emprestimo> listarAbertosPorAluno(int alunoId) {
        return emprestimos.stream()
                          .filter(e -> !e.isDevolvido() && e.getAluno().getId() == alunoId)
                          .toList();
    }

    public List<Emprestimo> listarAbertosPorLivro(int livroId) {
        return emprestimos.stream()
                          .filter(e -> !e.isDevolvido() && e.getLivro().getId() == livroId)
                          .toList();
    }

    public Optional<Emprestimo> buscarPorId(int id) {
        return emprestimos.stream().filter(e -> e.getId() == id).findFirst();
    }

    public int totalEmAberto()    { return (int) emprestimos.stream().filter(e -> !e.isDevolvido()).count(); }
    public int totalDevolvidos()  { return (int) emprestimos.stream().filter(Emprestimo::isDevolvido).count(); }
}
