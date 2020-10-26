package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import DAO.Arquivo;
import Modelagem.Grafo;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = -5564501739744869121L;
	private JPanel contentPane;
	private JPanel painelDoMundo;
	private JComboBox<String> comboBoxAlgoritmosDisponiveis;
	private JComboBox<String> comboBoxGrafosDisponiveis;
	private ArrayList<Pair<String, String>> grafosDisponiveis;
	private ArrayList<String> algoritmosDisponiveis;
	private ArrayList<Vertice> aeroportos;
	private Arquivo DAO;
	private mxGraph graph;

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
				Object v1 = graph.insertVertex(parent, null, v.getLabelVertice(), Math.abs(v.getLatitude())*escala,
						Math.abs(v.getLongitude())*escala, 15, 15, "ROUNDED");
			}
		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setBounds(0, 0, 1016, 500);
		graphComponent.setEnabled(false);
		graphComponent.setBackgroundImage(new ImageIcon(getClass().getResource("/UI/MapaMundo.png")));
		mxGraphModel graphModel = (mxGraphModel) graphComponent.getGraph().getModel();
		Collection<Object> cells = graphModel.getCells().values();
		mxUtils.setCellStyles(graphComponent.getGraph().getModel(), cells.toArray(), mxConstants.STYLE_ENDARROW,
				mxConstants.NONE);
		painelDoMundo.setLayout(null);
		painelDoMundo.add(graphComponent);
		painelDoMundo.revalidate();
		painelDoMundo.repaint();
	}

	private void inicializaMapa() {
		graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		mxStylesheet stylesheet = graph.getStylesheet();
		HashMap<String, Object> style = new HashMap<>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_DOUBLE_RECTANGLE);
		style.put(mxConstants.STYLE_OPACITY, 50);
		style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
		stylesheet.putCellStyle("ROUNDED", style);

		// 1cm ~ 38px
		int escala = 38;

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
		mxGraphModel graphModel = (mxGraphModel) graphComponent.getGraph().getModel();
		Collection<Object> cells = graphModel.getCells().values();
		mxUtils.setCellStyles(graphComponent.getGraph().getModel(), cells.toArray(), mxConstants.STYLE_ENDARROW,
				mxConstants.NONE);
		painelDoMundo.setLayout(null);
		painelDoMundo.add(graphComponent);
	}

	private void initComponents() {
		grafosDisponiveis = new ArrayList<>();
		algoritmosDisponiveis = new ArrayList<>();
		DAO = new Arquivo();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1360, 720);

		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuOpcoes = new JMenu("Op\u00E7\u00F5es");
		menuBar.add(menuOpcoes);

		JMenuItem menuOpcoesInserirGrafoManualmente = new JMenuItem("Inserir grafo manualmente");
		menuOpcoesInserirGrafoManualmente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nomeArq = JOptionPane.showInputDialog("Digite o nome do arquivo com sua respectiva extensão:");

			}
		});

		JMenuItem menuOpcaoInserirAeroportos = new JMenuItem("Inserir aeroportos");
		menuOpcoes.add(menuOpcaoInserirAeroportos);
		menuOpcoes.add(menuOpcoesInserirGrafoManualmente);

		JMenuItem menuOpcoesGerarGrafoAleatorio = new JMenuItem("Gerar grafo aleat\u00F3rio");
		menuOpcoesGerarGrafoAleatorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		menuOpcoes.add(menuOpcoesGerarGrafoAleatorio);

		JMenuItem menuOpcoesGerarGrafoCompleto = new JMenuItem("Gerar grafo completo");
		menuOpcoesGerarGrafoCompleto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		menuOpcoes.add(menuOpcoesGerarGrafoCompleto);

		JMenuItem menuOpcaoSalvarGrafosDisponiveis = new JMenuItem("Salvar grafos dispon\u00EDveis");
		menuOpcoes.add(menuOpcaoSalvarGrafosDisponiveis);

		JMenu menuPersonalizar = new JMenu("Personalizar");
		menuBar.add(menuPersonalizar);

		JMenuItem menuPersonalizarEstiloDoGrafo = new JMenuItem("Estilo do grafo");
		menuPersonalizar.add(menuPersonalizarEstiloDoGrafo);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuAjuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		menuBar.add(menuAjuda);

		JMenu menuSobre = new JMenu("Sobre");
		menuSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		botaoExecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		botaoExecutar.setBounds(79, 31, 176, 55);
		painelControle.add(botaoExecutar);

		JButton botaoExecutarPassoAPasso = new JButton("Executar passo a passo");
		botaoExecutarPassoAPasso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
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
		labelResposta.setBounds(837, 22, 73, 14);
		painelControle.add(labelResposta);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(761, 47, 201, 99);
		painelControle.add(scrollPane);

		JTextArea txtResposta = new JTextArea();
		txtResposta.setForeground(Color.WHITE);
		scrollPane.setViewportView(txtResposta);
		txtResposta.setText("Teste");
		txtResposta.setFont(new Font("Arial", Font.PLAIN, 12));
		txtResposta.setBackground(Color.DARK_GRAY);
		txtResposta.setToolTipText(
				"Aqui aparecer\u00E1 a resposta do algoritmo em formato de texto, podendo ser copiada.");
		txtResposta.setEditable(false);

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

					criaGrafo();
				}
			}
		});

	}

	private void addGrafoDisponivel(String nomeGrafo, String conteudo) {
		grafosDisponiveis.add(new Pair(nomeGrafo, conteudo));
		comboBoxGrafosDisponiveis.addItem(nomeGrafo);
	}

	private void addAlgoritmoDisponivel(String nomeAlgoritmo) {
		algoritmosDisponiveis.add(nomeAlgoritmo);
		comboBoxAlgoritmosDisponiveis.addItem(nomeAlgoritmo);
	}

	private void adicionarAlgoritmosDisponiveis() {
		addAlgoritmoDisponivel("Problema 1");
		addAlgoritmoDisponivel("Problema 2");
		addAlgoritmoDisponivel("Problema 3");
		addAlgoritmoDisponivel("Problema 4");
	}

	public void removeGrafosDisponiveis() {
		comboBoxGrafosDisponiveis.removeAllItems();
	}

	public void salvarGrafosDisponiveis() {

	}

	private double getRandomDoubleBetweenRange(double min, double max) {
		double x = (Math.random() * ((max - min) + 1)) + min;
		return x;
	}

	/**
	 * Gera uma matriz com valores aleatórios entre min e max
	 * 
	 * @param grafo contém a matriz que vai ser gerada
	 * @param min   valor mínimo
	 * @param max   valor máximo
	 */
	private void gerarMatrizAleatoria(Grafo grafo, double min, double max) {
		for (int i = 0; i < grafo.numVertices(); i++) {
			for (int j = i + 1; j < grafo.numVertices(); j++) {
				grafo.insereArestaNaoOrientada(i, j, (int) getRandomDoubleBetweenRange(min, max));
			}
		}
	}

}
