package utilitarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class GrafoListaAdj {

    private static int numVertices;
    public static Map<Integer, List<Integer>> adjListMap;
    private int[] color = new int[1000];
    private Stack<Integer> pilha;

    //Construtor 
    public GrafoListaAdj(int vertices) {
        numVertices = vertices;
        adjListMap = new HashMap<Integer, List<Integer>>();
        for (int i = 1; i <= vertices ; i++) {
            adjListMap.put(i, new LinkedList<Integer>());
        }
        this.pilha = new Stack<>();
    }

    //Adicionar aresta nao direcionada
    public void addAresta(int i, int j) {
        if (i > adjListMap.size() || j > adjListMap.size()) {
            return;
        }
        List<Integer> srcList = adjListMap.get(i);
        srcList.add(j);
        List<Integer> destList = adjListMap.get(j);
        destList.add(i);
    }

    //Adicionar aresta direcionada 
    public void addArestaDirecionada(int origem, int destino) {
        if (origem > adjListMap.size() || destino > adjListMap.size()) {
            return;
        }
        List<Integer> srcList = adjListMap.get(origem);
        srcList.add(destino);
    }

    public void addArestaPonderada(int i, int j, int peso) {
        if (i > adjListMap.size() || j > adjListMap.size()) {
            return;
        }
        List<Integer> srcList = adjListMap.get(i);
        srcList.add(j); // Adiciona o vértice j à lista de adjacência do vértice i
        List<Integer> destList = adjListMap.get(j);
        destList.add(i); // Se o grafo for não direcionado, adicione o vértice i à lista de adjacência do vértice j
        // Aqui você pode armazenar o peso da aresta em uma estrutura de dados adequada se precisar utilizá-lo posteriormente
    }


    //Remover aresta 
    public void removeAresta(int i, int j) {
        if (i > adjListMap.size() || j > adjListMap.size()) {
           return;
        }

        List<Integer> srcList = adjListMap.get(i);
        srcList.remove(Integer.valueOf(j)); // Remove o vértice j da lista de adjacência do vértice i
        List<Integer> destList = adjListMap.get(j);
        destList.remove(Integer.valueOf(i)); // Remove o vértice i da lista de adjacência do vértice j

    }

    //?
    public boolean isAresta(int i, int j) {
        if (i > adjListMap.size() || j > adjListMap.size()) {
            return false;
        }
        List<Integer> srcList = adjListMap.get(i);
        return srcList.contains(j);
    }

    //Imprime a lista
    public void imprimirLista() {
        for (Map.Entry<Integer, List<Integer>> entry : adjListMap.entrySet()) {
            int vertice = entry.getKey();
            List<Integer> vizinhos = entry.getValue();
            System.out.print("Vértice " + vertice + " está conectado a: ");
            for (int vizinho : vizinhos) {
                System.out.print(vizinho + " ");
            }
            System.out.println();
        }
    }

    //Obter a vizinhança de um vértice
    public List<Integer> vizinhanca(int vertice) {
        if (!adjListMap.containsKey(vertice)) {
            return new LinkedList<Integer>(); // Retorna uma lista vazia se o vértice não existir
        }
        return adjListMap.get(vertice);
    }

    //Obter o grau de um vértice nao direcionado
    public int grauVertice(int vertice) {
        if (!adjListMap.containsKey(vertice)) {
            return 0; // Retorna 0 se o vértice não existir
        }
        return adjListMap.get(vertice).size(); //Grau de saida
    }

    // Método para verificar se há uma aresta direcionada do vértice origem para o vértice destino
    public boolean isArestaDirecionada(int origem, int destino) {
        if (origem > adjListMap.size() || destino > adjListMap.size()) {
            return false;
        }
        List<Integer> srcList = adjListMap.get(origem);
        return srcList.contains(destino);
    }

    // Método para obter o grau de saída de um vértice em um grafo direcionado
    public int grauSaida(int vertice) {
        if (!adjListMap.containsKey(vertice)) {
            return 0; // Retorna 0 se o vértice não existir
        }
        return adjListMap.get(vertice).size(); // Grau de saída é o número de arestas saindo do vértice
    }

    // Método para obter o grau de entrada de um vértice em um grafo direcionado
    public int grauEntrada(int vertice) {
        if (!adjListMap.containsKey(vertice)) {
            return 0; // Retorna 0 se o vértice não existir
        }
        int grauEntrada = 0;
        for (Map.Entry<Integer, List<Integer>> entry : adjListMap.entrySet()) {
            List<Integer> vizinhos = entry.getValue();
            if (vizinhos.contains(vertice)) {
                grauEntrada++; // Incrementa o grau de entrada para cada vértice que tem uma aresta entrando nele
            }
        }
        return grauEntrada;
    }
    //retorna os sucessores
    public List<Integer> getSucessores(int v) {
    return new ArrayList<>(adjListMap.get(v));
        }
    //retorna os predecessores    
    public List<Integer> getPredecessores(int v) {
    List<Integer> predecessores = new ArrayList<>();
    for (Map.Entry<Integer, List<Integer>> entry : adjListMap.entrySet()) {
        if (entry.getValue().contains(v)) {
            predecessores.add(entry.getKey());
        }
    }
    return predecessores;
    }
    
    //TESTES DO GRAFO

    //Grafo simples
    public boolean isGrafoSimples() {
        for (Map.Entry<Integer, List<Integer>> entry : adjListMap.entrySet()) {
            int vertice = entry.getKey();
            List<Integer> vizinhos = entry.getValue();
            // Verificar se há laços
            if (vizinhos.contains(vertice)) {
                return false;
            }
            // Verificar se há múltiplas arestas
            Map<Integer, Integer> countMap = new HashMap<>();
            for (int vizinho : vizinhos) {
                countMap.put(vizinho, countMap.getOrDefault(vizinho, 0) + 1);
            }
            for (int count : countMap.values()) {
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //Grafo é regular
    public boolean isGrafoRegular() {
        int grauReferencia;
         grauReferencia = grauVertice(1); // Obtemos o grau do primeiro vértice como referência
        for (int vertice = 1; vertice <= numVertices; vertice++) {
            if (grauVertice(vertice) != grauVertice(grauReferencia)) {
                return false; // Se algum vértice tem um grau diferente, o grafo não é regular
            }
        }
        return true; // Se chegarmos até aqui, todos os vértices têm o mesmo grau
    }

    // Grafo é completo
    public boolean isGrafoCompleto() {
        // Verifica se cada vértice está conectado a todos os outros vértices
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (i != j && !isAresta(i, j)) {
                    return false; // Se não houver uma aresta entre dois vértices diferentes, o grafo não é completo
                }
            }
        }
        return true; // Se passar por todos os pares de vértices e houver uma aresta entre eles, o grafo é completo
    }

     
    // Grafo é bipartido
    public boolean Ebipartido(){ 
        // Inicializa o array de cores com valores padrão
        Arrays.fill(color, -1);
    
        // Itera sobre todos os vértices do grafo
        for (int i = 1; i <= numVertices; i++) {
            // Verifica se o vértice i ainda não foi visitado
            if (color[i] == -1) {
                // Inicia a coloração do componente conectado
                if (!bipartidoUtil(i)) {
                    // Se encontrar um componente não bipartido, retorna false
                    return false;
                }
            }
        }
        // Se nenhum componente não bipartido foi encontrado, retorna true
        return true;
    }
    // Função utilitária para colorir os vértices e verificar se o grafo é bipartido
    private boolean bipartidoUtil(int src) {  
        // Inicializa uma fila para realizar a busca em largura
        Queue<Integer> fila = new LinkedList<>();
        // Adiciona o vértice inicial na fila
        fila.add(src);
        // Atribui a cor 1 ao vértice inicial
        color[src] = 1;
    
        // Enquanto a fila não estiver vazia
        while (!fila.isEmpty()) {
            // Remove o primeiro vértice da fila
            int u = fila.poll();
            // Itera sobre todos os vértices adjacentes a u
            for (int v : adjListMap.get(u)) {
                // Verifica se o vértice v não foi visitado
                if (color[v] == -1) {
                    // Atribui a cor oposta ao vértice adjacente v
                    color[v] = 1 - color[u];
                    // Adiciona o vértice v na fila
                    fila.add(v);
                } else if (color[v] == color[u]) {
                    // Se o vértice adjacente v tiver a mesma cor que u, o grafo não é bipartido
                    return false;
                }
            }
        }
        // Se nenhum conflito foi encontrado, o grafo é bipartido
        return true;
    }
        





    //GRAFO DIRECIONADO
    // Grafo direcionado é regular
    public boolean isGrafoRegularDirecionado() {
        int grauSaidaReferencia = grauSaida(1); // Obtemos o grau de saída do primeiro vértice como referência
        int grauEntradaReferencia = grauEntrada(1); // Obtemos o grau de entrada do primeiro vértice como referência
        for (int vertice = 2; vertice <= numVertices; vertice++) {
            if (grauSaida(vertice) != grauSaidaReferencia || grauEntrada(vertice) != grauEntradaReferencia) {
                return false; // Se algum vértice tem um grau de saída ou de entrada diferente, o grafo não é regular
            }
        }
        return true; 
    }

    //Grafo direcionado é completo
    public boolean isGrafoCompletoDirecionado() {
        // Verifica se cada par de vértices distintos possui uma aresta direcionada
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (i != j && !isArestaDirecionada(i, j)) {
                    return false; // Se não houver uma aresta direcionada de i para j, o grafo não é completo
                }
            }
        }
        return true; 
    }

    // Grafo direcionado é bipartido
    public boolean isGrafoBipartidoDirecionado() {
        // Inicializa o array para armazenar os conjuntos de vértices
        int[] conjuntos = new int[numVertices + 1];
        
        // Executa a busca em profundidade (DFS) para atribuir os conjuntos aos vértices
        for (int i = 1; i <= numVertices; i++) {
            if (conjuntos[i] == 0 && !dfsBipartidoDirecionado(i, 1, conjuntos)) {
                return false; // Se um conjunto inválido for encontrado, o grafo não é bipartido
            }
        }
        // Se chegarmos até aqui, o grafo é bipartido
        return true;
    }

    // Função auxiliar para a busca em profundidade (DFS) em um grafo direcionado bipartido
    private boolean dfsBipartidoDirecionado(int vertice, int conjunto, int[] conjuntos) {
        // Atribui o conjunto atual ao vértice
        conjuntos[vertice] = conjunto;
        
        // Percorre os vizinhos do vértice
        for (int vizinho : adjListMap.get(vertice)) {
            // Se o vizinho já estiver no mesmo conjunto, o grafo não é bipartido
            if (conjuntos[vizinho] == conjunto) {
                return false;
            }
            // Se o vizinho ainda não tiver conjunto atribuído, chama recursivamente a DFS com o conjunto oposto
            else if (conjuntos[vizinho] == 0 && !dfsBipartidoDirecionado(vizinho, -conjunto, conjuntos)) {
                return false;
            }
        }
        return true;
    }






    //BUSCAS 

    //Metdo para Busca em Largura 
    public static void bfs(int vertice1) {

        //Caso o usuario digite um vertice que não está no grafo 
        if (vertice1 < 1 || vertice1 > numVertices) {
            System.out.println("\nVértice inválido.");
            return; // Retorna imediatamente se o vértice for inválido
        }
        
        // Inicializa um array para controlar os vértices visitados
        boolean[] visitados = new boolean[numVertices + 1];

        // Inicializa uma fila para a busca em largura
        Queue<Integer> fila = new LinkedList<>();

        // Marca o vértice inicial como visitado e o adiciona à fila
        visitados[vertice1] = true;
        fila.add(vertice1);

        // Enquanto a fila não estiver vazia
        System.out.print("\nA ordem da busca é: [ ");
        while (!fila.isEmpty()) {
            // Remove o vértice da fila e imprime seu valor
            int vertice_atual = fila.poll();
            System.out.print(vertice_atual + " ");

            // Percorre todos os vizinhos do vértice atual
            for (int vizinho : adjListMap.get(vertice_atual)) {
                // Se o vizinho não foi visitado, marca-o como visitado e o adiciona à fila
                if (!visitados[vizinho]) {
                    visitados[vizinho] = true;
                    fila.add(vizinho);
                }
            }
        }
        System.out.print("]");
    }
    

    //Busca em prufudidade 
    public void dfs(int vertice1) {
        boolean[] visitados = new boolean[numVertices + 1];
        System.out.print("\nA ordem da busca é: [ ");
        dfsUtil(vertice1, visitados);
        System.out.print("]");
    }

    private static void dfsUtil(int vertice, boolean[] visitados) {
        // Marca o vértice atual como visitado e imprime
        visitados[vertice] = true;
        System.out.print(vertice + " ");

        // Itera sobre os vizinhos do vértice atual
        for (int vizinho : adjListMap.get(vertice)) {
            // Se o vizinho não foi visitado, chama recursivamente a função para ele
            if (!visitados[vizinho]) {
                dfsUtil(vizinho, visitados);
            }
        }
    }




    //GRAFO CONEXO 
    public boolean isGrafoConexoNaoDirecionado() {
        if (numVertices <= 0) {
            return false;
        }
        boolean[] visitados = new boolean[numVertices + 1];
        bfs(1, visitados);
        for (int i = 1; i <= numVertices; i++) {
            if (!visitados[i]) {
                return false; 
            }
        }
        return true;
    }
    
    private void bfs(int vertice, boolean[] visitados) {
        Queue<Integer> fila = new LinkedList<>();
        fila.add(vertice);
        visitados[vertice] = true;

        while (!fila.isEmpty()) {
            int verticeAtual = fila.poll();
            for (int vizinho : adjListMap.get(verticeAtual)) {
                if (!visitados[vizinho]) {
                    visitados[vizinho] = true;
                    fila.add(vizinho);
                }
            }
        }
    }

    //Direcionado
    public boolean isGrafoConexoDirecionado() {
        if (numVertices <= 0) {
            return false;
        }
        boolean[] visitados = new boolean[numVertices + 1];
        dfs(1, visitados);
        for (int i = 1; i <= numVertices; i++) {
            if (!visitados[i]) {
                return false; 
            }
        }
        return true;
    }
    
    private void dfs(int vertice, boolean[] visitados) {
        visitados[vertice] = true;
        for (int vizinho : adjListMap.get(vertice)) {
            if (!visitados[vizinho]) {
                dfs(vizinho, visitados);
            }
        }
    }

    public List<Integer> ordenacaoTopologica() {
        Stack<Integer> ordenacaoTopologica = new Stack<>();
        boolean[] visitados = new boolean[numVertices + 1];

        // Percorre todos os vértices do grafo
        for (int i = 1; i <= numVertices; i++) {
            if (!visitados[i]) {
                dfs(i, visitados, ordenacaoTopologica);
            }
        }

        // Converte a pilha em uma lista para retornar a ordem topológica
        List<Integer> ordem = new ArrayList<>();
        while (!ordenacaoTopologica.isEmpty()) {
            ordem.add(ordenacaoTopologica.pop());
        }
        return ordem;
    }

    // Função de busca em profundidade (DFS) modificada para ordenação topológica
    private void dfs(int vertice, boolean[] visitados, Stack<Integer> ordenacaoTopologica) {
        visitados[vertice] = true;

        // Itera sobre os vizinhos do vértice
        for (int vizinho : adjListMap.get(vertice)) {
            if (!visitados[vizinho]) {
                dfs(vizinho, visitados, ordenacaoTopologica);
            }
        }

        // Após visitar todos os vizinhos, adiciona o vértice à pilha
        ordenacaoTopologica.push(vertice);
    }

    public List<Integer> dijkstra(int origem, int destino) {
        Map<Integer, Integer> custos = new HashMap<>();
        Map<Integer, Integer> predecessores = new HashMap<>();
        PriorityQueue<int[]> filaPrioridade = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        for (int i = 1; i <= numVertices; i++) {
            custos.put(i, Integer.MAX_VALUE);
        }
        custos.put(origem, 0);
        filaPrioridade.add(new int[]{origem, 0});

        while (!filaPrioridade.isEmpty()) {
            int[] verticeAtual = filaPrioridade.poll();
            int vertice = verticeAtual[0];
            int custoAtual = verticeAtual[1];

            if (custoAtual > custos.get(vertice)) {
                continue;
            }

            for (int vizinho : adjListMap.get(vertice)) {
                int novoCusto = custoAtual + 1; 
                
                if (novoCusto < custos.get(vizinho)) {
                    custos.put(vizinho, novoCusto);
                    predecessores.put(vizinho, vertice);
                    filaPrioridade.add(new int[]{vizinho, novoCusto});
                }
            }
        }
        
        List<Integer> caminho = new ArrayList<>();
        int vertice = destino;
        while (predecessores.containsKey(vertice)) {
            caminho.add(0, vertice);
            vertice = predecessores.get(vertice);
        }
        if (vertice == origem) {
            caminho.add(0, vertice);
            return caminho;
        } else {
            return Collections.emptyList();
        }
    }
}