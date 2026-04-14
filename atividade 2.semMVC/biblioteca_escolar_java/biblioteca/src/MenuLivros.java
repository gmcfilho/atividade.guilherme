import java.util.List;

/**
 * Menu interativo para gerenciamento do acervo de livros.
 */
public class MenuLivros {

    private final LivroService livroService;

    public MenuLivros(LivroService livroService) {
        this.livroService = livroService;
    }

    public void exibir() {
        while (true) {
            Console.titulo("Gerenciamento de Livros");
            System.out.println("  1. Cadastrar novo livro");
            System.out.println("  2. Listar todos os livros");
            System.out.println("  3. Listar livros disponíveis");
            System.out.println("  4. Buscar livro por ID");
            System.out.println("  0. Voltar ao menu principal");

            int opcao = Console.lerOpcaoMenu(0, 4);

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listarTodos();
                case 3 -> listarDisponiveis();
                case 4 -> buscarPorId();
                case 0 -> { return; }
            }
        }
    }

    // ── Ações ──────────────────────────────────────────────────────────────────

    private void cadastrar() {
        Console.subtitulo("Cadastrar Novo Livro");
        try {
            String titulo   = Console.lerTexto("  Título *: ");
            String autor    = Console.lerTextoOpcional("  Autor");
            String genero   = Console.lerTextoOpcional("  Gênero");
            int quantidade  = Console.lerInteiro("  Quantidade disponível *: ");

            Livro livro = livroService.cadastrar(titulo, autor, genero, quantidade);
            Console.sucesso("Livro cadastrado com sucesso!");
            System.out.println("  " + livro);

        } catch (DadosInvalidosException e) {
            Console.erro(e.getMessage());
        }
        Console.pausar();
    }

    private void listarTodos() {
        Console.subtitulo("Acervo Completo");
        List<Livro> lista = livroService.listarTodos();
        if (lista.isEmpty()) {
            Console.aviso("Nenhum livro cadastrado.");
        } else {
            lista.forEach(l -> System.out.println("  " + l));
            Console.linha();
            Console.info("Total: " + lista.size() + " livro(s).");
        }
        Console.pausar();
    }

    private void listarDisponiveis() {
        Console.subtitulo("Livros Disponíveis para Empréstimo");
        List<Livro> lista = livroService.listarDisponiveis();
        if (lista.isEmpty()) {
            Console.aviso("Nenhum livro disponível no momento.");
        } else {
            lista.forEach(l -> System.out.println("  " + l));
            Console.linha();
            Console.info("Total disponível: " + lista.size() + " livro(s).");
        }
        Console.pausar();
    }

    private void buscarPorId() {
        Console.subtitulo("Buscar Livro por ID");
        int id = Console.lerInteiro("  ID do livro: ");
        livroService.buscarPorId(id).ifPresentOrElse(
                l -> System.out.println("\n  " + l),
                () -> Console.aviso("Livro de ID " + id + " não encontrado.")
        );
        Console.pausar();
    }
}
