public class Cliente {
    private int id;
    private String nome;
    private String telefone;

    public Cliente(int id, String nome, String telefone) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }
        this.id = id;
        this.nome = nome.trim();
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }

    @Override
    public String toString() {
        return String.format("Cliente[id=%d, nome='%s', telefone='%s']", id, nome, telefone);
    }
}
