package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneradorTest {

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
}
