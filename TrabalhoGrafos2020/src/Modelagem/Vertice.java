package Modelagem;

public class Vertice {
	private String labelVertice;
	private int valorRepresentativo;
	private int grau;
	private double latitude;
	private double longitude;

	public Vertice(int valorRepresentativo, double latitude, double longitude) {
		this.labelVertice = "NoLabel";
		this.valorRepresentativo = valorRepresentativo;
		this.grau = 0;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Vertice(String labelVertice, int valorRepresentativo, double latitude, double longitude) {
		this.labelVertice = labelVertice;
		this.valorRepresentativo = valorRepresentativo;
		this.grau = 0;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getGrau() {
		return grau;
	}

	public String getLabelVertice() {
		return labelVertice;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getValorRepresentativo() {
		return valorRepresentativo;
	}

	public void setGrau(int grau) {
		this.grau = grau;
	}

	public void setLabelVertice(String labelVertice) {
		this.labelVertice = labelVertice;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setValorRepresentativo(int valorRepresentativo) {
		this.valorRepresentativo = valorRepresentativo;
	}

	@Override
	public String toString() {
		String vertice = "Valor representativo: " + this.valorRepresentativo;
		vertice += "\nAeroporto: " + this.labelVertice;
		vertice += "\nLatitude: " + this.latitude;
		vertice += "\nLongitude: " + this.longitude;
		vertice += "\n";
		return vertice;
	}
}
