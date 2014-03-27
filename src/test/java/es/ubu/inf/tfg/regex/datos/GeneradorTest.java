package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeneradorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Comprueba que el generador toma las propiedades correctas en cuanto al
	 * uso de vac�os.
	 */
	@Test
	public void testUsaVacio() {
		Generador generador = new Generador(0, true);
		assertTrue("Generador cree no usar vac�os incorrectamente.",
				generador.usaVacio());

		generador = new Generador(0, false);
		assertFalse("Generador cree usar vac�os incorrectamente.",
				generador.usaVacio());
	}

	/**
	 * Comprueba que el generador toma las propiedades correctas en cuanto al
	 * n�mero de s�mbolos.
	 */
	@Test
	public void testSimbolos() {
		Generador generador = new Generador(5, true);
		assertEquals(
				"Generador con vac�os contiene n�mero erroneo de s�mbolos.", 6,
				generador.simbolos());

		generador = new Generador(5, false);
		assertEquals(
				"Generador sin vac�os contiene n�mero erroneo de s�mbolos.", 5,
				generador.simbolos());
	}

	/**
	 * Comprueba que el generador produce �rboles de la profundidad correcta,
	 * utilizando s�mbolos correctos.
	 */
	@Test
	public void testSubArbol() {
		Generador generador = new Generador(2, true);
		ExpresionRegular expresion = generador.subArbol(8, null);

		assertEquals("Generado �rbol de profundidad erronea", 8,
				profundidad(expresion, 0));
	}

	/**
	 * Calcula de forma recursiva la profundidad de un �rbol de expresi�n
	 * regular.
	 * 
	 * @param expresion
	 *            Expresi�n regular en forma de �rbol.
	 * @param profundidad
	 *            Profundidad alcanzada hasta el momento.
	 * @return Profundidad alcanzada tras analizar el nodo actual.
	 */
	private int profundidad(ExpresionRegular expresion, int profundidad) {
		if (expresion.esSimbolo() || expresion.esVacio()) {
			return profundidad;
		} else if (expresion.esCierre()) {
			return profundidad(expresion.hijoIzquierdo(), profundidad + 1);
		} else {
			int profIzquierda = profundidad(expresion.hijoIzquierdo(),
					profundidad + 1);
			int profDerecha = profundidad(expresion.hijoDerecho(),
					profundidad + 1);
			return Math.max(profIzquierda, profDerecha);
		}
	}
}
