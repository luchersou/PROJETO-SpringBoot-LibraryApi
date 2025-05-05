package org.example;
import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Main {
    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.exibir();
    }
}

class MenuPrincipal {
    public void exibir() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== MENU PRINCIPAL ======");
            System.out.println("(1) Estudantes");
            System.out.println("(2) Professores");
            System.out.println("(3) Disciplinas");
            System.out.println("(4) Turmas");
            System.out.println("(5) Matrículas");
            System.out.println("(0) Sair");

            System.out.print("\nInforme a opção desejada: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    new MenuEstudantes().exibir();
                    break;
                case "2":
                    new MenuProfessores().exibir();
                    break;
                case "3":
                    new MenuDisciplinas().exibir();
                    break;
                case "4":
                    new MenuTurmas().exibir();
                    break;
                case "5":
                    new MenuMatriculas().exibir();
                    break;
                case "0":
                    System.out.println("\nEncerrando o programa...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
                    aguardarEnter(scanner);
            }
        }
    }

    public static void aguardarEnter(Scanner scanner) {
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}

class GerenciadorArquivos {
    @SuppressWarnings("unchecked")
    public static boolean salvarDados(String arquivo, List<Map<String, Object>> dados) {
        try (FileWriter file = new FileWriter(arquivo)) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(dados);
            file.write(jsonArray.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao salvar no arquivo " + arquivo + ": " + e.getMessage());
            return false;
        }
    }

