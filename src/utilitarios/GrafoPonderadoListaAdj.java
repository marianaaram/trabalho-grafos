package utilitarios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

public class GrafoPonderadoListaAdj {

    private Map<Integer, List<Pair<Integer, Integer>>> adjListMap;

    public GrafoPonderadoListaAdj(int numVertices) {
        adjListMap = new HashMap<>();
        for (int i = 1; i <= numVertices; i++) {
            adjListMap.put(i, new ArrayList<>());
        }
    }

    // Adicionar aresta ponderada direcionada
    public void addArestaDirecionadaPonderada(int origem, int destino, int peso) {
        if (origem <= 0 || destino <= 0 || origem > adjListMap.size() || destino > adjListMap.size()) {
            return;
        }
        List<Pair<Integer, Integer>> srcList = adjListMap.get(origem);
        srcList.add(new Pair<>(destino, peso));
    }

    public void removeAresta(int i, int j) {
        if (i <= 0 || j <= 0 || i > adjListMap.size() || j > adjListMap.size()) {
            return;
        }
    
        List<Pair<Integer, Integer>> srcList = adjListMap.get(i);
        if (srcList != null) {
            for (Pair<Integer, Integer> pair : srcList) {
                if (pair.getKey() == j) {
                    srcList.remove(pair); // Remove o par (vértice, peso) da lista de adjacência do vértice i
                    break;
                }
            }
        }
    
        List<Pair<Integer, Integer>> destList = adjListMap.get(j);
        if (destList != null) {
            for (Pair<Integer, Integer> pair : destList) {
                if (pair.getKey() == i) {
                    destList.remove(pair); // Remove o par (vértice, peso) da lista de adjacência do vértice j
                    break;
                }
            }
        }
    }

    public void imprimirLista() {
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> entry : adjListMap.entrySet()) {
            int vertice = entry.getKey();
            List<Pair<Integer, Integer>> vizinhos = entry.getValue();
            System.out.print("Vértice " + vertice + " está conectado a: ");
            for (Pair<Integer, Integer> vizinho : vizinhos) {
                System.out.print("(" + vizinho.getKey() + ", peso " + vizinho.getValue() + ") ");
            }
            System.out.println();
        }
    }

    public boolean isGrafoConexoDirecionado() {
        if (adjListMap.isEmpty()) {
            return false;
        }
    
        boolean[] visitados = new boolean[adjListMap.size() + 1];
        dfs(1, visitados);
    
        // Verifica se todos os vértices foram visitados na DFS
        for (int i = 1; i <= adjListMap.size(); i++) {
            if (!visitados[i]) {
                return false; // Se algum vértice não foi visitado, o grafo não é conexo
            }
        }
        return true;
    }

    public boolean isGrafoSimples() {
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> entry : adjListMap.entrySet()) {
            int vertice = entry.getKey();
            List<Pair<Integer, Integer>> vizinhos = entry.getValue();
            
            // Verificar se há laços
            for (Pair<Integer, Integer> vizinho : vizinhos) {
                if (vizinho.getKey() == vertice) {
                    return false;
                }
            }
            
            // Verificar se há múltiplas arestas
            Map<Integer, Integer> countMap = new HashMap<>();
            for (Pair<Integer, Integer> vizinho : vizinhos) {
                countMap.put(vizinho.getKey(), countMap.getOrDefault(vizinho.getKey(), 0) + 1);
            }
            for (int count : countMap.values()) {
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isArestaDirecionada(int origem, int destino) {
        if (origem <= 0 || destino <= 0 || origem > adjListMap.size() || destino > adjListMap.size()) {
            return false;
        }
        List<Pair<Integer, Integer>> srcList = adjListMap.get(origem);
        for (Pair<Integer, Integer> aresta : srcList) {
            if (aresta.getKey() == destino) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isGrafoCompletoDirecionado() {
        // Verifica se cada par de vértices distintos possui uma aresta direcionada
        for (int i = 1; i <= adjListMap.size(); i++) {
            for (int j = 1; j <= adjListMap.size(); j++) {
                if (i != j && !isArestaDirecionada(i, j)) {
                    return false; // Se não houver uma aresta direcionada de i para j, o grafo não é completo
                }
            }
        }
        return true; 
    }
    
    private void dfs(int vertice, boolean[] visitados) {
        visitados[vertice] = true;
        for (Pair<Integer, Integer> vizinho : adjListMap.get(vertice)) {
            int verticeVizinho = vizinho.getKey();
            if (!visitados[verticeVizinho]) {
                dfs(verticeVizinho, visitados);
            }
        }
    }

    public boolean isGrafoBipartidoDirecionado() {
        // Inicializa o array para armazenar os conjuntos de vértices
        int[] conjuntos = new int[adjListMap.size() + 1];
        
        // Executa a busca em profundidade (DFS) para atribuir os conjuntos aos vértices
        for (int i = 1; i <= adjListMap.size(); i++) {
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
        for (Pair<Integer, Integer> aresta : adjListMap.get(vertice)) {
            int vizinho = aresta.getKey();
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

    public List<Integer> ordenacaoTopologica() {
    Stack<Integer> ordenacaoTopologica = new Stack<>();
    boolean[] visitados = new boolean[adjListMap.size() + 1];

    // Percorre todos os vértices do grafo
    for (int i = 1; i <= adjListMap.size(); i++) {
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

    // Itera sobre as arestas do vértice
    for (Pair<Integer, Integer> aresta : adjListMap.get(vertice)) {
        int vizinho = aresta.getKey();
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
    for (int i = 1; i <= adjListMap.size(); i++) {
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

        for (Pair<Integer, Integer> aresta : adjListMap.get(vertice)) {
            int vizinho = aresta.getKey();
            int pesoAresta = aresta.getValue();
            int novoCusto = custoAtual + pesoAresta;

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

    public List<Integer> getSucessores(int v) {
        List<Integer> sucessores = new ArrayList<>();
        for (Pair<Integer, Integer> vizinho : adjListMap.get(v)) {
            sucessores.add(vizinho.getKey());
        }
        return sucessores;
    }

    // Método para retornar os predecessores de um vértice
    public List<Integer> getPredecessores(int v) {
        List<Integer> predecessores = new ArrayList<>();
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> entry : adjListMap.entrySet()) {
            int vertice = entry.getKey();
            List<Pair<Integer, Integer>> vizinhos = entry.getValue();
            for (Pair<Integer, Integer> vizinho : vizinhos) {
                if (vizinho.getKey() == v) {
                    predecessores.add(vertice);
                    break; // Parar de procurar depois de encontrar um predecessor
                }
            }
        }
        return predecessores;
    }

    public boolean isAresta(int origem, int destino) {
        List<Pair<Integer, Integer>> vizinhos = adjListMap.get(origem);
        for (Pair<Integer, Integer> vizinho : vizinhos) {
            if (vizinho.getKey() == destino) {
                return true; // Aresta encontrada
            }
        }
        return false; // Aresta não encontrada
    }
    
    public boolean isGrafoCompleto() {
        // Verifica se cada vértice está conectado a todos os outros vértices
        for (int i = 1; i <= adjListMap.size(); i++) {
            for (int j = 1; j <= adjListMap.size(); j++) {
                if (i != j && !isAresta(i, j)) {
                    return false; // Se não houver uma aresta entre dois vértices diferentes, o grafo não é completo
                }
            }
        }
        return true; // Se passar por todos os pares de vértices e houver uma aresta entre eles, o grafo é completo
    }
}

class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}