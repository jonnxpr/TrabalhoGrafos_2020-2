package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import Algoritmos.Problema3ForcaBruta;
import Algoritmos.Problema3HeuristicaGuloso;
import Algoritmos.Problema3HeuristicaSecundaria;
import Algoritmos.Problema1ForcaBruta;
import Algoritmos.Problema1Heuristica;
import Algoritmos.Problema2ForcaBruta;
import Algoritmos.Problema2Heuristica;
import DAO.Arquivo;
import Modelagem.Aresta;
import Modelagem.Grafo;
import Modelagem.Problema;
import Modelagem.Solucao;
import Modelagem.Vertice;
import javafx.util.Pair;

/*
	Faixa de valores válidos

	Escala 1cm = 38px

	X:
		26 é o máximo

	Y:
		12 é o máximo
*/

public class Tela extends JFrame {

	private static final long serialVersionUID = -5564501739744869121L;
	private JPanel contentPane;
	private JPanel painelDoMundo;
	private JComboBox<String> comboBoxAlgoritmosDisponiveis;
	private JComboBox<String> comboBoxGrafosDisponiveis;
	private ArrayList<String> algoritmosDisponiveis;
	private ArrayList<Vertice> aeroportos;
	private mxGraph graph;
	private HashMap<String, Grafo> grafosDisponiveis;
	private JTextArea txtResposta;
	private JCheckBox checkDirecionado;
	private boolean direcionado;
	private int[] verticePredecessorLargura;
	private JTextField textFieldVerticeInicial;
	private JTextField textFieldVerticeFinal;
	private boolean problema23Ativado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Tela() {
		direcionado = false;
		problema23Ativado = false;
		setAutoRequestFocus(false);
		setTitle("Trabalho de Grafos - Jonathan Douglas");
		setResizable(false);
		initComponents();
		inicializaMapa();
	}

