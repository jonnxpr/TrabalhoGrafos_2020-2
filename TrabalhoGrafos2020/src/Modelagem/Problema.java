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
	public double getDistancia(int v1, int v2) {
		return this.grafo.getDistancia(v1, v2);
	}
	
	public int getAltitude(int v1, int v2) {
		return this.grafo.getAltitude(v1, v2);
	}
	
	public int getPreco(int v1, int v2) {
		return this.grafo.getPreco(v1, v2);
	}
	
	/*
	 * unit:
	 * M - Milhas
	 * K - Quilometro
	 * N - Milhas Nauticas
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		}
		else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			if (unit.equals("K")) {
				dist = dist * 1.609344;
			} else if (unit.equals("N")) {
				dist = dist * 0.8684;
			}
			return (dist);
		}
	}
}
