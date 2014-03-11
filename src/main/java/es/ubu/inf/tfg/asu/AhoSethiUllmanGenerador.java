package es.ubu.inf.tfg.asu;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;

/**
 * AhoSethiUllmanGenerador implementa una clase encargada de generar problemas
 * de tipo AhoSethiUllman con los par�metros especificados, siguiendo un
 * algoritmo de b�squeda aleatoria, con generaci�n de �rboles basada en el
 * algoritmo GROW.
 * <p>
 * El generador implementa el patr�n de dise�o singleton, representando un
 * proveedor de problemas que recibe peticiones y devuelve los resultados de la
 * b�squeda.
 * <p>
 * El generador no garantiza que los resultados se adapten perfectamente a los
 * par�metros de entrada.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class AhoSethiUllmanGenerador {

	private static final int MAX_ITERACIONES = 25;

	private static final AhoSethiUllmanGenerador instance = new AhoSethiUllmanGenerador();
	private static final Random random = new Random(new Date().getTime());

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

		private static List<Character> simbolos;
		private static List<Character> simbolosRepetidos;
		private static int posicion;

		/**
		 * Inicializa los valores del enumerado para empezar a trabajar en un
		 * nuevo �rbol.
		 * 
		 * @param nSimbolos
		 *            N�mero de s�mbolos a generar.
		 * @param usaVacio
		 *            <code>true</code> si la expresi�n contendr� nodos vac�os,
		 *            <code>false</code> de lo contrario.
		 */
		private static void inicializa(int nSimbolos, boolean usaVacio) {
			simbolos = new ArrayList<>();
			for (int i = 0; i < nSimbolos; i++)
				simbolos.add((char) ('a' + i));
			if (usaVacio)
				simbolos.add('E');
			simbolosRepetidos = new ArrayList<>(simbolos);

			posicion = 0;
		}

		/**
		 * Genera un s�mbolo cualquiera de aquellos que puede incluir el �rbol.
		 * Puede contener el s�mbolo vac�o. Prioritiza s�mbolos que a�n no hayan
		 * aparecido.
		 * 
		 * @return Un s�mbolo cualquiera o el s�mbolo vac�o.
		 */
		private static char simbolo() {
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
		private static char simboloNoVacio() {
			int index;

			if (simbolosRepetidos.contains('E')) {
				List<Character> simbolosParcial;
				if (simbolos.size() > 1) {
					simbolosParcial = new ArrayList<>(simbolos);
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
		 * Devuelve la siguiente posici�n a a�adir en el �rbol.
		 * 
		 * @return Siguiente posici�n.
		 */
		private static int posicion() {
			return posicion++;
		}

		/**
		 * Devuelve la cantidad de s�mbolos distintos que debe contener el
		 * �rbol.
		 * 
		 * @return Cantidad de s�mbolos en el �rbol.
		 */
		private static int simbolos() {
			return simbolosRepetidos.size();
		}

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

		/**
		 * Comprueba si el �rbol contendr� nodos vac�os.
		 * 
		 * @return<code>true</code> si la expresi�n contendr� nodos vac�os,
		 *                          <code>false</code> de lo contrario.
		 */
		private static boolean usaVacio() {
			return simbolosRepetidos.contains('E');
		}
	}

	/**
	 * Constructor privado, no se le llama.
	 */
	private AhoSethiUllmanGenerador() {
	}

	/**
	 * Devuelve la instancia compartida de la clase.
	 * 
	 * @return Instancia �nica de AhoSethiUllmanGenerador.
	 */
	public static AhoSethiUllmanGenerador getInstance() {
		return instance;
	}

	/**
	 * Genera un nuevo problema de tipo AhoSethiUllman. Intentar� acercarse lo
	 * m�s posible al n�mero de s�mbolos y de estados especificado.
	 * 
	 * @param nSimbolos
	 *            N�mero de s�mbolos que se quiere que el problema utilice.
	 * @param nEstados
	 *            N�mero de estados que se quiere que contenga la tabla de
	 *            transici�n del problema.
	 * @param usaVacio
	 *            Si queremos que el problema genere nodos vac�os. Su aparici�n
	 *            no se garantiza.
	 * @return Un nuevo problema de tipo AhoSethiUllman.
	 */
	public AhoSethiUllman nuevo(int nSimbolos, int nEstados, boolean usaVacio) {
		AhoSethiUllman candidato = null, actual = null;
		ExpresionRegular expresion;
		int iteraciones = 0;
		int profundidad = 4;

		do {
			// Inicializa variables
			Operador.inicializa(nSimbolos, usaVacio);

			// Genera expresi�n
			expresion = subArbol(profundidad, null);

			// Evalua candidato
			if (candidato == null) {
				candidato = new AhoSethiUllman(expresion);
			} else {
				actual = new AhoSethiUllman(expresion);
				if (evalua(actual, nEstados) < evalua(candidato, nEstados))
					candidato = actual;
			}

			iteraciones++;
		} while (evalua(candidato, nEstados) != 0
				&& iteraciones < MAX_ITERACIONES);
		return candidato;
	}

	/**
	 * Genera un sub-�rbol de expresion regular con la profundidad dada y
	 * utilizando un conjunto de operadores concreto. Se llama a si mismo de
	 * manera recursiva para completar la construcci�n.
	 * 
	 * @param profundidad
	 *            Profundidad del �rbol pedido.
	 * @param operadores
	 *            Operadores a utilizar.
	 * @return Expresi�n regular generada.
	 */
	private ExpresionRegular subArbol(int profundidad,
			EnumSet<Operador> operadores) {

		ExpresionRegular hijoIzquierdo;
		ExpresionRegular hijoDerecho;

		// Raiz del �rbol, aumenta la expresi�n.
		if (operadores == null) {
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = ExpresionRegular.nodoAumentado(Operador.posicion());
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		}

		// Hoja del �rbol.
		if (profundidad <= 0) {
			if (operadores.equals(Operador.COMPLETO) && Operador.usaVacio())
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
				simbolo = Operador.simbolo();
			else
				simbolo = Operador.simboloNoVacio();

			if (simbolo == 'E')
				return ExpresionRegular.nodoVacio();
			return ExpresionRegular.nodoSimbolo(Operador.posicion(), simbolo);
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
	 * Evalua un problema en funci�n a como se adapta a los par�metros pedidos.
	 * Tiene en cuenta tanto que el n�mero de estados sea el pedido, como que
	 * use todos los s�mbolos.
	 * <p>
	 * Cuanto m�s cerca este del n�mero, m�s cerca esta el problema de la
	 * soluci�n.
	 * 
	 * @param problema
	 *            Problema a evaluar.
	 * @param nEstados
	 *            N�mero de estados en el problema pedido.
	 * @return Funci�n de evaluaci�n del problema.
	 */
	private int evalua(AhoSethiUllman problema, int nEstados) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos = Math.abs(problema.simbolos().size()
				- Operador.simbolos());

		return diferenciaEstados + diferenciaSimbolos;
	}
}
