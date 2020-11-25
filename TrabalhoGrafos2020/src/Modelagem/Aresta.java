package Modelagem;

public class Aresta {
	private Vertice v1;
	private Vertice v2;
	private int altitude;
	private double distancia;
	private int preco;
	
	public Aresta(Vertice v1, Vertice v2) {
		this.v1 = v1;
		this.v2 = v2;
		this.altitude = 10000;
		this.distancia = -1;
		this.preco = -1;
	}
	
	public Aresta(Vertice v1, Vertice v2, int altitude, double distancia, int preco) {
		this.v1 = v1;
		this.v2 = v2;
		this.altitude = altitude;
		this.distancia = distancia;
		this.preco = preco;
	}
	
	public Vertice getV1() {
		return v1;
	}

	public void setV1(Vertice v1) {
		this.v1 = v1;
	}

	public Vertice getV2() {
		return v2;
	}

	public void setV2(Vertice v2) {
		this.v2 = v2;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public int getPreco() {
		return preco;
	}

	public void setPreco(int preco) {
		this.preco = preco;
	}
	
	@Override
	public String toString() {
		String aresta = "V1: " + this.v1;
		aresta += "\nV2: " + this.v2;
		aresta += "\nAltitude: " + this.altitude;
		aresta += "\nDistância: " + this.distancia;
		aresta += "\nPreço:" + this.preco;
		aresta += "\n";
		return aresta;
	}
}
