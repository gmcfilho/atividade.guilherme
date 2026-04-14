import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PedidoService {

    private List<Pedido> pedidos = new ArrayList<>();

    public Pedido abrirPedido(String nomeCliente) {
        Pedido pedido = new Pedido(nomeCliente);
        pedidos.add(pedido);
        return pedido;
    }

    public void adicionarItem(Pedido pedido, Produto produto, int quantidade) {
        pedido.adicionarItem(produto, quantidade);
    }

    public void finalizarPedido(Pedido pedido) throws PedidoVazioException {
        pedido.finalizar();
    }

    public Optional<Pedido> buscarPorId(int id) {
        return pedidos.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Pedido> listarTodos() {
        return Collections.unmodifiableList(pedidos);
    }

    /**
     * Retorna todos os pedidos finalizados em uma data específica.
     */
    public List<Pedido> consultarPorData(LocalDate data) {
        return pedidos.stream()
                .filter(p -> p.getData().equals(data) && p.getStatus() == Pedido.Status.FINALIZADO)
                .collect(Collectors.toList());
    }

    /**
     * Calcula o faturamento total de um dia específico (apenas pedidos finalizados).
     */
    public double calcularFaturamentoDia(LocalDate data) {
        return consultarPorData(data)
                .stream()
                .mapToDouble(Pedido::calcularTotal)
                .sum();
    }
}
