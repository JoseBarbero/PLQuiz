package es.ubu.inf.tfg.regex.datos;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generador implementa una clase encargada de generar �rboles de expresi�n
 * regular de un tama�o y caracter�sticas dadas, con generaci�n de �rboles
 * basada en el algoritmo GROW.
 * <p>
 * Permite tambi�n realizar mutaciones aleatorias sobre dichos �rboles, en las
 * cuales un sub�rbol de la expresi�n se intercambia por uno nuevo generado
 * aleatoriamente.
 * 
 * @author Roberto Izquierdo Amo.
 * 
 */
public class Generador {

	private static final Logger log = LoggerFactory.getLogger(Generador.class);

	private static final Random random = new Random(new Date().getTime());

	private List<Character> simbolos;
	private List<Character> simbolosRepetidos;
	private int posicion;

	private final int nSimbolos;
	private final boolean usaVacio;

	/**
	 * Operador implementa un tipo enumerado que contiene los tipos de
	 * operadores que utilizar� el �rbol, los conjuntos de operadores que se
	 * utilizan en cada circunstancia, y una serie de operaciones relacionadas.
	 * <p>
	 * Se utiliza tambi�n para almacenar el estado del �rbol en un momento
	 * determinado.
	 * 
	 * @author Roberto Izquierdo Amo
	 * 
	 */
	private static enum Operador {
		SIMBOLO, VACIO, CIERRE, CONCAT, UNION;

		private static final EnumSet<Operador> COMPLETO = EnumSet.range(CIERRE,
				UNION);
		private static final EnumSet<Operador> PARCIAL = EnumSet.of(CONCAT,
				UNION);
		private static final EnumSet<Operador> FINAL_COMPLETO = EnumSet.of(
				SIMBOLO, VACIO);
		private static final EnumSet<Operador> FINAL_PARCIAL = EnumSet
				.of(SIMBOLO);
	}

	/**
	 * Constructor. Prepara un generador para expresiones con unas
	 * caracter�sticas dadas.
	 * 
	 * @param nSimbolos
	 *            N�mero de s�mbolos a utilizar en la expresi�n, empezando por
	 *            la 'a'.
	 * @param usaVacio
	 *            <code>true</code> en caso de que la expresi�n contenga nodos
	 *            vac�os, <code>false</code> en caso contrario.
	 */
	public Generador(int nSimbolos, boolean usaVacio) {
		this.nSimbolos = nSimbolos;
		this.usaVacio = usaVacio;
	}

	/**
	 * Comprueba si el generador devolver� expresiones que contenga nodos
	 * vac�os.
	 * 
	 * @return <code>true</code> en caso de que las expresiones generadas puedan
	 *         contener nodos vac�os, <code>false</code> en caso contrario.
	 */
	public boolean usaVacio() {
		return usaVacio;
	}

	/**
	 * Reemplaza un sub�rbol al azar de la expresi�n por uno nuevo generado. En
	 * caso de que se le proporcione una expresi�n aumentada, la expresi�n
	 * mutada ser� tambi�n una expresi�n aumentada v�lida.
	 * <p>
	 * Toda modificaci�n realizada afecta a una copia de la expresi�n original,
	 * no a la propia expresi�n.
	 * 
	 * @param expresion
	 *            Expresi�n a partir de la cual obtenemos la mutaci�n.
	 * @return Expresi�n mutada.
	 */
	public ExpresionRegular mutacion(ExpresionRegular expresion) {
		ExpresionRegular mutante = ExpresionRegular.copia(expresion);

		// Trabaja con la expresi�n sin aumentar.
		if (expresion.hijoDerecho().esSimbolo()
				&& expresion.hijoDerecho().simbolo() == '$')
			mutante = mutante.hijoIzquierdo();

		List<ExpresionRegular> nodos = mutante.nodos().stream()
		// .filter(e -> !e.esSimbolo() && !e.esVacio())
				.collect(Collectors.toList());
		ExpresionRegular seleccion = nodos.get(random.nextInt(nodos.size()));

		for (ExpresionRegular nodo : mutante.nodos()) {
			if (!nodo.esSimbolo() && !nodo.esVacio()) {
				if (nodo.hijoIzquierdo().equals(seleccion))
					nodo.hijoIzquierdo(arbol(seleccion.profundidad()));
				if (!nodo.esCierre()) {
					if (nodo.hijoDerecho().equals(seleccion))
						nodo.hijoDerecho(arbol(seleccion.profundidad()));
				}
			}
		}

		// Vuelve a aumentar la expresi�n.
		if (expresion.hijoDerecho().esSimbolo()
				&& expresion.hijoDerecho().simbolo() == '$') {
			int index = mutante.nodos().stream().filter(e -> e.esSimbolo())
					.mapToInt(ExpresionRegular::posicion).max().getAsInt() + 1;

			mutante = ExpresionRegular.nodoConcat(
					ExpresionRegular.nodoAumentado(index), mutante);
		}

		log.debug("Mutaci�n de {} -> {}", expresion, mutante);

		return mutante;
	}

