public class Produto {

    private static int contadorId = 1;

    private int id;
    private String nome;
    private String descricao;
    private double preco;

    public Produto(String nome, String descricao, double preco) {
        validarNome(nome);
        validarPreco(preco);
        this.id = contadorId++;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
    }

    private void validarPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo.");
        }
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public void setPreco(double preco) {
        validarPreco(preco);
        this.preco = preco;
    }

    @Override
    public String toString() {
        return String.format("[#%d] %s - R$ %.2f | %s", id, nome, preco, descricao);
    }
}
