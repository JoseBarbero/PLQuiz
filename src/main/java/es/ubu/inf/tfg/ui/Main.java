package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import es.ubu.inf.tfg.doc.Documento;
import java.awt.Component;
import javax.swing.Box;
import java.awt.FlowLayout;

public class Main {

	private JFrame frmPlquiz;
	private JPanel controlPanel;
	private JPanel vistaPreviaPanel;
	private JScrollPane vistaPreviaScroll;
	private JTextPane vistaPreviaText;
	private JPanel a�adirPanel;
	private JButton a�adirButton;
	private JComboBox<String> a�adirBox;
	private JPanel contenedorPanel;

	private Documento vistaPrevia;
	private Component a�adirDerechoStrut;
	private Component a�adirIzquierdoStrut;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmPlquiz.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
		this.vistaPrevia = Documento.DocumentoHTML();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmPlquiz = new JFrame();
		this.frmPlquiz.setTitle("PLQuiz");
		this.frmPlquiz.setBounds(100, 100, 1100, 900);
		this.frmPlquiz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.controlPanel = new JPanel();
		this.frmPlquiz.getContentPane().add(this.controlPanel, BorderLayout.WEST);
		this.controlPanel.setLayout(new BorderLayout(0, 0));

		this.contenedorPanel = new JPanel();
		this.controlPanel.add(this.contenedorPanel, BorderLayout.NORTH);
		this.contenedorPanel.setLayout(new BoxLayout(this.contenedorPanel,
				BoxLayout.Y_AXIS));

		this.a�adirPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) this.a�adirPanel.getLayout();
		this.controlPanel.add(this.a�adirPanel, BorderLayout.SOUTH);

		this.a�adirButton = new JButton("+");
		this.a�adirButton.addActionListener(new AddButtonActionListener());
		
		this.a�adirIzquierdoStrut = Box.createHorizontalStrut(110);
		this.a�adirPanel.add(this.a�adirIzquierdoStrut);
		this.a�adirPanel.add(this.a�adirButton);

		this.a�adirBox = new JComboBox<>();
		this.a�adirBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Aho-Sethi-Ullman", "Thompson" }));
		this.a�adirPanel.add(this.a�adirBox);
		
		this.a�adirDerechoStrut = Box.createHorizontalStrut(110);
		this.a�adirPanel.add(this.a�adirDerechoStrut);

		this.vistaPreviaPanel = new JPanel();
		this.frmPlquiz.getContentPane().add(this.vistaPreviaPanel,
				BorderLayout.CENTER);
		this.vistaPreviaPanel.setLayout(new BorderLayout(0, 0));

		this.vistaPreviaScroll = new JScrollPane();
		this.vistaPreviaScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.vistaPreviaPanel.add(this.vistaPreviaScroll);

		this.vistaPreviaText = new JTextPane();
		this.vistaPreviaText.setEditable(false);
		this.vistaPreviaText.setContentType("text/html");
		this.vistaPreviaScroll.add(this.vistaPreviaText);
		this.vistaPreviaScroll.setViewportView(this.vistaPreviaText);
	}

	private class AddButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JPanel nuevoPanel = null;

			if (a�adirBox.getSelectedItem().equals("Aho-Sethi-Ullman"))
				nuevoPanel = new AhoSethiUllmanPanel(contenedorPanel,
						vistaPrevia, vistaPreviaText);

			if (nuevoPanel != null) {
				contenedorPanel.add(nuevoPanel);
				contenedorPanel.revalidate();
			}
		}
	}
}