	/**
	 * Obtiene un �rbol de la profundidad dada, con los par�metros establecidos
	 * en el generador.
	 * 
	 * @param profundidad
	 *            Profundidad del �rbol de expresi�n regular a obtener.
	 * @return Expresi�n regular con un �rbol correspondiente de la profundidad
	 *         dada.
	 */
	public ExpresionRegular arbol(int profundidad) {
		simbolosRepetidos = new ArrayList<>();
		for (int i = 0; i < this.nSimbolos; i++)
			simbolosRepetidos.add((char) ('a' + i));
		if (this.usaVacio)
			simbolosRepetidos.add('E');

		simbolos = new ArrayList<>(simbolosRepetidos);

		posicion = 0;

		return subArbol(profundidad, null);
	}

	/**
	 * Genera un sub-�rbol de expresion regular con la profundidad dada y
	 * utilizando un conjunto de operadores concreto. Se llama a si mismo de
	 * manera recursiva para completar la construcci�n.
	 * 
	 * @param profundidad
	 *            Profundidad del �rbol pedido.
	 * @param operadores
	 *            Operadores a utilizar. Cuando se llama desde el exterior de la
	 *            clase, este argumento ser� <code>null</code>.
	 * @return Expresi�n regular generada.
	 */
	private ExpresionRegular subArbol(int profundidad,
			EnumSet<Operador> operadores) {
		ExpresionRegular hijoIzquierdo;
		ExpresionRegular hijoDerecho;

		if (operadores == null)
			operadores = Operador.COMPLETO;

		// Hoja del �rbol.
		if (profundidad <= 0) {
			if (operadores.equals(Operador.COMPLETO) && usaVacio())
				operadores = Operador.FINAL_COMPLETO;
			else
				operadores = Operador.FINAL_PARCIAL;
		}

		// Nodo operador.
		switch (operador(operadores)) {
		case VACIO: // Vac�o solo actua como marcador.
		case SIMBOLO:
			char simbolo;
			if (operadores.equals(Operador.FINAL_COMPLETO))
				simbolo = simbolo();
			else
				simbolo = simboloNoVacio();

			if (simbolo == 'E')
				return ExpresionRegular.nodoVacio();
			return ExpresionRegular.nodoSimbolo(++posicion, simbolo);
		case CIERRE:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.PARCIAL);
			return ExpresionRegular.nodoCierre(hijoIzquierdo);
		case CONCAT:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		case UNION:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoUnion(hijoDerecho, hijoIzquierdo);
		default:
			return null;
		}
	}

	/**
	 * Devuelve un operador aleatorio de entre un conjunto de operadores.
	 * 
	 * @param operadores
	 *            Conjunto de operadores entre los que elegir.
	 * @return Operador cualquiera del conjunto.
	 */
	private Operador operador(EnumSet<Operador> operadores) {
		int index = random.nextInt(operadores.size());
		return (Operador) operadores.toArray()[index];
	}

	/**
	 * Genera un s�mbolo cualquiera de aquellos que puede incluir el �rbol.
	 * Puede contener el s�mbolo vac�o. Prioritiza s�mbolos que a�n no hayan
	 * aparecido.
	 * 
	 * @return Un s�mbolo cualquiera o el s�mbolo vac�o.
	 */
	private char simbolo() {
		int index;

		if (simbolos.size() > 0) {
			index = random.nextInt(simbolos.size());
			char simbolo = simbolos.get(index);
			simbolos.remove(index);
			return simbolo;
		} else {
			index = random.nextInt(simbolosRepetidos.size());
			return simbolosRepetidos.get(index);
		}
	}

	/**
	 * Genera un s�mbolo cualquiera de aquellos que puede incluir el �rbol,
	 * excluyendo el s�mbolo vac�o. Prioritiza s�mbolos que a�n no hayan
	 * aparecido.
	 * 
	 * @return Un s�mbolo cualquiera, exceptuando el s�mbolo vac�o.
	 */
	private char simboloNoVacio() {
		int index;

		if (simbolosRepetidos.contains('E')) {
			List<Character> simbolosParcial;
			if (simbolos.size() > 1) {
				simbolosParcial = new ArrayList<>(simbolos);
				if (simbolosParcial.contains('E'))
					simbolosParcial.remove(simbolosParcial.indexOf('E'));
				index = random.nextInt(simbolosParcial.size());
				char simbolo = simbolosParcial.get(index);
				simbolos.remove(index);
				return simbolo;
			} else {
				simbolosParcial = new ArrayList<>(simbolosRepetidos);
				simbolosParcial.remove(simbolosParcial.indexOf('E'));
				index = random.nextInt(simbolosParcial.size());
				return simbolosParcial.get(index);
			}
		} else
			return simbolo();
	}
}
