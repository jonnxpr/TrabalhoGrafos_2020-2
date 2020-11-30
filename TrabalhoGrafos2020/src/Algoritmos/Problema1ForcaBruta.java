package Algoritmos;

import java.util.ArrayList;
import java.util.LinkedList;

import Modelagem.Grafo;
import Modelagem.Permutacoes;
//Importações
import Modelagem.Problema;
import Modelagem.Solucao;
import javafx.util.Pair;

/**
 * Classe ForcaBruta Implementa o algoritmo de força bruta
 *
 * Funcionamento: Consiste em testar todas as possibilidades de caminhos
 * existentes até que se encontre o melhor(aquele cuja distância total do
 * percurso seja a menor dentre todos)
 *
 * Complexidade: O(n!)
 *
 * @author Jonathan Douglas Diego Tavares
 */
public class Problema1ForcaBruta {

	// Atributos
	private final Problema problema; // atributo que carrega a instância do problema
	private final Solucao solucao; // atributo que carrega a solução obtida ao final da solução do problema
	ArrayList<int[]> caminhos; // conjunto de caminhos obtidos na solução do problema
	Permutacoes permutacoes; // conjunto de permutações geradas para verificar os caminhos possíveis
	private int[] verticePredecessorLargura;

	/**
	 * Construtor
	 *
	 * @param problema
	 */
	public Problema1ForcaBruta(Problema problema) {
		this.problema = problema;
		solucao = new Solucao();
		caminhos = new ArrayList<>();
		permutacoes = new Permutacoes();
	}

	/**
	 * Método buscaLargura, responsável por efetuar a busca em largura a partir de
	 * um vértice inicial até o vértice final utilizando uma fila de vértices a
	 * serem conhecidos e um vetor de vértices já visitados.
	 * 
	 * @param verticeInicial
	 * @param verticeFinal
	 * @return
	 */
	private boolean buscaLargura(int verticeInicial, int verticeFinal) {
		Grafo grafo = problema.getGrafo();
		boolean visitados[] = new boolean[grafo.numVertices()];

		// seta os vértices como não visitados
		for (int i = 0; i < visitados.length; i++) {
			visitados[i] = false;
		}

		// cria a lista de vértices a serem conhecidos
		LinkedList<Integer> fila = new LinkedList<>();

		// marca o vértice inicial como visitado
		visitados[verticeInicial] = true;

		// adiciona o vértice inicial na fila de vértices conhecidos
		fila.add(verticeInicial);

		// enquanto a fila de vértices conhecidos não estiver vazia
		while (!fila.isEmpty()) {
			// pega o elemento presente no topo (posição inicial) da fila
			int v = fila.poll();
			// System.out.println("vAtual = " + v);

			// obtém a lista de adjacência do vértice obtido da fila
			ArrayList<Integer> listaAdj = grafo.listaDeAdjacencia(v);

			// percorre a lista de adjacência
			for (int w = 0; w < listaAdj.size(); w++) {
				// se o vértice atual não foi visitado ainda, então o visite,
				// marque que foi visitado e o adicione na lista de vértices a serem conhecidos
				// seta também o vértice predecessor
				if (visitados[listaAdj.get(w)] == false) {
					visitados[listaAdj.get(w)] = true;
					fila.add(listaAdj.get(w));

					verticePredecessorLargura[listaAdj.get(w)] = v;
				}
			}
		}

		// retorna se existe um caminho entre o vértice final e inicial
		return (visitados[verticeFinal] == true);
	}

