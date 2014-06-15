package es.ubu.inf.tfg.regex.asu;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;

/**
 * AhoSethiUllmanGenerador implementa una clase encargada de generar problemas
 * de tipo AhoSethiUllman con los par�metros especificados, siguiendo un
 * algoritmo de b�squeda aleatoria.
 * <p>
 * El generador no garantiza que los resultados se adapten perfectamente a los
 * par�metros de entrada.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class AhoSethiUllmanGenerador {

	private static final Logger log = LoggerFactory
			.getLogger(AhoSethiUllmanGenerador.class);

	private static final int MAX_ITERACIONES = 3000;
	private static final int MAX_PROFUNDIDAD = 6;
	private static final int MIN_PROFUNDIDAD = 3;

	private Generador generador;
	private AtomicBoolean cancelar = new AtomicBoolean();

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
		log.info(
				"Generando problema de Aho-Sethi-Ullman con {} simbolos y {} estados, vacios = {}.",
				nSimbolos, nEstados, usaVacio);

		AhoSethiUllman candidato = null, actual = null;
		int evaluaCandidato = 0, evaluaActual;
		ExpresionRegular expresion;

		int iteraciones = 0;
		int profundidad = MIN_PROFUNDIDAD;

		// Inicializa variables
		generador = new Generador(nSimbolos, usaVacio, true);

		do {
			expresion = generador.arbol(profundidad);
			actual = new AhoSethiUllman(expresion);
			
			evaluaActual = evalua(actual, nEstados, nSimbolos);
			
			if (candidato == null
					||  (evaluaActual < evaluaCandidato)) {
				candidato = actual;
				evaluaCandidato = evaluaActual;
			}

			// Modifica la profundidad
			int dif = nEstados - actual.estados().size();
			if (dif > 1 && profundidad < MAX_PROFUNDIDAD)
				profundidad++;
			else if (dif < 1 && profundidad > MIN_PROFUNDIDAD)
				profundidad--;

			iteraciones++;
		} while (evalua(candidato, nEstados, nSimbolos) != 0
				&& iteraciones < MAX_ITERACIONES);

		log.info("Soluci�n encontrada en {} iteraciones.", iteraciones);

		return candidato;
	}

	/**
	 * Evalua un problema en funci�n a como se adapta a los par�metros pedidos.
	 * Tiene en cuenta tanto que el n�mero de estados sea el pedido, como que
	 * use todos los s�mbolos.
	 * <p>
	 * Cuanto m�s cerca este del cero, m�s cerca esta el problema de la
	 * soluci�n.
	 * 
	 * @param problema
	 *            Problema a evaluar.
	 * @param nEstados
	 *            N�mero de estados en el problema pedido.
	 * @return Funci�n de evaluaci�n del problema.
	 */
	private int evalua(AhoSethiUllman problema, int nEstados, int nSimbolos) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos = Math.abs(problema.simbolos().size() - 1
				- nSimbolos);

		return diferenciaEstados + diferenciaSimbolos;
	}

	/**
	 * Cancela la generaci�n del problema, devolviendo el resultado de la
	 * iteraci�n actual.
	 */
	public void cancelar() {
		log.info("Cancelando generaci�n de problema.");
		cancelar.compareAndSet(false, true);
	}
}
