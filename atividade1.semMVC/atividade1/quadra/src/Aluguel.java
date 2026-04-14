import java.time.LocalDate;

public class Aluguel {
    private int id;
    private Cliente cliente;
    private Horario horario;
    private LocalDate data;
    private double valorCobrado;
    private boolean pago;

    public Aluguel(int id, Cliente cliente, Horario horario, LocalDate data, double valorCobrado) {
        this.id = id;
        this.cliente = cliente;
        this.horario = horario;
        this.data = data;
        this.valorCobrado = valorCobrado;
        this.pago = false;
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Horario getHorario() { return horario; }
    public LocalDate getData() { return data; }
    public double getValorCobrado() { return valorCobrado; }
    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    @Override
    public String toString() {
        return String.format(
            "Aluguel[id=%d, cliente='%s', horario='%s', data=%s, valor=R$%.2f, pago=%s]",
            id, cliente.getNome(), horario.getDescricao(), data, valorCobrado, pago ? "Sim" : "Não"
        );
    }
}
