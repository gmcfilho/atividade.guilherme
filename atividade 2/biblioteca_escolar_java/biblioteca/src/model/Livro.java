package model;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String genero;
    private int quantidadeTotal;
    private int quantidadeDisponivel;

    public Livro(int id, String titulo, String autor, String genero, int quantidadeTotal) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
    }


    public int getId()                     { return id; }
    public String getTitulo()              { return titulo; }
    public String getAutor()               { return autor; }
    public String getGenero()              { return genero; }
    public int getQuantidadeTotal()        { return quantidadeTotal; }
    public int getQuantidadeDisponivel()   { return quantidadeDisponivel; }

    public void emprestar() {
        if (quantidadeDisponivel <= 0) {
            throw new IllegalStateException("Livro sem exemplares disponíveis.");
        }
        quantidadeDisponivel--;
    }

    public void devolver() {
        if (quantidadeDisponivel >= quantidadeTotal) {
            throw new IllegalStateException("Quantidade disponível já está no máximo.");
        }
        quantidadeDisponivel++;
    }

    public boolean isDisponivel() {
        return quantidadeDisponivel > 0;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s — %s | %s | Disponíveis: %d/%d",
                id, titulo, autor, genero, quantidadeDisponivel, quantidadeTotal);
    }
}
