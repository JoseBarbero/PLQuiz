package es.ubu.inf.tfg.regex.datos;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Generador implementa una clase encargada de generar �rboles de expresi�n
 * regular de un tama�o y caracter�sticas dadas, con generaci�n de �rboles
 * basada en el algoritmo GROW.
 * 
 * @author Roberto Izquierdo Amo.
 * 
 */
public class Generador {

	private static final Random random = new Random(new Date().getTime());

	private List<Character> simbolos;
	private List<Character> simbolosRepetidos;
	private int posicion;

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

		/**
		 * Devuelve un operador aleatorio de entre un conjunto de operadores.
		 * 
		 * @param operadores
		 *            Conjunto de operadores entre los que elegir.
		 * @return Operador cualquiera del conjunto.
		 */
		private static Operador random(EnumSet<Operador> operadores) {
			int index = random.nextInt(operadores.size());
			return (Operador) operadores.toArray()[index];
		}
	}

	/**
	 * Inicializa los valores del generador para empezar a trabajar en un nuevo
	 * �rbol.
	 * 
	 * @param nSimbolos
	 *            N�mero de s�mbolos a generar.
	 * @param usaVacio
	 *            <code>true</code> si la expresi�n contendr� nodos vac�os,
	 *            <code>false</code> de lo contrario.
	 */
	public Generador(int nSimbolos, boolean usaVacio) {
		simbolos = new ArrayList<>();
		for (int i = 0; i < nSimbolos; i++)
			simbolos.add((char) ('a' + i));
		if (usaVacio)
			simbolos.add('E');
		simbolosRepetidos = new ArrayList<>(simbolos);

		posicion = 0;
	}

	/**
	 * Comprueba si el �rbol contendr� nodos vac�os.
	 * 
	 * @return<code>true</code> si la expresi�n contendr� nodos vac�os,
	 *                          <code>false</code> de lo contrario.
	 */
	public boolean usaVacio() {
		return simbolosRepetidos.contains('E');
	}

	/**
	 * Devuelve la cantidad de s�mbolos distintos que debe contener el �rbol.
	 * 
	 * @return Cantidad de s�mbolos en el �rbol.
	 */
	public int simbolos() {
		return simbolosRepetidos.size();
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
	 * @return Un s�mbolo cualquiera.
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
	public ExpresionRegular subArbol(int profundidad,
			EnumSet<Operador> operadores) {

		ExpresionRegular hijoIzquierdo;
		ExpresionRegular hijoDerecho;

		// Raiz del �rbol, aumenta la expresi�n.
		if (operadores == null) {
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = ExpresionRegular.nodoAumentado(posicion++);
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		}

		// Hoja del �rbol.
		if (profundidad <= 0) {
			if (operadores.equals(Operador.COMPLETO) && usaVacio())
				operadores = Operador.FINAL_COMPLETO;
			else
				operadores = Operador.FINAL_PARCIAL;
		}

		// Nodo operador.
		switch (Operador.random(operadores)) {
		case VACIO: // Vac�o solo actua como marcador.
		case SIMBOLO:
			char simbolo;
			if (operadores.equals(Operador.FINAL_COMPLETO))
				simbolo = simbolo();
			else
				simbolo = simboloNoVacio();

			if (simbolo == 'E')
				return ExpresionRegular.nodoVacio();
			return ExpresionRegular.nodoSimbolo(posicion++, simbolo);
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
}
