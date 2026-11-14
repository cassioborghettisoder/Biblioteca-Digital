package biblio.model;

public class LivroFisico extends Livro {

    private final int numeroPaginas;

    public LivroFisico(String titulo, String autor, int anoPublicacao, int numeroPaginas) {
        super(titulo, autor, anoPublicacao);
        this.numeroPaginas = numeroPaginas;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    @Override
    public String getTipo() {
        return "Livro Físico";
    }

    @Override
    protected String getDetalhes() {
        return numeroPaginas + " páginas";
    }
}