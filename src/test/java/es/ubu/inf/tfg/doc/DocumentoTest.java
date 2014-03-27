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
		
		esperado = esperado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		
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
	 * Commprueba que se a�aden problemas Aho-Sethi-Ullman correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testA�adirASU() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.a�adirProblema(asuProblemaA);
		documento.a�adirProblema(asuProblemaB);
		documento.a�adirProblema(asuProblemaC);

		// Vista previa
		esperado = toString("a�adirASU.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("a�adirASU.xml");
		ficheroTemporal = ficheroTemporal("a�adir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		esperado = esperado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		
		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("a�adirASU.tex");
		ficheroTemporal = ficheroTemporal("a�adir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas Aho-Sethi-Ullman correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarASU() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(asuProblemaA);
		documento.a�adirProblema(asuProblemaB);
		documento.a�adirProblema(asuProblemaC);
		documento.eliminarProblema(asuProblemaC);

		esperado = toString("eliminarASU.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarASU.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		esperado = esperado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		
		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarASU.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas Aho-Sethi-Ullman correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirASU() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(asuProblemaA);
		documento.a�adirProblema(asuProblemaB);
		documento.sustituirProblema(asuProblemaB, asuProblemaC);

		esperado = toString("sustituirASU.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirASU.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		esperado = esperado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		
		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirASU.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Commprueba que se a�aden problemas de construcci�n de subconjuntos
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testA�adirCS() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.a�adirProblema(csProblemaA);
		documento.a�adirProblema(csProblemaB);
		documento.a�adirProblema(csProblemaC);

		// Vista previa
		esperado = toString("a�adirCS.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("a�adirCS.xml");
		ficheroTemporal = ficheroTemporal("a�adir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		esperado = esperado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		
		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("a�adirCS.tex");
		ficheroTemporal = ficheroTemporal("a�adir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"A�adido err�neo de problemas de construcci�n de subconjuntos a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas de construcci�n de subconjuntos
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarCS() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(csProblemaA);
		documento.a�adirProblema(csProblemaB);
		documento.a�adirProblema(csProblemaC);
		documento.eliminarProblema(csProblemaC);

		esperado = toString("eliminarCS.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarCS.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		esperado = esperado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		
		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarCS.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Borrado err�neo de problemas de construcci�n de subconjuntos en documento XML exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas de construcci�n de subconjuntos
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirCS() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.a�adirProblema(csProblemaA);
		documento.a�adirProblema(csProblemaB);
		documento.sustituirProblema(csProblemaB, csProblemaC);

		esperado = toString("sustituirCS.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirCS.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		esperado = esperado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", "{1:MULTICHOICE:}");
		
		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirCS.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Sustituci�n err�nea de problemas de construcci�n de subconjuntos en documento Latex exportado.",
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