    public static List<Map<String, Object>> carregarDados(String arquivo) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(arquivo)) {
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            List<Map<String, Object>> lista = new ArrayList<>();

            for (Object item : jsonArray) {
                JSONObject jsonObject = (JSONObject) item;
                Map<String, Object> map = new HashMap<>();

                for (Object key : jsonObject.keySet()) {
                    String chave = (String) key;
                    map.put(chave, jsonObject.get(chave));
                }

                lista.add(map);
            }

            return lista;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ParseException e) {
            System.out.println("Erro ao carregar o arquivo " + arquivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

class CrudUtils {
    public static boolean incluirRegistro(List<Map<String, Object>> lista, Map<String, Object> dados, String chavePrimaria) {
        if (chavePrimaria != null && dados.containsKey(chavePrimaria)) {
            Object valorChave = dados.get(chavePrimaria);

            for (Map<String, Object> item : lista) {
                if (valorChave.equals(item.get(chavePrimaria))) {
                    System.out.println("\nJá existe um registro com " + chavePrimaria + " = " + valorChave + ".");
                    return false;
                }
            }
        }

        lista.add(dados);
        return true;
    }

    public static boolean listarRegistros(List<Map<String, Object>> lista, String titulo) {
        System.out.println("\n===== " + titulo + " =====");

        if (lista.isEmpty()) {
            System.out.println("Não há registros cadastrados.");
            return false;
        }

        for (int i = 0; i < lista.size(); i++) {
            System.out.println("\nRegistro #" + (i + 1) + ":");
            Map<String, Object> item = lista.get(i);

            for (Map.Entry<String, Object> entry : item.entrySet()) {
                System.out.println("- " + entry.getKey() + ": " + entry.getValue());
            }
        }

        return true;
    }

    public static Map.Entry<Integer, Map<String, Object>> buscarRegistro(List<Map<String, Object>> lista, String chave, Object valor) {
        for (int i = 0; i < lista.size(); i++) {
            Map<String, Object> item = lista.get(i);
            if (valor.equals(item.get(chave))) {
                return new AbstractMap.SimpleEntry<>(i, item);
            }
        }
        return new AbstractMap.SimpleEntry<>(-1, null);
    }

    public static boolean atualizarRegistro(List<Map<String, Object>> lista, String chaveBusca, Object valorBusca, Map<String, Object> novosDados) {
        Map.Entry<Integer, Map<String, Object>> resultado = buscarRegistro(lista, chaveBusca, valorBusca);

        if (resultado.getKey() >= 0) {
            Map<String, Object> item = resultado.getValue();

            for (Map.Entry<String, Object> entry : novosDados.entrySet()) {
                item.put(entry.getKey(), entry.getValue());
            }

            return true;
        }

        return false;
    }

    public static boolean excluirRegistro(List<Map<String, Object>> lista, String chave, Object valor) {
        Map.Entry<Integer, Map<String, Object>> resultado = buscarRegistro(lista, chave, valor);

        if (resultado.getKey() >= 0) {
            lista.remove(resultado.getKey().intValue());
            return true;
        }

        return false;
    }
}

abstract class MenuGenerico {
    protected String arquivo;
    protected String campoChave;
    protected Map<String, Class<?>> campos;
    protected String titulo;

    public void exibir() {
        Scanner scanner = new Scanner(System.in);
        List<Map<String, Object>> dados = GerenciadorArquivos.carregarDados(arquivo);

        while (true) {
            System.out.println("\n***** [" + titulo + "] MENU DE OPERAÇÕES *****");
            System.out.println("(1) Incluir.");
            System.out.println("(2) Listar.");
            System.out.println("(3) Atualizar.");
            System.out.println("(4) Excluir.");
            System.out.println("(9) Voltar ao menu principal.");

            System.out.print("\nInforme a ação desejada: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    incluirRegistro(scanner, dados);
                    break;
                case "2":
                    listarRegistros(dados);
                    break;
                case "3":
                    atualizarRegistro(scanner, dados);
                    break;
                case "4":
                    excluirRegistro(scanner, dados);
                    break;
                case "9":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }

            MenuPrincipal.aguardarEnter(scanner);
        }
    }

    private void incluirRegistro(Scanner scanner, List<Map<String, Object>> dados) {
        System.out.println("\n===== INCLUSÃO DE " + titulo + " =====");
        Map<String, Object> novoRegistro = new HashMap<>();

        for (Map.Entry<String, Class<?>> campo : campos.entrySet()) {
            while (true) {
                try {
                    System.out.print("Informe o " + campo.getKey() + ": ");
                    String valor = scanner.nextLine();

                    if (campo.getValue() == Integer.class) {
                        novoRegistro.put(campo.getKey(), Integer.parseInt(valor));
                    } else {
                        novoRegistro.put(campo.getKey(), valor);
                    }

                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido. O " + campo.getKey() + " deve ser um número inteiro.");
                }
            }
        }

        if (CrudUtils.incluirRegistro(dados, novoRegistro, campoChave)) {
            if (GerenciadorArquivos.salvarDados(arquivo, dados)) {
                System.out.println(titulo + " incluído(a) com sucesso!");
            } else {
                System.out.println("Erro ao salvar os dados de " + titulo + ".");
            }
        }
    }

    private void listarRegistros(List<Map<String, Object>> dados) {
        CrudUtils.listarRegistros(dados, "LISTAGEM DE " + titulo);
    }

    private void atualizarRegistro(Scanner scanner, List<Map<String, Object>> dados) {
        System.out.println("\n===== ATUALIZAÇÃO DE " + titulo + " =====");

        if (!CrudUtils.listarRegistros(dados, "LISTAGEM DE " + titulo)) {
            return;
        }

        try {
            System.out.print("\nInforme o " + campoChave + " do registro a atualizar: ");
            int idBusca = Integer.parseInt(scanner.nextLine());

            Map.Entry<Integer, Map<String, Object>> resultado = CrudUtils.buscarRegistro(dados, campoChave, idBusca);

            if (resultado.getValue() != null) {
                System.out.println("\nInforme os novos dados (deixe em branco para manter o valor atual):");
                Map<String, Object> novosDados = new HashMap<>();

                for (Map.Entry<String, Class<?>> campo : campos.entrySet()) {
                    Object valorAtual = resultado.getValue().get(campo.getKey());
                    System.out.print(campo.getKey() + " [" + valorAtual + "]: ");
                    String entrada = scanner.nextLine();

                    if (!entrada.trim().isEmpty()) {
                        try {
                            if (campo.getValue() == Integer.class) {
                                novosDados.put(campo.getKey(), Integer.parseInt(entrada));
                            } else {
                                novosDados.put(campo.getKey(), entrada);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido para " + campo.getKey() + ". Mantendo valor original.");
                        }
                    }
                }

                if (CrudUtils.atualizarRegistro(dados, campoChave, idBusca, novosDados)) {
                    if (GerenciadorArquivos.salvarDados(arquivo, dados)) {
                        System.out.println(titulo + " atualizado(a) com sucesso!");
                    } else {
                        System.out.println("Erro ao salvar os dados de " + titulo + ".");
                    }
                } else {
                    System.out.println("Erro ao atualizar " + titulo + ".");
                }
            } else {
                System.out.println(titulo + " não encontrado(a).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }

    private void excluirRegistro(Scanner scanner, List<Map<String, Object>> dados) {
        System.out.println("\n===== EXCLUSÃO DE " + titulo + " =====");

        if (!CrudUtils.listarRegistros(dados, "LISTAGEM DE " + titulo)) {
            return;
        }

        try {
            System.out.print("\nInforme o " + campoChave + " do registro a excluir: ");
            int idBusca = Integer.parseInt(scanner.nextLine());

            if (verificarDependencias(idBusca)) {
                System.out.print("Tem certeza que deseja excluir este(a) " + titulo.toLowerCase() + "? (S/N): ");
                String confirmacao = scanner.nextLine();

                if (confirmacao.equalsIgnoreCase("S")) {
                    if (CrudUtils.excluirRegistro(dados, campoChave, idBusca)) {
                        if (GerenciadorArquivos.salvarDados(arquivo, dados)) {
                            System.out.println(titulo + " excluído(a) com sucesso!");
                        } else {
                            System.out.println("Erro ao salvar os dados de " + titulo + ".");
                        }
                    } else {
                        System.out.println(titulo + " não encontrado(a).");
                    }
                } else {
                    System.out.println("Operação cancelada.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }

    protected boolean verificarDependencias(int codigo) {
        // Implementação específica para cada classe filha
        return true;
    }

    protected static boolean verificarExistencia(String tipo, int codigo) {
        String arquivo = tipo.toLowerCase() + "s.json";
        List<Map<String, Object>> dados = GerenciadorArquivos.carregarDados(arquivo);

        String campoChave = tipo.equals("estudante") || tipo.equals("professor") ?
                "código do " + tipo : "código da " + tipo;

        for (Map<String, Object> item : dados) {
            if (codigo == ((Number) item.get(campoChave)).intValue()) {
                return true;
            }
        }

        System.out.println(tipo.substring(0, 1).toUpperCase() + tipo.substring(1) +
                " com código " + codigo + " não existe.");
        return false;
    }
}

class MenuEstudantes extends MenuGenerico {
    public MenuEstudantes() {
        this.arquivo = "estudantes.json";
        this.campoChave = "código do estudante";
        this.titulo = "ESTUDANTES";

        this.campos = new LinkedHashMap<>();
        campos.put("código do estudante", Integer.class);
        campos.put("nome do estudante", String.class);
        campos.put("cpf do estudante", String.class);
    }

    @Override
    protected boolean verificarDependencias(int codigo) {
        List<Map<String, Object>> matriculas = GerenciadorArquivos.carregarDados("matriculas.json");

        for (Map<String, Object> matricula : matriculas) {
            if (codigo == ((Number) matricula.get("código do estudante")).intValue()) {
                System.out.println("Este estudante possui matrículas. Exclua as matrículas primeiro.");
                return false;
            }
        }

        return true;
    }
}

class MenuProfessores extends MenuGenerico {
    public MenuProfessores() {
        this.arquivo = "professores.json";
        this.campoChave = "código do professor";
        this.titulo = "PROFESSORES";

        this.campos = new LinkedHashMap<>();
        campos.put("código do professor", Integer.class);
        campos.put("nome do professor", String.class);
        campos.put("cpf do professor", String.class);
    }

    @Override
    protected boolean verificarDependencias(int codigo) {
        List<Map<String, Object>> turmas = GerenciadorArquivos.carregarDados("turmas.json");

        for (Map<String, Object> turma : turmas) {
            if (codigo == ((Number) turma.get("código do professor")).intValue()) {
                System.out.println("Este professor está associado a turmas. Exclua as turmas primeiro.");
                return false;
            }
        }

        return true;
    }
}

class MenuDisciplinas extends MenuGenerico {
    public MenuDisciplinas() {
        this.arquivo = "disciplinas.json";
        this.campoChave = "código da disciplina";
        this.titulo = "DISCIPLINAS";

        this.campos = new LinkedHashMap<>();
        campos.put("código da disciplina", Integer.class);
        campos.put("nome da disciplina", String.class);
    }

    @Override
    protected boolean verificarDependencias(int codigo) {
        List<Map<String, Object>> turmas = GerenciadorArquivos.carregarDados("turmas.json");

        for (Map<String, Object> turma : turmas) {
            if (codigo == ((Number) turma.get("código da disciplina")).intValue()) {
                System.out.println("Esta disciplina está associada a turmas. Exclua as turmas primeiro.");
                return false;
            }
        }

        return true;
    }
}

class MenuTurmas {
    public void exibir() {
        Scanner scanner = new Scanner(System.in);
        List<Map<String, Object>> turmas = GerenciadorArquivos.carregarDados("turmas.json");

        while (true) {
            System.out.println("\n***** [TURMAS] MENU DE OPERAÇÕES *****");
            System.out.println("(1) Incluir.");
            System.out.println("(2) Listar.");
            System.out.println("(3) Atualizar.");
            System.out.println("(4) Excluir.");
            System.out.println("(9) Voltar ao menu principal.");

            System.out.print("\nInforme a ação desejada: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    incluirTurma(scanner, turmas);
                    break;
                case "2":
                    listarTurmas(turmas);
                    break;
                case "3":
                    atualizarTurma(scanner, turmas);
                    break;
                case "4":
                    excluirTurma(scanner, turmas);
                    break;
                case "9":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }

            MenuPrincipal.aguardarEnter(scanner);
        }
    }

    private void incluirTurma(Scanner scanner, List<Map<String, Object>> turmas) {
        System.out.println("\n===== INCLUSÃO DE TURMA =====");

        try {
            System.out.print("Informe o código da turma: ");
            int codigoTurma = Integer.parseInt(scanner.nextLine());

            // Verificar se já existe turma com esse código
            for (Map<String, Object> turma : turmas) {
                if (codigoTurma == ((Number) turma.get("código da turma")).intValue()) {
                    System.out.println("Já existe uma turma com esse código.");
                    return;
                }
            }

            System.out.print("Informe o código do professor: ");
            int codigoProfessor = Integer.parseInt(scanner.nextLine());
            if (!MenuGenerico.verificarExistencia("professor", codigoProfessor)) {
                return;
            }

            System.out.print("Informe o código da disciplina: ");
            int codigoDisciplina = Integer.parseInt(scanner.nextLine());
            if (!MenuGenerico.verificarExistencia("disciplina", codigoDisciplina)) {
                return;
            }

            Map<String, Object> novaTurma = new HashMap<>();
            novaTurma.put("código da turma", codigoTurma);
            novaTurma.put("código do professor", codigoProfessor);
            novaTurma.put("código da disciplina", codigoDisciplina);

            turmas.add(novaTurma);
            if (GerenciadorArquivos.salvarDados("turmas.json", turmas)) {
                System.out.println("Turma incluída com sucesso!");
            } else {
                System.out.println("Erro ao salvar os dados de turmas.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }

    private void listarTurmas(List<Map<String, Object>> turmas) {
        System.out.println("\n===== LISTAGEM DE TURMAS =====");

        if (turmas.isEmpty()) {
            System.out.println("Não há turmas cadastradas.");
            return;
        }

        List<Map<String, Object>> professores = GerenciadorArquivos.carregarDados("professores.json");
        List<Map<String, Object>> disciplinas = GerenciadorArquivos.carregarDados("disciplinas.json");

        for (int i = 0; i < turmas.size(); i++) {
            System.out.println("\nTurma #" + (i + 1) + ":");
            Map<String, Object> turma = turmas.get(i);

            System.out.println("- Código da turma: " + turma.get("código da turma"));

            // Buscar e exibir nome do professor
            int codProf = ((Number) turma.get("código do professor")).intValue();
            String nomeProf = "Não encontrado";

            for (Map<String, Object> prof : professores) {
                if (codProf == ((Number) prof.get("código do professor")).intValue()) {
                    nomeProf = (String) prof.get("nome do professor");
                    break;
                }
            }

            System.out.println("- Professor: " + nomeProf + " (Código: " + codProf + ")");

            // Buscar e exibir nome da disciplina
            int codDisc = ((Number) turma.get("código da disciplina")).intValue();
            String nomeDisc = "Não encontrada";

            for (Map<String, Object> disc : disciplinas) {
                if (codDisc == ((Number) disc.get("código da disciplina")).intValue()) {
                    nomeDisc = (String) disc.get("nome da disciplina");
                    break;
                }
            }

            System.out.println("- Disciplina: " + nomeDisc + " (Código: " + codDisc + ")");
        }
    }

    private void atualizarTurma(Scanner scanner, List<Map<String, Object>> turmas) {
        System.out.println("\n===== ATUALIZAÇÃO DE TURMA =====");

        if (!CrudUtils.listarRegistros(turmas, "TURMAS")) {
            return;
        }

        try {
            System.out.print("\nInforme o código da turma a atualizar: ");
            int codigoTurma = Integer.parseInt(scanner.nextLine());

            Map.Entry<Integer, Map<String, Object>> resultado = CrudUtils.buscarRegistro(turmas, "código da turma", codigoTurma);

            if (resultado.getKey() >= 0) {
                Map<String, Object> turma = resultado.getValue();
                System.out.println("\nInforme os novos dados (deixe em branco para manter o valor atual):");

                // Atualizar código do professor
                int codProf = ((Number) turma.get("código do professor")).intValue();
                System.out.print("Código do professor [" + codProf + "]: ");
                String entradaProf = scanner.nextLine();

                if (!entradaProf.trim().isEmpty()) {
                    try {
                        int novoCodProf = Integer.parseInt(entradaProf);
                        if (MenuGenerico.verificarExistencia("professor", novoCodProf)) {
                            turma.put("código do professor", novoCodProf);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Mantendo valor original.");
                    }
                }

                // Atualizar código da disciplina
                int codDisc = ((Number) turma.get("código da disciplina")).intValue();
                System.out.print("Código da disciplina [" + codDisc + "]: ");
                String entradaDisc = scanner.nextLine();

                if (!entradaDisc.trim().isEmpty()) {
                    try {
                        int novoCodDisc = Integer.parseInt(entradaDisc);
                        if (MenuGenerico.verificarExistencia("disciplina", novoCodDisc)) {
                            turma.put("código da disciplina", novoCodDisc);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Mantendo valor original.");
                    }
                }

                if (GerenciadorArquivos.salvarDados("turmas.json", turmas)) {
                    System.out.println("Turma atualizada com sucesso!");
                } else {
                    System.out.println("Erro ao salvar os dados de turmas.");
                }
            } else {
                System.out.println("Turma não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }

    private void excluirTurma(Scanner scanner, List<Map<String, Object>> turmas) {
        System.out.println("\n===== EXCLUSÃO DE TURMA =====");

        if (!CrudUtils.listarRegistros(turmas, "TURMAS")) {
            return;
        }

        try {
            System.out.print("\nInforme o código da turma a excluir: ");
            int codigoTurma = Integer.parseInt(scanner.nextLine());

            // Verificar se há matrículas para esta turma
            List<Map<String, Object>> matriculas = GerenciadorArquivos.carregarDados("matriculas.json");

            for (Map<String, Object> matricula : matriculas) {
                if (codigoTurma == ((Number) matricula.get("código da turma")).intValue()) {
                    System.out.println("Esta turma possui matrículas. Exclua as matrículas primeiro.");
                    return;
                }
            }

            System.out.print("Tem certeza que deseja excluir esta turma? (S/N): ");
            String confirmacao = scanner.nextLine();

            if (confirmacao.equalsIgnoreCase("S")) {
                if (CrudUtils.excluirRegistro(turmas, "código da turma", codigoTurma)) {
                    if (GerenciadorArquivos.salvarDados("turmas.json", turmas)) {
                        System.out.println("Turma excluída com sucesso!");
                    } else {
                        System.out.println("Erro ao salvar os dados de turmas.");
                    }
                } else {
                    System.out.println("Turma não encontrada.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }
}

class MenuMatriculas {
    public void exibir() {
        Scanner scanner = new Scanner(System.in);
        List<Map<String, Object>> matriculas = GerenciadorArquivos.carregarDados("matriculas.json");

        while (true) {
            System.out.println("\n***** [MATRÍCULAS] MENU DE OPERAÇÕES *****");
            System.out.println("(1) Incluir.");
            System.out.println("(2) Listar.");
            System.out.println("(3) Atualizar.");
            System.out.println("(4) Excluir.");
            System.out.println("(9) Voltar ao menu principal.");

            System.out.print("\nInforme a ação desejada: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    incluirMatricula(scanner, matriculas);
                    break;
                case "2":
                    listarMatriculas(matriculas);
                    break;
                case "3":
                    atualizarMatricula(scanner, matriculas);
                    break;
                case "4":
                    excluirMatricula(scanner, matriculas);
                    break;
                case "9":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }

            MenuPrincipal.aguardarEnter(scanner);
        }
    }

    private void incluirMatricula(Scanner scanner, List<Map<String, Object>> matriculas) {
        System.out.println("\n===== INCLUSÃO DE MATRÍCULA =====");

        try {
            System.out.print("Informe o código da turma: ");
            int codigoTurma = Integer.parseInt(scanner.nextLine());
            if (!MenuGenerico.verificarExistencia("turma", codigoTurma)) {
                return;
            }

            System.out.print("Informe o código do estudante: ");
            int codigoEstudante = Integer.parseInt(scanner.nextLine());
            if (!MenuGenerico.verificarExistencia("estudante", codigoEstudante)) {
                return;
            }

            // Verificar se já existe esta matrícula
            for (Map<String, Object> matricula : matriculas) {
                if (codigoTurma == ((Number) matricula.get("código da turma")).intValue() &&
                        codigoEstudante == ((Number) matricula.get("código do estudante")).intValue()) {
                    System.out.println("Este estudante já está matriculado nesta turma.");
                    return;
                }
            }

            Map<String, Object> novaMatricula = new HashMap<>();
            novaMatricula.put("código da turma", codigoTurma);
            novaMatricula.put("código do estudante", codigoEstudante);

            matriculas.add(novaMatricula);
            if (GerenciadorArquivos.salvarDados("matriculas.json", matriculas)) {
                System.out.println("Matrícula realizada com sucesso!");
            } else {
                System.out.println("Erro ao salvar os dados de matrículas.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }

    private void listarMatriculas(List<Map<String, Object>> matriculas) {
        System.out.println("\n===== LISTAGEM DE MATRÍCULAS =====");

        if (matriculas.isEmpty()) {
            System.out.println("Não há matrículas realizadas.");
            return;
        }

        List<Map<String, Object>> estudantes = GerenciadorArquivos.carregarDados("estudantes.json");
        List<Map<String, Object>> turmas = GerenciadorArquivos.carregarDados("turmas.json");
        List<Map<String, Object>> disciplinas = GerenciadorArquivos.carregarDados("disciplinas.json");

        for (int i = 0; i < matriculas.size(); i++) {
            System.out.println("\nMatrícula #" + (i + 1) + ":");
            Map<String, Object> matricula = matriculas.get(i);

            // Buscar informações da turma
            int codTurma = ((Number) matricula.get("código da turma")).intValue();
            String discNome = "Não encontrada";

            for (Map<String, Object> turma : turmas) {
                if (codTurma == ((Number) turma.get("código da turma")).intValue()) {
                    int codDisc = ((Number) turma.get("código da disciplina")).intValue();

                    for (Map<String, Object> disc : disciplinas) {
                        if (codDisc == ((Number) disc.get("código da disciplina")).intValue()) {
                            discNome = (String) disc.get("nome da disciplina");
                            break;
                        }
                    }

                    System.out.println("- Turma: " + codTurma + " (Disciplina: " + discNome + ")");
                    break;
                }
            }

            // Buscar nome do estudante
            int codEst = ((Number) matricula.get("código do estudante")).intValue();
            String estNome = "Não encontrado";

            for (Map<String, Object> estudante : estudantes) {
                if (codEst == ((Number) estudante.get("código do estudante")).intValue()) {
                    estNome = (String) estudante.get("nome do estudante");
                    break;
                }
            }

            System.out.println("- Estudante: " + estNome + " (Código: " + codEst + ")");
        }
    }

    private void atualizarMatricula(Scanner scanner, List<Map<String, Object>> matriculas) {
        System.out.println("\n===== ATUALIZAÇÃO DE MATRÍCULA =====");

        if (!CrudUtils.listarRegistros(matriculas, "MATRÍCULAS")) {
            return;
        }

        try {
            System.out.println("\nInforme os dados da matrícula a atualizar:");
            System.out.print("Código da turma: ");
            int codTurmaAtual = Integer.parseInt(scanner.nextLine());

            System.out.print("Código do estudante: ");
            int codEstAtual = Integer.parseInt(scanner.nextLine());

            // Buscar a matrícula
            int idx = -1;
            for (int i = 0; i < matriculas.size(); i++) {
                Map<String, Object> m = matriculas.get(i);
                if (codTurmaAtual == ((Number) m.get("código da turma")).intValue() &&
                        codEstAtual == ((Number) m.get("código do estudante")).intValue()) {
                    idx = i;
                    break;
                }
            }

            if (idx >= 0) {
                System.out.println("\nInforme os novos dados (deixe em branco para manter o valor atual):");

                // Atualizar código da turma
                System.out.print("Novo código da turma [" + codTurmaAtual + "]: ");
                String entradaTurma = scanner.nextLine();

                if (!entradaTurma.trim().isEmpty()) {
                    try {
                        int novoCodTurma = Integer.parseInt(entradaTurma);
                        if (MenuGenerico.verificarExistencia("turma", novoCodTurma)) {
                            // Verificar se já existe esta matrícula
                            for (int i = 0; i < matriculas.size(); i++) {
                                if (i != idx) {
                                    Map<String, Object> m = matriculas.get(i);
                                    if (novoCodTurma == ((Number) m.get("código da turma")).intValue() &&
                                            codEstAtual == ((Number) m.get("código do estudante")).intValue()) {
                                        System.out.println("Este estudante já está matriculado nesta turma.");
                                        return;
                                    }
                                }
                            }

                            matriculas.get(idx).put("código da turma", novoCodTurma);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Mantendo valor original.");
                    }
                }

                // Atualizar código do estudante
                System.out.print("Novo código do estudante [" + codEstAtual + "]: ");
                String entradaEst = scanner.nextLine();

                if (!entradaEst.trim().isEmpty()) {
                    try {
                        int novoCodEst = Integer.parseInt(entradaEst);
                        if (MenuGenerico.verificarExistencia("estudante", novoCodEst)) {
                            // Verificar se já existe esta matrícula
                            int codTurma = ((Number) matriculas.get(idx).get("código da turma")).intValue();
                            for (int i = 0; i < matriculas.size(); i++) {
                                if (i != idx) {
                                    Map<String, Object> m = matriculas.get(i);
                                    if (codTurma == ((Number) m.get("código da turma")).intValue() &&
                                            novoCodEst == ((Number) m.get("código do estudante")).intValue()) {
                                        System.out.println("Este estudante já está matriculado nesta turma.");
                                        return;
                                    }
                                }
                            }

                            matriculas.get(idx).put("código do estudante", novoCodEst);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Mantendo valor original.");
                    }
                }

                if (GerenciadorArquivos.salvarDados("matriculas.json", matriculas)) {
                    System.out.println("Matrícula atualizada com sucesso!");
                } else {
                    System.out.println("Erro ao salvar os dados de matrículas.");
                }
            } else {
                System.out.println("Matrícula não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }

    private void excluirMatricula(Scanner scanner, List<Map<String, Object>> matriculas) {
        System.out.println("\n===== EXCLUSÃO DE MATRÍCULA =====");

        if (!CrudUtils.listarRegistros(matriculas, "MATRÍCULAS")) {
            return;
        }

        try {
            System.out.println("\nInforme os dados da matrícula a excluir:");
            System.out.print("Código da turma: ");
            int codTurma = Integer.parseInt(scanner.nextLine());

            System.out.print("Código do estudante: ");
            int codEst = Integer.parseInt(scanner.nextLine());

            // Buscar a matrícula
            int idx = -1;
            for (int i = 0; i < matriculas.size(); i++) {
                Map<String, Object> m = matriculas.get(i);
                if (codTurma == ((Number) m.get("código da turma")).intValue() &&
                        codEst == ((Number) m.get("código do estudante")).intValue()) {
                    idx = i;
                    break;
                }
            }

            if (idx >= 0) {
                System.out.print("Tem certeza que deseja excluir esta matrícula? (S/N): ");
                String confirmacao = scanner.nextLine();

                if (confirmacao.equalsIgnoreCase("S")) {
                    matriculas.remove(idx);
                    if (GerenciadorArquivos.salvarDados("matriculas.json", matriculas)) {
                        System.out.println("Matrícula excluída com sucesso!");
                    } else {
                        System.out.println("Erro ao salvar os dados de matrículas.");
                    }
                } else {
                    System.out.println("Operação cancelada.");
                }
            } else {
                System.out.println("Matrícula não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número inteiro válido.");
        }
    }
}