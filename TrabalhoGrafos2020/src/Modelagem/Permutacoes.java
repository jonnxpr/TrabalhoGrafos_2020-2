package Modelagem;

//Importações
import java.util.ArrayList;

/**
 * Classe Permutacoes
 *
 * @author Jonathan Douglas Diego Tavares
 */
public class Permutacoes {

	private int permutacaoAtual[]; // guarda a permutação atual gerada

	private ArrayList<int[]> permutacoes; // guarda o conjunto de permutações geradas

	/**
	 * Retorna o conjunto de permutações geradas a partir de uma permutação
	 * inicial passada via parâmetro
	 *
	 * @param vetor permutação inicial
	 * @return conjunto de permutações geradas
	 */
	public ArrayList<int[]> getPermutacoes(int[] vetor) {
		permuta(vetor);
		return permutacoes;
	}

	/**
	 * Adiciona a permutação atual no ArrayList permutacoes
	 */
	private void imprime() {
		int aux[] = new int[permutacaoAtual.length];
		// System.out.println();
		// System.out.print("(" + cont + ") : ");
		for (int i = 0; i < permutacaoAtual.length; i++) {
			// System.out.print(p[i] + " ");
			aux[i] = permutacaoAtual[i];
		}

		permutacoes.add(aux);
	}

	/**
	 * Faz a chamada recursiva do método permuta e preenche o ArrayList permutacoes
	 * com as permutações obtidas
	 *
	 * @param vet vetor contendo a permutação inicial
	 */
	public void permuta(int[] vet) {
		permutacoes = new ArrayList<>();
		permutacaoAtual = new int[vet.length];
		permuta(vet, 0);
	}

	/**
	 * Faz recursivamente as permutações a partir de uma permutação inicial
	 *
	 * @param vet permutação inicial
	 * @param n   tamanho do vetor permutado atual
	 */
	private void permuta(int[] vet, int n) {

		if (n == vet.length) {
			imprime();
		} else {

			for (int i = 0; i < vet.length; i++) {

				boolean achou = false;

				for (int j = 0; j < n; j++) {

					if (permutacaoAtual[j] == vet[i]) {
						achou = true;
					}
				}

				if (!achou) {

					permutacaoAtual[n] = vet[i];

					permuta(vet, n + 1);
				}

			}

		}
	}
}
