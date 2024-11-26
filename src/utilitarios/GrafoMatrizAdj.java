package utilitarios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class GrafoMatrizAdj {
    private int matriz[][];
    private int numVertices;

    //Construtor
    public GrafoMatrizAdj(int numVertices) {
        this.matriz = new int[numVertices][numVertices];
        this.numVertices = numVertices;
    }

    //Adiciona aresta nao direcionado
    public void addAresta(int i, int j) {
        //Desconsiderar a linha e coluna 0
        i--;
        j--;

        if (i < 0 || j < 0 || i >= numVertices || j >=numVertices) {
            System.out.println("Vértices inválidos");
            return;
        } else {
            if(i==j){
                matriz[i][j] = 1; //Laço
                System.out.println("Aresta criada com sucesso");
            }
            else{
                matriz[i][j] ++;
                matriz[j][i] ++;
                System.out.println("Aresta criada com sucesso");
            }
        }
    }

    //Adiciona aresta direcionado
    public void addArestaDir(int destino, int origem) {
        //Desconsiderar a linha e coluna 0
        destino--;
        origem--;

        if (destino < 0 || origem < 0 || destino >= numVertices || origem >= numVertices) {
            System.out.println("Vértices inválidos");
            return;
        } else {
            matriz[origem][destino] ++;
            System.out.println("Aresta criada com sucesso");
        }
    }

    public void addArestaPonderada(int i, int j, int peso) {
        // Desconsiderar a linha e coluna 0
        i--;
        j--;

        if (i < 0 || j < 0 || i >= numVertices || j >= numVertices) {
            System.out.println("Vértices inválidos");
            return;
        } else {
            matriz[i][j] = peso;
            matriz[j][i] = peso;
            System.out.println("Aresta criada com sucesso");
        }
    }

    // Adiciona aresta direcionada ponderada
    public void addArestaDirPonderada(int origem, int destino, int peso) {
        // Desconsiderar a linha e coluna 0
        origem--;
        destino--;

        if (origem < 0 || destino < 0 || origem >= numVertices || destino >= numVertices) {
            System.out.println("Vértices inválidos");
            return;
        } else {
            matriz[origem][destino] = peso;
            System.out.println("Aresta criada com sucesso");
        }
    }

    //Remove aresta 
    public void removeAresta(int i, int j) {
        i--;
        j--;

        if( i >= numVertices || j >=numVertices ||matriz[i][j]==0){
            System.out.println("Essa aresta não existe\n");
        }
        else{
            matriz[i][j] = 0;
            matriz[j][i] = 0;
            System.out.println("Aresta removida!\n");
        }
        
    }

    // Mostra a representação da matriz 
     public void imprimizrMatriz() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
    //retorna os sucessores
    public List<Integer> getSucessores(int v) {
    List<Integer> sucessores = new ArrayList<>();
    for (int i = 0; i < numVertices; i++) {
        if (matriz[v][i] == 1) {
            sucessores.add(i);
        }
    }
    return sucessores;
    }
    //retorna os predecessores
    public List<Integer> getPredecessores(int v) {
    List<Integer> predecessores = new ArrayList<>();
    for (int i = 0; i < numVertices; i++) {
        if (matriz[i][v] == 1) {
            predecessores.add(i);
        }
    }
    return predecessores;
}
    public int getPesoAresta(int i, int j) {
        return matriz[i][j];
    }

    public int grauVertice(int vertice) {
        if (vertice <= 0 || vertice > numVertices) {
            return 0; // Retorna 0 se o vértice não existir
        }
        int grau = 0;
        for (int j = 0; j < numVertices; j++) {
            grau += matriz[vertice - 1][j];
        }
        return grau; // Grau é o número de arestas incidentes
    }

    public List<Integer> dijkstra(int origem, int destino) {
        Map<Integer, Integer> custos = new HashMap<>();
        Map<Integer, Integer> predecessores = new HashMap<>();
        PriorityQueue<int[]> filaPrioridade = new PriorityQueue<>((a, b) -> a[1] - b[1]);

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

            for (int vizinho = 1; vizinho <= numVertices; vizinho++) {
                if (matriz[vertice - 1][vizinho - 1] > 0) { // Verifica se há uma aresta entre os vértices
                    int novoCusto = custoAtual + matriz[vertice - 1][vizinho - 1];
                    if (novoCusto < custos.get(vizinho)) {
                        custos.put(vizinho, novoCusto);
                        predecessores.put(vizinho, vertice);
                        filaPrioridade.add(new int[]{vizinho, novoCusto});
                    }
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

    public Resultado kruskal() {
        List<Aresta> arestas = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (matriz[i][j] > 0) {
                    arestas.add(new Aresta(i, j, matriz[i][j]));
                }
            }
        }

        Collections.sort(arestas, Comparator.comparingInt(a -> a.peso));

        Conjunto[] conjuntos = new Conjunto[numVertices];
        for (int i = 0; i < numVertices; i++) {
            conjuntos[i] = new Conjunto(i, 0);
        }

        List<Aresta> arvoreGeradoraMinima = new ArrayList<>();
        int pesoTotal = 0;

        for (Aresta aresta : arestas) {
            int raizOrigem = encontrar(conjuntos, aresta.origem);
            int raizDestino = encontrar(conjuntos, aresta.destino);

            if (raizOrigem != raizDestino) {
                arvoreGeradoraMinima.add(aresta);
                pesoTotal += aresta.peso;
                unir(conjuntos, raizOrigem, raizDestino);
            }
        }

        return new Resultado(arvoreGeradoraMinima, pesoTotal);
    }

    public class Aresta {
        public int origem;
        public int destino;
        public int peso;

        public Aresta(int origem, int destino, int peso) {
            this.origem = origem;
            this.destino = destino;
            this.peso = peso;
        }
    }

    static class Conjunto {
        int pai, rank;

        public Conjunto(int pai, int rank) {
            this.pai = pai;
            this.rank = rank;
        }
    }

    static int encontrar(Conjunto[] conjuntos, int i) {
        if (conjuntos[i].pai != i) {
            conjuntos[i].pai = encontrar(conjuntos, conjuntos[i].pai);
        }
        return conjuntos[i].pai;
    }

    static void unir(Conjunto[] conjuntos, int x, int y) {
        int raizX = encontrar(conjuntos, x);
        int raizY = encontrar(conjuntos, y);

        if (conjuntos[raizX].rank < conjuntos[raizY].rank) {
            conjuntos[raizX].pai = raizY;
        } else if (conjuntos[raizX].rank > conjuntos[raizY].rank) {
            conjuntos[raizY].pai = raizX;
        } else {
            conjuntos[raizY].pai = raizX;
            conjuntos[raizX].rank++;
        }
    }

    public static class Resultado {
        public List<Aresta> arvoreGeradoraMinima;
        public int pesoTotal;

        public Resultado(List<Aresta> arvoreGeradoraMinima, int pesoTotal) {
            this.arvoreGeradoraMinima = arvoreGeradoraMinima;
            this.pesoTotal = pesoTotal;
        }
    }

    public int grauEntrada(int vertice) {
        if (vertice <= 0 || vertice > numVertices) {
            return 0; // Retorna 0 se o vértice não existir
        }
        int grauEntrada = 0;
        for (int j = 0; j < numVertices; j++) {
            grauEntrada += matriz[j][vertice - 1];
        }
        return grauEntrada; // Grau de entrada é o número de arestas que chegam ao vértice
    }

    public int grauSaida(int vertice) {
        if (vertice <= 0 || vertice > numVertices) {
            return 0; // Retorna 0 se o vértice não existir
        }
        int grauSaida = 0;
        for (int j = 0; j < numVertices; j++) {
            if (matriz[vertice - 1][j] > 0) {
                grauSaida++; // Incrementa o grau de saída para cada aresta saindo do vértice
            }
        }
        return grauSaida;
    }

    public boolean isGrafoRegularDirecionadoPonderado() {
        int grauSaidaReferencia = grauSaida(1); // Obtemos o grau de saída do primeiro vértice como referência
        int grauEntradaReferencia = grauEntrada(1); // Obtemos o grau de entrada do primeiro vértice como referência
        for (int vertice = 2; vertice <= numVertices; vertice++) {
            if (grauSaida(vertice) != grauSaidaReferencia || grauEntrada(vertice) != grauEntradaReferencia) {
                return false; // Se algum vértice tem um grau de saída ou de entrada diferente, o grafo não é regular
            }
        }
        return true; 
    }

    public void bfsPonderada(int vertice1) {
        // Verifica se o vértice fornecido é válido
        if (vertice1 < 1 || vertice1 > numVertices) {
            System.out.println("\nVértice inválido.");
            return;
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
            int verticeAtual = fila.poll();
            System.out.print(verticeAtual + " ");
    
            // Percorre todos os vizinhos do vértice atual
            for (int vizinho = 1; vizinho <= numVertices; vizinho++) {
                // Se houver uma aresta entre o vértice atual e o vizinho
                if (matriz[verticeAtual - 1][vizinho - 1] > 0) {
                    // Se o vizinho não foi visitado, marca-o como visitado e o adiciona à fila
                    if (!visitados[vizinho]) {
                        visitados[vizinho] = true;
                        fila.add(vizinho);
                    }
                }
            }
        }
        System.out.print("]");
    }
    
    public void dfsPonderada(int vertice1) {
        // Verifica se o vértice fornecido é válido
        if (vertice1 < 1 || vertice1 > numVertices) {
            System.out.println("\nVértice inválido.");
            return;
        }
    
        // Inicializa um array para controlar os vértices visitados
        boolean[] visitados = new boolean[numVertices + 1];
    
        // Chama o método auxiliar para realizar a DFS a partir do vértice fornecido
        System.out.print("\nA ordem da busca é: [ ");
        dfsUtilPonderada(vertice1, visitados);
        System.out.print("]");
    }
    
    private void dfsUtilPonderada(int vertice, boolean[] visitados) {
        // Marca o vértice atual como visitado e imprime
        visitados[vertice] = true;
        System.out.print(vertice + " ");
    
        // Itera sobre todos os vértices
        for (int vizinho = 1; vizinho <= numVertices; vizinho++) {
            // Se houver uma aresta entre o vértice atual e o vizinho
            if (matriz[vertice - 1][vizinho - 1] > 0) {
                // Se o vizinho não foi visitado, chama recursivamente a função para ele
                if (!visitados[vizinho]) {
                    dfsUtilPonderada(vizinho, visitados);
                }
            }
        }
    }
    
}
