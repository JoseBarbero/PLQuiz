package es.ubu.inf.tfg.asu;

import static org.junit.Assert.*;

import org.junit.Test;

public class AhoSethiUllmanGeneradorTest {

	AhoSethiUllmanGenerador generador = AhoSethiUllmanGenerador.getInstance();

	/**
	 * Comprueba que la petici�n de m�ltiples instancias de
	 * AhoSethiUllmanGenerador da como resultado m�ltiples referencias a la
	 * misma instancia. Es decir, implementa correctamente el patr�n singleton.
	 */
	@Test
	public void testGetInstance() {
		AhoSethiUllmanGenerador generadorB = AhoSethiUllmanGenerador
				.getInstance();

		assertSame("Comportamiento de singleton erroneo.", generador,
				generadorB);
	}
	
	/**
	 * Comprueba que la clase genera un problema sin incluir nodos vac�os y con los
	 * par�metros pedidos o similares.
	 */
	@Test
	public void testNuevoNoVacio() {
		AhoSethiUllman problema = generador.nuevo(3, 8, false);
		
		// Tenemos en cuenta $.
		boolean similarSimbolos = Math.abs(4 - problema.simbolos().size()) <= 1;
		boolean similarEstados = Math.abs(8 - problema.estados().size()) <= 1;

		assertTrue("Generado problema no v�lido", similarSimbolos || similarEstados);
	}

	/**
	 * Comprueba que la clase genera un problema incluyendo nodos vac�os y con los
	 * par�metros pedidos o similares.
	 */
	@Test
	public void testNuevoVacio() {
		AhoSethiUllman problema = generador.nuevo(5, 10, true);

		// Tenemos en cuenta $.
		boolean similarSimbolos = Math.abs(7 - problema.simbolos().size()) <= 1;
		boolean similarEstados = Math.abs(10 - problema.estados().size()) <= 1;

		assertTrue("Generado problema no v�lido", similarSimbolos || similarEstados);
	}
}
