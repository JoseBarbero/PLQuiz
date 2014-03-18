package es.ubu.inf.tfg.regex.thompson.datos;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Nodo implementa cada uno de los estados del aut�mata finito no determinista
 * definido por una expresi�n regular dada. Contiene referencias a su posici�n,
 * una serie de transiciones que no consumen entrada y una serie de transiciones
 * que consumen cada una un s�mbolo dado.
 * <p>
 * Implementa sus propios m�todos <code>equals</code> y <code>compareTo</code>
 * para su uso dentro de sets ordenados.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Nodo implements Comparable<Nodo> {
	private int posicion;
	private boolean esFinal;

	private Map<Character, Nodo> transiciones;
	private Set<Nodo> transicionesVacias;

	/**
	 * Constructor. Define un nodo desconectado con la posici�n dada y un estado
	 * final o no.
	 * 
	 * @param posicion
	 *            Posici�n del nodo.
	 * @param esFinal
	 *            Si el nodo es final.
	 */
	public Nodo(int posicion, boolean esFinal) {
		this.posicion = posicion;
		this.esFinal = esFinal;

		this.transiciones = new TreeMap<>();
		this.transicionesVacias = new TreeSet<>();
	}

	/**
	 * Devuelve la posici�n de nodo dentro del aut�mata.
	 * 
	 * @return Posici�n del nodo.
	 */
	public int posicion() {
		return this.posicion;
	}

	/**
	 * Comprueba si el nodo es un nodo final.
	 * 
	 * @return <code>true</code> si el nodo es final, <code>false</code> si no.
	 */
	public boolean esFinal() {
		return this.esFinal;
	}

	/**
	 * A�ade al nodo una transici�n que consumir� una entrada determinada. Si el
	 * nodo ya contenia una transici�n para dicha entrada, este m�todo la
	 * sobreescribe con la nueva.
	 * 
	 * @param simbolo
	 *            S�mbolo a consumir.
	 * @param destino
	 *            Nodo de destino.
	 */
	public void a�adeTransicion(char simbolo, Nodo destino) {
		this.transiciones.put(simbolo, destino);
	}

	/**
	 * A�ade al nodo una transici�n que no consume entrada.
	 * 
	 * @param destino
	 *            Nodo de destino.
	 */
	public void a�adeTransicionVacia(Nodo destino) {
		this.transicionesVacias.add(destino);
	}

	/**
	 * Obtiene el nodo de destino para la transici�n que consume una entrada
	 * determinada, o null si no hay una transici�n para dicha entrada.
	 * 
	 * @param simbolo
	 *            S�mbolo a consumir.
	 * @return Nodo de destino o <code>null</code> si no existe la transici�n.
	 */
	public Nodo transicion(char simbolo) {
		return this.transiciones.get(simbolo);
	}

	/**
	 * Obtiene los nodos de destino a los que podemos llegar sin consumir
	 * entrada. Si no hay ninguno se devuelve el conjunto vac�o.
	 * 
	 * @return Conjunto de nodos de destino.
	 */
	public Set<Nodo> transicionVacia() {
		return this.transicionesVacias;
	}

	/**
	 * Un nodo se representa utilizando su posici�n.
	 */
	@Override
	public String toString() {
		return "" + posicion();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Nodo))
			return false;

		// Consideramos dos nodos iguales si tienen la misma posici�n.
		if (!(((Nodo) o).posicion() == posicion()))
			return false;

		return true;
	}

	@Override
	public int compareTo(Nodo otro) {
		if (otro.posicion() < posicion())
			return 1;
		else if (otro.posicion() > posicion())
			return -1;
		return 0;
	}
}
