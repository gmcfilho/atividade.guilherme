package model;

import exception.PedidoVazioException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {

    public enum Status { ABERTO, FINALIZADO }

    private static int contadorId = 1;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private String nomeCliente;
    private List<ItemPedido> itens;
    private LocalDateTime dataHora;
    private Status status;

    public Pedido(String nomeCliente) {
        this.id = contadorId++;
        this.nomeCliente = (nomeCliente == null || nomeCliente.trim().isEmpty())
                ? "Cliente " + id
                : nomeCliente.trim();
        this.itens = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
        this.status = Status.ABERTO;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (status == Status.FINALIZADO) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido já finalizado.");
        }
        // Se o produto já existe no pedido, aumenta a quantidade
        for (ItemPedido item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                itens.remove(item);
                itens.add(new ItemPedido(produto, item.getQuantidade() + quantidade));
                return;
            }
        }
        itens.add(new ItemPedido(produto, quantidade));
    }

    public void finalizar() throws PedidoVazioException {
        if (itens.isEmpty()) {
            throw new PedidoVazioException("Um pedido só pode ser finalizado se tiver pelo menos um produto.");
        }
        this.status = Status.FINALIZADO;
    }

    public double calcularTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    public LocalDate getData() { return dataHora.toLocalDate(); }

    public int getId() { return id; }
    public String getNomeCliente() { return nomeCliente; }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public LocalDateTime getDataHora() { return dataHora; }
    public Status getStatus() { return status; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("╔══════════════════════════════════════╗%n"));
        sb.append(String.format("  Pedido #%d | %s%n", id, dataHora.format(FORMATTER)));
        sb.append(String.format("  Cliente: %s | Status: %s%n", nomeCliente, status));
        sb.append(String.format("---------------------------------------%n"));
        if (itens.isEmpty()) {
            sb.append("  (nenhum item)%n");
        } else {
            for (ItemPedido item : itens) {
                sb.append(item).append("\n");
            }
        }
        sb.append(String.format("---------------------------------------%n"));
        sb.append(String.format("  TOTAL: R$ %.2f%n", calcularTotal()));
        sb.append(String.format("╚══════════════════════════════════════╝%n"));
        return sb.toString();
    }
}
