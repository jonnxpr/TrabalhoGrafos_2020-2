package Modelagem;

/**
 * Classe Problema
 *
 * @author Jonathan Douglas Diego Tavares
 */
public class Problema {

	// Atributos
	private final Grafo grafo;

	/**
	 * Construtor
	 *
	 * @param grafo
	 */
	public Problema(Grafo grafo) {
		this.grafo = grafo;
	}

	/**
	 * Retorna o grafo que representa a instância do problema
	 *
	 * @return grafo representando a instância do problema
	 */
	public Grafo getGrafo() {
		return this.grafo;
	}

	/**
	 * Retorna o peso da aresta entre dois vértices do grafo
	 *
	 * @param inicio
	 * @param fim
	 * @return peso de uma aresta entre dois vértices
	 */
	public int getDistancia(int inicio, int fim) {
		return this.grafo.getPeso(inicio, fim);
	}
}
