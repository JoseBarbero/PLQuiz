package es.ubu.inf.tfg.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class DocumentoTest {

	@Rule
	public TemporaryFolder directorioTemporal = new TemporaryFolder();

	Documento documento;

	String problemaA = "((a|b*)a*c)*";
	String problemaB = "(a|b*)c*a";
	String problemaC = "(a|b)*a(a|b)(a|b)";

	AhoSethiUllman asuProblemaA;
	AhoSethiUllman asuProblemaB;
	AhoSethiUllman asuProblemaC;

	ConstruccionSubconjuntos csProblemaA;
	ConstruccionSubconjuntos csProblemaB;
	ConstruccionSubconjuntos csProblemaC;

	@Before
	public void setUp() throws Exception {
		documento = new Documento();

		asuProblemaA = new AhoSethiUllman(problemaA);
		asuProblemaB = new AhoSethiUllman(problemaB);
		asuProblemaC = new AhoSethiUllman(problemaC);

		csProblemaA = new ConstruccionSubconjuntos(problemaA);
		csProblemaB = new ConstruccionSubconjuntos(problemaB);
		csProblemaC = new ConstruccionSubconjuntos(problemaC);
	}

	@After
	public void tearDown() throws Exception {
		documento = null;

		asuProblemaA = null;
		asuProblemaB = null;
		asuProblemaC = null;
	}

	/**
	 * Comprueba que se generan correctamente documentos vac�os.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testVacio() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		esperado = toString("vacio.html");
		encontrado = documento.vistaPrevia();

		assertEquals("Vista previa de documento vac�o erronea.", esperado,
				encontrado);

		// Fichero XML
		esperado = toString("vacio.xml");
		ficheroTemporal = ficheroTemporal("vacio.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		assertEquals("Exportaci�n de documento XML vac�o erronea.", esperado,
				encontrado);

		// Fichero Latex
		esperado = toString("vacio.tex");
		ficheroTemporal = ficheroTemporal("vacio.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals("Exportaci�n de documento Latex vac�o erronea.", esperado,
				encontrado);
	}

	/**
	 * Commprueba que se a�aden problemas Aho-Sethi-Ullman subtipo completo
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testA�adirASUCompleto() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaA));
		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaB));
		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaC));

		// Vista previa
		esperado = toString("a�adirASUCompleto.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman subtipo completo a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("a�adirASUCompleto.xml");
		ficheroTemporal = ficheroTemporal("a�adir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman subtipo completo a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("a�adirASUCompleto.tex");
		ficheroTemporal = ficheroTemporal("a�adir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman subtipo completo a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Commprueba que se a�aden problemas Aho-Sethi-Ullman subtipo �rbol
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testA�adirASUArbol() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.a�adirProblema(Problema.ASUArbol(asuProblemaA));
		documento.a�adirProblema(Problema.ASUArbol(asuProblemaB));
		documento.a�adirProblema(Problema.ASUArbol(asuProblemaC));

		// Vista previa
		esperado = toString("a�adirASUArbol.html");
		encontrado = documento.vistaPrevia();
		
		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman subtipo �rbol a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("a�adirASUArbol.xml");
		ficheroTemporal = ficheroTemporal("a�adir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman subtipo �rbol a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("a�adirASUArbol.tex");
		ficheroTemporal = ficheroTemporal("a�adir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");
		
		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman subtipo �rbol a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas Aho-Sethi-Ullman subtipo completo
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarASUCompleto() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaA));
		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaB));
		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaC));
		documento.eliminarProblema(Problema.ASUCompleto(asuProblemaC));

		esperado = toString("eliminarASUCompleto.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman subtipo completo en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarASUCompleto.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman subtipo completo en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarASUCompleto.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman subtipo completo en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas Aho-Sethi-Ullman subtipo �rbol
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarASUArbol() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.ASUArbol(asuProblemaA));
		documento.a�adirProblema(Problema.ASUArbol(asuProblemaB));
		documento.a�adirProblema(Problema.ASUArbol(asuProblemaC));
		documento.eliminarProblema(Problema.ASUArbol(asuProblemaC));

		esperado = toString("eliminarASUArbol.html");
		encontrado = documento.vistaPrevia();
		
		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman subtipo �rbol en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarASUArbol.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman subtipo �rbol en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarASUArbol.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");
		
		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman subtipo �rbol en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas Aho-Sethi-Ullman subtipo completo
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirASUCompleto() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaA));
		documento.a�adirProblema(Problema.ASUCompleto(asuProblemaB));
		documento.sustituirProblema(Problema.ASUCompleto(asuProblemaB),
				Problema.ASUCompleto(asuProblemaC));

		esperado = toString("sustituirASUCompleto.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman subtipo completo en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirASUCompleto.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman subtipo completo en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirASUCompleto.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman subtipo completo en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas Aho-Sethi-Ullman subtipo �rbol
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirASUArbol() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.ASUArbol(asuProblemaA));
		documento.a�adirProblema(Problema.ASUArbol(asuProblemaB));
		documento.sustituirProblema(Problema.ASUArbol(asuProblemaB),
				Problema.ASUArbol(asuProblemaC));

		esperado = toString("sustituirASUArbol.html");
		encontrado = documento.vistaPrevia();
		
		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman subtipo �rbol en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirASUArbol.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman subtipo �rbol en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirASUArbol.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");
		
		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman subtipo �rbol en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Commprueba que se a�aden problemas de construcci�n de subconjuntos
	 * subtipo expresion correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testA�adirCSExpresion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.a�adirProblema(Problema.CSExpresion(csProblemaA));
		documento.a�adirProblema(Problema.CSExpresion(csProblemaB));
		documento.a�adirProblema(Problema.CSExpresion(csProblemaC));

		// Vista previa
		esperado = toString("a�adirCSExpresion.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos subtipo expresion a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("a�adirCSExpresion.xml");
		ficheroTemporal = ficheroTemporal("a�adir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos subtipo expresion a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("a�adirCSExpresion.tex");
		ficheroTemporal = ficheroTemporal("a�adir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos subtipo expresion a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas de construcci�n de subconjuntos
	 * subtipo expresion correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarCSExpresion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.CSExpresion(csProblemaA));
		documento.a�adirProblema(Problema.CSExpresion(csProblemaB));
		documento.a�adirProblema(Problema.CSExpresion(csProblemaC));
		documento.eliminarProblema(Problema.CSExpresion(csProblemaC));

		esperado = toString("eliminarCSExpresion.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos subtipo expresion en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarCSExpresion.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos subtipo expresion en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarCSExpresion.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos subtipo expresion en documento XML exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas de construcci�n de subconjuntos
	 * subtipo expresion correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirCSExpresion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.CSExpresion(csProblemaA));
		documento.a�adirProblema(Problema.CSExpresion(csProblemaB));
		documento.sustituirProblema(Problema.CSExpresion(csProblemaB),
				Problema.CSExpresion(csProblemaC));

		esperado = toString("sustituirCSExpresion.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos subtipo expresion en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirCSExpresion.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos subtipo expresion en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirCSExpresion.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos subtipo expresion en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Commprueba que se a�aden problemas de construcci�n de subconjuntos
	 * subtipo automata correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testA�adirCSAutomata() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.a�adirProblema(Problema.CSAutomata(csProblemaA));
		documento.a�adirProblema(Problema.CSAutomata(csProblemaB));
		documento.a�adirProblema(Problema.CSAutomata(csProblemaC));

		// Vista previa
		esperado = toString("a�adirCSAutomata.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos subtipo automata a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("a�adirCSAutomata.xml");
		ficheroTemporal = ficheroTemporal("a�adir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos subtipo automata a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("a�adirCSAutomata.tex");
		ficheroTemporal = ficheroTemporal("a�adir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");

		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos subtipo automata a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas de construcci�n de subconjuntos
	 * subtipo automata correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarCSAutomata() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.CSAutomata(csProblemaA));
		documento.a�adirProblema(Problema.CSAutomata(csProblemaB));
		documento.a�adirProblema(Problema.CSAutomata(csProblemaC));
		documento.eliminarProblema(Problema.CSAutomata(csProblemaC));

		esperado = toString("eliminarCSAutomata.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos subtipo automata en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarCSAutomata.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos subtipo automata en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarCSAutomata.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");
		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos subtipo automata en documento XML exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas de construcci�n de subconjuntos
	 * subtipo automata correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirCSAutomata() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(Problema.CSAutomata(csProblemaA));
		documento.a�adirProblema(Problema.CSAutomata(csProblemaB));
		documento.sustituirProblema(Problema.CSAutomata(csProblemaB),
				Problema.CSAutomata(csProblemaC));

		esperado = toString("sustituirCSAutomata.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos subtipo autoamta en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirCSAutomata.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos subtipo automata en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirCSAutomata.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");
		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos subtipo automata en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Genera un fichero temporal con el nombre dado. Los ficheros temporales
	 * desaparecen al finalizar los test.
	 * 
	 * @param nombre
	 *            Nombre del fichero a crear.
	 * @return Fichero temporal.
	 */
	private File ficheroTemporal(String nombre) {
		File fichero = null;
		try {
			fichero = directorioTemporal.newFile(nombre);
		} catch (IOException e) {
			fail("Error al crear el fichero temporal " + nombre);
		}
		return fichero;
	}

	/**
	 * Lee un fichero como una cadena de caracteres. Usado con ficheros
	 * temporales.
	 * 
	 * @param fichero
	 *            Fichero temporal a leer.
	 * @return Contenido del fichero.
	 */
	private String toString(File fichero) {
		String resultado;
		StringBuilder contenido;
		String linea;

		try (InputStream entrada = new FileInputStream(fichero);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) {

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n");
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			fail("Error al abrir el archivo temporal " + fichero.getName());
			return "";
		}
	}

	/**
	 * Lee un recurso como una cadena de caracteres.
	 * 
	 * @param fichero
	 *            Recurso a leer.
	 * @return Contenido del recurso.
	 */
	private String toString(String fichero) {
		String resultado;
		StringBuilder contenido;
		String linea;

		try (InputStream entrada = getClass().getResourceAsStream(fichero);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) {

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n");
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			fail("Error al abrir el archivo " + fichero);
			return "";
		}
	}
}
