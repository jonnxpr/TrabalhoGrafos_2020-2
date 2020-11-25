package Modelagem;

//Importações
import java.util.ArrayList;

/**
 * Classe Solucao
 *
 * @author Jonathan Douglas Diego Tavares
 */
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
	 * Incrementa a distância total com o valor passado pelo parâmetro
	 *
	 * @param distancia
	 */
	public void incrementarDistanciaTotal(double distancia) {
		distanciaTotal = distanciaTotal + distancia;
	}

	/**
	 * Adiciona um inteiro referente a uma cidade no caminho solução
	 *
	 * @param cidade
	 */
	public void addCidadeNoCaminho(int cidade) {
		caminho.add(cidade);
	}

	/**
	 * Retorna a distância total do caminho solução
	 *
	 * @return inteiro que representa a distância total do caminho solução
	 */
	public double getDistanciaTotal() {
		return distanciaTotal;
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
	 * Retorna a última cidade do caminho solução
	 *
	 * @return inteiro que representa a última cidade do caminho solução
	 */
	public int getUltimaCidade() {
		return caminho.get(caminho.size() - 1);
	}

	/**
	 * Retorna a quantidade de cidades no caminho solução
	 *
	 * @return quantidade de cidades no caminho solução
	 */
	public int getQuantCidadesNoCaminho() {
		return caminho.size();
	}

	/**
	 * Imprime o caminho solução
	 */
	public void mostrarCaminho() {
		System.out.println("Caminho:");
		for (Integer i : caminho) {
			System.out.print(i + " ");
		}
		System.out.println("");
	}

	/**
	 * Seta o caminho solução com o caminho passado via parâmetro
	 *
	 * @param caminho
	 */
	public void setCaminho(ArrayList<Integer> caminho) {
		this.caminho = caminho;
	}

	public int getAltitudeTotal() {
		return altitudeTotal;
	}

	public void setAltitudeTotal(int altitudeTotal) {
		this.altitudeTotal = altitudeTotal;
	}

	public int getPrecoTotal() {
		return precoTotal;
	}

	public void setPrecoTotal(int precoTotal) {
		this.precoTotal = precoTotal;
	}

	public void setDistanciaTotal(double distanciaTotal) {
		this.distanciaTotal = distanciaTotal;
	}
}
