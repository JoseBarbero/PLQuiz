package es.ubu.inf.tfg.regex.asu.datos;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * MapaPosiciones implementa una interfaz simple para acceder a un mapa gen�rico
 * que almacena un conjunto de posiciones como valores. Provee m�todos para
 * a�adir elementos al mapa en distintos formatos, as� como m�todos que permiten
 * acceder a las partes �tiles de la informaci�n del mapa.
 * <p>
 * La �nica manera de modificar la informaci�n de un MapaPosiciones es a trav�s
 * de los m�todos {@link #add(E) add}, {@link #add(E, int) add}, {@link #add(E,
 * Set<Integer>) add} y {@link #add(Set<E>, Set<Integer>) add}. La informaci�n
 * obtenida no es nunca una referencia directa al elemento almacenado, sino una
 * copia.
 * <p>
 * La clase provee m�todos est�ticos que permiten realizar operaciones de uni�n
 * y copia sobre instancias de esta clase.
 * 
 * @author Roberto Izquierdo Amo
 * 
 * @param <E>
 *            Tipo de claves que manejar� el mapa interno. Normalmente ser�
 *            <code>Integer</code> (funci�n stePos) o <code>Character</code>
 *            (diccionario de s�mbolos).
 */
public class MapaPosiciones<E> {
	private Map<E, Set<Integer>> mapaPosiciones;

	/**
	 * Construye un mapa que asigna un conjunto de posiciones a una clave de un
	 * tipo dado.
	 */
	public MapaPosiciones() {
		this.mapaPosiciones = new TreeMap<>();
	}

	/**
	 * A�ade una clave al mapa, asociada a un conjunto de posiciones vac�o. Si
	 * el mapa ya contiene la clave, no hace nada.
	 * 
	 * @param n
	 *            Clave que a�adir al mapa.
	 */
	public void add(E n) {
		if (!this.mapaPosiciones.containsKey(n))
			this.mapaPosiciones.put(n, new TreeSet<Integer>());
	}

	/**
	 * A�ade una posici�n al conjunto de posiciones de una clave dada. Si la
	 * clave ya existe en el mapa, la posici�n se a�ade al conjunto existente.
	 * 
	 * @param n
	 *            Clave del conjunto.
	 * @param posicion
	 *            Posici�n a a�adir.
	 */
	public void add(E n, int posicion) {
		if (this.mapaPosiciones.containsKey(n)) {
			this.mapaPosiciones.get(n).add(posicion);
		} else {
			Set<Integer> posiciones = new TreeSet<>();
			posiciones.add(posicion);
			this.mapaPosiciones.put(n, posiciones);
		}
	}

	/**
	 * A�ade un conjunto de posiciones al conjunto de posiciones de una clave
	 * dada. Si la clave ya existe en el mapa, las posiciones se a�aden al
	 * conjunto existente.
	 * 
	 * @param n
	 *            Clave del conjunto.
	 * @param posiciones
	 *            Posiciones a a�adir.
	 */
	public void add(E n, Set<Integer> posiciones) {
		if (this.mapaPosiciones.containsKey(n))
			this.mapaPosiciones.get(n).addAll(posiciones);
		else
			this.mapaPosiciones.put(n, new TreeSet<>(posiciones));
	}

	/**
	 * A�ade un conjunto de posiciones al conjunto de posiciones de una serie de
	 * clave dadas. Esta implementaci�n utiliza el m�todo {@link #add(E,
	 * Set<Integer>) add}.
	 * 
	 * @param ns
	 * @param posiciones
	 */
	public void add(Set<E> ns, Set<Integer> posiciones) {
		for (E n : ns)
			add(n, posiciones);
	}

	/**
	 * Devuelve una copia del conjunto de claves del mapa de posiciones. Si se
	 * devolviera el conjunto de claves original podr�an introducirse cambios al
	 * mapa desde el exterior.
	 * 
	 * @return Conjunto de claves de posici�n.
	 */
	public Set<E> keys() {
		return new TreeSet<>(this.mapaPosiciones.keySet());
	}

	/**
	 * Devuelve una copia del conjunto de posiciones asociado a una determinada
	 * clave. Si la clave no existe, se devuelve el conjunto vac�o.
	 * 
	 * @param key
	 *            Clave que buscar.
	 * @return Conjunto de posiciones asociado.
	 */
	public Set<Integer> get(E key) {
		if (this.mapaPosiciones.containsKey(key))
			return new TreeSet<Integer>(this.mapaPosiciones.get(key));
		else
			return new TreeSet<Integer>();
	}

	/**
	 * Devuelve el n�mero de claves contenidas en el mapa.
	 * 
	 * @return Tama�o del mapa.
	 */
	public int size() {
		return this.mapaPosiciones.size();
	}

	/**
	 * Devuelve un mapa que constituye la uni�n de otros dos mapas. Si una clave
	 * se encuentra en ambos mapas, sus conjuntos de posiciones se unen. Si una
	 * clave se encuentra en solo un mapa, esta se a�ade al mapa tal cual. Esta
	 * implementaci�n utiliza el m�todo {@link #add(E, Set<Integer>) add}.
	 * 
	 * @param a
	 *            Primer mapa.
	 * @param b
	 *            Segundo mapa.
	 * @return Mapa uni�n del primer y segundo mapa.
	 */
	public static <E> MapaPosiciones<E> union(MapaPosiciones<E> a,
			MapaPosiciones<E> b) {
		MapaPosiciones<E> resultado = new MapaPosiciones<>();

		for (Entry<E, Set<Integer>> e : a.mapaPosiciones.entrySet())
			resultado.add(e.getKey(), e.getValue());

		for (Entry<E, Set<Integer>> e : b.mapaPosiciones.entrySet())
			resultado.add(e.getKey(), e.getValue());

		return resultado;
	}

	/**
	 * Devuelve un mapa copia del mapa dado, pero sin compartir referencias al
	 * mismo. Esta implementaci�n utiliza el m�todo {@link #add(E, Set<Integer>)
	 * add}.
	 * 
	 * @param original
	 *            Mapa original.
	 * @return Copia del mapa original.
	 */
	public static <E> MapaPosiciones<E> copia(MapaPosiciones<E> original) {
		MapaPosiciones<E> resultado = new MapaPosiciones<>();

		for (Entry<E, Set<Integer>> e : original.mapaPosiciones.entrySet())
			resultado.add(e.getKey(), e.getValue());

		return resultado;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof MapaPosiciones))
			return false;

		if (size() != ((MapaPosiciones<E>) o).size())
			return false;

		for (Entry<E, Set<Integer>> e : this.mapaPosiciones.entrySet()) {
			if (!(e.getValue().equals(((MapaPosiciones<E>) o).get(e.getKey()))))
				return false;
		}

		return true;
	}
}
