package Algoritmos;

import java.util.ArrayList;

import Modelagem.Permutacoes;
//Importações
import Modelagem.Problema;
import Modelagem.Solucao;

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
	 * Gera permutação inicial a ser utilizada no método que irá gerar todas as
	 * permutações
	 *
	 * @param cidadeInicial
	 * @return vetor de inteiros contendo a permutação gerada
	 */
	private int[] geraPermutacaoInicial(int cidadeInicial) {
		int permutacao[] = new int[problema.getGrafo().numVertices() - 1];
		int i = 0;
		int pos = 0;

		while (i < permutacao.length + 1) {
			if (i != cidadeInicial) {
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

	private ArrayList<int[]> removerPermIndesejada(int aeroportoInicial, int aeroportoFinal, ArrayList<int[]> array) {

		for (int i = 0; i < array.size(); i++) {
			for (int j = 0; j < problema.getGrafo().numVertices() - 2; j++) {
				//System.out.println("\ni = " + i + " j = " + array.get(i)[j] + " j + 1 = " + array.get(i)[j + 1]);
				//System.out.println(problema.getGrafo().existeAresta(array.get(i)[j], array.get(i)[j + 1]));
				// System.out.println("\n i = " + i + " Antes");
				// imprime(array);
				if (!problema.getGrafo().existeAresta(array.get(i)[j], array.get(i)[j + 1])
						|| !problema.getGrafo().existeAresta(array.get(i)[j + 1], array.get(i)[j])) {
					//System.out.println("vetor removido: ");
					//imprimeVetor(array.get(i));
					array.remove(array.get(i));
					//System.out.println("\n i = " + i + " Depois");
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

	private void imprimeVetor(int[] vetor) {
		for (int i = 0; i < vetor.length; i++) {
			System.out.print(vetor[i] + " ");
		}
		System.out.println();
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
	 * Calcula o menor caminho comparando a distância total obtida para cada
	 * permutação gerada
	 *
	 * @param cidadeInicial
	 */
	private void calculaMenorCaminho(int aeroportoInicial, int aeroportoFinal) {
		// gera todas as permutações
		//System.out.println("aeroInicial = " + aeroportoInicial + "\naeroFinal =" + aeroportoFinal);
		caminhos = permutacoes.getPermutacoes(geraPermutacaoInicial(aeroportoInicial));
		//System.out.println("\n\nperminiciais\n\n");
		//imprime(caminhos);
		// completa os caminhos de forma a ser possível sair da cidade inicial e voltar
		// para a mesma
		ArrayList<int[]> caminhosDesejados = removerPermIndesejada(aeroportoInicial, aeroportoFinal, caminhos);
		caminhosDesejados = completarCaminhos(aeroportoInicial, aeroportoFinal, caminhosDesejados);

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
	}

	/**
	 * Imprime os elementos de um ArrayList que armazena um conjunto de vetores de
	 * inteiros
	 *
	 * @param arrayList
	 */
	@SuppressWarnings("unused")
	private void imprime(ArrayList<int[]> arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			for (int j = 0; j < arrayList.get(i).length; j++) {
				System.out.print(arrayList.get(i)[j] + " ");
			}
			System.out.println("");
		}
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
}
