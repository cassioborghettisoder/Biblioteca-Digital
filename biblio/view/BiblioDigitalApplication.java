package biblio.view;

import biblio.exception.RegraNegocioException;
import biblio.model.AudioLivro;
import biblio.model.Livro;
import biblio.model.LivroDigital;
import biblio.model.LivroFisico;
import biblio.repository.LivroRepository;
import biblio.service.LivroService;

import java.io.IO;
import java.util.List;

public class BiblioDigitalApplication {

    private static final LivroService service = new LivroService(new LivroRepository());

    public static void main(String[] args) {
        IO.println("===== Bem vindo ao BiblioDigital =====");
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("");
            try {
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> pesquisarPorTitulo();
                    case 3 -> pesquisarPorAutor();
                    case 4 -> alterar();
                    case 5 -> excluir();
                    case 0 -> IO.println("Encerrando o BiblioDigital. Até logo!");
                    default -> IO.println("Opção inexistente! Tente novamente.");
                }
            } catch (RegraNegocioException e) {
                IO.println("Erro: " + e.getMessage());
            } catch (NumberFormatException e) {
                IO.println("Erro: valor numérico inválido.");
            }
        } while (opcao != 0);
    }

    private static void exibirMenu() {
        IO.println("""

                Menu:

                1 - Cadastrar novo Livro
                2 - Pesquisar Livro por título
                3 - Pesquisar Livro por autor
                4 - Alterar dados do Livro
                5 - Excluir Livro
                0 - Sair
                """);
        IO.print("Escolha uma das opções acima: ");
    }

    private static void cadastrar() {
        Livro livro = lerDadosLivro();
        service.cadastrar(livro);
        IO.println("Livro cadastrado com sucesso!");
    }

    private static void pesquisarPorTitulo() {
        String texto = IO.readln("Informe o título (ou parte dele): ");
        exibirResultados(service.pesquisarPorTitulo(texto));
    }

    private static void pesquisarPorAutor() {
        String texto = IO.readln("Informe o autor (ou parte do nome): ");
        exibirResultados(service.pesquisarPorAutor(texto));
    }

    private static void alterar() {
        if (!listarLivros()) {
            return;
        }
        int indice = lerInteiro("Informe o índice do livro que deseja alterar: ");
        Livro atual = service.buscarPorIndice(indice);

        IO.println("\nDados atuais: " + atual);
        IO.println("Informe os novos dados:");
        Livro novo = lerDadosLivro();

        service.alterar(indice, novo);
        IO.println("Livro alterado com sucesso!");
    }

    private static void excluir() {
        if (!listarLivros()) {
            return;
        }
        int indice = lerInteiro("Informe o índice do livro que deseja excluir: ");
        Livro livro = service.buscarPorIndice(indice);

        String resposta = IO.readln("Deseja realmente excluir \"" + livro.getTitulo() + "\"? (S/N): ");
        if (resposta.trim().equalsIgnoreCase("S")) {
            service.excluir(indice);
            IO.println("Livro excluído com sucesso!");
        } else {
            IO.println("Operação cancelada.");
        }
    }


    private static boolean listarLivros() {
        List<Livro> livros = service.listarTodos();
        if (livros.isEmpty()) {
            IO.println("O acervo está vazio.");
            return false;
        }
        IO.println("\n===== Livros cadastrados =====\n");
        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
        IO.println("");
        return true;
    }

    private static void exibirResultados(List<Livro> resultados) {
        if (resultados.isEmpty()) {
            IO.println("Não existem resultados para a pesquisa realizada.");
            return;
        }
        IO.println("\n===== Resultados =====");
        resultados.forEach(l -> IO.println(l.toString()));
    }

    private static Livro lerDadosLivro() {
        IO.println("Tipo do livro: 1 - Físico | 2 - Digital | 3 - Audiolivro");
        int tipo = lerInteiro("Escolha o tipo: ");
        if (tipo < 1 || tipo > 3) {
            throw new RegraNegocioException("Tipo de livro inválido.");
        }

        String titulo = IO.readln("Título: ");
        String autor = IO.readln("Autor: ");
        int ano = lerInteiro("Ano de publicação: ");

        return switch (tipo) {
            case 1 -> new LivroFisico(titulo, autor, ano,
                    lerInteiro("Número de páginas: "));
            case 2 -> new LivroDigital(titulo, autor, ano,
                    IO.readln("Formato (PDF, EPUB...): "),
                    Double.parseDouble(IO.readln("Tamanho em MB: ").trim().replace(',', '.')));
            default -> new AudioLivro(titulo, autor, ano,
                    IO.readln("Narrador: "),
                    lerInteiro("Duração em minutos: "));
        };
    }

    private static int lerInteiro(String mensagem) {
        String entrada = mensagem.isEmpty() ? IO.readln() : IO.readln(mensagem);
        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}