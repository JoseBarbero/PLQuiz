package es.ubu.inf.tfg.doc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class ProblemaTest {
	
	/**
	 * Comprueba que los problemas de tipo Aho-Sethi-Ullman subtipo construcci�n se
	 * crean correctamente.
	 */
	@Test
	public void testASUConstruccion() {
		AhoSethiUllman asu = new AhoSethiUllman("a*");
		Problema<AhoSethiUllman> problema = Problema.asuConstruccion(asu, 1);
		
		assertEquals("Error recuperando problema original tipo AhoSethiUllman subtipo construcci�n",  asu, problema.getProblema());
		assertEquals("Error identificando problema tipo AhoSethiUllman subtipo construcci�n", "AhoSethiUllmanConstruccion", problema.getTipo());
	}
	
	/**
	 * Comprueba que los problemas de tipo Aho-Sethi-Ullman subtipo etiquetado se
	 * crean correctamente.
	 */
	@Test
	public void testASUEtiquetado() {
		AhoSethiUllman asu = new AhoSethiUllman("a*");
		Problema<AhoSethiUllman> problema = Problema.asuEtiquetado(asu, 1);
		
		assertEquals("Error recuperando problema original tipo AhoSethiUllman subtipo etiquetado",  asu, problema.getProblema());
		assertEquals("Error identificando problema tipo AhoSethiUllman subtipo etiquetado", "AhoSethiUllmanEtiquetado", problema.getTipo());
	}
	
	/**
	 * Comprueba que los problemas de tipo Aho-Sethi-Ullman subtipo tablas se
	 * crean correctamente.
	 */
	@Test
	public void testASUTablas() {
		AhoSethiUllman asu = new AhoSethiUllman("a*");
		Problema<AhoSethiUllman> problema = Problema.asuTablas(asu, 1);
		
		assertEquals("Error recuperando problema original tipo AhoSethiUllman subtipo completo",  asu, problema.getProblema());
		assertEquals("Error identificando problema tipo AhoSethiUllman subtipo completo", "AhoSethiUllmanTablas", problema.getTipo());
	}
	
	/**
	 * Comprueba que los problemas de tipo construcci�n de subconjuntos subtipo
	 * construcci�n se crean correctamente.
	 */
	@Test
	public void testCSConstrucci�n() {
		ConstruccionSubconjuntos cs = new ConstruccionSubconjuntos("a*");
		Problema<ConstruccionSubconjuntos> problema = Problema.CSConstruccion(cs, 1);
		
		assertEquals("Error recuperando problema original tipo ConstruccionSubconjuntos subtipo construcci�n",  cs, problema.getProblema());
		assertEquals("Error identificando problema tipo ConstruccionSubconjuntos subtipo construcci�n", "ConstruccionSubconjuntosConstruccion", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo construcci�n de subconjuntos subtipo
	 * expresi�n se crean correctamente.
	 */
	@Test
	public void testCSExpresion() {
		ConstruccionSubconjuntos cs = new ConstruccionSubconjuntos("a*");
		Problema<ConstruccionSubconjuntos> problema = Problema.CSExpresion(cs, 1);
		
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
		Problema<ConstruccionSubconjuntos> problema = Problema.CSAutomata(cs, 1);
		
		assertEquals("Error recuperando problema original tipo ConstruccionSubconjuntos subtipo aut�mata",  cs, problema.getProblema());
		assertEquals("Error identificando problema tipo ConstruccionSubconjuntos subtipo aut�mata", "ConstruccionSubconjuntosAutomata", problema.getTipo());
	}
}
