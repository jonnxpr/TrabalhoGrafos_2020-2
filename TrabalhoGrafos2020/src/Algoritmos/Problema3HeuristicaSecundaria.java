/**
 * Trabalho Pr�tico - Classe Problema3HeuristicaSecundaria
 *
 * @author Jonathan Douglas Diego Tavares
 * @matricula 540504
 * @disciplina Algortimos em Grafos
 * @professor Alexei Machado
 */

package Algoritmos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//Importações
import Modelagem.Problema;
import Modelagem.Solucao;
import javafx.util.Pair;

/**
 * Classe Heuristica Implementa a heurística da "inserção mais barata"
 *
 * Funcionamento: Consiste em construir uma rota passo a passo, partindo de rota
 * inicial envolvendo 3 aeroportos (obtidas por um método qualquer) e adicionar a
 * cada passo, a cidade k (ainda não visitada) entre a ligação (i, j) de
 * aeroportos já visitadas, cujo custo de inserção seja o mais barato
 *
 * Complexidade: O(n^3)
 *
 * @author Jonathan Douglas Diego Tavares
 */
public class Problema3HeuristicaSecundaria {

	// Atributos
	private final Problema problema; // atributo que carrega a instância do problema
	private final Solucao solucao; // atributo que carrega a solução obtida ao final da solução do problema

	/**
	 * Construtor
	 *
	 * @param problema
	 */
	public Problema3HeuristicaSecundaria(Problema problema) {
		this.problema = problema;
		solucao = new Solucao();
	}

	/**
	 * Atualiza o conjunto de vértices(cidades) não visitados baseado no conjunto
	 * de visitados
	 *
	 * @param visitados
	 * @param array
	 */
	private void atualizaNaoVisitados(ArrayList<Integer> visitados, ArrayList<Integer> array) {
		for (int i = 0; i < problema.getGrafo().numVertices(); i++) {
			if (!visitados.contains(i)) {
				array.add(i);
			}
		}
	}

	/**
	 * Calcula o melhor percurso partindo de uma determinada cidade inicial
	 *
	 * @param cidadeInicial
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void calculaMenorCaminho(int cidadeInicial) {
		// Consideramos que para esta heurística funcionar o mínimo de cidades
		// deve ser 3, pois para o funcionamento da mesma é admitido um caminho
		// inicial entre 3 cidades
		if (problema.getGrafo().numVertices() < 3) {
			System.out.println("Para este algoritmo o número mínimo de cidades é de 3.");
			return;
		}

		ArrayList<Integer> caminho = new ArrayList<>(); // representa o melhor caminho
		ArrayList<Integer> naoVisitados = new ArrayList<>(); // conjunto de cidades não visitadas
		ArrayList<Pair<Integer[], Double>> tabelaTemp = new ArrayList<>(); // tabela de custos

		// gera o caminho inicial contendo 3 cidades, incluindo a cidade inicial
		geraCaminhoInicial(caminho, cidadeInicial);

		// atualiza o conjunto de cidades não visitadas
		atualizaNaoVisitados(caminho, naoVisitados);

		// enquanto a quantidade de cidades no caminho não for igual a quantidade de
		// cidades do problema inicial
		while (caminho.size() < problema.getGrafo().numVertices()) {
			// percorre o conjunto de cidades visitadas, ou seja, cidades que já estão no
			// caminho
			for (Integer visitado : caminho) {
				// percorre o conjunto de cidades não visitadas, ou seja, cidades que ainda
				// não estão no caminho
				for (Integer naoVisitado : naoVisitados) {
					// percorre o conjunto de cidades visitadas
					for (Integer segVisitado : caminho) {
						// valor que determina a garantia de que somente o caminho já existente será
						// verificado para o calculo do custo na tabela
						int valorDesejado = (caminho.indexOf(visitado) == caminho.size() - 1)
								? caminho.get(caminho.indexOf(visitado))
								: caminho.get(caminho.indexOf(visitado) + 1);
						// se a cidade da iteração atual for diferente da cidade da primeira
						// iteração
						// e existe um caminho entre a cidade da iteração atual e cidade da primeira
						// iteração
						// no ArrayList que representa o melhor caminho
						if (segVisitado != visitado && segVisitado == valorDesejado) {
							// calcula o custo da inserção do vértice ainda não visitado
							// Custo da inserção = dik + dkj - dij
							// i -> cidade da primeira iteração
							// k -> cidade ainda não visitada
							// j -> cidade da iteração atual que é obrigatoriamente diferente da cidade
							// da primeira iteração
							double custo = problema.getDistancia(visitado, naoVisitado)
									+ problema.getDistancia(naoVisitado, segVisitado)
									- problema.getDistancia(visitado, segVisitado);
							// armazena o custo atual e o percurso (i->k->j)
							Integer[] caminhoTemp = new Integer[3];
							caminhoTemp[0] = visitado;
							caminhoTemp[1] = naoVisitado;
							caminhoTemp[2] = segVisitado;
							// adiciona na tabela de custos
							tabelaTemp.add(new Pair(caminhoTemp, custo));
						}
					}
				}
			}

			// obtém da tabela de custos o percurso cujo custo de inserção é o menor
			Pair<Integer[], Double> melhorOpcao = getMelhorOpcao(tabelaTemp);
			// faz a inserção da cidade cujo custo de inserção é o menor
			insereMelhorOpcaoNoCaminho(melhorOpcao, caminho);
			// reinicializa a tabela de custos e o conjunto de cidades não visitadas
			tabelaTemp = new ArrayList<>();
			naoVisitados = new ArrayList<>();
			// atualiza o conjunto de cidades não visitadas
			atualizaNaoVisitados(caminho, naoVisitados);
		}

		// fecha o caminho adicionando a volta para a cidade inicial
		caminho.add(cidadeInicial);
		// seta o caminho obtido como solução
		solucao.setCaminho(caminho);
		// converte ArrayList<Integer> em um vetor de inteiros
		Integer caminhoFinal[] = Arrays.asList(caminho.toArray()).toArray(new Integer[0]);
		// seta a distância total obtida do caminho solução
		solucao.setDistanciaTotal(getDistanciaCaminho(caminhoFinal));
	}

	/**
	 * Gera um caminho contendo 3 cidades (sendo uma delas a cidade inicial),
	 * representando o percurso inicial
	 *
	 * @param caminho
	 * @param cidadeInicial
	 */
	private void geraCaminhoInicial(ArrayList<Integer> caminho, int cidadeInicial) {
		caminho.add(cidadeInicial);
		caminho.add(sorteiaDiferente(caminho));
		caminho.add(sorteiaDiferente(caminho));
	}

