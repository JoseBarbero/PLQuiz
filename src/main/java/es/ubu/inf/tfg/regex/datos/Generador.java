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
 * Generador implementa una clase encargada de generar árboles de expresión
 * regular de un tamaño y características dadas, con generación de árboles
 * basada en el algoritmo GROW.
 * <p>
 * Permite también realizar mutaciones aleatorias sobre dichos árboles, en las
 * cuales un subárbol de la expresión se intercambia por uno nuevo generado
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
	private boolean esAumentada;

	/**
	 * Operador implementa un tipo enumerado que contiene los tipos de
	 * operadores que utilizará el árbol, los conjuntos de operadores que se
	 * utilizan en cada circunstancia, y una serie de operaciones relacionadas.
	 * <p>
	 * Se utiliza también para almacenar el estado del árbol en un momento
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
	 * características dadas.
	 * 
	 * @param nSimbolos
	 *            Número de símbolos a utilizar en la expresión, empezando por
	 *            la 'a'.
	 * @param usaVacio
	 *            <code>true</code> en caso de que la expresión contenga nodos
	 *            vacíos, <code>false</code> en caso contrario.
	 * @param esAumentada
	 *            Especifica si el algoritmo trabajará con expresiones
	 *            aumentadas (<code>true</code>), o no (<code>false</code>).
	 */
	public Generador(int nSimbolos, boolean usaVacio, boolean esAumentada) {
		this.nSimbolos = nSimbolos;
		this.usaVacio = usaVacio;
		this.esAumentada = esAumentada;

		simbolosRepetidos = new ArrayList<>();
		for (int i = 0; i < this.nSimbolos; i++)
			simbolosRepetidos.add((char) ('a' + i));
		if (this.usaVacio)
			simbolosRepetidos.add('E');

		simbolos = new ArrayList<>();
	}

	/**
	 * Comprueba si el generador devolverá expresiones que contenga nodos
	 * vacíos.
	 * 
	 * @return <code>true</code> en caso de que las expresiones generadas puedan
	 *         contener nodos vacíos, <code>false</code> en caso contrario.
	 */
	public boolean usaVacio() {
		return usaVacio;
	}

	/**
	 * Reemplaza un subárbol al azar de la expresión por uno nuevo generado. En
	 * caso de que se le proporcione una expresión aumentada, la expresión
	 * mutada será también una expresión aumentada válida.
	 * <p>
	 * Toda modificación realizada afecta a una copia de la expresión original,
	 * no a la propia expresión.
	 * 
	 * @param expresion
	 *            Expresión a partir de la cual obtenemos la mutación.
	 * @return Expresión mutada.
	 */
	public ExpresionRegular mutacion(ExpresionRegular expresion) {
		boolean esAumentada = expresion.esConcat()
				&& expresion.hijoDerecho().esSimbolo()
				&& expresion.hijoDerecho().simbolo() == '$';

		// Trabaja con la expresión sin aumentar.
		if (esAumentada)
			expresion = expresion.hijoIzquierdo();

		List<ExpresionRegular> nodos = expresion.nodos().stream()
				.filter(e -> !e.esSimbolo() && !e.esVacio())
				// .filter(e -> e.profundidad() < 2)
				.collect(Collectors.toList());
		ExpresionRegular nodo;
		if (nodos.size() > 1)
			nodo = nodos.get(random.nextInt(nodos.size()));
		else
			throw new UnsupportedOperationException(
					"Imposible aplicar operación de mutación sobre expresión de profundidad " + expresion.profundidad());
		log.debug("Nodo: {}", nodo);
		// ExpresionRegular nuevo = arbol(nodo.profundidad());
		this.esAumentada = false;
		ExpresionRegular nuevo = arbol(nodo.profundidad() + random.nextInt(3)
				- 1);
		this.esAumentada = esAumentada;

		posicion = 0;
		ExpresionRegular mutante = sustituir(expresion, nodo, nuevo);

		// Vuelve a aumentar la expresión.
		if (esAumentada)
			mutante = ExpresionRegular.nodoConcat(mutante,
					ExpresionRegular.nodoAumentado(posicion++));

		log.debug("Mutación de {} -> {}", expresion, mutante);

		return mutante;
	}

	/**
	 * Sustituye un subárbol en un nodo dado de una expresión regular existente,
	 * sin modificar la expresión original. Los nodos se numeran correctamente,
	 * incluyendo el nuevo subárbol.
	 * <p>
	 * Construye la expresión de manera recursiva.
	 * 
	 * @param original
	 *            Expresión regular original.
	 * @param nodo
	 *            Nodo en el que realizar la sustitución. Será reemplazado por
	 *            la raíz del subárbol.
	 * @param nuevo
	 *            Subárbol a introducir en la expresión.
	 * @return Nueva expresión regular con la sustitución realizada.
	 */
	private ExpresionRegular sustituir(ExpresionRegular original,
			ExpresionRegular nodo, ExpresionRegular nuevo) {
		ExpresionRegular hijoDerecho;
		ExpresionRegular hijoIzquierdo;

		if (original.esVacio())
			return original;
		else if (original.esSimbolo())
			return ExpresionRegular.nodoSimbolo(++posicion, original.simbolo());
		else if (original.esCierre()) {
			if (original.hijoIzquierdo().equals(nodo))
				hijoIzquierdo = nuevo;
			else
				hijoIzquierdo = sustituir(original.hijoIzquierdo(), nodo, nuevo);
			return ExpresionRegular.nodoCierre(hijoIzquierdo);
		} else if (original.esConcat()) {
			if (original.hijoDerecho().equals(nodo))
				hijoDerecho = nuevo;
			else
				hijoDerecho = sustituir(original.hijoDerecho(), nodo, nuevo);
			if (original.hijoIzquierdo().equals(nodo))
				hijoIzquierdo = nuevo;
			else
				hijoIzquierdo = sustituir(original.hijoIzquierdo(), nodo, nuevo);
			return ExpresionRegular.nodoConcat(hijoIzquierdo, hijoDerecho);
		} else if (original.esUnion()) {
			if (original.hijoDerecho().equals(nodo))
				hijoDerecho = nuevo;
			else
				hijoDerecho = sustituir(original.hijoDerecho(), nodo, nuevo);
			if (original.hijoIzquierdo().equals(nodo))
				hijoIzquierdo = nuevo;
			else
				hijoIzquierdo = sustituir(original.hijoIzquierdo(), nodo, nuevo);
			return ExpresionRegular.nodoUnion(hijoIzquierdo, hijoDerecho);
		}

		log.error(
				"Encontrado nodo de tipo no válido al sustituir subárbol, en expresión {}, nodo {}, subárbol {}",
				original, nodo, nuevo);
		throw new UnsupportedOperationException("Nodo de tipo no válido.");
	}

	/**
	 * Obtiene un árbol de la profundidad dada, con los parámetros establecidos
	 * en el generador.
	 * 
	 * @param profundidad
	 *            Profundidad del árbol de expresión regular a obtener.
	 * @return Expresión regular con un árbol correspondiente de la profundidad
	 *         dada.
	 */
	public ExpresionRegular arbol(int profundidad) {
		simbolos.addAll(simbolosRepetidos);
		posicion = 0;

		return subArbol(profundidad, null);
	}

	/**
	 * Genera un sub-árbol de expresión regular con la profundidad dada y
	 * utilizando un conjunto de operadores concreto. Se llama a si mismo de
	 * manera recursiva para completar la construcción.
	 * 
	 * @param profundidad
	 *            Profundidad del árbol pedido.
	 * @param operadores
	 *            Operadores a utilizar. Cuando se llama desde el exterior de la
	 *            clase, este argumento será <code>null</code>.
	 * @return Expresión regular generada.
	 */
	private ExpresionRegular subArbol(int profundidad,
			EnumSet<Operador> operadores) {
		ExpresionRegular hijoIzquierdo;
		ExpresionRegular hijoDerecho;

		if (operadores == null) {
			if (esAumentada) {
				hijoIzquierdo = subArbol(profundidad, Operador.COMPLETO);
				return ExpresionRegular
						.nodoConcat(hijoIzquierdo,
								ExpresionRegular.nodoAumentado(posicion));
			} else {
				operadores = Operador.COMPLETO;
			}
		}

		// Hoja del árbol.
		if (profundidad <= 0) {
			if (operadores.equals(Operador.COMPLETO) && usaVacio())
				operadores = Operador.FINAL_COMPLETO;
			else
				operadores = Operador.FINAL_PARCIAL;
		}

		// Nodo operador.
		switch (operador(operadores)) {
		case VACIO: // Vacío solo actúa como marcador.
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
			return ExpresionRegular.nodoConcat(hijoIzquierdo, hijoDerecho);
		case UNION:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoUnion(hijoIzquierdo, hijoDerecho);
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
	 * Genera un símbolo cualquiera de aquellos que puede incluir el árbol.
	 * Puede contener el símbolo vacío. Prioritiza símbolos que aún no hayan
	 * aparecido.
	 * 
	 * @return Un símbolo cualquiera o el símbolo vacío.
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
	 * Genera un símbolo cualquiera de aquellos que puede incluir el árbol,
	 * excluyendo el símbolo vacío. Prioritiza símbolos que aún no hayan
	 * aparecido.
	 * 
	 * @return Un símbolo cualquiera, exceptuando el símbolo vacío.
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
