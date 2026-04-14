package ui;

import exception.PedidoVazioException;
import model.Pedido;
import model.Produto;
import service.PedidoService;
import service.ProdutoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private final ProdutoService produtoService = new ProdutoService();
    private final PedidoService pedidoService = new PedidoService();
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void iniciar() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("     🍔  SISTEMA DE LANCHONETE  🍔     ");
        System.out.println("╚══════════════════════════════════════╝");
        carregarDadosExemplo();

        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> menuProdutos();
                case 2 -> menuPedidos();
                case 3 -> menuRelatorios();
                case 0 -> System.out.println("\n👋 Encerrando o sistema. Até logo!\n");
                default -> System.out.println("⚠️  Opção inválida. Tente novamente.\n");
            }
        } while (opcao != 0);
    }

    // ─────────────────────────── MENUS ───────────────────────────

    private void exibirMenuPrincipal() {
        System.out.println("\n════════════  MENU PRINCIPAL  ═══════════");
        System.out.println("  1. Gerenciar Produtos");
        System.out.println("  2. Gerenciar Pedidos");
        System.out.println("  3. Relatórios");
        System.out.println("  0. Sair");
        System.out.println("═════════════════════════════════════════");
    }

    // ─────────────────── PRODUTOS ───────────────────

    private void menuProdutos() {
        int opcao;
        do {
            System.out.println("\n──────────  PRODUTOS  ──────────");
            System.out.println("  1. Cadastrar produto");
            System.out.println("  2. Listar produtos");
            System.out.println("  3. Remover produto");
            System.out.println("  0. Voltar");
            System.out.println("────────────────────────────────");
            opcao = lerInteiro("Escolha: ");
            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> removerProduto();
                case 0 -> {}
                default -> System.out.println("⚠️  Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarProduto() {
        System.out.println("\n── Novo Produto ──");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();
        double preco = lerDouble("Preço (R$): ");

        try {
            Produto p = produtoService.cadastrar(nome, descricao, preco);
            System.out.println("✅ Produto cadastrado com sucesso!");
            System.out.println(p);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void listarProdutos() {
        List<Produto> lista = produtoService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("\n── Produtos Cadastrados ──");
        lista.forEach(System.out::println);
    }

    private void removerProduto() {
        listarProdutos();
        int id = lerInteiro("ID do produto a remover (0 = cancelar): ");
        if (id == 0) return;
        if (produtoService.remover(id)) {
            System.out.println("✅ Produto removido.");
        } else {
            System.out.println("❌ Produto não encontrado.");
        }
    }

    // ─────────────────── PEDIDOS ───────────────────

    private void menuPedidos() {
        int opcao;
        do {
            System.out.println("\n──────────  PEDIDOS  ──────────");
            System.out.println("  1. Novo pedido");
            System.out.println("  2. Adicionar item a pedido aberto");
            System.out.println("  3. Finalizar pedido");
            System.out.println("  4. Consultar pedido por ID");
            System.out.println("  5. Listar todos os pedidos");
            System.out.println("  0. Voltar");
            System.out.println("───────────────────────────────");
            opcao = lerInteiro("Escolha: ");
            switch (opcao) {
                case 1 -> novoPedido();
                case 2 -> adicionarItemAoPedido();
                case 3 -> finalizarPedido();
                case 4 -> consultarPedidoPorId();
                case 5 -> listarTodosPedidos();
                case 0 -> {}
                default -> System.out.println("⚠️  Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void novoPedido() {
        System.out.print("Nome do cliente (deixe em branco para anônimo): ");
        String nome = scanner.nextLine().trim();
        Pedido pedido = pedidoService.abrirPedido(nome);
        System.out.println("✅ Pedido #" + pedido.getId() + " aberto para: " + pedido.getNomeCliente());
        adicionarItensAoPedido(pedido);
    }

    private void adicionarItensAoPedido(Pedido pedido) {
        boolean continuar = true;
        while (continuar) {
            listarProdutos();
            int idProduto = lerInteiro("ID do produto (0 = parar de adicionar): ");
            if (idProduto == 0) break;

            Optional<Produto> optProduto = produtoService.buscarPorId(idProduto);
            if (optProduto.isEmpty()) {
                System.out.println("❌ Produto não encontrado.");
                continue;
            }

            int qtd = lerInteiro("Quantidade: ");
            try {
                pedidoService.adicionarItem(pedido, optProduto.get(), qtd);
                System.out.println("✅ Item adicionado!");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("❌ Erro: " + e.getMessage());
            }

            System.out.println("\n── Pedido atual ──");
            System.out.println(pedido);
        }
    }

    private void adicionarItemAoPedido() {
        int idPedido = lerInteiro("ID do pedido aberto: ");
        Optional<Pedido> optPedido = pedidoService.buscarPorId(idPedido);
        if (optPedido.isEmpty()) { System.out.println("❌ Pedido não encontrado."); return; }
        Pedido pedido = optPedido.get();
        if (pedido.getStatus() == Pedido.Status.FINALIZADO) {
            System.out.println("❌ Este pedido já foi finalizado.");
            return;
        }
        adicionarItensAoPedido(pedido);
    }

    private void finalizarPedido() {
        int id = lerInteiro("ID do pedido a finalizar: ");
        Optional<Pedido> opt = pedidoService.buscarPorId(id);
        if (opt.isEmpty()) { System.out.println("❌ Pedido não encontrado."); return; }
        try {
            pedidoService.finalizarPedido(opt.get());
            System.out.println("✅ Pedido finalizado com sucesso!");
            System.out.println(opt.get());
        } catch (PedidoVazioException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void consultarPedidoPorId() {
        int id = lerInteiro("ID do pedido: ");
        pedidoService.buscarPorId(id)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("❌ Pedido não encontrado.")
                );
    }

    private void listarTodosPedidos() {
        List<Pedido> lista = pedidoService.listarTodos();
        if (lista.isEmpty()) { System.out.println("Nenhum pedido registrado."); return; }
        System.out.println("\n── Todos os Pedidos ──");
        lista.forEach(System.out::println);
    }

    // ─────────────────── RELATÓRIOS ───────────────────

    private void menuRelatorios() {
        System.out.println("\n──────────  RELATÓRIOS  ──────────");
        System.out.println("  1. Pedidos finalizados por data");
        System.out.println("  2. Faturamento do dia");
        System.out.println("  0. Voltar");
        System.out.println("───────────────────────────────────");
        int opcao = lerInteiro("Escolha: ");
        switch (opcao) {
            case 1 -> relatorioPorData();
            case 2 -> relatorioFaturamento();
            case 0 -> {}
            default -> System.out.println("⚠️  Opção inválida.");
        }
    }

    private void relatorioPorData() {
        LocalDate data = lerData("Data (dd/MM/yyyy) [Enter = hoje]: ");
        List<Pedido> pedidos = pedidoService.consultarPorData(data);
        System.out.println("\n── Pedidos finalizados em " + data.format(DATE_FORMATTER) + " ──");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido finalizado nesta data.");
        } else {
            pedidos.forEach(System.out::println);
        }
    }

    private void relatorioFaturamento() {
        LocalDate data = lerData("Data (dd/MM/yyyy) [Enter = hoje]: ");
        double total = pedidoService.calcularFaturamentoDia(data);
        int qtd = pedidoService.consultarPorData(data).size();
        System.out.printf("%n── Faturamento em %s ──%n", data.format(DATE_FORMATTER));
        System.out.printf("  Pedidos finalizados: %d%n", qtd);
        System.out.printf("  Total faturado:      R$ %.2f%n", total);
    }

    // ─────────────────── UTILITÁRIOS ───────────────────

    private int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Digite um número inteiro válido.");
            }
        }
    }

    private double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Digite um valor numérico válido (ex: 12.50).");
            }
        }
    }

    private LocalDate lerData(String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        if (entrada.isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(entrada, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("⚠️  Data inválida. Usando data de hoje.");
            return LocalDate.now();
        }
    }

    private void carregarDadosExemplo() {
        produtoService.cadastrar("X-Burguer", "Hambúrguer artesanal com queijo", 18.50);
        produtoService.cadastrar("Batata Frita", "Porção grande crocante", 12.00);
        produtoService.cadastrar("Refrigerante", "Lata 350ml", 6.00);
        produtoService.cadastrar("Milk Shake", "Chocolate, morango ou baunilha", 15.00);
        produtoService.cadastrar("Hot Dog", "Salsicha com molhos especiais", 10.00);
        System.out.println("ℹ️  5 produtos de exemplo carregados.\n");
    }
}
