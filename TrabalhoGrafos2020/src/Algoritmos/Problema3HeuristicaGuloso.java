package Algoritmos;

import java.util.ArrayList;

//Importações
import Modelagem.Problema;
import Modelagem.Solucao;

/**
 * Classe Guloso Implementa a heurística gulosa do "vizinho mais próximo"
 *
 * Funcionamento: Consiste em pegar e adicionar a solução a cidade cuja
 * distância é a menor a partir da cidade atual
 *
 * Complexidade: O(n^2)
 *
 * @author Jonathan Douglas Diego Tavares
 */
public class Problema3HeuristicaGuloso {

	// Atributos
	private final Problema problema; // atributo que carrega a instância do problema
	private final Solucao solucao; // atributo que carrega a solução obtida ao final da solução do problema

	/**
	 * Construtor
	 *
	 * @param problema
	 */
	public Problema3HeuristicaGuloso(Problema problema) {
		this.problema = problema;
		solucao = new Solucao();
	}

	/**
	 * Calcula o melhor percurso partindo de uma determinada cidade inicial
	 *
	 * @param aeroportoInicial
	 */
	private void calculaMenorCaminho(int aeroportoInicial) {
		ArrayList<Integer> caminhoTemp = new ArrayList<>(); // carregará o melhor caminho obtido
		caminhoTemp.add(aeroportoInicial); // adiciona a cidade inicial ao início do percurso

		// utiliza o conceito de cidade atual (vértice atual) e
		// cidade mais próxima(vértice cuja aresta tem menor peso a partir do vértice
		// atual)
		int aeroportoAtual = aeroportoInicial;
		int aeroportoMaisProximo;
		int precoTotal = 0; // distancia total obtida no percurso
		int verticeDeMenorAresta; // vértice cujo peso é o menor a patir do vértice atual
		int nAeroportos = problema.getGrafo().numVertices(); // obtém a quantidade de cidades do problema

		// enquanto o caminho não houver a mesma quantidade de cidades do problema
		// inicial
		while (caminhoTemp.size() < nAeroportos) {
			// obtém a lista de adjacência do vértice atual (cidade atual)
			ArrayList<Integer> listaAdj = problema.getGrafo().listaDeAdjacencia(aeroportoAtual);

			// retira todas as cidades já percorridas da lista de adjacência
			for (Integer i : caminhoTemp) {
				listaAdj.remove(i);
			}

			// obtém a cidade mais próxima da cidade atual
			verticeDeMenorAresta = getVerticeComArestaDeMenorPeso(aeroportoAtual, listaAdj);
			aeroportoMaisProximo = verticeDeMenorAresta;

			// System.out.println("DistanciaEscolhida: " +
			// problema.getDistancia(cidadeAtual, cidadeMaisProxima));
			// incrementa a distância total
			precoTotal = precoTotal + problema.getPreco(aeroportoAtual, aeroportoMaisProximo);

			// adiciona a cidade mais próxima ao percurso
			caminhoTemp.add(aeroportoMaisProximo);

			// seta a cidade atual como a cidade mais próxima obtida na solução
			aeroportoAtual = aeroportoMaisProximo;
		}

		// adiciona o custo da volta da última cidade para a cidade inicial
		precoTotal = precoTotal + problema.getPreco(aeroportoAtual, caminhoTemp.get(0));
		// adiciona a volta para a cidade inicial
		caminhoTemp.add(aeroportoInicial);

		// seta o melhor percurso obtido
		solucao.setCaminho(caminhoTemp);
		// solucao.mostrarCaminho();
		// seta a distância total obtida para o melhor percurso
		solucao.setPrecoTotal(precoTotal);
	}

	/**
	 * Retorna a solução
	 *
	 * @param aeroportoInicial
	 * @return solução obtida após o calculo do melhor caminho
	 */
	public Solucao getSolucao(int aeroportoInicial) {
		calculaMenorCaminho(aeroportoInicial);
		return solucao;
	}

	/**
	 * Retorna a cidade mais próxima da cidade atual, ou seja, o vértice cuja
	 * aresta tem menor peso a partir do vértice atual
	 *
	 * @param aeroportoAtual
	 * @param lista
	 * @return vértice representando a cidade mais próxima da cidade atual
	 */
	private int getVerticeComArestaDeMenorPeso(int aeroportoAtual, ArrayList<Integer> lista) {
		int verticeEscolhido = lista.get(0);

		for (int i = 1; i < lista.size(); i++) {
			if (problema.getPreco(aeroportoAtual, lista.get(i)) < problema.getPreco(aeroportoAtual, verticeEscolhido)) {
				verticeEscolhido = lista.get(i);
			}
		}
		return verticeEscolhido;
	}
}
