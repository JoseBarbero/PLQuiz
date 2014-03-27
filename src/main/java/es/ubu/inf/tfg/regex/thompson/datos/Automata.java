package es.ubu.inf.tfg.regex.thompson.datos;

import java.util.Set;
import java.util.TreeSet;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

/**
 * Automata implementa el modelo l�gico de un automata finito no determinista,
 * compuesto por una serie de nodos y de transiciones entre dichos nodos.
 * Permite obtener los nodos obtenidos a partir de uno dado utilizando un tipo
 * de transici�n determinado, y consumiendo o no un caracter dado.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Automata {

	private Nodo nodoInicial;
	private Nodo nodoFinal;
	private Set<Character> simbolos;

	/**
	 * Constructor. Define un automata finito no determinista a partir de un
	 * �rbol de expresion regular dado, de manera recursiva.
	 * 
	 * @param expresion
	 *            �rbol de expresion regular a partir del cual generar el
	 *            aut�mata.
	 */
	public Automata(ExpresionRegular expresion, int posicionInicial) {

		this.simbolos = new TreeSet<>();

		if (expresion.esVacio()) {
			this.nodoInicial = new Nodo(posicionInicial, false);
			this.nodoFinal = new Nodo(posicionInicial + 1, true);
			this.nodoInicial.a�adeTransicionVacia(this.nodoFinal);
		} else if (expresion.esSimbolo()) {
			this.nodoInicial = new Nodo(posicionInicial, false);
			this.nodoFinal = new Nodo(posicionInicial + 1, true);
			this.nodoInicial.a�adeTransicion(expresion.simbolo(),
					this.nodoFinal);
			this.simbolos.add(expresion.simbolo());
		} else if (expresion.esCierre()) {
			this.nodoInicial = new Nodo(posicionInicial, false);
			Automata hijo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial + 1);
			this.nodoFinal = new Nodo(hijo.nodoFinal().posicion() + 1, true);

			this.nodoInicial.a�adeTransicionVacia(hijo.nodoInicial());
			this.nodoInicial.a�adeTransicionVacia(this.nodoFinal);
			hijo.nodoFinal().a�adeTransicionVacia(hijo.nodoInicial());
			hijo.nodoFinal().a�adeTransicionVacia(this.nodoFinal);

			this.simbolos.addAll(hijo.simbolos());
		} else if (expresion.esConcat()) {
			Automata hijoIzquierdo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial);
			Automata hijoDerecho = new Automata(expresion.hijoDerecho(),
					hijoIzquierdo.nodoFinal().posicion());

			hijoIzquierdo.nodoFinal().unir(hijoDerecho.nodoInicial());
			this.nodoInicial = hijoIzquierdo.nodoInicial();
			this.nodoFinal = hijoDerecho.nodoFinal();

			this.simbolos.addAll(hijoIzquierdo.simbolos());
			this.simbolos.addAll(hijoDerecho.simbolos());
		} else if (expresion.esUnion()) {
			this.nodoInicial = new Nodo(posicionInicial, false);

			Automata hijoIzquierdo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial + 1);
			Automata hijoDerecho = new Automata(expresion.hijoDerecho(),
					hijoIzquierdo.nodoFinal().posicion() + 1);

			this.nodoFinal = new Nodo(hijoDerecho.nodoFinal().posicion() + 1,
					true);

			this.nodoInicial.a�adeTransicionVacia(hijoIzquierdo.nodoInicial());
			this.nodoInicial.a�adeTransicionVacia(hijoDerecho.nodoInicial());
			hijoIzquierdo.nodoFinal().a�adeTransicionVacia(this.nodoFinal);
			hijoDerecho.nodoFinal().a�adeTransicionVacia(this.nodoFinal);

			this.simbolos.addAll(hijoIzquierdo.simbolos());
			this.simbolos.addAll(hijoDerecho.simbolos());
		} else { // runtime exception
			throw new IllegalArgumentException(
					"Expresion regular de tipo desconocido.");
		}
	}

	/**
	 * Nodo de entrada del aut�mata.
	 * 
	 * @return Nodo inicial.
	 */
	public Nodo nodoInicial() {
		return this.nodoInicial;
	}

	/**
	 * Nodo final del aut�mata.
	 * 
	 * @return Nodo final.
	 */
	public Nodo nodoFinal() {
		return this.nodoFinal;
	}

	/**
	 * Devuelve el conjunto de s�mbolos que el automata utiliza en sus
	 * transiciones. No se incluye epsilon ni cualquier otro indicador de
	 * transici�n vac�a.
	 * 
	 * @return Conjunto de s�mbolos del aut�mata.
	 */
	public Set<Character> simbolos() {
		return new TreeSet<>(this.simbolos);
	}

	/**
	 * Obtiene el conjunto de nodos al que se llega a partir de un nodo inicial
	 * y tras consumir un s�mbolo determinado. Solo se cuentan las transiciones
	 * vac�as efectuadas tras consumir la entrada.
	 * 
	 * @param inicio
	 *            Nodo de inicio.
	 * @param simbolo
	 *            S�mbolo de entrada.
	 * @return Conjunto de nodos de llegada.
	 */
	public Set<Nodo> transicion(Nodo inicio, char simbolo) {
		// Nodos a los que llegamos desde el inicio sin consumir
		Set<Nodo> iniciales = transicionVacia(inicio);
		// Nodos a los que llegamos tras consumir la entrada
		Set<Nodo> transicionConsumiendo = new TreeSet<>();
		// Nodos a los que llegamos tras consumir la entrada
		Set<Nodo> transicionNoConsumiendo = new TreeSet<>();
		Nodo actual;

		for (Nodo nodo : iniciales) {
			actual = nodo.transicion(simbolo);
			if (actual != null)
				transicionConsumiendo.add(actual);
		}

		for (Nodo nodo : transicionConsumiendo)
			transicionNoConsumiendo.addAll(transicionVacia(nodo));

		transicionConsumiendo.addAll(transicionNoConsumiendo);

		return transicionConsumiendo;
	}

	/**
	 * Obtiene el conjunto de nodos al que se llega a partir de un nodo inicial
	 * y sin consumir ning�n s�mbolo.
	 * 
	 * @param inicio
	 *            Nodo de inicio.
	 * @return Conjunto de nodos de llegada.
	 */
	public Set<Nodo> transicionVacia(Nodo inicio) {
		// Nodos a los que llegamos sin consumir entrada
		Set<Nodo> actuales = new TreeSet<>();
		Set<Nodo> visitados = new TreeSet<>();
		Nodo actual;

		actuales.add(inicio);
		while (!actuales.isEmpty()) {
			actual = actuales.iterator().next();
			actuales.addAll(actual.transicionVacia());

			visitados.add(actual);
			actuales.removeAll(visitados);
		}

		return visitados;
	}
}