	/**
	 * Calcula o menor caminho comparando a distância total obtida para cada
	 * permutação gerada
	 *
	 * @param cidadeInicial
	 */
	private void calculaMenorCaminho(int aeroportoInicial, int aeroportoFinal) {
		// gera todas as permutações
		// System.out.println("aeroInicial = " + aeroportoInicial + "\naeroFinal =" +
		// aeroportoFinal);

		System.out.println("Matriz de adj:\n");
		problema.getGrafo().imprimeMatrizAdj();

		if (aeroportoInicial != aeroportoFinal) {
			caminhos = permutacoes.getPermutacoes(geraPermutacaoInicial(aeroportoInicial));
			// System.out.println("\n\nperminiciais\n\n");
			// imprime(caminhos);
			// completa os caminhos de forma a ser possível sair da cidade inicial e voltar
			// para a mesma
			ArrayList<int[]> caminhosDesejados = removerPermIndesejada(caminhos);
			System.out.println("\n\nCaminhos Desesejados: \n\n");
			imprime(caminhosDesejados);
			caminhosDesejados = completarCaminhos(aeroportoInicial, aeroportoFinal, caminhosDesejados);
			System.out.println("\n\nCaminhos Completos: \n\n");
			imprime(caminhosDesejados);
			System.out.println("tamDesej =" + caminhosDesejados.size());
			System.out.println(caminhosDesejados.isEmpty());

			int menorCaminho[] = caminhosDesejados.get(0);
			ArrayList<Integer> menorCaminhoList = new ArrayList<>();
			// imprime(caminhosDesejados);

			// percorre o conjunto de caminhos comparando suas distâncias totais
			// o menor caminho será aquele cuja distância total tenha o menor valor dentre
			// o conjunto verificado
			for (int i = 1; i < caminhosDesejados.size(); i++) {
				if (getDistanciaCaminho(caminhosDesejados.get(i), aeroportoFinal) < getDistanciaCaminho(menorCaminho,
						aeroportoFinal)) {
					menorCaminho = caminhosDesejados.get(i);
				}
			}

			// passa os vértices obtidos do menor caminho para um ArrayList que será
			// passado para a solução
			for (int i = 0; i < menorCaminho.length; i++) {
				if (menorCaminho[i] == aeroportoFinal) {
					menorCaminhoList.add(menorCaminho[i]);
					break;
				}
				menorCaminhoList.add(menorCaminho[i]);
			}

			// seta o melhor caminho obtido na solução
			solucao.setCaminho(menorCaminhoList);
			// solucao.mostrarCaminho();
			// seta a distância total obtida no melhor caminho
			solucao.setDistanciaTotal(getDistanciaCaminho(menorCaminho, aeroportoFinal));
		} else {
			ArrayList<Integer> menorCaminhoList = new ArrayList<>();
			menorCaminhoList.add(aeroportoInicial);
			solucao.setCaminho(menorCaminhoList);
			solucao.setDistanciaTotal(0);
		}

	}

	/**
	 * Adiciona a cidade inicial ao inicio e fim dos caminhos gerados no conjunto de
	 * permutações
	 *
	 * @param cidadeInicial
	 * @param array
	 * @return arraylist contendo o conjunto de vetores(permutações) com o caminho
	 *         completo, ou seja, inicia na cidade inicial e termina na mesma.
	 */
	private ArrayList<int[]> completarCaminhos(int aeroportoInicial, int aeroportoFinal, ArrayList<int[]> array) {
		ArrayList<int[]> caminhosCompletos = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			int aux[] = new int[problema.getGrafo().numVertices()];
			aux[0] = aeroportoInicial;
			// aux[aux.length - 1] = aeroportoFinal;

			for (int j = 1; j < array.get(i).length + 1; j++) {
				if (array.get(i)[j - 1] == aeroportoFinal) {
					aux[j] = array.get(i)[j - 1];
					break;
				}
				aux[j] = array.get(i)[j - 1];
			}

			caminhosCompletos.add(aux);
		}

