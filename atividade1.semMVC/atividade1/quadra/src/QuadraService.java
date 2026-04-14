import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuadraService {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Horario> horarios = new ArrayList<>();
    private List<Aluguel> alugueis = new ArrayList<>();

    private int proximoIdCliente = 1;
    private int proximoIdHorario = 1;
    private int proximoIdAluguel = 1;

    // ─── CLIENTES ────────────────────────────────────────────────────────────

    public Cliente cadastrarCliente(String nome, String telefone) {
        // Valida nome vazio (a própria classe Cliente também valida)
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }
        Cliente cliente = new Cliente(proximoIdCliente++, nome, telefone);
        clientes.add(cliente);
        return cliente;
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    public Cliente buscarClientePorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Horario cadastrarHorario(String descricao, double valorPorHora) {
        if (valorPorHora < 0) {
            throw new IllegalArgumentException("Valor por hora não pode ser negativo.");
        }
        Horario horario = new Horario(proximoIdHorario++, descricao, valorPorHora);
        horarios.add(horario);
        return horario;
    }

    public List<Horario> listarHorarios() {
        return new ArrayList<>(horarios);
    }

    public Horario buscarHorarioPorId(int id) {
        return horarios.stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public Aluguel registrarAluguel(int clienteId, int horarioId, LocalDate data)
            throws HorarioIndisponivelException {

        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com id " + clienteId + " não encontrado.");
        }

        Horario horario = buscarHorarioPorId(horarioId);
        if (horario == null) {
            throw new IllegalArgumentException("Horário com id " + horarioId + " não encontrado.");
        }

        boolean horarioOcupado = alugueis.stream()
                .anyMatch(a -> a.getHorario().getId() == horarioId
                        && a.getData().equals(data));

        if (horarioOcupado) {
            throw new HorarioIndisponivelException(
                "O horário '" + horario.getDescricao() + "' já está reservado em " + data + ".");
        }

        Aluguel aluguel = new Aluguel(
                proximoIdAluguel++, cliente, horario, data, horario.getValorPorHora());
        alugueis.add(aluguel);
        return aluguel;
    }

    public List<Aluguel> consultarAlugueisPorData(LocalDate data) {
        return alugueis.stream()
                .filter(a -> a.getData().equals(data))
                .collect(Collectors.toList());
    }

    public double calcularTotalClienteNoDia(int clienteId, LocalDate data) {
        return alugueis.stream()
                .filter(a -> a.getCliente().getId() == clienteId && a.getData().equals(data))
                .mapToDouble(Aluguel::getValorCobrado)
                .sum();
    }
    public boolean marcarComoPago(int aluguelId) {
        return alugueis.stream()
                .filter(a -> a.getId() == aluguelId)
                .findFirst()
                .map(a -> { a.setPago(true); return true; })
                .orElse(false);
    }
    public List<Aluguel> listarTodosAlugueis() {
        return new ArrayList<>(alugueis);
    }
}
