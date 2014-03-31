package es.ubu.inf.tfg.regex.thompson;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;

/**
 * ConstruccionSubconjuntosGenerador implementa una clase encargada de generar
 * problemas de tipo construcci�n de subconjuntos con los par�metros
 * especificados, siguiendo un algoritmo de b�squeda aleatoria.
 * <p>
 * El generador no garantiza que los resultados se adapten perfectamente a los
 * par�metros de entrada.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class ConstruccionSubconjuntosGenerador {

	private static final int MAX_ITERACIONES = Integer.MAX_VALUE;
	private static final int MAX_PROFUNDIDAD = 6;
	private static final int MIN_PROFUNDIDAD = 2;

	private Generador generador;

	/**
	 * Genera un nuevo problema de tipo ConstruccionSubconjuntos. Intentar�
	 * acercarse lo m�s posible al n�mero de s�mbolos y de estados especificado.
	 * El algoritmo es capaz de variar la profundidad a la que busca en funci�n
	 * de los resultados que vaya obteniendo, entre ciertos m�rgenes.
	 * 
	 * @param nSimbolos
	 *            N�mero de s�mbolos que se quiere que el problema utilice.
	 * @param nEstados
	 *            N�mero de estados que se quiere que contenga la tabla de
	 *            transici�n del problema.
	 * @param usaVacio
	 *            Si queremos que el problema genere nodos vac�os. Su aparici�n
	 *            no se garantiza.
	 * @return Un nuevo problema de tipo ConstruccionSubconjuntos.
	 */
	public ConstruccionSubconjuntos nuevo(int nSimbolos, int nEstados,
			boolean usaVacio) {
		ConstruccionSubconjuntos candidato = null, actual = null;
		ExpresionRegular expresion;

		int iteraciones = 0;
		int profundidad = MIN_PROFUNDIDAD;

		do {
			// Inicializa variables
			generador = new Generador(nSimbolos, usaVacio);

			// Genera expresi�n
			expresion = generador.subArbol(profundidad, null);

			// Evalua candidato
			actual = new ConstruccionSubconjuntos(expresion);
			if (candidato == null
					|| evalua(actual, nEstados) < evalua(candidato, nEstados))
				candidato = actual;

			// Modifica la profundidad
			int dif = nEstados - actual.estados().size();
			if (dif > 1)
				profundidad++;
			else if (dif < 1)
				profundidad--;

			if (profundidad < MIN_PROFUNDIDAD)
				profundidad = MIN_PROFUNDIDAD;
			else if (profundidad > MAX_PROFUNDIDAD)
				profundidad = MAX_PROFUNDIDAD;

			iteraciones++;
		} while (evalua(candidato, nEstados) != 0
				&& iteraciones < MAX_ITERACIONES);

		return candidato;
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
	private int evalua(ConstruccionSubconjuntos problema, int nEstados) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos;

		if (generador.usaVacio())
			diferenciaSimbolos = Math.abs(problema.simbolos().size()
					- generador.simbolos() + 1);
		else
			diferenciaSimbolos = Math.abs(problema.simbolos().size()
					- generador.simbolos());

		return diferenciaEstados + diferenciaSimbolos;
	}
}