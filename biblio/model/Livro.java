package biblio.model;

public abstract class Livro {

    private final String titulo;
    private final String autor;
    private final int anoPublicacao;

    protected Livro(String titulo, String autor, int anoPublicacao) {
        // dados textuais armazenados sem espaços no início/fim
        this.titulo = titulo == null ? null : titulo.trim();
        this.autor = autor == null ? null : autor.trim();
        this.anoPublicacao = anoPublicacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public abstract String getTipo();

    protected abstract String getDetalhes();

    @Override
    public String toString() {
        return "[" + getTipo() + "] " + titulo + " - " + autor
                + " (" + anoPublicacao + ") | " + getDetalhes();
    }
}