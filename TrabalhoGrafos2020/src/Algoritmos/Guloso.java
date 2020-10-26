package Algoritmos;

//Importações
import Modelagem.Problema;
import Modelagem.Solucao;
import java.util.ArrayList;

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
public class Guloso {

	// Atributos
	private final Problema problema; // atributo que carrega a instância do problema
	private final Solucao solucao; // atributo que carrega a solução obtida ao final da solução do problema

	/**
	 * Construtor
	 *
	 * @param problema
	 */
	public Guloso(Problema problema) {
		this.problema = problema;
		solucao = new Solucao();
	}

	/**
	 * Retorna a cidade mais próxima da cidade atual, ou seja, o vértice cuja
	 * aresta tem menor peso a partir do vértice atual
	 *
	 * @param cidadeAtual
	 * @param lista
	 * @return vértice representando a cidade mais próxima da cidade atual
	 */
	private int getVerticeComArestaDeMenorPeso(int cidadeAtual, ArrayList<Integer> lista) {
		int verticeEscolhido = lista.get(0);

		for (int i = 1; i < lista.size(); i++) {
			if (problema.getDistancia(cidadeAtual, lista.get(i)) < problema.getDistancia(cidadeAtual,
					verticeEscolhido)) {
				verticeEscolhido = lista.get(i);
			}
		}
		return verticeEscolhido;
	}

	/**
	 * Calcula o melhor percurso partindo de uma determinada cidade inicial
	 *
	 * @param cidadeInicial
	 */
	private void calculaMenorCaminho(int cidadeInicial) {
		ArrayList<Integer> caminhoTemp = new ArrayList<>(); // carregará o melhor caminho obtido
		caminhoTemp.add(cidadeInicial); // adiciona a cidade inicial ao início do percurso

		// utiliza o conceito de cidade atual (vértice atual) e
		// cidade mais próxima(vértice cuja aresta tem menor peso a partir do vértice
		// atual)
		int cidadeAtual = cidadeInicial;
		int cidadeMaisProxima;
		int distanciaTotal = 0; // distancia total obtida no percurso
		int verticeDeMenorAresta; // vértice cujo peso é o menor a patir do vértice atual
		int nCidades = problema.getGrafo().numVertices(); // obtém a quantidade de cidades do problema

		// enquanto o caminho não houver a mesma quantidade de cidades do problema
		// inicial
		while (caminhoTemp.size() < nCidades) {
			// obtém a lista de adjacência do vértice atual (cidade atual)
			ArrayList<Integer> listaAdj = problema.getGrafo().listaDeAdjacencia(cidadeAtual);

			// retira todas as cidades já percorridas da lista de adjacência
			for (Integer i : caminhoTemp) {
				listaAdj.remove(i);
			}

			// obtém a cidade mais próxima da cidade atual
			verticeDeMenorAresta = getVerticeComArestaDeMenorPeso(cidadeAtual, listaAdj);
			cidadeMaisProxima = verticeDeMenorAresta;

			// System.out.println("DistanciaEscolhida: " +
			// problema.getDistancia(cidadeAtual, cidadeMaisProxima));
			// incrementa a distância total
			distanciaTotal = distanciaTotal + problema.getDistancia(cidadeAtual, cidadeMaisProxima);

			// adiciona a cidade mais próxima ao percurso
			caminhoTemp.add(cidadeMaisProxima);

			// seta a cidade atual como a cidade mais próxima obtida na solução
			cidadeAtual = cidadeMaisProxima;
		}

		// adiciona o custo da volta da última cidade para a cidade inicial
		distanciaTotal = distanciaTotal + problema.getDistancia(cidadeAtual, caminhoTemp.get(0));
		// adiciona a volta para a cidade inicial
		caminhoTemp.add(cidadeInicial);

		// seta o melhor percurso obtido
		solucao.setCaminho(caminhoTemp);
		// solucao.mostrarCaminho();
		// seta a distância total obtida para o melhor percurso
		solucao.setDistanciaTotal(distanciaTotal);
	}

	/**
	 * Retorna a solução
	 *
	 * @param cidadeInicial
	 * @return solução obtida após o calculo do melhor caminho
	 */
	public Solucao getSolucao(int cidadeInicial) {
		calculaMenorCaminho(cidadeInicial);
		return solucao;
	}
}
