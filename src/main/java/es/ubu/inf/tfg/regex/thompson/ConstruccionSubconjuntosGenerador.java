package es.ubu.inf.tfg.regex.thompson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger log = LoggerFactory
			.getLogger(ConstruccionSubconjuntosGenerador.class);

	private static final Random random = new Random(new Date().getTime());

	private static final int MAX_ITERACIONES = 3000;// Integer.MAX_VALUE;
	private static final int MAX_PROFUNDIDAD = 10;
	private static final int MIN_PROFUNDIDAD = 2;

	private static final int ELITISMO = 1;
	private static final int MUTACION = 2;
	private static final int NUEVOS = 3;

	private Generador generador;
	private AtomicBoolean cancelar = new AtomicBoolean();

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
		log.info(
				"Generando problema de construcci�n de subconjuntos con {} simbolos y {} estados, vacios = {}.",
				nSimbolos, nEstados, usaVacio);

		List<ExpresionRegular> poblacion = new ArrayList<>();
		List<ExpresionRegular> elite, mutacion, nuevos = new ArrayList<>();
		ConstruccionSubconjuntos candidato = null;
		ExpresionRegular candidatoExpresion = null;
		int candidatoEvalua = 0;
		generador = new Generador(nSimbolos, usaVacio, false);

		int iteraciones = 0;
		int profundidad;

		Comparator<ExpresionRegular> evalua = Comparator.comparing(e -> evalua(
				new ConstruccionSubconjuntos(e), nEstados, nSimbolos));

		// inicializa poblaci�n
		for (int i = 0; i < (ELITISMO + MUTACION + NUEVOS); i++) {
			profundidad = random.nextInt(MAX_PROFUNDIDAD - MIN_PROFUNDIDAD)
					+ MIN_PROFUNDIDAD;
			poblacion.add(generador.arbol(profundidad));
		}

		mutacion = new ArrayList<>();
		do {
			poblacion.sort(evalua);

			elite = poblacion.stream().limit(ELITISMO)
					.collect(Collectors.toList());
			// mutacion = poblacion.stream()
			// //.skip(ELITISMO)
			// .limit(MUTACION)
			// .map(e -> generador.mutacion(e))
			// .collect(Collectors.toList());

			mutacion.clear();
			for (int i = 0; i < MUTACION && candidatoExpresion != null; i++)
				mutacion.add(generador.mutacion(candidatoExpresion));

			nuevos.clear();

			for (int i = 0; i < NUEVOS; i++) {
				profundidad = elite.get(0).profundidad()
						+ (random.nextInt(3) - 1);
				if (profundidad < MIN_PROFUNDIDAD)
					profundidad = MIN_PROFUNDIDAD;
				if (profundidad > MAX_PROFUNDIDAD)
					profundidad = MAX_PROFUNDIDAD;

				nuevos.add(generador.arbol(profundidad));
			}

			if (candidatoExpresion == null
					|| !poblacion.get(0).equals(candidatoExpresion)) {
				candidatoExpresion = poblacion.get(0);
				candidato = new ConstruccionSubconjuntos(candidatoExpresion);
				candidatoEvalua = evalua(candidato, nEstados, nSimbolos);

				iteraciones = 0;
			} else {
				iteraciones++;
			}

			poblacion.clear();
			poblacion.addAll(elite);
			poblacion.addAll(mutacion);
			poblacion.addAll(nuevos);

			if (iteraciones % 1000 == 0)
				log.warn("{} it con mejor {} prof{}", iteraciones,
						candidatoEvalua, candidatoExpresion.profundidad());

			// log.info("{} iteraciones, {} poblacion, {} prof candidato, {} fitness",
			// iteraciones, poblacion.size(), poblacion.get(0).profundidad(),
			// candidatoEvalua);
		} while (candidatoEvalua != 0 && iteraciones < MAX_ITERACIONES
				&& !cancelar.get());

		// log.warn("{}", candidatoExpresion.profundidad());
		log.info("Soluci�n encontrada en {} iteraciones con valor {}",
				iteraciones, candidatoEvalua);

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
	private int evalua(ConstruccionSubconjuntos problema, int nEstados,
			int nSimbolos) {
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
