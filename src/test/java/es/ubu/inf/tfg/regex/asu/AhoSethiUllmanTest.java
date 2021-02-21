package es.ubu.inf.tfg.regex.asu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AhoSethiUllmanTest {

	private AhoSethiUllman asu;

	@Before
	public void setUp() throws Exception {
		asu = new AhoSethiUllman("((a|b*)a*c)*");
	}

	@After
	public void tearDown() throws Exception {
		asu = null;
	}

	/**
	 * Comprueba que el ejercicio lanza UnsupportedOperationException ante
	 * expresiones no v�lidas.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testParserException() {
		asu = new AhoSethiUllman("a||"); // ParseException
	}

	/**
	 * Comprueba que el ejercicio lanza UnsupportedOperationException ante
	 * expresiones no v�lidas.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testTokenManagerException() {
		asu = new AhoSethiUllman("ab*;"); // TokenMgrError
	}

	/**
	 * Comprueba que el ejercicio almacena el problema correctamente.
	 */
	@Test
	public void testProblema() {
		assertEquals("El ejercicio almacena el problema err�neo.",
				"((a|b*)a*c)*", asu.problema());
	}

	/**
	 * Comprueba que el ejercicio devuelve la expresi�n aumentada correcta.
	 */
	@Test
	public void testExpresionAumentada() {
		System.out.println(asu.expresionAumentada());
		assertEquals("El ejercicio almacena la expresi�n aumentada err�nea.",
				"((a|b*)�a*�c)*�$", asu.expresionAumentada());
	}

	/**
	 * Comprueba que el ejercicio calcula correctamente el conjunto primera-pos.
	 */
	@Test
	public void testPrimeraPos() {
		assertEquals("Calculo incorrecto del conjunto primera-pos.",
				set(1, 2, 3, 4, 5), asu.primeraPos());
	}

	/**
	 * Comprueba que el ejercicio mantiene una lista de s�mbolos correcta.
	 */
	@Test
	public void testSimbolos() {
		assertEquals("Conjunto de s�mbolos incorrecto.",
				set('a', 'b', 'c', '$'), asu.simbolos());
	}

	/**
	 * Comprueba que el ejercicio es capaz de devolver las posiciones correctas
	 * para cada s�mbolo.
	 */
	@Test
	public void testPosicion() {
		assertEquals("Incorrecta posici�n para un s�mbolo dado.", set(1, 3),
				asu.posiciones('a'));
		assertEquals("Incorrecta posici�n para un s�mbolo dado.", set(2),
				asu.posiciones('b'));
		assertEquals("Incorrecta posici�n para un s�mbolo dado.", set(4),
				asu.posiciones('c'));
		assertEquals("Incorrecta posici�n para un s�mbolo dado.", set(5),
				asu.posiciones('$'));
	}

	/**
	 * Comprueba que el ejercicio mantiene una lista de posiciones correcta.
	 */
	@Test
	public void testPosiciones() {
		assertEquals("Conjunto de posiciones incorrecto.", set(1, 2, 3, 4, 5),
				asu.posiciones());
	}

	/**
	 * Comprueba que el ejercicio es capaz de calcular correctamente la tabla
	 * siguiente-pos.
	 */
	@Test
	public void testSiguientePos() {
		assertEquals("Funci�n siguiente-pos err�nea.", set(3, 4),
				asu.siguientePos(1));
		assertEquals("Funci�n siguiente-pos err�nea.", set(2, 3, 4),
				asu.siguientePos(2));
		assertEquals("Funci�n siguiente-pos err�nea.", set(3, 4),
				asu.siguientePos(3));
		assertEquals("Funci�n siguiente-pos err�nea.", set(1, 2, 3, 4, 5),
				asu.siguientePos(4));
		assertEquals("Funci�n siguiente-pos err�nea.", new TreeSet<Integer>(),
				asu.siguientePos(5));
	}

	/**
	 * Comprueba que el ejercicio es capaz de obtener una lista de estados
	 * correcta.
	 */
	@Test
	public void testEstados() {
		assertEquals("Conjunto de estados incorrecto.", set('A', 'B', 'C', 'D'),
				asu.estados());
	}

	/**
	 * Comprueba que el ejercicio es capaz de obtener el conjunto de posiciones
	 * correcto para un estado dado.
	 */
	@Test
	public void testEstado() {
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(1, 2, 3, 4, 5), asu.estado('A'));
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(3, 4), asu.estado('B'));
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(2, 3, 4), asu.estado('C'));
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				new TreeSet<Integer>(), asu.estado('D'));
	}

	/**
	 * Comprueba que el ejercicio es capaz de calcular correctamente una funci�n
	 * de transici�n.
	 */
	@Test
	public void testMueve() {
		assertEquals("Funci�n de transici�n incorrecta.", 'B',
				asu.mueve('A', 'a'));
		assertEquals("Funci�n de transici�n incorrecta.", 'C',
				asu.mueve('A', 'b'));
		assertEquals("Funci�n de transici�n incorrecta.", 'A',
				asu.mueve('A', 'c'));
		assertEquals("Funci�n de transici�n incorrecta.", 'B',
				asu.mueve('B', 'a'));
		assertEquals("Funci�n de transici�n incorrecta.", 'D',
				asu.mueve('B', 'b'));
		assertEquals("Funci�n de transici�n incorrecta.", 'A',
				asu.mueve('B', 'c'));
		assertEquals("Funci�n de transici�n incorrecta.", 'B',
				asu.mueve('C', 'a'));
		assertEquals("Funci�n de transici�n incorrecta.", 'C',
				asu.mueve('C', 'b'));
		assertEquals("Funci�n de transici�n incorrecta.", 'A',
				asu.mueve('C', 'c'));
		assertEquals("Funci�n de transici�n incorrecta.", 'D',
				asu.mueve('D', 'a'));
		assertEquals("Funci�n de transici�n incorrecta.", 'D',
				asu.mueve('D', 'b'));
		assertEquals("Funci�n de transici�n incorrecta.", 'D',
				asu.mueve('D', 'c'));
	}

	/**
	 * Comprueba que el ejercicio es capaz de identificar correctamente los
	 * estados finales.
	 */
	@Test
	public void testEsFinal() {
		assertTrue("Estado final identificado como no final.", asu.esFinal('A'));
		assertFalse("Estado no final identificado como final.",
				asu.esFinal('B'));
		assertFalse("Estado no final identificado como final.",
				asu.esFinal('C'));
		assertFalse("Estado no final identificado como final.",
				asu.esFinal('D'));
	}

	/**
	 * Genera un set a partir de una lista de enteros de longitud variable.
	 * 
	 * @param ns
	 *            Lista de enteros de longitud variable.
	 * @return Set de enteros.
	 */
	private static Set<Integer> set(int... ns) {
		Set<Integer> set = new TreeSet<>();
		for (int n : ns)
			set.add(n);
		return set;
	}

	/**
	 * Genera un set a partir de una lista de caracteres de longitud variable.
	 * 
	 * @param ns
	 *            Lista de caracteres de longitud variable.
	 * @return Set de caracteres.
	 */
	private static Set<Character> set(char... ns) {
		Set<Character> set = new TreeSet<>();
		for (char n : ns)
			set.add(n);
		return set;
	}
}
