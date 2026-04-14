import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuConsultas {

    private final EmprestimoService emprestimoService;
    private final AlunoService alunoService;
    private final LivroService livroService;

    public MenuConsultas(EmprestimoService emprestimoService,
                         AlunoService alunoService,
                         LivroService livroService) {
        this.emprestimoService = emprestimoService;
        this.alunoService = alunoService;
        this.livroService = livroService;
    }

    public void exibir() {
        while (true) {
            Console.titulo("Consultas e Relatórios");
            System.out.println("  1. Livros atualmente emprestados");
            System.out.println("  2. Alunos com livros em aberto");
            System.out.println("  3. Painel geral (resumo)");
            System.out.println("  0. Voltar ao menu principal");

            int opcao = Console.lerOpcaoMenu(0, 3);

            switch (opcao) {
                case 1 -> livrosEmprestados();
                case 2 -> alunosComAbertos();
                case 3 -> painelGeral();
                case 0 -> { return; }
            }
        }
    }


    private void livrosEmprestados() {
        Console.subtitulo("Livros Atualmente Emprestados");

        List<Emprestimo> abertos = emprestimoService.listarEmAberto();
        if (abertos.isEmpty()) {
            Console.sucesso("Nenhum livro emprestado no momento.");
            Console.pausar();
            return;
        }

        Map<String, Long> porLivro = abertos.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getLivro().getTitulo(),
                        Collectors.counting()
                ));

        System.out.printf("  %-45s  %s%n", "Título", "Exemplares fora");
        Console.linha();
        porLivro.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry ->
                        System.out.printf("  %-45s  %d%n", entry.getKey(), entry.getValue()));

        Console.linha();
        Console.info("Total de empréstimos em aberto: " + abertos.size());
        Console.pausar();
    }

    private void alunosComAbertos() {
        Console.subtitulo("Alunos com Livros em Aberto");

        List<Emprestimo> abertos = emprestimoService.listarEmAberto();
        if (abertos.isEmpty()) {
            Console.sucesso("Nenhum aluno com empréstimo em aberto.");
            Console.pausar();
            return;
        }

        Map<Aluno, List<Emprestimo>> porAluno = abertos.stream()
                .collect(Collectors.groupingBy(Emprestimo::getAluno));

        for (Map.Entry<Aluno, List<Emprestimo>> entry : porAluno.entrySet()) {
            Aluno a = entry.getKey();
            List<Emprestimo> emps = entry.getValue();
            System.out.printf("%n  👤 %s — %s (%d livro(s)):%n",
                    a.getNome(), a.getTurma(), emps.size());
            emps.forEach(e ->
                    System.out.println("     📖 " + e.getLivro().getTitulo()));
        }
        Console.pausar();
    }

    private void painelGeral() {
        Console.subtitulo("Painel Geral da Biblioteca");

        List<Livro> livros   = livroService.listarTodos();
        List<Aluno> alunos   = alunoService.listarTodos();

        int totalLivros      = livros.size();
        int totalExemplares  = livros.stream().mapToInt(Livro::getQuantidadeTotal).sum();
        int disponiveis      = livros.stream().mapToInt(Livro::getQuantidadeDisponivel).sum();
        int emprestados      = totalExemplares - disponiveis;
        int semEstoque       = (int) livros.stream().filter(l -> !l.isDisponivel()).count();

        System.out.println();
        System.out.printf("   Títulos no acervo      : %d%n", totalLivros);
        System.out.printf("  Total de exemplares     : %d%n", totalExemplares);
        System.out.printf("   Exemplares disponíveis  : %d%n", disponiveis);
        System.out.printf("   Exemplares emprestados  : %d%n", emprestados);
        System.out.printf("   Títulos sem estoque     : %d%n", semEstoque);
        Console.linha();
        System.out.printf("   Alunos cadastrados      : %d%n", alunos.size());
        System.out.printf("   Empréstimos em aberto   : %d%n", emprestimoService.totalEmAberto());
        System.out.printf("   Total de devoluções     : %d%n", emprestimoService.totalDevolvidos());

        Console.pausar();
    }
}
