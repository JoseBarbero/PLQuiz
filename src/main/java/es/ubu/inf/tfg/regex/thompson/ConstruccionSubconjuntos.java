package es.ubu.inf.tfg.regex.thompson;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;
import es.ubu.inf.tfg.regex.datos.MapaEstados;
import es.ubu.inf.tfg.regex.parser.CharStream;
import es.ubu.inf.tfg.regex.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.regex.parser.JavaCharStream;
import es.ubu.inf.tfg.regex.parser.ParseException;
import es.ubu.inf.tfg.regex.parser.TokenMgrError;
import es.ubu.inf.tfg.regex.thompson.datos.Automata;
import es.ubu.inf.tfg.regex.thompson.datos.Nodo;

/**
 * * Resuelve un problema de construcci�n de subconjuntos a partir de un AFND,
 * generado a partir de una expresi�n regular con el m�todo de
 * McNaughton-Yamada-Thompson. Es capaz de trabajar tanto con una cadena de
 * caracteres como con una ExpresionRegular ya construida.
 * <p>
 * Asimismo act�a como fachada del subsistema es.ubu.inf.tfg.regex.thompson,
 * evitando dependencias con los tipos de datos internos. Todas las salidas se
 * codifican en tipos de datos est�ndar.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class ConstruccionSubconjuntos {

	private static final Logger log = LoggerFactory
			.getLogger(ConstruccionSubconjuntos.class);

	private String problema;
	private ExpresionRegular expresion;
	private Automata automata;
	private Map<Character, Set<Nodo>> estados;

	private MapaEstados transiciones;
	private List<BufferedImage> alternativas;
	private List<String> alternativasDot;

	/**
	 * Resuelve un problema de construcci�n de subconjuntos a partir de una
	 * expresi�n regular en forma de String, construyendo el aut�mata seg�n el
	 * algoritmo de McNaughton-Yamada-Thompson.
	 * 
	 * @param problema
	 *            Cadena de caracteres conteniendo la expresi�n regular a
	 *            resolver.
	 * @throws UnsupportedOperationException
	 *             Error del parser o del token manager. Indica que la expresi�n
	 *             no es v�lida o que contiene caracteres no reconocidos.
	 */
	public ConstruccionSubconjuntos(String problema)
			throws UnsupportedOperationException {
		if (problema.charAt(problema.length() - 1) != '\n')
			problema += '\n';

		this.problema = problema.substring(0, problema.length() - 1);

		CharStream input = new JavaCharStream(new StringReader(problema));
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			this.expresion = parser.expresion();
			// No utilizamos la expresi�n aumentada
			this.expresion = this.expresion.hijoIzquierdo();
		} catch (ParseException | TokenMgrError e) {
			throw new UnsupportedOperationException("Expresi�n no v�lida.");
		}

		this.automata = new Automata(this.expresion, 0);

		// Calculo de estados
		this.estados = new TreeMap<>();
		this.transiciones = new MapaEstados();

		char estadoActual = 'A';
		Set<Nodo> posiciones = automata.transicionVacia(automata.nodoInicial());
		estados.put(estadoActual, posiciones);

		while (estados.keySet().contains(estadoActual)) {
			for (char simbolo : this.automata.simbolos()) {
				char destino = transicion(estadoActual, simbolo);
				this.transiciones.add(estadoActual, simbolo, destino);
			}
			estadoActual++;
		}
	}

	/**
	 * Resuelve un problema de construcci�n de subconjuntos a partir de una
	 * expresi�n regular en forma de ExpresionRegular, construyendo el aut�mata
	 * seg�n el algoritmo de McNaughton-Yamada-Thompson.
	 * 
	 * @param expresionExpresionRegular
	 *            conteniendo la expresi�n regular a resolver.
	 */
	public ConstruccionSubconjuntos(ExpresionRegular expresion) {
		// Expresi�n sin aumentar.
		this.expresion = expresion.hijoIzquierdo();
		this.problema = this.expresion.toString();

		this.automata = new Automata(this.expresion, 0);

		// Calculo de estados
		this.estados = new TreeMap<>();
		this.transiciones = new MapaEstados();

		char estadoActual = 'A';
		Set<Nodo> posiciones = automata.transicionVacia(automata.nodoInicial());
		estados.put(estadoActual, posiciones);

		while (estados.keySet().contains(estadoActual)) {
			for (char simbolo : this.automata.simbolos()) {
				char destino = transicion(estadoActual, simbolo);
				this.transiciones.add(estadoActual, simbolo, destino);
			}
			estadoActual++;
		}
	}

	/**
	 * Calcula la transici�n a partir de un estado dado mediante un s�mbolo
	 * concreto.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            S�mbolo de transici�n.
	 * @return Estado de destino.
	 */
	private char transicion(char estado, char simbolo) {
		Set<Nodo> posiciones = new TreeSet<>();

		for (Nodo nodo : estados.get(estado)) {
			if (this.estados.get(estado).contains(nodo))
				posiciones.addAll(automata.transicion(nodo, simbolo));
		}

		for (char est : estados()) {
			if (estados.get(est).equals(posiciones))
				return est;
		}

		char est = (char) (this.estados.size() + 'A');
		this.estados.put(est, posiciones);

		return est;
	}

	/**
	 * Devuelve el problema original, la expresi�n regular. Puede contener
	 * caracteres especiales.
	 * 
	 * @return Problema a resolver.
	 */
	public String problema() {
		return this.problema;
	}

	/**
	 * Devuelve el conjunto de s�mbolos que se utilizan en la expresi�n regular.
	 * 
	 * @return S�mbolos que utiliza la expresi�n regular.
	 */
	public Set<Character> simbolos() {
		return automata.simbolos();
	}

	/**
	 * Devuelve un conjunto de caracteres representando los estados existentes
	 * en la tabla de transici�n.
	 * 
	 * @return Conjunto de estados en la tabla de transici�n.
	 */
	public Set<Character> estados() {
		return new TreeSet<>(estados.keySet());
	}

	/**
	 * Devuelve el conjunto de posiciones asociadas a un estado dado en la tabla
	 * de transici�n.
	 * 
	 * @param estado
	 *            Estado del que queremos calcular posiciones.
	 * @return Conjunto de posiciones asociadas al estado.
	 */
	public Set<Integer> posiciones(char estado) {
		Set<Integer> posiciones = new TreeSet<>();

		for (Nodo nodo : this.estados.get(estado))
			posiciones.add(nodo.posicion());

		return posiciones;
	}

	/**
	 * Devuelve el conjunto de posiciones encontradas en la expresi�n regular.
	 * El conjunto ser� un rango [1, n].
	 * 
	 * @return Conjunto de posiciones en la expresi�n regular.
	 */
	public Set<Integer> posiciones() {
		Set<Integer> posiciones = new TreeSet<Integer>();
		for (int i = this.automata.nodoInicial().posicion(); i <= this.automata
				.nodoFinal().posicion(); i++) {
			posiciones.add(i);
		}
		return posiciones;
	}

	/**
	 * Calcula el estado de destino para un estado de origen y un s�mbolo de
	 * transici�n.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            S�mbolo de transici�n.
	 * @return Estado de destino.
	 */
	public char mueve(char estado, char simbolo) {
		return this.transiciones.get(estado, simbolo);
	}

	/**
	 * Comprueba si un estado es final, es decir, si su conjunto de posiciones
	 * contiene un nodo final. Devuelve <code>true</code> si el estado es final,
	 * <code>false</code> si no.
	 * 
	 * @param estado
	 *            Estado a comprobar.
	 * @return <code>true</code> si el estado es final, <code>false</code> si
	 *         no.
	 */
	public boolean esFinal(char estado) {
		boolean esFinal = false;

		for (Nodo nodo : this.estados.get(estado))
			esFinal = esFinal || nodo.esFinal();

		return esFinal;
	}

	/**
	 * Devuelve una imagen representando el aut�mata asociado a este problema.
	 * 
	 * @return Imagen del aut�mata.
	 */
	public BufferedImage automata() {
		return automata.imagen();
	}

	/**
	 * Devuelve una programa en formato dot para generar la imagen representando
	 * el aut�mata asociado a este problema.
	 * 
	 * @return Programa formato dot para representar el aut�mata.
	 */
	public String automataDot() {
		return automata.imagenDot();
	}
	
	/**
	 * Devuelve una programa en formato svg para generar la imagen representando
	 * el aut�mata asociado a este problema.
	 * 
	 * @return Programa formato svg para representar el aut�mata.
	 */
	public String automataSvg() {
		return automata.imagenSvg();
	}
	
	/**
	 * Devuelve una soluci�n (en azul) en formato svg para generar la imagen representando
	 * el aut�mata asociado a este problema.
	 * 
	 * @return Programa formato svg para representar el aut�mata.
	 */
	public String automataSvgSolucion() {
		String imagensvg = automata.imagenSvg();
		imagensvg = imagensvg.replace("stroke=\"black\"", "stroke=\"navy\"");
		imagensvg = imagensvg.replace("fill=\"black\"", "fill=\"navy\"");
		return imagensvg;
	}
	

	/**
	 * Genera una serie de cuatro im�genes correspondientes a los aut�matas de
	 * la expresi�n regular original del problema y de tres mutaciones de la
	 * misma, como alternativas en un problema de construcci�n de �rbol.
	 * 
	 * @return Array de cuatro im�genes representando �rboles de expresi�n
	 *         regular, una correspondiente al del problema y tres alternativas.
	 */
	public List<BufferedImage> alternativas() {
		if (this.alternativas == null) {
			alternativas = new ArrayList<>();

			Automata automata;
			for (ExpresionRegular expresion : expresionesAlternativas()) {
				automata = new Automata(expresion, 0);
				alternativas.add(automata.imagen());
			}
		}

		return new ArrayList<>(alternativas);
	}

	/**
	 * Genera una serie de cuatro programas dot con las im�genes
	 * correspondientes los aut�matas de la expresi�n regular original del
	 * problema y de tres mutaciones de la misma, como alternativas en un
	 * problema de construcci�n de �rbol.
	 * 
	 * @return Array de cuatro cadenas de caracteres conteniendo programas dot
	 *         representando aut�matas de expresi�n regular, una correspondiente
	 *         al del problema y tres alternativas.
	 */
	public List<String> alternativasDot() {
		if (this.alternativasDot == null) {
			alternativasDot = new ArrayList<>();
			Automata automata;
			for (ExpresionRegular expresion : expresionesAlternativas()) {
				automata = new Automata(expresion, 0);
				alternativasDot.add(automata.imagenDot());
			}
		}

		return new ArrayList<>(alternativasDot);
	}

	/**
	 * Genera un set de alternativas para una expresi�n regular, incluyendo la
	 * original y tres otras.
	 * 
	 * @return Set completo de alternativas.
	 */
	public Set<ExpresionRegular> expresionesAlternativas() {
		log.info("Generando im�genes alternativas");

		int nSimbolos = simbolos().size();
		boolean usaVacio = simbolos().contains('\u0000');
		if (usaVacio)
			nSimbolos--;
		Generador generador = new Generador(nSimbolos, usaVacio, true);

		Set<ExpresionRegular> expresiones = new HashSet<>();
		expresiones.add(expresion);
		ExpresionRegular alternativa;
		while (expresiones.size() < 4) {
			alternativa = generador.mutacion(expresion);
			log.debug("Generada expresi�n alternativa {}", alternativa);
			expresiones.add(alternativa);
		}

		return expresiones;
	}
}
