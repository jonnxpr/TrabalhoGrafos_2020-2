/**
 * Trabalho Pr�tico - Classe Grafo
 *
 * @author Jonathan Douglas Diego Tavares
 * @matricula 540504
 * @disciplina Algortimos em Grafos
 * @professor Alexei Machado
 */
package Modelagem;

//Importações
import java.util.ArrayList;

/**
 * Classe Grafo
 *
 * @author Jonathan Douglas Diego Tavares
 */
public class Grafo {

	// Atributos
	private String nomeGrafo;
	private final int numeroVertices; // aka numero de aeroportos
	private Aresta[][] matrizAdjacencia;

	/**
	 * Método construtor, responsável por inicializar o número de vértices e a
	 * matriz de adjacência
	 *
	 * @param vertices quantidade de vértices do grafo
	 */
	public Grafo(int vertices) {
		numeroVertices = vertices;
		matrizAdjacencia = new Aresta[numeroVertices][numeroVertices];
		inicializaMatrizAdj();
	}

	/**
	 * Método existeAresta, responsável por verificar se existe determinada aresta
	 * na matriz de adjacência
	 *
	 * @param vertice1
	 * @param vertice2
	 * @return true se existir, false caso não exista
	 */
	public boolean existeAresta(int vertice1, int vertice2) {
		return matrizAdjacencia[vertice1][vertice2] != null;
	}

	/**
	 * Método getPeso, responsável por retornar o peso de uma aresta.
	 *
	 * @param vertice1
	 * @param vertice2
	 * @return o peso de determinada aresta contida na matriz
	 */
	public int getAltitude(int vertice1, int vertice2) {
		return this.matrizAdjacencia[vertice1][vertice2].getAltitude();
	}

	public double getDistancia(int vertice1, int vertice2) {
		return (this.matrizAdjacencia[vertice1][vertice2] != null)
				? this.matrizAdjacencia[vertice1][vertice2].getDistancia()
				: -1;
	}

	public Aresta[][] getMatriz() {
		return this.matrizAdjacencia;
	}

	public String getNomeGrafo() {
		return nomeGrafo;
	}

	public int getPreco(int vertice1, int vertice2) {
		//System.out.println("V1 = " + vertice1 + "\nV2 =" + vertice2);
		return this.matrizAdjacencia[vertice1][vertice2].getPreco();
	}

	/**
	 * Método imprimeMatrizAdj, responsável por realizar a impressão da matriz de
	 * adjacência
	 */
	public void imprimeMatrizAdj() {
		System.out.println("Matriz de adjacência:");
		for (int i = 0; i < numeroVertices; i++) {
			for (int j = 0; j < numeroVertices; j++) {
				if (matrizAdjacencia[i][j] != null) {
					System.out.print(matrizAdjacencia[i][j].getV1().getLabelVertice()
							+ matrizAdjacencia[i][j].getV2().getLabelVertice() + "                 ");
				} else {
					System.out.print("-1                 ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Método auxiliar para colocar o valor 0 nas posições da matriz de
	 * adjacência.
	 */
	private void inicializaMatrizAdj() {
		for (int i = 0; i < numeroVertices; i++) {
			for (int j = 0; j < numeroVertices; j++) {
				matrizAdjacencia[i][j] = null;
			}
		}
	}

	/**
	 * Método insereAresta, responsável por inserir uma aresta na matriz de
	 * adjacência
	 *
	 * @param vertice1 vértice linha
	 * @param vertice2 vértice coluna
	 * @param peso     peso da aresta
	 */
	public void insereAresta(Vertice vertice1, Vertice vertice2, int altitude, double distancia, int preco) {
		// adiciona na posição [v1][v2] o valor do peso correspondente a aresta
		// adicionada
		matrizAdjacencia[vertice1.getValorRepresentativo()][vertice2.getValorRepresentativo()] = new Aresta(vertice1,
				vertice2, altitude, distancia, preco);
	}

	/**
	 * Método insereArestaNaoOrientada, responsável por inserir uma aresta não
	 * orientada na matriz de adjacência
	 *
	 * @param vertice1
	 * @param vertice2
	 * @param peso
	 */
	public void insereArestaNaoOrientada(Vertice vertice1, Vertice vertice2, int altitude, double distancia,
			int preco) {
		insereAresta(vertice1, vertice2, altitude, distancia, preco);
		insereAresta(vertice2, vertice1, altitude, distancia, preco);
	}

	/**
	 * Método listaDeAdjacencia, responsável por retornar a lista de adjacência
	 * obtida a partir da matriz
	 *
	 * @param vertice1
	 * @return ArrayList contendo as adjacências de determinado vértice
	 */
	public ArrayList<Integer> listaDeAdjacencia(int vertice1) {
		ArrayList<Integer> listaAdj = new ArrayList<>();

		for (int i = 0; i < numeroVertices; i++) {
			if (matrizAdjacencia[vertice1][i] != null) {
				listaAdj.add(i);
			}
		}

		return listaAdj;
	}

	/**
	 * Método numVertices, responsável por retornar a quantidade de vértices no
	 * grafo
	 *
	 * @return quantidade de vértices no grafo
	 */
	public int numVertices() {
		return this.numeroVertices;
	}

	/**
	 * Seta a matriz de adjacência com a matriz passada pelo parâmetro
	 *
	 * @param matriz
	 */
	public void setMatriz(Aresta[][] matriz) {
		matrizAdjacencia = matriz;
	}

	public void setNomeGrafo(String nomeGrafo) {
		this.nomeGrafo = nomeGrafo;
	}
}
