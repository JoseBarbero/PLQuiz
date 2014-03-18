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
	public Automata(ExpresionRegular expresion) {
		// TODO completamente provisional
		Nodo nodo0 = new Nodo(0, false);
		Nodo nodo1 = new Nodo(1, false);
		Nodo nodo2 = new Nodo(2, false);
		Nodo nodo3 = new Nodo(3, false);
		Nodo nodo4 = new Nodo(4, false);
		Nodo nodo5 = new Nodo(5, false);
		Nodo nodo6 = new Nodo(6, false);
		Nodo nodo7 = new Nodo(7, false);
		Nodo nodo8 = new Nodo(8, false);
		Nodo nodo9 = new Nodo(9, false);
		Nodo nodo10 = new Nodo(10, true);

		nodo0.a�adeTransicionVacia(nodo1);
		nodo0.a�adeTransicionVacia(nodo7);

		nodo1.a�adeTransicionVacia(nodo2);
		nodo1.a�adeTransicionVacia(nodo4);

		nodo2.a�adeTransicion('a', nodo3);

		nodo3.a�adeTransicionVacia(nodo6);

		nodo4.a�adeTransicion('b', nodo5);

		nodo5.a�adeTransicionVacia(nodo6);

		nodo6.a�adeTransicionVacia(nodo7);
		nodo6.a�adeTransicionVacia(nodo1);

		nodo7.a�adeTransicion('a', nodo8);

		nodo8.a�adeTransicion('b', nodo9);

		nodo9.a�adeTransicion('b', nodo10);

		this.nodoInicial = nodo0;
		this.nodoFinal = nodo10;

		this.simbolos = new TreeSet<>();
		this.simbolos.add('a');
		this.simbolos.add('b');
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
			actuales.remove(actual);
		}

		return visitados;
	}
}
