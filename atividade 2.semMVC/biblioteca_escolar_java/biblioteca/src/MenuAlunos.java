import java.util.List;

public class MenuAlunos {

    private final AlunoService alunoService;
    private final EmprestimoService emprestimoService;

    public MenuAlunos(AlunoService alunoService, EmprestimoService emprestimoService) {
        this.alunoService = alunoService;
        this.emprestimoService = emprestimoService;
    }

    public void exibir() {
        while (true) {
            Console.titulo("Gerenciamento de Alunos");
            System.out.println("  1. Cadastrar novo aluno");
            System.out.println("  2. Listar todos os alunos");
            System.out.println("  3. Ver histórico de empréstimos de um aluno");
            System.out.println("  4. Buscar aluno por ID");
            System.out.println("  0. Voltar ao menu principal");

            int opcao = Console.lerOpcaoMenu(0, 4);

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listarTodos();
                case 3 -> historicoAluno();
                case 4 -> buscarPorId();
                case 0 -> { return; }
            }
        }
    }

    private void cadastrar() {
        Console.subtitulo("Cadastrar Novo Aluno");
        try {
            String nome       = Console.lerTexto("  Nome completo *: ");
            String turma      = Console.lerTextoOpcional("  Turma");
            String matricula  = Console.lerTextoOpcional("  Matrícula");

            Aluno aluno = alunoService.cadastrar(nome, turma, matricula);
            Console.sucesso("Aluno cadastrado com sucesso!");
            System.out.println("  " + aluno);

        } catch (DadosInvalidosException e) {
            Console.erro(e.getMessage());
        }
        Console.pausar();
    }

    private void listarTodos() {
        Console.subtitulo("Alunos Cadastrados");
        List<Aluno> lista = alunoService.listarTodos();
        if (lista.isEmpty()) {
            Console.aviso("Nenhum aluno cadastrado.");
        } else {
            for (Aluno a : lista) {
                int abertos = emprestimoService.listarAbertosPorAluno(a.getId()).size();
                String situacao = abertos > 0
                        ? " | ⚠️  " + abertos + " livro(s) em aberto"
                        : " | ✅ Sem pendências";
                System.out.println("  " + a + situacao);
            }
            Console.linha();
            Console.info("Total: " + lista.size() + " aluno(s).");
        }
        Console.pausar();
    }

    private void historicoAluno() {
        Console.subtitulo("Histórico de Empréstimos");
        int id = Console.lerInteiro("  ID do aluno: ");

        alunoService.buscarPorId(id).ifPresentOrElse(aluno -> {
            System.out.println("\n  Aluno: " + aluno.getNome() + " — " + aluno.getTurma());
            Console.linha();
            List<Emprestimo> historico = emprestimoService.listarPorAluno(id);
            if (historico.isEmpty()) {
                Console.aviso("Nenhum empréstimo registrado para este aluno.");
            } else {
                historico.forEach(e -> System.out.println("  " + e));
            }
        }, () -> Console.aviso("Aluno de ID " + id + " não encontrado."));

        Console.pausar();
    }

    private void buscarPorId() {
        Console.subtitulo("Buscar Aluno por ID");
        int id = Console.lerInteiro("  ID do aluno: ");
        alunoService.buscarPorId(id).ifPresentOrElse(
                a -> System.out.println("\n  " + a),
                () -> Console.aviso("Aluno de ID " + id + " não encontrado.")
        );
        Console.pausar();
    }
}
