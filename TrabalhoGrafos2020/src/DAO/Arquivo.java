package DAO;

//Importações
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Modelagem.Grafo;
import Modelagem.Vertice;

/**
 * Classe Arquivo
 *
 * @author Jonathan Douglas Diego Tavares
 */
public class Arquivo {

	@SuppressWarnings("resource")
	public static ArrayList<Vertice> getVertices(String filename) {
		int valorRep = 0;

		ArrayList<Vertice> vertices = new ArrayList<>();

		try {
			File file = new File(filename);

			// tenta fazer abertura do arquivo
			Scanner in = new Scanner(file);

			int quantVertices = Integer.parseInt(in.nextLine());
			System.out.println(quantVertices);

			for (int i = 0; i < quantVertices; i++) {
				String[] buffer = in.nextLine().split(" ");
				vertices.add(
						new Vertice(buffer[0], valorRep, Double.parseDouble(buffer[1]), Double.parseDouble(buffer[2])));
				valorRep = valorRep + 1;
			}

			in.close();

		} catch (Exception e) {
			System.out.println("Ocorreu um erro na tentativa de ler o arquivo " + filename + ".");
			// e.printStackTrace();
		}

		return vertices;
	}

	@SuppressWarnings("resource")
	public static Grafo getGrafoNaoOrientado(String filename, ArrayList<Vertice> vertices) {
		Grafo grafo = null;
		try {
			File file = new File(filename);

			// tenta fazer abertura do arquivo
			Scanner in = new Scanner(file);

			int quantVertices = Integer.parseInt(in.nextLine());
			System.out.println(quantVertices);

			grafo = new Grafo(quantVertices);
			grafo.setNomeGrafo(filename.substring(0, filename.length() - 4));

			for (int i = 0; i < quantVertices; i++) {
				String[] buffer = in.nextLine().split(" ");
				int v1 = findByName(buffer[0], vertices).getValorRepresentativo();
				int v2 = findByName(buffer[1], vertices).getValorRepresentativo();
				int peso = Integer.parseInt(buffer[2]);
				grafo.insereArestaNaoOrientada(v1, v2, peso);
			}

			in.close();

		} catch (Exception e) {
			System.out.println("Ocorreu um erro na tentativa de ler o arquivo " + filename + ".");
			// e.printStackTrace();
		}

		return grafo;
	}

	@SuppressWarnings("resource")
	public static Grafo getGrafoOrientado(String filename, ArrayList<Vertice> vertices) {
		Grafo grafo = null;
		try {
			File file = new File(filename);

			// tenta fazer abertura do arquivo
			Scanner in = new Scanner(file);

			int quantVertices = Integer.parseInt(in.nextLine());
			System.out.println(quantVertices);

			grafo = new Grafo(quantVertices);
			grafo.setNomeGrafo(filename.substring(0, filename.length() - 4));

			for (int i = 0; i < quantVertices; i++) {
				String[] buffer = in.nextLine().split(" ");
				int v1 = findByName(buffer[0], vertices).getValorRepresentativo();
				int v2 = findByName(buffer[1], vertices).getValorRepresentativo();
				int peso = Integer.parseInt(buffer[2]);
				grafo.insereAresta(v1, v2, peso);
			}

			in.close();

		} catch (Exception e) {
			System.out.println("Ocorreu um erro na tentativa de ler o arquivo " + filename + ".");
			// e.printStackTrace();
		}

		return grafo;
	}

	public static Vertice findByName(String label, ArrayList<Vertice> vertices) {
		for (Vertice v : vertices) {
			if (label.equals(v.getLabelVertice())) {
				return v;
			}
		}
		return null;
	}

	/**
	 * Retorna uma matriz obtida a partir de um arquivo passado pela entrada
	 * principal.
	 *
	 * @return matriz de inteiros obtida através do arquivo passado pela entrada
	 *         principal
	 */
	public static int[][] getMatrizFromFile() {
		// instancia entrada principal
		Scanner stdin = new Scanner(System.in);

		// nome do arquivo
		String filename;

		// matriz
		int[][] matriz = new int[1][1];

		// arquivo
		File file;

		// flag para verificar se os dados fornecidos vem como diagonal superior ou
		// inferior
		boolean isDiagSup = false;

		System.out.print("Insira o nome do arquivo: ");
		filename = stdin.nextLine();
		System.out.println("");

		file = new File(filename);

		try {
			// tenta fazer abertura do arquivo
			Scanner in = new Scanner(file);

			// lê a primeira linha do arquivo
			String buffer = in.nextLine();

			// enquanto não for o fim do arquivo
			while (in.hasNextLine()) {

				// verifica se a leitura já pode ser finalizada
				if (buffer.contains("EOF") || buffer.contains("DISPLAY_DATA_SECTION")) {
					break;
				}

				// busca a próxima linha
				buffer = in.nextLine();

				// se a palavra "DIMENSION" for encontrada então verifica o nome do arquivo
				// para posterior obtenção da dimensão da matriz
				if (buffer.contains("DIMENSION")) {
					String tamanhoAux = (!filename.contains("pa"))
							? buffer.substring(new String("DIMENSION").length() + 2)
							: buffer.substring(new String("DIMENSION").length() + 3);

					int tamanhoMatriz = Integer.parseInt(tamanhoAux);
					// System.out.println("tamanhoMatriz = " + tamanhoMatriz);
					matriz = new int[tamanhoMatriz][tamanhoMatriz];
				}

				// descobre se os dados fornecidos estão em diagonal inferior
				if (buffer.contains("LOWER_DIAG_ROW")) {
					isDiagSup = false;
				}

				// descobre se os dados fornecidos estão em diagonal superior
				if (buffer.contains("UPPER_DIAG_ROW")) {
					isDiagSup = true;
				}

				// verifica se chegou no trecho do arquivo que contém os dados da matriz
				if (buffer.contains("EDGE_WEIGHT_SECTION")) {
					// se os dados foram fornecidos em diagonal superior
					if (isDiagSup) {
						// percorre o arquivo preenchendo a matriz com índice j iniciando com o valor
						// do índice i
						for (int i = 0; i < matriz.length; i++) {
							for (int j = i; j < matriz.length; j++) {
								matriz[i][j] = matriz[j][i] = in.nextInt();
							}
						}
					} else {
						// percorre o arquivo preenchendo a matriz com índice j finalizando quando for
						// igual ao
						// índice i + 1
						for (int i = 0; i < matriz.length; i++) {
							for (int j = 0; j < i + 1; j++) {
								matriz[i][j] = matriz[j][i] = in.nextInt();
							}
						}
					}
				}
			}

			in.close();

		} catch (FileNotFoundException exception) {
			// Caso o arquivo não tenha sido encontrado, retorna mensagem de erro
			System.out.println("Não foi possível abrir o arquivo " + filename);
			exception.printStackTrace();
		} finally {
			stdin.close();
		}
		
		
		return matriz;
	}

}
