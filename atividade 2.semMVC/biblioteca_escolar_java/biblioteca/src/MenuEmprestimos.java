import java.util.List;
import java.util.Optional;

public class MenuEmprestimos {

    private final EmprestimoService emprestimoService;
    private final AlunoService alunoService;
    private final LivroService livroService;

    public MenuEmprestimos(EmprestimoService emprestimoService,
                           AlunoService alunoService,
                           LivroService livroService) {
        this.emprestimoService = emprestimoService;
        this.alunoService = alunoService;
        this.livroService = livroService;
    }

    public void exibir() {
        while (true) {
            Console.titulo("Empréstimos e Devoluções");
            System.out.println("  1. Registrar empréstimo");
            System.out.println("  2. Registrar devolução");
            System.out.println("  3. Listar todos os empréstimos");
            System.out.println("  4. Listar empréstimos em aberto");
            System.out.println("  0. Voltar ao menu principal");

            int opcao = Console.lerOpcaoMenu(0, 4);

            switch (opcao) {
                case 1 -> registrarEmprestimo();
                case 2 -> registrarDevolucao();
                case 3 -> listarTodos();
                case 4 -> listarEmAberto();
                case 0 -> { return; }
            }
        }
    }

    private void registrarEmprestimo() {
        Console.subtitulo("Registrar Empréstimo");

        // Selecionar aluno
        int alunoId = Console.lerInteiro("  ID do aluno: ");
        Optional<Aluno> optAluno = alunoService.buscarPorId(alunoId);
        if (optAluno.isEmpty()) {
            Console.erro("Aluno de ID " + alunoId + " não encontrado.");
            Console.pausar();
            return;
        }
        Aluno aluno = optAluno.get();
        System.out.println("  Aluno selecionado: " + aluno.getNome() + " (" + aluno.getTurma() + ")");

        int livroId = Console.lerInteiro("  ID do livro: ");
        Optional<Livro> optLivro = livroService.buscarPorId(livroId);
        if (optLivro.isEmpty()) {
            Console.erro("Livro de ID " + livroId + " não encontrado.");
            Console.pausar();
            return;
        }
        Livro livro = optLivro.get();
        System.out.println("  Livro selecionado: " + livro.getTitulo()
                + " (disponíveis: " + livro.getQuantidadeDisponivel() + ")");

        try {
            Emprestimo emp = emprestimoService.registrarEmprestimo(aluno, livro);
            Console.sucesso("Empréstimo registrado com sucesso!");
            System.out.println("  " + emp);

        } catch (LivroIndisponivelException | IllegalStateException e) {
            Console.erro(e.getMessage());
        }
        Console.pausar();
    }

    private void registrarDevolucao() {
        Console.subtitulo("Registrar Devolução");

        List<Emprestimo> abertos = emprestimoService.listarEmAberto();
        if (abertos.isEmpty()) {
            Console.aviso("Não há empréstimos em aberto no momento.");
            Console.pausar();
            return;
        }

        System.out.println("  Empréstimos em aberto:");
        abertos.forEach(e -> System.out.println("  " + e));
        Console.linha();

        int id = Console.lerInteiro("  ID do empréstimo a devolver: ");
        try {
            Emprestimo emp = emprestimoService.registrarDevolucao(id);
            Console.sucesso("Devolução registrada com sucesso!");
            System.out.println("  Livro: " + emp.getLivro().getTitulo());
            System.out.println("  Aluno: " + emp.getAluno().getNome());
            Console.info("Exemplares disponíveis agora: " + emp.getLivro().getQuantidadeDisponivel());

        } catch (IllegalArgumentException | IllegalStateException e) {
            Console.erro(e.getMessage());
        }
        Console.pausar();
    }

    private void listarTodos() {
        Console.subtitulo("Todos os Empréstimos");
        List<Emprestimo> lista = emprestimoService.listarTodos();
        if (lista.isEmpty()) {
            Console.aviso("Nenhum empréstimo registrado.");
        } else {
            lista.forEach(e -> System.out.println("  " + e));
            Console.linha();
            Console.info("Total: " + lista.size()
                    + " | Em aberto: " + emprestimoService.totalEmAberto()
                    + " | Devolvidos: " + emprestimoService.totalDevolvidos());
        }
        Console.pausar();
    }

    private void listarEmAberto() {
        Console.subtitulo("Empréstimos em Aberto");
        List<Emprestimo> lista = emprestimoService.listarEmAberto();
        if (lista.isEmpty()) {
            Console.sucesso("Nenhum empréstimo em aberto. Tudo devolvido!");
        } else {
            lista.forEach(e -> System.out.println("  " + e));
            Console.linha();
            Console.info("Total em aberto: " + lista.size());
        }
        Console.pausar();
    }
}
