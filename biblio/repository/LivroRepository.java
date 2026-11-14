package biblio.repository;

import biblio.model.Livro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LivroRepository {

    private final List<Livro> livros = new ArrayList<>();

    public void inserir(Livro livro) {
        livros.add(livro);
    }

    /** Retorna uma visão somente leitura, protegendo a lista interna. */
    public List<Livro> consultarTodos() {
        return Collections.unmodifiableList(livros);
    }

    public Livro consultarPorPosicao(int posicao) {
        return livros.get(posicao);
    }

    public void alterar(int posicao, Livro livro) {
        livros.set(posicao, livro);
    }

    public void excluir(int posicao) {
        livros.remove(posicao);
    }

    public int quantidade() {
        return livros.size();
    }
}