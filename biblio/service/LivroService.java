package biblio.service;

import biblio.exception.RegraNegocioException;
import biblio.model.Livro;
import biblio.repository.LivroRepository;

import java.util.List;

public class LivroService {

    private static final int ANO_MINIMO = 1900;

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Livro livro) {
        validar(livro, null);
        repository.inserir(livro);
    }

    public List<Livro> listarTodos() {
        return repository.consultarTodos();
    }

    public List<Livro> pesquisarPorTitulo(String texto) {
        String termo = normalizarTermo(texto);
        return repository.consultarTodos().stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(termo))
                .toList();
    }

    public List<Livro> pesquisarPorAutor(String texto) {
        String termo = normalizarTermo(texto);
        return repository.consultarTodos().stream()
                .filter(l -> l.getAutor().toLowerCase().contains(termo))
                .toList();
    }


    public Livro buscarPorIndice(int indice) {
        return repository.consultarPorPosicao(posicaoValida(indice));
    }

    public void alterar(int indice, Livro novosDados) {
        int posicao = posicaoValida(indice);
        Livro original = repository.consultarPorPosicao(posicao);
        validar(novosDados, original);
        repository.alterar(posicao, novosDados);
    }

    public void excluir(int indice) {
        repository.excluir(posicaoValida(indice));
    }



    private void validar(Livro livro, Livro livroEmEdicao) {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new RegraNegocioException("O título não pode ser vazio.");
        }
        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new RegraNegocioException("O autor não pode ser vazio.");
        }
        if (livro.getAnoPublicacao() < ANO_MINIMO) {
            throw new RegraNegocioException(
                    "O ano de publicação deve ser igual ou superior a " + ANO_MINIMO + ".");
        }
        boolean duplicado = repository.consultarTodos().stream()
                .filter(existente -> existente != livroEmEdicao) // ignora o próprio livro
                .anyMatch(existente -> existente.getTitulo().equalsIgnoreCase(livro.getTitulo()));
        if (duplicado) {
            throw new RegraNegocioException("Já existe um livro cadastrado com esse título.");
        }
    }

    private int posicaoValida(int indice) {
        if (repository.quantidade() == 0) {
            throw new RegraNegocioException("O acervo está vazio.");
        }
        if (indice < 1 || indice > repository.quantidade()) {
            throw new RegraNegocioException("Índice inválido: " + indice
                    + ". Informe um valor entre 1 e " + repository.quantidade() + ".");
        }
        return indice - 1;
    }

    private String normalizarTermo(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase();
    }
}