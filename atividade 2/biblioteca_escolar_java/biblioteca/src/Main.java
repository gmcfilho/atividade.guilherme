import service.AlunoService;
import service.EmprestimoService;
import service.LivroService;
import ui.Console;
import ui.MenuAlunos;
import ui.MenuConsultas;
import ui.MenuEmprestimos;
import ui.MenuLivros;

public class Main {

    public static void main(String[] args) {

        LivroService livroService         = new LivroService();
        AlunoService alunoService         = new AlunoService();
        EmprestimoService empService      = new EmprestimoService();

        MenuLivros      menuLivros      = new MenuLivros(livroService);
        MenuAlunos      menuAlunos      = new MenuAlunos(alunoService, empService);
        MenuEmprestimos menuEmprestimos = new MenuEmprestimos(empService, alunoService, livroService);
        MenuConsultas   menuConsultas   = new MenuConsultas(empService, alunoService, livroService);

        carregarDadosExemplo(livroService, alunoService, empService);

        while (true) {
            Console.titulo("Sistema de Biblioteca Escolar");
            System.out.printf("  📚 Livros: %-5d  👥 Alunos: %-5d  📋 Em aberto: %d%n%n",
                    livroService.totalCadastrados(),
                    alunoService.totalCadastrados(),
                    empService.totalEmAberto());

            System.out.println("  1. Livros");
            System.out.println("  2. Alunos");
            System.out.println("  3. Empréstimos e Devoluções");
            System.out.println("  4. Consultas e Relatórios");
            System.out.println("  0. Sair");

            int opcao = Console.lerOpcaoMenu(0, 4);

            switch (opcao) {
                case 1 -> menuLivros.exibir();
                case 2 -> menuAlunos.exibir();
                case 3 -> menuEmprestimos.exibir();
                case 4 -> menuConsultas.exibir();
                case 0 -> {
                    System.out.println("\n  Até logo! 📚\n");
                    System.exit(0);
                }
            }
        }
    }

    private static void carregarDadosExemplo(LivroService livros,
                                             AlunoService alunos,
                                             EmprestimoService emprestimos) {
        try {
            var l1 = livros.cadastrar("O Pequeno Príncipe",          "Antoine de Saint-Exupéry", "Literatura",          3);
            var l2 = livros.cadastrar("Iracema",                      "José de Alencar",          "Clássico Brasileiro", 2);
            var l3 = livros.cadastrar("Harry Potter e a Pedra Filosofal", "J.K. Rowling",         "Fantasia",            4);
            var l4 = livros.cadastrar("Dom Casmurro",                 "Machado de Assis",         "Romance",             2);

            var a1 = alunos.cadastrar("Ana Beatriz Santos",    "8º A", "2024001");
            var a2 = alunos.cadastrar("Carlos Eduardo Lima",   "7º B", "2024002");
            var a3 = alunos.cadastrar("Fernanda Oliveira",     "9º A", "2024003");

            emprestimos.registrarEmprestimo(a1, l1);
            emprestimos.registrarEmprestimo(a2, l3);

        } catch (Exception e) {
            System.err.println("Erro ao carregar dados de exemplo: " + e.getMessage());
        }
    }
}