	/**
	 * Calcula a distância total de um determinado caminho fornecido como um vetor
	 * de inteiros
	 *
	 * @param caminho
	 * @return inteiro representando a distância total obtida no caminho
	 */
	private double getDistanciaCaminho(Integer[] caminho) {
		double distancia = 0;

		for (int i = 0; i < caminho.length - 1; i++) {
			distancia = distancia + problema.getDistancia(caminho[i], caminho[i + 1]);
		}

		return distancia;
	}

	/**
	 * Verifica qual é o custo de inserção mais barato do conjunto de custos
	 * presentes na tabela
	 *
	 * @param tabela
	 * @return Pair contendo o percurso do menor custo e o respectivo custo.
	 */
	private Pair<Integer[], Double> getMelhorOpcao(ArrayList<Pair<Integer[], Double>> tabela) {
		Pair<Integer[], Double> melhorOpcao = tabela.get(0);

		for (int i = 1; i < tabela.size(); i++) {
			if (tabela.get(i).getValue() < melhorOpcao.getValue()) {
				melhorOpcao = tabela.get(i);
			}
		}

		// System.out.println("mv = " + melhorOpcao.getValue());
		return melhorOpcao;
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

	/**
	 * Faz o passo de inserção da cidade k ainda não presente no caminho entre as
	 * cidades i e j já presentes no caminho
	 *
	 * @param melhorOpcao
	 * @param caminho
	 */
	private void insereMelhorOpcaoNoCaminho(Pair<Integer[], Double> melhorOpcao, ArrayList<Integer> caminho) {
		/*
		 * System.out.print("->->"); for (Integer i : caminho){ System.out.print (i +
		 * " "); } System.out.println("-");
		 */

		// posição de inserção é obtida através da posição da cidade i + 1
		// no ArrayList que representa o caminho
		int posicaoDeInsercao = caminho.indexOf(melhorOpcao.getKey()[0]);
		caminho.add(posicaoDeInsercao + 1, melhorOpcao.getKey()[1]);

		/*
		 * System.out.print("->->"); for (Integer i : caminho){ System.out.print (i +
		 * " "); } System.out.println("");
		 */
	}

	/**
	 * Sorteia um valor inteiro representando uma cidade que ainda não esteja no
	 * caminho
	 *
	 * @param caminho
	 * @return valor inteiro representando a cidade sorteada
	 */
	private int sorteiaDiferente(ArrayList<Integer> caminho) {
		Random random = new Random();
		int sorteado = random.nextInt(problema.getGrafo().numVertices());
		while (caminho.contains(sorteado)) {
			sorteado = random.nextInt(problema.getGrafo().numVertices());
		}
		return sorteado;
	}
}
