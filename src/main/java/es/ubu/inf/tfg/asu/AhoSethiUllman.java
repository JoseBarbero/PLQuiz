package es.ubu.inf.tfg.asu;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;
import java.util.TreeSet;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;
import es.ubu.inf.tfg.asu.datos.MapaEstados;
import es.ubu.inf.tfg.asu.datos.MapaPosiciones;
import es.ubu.inf.tfg.asu.datos.Nodo;
import es.ubu.inf.tfg.asu.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.asu.parser.ParseException;
import es.ubu.inf.tfg.asu.parser.TokenMgrError;

public class AhoSethiUllman {

	private String problema;
	private ExpresionRegular expresion;
	private Nodo solucion;
	private MapaPosiciones<Character> estados;
	private MapaEstados transiciones;

	public AhoSethiUllman(String problema) throws UnsupportedOperationException {
		if (problema.charAt(problema.length() - 1) != '\n')
			problema += '\n';

		this.problema = problema.substring(0, problema.length() - 1);

		Reader input = new StringReader(problema);
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			this.expresion = parser.expresion();
		} catch (ParseException | TokenMgrError e) {
			throw new UnsupportedOperationException("Expresi�n no v�lida.");
		}

		this.solucion = new Nodo(this.expresion);

		// calculo estados
		this.estados = new MapaPosiciones<>();
		this.transiciones = new MapaEstados();

		this.estados.add('A', primeraPos());
		char estadoActual = 'A';

		// Mientras queden estados por rellenar
		while (estados.keys().contains(estadoActual)) {
			// Para cada s�mbolo no final
			for (char simbolo : simbolos()) {
				if (simbolo != '$') {
					// Calculamos y almacenamos la transici�n
					char destino = transicion(estadoActual, simbolo);
					this.transiciones.add(estadoActual, simbolo, destino);
				}
			}
			// Pasamos al siguiente estado, alfabeticamente
			estadoActual++;
		}
	}

	private char transicion(char estado, char simbolo) {
		Set<Integer> posiciones = new TreeSet<>();

		for (int pos : posiciones(simbolo)) {
			if (this.estados.get(estado).contains(pos))
				posiciones.addAll(siguientePos(pos));
		}

		// Comprobar si existe el estado o crear uno nuevo.
		for (char est : estados()) {
			if (estado(est).equals(posiciones)) {
				return est;
			}
		}

		char est = (char) (this.estados.size() + 'A');
		this.estados.add(est, posiciones);
		return est;
	}

	public String problema() {
		return this.problema;
	}

	public String expresionAumentada() {
		return this.expresion.toString();
	}

	public Set<Integer> primeraPos() {
		return this.solucion.primeraPos();
	}

	public Set<Character> simbolos() {
		return this.solucion.simbolos().keys();
	}

	public Set<Integer> posiciones(char simbolo) {
		return this.solucion.simbolos().get(simbolo);
	}

	public Set<Integer> posiciones() {
		return this.solucion.siguientePos().keys();
	}

	public Set<Integer> siguientePos(int n) {
		return this.solucion.siguientePos().get(n);
	}

	public Set<Character> estados() {
		return this.estados.keys();
	}

	public Set<Integer> estado(char key) {
		return this.estados.get(key);
	}

	public char mueve(char estado, char simbolo) {
		return this.transiciones.get(estado, simbolo);
	}

	public boolean esFinal(char estado) {
		// Solo hay una posici�n final por expresi�n.
		int posicionfinal = posiciones('$').iterator().next();
		return this.estados.get(estado).contains(posicionfinal);
	}
}
