package main;

import exception.HorarioIndisponivelException;
import model.Aluguel;
import model.Cliente;
import model.Horario;
import service.QuadraService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final QuadraService service = new QuadraService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  BEM-VINDO AO SISTEMA DE ALUGUEL DE QUADRA");
        System.out.println("===========================================");

        popularDadosExemplo();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            processarOpcao(opcao);
        } while (opcao != 0);

        System.out.println("Sistema encerrado. Até logo!");
    }

    private static void exibirMenu() {
        System.out.println("\n─── MENU PRINCIPAL ───────────────────────");
        System.out.println(" 1. Cadastrar cliente");
        System.out.println(" 2. Listar clientes");
        System.out.println(" 3. Cadastrar horário");
        System.out.println(" 4. Listar horários");
        System.out.println(" 5. Registrar aluguel");
        System.out.println(" 6. Consultar aluguéis por data");
        System.out.println(" 7. Ver total de um cliente no dia");
        System.out.println(" 8. Marcar aluguel como pago");
        System.out.println(" 0. Sair");
        System.out.println("──────────────────────────────────────────");
    }

    private static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarCliente();
            case 2 -> listarClientes();
            case 3 -> cadastrarHorario();
            case 4 -> listarHorarios();
            case 5 -> registrarAluguel();
            case 6 -> consultarPorData();
            case 7 -> totalClienteNoDia();
            case 8 -> marcarPago();
            case 0 -> {} // sai
            default -> System.out.println("⚠ Opção inválida. Tente novamente.");
        }
    }

    private static void cadastrarCliente() {
        System.out.println("\n── Cadastrar Cliente ──");
        String nome = lerTexto("Nome: ");
        String telefone = lerTexto("Telefone: ");
        try {
            Cliente c = service.cadastrarCliente(nome, telefone);
            System.out.println("✔ Cliente cadastrado: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("✖ Erro: " + e.getMessage());
        }
    }

    private static void listarClientes() {
        System.out.println("\n── Clientes Cadastrados ──");
        List<Cliente> lista = service.listarClientes();
        if (lista.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            lista.forEach(c -> System.out.printf(
                "  [%d] %s | Tel: %s%n", c.getId(), c.getNome(), c.getTelefone()));
        }
    }

    private static void cadastrarHorario() {
        System.out.println("\n── Cadastrar Horário ──");
        String descricao = lerTexto("Descrição (ex: 08:00 - 09:00): ");
        double valor = lerDouble("Valor por hora (R$): ");
        try {
            Horario h = service.cadastrarHorario(descricao, valor);
            System.out.println("✔ Horário cadastrado: " + h);
        } catch (IllegalArgumentException e) {
            System.out.println("✖ Erro: " + e.getMessage());
        }
    }

    private static void listarHorarios() {
        System.out.println("\n── Horários Cadastrados ──");
        List<Horario> lista = service.listarHorarios();
        if (lista.isEmpty()) {
            System.out.println("Nenhum horário cadastrado.");
        } else {
            lista.forEach(h -> System.out.printf(
                "  [%d] %s | R$ %.2f%n", h.getId(), h.getDescricao(), h.getValorPorHora()));
        }
    }

    private static void registrarAluguel() {
        System.out.println("\n── Registrar Aluguel ──");
        listarClientes();
        int clienteId = lerInteiro("ID do cliente: ");
        listarHorarios();
        int horarioId = lerInteiro("ID do horário: ");
        LocalDate data = lerData("Data do aluguel (dd/MM/yyyy): ");
        if (data == null) return;

        try {
            Aluguel a = service.registrarAluguel(clienteId, horarioId, data);
            System.out.println("✔ Aluguel registrado: " + a);

            double total = service.calcularTotalClienteNoDia(clienteId, data);
            System.out.printf("   Total do cliente %s em %s: R$ %.2f%n",
                a.getCliente().getNome(), data.format(FMT), total);

        } catch (HorarioIndisponivelException e) {
            System.out.println("✖ Horário indisponível: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("✖ Erro: " + e.getMessage());
        }
    }

    private static void consultarPorData() {
        System.out.println("\n── Aluguéis por Data ──");
        LocalDate data = lerData("Data (dd/MM/yyyy): ");
        if (data == null) return;

        List<Aluguel> lista = service.consultarAlugueisPorData(data);
        if (lista.isEmpty()) {
            System.out.println("Nenhum aluguel encontrado para " + data.format(FMT) + ".");
        } else {
            System.out.println("Aluguéis em " + data.format(FMT) + ":");
            lista.forEach(a -> System.out.printf(
                "  [%d] %s | %s | R$ %.2f | Pago: %s%n",
                a.getId(), a.getCliente().getNome(),
                a.getHorario().getDescricao(),
                a.getValorCobrado(), a.isPago() ? "Sim" : "Não"));
        }
    }

    private static void totalClienteNoDia() {
        System.out.println("\n── Total do Cliente no Dia ──");
        listarClientes();
        int clienteId = lerInteiro("ID do cliente: ");
        LocalDate data = lerData("Data (dd/MM/yyyy): ");
        if (data == null) return;

        double total = service.calcularTotalClienteNoDia(clienteId, data);
        Cliente c = service.buscarClientePorId(clienteId);
        String nome = (c != null) ? c.getNome() : "ID " + clienteId;
        System.out.printf("Total de %s em %s: R$ %.2f%n", nome, data.format(FMT), total);
    }

    private static void marcarPago() {
        System.out.println("\n── Marcar Aluguel como Pago ──");
        service.listarTodosAlugueis().forEach(a -> System.out.printf(
            "  [%d] %s | %s | %s | Pago: %s%n",
            a.getId(), a.getCliente().getNome(),
            a.getHorario().getDescricao(),
            a.getData().format(FMT),
            a.isPago() ? "✔ Sim" : "✖ Não"));

        int id = lerInteiro("ID do aluguel: ");
        boolean ok = service.marcarComoPago(id);
        System.out.println(ok ? "✔ Aluguel marcado como pago!" : "✖ Aluguel não encontrado.");
    }

    private static String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("⚠ Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("⚠ Digite um valor numérico válido (ex: 50.00).");
            }
        }
    }

    private static LocalDate lerData(String prompt) {
        System.out.print(prompt);
        try {
            return LocalDate.parse(scanner.nextLine().trim(), FMT);
        } catch (DateTimeParseException e) {
            System.out.println("⚠ Data inválida. Use o formato dd/MM/yyyy.");
            return null;
        }
    }

    // ─── DADOS DE EXEMPLO ────────────────────────────────────────────────────

    private static void popularDadosExemplo() {
        service.cadastrarCliente("Carlos Silva", "(41) 99999-1111");
        service.cadastrarCliente("Ana Souza",   "(41) 98888-2222");
        service.cadastrarCliente("João Lima",   "(41) 97777-3333");

        service.cadastrarHorario("08:00 - 09:00", 80.00);
        service.cadastrarHorario("09:00 - 10:00", 80.00);
        service.cadastrarHorario("10:00 - 11:00", 90.00);
        service.cadastrarHorario("19:00 - 20:00", 120.00);
        service.cadastrarHorario("20:00 - 21:00", 120.00);

        System.out.println("✔ Dados de exemplo carregados (3 clientes, 5 horários).");
    }
}
