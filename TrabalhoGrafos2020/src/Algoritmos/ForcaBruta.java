package Algoritmos;

//Importações
import Modelagem.Problema;
import Modelagem.Permutacoes;
import Modelagem.Solucao;
import java.util.ArrayList;

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
public class ForcaBruta {

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
	public ForcaBruta(Problema problema) {
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
	private ArrayList<int[]> completarCaminhos(int cidadeInicial, ArrayList<int[]> array) {
		ArrayList<int[]> caminhosCompletos = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			int aux[] = new int[problema.getGrafo().numVertices() + 1];
			aux[0] = cidadeInicial;
			aux[aux.length - 1] = cidadeInicial;

			for (int j = 1; j < array.get(i).length + 1; j++) {
				aux[j] = array.get(i)[j - 1];
			}

			caminhosCompletos.add(aux);
		}

		return caminhosCompletos;
	}

	/**
	 * Calcula a distância total de um determinado caminho fornecido como um vetor
	 * de inteiros
	 *
	 * @param caminho
	 * @return inteiro representando a distância total obtida no caminho
	 */
	private int getDistanciaCaminho(int[] caminho) {
		int distancia = 0;

		for (int i = 0; i < caminho.length - 1; i++) {
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
	private void calculaMenorCaminho(int cidadeInicial) {
		// gera todas as permutações
		caminhos = permutacoes.getPermutacoes(geraPermutacaoInicial(cidadeInicial));
		// imprime(caminhos);
		// completa os caminhos de forma a ser possível sair da cidade inicial e voltar
		// para a mesma
		ArrayList<int[]> caminhosCompletos = completarCaminhos(cidadeInicial, caminhos);

		int menorCaminho[] = caminhosCompletos.get(0);
		ArrayList<Integer> menorCaminhoList = new ArrayList<>();
		// imprime(caminhosCompletos);

		// percorre o conjunto de caminhos comparando suas distâncias totais
		// o menor caminho será aquele cuja distância total tenha o menor valor dentre
		// o conjunto verificado
		for (int i = 1; i < caminhosCompletos.size(); i++) {
			if (getDistanciaCaminho(caminhosCompletos.get(i)) < getDistanciaCaminho(menorCaminho)) {
				menorCaminho = caminhosCompletos.get(i);
			}
		}

		// passa os vértices obtidos do menor caminho para um ArrayList que será
		// passado para a solução
		for (int i = 0; i < menorCaminho.length; i++) {
			menorCaminhoList.add(menorCaminho[i]);
		}

		// seta o melhor caminho obtido na solução
		solucao.setCaminho(menorCaminhoList);
		// solucao.mostrarCaminho();
		// seta a distância total obtida no melhor caminho
		solucao.setDistanciaTotal(getDistanciaCaminho(menorCaminho));
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
