/**
 * Trabalho Pr�tico - Classe Solucao
 *
 * @author Jonathan Douglas Diego Tavares
 * @matricula 540504
 * @disciplina Algortimos em Grafos
 * @professor Alexei Machado
 */

package Modelagem;

//Importações
import java.util.ArrayList;

public class Solucao {

	// Atributos
	private double distanciaTotal; // distância obtida do caminho solução
	private int altitudeTotal;
	private int precoTotal;
	private ArrayList<Integer> caminho; // caminho solução

	/**
	 * Construtor
	 */
	public Solucao() {
		this.distanciaTotal = 0;
		this.altitudeTotal = 0;
		this.precoTotal = 0;
		caminho = new ArrayList<>();
	}

	/**
	 * Adiciona um inteiro referente a um aeroporto no caminho solu��o
	 *
	 * @param aeroporto
	 */
	public void addAeroportoNoCaminho(int aeroporto) {
		caminho.add(aeroporto);
	}

	public int getAltitudeTotal() {
		return altitudeTotal;
	}

	/**
	 * Retorna o caminho solução
	 *
	 * @return clone do ArrayList que representa o caminho solução
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getCaminho() {
		return (ArrayList<Integer>) caminho.clone();
	}

	/**
	 * Retorna a distância total do caminho solução
	 *
	 * @return inteiro que representa a distância total do caminho solução
	 */
	public double getDistanciaTotal() {
		return distanciaTotal;
	}

	public int getPrecoTotal() {
		return precoTotal;
	}

	/**
	 * Retorna a quantidade de aeroportos no caminho solução
	 *
	 * @return quantidade de cidades no caminho solução
	 */
	public int getQuantAeroportosNoCaminho() {
		return caminho.size();
	}

	/**
	 * Retorna a última aeroporto do caminho solução
	 *
	 * @return inteiro que representa a última aeroporto do caminho solução
	 */
	public int getUltimaCidade() {
		return caminho.get(caminho.size() - 1);
	}

	/**
	 * Incrementa a distância total com o valor passado pelo parâmetro
	 *
	 * @param distancia
	 */
	public void incrementarDistanciaTotal(double distancia) {
		distanciaTotal = distanciaTotal + distancia;
	}

	/**
	 * Imprime o caminho solução
	 */
	public String mostrarCaminho() {
		String caminhoRet = "Caminho: ";
		System.out.println("Caminho:");
		for (Integer i : caminho) {
			caminhoRet += i + " ";
			System.out.print(i + " ");
		}
		caminhoRet += "\n";
		System.out.println("");

		return caminhoRet;
	}

	public void setAltitudeTotal(int altitudeTotal) {
		this.altitudeTotal = altitudeTotal;
	}

	/**
	 * Seta o caminho solução com o aeroporto passado via parâmetro
	 *
	 * @param caminho
	 */
	public void setCaminho(ArrayList<Integer> caminho) {
		this.caminho = caminho;
	}

	public void setDistanciaTotal(double distanciaTotal) {
		this.distanciaTotal = distanciaTotal;
	}

	public void setPrecoTotal(int precoTotal) {
		this.precoTotal = precoTotal;
	}
}
