package es.ubu.inf.tfg.regex.asu.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

public class NodoTest {

	private ExpresionRegular expresion; // ((a|b*)a*c)*
	private Nodo nodo;

	@Before
	public void setUp() throws Exception {
		expresion = ExpresionRegular.nodoSimbolo(1, 'a');
		expresion = ExpresionRegular.nodoUnion(ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(2, 'b')), expresion);
		expresion = ExpresionRegular.nodoConcat(ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(3, 'a')), expresion);
		expresion = ExpresionRegular.nodoConcat(
				ExpresionRegular.nodoSimbolo(4, 'c'), expresion);
		expresion = ExpresionRegular.nodoCierre(expresion);
		expresion = ExpresionRegular.nodoConcat(
				ExpresionRegular.nodoAumentado(5), expresion);

		nodo = new Nodo(expresion);
	}

	@After
	public void tearDown() throws Exception {
		expresion = null;
		nodo = null;
	}

	/**
	 * Comprueba que el nodo mantiene las referencias correctas.
	 */
	@Test
	public void testExpresion() {
		assertEquals("Referencia inv�lida a expresi�n regular", expresion,
				nodo.expresion());
	}

	/**
	 * Comprueba que los nodos hijos mantienen las referencias correctas.
	 */
	@Test
	public void testExpresionHijos() {
		assertEquals("Referencia inv�lida a expresi�n regular",
				expresion.hijoDerecho(), nodo.hijoDerecho().expresion());
		assertEquals("Referencia inv�lida a expresi�n regular",
				expresion.hijoIzquierdo(), nodo.hijoIzquierdo().expresion());
	}

	/**
	 * Comprueba que los nodos correctos sean anulables
	 */
	@Test
	public void testEsAnulable() {
		// ra�z
		assertFalse("Nodo incorrectamente anulable.", nodo.esAnulable());

		// profundidad 1
		assertTrue("Nodo incorrectamente no anulable.", nodo.hijoIzquierdo()
				.esAnulable());
		assertFalse("Nodo incorrectamente anulable.", nodo.hijoDerecho()
				.esAnulable());

		// profundidad 2
		assertFalse("Nodo incorrectamente anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().esAnulable());

		// profundidad 3
		assertTrue("Nodo incorrectamente no anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().esAnulable());
		assertFalse("Nodo incorrectamente anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoDerecho().esAnulable());

		// profundidad 4
		assertTrue("Nodo incorrectamente no anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().esAnulable());
		assertTrue("Nodo incorrectamente no anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoDerecho().esAnulable());

		// profundidad 5
		assertFalse("Nodo incorrectamente anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo()
				.hijoIzquierdo().esAnulable());
		assertTrue("Nodo incorrectamente no anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().hijoDerecho()
				.esAnulable());
		assertFalse("Nodo incorrectamente anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoDerecho().hijoIzquierdo()
				.esAnulable());

		// profundidad 6
		assertFalse("Nodo incorrectamente anulable.", nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().hijoDerecho()
				.hijoIzquierdo().esAnulable());
	}

	/**
	 * Comprueba que la primera-pos de cada nodo sea la correcta.
	 */
	@Test
	public void testPrimeraPos() {
		// ra�z
		assertEquals("Primera-pos incorrecta.", set(1, 2, 3, 4, 5),
				nodo.primeraPos());

		// profundidad 1
		assertEquals("Primera-pos incorrecta.", set(1, 2, 3, 4), nodo
				.hijoIzquierdo().primeraPos());
		assertEquals("Primera-pos incorrecta.", set(5), nodo.hijoDerecho()
				.primeraPos());

		// profundidad 2
		assertEquals("Primera-pos incorrecta.", set(1, 2, 3, 4), nodo
				.hijoIzquierdo().hijoIzquierdo().primeraPos());

		// profundidad 3
		assertEquals("Primera-pos incorrecta.", set(1, 2, 3), nodo
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().primeraPos());
		assertEquals("Primera-pos incorrecta.", set(4), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoDerecho().primeraPos());

		// profundidad 4
		assertEquals("Primera-pos incorrecta.", set(1, 2), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().primeraPos());
		assertEquals("Primera-pos incorrecta.", set(3), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoDerecho().primeraPos());

		// profundidad 5
		assertEquals("Primera-pos incorrecta.", set(1), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo()
				.hijoIzquierdo().primeraPos());
		assertEquals("Primera-pos incorrecta.", set(2), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().hijoDerecho()
				.primeraPos());
		assertEquals("Primera-pos incorrecta.", set(3), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoDerecho().hijoIzquierdo()
				.primeraPos());

		// profundidad 6
		assertEquals("Primera-pos incorrecta.", set(2), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().hijoDerecho()
				.hijoIzquierdo().primeraPos());
	}

	/**
	 * Comprueba que la �ltima-pos de cada nodo sea la correcta.
	 */
	@Test
	public void testUltimaPos() {
		// ra�z
		assertEquals("Ultima-pos incorrecta.", set(5), nodo.ultimaPos());

		// profundidad 1
		assertEquals("Ultima-pos incorrecta.", set(4), nodo.hijoIzquierdo()
				.ultimaPos());
		assertEquals("Ultima-pos incorrecta.", set(5), nodo.hijoDerecho()
				.ultimaPos());

		// profundidad 2
		assertEquals("Ultima-pos incorrecta.", set(4), nodo.hijoIzquierdo()
				.hijoIzquierdo().ultimaPos());

		// profundidad 3
		assertEquals("Ultima-pos incorrecta.", set(1, 2, 3), nodo
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().ultimaPos());
		assertEquals("Ultima-pos incorrecta.", set(4), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoDerecho().ultimaPos());

		// profundidad 4
		assertEquals("Ultima-pos incorrecta.", set(1, 2), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().ultimaPos());
		assertEquals("Ultima-pos incorrecta.", set(3), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoDerecho().ultimaPos());

		// profundidad 5
		assertEquals("Ultima-pos incorrecta.", set(1), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo()
				.hijoIzquierdo().ultimaPos());
		assertEquals("Ultima-pos incorrecta.", set(2), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().hijoDerecho()
				.ultimaPos());
		assertEquals("Ultima-pos incorrecta.", set(3), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoDerecho().hijoIzquierdo()
				.ultimaPos());

		// profundidad 6
		assertEquals("Ultima-pos incorrecta.", set(2), nodo.hijoIzquierdo()
				.hijoIzquierdo().hijoIzquierdo().hijoIzquierdo().hijoDerecho()
				.hijoIzquierdo().ultimaPos());
	}

	/**
	 * Comprueba que el diccionario de s�mbolos sea el correcto.
	 */
	@Test
	public void testSimbolos() {
		MapaPosiciones<Character> simbolos = new MapaPosiciones<>();
		simbolos.add('a', set(1, 3));
		simbolos.add('b', 2);
		simbolos.add('c', 4);
		simbolos.add('$', 5);

		assertEquals("Diccionario de s�mbolos incorrecto", simbolos,
				nodo.simbolos());
	}

	/**
	 * Comprueba que la tabla siguiente-pos sea la correcta.
	 */
	@Test
	public void testSiguientePos() {
		MapaPosiciones<Integer> siguientePos = new MapaPosiciones<>();
		siguientePos.add(1, set(3, 4));
		siguientePos.add(2, set(2, 3, 4));
		siguientePos.add(3, set(3, 4));
		siguientePos.add(4, set(1, 2, 3, 4, 5));
		siguientePos.add(5, set());

		assertEquals("Tabla siguiente-pos incorrecta", siguientePos,
				nodo.siguientePos());
	}

	/**
	 * Comprueba que los gr�ficos graphviz se generan correctamente.
	 */
	@Test
	public void testImagenDot() {
		String esperado = "digraph {\n\tA [label=\"A\n&#8226;\"];\n\t"
				+ "A -> B\n\tB [label=\"B\n*\"];\n\t"
				+ "A -> C\n\tC [label=\"C\n$\"];\n\t"
				+ "B -> D\n\tD [label=\"D\n&#8226;\"];\n\t"
				+ "D -> E\n\tE [label=\"E\n&#8226;\"];\n\t"
				+ "D -> F\n\tF [label=\"F\nc\"];\n\tE -> G\n\t"
				+ "G [label=\"G\n|\"];\n\tE -> H\n\tH [label=\"H\n*\"];\n\t"
				+ "G -> I\n\tI [label=\"I\na\"];\n\tG -> J\n\t"
				+ "J [label=\"J\n*\"];\n\tH -> K\n\tK [label=\"K\na\"];\n\t"
				+ "J -> L\n\tL [label=\"L\nb\"];\n}";

		assertEquals("Error generando imagen de �rbol en formato dot.",
				esperado, nodo.imagenDot());
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
}
