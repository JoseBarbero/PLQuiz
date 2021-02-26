package es.ubu.inf.tfg.regex.thompson.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

public class AutomataTest {
	private Automata automata;

	@Before
	public void setUp() throws Exception { // (a|b)*abb
		ExpresionRegular expresion;

		expresion = ExpresionRegular.nodoSimbolo(1, 'a');   // a
		expresion = ExpresionRegular.nodoUnion(expresion,
				ExpresionRegular.nodoSimbolo(2, 'b'));      // (a|b)
		expresion = ExpresionRegular.nodoCierre(expresion); // (a|b)*
		expresion = ExpresionRegular.nodoConcat(expresion,
				ExpresionRegular.nodoSimbolo(3, 'a'));      // (a|b)*a
		expresion = ExpresionRegular.nodoConcat(expresion,
				ExpresionRegular.nodoSimbolo(4, 'b'));      // (a|b)*ab
		expresion = ExpresionRegular.nodoConcat(expresion,
				ExpresionRegular.nodoSimbolo(5, 'b'));      // (a|b)*abb

		automata = new Automata(expresion, 0);
	}

	@After
	public void tearDown() throws Exception {
		automata = null;
	}

	/**
	 * Comprueba las propiedades del nodo inicial y sus transiciones derivadas.
	 */
	@Test
	public void testNodoInicial() {
		Nodo nodo = automata.nodoInicial();

		assertFalse("Error identificando el nodo inicial como no final.",
				nodo.esFinal());
		assertEquals("Error identificando la posici�n del nodo inicial.", 0,
				nodo.posicion());
		assertEquals(
				"Error identificando transiciones vac�as del nodo inicial.",
				set(new Nodo(1, false), new Nodo(7, false)),
				nodo.transicionVacia());
		assertEquals("Error identificando transiciones del nodo inicial.",
				null, nodo.transicion('a'));
		assertEquals("Error identificando transiciones del nodo inicial.",
				null, nodo.transicion('b'));
	}

	/**
	 * Comprueba las propiedades del nodo final y sus transiciones derivadas.
	 */
	@Test
	public void testNodoFinal() {
		Nodo nodo = automata.nodoFinal();

		assertTrue("Error identificando el nodo final como final.",
				nodo.esFinal());
		assertEquals("Error identificando la posici�n del nodo final.", 10,
				nodo.posicion());
		assertEquals("Error identificando transiciones vac�as del nodo final.",
				set(), nodo.transicionVacia());
		assertEquals("Error identificando transiciones del nodo final.", null,
				nodo.transicion('a'));
		assertEquals("Error identificando transiciones del nodo final.", null,
				nodo.transicion('b'));
	}

	/**
	 * Comprueba el funcionamiento de las transiciones vac�as.
	 */
	@Test
	public void testTransicionVacia() {
		Set<Nodo> iniciales = automata.transicionVacia(automata.nodoInicial());
		Set<Nodo> esperados = set(new Nodo(0, false), new Nodo(1, false),
				new Nodo(2, false), new Nodo(4, false), new Nodo(7, false));

		assertEquals("Error comprobando transiciones vac�as del nodo inicial.",
				esperados, iniciales);
	}

	/**
	 * Comprueba el funcionamiento de las transiciones consumiendo entrada.
	 */
	@Test
	public void testTransicion() {
		Set<Nodo> inicialesA = automata.transicion(automata.nodoInicial(), 'a');
		Set<Nodo> esperadosA = set(new Nodo(1, false), new Nodo(2, false),
				new Nodo(3, false), new Nodo(4, false), new Nodo(6, false),
				new Nodo(7, false), new Nodo(8, false));

		assertEquals(
				"Error comprobando transiciones del nodo inicial con entrada 'a'.",
				esperadosA, inicialesA);

		Set<Nodo> inicialesB = automata.transicion(automata.nodoInicial(), 'b');
		Set<Nodo> esperadosB = set(new Nodo(1, false), new Nodo(2, false),
				new Nodo(4, false), new Nodo(5, false), new Nodo(6, false),
				new Nodo(7, false));

		assertEquals(
				"Error comprobando transiciones del nodo inicial con entrada 'b'.",
				esperadosB, inicialesB);
	}

	/**
	 * Comprueba la correcta generaci�n del programa dot que construye la imagen
	 * del aut�mata.
	 */
	@Test
	public void testAutomataDot() {
		String esperado = "digraph {\n\trankdir=LR;\n\t0 -> 1\n\t0 -> 7\n\t"
				+ "1 -> 2\n\t1 -> 4\n\t7 -> 8[label=\"a\"];\n\t"
				+ "2 -> 3[label=\"a\"];\n\t4 -> 5[label=\"b\"];\n\t"
				+ "8 -> 9[label=\"b\"];\n\t3 -> 6\n\t5 -> 6\n\t"
				+ "9 -> 10[label=\"b\"];\n\t6 -> 1\n\t6 -> 7\n}";

		assertEquals("Error generando imagen dot del aut�mata.", esperado,
				automata.imagenDot());
	}

	/**
	 * Genera un set a partir de una lista de nodos de longitud variable.
	 * 
	 * @param ns
	 *            Lista de nodos de longitud variable.
	 * @return Set de nodos.
	 */
	private static Set<Nodo> set(Nodo... ns) {
		Set<Nodo> set = new TreeSet<>();
		for (Nodo n : ns)
			set.add(n);
		return set;
	}
}