		return caminhosCompletos;
	}

	/**
	 * Gera permutação inicial a ser utilizada no método que irá gerar todas as
	 * permutações
	 *
	 * @param cidadeInicial
	 * @return vetor de inteiros contendo a permutação gerada
	 */
	private int[] geraPermutacaoInicial(int aeroportoInicial) {
		int permutacao[] = new int[problema.getGrafo().numVertices() - 1];
		int i = 0;
		int pos = 0;

		while (i < permutacao.length + 1) {
			if (i != aeroportoInicial) {
				permutacao[pos] = i;
				pos++;
				i++;
			} else {
				i++;
			}
		}

		return permutacao;
	}

	/**
	 * Calcula a distância total de um determinado caminho fornecido como um vetor
	 * de inteiros
	 *
	 * @param caminho
	 * @return inteiro representando a distância total obtida no caminho
	 */
	private double getDistanciaCaminho(int[] caminho, int aeroportoFinal) {
		double distancia = 0;

		for (int i = 0; i < caminho.length - 1; i++) {
			if (caminho[i] == aeroportoFinal) {
				return distancia;
			}
			distancia = distancia + problema.getDistancia(caminho[i], caminho[i + 1]);
		}

		return distancia;
	}

	/**
	 * Retorna a solução
	 *
	 * @param cidadeInicial
	 * @return solução obtida após o calculo do melhor caminho
	 */
	public Solucao getSolucao(int aeroportoInicial, int aeroportoFinal) {
		calculaMenorCaminho(aeroportoInicial, aeroportoFinal);
		return solucao;
	}

	/**
	 * Imprime os elementos de um ArrayList que armazena um conjunto de vetores de
	 * inteiros
	 *
	 * @param arrayList
	 */
	private void imprime(ArrayList<int[]> arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			for (int j = 0; j < arrayList.get(i).length; j++) {
				System.out.print(arrayList.get(i)[j] + " ");
			}
			System.out.println("");
		}
	}

	private void imprimeVetor(int[] vetor) {
		for (int i = 0; i < vetor.length; i++) {
			System.out.print(vetor[i] + " ");
		}
		System.out.println();
	}

	/**
	 * ********************************************************************* BUSCA
	 * EM LARGURA
	 *********************************************************************
	 */

	/**
	 * Método iniciaBuscaEmLargura, responsável por inicializar o array de
	 * vértices predecessores a ser preenchido durante a busca.
	 * 
	 * @param verticeInicial
	 * @param verticeFinal
	 * @return booleano indicando se existe um caminho entre o vértice inicial e
	 *         final
	 */
	public boolean iniciaBuscaEmLargura(int verticeInicial, int verticeFinal) {
		verticePredecessorLargura = new int[problema.getGrafo().numVertices()];
		int tamanhoVetor = verticePredecessorLargura.length;

		// Percorre o vetor de distâncias inicializando todas as posições com o valor
		// V+1
		for (int i = 0; i < tamanhoVetor; i++) {
			// diz que o predecessor não existe ainda
			verticePredecessorLargura[i] = -1;
		}
		// chama a busca em profundidade
		return buscaLargura(verticeInicial, verticeFinal);
	}

	/**
	 * Método obtemCaminhoLargura, responsável por obter o caminho enter o
	 * vértice inicial e final da busca baseado no array de vértice predecessor
	 * resultante da busca.
	 * 
	 * @param verticeInicial
	 * @param verticeFinal
	 * @return ArrayList de Pair contendo o caminho obtido da busca em largura entre
	 *         o vértice inicial e final
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<Pair<Integer, Integer>> obtemCaminhoLargura(int verticeInicial, int verticeFinal) {
		ArrayList<Pair<Integer, Integer>> caminho = new ArrayList<>();
		int controle = verticeFinal;

		while (controle != verticeInicial) {
			caminho.add(new Pair(verticePredecessorLargura[controle], controle));
			controle = verticePredecessorLargura[controle];
		}
		return caminho;
	}

	private ArrayList<int[]> removerPermIndesejada(ArrayList<int[]> array) {

		for (int i = 0; i < array.size(); i++) {
			for (int j = 0; j < problema.getGrafo().numVertices() - 2; j++) {
				// System.out.println("\ni = " + i + " j = " + array.get(i)[j] + " j + 1 = " +
				// array.get(i)[j]);
				// System.out.println(problema.getGrafo().existeAresta(array.get(i)[j],
				// array.get(i)[j + 1]));
				// System.out.println("\n i = " + i + " Antes");
				// imprime(array);
				if (!problema.getGrafo().existeAresta(array.get(i)[j], array.get(i)[j + 1])) {
					System.out.println("vetor removido: ");
					imprimeVetor(array.get(i));
					array.remove(array.get(i));
					// System.out.println("\n i = " + i + " Depois");
					i--;
					imprime(array);
					break;
				}
			}
		}
		// System.out.println("\nT2\n");
		// imprime(array);
		return array;
	}
}
