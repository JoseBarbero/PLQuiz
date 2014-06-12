package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class GeneradorTest {

	private static final Logger log = LoggerFactory
			.getLogger(GeneradorTest.class);

	private final int ITERACIONES = 100_000;
	private final int MAX_PROFUNDIDAD = 6;

	/**
	 * Comprueba que el generador produce �rboles de la profundidad correcta,
	 * utilizando s�mbolos correctos.
	 */
	@Test
	public void testArbol() {
		Generador generador = new Generador(2, true, false);
		ExpresionRegular expresion = generador.arbol(8);

		assertEquals("Generado �rbol de profundidad erronea", 8,
				expresion.profundidad());

		generador = new Generador(2, true, true);
		expresion = generador.arbol(8);

		assertEquals("Generado �rbol de profundidad erronea", 9,
				expresion.profundidad());

		generador = new Generador(5, false, false);
		expresion = generador.arbol(12);

		assertEquals("Generado �rbol de profundidad erronea", 12,
				expresion.profundidad());

		generador = new Generador(5, false, true);
		expresion = generador.arbol(12);

		assertEquals("Generado �rbol de profundidad erronea", 13,
				expresion.profundidad());
	}

	/**
	 * Comprueba que las mutaciones sobre una expresi�n devuelven expresiones
	 * correctas (profundidad dada +/- 1).
	 */
	@Test
	public void testMutacion() {
		Generador generador = new Generador(2, true, false);
		ExpresionRegular expresion = generador.arbol(8);
		ExpresionRegular mutante = generador.mutacion(expresion);

		assertFalse("La expresi�n mutada es igual a la original.",
				expresion.equals(mutante));
		assertTrue("La expresi�n mutada tiene una profundidad erronea.",
				Math.abs(8 - mutante.profundidad()) <= 1);
	}

	/**
	 * Genera una serie de problemas Aho-Sethi-Ullman a partir de expresiones
	 * regulares aleatorias y almacena sus caracteristicas.
	 * <p>
	 * No incluye nodos vac�os.
	 */
	@Test
	@Ignore
	public void testGeneracionASUNoVacio() {
		genera(ITERACIONES, false, "ASU", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de problemas de Construcci�n de Subconjuntos a partir de
	 * expresiones regulares aleatorias y almacena sus caracteristicas.
	 * <p>
	 * No incluye nodos vac�os.
	 */
	@Test
	@Ignore
	public void testGeneracionCSNoVacio() {
		genera(ITERACIONES, false, "CS", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de problemas Aho-Sethi-Ullman a partir de expresiones
	 * regulares aleatorias y almacena sus caracteristicas.
	 * <p>
	 * Incluye nodos vac�os.
	 */
	@Test
	@Ignore
	public void testGeneracionASUVacio() {
		genera(ITERACIONES, true, "ASU", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de problemas de Construcci�n de Subconjuntos a partir de
	 * expresiones regulares aleatorias y almacena sus caracteristicas.
	 * <p>
	 * Incluye nodos vac�os.
	 */
	@Test
	@Ignore
	public void testGeneracionCSVacio() {
		genera(ITERACIONES, true, "CS", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de instancias de expresiones regulares, con o sin nodos
	 * vac�os, y analiza sus caracter�sticas de acuerdo con un tipo de problema
	 * dado.
	 * <p>
	 * Los resultados se almacenan en el log.
	 */
	private void genera(int n, boolean vacio, String tipo, int profundidad) {
		Generador generador;
		ExpresionRegular expresion;
		int it;

		switch (tipo) {
		case "ASU":
			AhoSethiUllman asu;
			generador = new Generador(6, vacio, true);

			while (profundidad > 0) {
				it = n;
				log.info("profundidad {}", profundidad);
				while (it > 0) {
					expresion = generador.arbol(profundidad);
					asu = new AhoSethiUllman(expresion);

					log.info("{} {}", asu.simbolos().size(), asu.estados()
							.size());

					it--;
				}
				profundidad--;
			}
			break;
		case "CS":
			ConstruccionSubconjuntos cs;
			generador = new Generador(6, vacio, false);

			while (profundidad > 0) {
				it = n;
				log.info("profundidad {}", profundidad);
				while (it > 0) {
					expresion = generador.arbol(profundidad);
					cs = new ConstruccionSubconjuntos(expresion);

					log.info("{} {}", cs.simbolos().size(), cs.estados().size());

					it--;
				}
				profundidad--;
			}
			break;
		}
	}
}
