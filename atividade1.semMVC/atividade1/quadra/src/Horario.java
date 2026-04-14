public class Horario {
    private int id;
    private String descricao; // Ex: "08:00 - 09:00"
    private double valorPorHora;

    public Horario(int id, String descricao, double valorPorHora) {
        if (valorPorHora < 0) {
            throw new IllegalArgumentException("Valor por hora não pode ser negativo.");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do horário não pode ser vazia.");
        }
        this.id = id;
        this.descricao = descricao.trim();
        this.valorPorHora = valorPorHora;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValorPorHora() { return valorPorHora; }

    @Override
    public String toString() {
        return String.format("Horario[id=%d, descricao='%s', valor=R$%.2f]", id, descricao, valorPorHora);
    }
}