	@SuppressWarnings("deprecation")
	private void criaGrafo() {
		// Get the components in the panel
		Component[] componentList = painelDoMundo.getComponents();

		// Loop through the components
		for (Component c : componentList) {

			// Find the components you want to remove
			if (c instanceof mxGraphComponent) {

				// Remove it
				painelDoMundo.remove(c);
			}
		}

		painelDoMundo.removeAll();
		graph.removeCells();

		graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		// 1cm ~ 38px
		int escala = 38;

		graph.getModel().beginUpdate();
		try {

			for (Vertice v : aeroportos) {
				@SuppressWarnings("unused")
				Object v1 = graph.insertVertex(parent, null, v.getLabelVertice(), Math.abs(v.getLatitude()) * escala,
						Math.abs(v.getLongitude()) * escala, 15, 15, "ROUNDED");
			}
		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setBounds(0, 0, 1016, 500);
		graphComponent.setEnabled(false);
		graphComponent.setBackgroundImage(new ImageIcon(getClass().getResource("/UI/MapaMundo.png")));
		if (!direcionado) {
			mxGraphModel graphModel = (mxGraphModel) graphComponent.getGraph().getModel();
			Collection<Object> cells = graphModel.getCells().values();
			mxUtils.setCellStyles(graphComponent.getGraph().getModel(), cells.toArray(), mxConstants.STYLE_ENDARROW,
					mxConstants.NONE);
		} else {
			mxGraphModel graphModel = (mxGraphModel) graphComponent.getGraph().getModel();
			Collection<Object> cells = graphModel.getCells().values();
			mxUtils.setCellStyles(graphComponent.getGraph().getModel(), cells.toArray(), mxConstants.EDGESTYLE_ELBOW,
					mxConstants.NONE);
		}

		painelDoMundo.setLayout(null);
		painelDoMundo.add(graphComponent);
		painelDoMundo.revalidate();
		painelDoMundo.repaint();
	}

	@SuppressWarnings("deprecation")
	private void criaGrafo(String nomeGrafo) {
		// Get the components in the panel
		Component[] componentList = painelDoMundo.getComponents();

		// Loop through the components
		for (Component c : componentList) {

			// Find the components you want to remove
			if (c instanceof mxGraphComponent) {

				// Remove it
				painelDoMundo.remove(c);
			}
		}

		painelDoMundo.removeAll();
		graph.removeCells();

		Grafo grafo = grafosDisponiveis.get(nomeGrafo);
		// System.out.println("DEBUG GRAFO: " + nomeGrafo + "\n");
		grafo.imprimeMatrizAdj();

		graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		// 1cm ~ 38px
		int escala = 38;

		ArrayList<Object> verticesA = new ArrayList<>();

		graph.getModel().beginUpdate();
		try {

			for (Vertice v : aeroportos) {
				Object v1 = graph.insertVertex(parent, null,
						v.getLabelVertice() + "(" + v.getValorRepresentativo() + ")",
						Math.abs(v.getLatitude()) * escala, Math.abs(v.getLongitude()) * escala, 15, 15, "ROUNDED");
				verticesA.add(v1);
			}

			Aresta matrizAdj[][] = grafo.getMatriz();
			Locale locale = new Locale("en", "ENGLISH");
			String pattern = "######.##";
			DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
			df.applyPattern(pattern);

			for (int i = 0; i < grafo.numVertices(); i++) {
				for (int j = 0; j < grafo.numVertices(); j++) {
					if (matrizAdj[i][j] != null) {
						graph.insertEdge(parent, null, String.valueOf(df.format(
								(problema23Ativado) ? matrizAdj[i][j].getPreco() : matrizAdj[i][j].getDistancia())),
								verticesA.get(i), verticesA.get(j));
					}
				}
			}

		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setBounds(0, 0, 1016, 500);
		graphComponent.setEnabled(false);
		graphComponent.setBackgroundImage(new ImageIcon(getClass().getResource("/UI/MapaMundo.png")));
		if (!direcionado) {
			mxGraphModel graphModel = (mxGraphModel) graphComponent.getGraph().getModel();
			Collection<Object> cells = graphModel.getCells().values();
			mxUtils.setCellStyles(graphComponent.getGraph().getModel(), cells.toArray(), mxConstants.STYLE_ENDARROW,
					mxConstants.NONE);

		} else {
			mxGraphModel graphModel = (mxGraphModel) graphComponent.getGraph().getModel();
			Collection<Object> cells = graphModel.getCells().values();
			mxUtils.setCellStyles(graphComponent.getGraph().getModel(), cells.toArray(), mxConstants.EDGESTYLE_ELBOW,
					mxConstants.NONE);
		}
		painelDoMundo.setLayout(null);
		painelDoMundo.add(graphComponent);
		painelDoMundo.revalidate();
		painelDoMundo.repaint();
	}

	private void inicializaMapa() {
		graph = new mxGraph();

		mxStylesheet stylesheet = graph.getStylesheet();
		HashMap<String, Object> style = new HashMap<>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_DOUBLE_RECTANGLE);
		style.put(mxConstants.STYLE_OPACITY, 50);
		style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
		stylesheet.putCellStyle("ROUNDED", style);

		graph.getModel().beginUpdate();
		try {
			try {

			} finally {
				graph.getModel().endUpdate();
			}
		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setBounds(0, 0, 1016, 500);
		graphComponent.setEnabled(false);
		graphComponent.setBackgroundImage(new ImageIcon(getClass().getResource("/UI/MapaMundo.png")));
		painelDoMundo.setLayout(null);
		painelDoMundo.add(graphComponent);
	}

	private void initComponents() {
		grafosDisponiveis = new HashMap<>();
		algoritmosDisponiveis = new ArrayList<>();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1360, 720);

		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuOpcoes = new JMenu("Op\u00E7\u00F5es");
		menuBar.add(menuOpcoes);

		JMenuItem menuOpcoesInserirGrafoManualmente = new JMenuItem("Inserir grafo manualmente");

		JMenuItem menuOpcaoInserirAeroportos = new JMenuItem("Inserir aeroportos");
		menuOpcoes.add(menuOpcaoInserirAeroportos);
		menuOpcoes.add(menuOpcoesInserirGrafoManualmente);

		JMenuItem menuOpcoesGerarGrafoAleatorio = new JMenuItem("Gerar grafo aleat\u00F3rio");

		menuOpcoes.add(menuOpcoesGerarGrafoAleatorio);

		JMenuItem menuOpcoesGerarGrafoCompleto = new JMenuItem("Gerar grafo completo");

		menuOpcoes.add(menuOpcoesGerarGrafoCompleto);

		JMenuItem menuOpcaoSalvarGrafosDisponiveis = new JMenuItem("Salvar grafos dispon\u00EDveis");
		menuOpcoes.add(menuOpcaoSalvarGrafosDisponiveis);

		JMenu menuPersonalizar = new JMenu("Personalizar");
		menuBar.add(menuPersonalizar);

		JMenuItem menuPersonalizarEstiloDoGrafo = new JMenuItem("Estilo do grafo");
		menuPersonalizar.add(menuPersonalizarEstiloDoGrafo);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuAjuda.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ImageIcon image = new ImageIcon(getClass().getResource("/UI/interrogation.png"));
				JOptionPane.showMessageDialog(rootPane,
						"Consulte a documentação para mais informações sobre o software.", "Ajuda",
						JOptionPane.INFORMATION_MESSAGE, image);
			}
		});

		menuBar.add(menuAjuda);

		JMenu menuSobre = new JMenu("Sobre");
		menuSobre.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ImageIcon image = new ImageIcon(getClass().getResource("/UI/pikaDasGalaxias.png"));
				JOptionPane.showMessageDialog(rootPane,
						"Este é um trabalho desenvolvido para a disciplina de Grafos que está sendo ministrada"
								+ " no Curso de Ciência da Computação da PUCMG (Unidade Coreu) em 2020/2.\n Seu objetivo é trabalhar de forma prática algoritmos relacionados a problemas na área de Grafos.\n",
						"Sobre", JOptionPane.INFORMATION_MESSAGE, image);
			}
		});

		menuBar.add(menuSobre);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		painelDoMundo = new JPanel();
		painelDoMundo.setBounds(0, 0, 1016, 500);
		contentPane.add(painelDoMundo);

		JPanel painelControle = new JPanel();
		painelControle.setBorder(new TitledBorder(null, "Controle", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		painelControle.setBounds(0, 501, 1354, 173);
		contentPane.add(painelControle);
		painelControle.setLayout(null);

		JButton botaoExecutar = new JButton("Executar");

		botaoExecutar.setBounds(79, 31, 176, 55);
		painelControle.add(botaoExecutar);

		JButton botaoExecutarPassoAPasso = new JButton("Executar passo a passo");

		botaoExecutarPassoAPasso.setBounds(79, 97, 176, 55);
		painelControle.add(botaoExecutarPassoAPasso);

		JLabel labelGrafosDisponiveis = new JLabel("Grafos dispon\u00EDveis");
		labelGrafosDisponiveis.setBounds(309, 22, 125, 14);
		painelControle.add(labelGrafosDisponiveis);

		comboBoxGrafosDisponiveis = new JComboBox<String>();
		comboBoxGrafosDisponiveis.setBounds(286, 47, 153, 22);
		painelControle.add(comboBoxGrafosDisponiveis);

		JLabel labelAlgoritmosDisponiveis = new JLabel("Algoritmos dispon\u00EDveis");
		labelAlgoritmosDisponiveis.setBounds(522, 22, 137, 14);
		painelControle.add(labelAlgoritmosDisponiveis);

		comboBoxAlgoritmosDisponiveis = new JComboBox<String>();
		comboBoxAlgoritmosDisponiveis.setBounds(511, 47, 148, 22);
		painelControle.add(comboBoxAlgoritmosDisponiveis);
		adicionarAlgoritmosDisponiveis();

		JLabel labelResposta = new JLabel("Resposta");
		labelResposta.setBounds(1044, 22, 73, 14);
		painelControle.add(labelResposta);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(761, 47, 583, 115);
		painelControle.add(scrollPane);

		txtResposta = new JTextArea();
		txtResposta.setColumns(50);
		txtResposta.setRows(6);
		txtResposta.setForeground(Color.WHITE);
		scrollPane.setViewportView(txtResposta);
		txtResposta.setFont(new Font("Arial", Font.PLAIN, 12));
		txtResposta.setBackground(Color.DARK_GRAY);
		txtResposta.setToolTipText(
				"Aqui aparecer\u00E1 a resposta do algoritmo em formato de texto, podendo ser copiada.");
		txtResposta.setEditable(false);

		JButton botaoCarregarGrafo = new JButton("Carregar grafo");

		botaoCarregarGrafo.setBounds(300, 80, 119, 23);
		painelControle.add(botaoCarregarGrafo);

		checkDirecionado = new JCheckBox("Direcionado?");
		checkDirecionado.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (checkDirecionado.isSelected()) {
					direcionado = true;
				} else {
					direcionado = false;
				}
			}
		});
		checkDirecionado.setBounds(540, 113, 119, 23);
		painelControle.add(checkDirecionado);

		textFieldVerticeInicial = new JTextField();
		textFieldVerticeInicial.setBounds(286, 142, 86, 20);
		painelControle.add(textFieldVerticeInicial);
		textFieldVerticeInicial.setColumns(10);

		textFieldVerticeFinal = new JTextField();
		textFieldVerticeFinal.setBounds(382, 142, 86, 20);
		painelControle.add(textFieldVerticeFinal);
		textFieldVerticeFinal.setColumns(10);

		JLabel labelVerticeInicial = new JLabel("Vértice inicial");
		labelVerticeInicial.setBounds(286, 117, 86, 14);
		painelControle.add(labelVerticeInicial);

		JLabel labelVerticeFinal = new JLabel("Vértice final");
		labelVerticeFinal.setBounds(382, 117, 86, 14);
		painelControle.add(labelVerticeFinal);

		JPanel painelAeroportos = new JPanel();
		painelAeroportos
				.setBorder(new TitledBorder(null, "Aeroportos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		painelAeroportos.setBounds(1018, 0, 336, 500);
		contentPane.add(painelAeroportos);
		painelAeroportos.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 24, 316, 465);
		painelAeroportos.add(scrollPane_1);

		JTextArea textAreaAeroportos = new JTextArea();
		textAreaAeroportos.setRows(10000);
		textAreaAeroportos.setColumns(50);
		textAreaAeroportos.setEditable(false);
		textAreaAeroportos.setForeground(Color.WHITE);
		textAreaAeroportos.setBackground(Color.DARK_GRAY);
		scrollPane_1.setViewportView(textAreaAeroportos);

		menuOpcaoInserirAeroportos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textAreaAeroportos.setText("");

				String nomeArq = JOptionPane.showInputDialog(rootPane,
						"Digite o nome do arquivo com sua respectiva extensão:");
				aeroportos = Arquivo.getVertices(nomeArq);

				if (aeroportos.isEmpty()) {
					JOptionPane.showMessageDialog(rootPane, "Você não carregou nenhum arquivo ou houve algum erro.");
				} else {
					for (Vertice v : aeroportos) {
						textAreaAeroportos.setText(textAreaAeroportos.getText() + v.toString() + "\n\n");
					}
					grafosDisponiveis.clear();
					comboBoxGrafosDisponiveis.removeAllItems();
					criaGrafo();
				}
			}
		});

		menuOpcoesInserirGrafoManualmente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Grafo grafo = null;
				if (aeroportos == null || aeroportos.isEmpty()) {
					JOptionPane.showMessageDialog(rootPane,
							"Você precisa carregar os aeroportos antes de inserir o grafo.");
				} else {
					String nomeArq = JOptionPane
							.showInputDialog("Digite o nome do arquivo com sua respectiva extensão:");
					grafo = ((direcionado) ? Arquivo.getGrafoOrientado(nomeArq, aeroportos)
							: Arquivo.getGrafoNaoOrientado(nomeArq, aeroportos));
					if (grafo == null) {
						JOptionPane.showMessageDialog(rootPane,
								"Você não inseriu um arquivo válido ou houve algum erro.");
					} else {
						addGrafoDisponivel(grafo.getNomeGrafo(), grafo);
						criaGrafo(grafo.getNomeGrafo());
					}
				}
			}
		});

		botaoExecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBoxGrafosDisponiveis.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(rootPane, "Não é possível executar sem selecionar um grafo.");
				} else {
					execProblemas(String.valueOf(comboBoxAlgoritmosDisponiveis.getSelectedItem()),
							String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem()));
				}
			}
		});

		botaoExecutarPassoAPasso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		menuOpcoesGerarGrafoAleatorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (aeroportos == null || aeroportos.isEmpty()) {
					JOptionPane.showMessageDialog(rootPane,
							"Você precisa carregar os aeroportos antes de inserir o grafo.");
				} else {
					Grafo grafo = gerarGrafoAleatorio(aeroportos.size());
					if (grafo == null) {
						JOptionPane.showMessageDialog(rootPane, "Houve algum erro.");
					} else {
						addGrafoDisponivel(grafo.getNomeGrafo(), grafo);
						criaGrafo(grafo.getNomeGrafo());
					}
				}
			}
		});

		menuOpcoesGerarGrafoCompleto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (aeroportos == null || aeroportos.isEmpty()) {
					JOptionPane.showMessageDialog(rootPane,
							"Você precisa carregar os aeroportos antes de inserir o grafo.");
				} else {
					Grafo grafo = gerarGrafoCompleto(aeroportos.size());
					if (grafo == null) {
						JOptionPane.showMessageDialog(rootPane, "Houve algum erro.");
					} else {
						addGrafoDisponivel(grafo.getNomeGrafo(), grafo);
						criaGrafo(grafo.getNomeGrafo());
					}
				}
			}
		});

		botaoCarregarGrafo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBoxGrafosDisponiveis.getSelectedItem() != null) {
					String nomeGrafo = String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem());
					criaGrafo(nomeGrafo);
				} else {
					JOptionPane.showMessageDialog(rootPane, "Você precisa selecionar um grafo para carregar.");
				}
			}
		});

	}

	private void addGrafoDisponivel(String nomeGrafo, Grafo grafo) {
		if (!grafosDisponiveis.containsKey(nomeGrafo)) {
			grafosDisponiveis.put(nomeGrafo, grafo);
			comboBoxGrafosDisponiveis.addItem(nomeGrafo);
		}
	}

	private void addAlgoritmoDisponivel(String nomeAlgoritmo) {
		algoritmosDisponiveis.add(nomeAlgoritmo);
		comboBoxAlgoritmosDisponiveis.addItem(nomeAlgoritmo);
	}

	private void adicionarAlgoritmosDisponiveis() {
		addAlgoritmoDisponivel("Problema 1 - Forca Bruta");
		addAlgoritmoDisponivel("Problema 1 - Heuristica");
		addAlgoritmoDisponivel("Problema 2 - Forca Bruta");
		addAlgoritmoDisponivel("Problema 2 - Heuristica");
		addAlgoritmoDisponivel("Problema 3 - Forca Bruta e Heuristica");
		addAlgoritmoDisponivel("Problema 4 - Forca Bruta");
		addAlgoritmoDisponivel("Problema 4 - Heuristica");
		addAlgoritmoDisponivel("Busca em Largura");
	}

	public void removeGrafosDisponiveis() {
		grafosDisponiveis.clear();
		comboBoxGrafosDisponiveis.removeAllItems();
	}

	public void salvarGrafosDisponiveis() {

	}

	private double getRandomDoubleBetweenRange(double min, double max) {

		double x = (Math.random() * ((max - min) + 1)) + min;
		return x;
	}

	private void gerarMatrizAleatoria(Grafo grafo, double min, double max) {
		for (int i = 0; i < grafo.numVertices(); i++) {
			for (int j = i + 1; j < grafo.numVertices(); j++) {
				if ((int) getRandomDoubleBetweenRange(1, 50000) >= (int) getRandomDoubleBetweenRange(1, 50000)) {
					grafo.insereArestaNaoOrientada(new Vertice(i, 0, 0), new Vertice(j, 0, 0), 10000,
							getRandomDoubleBetweenRange(min, max), (int) getRandomDoubleBetweenRange(min, max));
				}
			}
		}
	}

	/**
	 * Gera uma matriz com valores aleatórios entre min e max
	 * 
	 * @param grafo contém a matriz que vai ser gerada
	 * @param min   valor mínimo
	 * @param max   valor máximo
	 */
	private void gerarMatrizCompleta(Grafo grafo, double min, double max) {
		for (int i = 0; i < grafo.numVertices(); i++) {
			for (int j = i + 1; j < grafo.numVertices(); j++) {
				grafo.insereArestaNaoOrientada(new Vertice(i, 0, 0), new Vertice(j, 0, 0), 10000,
						getRandomDoubleBetweenRange(min, max), (int) getRandomDoubleBetweenRange(min, max));
			}
		}
	}

	private Grafo gerarGrafoAleatorio(int numVertices) {
		Grafo grafo = new Grafo(numVertices);
		grafo.setNomeGrafo("AleatorioTeste" + String.valueOf((int) getRandomDoubleBetweenRange(0, 500)));
		gerarMatrizAleatoria(grafo, getRandomDoubleBetweenRange(0, 100), getRandomDoubleBetweenRange(101, 1000));
		return grafo;
	}

	private Grafo gerarGrafoCompleto(int numVertices) {
		Grafo grafo = new Grafo(numVertices);
		grafo.setNomeGrafo("CompletoTeste" + String.valueOf((int) getRandomDoubleBetweenRange(0, 500)));
		gerarMatrizCompleta(grafo, getRandomDoubleBetweenRange(0, 100), getRandomDoubleBetweenRange(101, 1000));
		return grafo;
	}

	private void execProblemas(String labelProblema, String nomeGrafo) {
		switch (labelProblema) {
		case "Problema 1 - Forca Bruta":
			execProblema1ForcaBruta();
			break;

		case "Problema 1 - Heuristica":
			execProblema1Heuristica();
			break;

		case "Problema 2 - Forca Bruta":
			execProblema2ForcaBruta();
			break;

		case "Problema 2 - Heuristica":
			execProblema2Heuristica();
			break;

		case "Problema 3 - Forca Bruta e Heuristica":
			execProblema3ForcaBrutaHeuristica();
			break;

		case "Problema 4 - Forca Bruta":
			execProblema4ForcaBruta();
			break;

		case "Problema 4 - Heuristica":
			execProblema4Heuristica();
			break;
		case "Busca em Largura":
			buscaEmLargura();
			break;
		default:
			break;
		}
	}

	private void execProblema1ForcaBruta() {
		verticePredecessorLargura = new int[grafosDisponiveis
				.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem())).numVertices()];
		if (textFieldVerticeInicial.getText() == null || textFieldVerticeFinal.getText() == null
				|| textFieldVerticeInicial.getText().isEmpty() || textFieldVerticeFinal.getText().isEmpty()
				|| !isNumeric(textFieldVerticeInicial.getText()) || !isNumeric(textFieldVerticeFinal.getText())
				|| Integer.valueOf(textFieldVerticeInicial.getText()) < 0
				|| Integer.valueOf(textFieldVerticeInicial.getText()) > aeroportos.size() - 1
				|| Integer.valueOf(textFieldVerticeFinal.getText()) < 0
				|| Integer.valueOf(textFieldVerticeFinal.getText()) > aeroportos.size() - 1) {
			JOptionPane.showMessageDialog(rootPane,
					"Você deve preencher o vértice inicial e final com valores válidos.");
			return;
		}

		if (iniciaBuscaEmLargura(Integer.valueOf(textFieldVerticeInicial.getText()),
				Integer.valueOf(textFieldVerticeFinal.getText())) == false) {
			JOptionPane.showMessageDialog(rootPane,
					"Não é possível fazer a operação desejada pois não existe um caminho entre o vertice inicial e final.");
			return;
		}

		int aeroportoInicial = Integer.parseInt(textFieldVerticeInicial.getText());
		int aeroportoFinal = Integer.parseInt(textFieldVerticeFinal.getText());

		Problema1ForcaBruta forcaBruta1;

		Problema problema;
		Solucao solucao;

		Grafo grafo = grafosDisponiveis.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem()));

		long tempoInicial;
		long tempoFinal;

		problema = new Problema(grafo);

		forcaBruta1 = new Problema1ForcaBruta(problema);

		tempoInicial = System.currentTimeMillis();

		solucao = forcaBruta1.getSolucao(aeroportoInicial, aeroportoFinal);

		tempoFinal = System.currentTimeMillis();

		// habilitar a linha abaixo caso deseje ver o caminho o obtido pela solução
		solucao.mostrarCaminho();
		txtResposta.setText("\n\nSolução = " + solucao.getCaminho());
		txtResposta.setText(txtResposta.getText() + "\n\nSolução Força Bruta - Aeroporto inicial (" + aeroportoInicial
				+ "): " + solucao.getDistanciaTotal());

		System.out.printf("Tempo total de execução: %.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
		System.out.println("");

	}

	private void execProblema1Heuristica() {
		verticePredecessorLargura = new int[grafosDisponiveis
				.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem())).numVertices()];
		if (textFieldVerticeInicial.getText() == null || textFieldVerticeFinal.getText() == null
				|| textFieldVerticeInicial.getText().isEmpty() || textFieldVerticeFinal.getText().isEmpty()
				|| !isNumeric(textFieldVerticeInicial.getText()) || !isNumeric(textFieldVerticeFinal.getText())
				|| Integer.valueOf(textFieldVerticeInicial.getText()) < 0
				|| Integer.valueOf(textFieldVerticeInicial.getText()) > aeroportos.size() - 1
				|| Integer.valueOf(textFieldVerticeFinal.getText()) < 0
				|| Integer.valueOf(textFieldVerticeFinal.getText()) > aeroportos.size() - 1) {
			JOptionPane.showMessageDialog(rootPane,
					"Você deve preencher o vértice inicial e final com valores válidos.");
			return;
		}

		if (iniciaBuscaEmLargura(Integer.valueOf(textFieldVerticeInicial.getText()),
				Integer.valueOf(textFieldVerticeFinal.getText())) == false) {
			JOptionPane.showMessageDialog(rootPane,
					"Não é possível fazer a operação desejada pois não existe um caminho entre o vertice inicial e final.");
			return;
		}

		int aeroportoInicial = Integer.parseInt(textFieldVerticeInicial.getText());
		int aeroportoFinal = Integer.parseInt(textFieldVerticeFinal.getText());

		Problema1Heuristica heuristica1;

		Problema problema;
		Solucao solucao;

		Grafo grafo = grafosDisponiveis.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem()));

		long tempoInicial;
		long tempoFinal;

		problema = new Problema(grafo);

		heuristica1 = new Problema1Heuristica(problema);

		tempoInicial = System.currentTimeMillis();

		solucao = heuristica1.getSolucao(aeroportoInicial, aeroportoFinal);

		tempoFinal = System.currentTimeMillis();

		// habilitar a linha abaixo caso deseje ver o caminho o obtido pela solução
		solucao.mostrarCaminho();
		txtResposta.setText("\n\nSolução = " + solucao.getCaminho());
		txtResposta.setText(txtResposta.getText() + "\n\nSolução Heurística - Aeroporto inicial (" + aeroportoInicial
				+ "): " + solucao.getDistanciaTotal());

		System.out.printf("Tempo total de execução: %.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
		System.out.println("");
	}

	@SuppressWarnings("unused")
	private void execProblema2ForcaBruta() {
		// System.out.println("2 forca bruta");
		problema23Ativado = true;
		if (comboBoxGrafosDisponiveis.getSelectedItem() != null) {
			String nomeGrafo = String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem());
			criaGrafo(nomeGrafo);

		} else {
			JOptionPane.showMessageDialog(rootPane, "Você precisa selecionar um grafo para carregar.");
			return;
		}

		verticePredecessorLargura = new int[grafosDisponiveis
				.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem())).numVertices()];
		if (textFieldVerticeInicial.getText() == null || textFieldVerticeFinal.getText() == null
				|| textFieldVerticeInicial.getText().isEmpty() || textFieldVerticeFinal.getText().isEmpty()
				|| !isNumeric(textFieldVerticeInicial.getText()) || !isNumeric(textFieldVerticeFinal.getText())
				|| Integer.valueOf(textFieldVerticeInicial.getText()) < 0
				|| Integer.valueOf(textFieldVerticeInicial.getText()) > aeroportos.size() - 1
				|| Integer.valueOf(textFieldVerticeFinal.getText()) < 0
				|| Integer.valueOf(textFieldVerticeFinal.getText()) > aeroportos.size() - 1) {
			JOptionPane.showMessageDialog(rootPane,
					"Você deve preencher o vértice inicial e final com valores válidos.");
			return;
		}

		if (iniciaBuscaEmLargura(Integer.valueOf(textFieldVerticeInicial.getText()),
				Integer.valueOf(textFieldVerticeFinal.getText())) == false) {
			JOptionPane.showMessageDialog(rootPane,
					"Não é possível fazer a operação desejada pois não existe um caminho entre o vertice inicial e final.");
			return;
		}

		int aeroportoInicial = Integer.parseInt(textFieldVerticeInicial.getText());
		int aeroportoFinal = Integer.parseInt(textFieldVerticeFinal.getText());

		Problema2ForcaBruta forcaBruta2;

		Problema problema;
		Solucao solucao;

		Grafo grafo = grafosDisponiveis.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem()));

		long tempoInicial;
		long tempoFinal;

		problema = new Problema(grafo);

		forcaBruta2 = new Problema2ForcaBruta(problema);

		tempoInicial = System.currentTimeMillis();

		solucao = forcaBruta2.getSolucao(aeroportoInicial, aeroportoFinal);

		tempoFinal = System.currentTimeMillis();

		// habilitar a linha abaixo caso deseje ver o caminho o obtido pela solução
		solucao.mostrarCaminho();
		txtResposta.setText("\n\nSolução = " + solucao.getCaminho());
		txtResposta.setText(txtResposta.getText() + "\n\nSolução Força Bruta - Aeroporto inicial (" + aeroportoInicial
				+ "): " + solucao.getPrecoTotal());

		System.out.printf("Tempo total de execução: %.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
		System.out.println("");

		problema23Ativado = false;
	}

	private void execProblema2Heuristica() {
		// System.out.println("2 heuristica");
		problema23Ativado = true;
		if (comboBoxGrafosDisponiveis.getSelectedItem() != null) {
			String nomeGrafo = String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem());
			criaGrafo(nomeGrafo);
		} else {
			JOptionPane.showMessageDialog(rootPane, "Você precisa selecionar um grafo para carregar.");
		}

		verticePredecessorLargura = new int[grafosDisponiveis
				.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem())).numVertices()];
		if (textFieldVerticeInicial.getText() == null || textFieldVerticeFinal.getText() == null
				|| textFieldVerticeInicial.getText().isEmpty() || textFieldVerticeFinal.getText().isEmpty()
				|| !isNumeric(textFieldVerticeInicial.getText()) || !isNumeric(textFieldVerticeFinal.getText())
				|| Integer.valueOf(textFieldVerticeInicial.getText()) < 0
				|| Integer.valueOf(textFieldVerticeInicial.getText()) > aeroportos.size() - 1
				|| Integer.valueOf(textFieldVerticeFinal.getText()) < 0
				|| Integer.valueOf(textFieldVerticeFinal.getText()) > aeroportos.size() - 1) {
			JOptionPane.showMessageDialog(rootPane,
					"Você deve preencher o vértice inicial e final com valores válidos.");
			return;
		}

		if (iniciaBuscaEmLargura(Integer.valueOf(textFieldVerticeInicial.getText()),
				Integer.valueOf(textFieldVerticeFinal.getText())) == false) {
			JOptionPane.showMessageDialog(rootPane,
					"Não é possível fazer a operação desejada pois não existe um caminho entre o vertice inicial e final.");
			return;
		}

		int aeroportoInicial = Integer.parseInt(textFieldVerticeInicial.getText());
		int aeroportoFinal = Integer.parseInt(textFieldVerticeFinal.getText());

		Problema2Heuristica heuristica2;

		Problema problema;
		Solucao solucao;

		Grafo grafo = grafosDisponiveis.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem()));

		long tempoInicial;
		long tempoFinal;

		problema = new Problema(grafo);

		heuristica2 = new Problema2Heuristica(problema);

		tempoInicial = System.currentTimeMillis();

		solucao = heuristica2.getSolucao(aeroportoInicial, aeroportoFinal);

		tempoFinal = System.currentTimeMillis();

		// habilitar a linha abaixo caso deseje ver o caminho o obtido pela solução
		solucao.mostrarCaminho();
		txtResposta.setText("\n\nSolução = " + solucao.getCaminho());
		txtResposta.setText(txtResposta.getText() + "\n\nSolução Heurística - Aeroporto inicial (" + aeroportoInicial
				+ "): " + solucao.getPrecoTotal());

		System.out.printf("Tempo total de execução: %.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
		System.out.println("");
		problema23Ativado = false;
	}

	@SuppressWarnings("unused")
	private void execProblema3ForcaBrutaHeuristica() {
		problema23Ativado = true;
		if (comboBoxGrafosDisponiveis.getSelectedItem() != null) {
			String nomeGrafo = String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem());
			criaGrafo(nomeGrafo);

		} else {
			JOptionPane.showMessageDialog(rootPane, "Você precisa selecionar um grafo para carregar.");
			return;
		}

		verticePredecessorLargura = new int[grafosDisponiveis
				.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem())).numVertices()];
		if (textFieldVerticeInicial.getText() == null || textFieldVerticeInicial.getText().isEmpty()
				|| !isNumeric(textFieldVerticeInicial.getText())
				|| Integer.valueOf(textFieldVerticeInicial.getText()) < 0
				|| Integer.valueOf(textFieldVerticeInicial.getText()) > aeroportos.size() - 1) {
			JOptionPane.showMessageDialog(rootPane,
					"Você deve preencher o vértice inicial e final com valores válidos.");
			return;
		}
		int aeroportoInicial = Integer.parseInt(textFieldVerticeInicial.getText()); // aeroporto inicial do caminho
		// algoritmos
		Problema3HeuristicaGuloso guloso;
		Problema3ForcaBruta forcaBruta;
		Problema3HeuristicaSecundaria heuristica;

		// problema e solução
		Problema problema;
		Solucao solucao;

		Grafo grafo = grafosDisponiveis.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem()));

		long tempoInicial;
		long tempoFinal;

		problema = new Problema(grafo);
		guloso = new Problema3HeuristicaGuloso(problema);
		forcaBruta = new Problema3ForcaBruta(problema);
		heuristica = new Problema3HeuristicaSecundaria(problema);

		/**
		 * ************************************* Solução Força bruta Máx cidades: 11
		 * 
		 * Comentar trecho de código caso não vá utilizar o algoritmo
		 *************************************
		 */
		tempoInicial = System.currentTimeMillis();

		solucao = forcaBruta.getSolucao(aeroportoInicial);

		tempoFinal = System.currentTimeMillis();

		// habilitar a linha abaixo caso deseje ver o caminho o obtido pela solução
		solucao.mostrarCaminho();

		txtResposta.setText("\n\nSolução Força Bruta - Aeroporto inicial (" + aeroportoInicial + "): "
				+ solucao.getPrecoTotal() + "\n" + solucao.mostrarCaminho());

		System.out.printf("Tempo total de execução: %.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
		System.out.println("");

		/**
		 * ************************************* Solução Heurística Gulosa Vizinho mais
		 * próximo
		 * 
		 * Comentar trecho de código caso não vá utilizar o algoritmo
		 *************************************
		 */
		tempoInicial = System.currentTimeMillis();

		solucao = guloso.getSolucao(aeroportoInicial);

		tempoFinal = System.currentTimeMillis();

		// habilitar a linha abaixo caso deseje ver o caminho o obtido pela solução
		solucao.mostrarCaminho();

		txtResposta.setText(
				txtResposta.getText() + "\n\nSolução Heurística Gulosa(Vizinho mais próximo) - Aeroporto inicial ("
						+ aeroportoInicial + "): " + solucao.getPrecoTotal() + "\n" + solucao.mostrarCaminho());

		System.out.printf("Tempo total de execução: %.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
		System.out.println("");

		/**
		 * ************************************* Solução Heurística Inserção mais barata
		 * 
		 * Comentar trecho de código caso não vá utilizar o algoritmo
		 *************************************
		 */
		/*
		 * tempoInicial = System.currentTimeMillis();
		 * 
		 * solucao = heuristica.getSolucao(aeroportoInicial);
		 * 
		 * tempoFinal = System.currentTimeMillis();
		 * 
		 * // habilitar a linha abaixo caso deseje ver o caminho o obtido pela solução
		 * solucao.mostrarCaminho();
		 * 
		 * txtResposta.setText(txtResposta.getText() +
		 * "\n\nSolução Heurística(Inserção mais barata) - Aeroporto inicial (" +
		 * aeroportoInicial + "): " + solucao.getDistanciaTotal());
		 * 
		 * System.out.printf("Tempo total de execução: %.3f ms%n", (tempoFinal -
		 * tempoInicial) / 1000d);
		 */
		problema23Ativado = false;
	}

	@SuppressWarnings("unused")
	private void execProblema4ForcaBruta() {
		System.out.println("4 forca bruta");
	}

	private void execProblema4Heuristica() {
		System.out.println("4 heuristica");
	}

	private void buscaEmLargura() {
		if (textFieldVerticeInicial.getText() == null || textFieldVerticeFinal.getText() == null
				|| textFieldVerticeInicial.getText().isEmpty() || textFieldVerticeFinal.getText().isEmpty()
				|| !isNumeric(textFieldVerticeInicial.getText()) || !isNumeric(textFieldVerticeFinal.getText())
				|| Integer.valueOf(textFieldVerticeInicial.getText()) < 0
				|| Integer.valueOf(textFieldVerticeInicial.getText()) > aeroportos.size() - 1
				|| Integer.valueOf(textFieldVerticeFinal.getText()) < 0
				|| Integer.valueOf(textFieldVerticeFinal.getText()) > aeroportos.size() - 1) {
			JOptionPane.showMessageDialog(rootPane,
					"Você deve preencher o vértice inicial e final com valores válidos.");
			return;
		}
		txtResposta.setText(
				"Existe caminho? -> " + iniciaBuscaEmLargura(Integer.parseInt(textFieldVerticeInicial.getText()),
						Integer.parseInt(textFieldVerticeFinal.getText())));
	}

	/**
	 * ********************************************************************* BUSCA
	 * EM LARGURA
	 *********************************************************************
	 */

	/**
	 * MÃ©todo iniciaBuscaEmLargura, responsÃ¡vel por inicializar o array de
	 * vÃ©rtices predecessores a ser preenchido durante a busca.
	 * 
	 * @param verticeInicial
	 * @param verticeFinal
	 * @return booleano indicando se existe um caminho entre o vÃ©rtice inicial e
	 *         final
	 */
	public boolean iniciaBuscaEmLargura(int verticeInicial, int verticeFinal) {
		verticePredecessorLargura = new int[grafosDisponiveis
				.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem())).numVertices()];
		int tamanhoVetor = verticePredecessorLargura.length;

		// Percorre o vetor de distÃ¢ncias inicializando todas as posiÃ§Ãµes com o valor
		// V+1
		for (int i = 0; i < tamanhoVetor; i++) {
			// diz que o predecessor nÃ£o existe ainda
			verticePredecessorLargura[i] = -1;
		}
		// chama a busca em profundidade
		return buscaLargura(verticeInicial, verticeFinal);
	}

	/**
	 * MÃ©todo buscaLargura, responsÃ¡vel por efetuar a busca em largura a partir de
	 * um vÃ©rtice inicial atÃ© o vÃ©rtice final utilizando uma fila de vÃ©rtices a
	 * serem conhecidos e um vetor de vÃ©rtices jÃ¡ visitados.
	 * 
	 * @param verticeInicial
	 * @param verticeFinal
	 * @return
	 */
	private boolean buscaLargura(int verticeInicial, int verticeFinal) {
		Grafo grafo = grafosDisponiveis.get(String.valueOf(comboBoxGrafosDisponiveis.getSelectedItem()));
		boolean visitados[] = new boolean[grafo.numVertices()];

		// seta os vÃ©rtices como nÃ£o visitados
		for (int i = 0; i < visitados.length; i++) {
			visitados[i] = false;
		}

		// cria a lista de vÃ©rtices a serem conhecidos
		LinkedList<Integer> fila = new LinkedList<>();

		// marca o vÃ©rtice inicial como visitado
		visitados[verticeInicial] = true;

		// adiciona o vÃ©rtice inicial na fila de vÃ©rtices conhecidos
		fila.add(verticeInicial);

		// enquanto a fila de vÃ©rtices conhecidos nÃ£o estiver vazia
		while (!fila.isEmpty()) {
			// pega o elemento presente no topo (posiÃ§Ã£o inicial) da fila
			int v = fila.poll();
			// System.out.println("vAtual = " + v);

			// obtÃ©m a lista de adjacÃªncia do vÃ©rtice obtido da fila
			ArrayList<Integer> listaAdj = grafo.listaDeAdjacencia(v);

			// percorre a lista de adjacÃªncia
			for (int w = 0; w < listaAdj.size(); w++) {
				// se o vÃ©rtice atual nÃ£o foi visitado ainda, entÃ£o o visite,
				// marque que foi visitado e o adicione na lista de vÃ©rtices a serem conhecidos
				// seta tambÃ©m o vÃ©rtice predecessor
				if (visitados[listaAdj.get(w)] == false) {
					visitados[listaAdj.get(w)] = true;
					fila.add(listaAdj.get(w));

					verticePredecessorLargura[listaAdj.get(w)] = v;
				}
			}
		}

		// retorna se existe um caminho entre o vÃ©rtice final e inicial
		return (visitados[verticeFinal] == true);
	}

	/**
	 * MÃ©todo obtemCaminhoLargura, responsÃ¡vel por obter o caminho enter o
	 * vÃ©rtice inicial e final da busca baseado no array de vÃ©rtice predecessor
	 * resultante da busca.
	 * 
	 * @param verticeInicial
	 * @param verticeFinal
	 * @return ArrayList de Pair contendo o caminho obtido da busca em largura entre
	 *         o vÃ©rtice inicial e final
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

	private boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
