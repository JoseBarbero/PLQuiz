package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllmanGenerador;

public class AhoSethiUllmanPanel extends JPanel {

	private static final long serialVersionUID = -8899275410326830826L;

	private final JPanel contenedorPanel;
	private final JPanel actualPanel = this;
	private final Documento documento;
	private final JTextPane vistaPrevia;
	private AhoSethiUllman problemaActual = null;

	private JPanel expresionPanel;
	private JTextField expresionText;
	private JPanel botonesPanel;
	private JButton generarButton;
	private JButton resolverButton;
	private JButton borrarButton;
	private JPanel opcionesPanel;
	private JPanel vacioPanel;
	private JCheckBox vacioCheck;
	private JPanel simbolosPanel;
	private JLabel simbolosLabel;
	private JSlider simbolosSlider;
	private JLabel simbolosEstadoLabel;
	private JPanel estadosPanel;
	private JLabel estadosLabel;
	private JSlider estadosSlider;
	private JLabel estadosEstadoLabel;
	private JPanel progresoPanel;
	private JProgressBar progresoBar;

	public AhoSethiUllmanPanel(JPanel contenedor, Documento documento,
			JTextPane vistaPrevia) {

		this.contenedorPanel = contenedor;
		this.documento = documento;
		this.vistaPrevia = vistaPrevia;

		setBorder(new CompoundBorder(new EmptyBorder(5, 5, 15, 5),
				new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
						"Aho-Sethi-Ullman", TitledBorder.LEADING,
						TitledBorder.TOP, null, null)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.expresionPanel = new JPanel();
		add(this.expresionPanel);
		this.expresionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.borrarButton = new JButton("-");
		this.borrarButton.addActionListener(new BotonBorrarActionListener());
		this.expresionPanel.add(this.borrarButton);

		this.expresionText = new JTextField();
		this.expresionText.addActionListener(new BotonResolverActionListener());
		this.expresionPanel.add(this.expresionText);
		this.expresionText.setColumns(40);

		this.botonesPanel = new JPanel();
		add(this.botonesPanel);

		this.generarButton = new JButton("Generar");
		this.generarButton.addActionListener(new BotonGenerarActionListener());
		this.botonesPanel.add(this.generarButton);

		this.resolverButton = new JButton("Resolver");
		this.resolverButton
				.addActionListener(new BotonResolverActionListener());
		this.botonesPanel.add(this.resolverButton);

		this.opcionesPanel = new JPanel();
		add(this.opcionesPanel);
		this.opcionesPanel.setLayout(new BoxLayout(this.opcionesPanel,
				BoxLayout.Y_AXIS));

		this.vacioPanel = new JPanel();
		this.opcionesPanel.add(this.vacioPanel);

		this.vacioCheck = new JCheckBox("Incluir s\u00EDmbolo vac\u00EDo");
		this.vacioPanel.add(this.vacioCheck);

		this.simbolosPanel = new JPanel();
		this.opcionesPanel.add(this.simbolosPanel);

		this.simbolosLabel = new JLabel("S\u00EDmbolos");
		this.simbolosPanel.add(this.simbolosLabel);

		this.simbolosSlider = new JSlider();
		this.simbolosSlider.setValue(3);
		this.simbolosSlider.setMaximum(10);
		this.simbolosSlider.setMinimum(1);
		this.simbolosSlider.addChangeListener(new SliderChangeListener());
		this.simbolosPanel.add(this.simbolosSlider);

		this.simbolosEstadoLabel = new JLabel("3");
		this.simbolosPanel.add(this.simbolosEstadoLabel);

		this.estadosPanel = new JPanel();
		this.opcionesPanel.add(this.estadosPanel);

		this.estadosLabel = new JLabel("Estados");
		this.estadosPanel.add(this.estadosLabel);

		this.estadosSlider = new JSlider();
		this.estadosSlider.setValue(5);
		this.estadosSlider.setMinimum(3);
		this.estadosSlider.setMaximum(21);
		this.estadosSlider.addChangeListener(new SliderChangeListener());
		this.estadosPanel.add(this.estadosSlider);

		this.estadosEstadoLabel = new JLabel("5");
		this.estadosPanel.add(this.estadosEstadoLabel);
		
		this.progresoPanel = new JPanel();
		this.opcionesPanel.add(this.progresoPanel);
		this.progresoPanel.setLayout(new BorderLayout(0, 0));
		
		this.progresoBar = new JProgressBar();
		this.progresoBar.setVisible(false);
		this.progresoBar.setIndeterminate(true);
		this.progresoPanel.add(this.progresoBar, BorderLayout.CENTER);

	}

	private class SliderChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			JSlider source = (JSlider) event.getSource();
			if (source == simbolosSlider)
				simbolosEstadoLabel.setText("" + simbolosSlider.getValue());
			else if (source == estadosSlider)
				estadosEstadoLabel.setText("" + estadosSlider.getValue());
		}
	}

	private class BotonGenerarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			SwingWorker<AhoSethiUllman, Void> worker = new SwingWorker<AhoSethiUllman, Void>() {

				@Override
				protected AhoSethiUllman doInBackground() throws Exception {
					AhoSethiUllmanGenerador generador = AhoSethiUllmanGenerador
							.getInstance();
					int nSimbolos = simbolosSlider.getValue();
					int nEstados = estadosSlider.getValue();
					boolean usaVacio = vacioCheck.isSelected();
					progresoBar.setVisible(true);

					AhoSethiUllman problema = generador.nuevo(nSimbolos,
							nEstados, usaVacio);
					return problema;
				}

				@Override
				public void done() {
					AhoSethiUllman problema = null;
					try {
						problema = get();
						progresoBar.setVisible(false);

						if (problemaActual != null)
							documento.sustituirProblema(problemaActual,
									problema);
						else
							documento.aņadirProblema(problema);

						problemaActual = problema;
						expresionText.setText(problema.problema());
						vistaPrevia.setText(documento.vistaPrevia());
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};

			worker.execute();
		}
	}

	private class BotonBorrarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (problemaActual != null) {
				documento.eliminarProblema(problemaActual);
				vistaPrevia.setText(documento.vistaPrevia());
			}

			contenedorPanel.remove(actualPanel);
			contenedorPanel.revalidate();
		}
	}

	private class BotonResolverActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String expresion = expresionText.getText();

			if (expresion.length() > 0) {
				if (problemaActual != null) {
					if (!expresion.equals(problemaActual.problema())) {
						AhoSethiUllman problema = new AhoSethiUllman(expresion);
						documento.sustituirProblema(problemaActual, problema);
						problemaActual = problema;
					}
				} else {
					AhoSethiUllman problema = new AhoSethiUllman(expresion);
					documento.aņadirProblema(problema);
					problemaActual = problema;
				}
				vistaPrevia.setText(documento.vistaPrevia());
			}
		}
	}
}
