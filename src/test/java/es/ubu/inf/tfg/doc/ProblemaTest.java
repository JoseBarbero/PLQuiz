package es.ubu.inf.tfg.doc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class ProblemaTest {
	/**
	 * Comprueba que los problemas de tipo Aho-Sethi-Ullman subtipo completo se
	 * crean correctamente.
	 */
	@Test
	public void testASUCompleto() {
		AhoSethiUllman asu = new AhoSethiUllman("a*");
		Problema<AhoSethiUllman> problema = Problema.ASUCompleto(asu);
		
		assertEquals("Error recuperando problema original tipo AhoSethiUllman subtipo completo",  asu, problema.getProblema());
		assertEquals("Error identificando problema tipo AhoSethiUllman subtipo completo", "AhoSethiUllmanCompleto", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo Aho-Sethi-Ullman subtipo �rbol se
	 * crean correctamente.
	 */
	@Test
	public void testASUArbol() {
		AhoSethiUllman asu = new AhoSethiUllman("a*");
		Problema<AhoSethiUllman> problema = Problema.ASUArbol(asu);
		
		assertEquals("Error recuperando problema original tipo AhoSethiUllman subtipo �rbol",  asu, problema.getProblema());
		assertEquals("Error identificando problema tipo AhoSethiUllman subtipo �rbol", "AhoSethiUllmanArbol", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo construcci�n de subconjuntos subtipo
	 * expresi�n se crean correctamente.
	 */
	@Test
	public void testCSExpresion() {
		ConstruccionSubconjuntos cs = new ConstruccionSubconjuntos("a*");
		Problema<ConstruccionSubconjuntos> problema = Problema.CSExpresion(cs);
		
		assertEquals("Error recuperando problema original tipo ConstruccionSubconjuntos subtipo expresi�n",  cs, problema.getProblema());
		assertEquals("Error identificando problema tipo ConstruccionSubconjuntos subtipo expresi�n", "ConstruccionSubconjuntosExpresion", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo construcci�n de subconjuntos subtipo
	 * aut�mata se crean correctamente.
	 */
	@Test
	public void testCSAutomata() {
		ConstruccionSubconjuntos cs = new ConstruccionSubconjuntos("a*");
		Problema<ConstruccionSubconjuntos> problema = Problema.CSAutomata(cs);
		
		assertEquals("Error recuperando problema original tipo ConstruccionSubconjuntos subtipo aut�mata",  cs, problema.getProblema());
		assertEquals("Error identificando problema tipo ConstruccionSubconjuntos subtipo aut�mata", "ConstruccionSubconjuntosAutomata", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo thompson se crean correctamente.
	 */
	@Test
	public void testThompson() {
		// TODO
	}

}
