package exception;

public class LivroIndisponivelException extends Exception {
    public LivroIndisponivelException(String titulo) {
        super("O livro \"" + titulo + "\" não possui exemplares disponíveis no momento.");
    }
}
