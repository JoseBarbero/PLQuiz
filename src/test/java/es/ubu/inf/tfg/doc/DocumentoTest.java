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

public class DocumentoTest {

	@Rule
	public TemporaryFolder directorioTemporal = new TemporaryFolder();

	Documento documento;

	AhoSethiUllman asuProblemaA;
	AhoSethiUllman asuProblemaB;
	AhoSethiUllman asuProblemaC;

	@Before
	public void setUp() throws Exception {
		documento = new Documento();

		asuProblemaA = new AhoSethiUllman("((a|b*)a*c)*");
		asuProblemaB = new AhoSethiUllman("(a|b*)c*a");
		asuProblemaC = new AhoSethiUllman("(a|b)*a(a|b)(a|b)");
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

		// Fichero HTML
		ficheroTemporal = ficheroTemporal("vacio.html");

		documento.exportaHTML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals("Exportaci�n de documento HTML vac�o erronea.", esperado,
				encontrado);

		// Fichero XML
		esperado = toString("vacio.xml");
		ficheroTemporal = ficheroTemporal("vacio.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals("Exportaci�n de documento XML vac�o erronea.", esperado,
				encontrado);
	}

	/**
	 * Commprueba que se a�aden problemas Aho-Sethi-Ullman correctamente.
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
		esperado = toString("a�adir.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman a vista previa.",
				esperado, encontrado);

		// Fichero HTML
		ficheroTemporal = ficheroTemporal("a�adir.html");

		documento.exportaHTML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman a documento HTML exportado.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("a�adir.xml");
		ficheroTemporal = ficheroTemporal("a�adir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"A�adido err�neo de problemas Aho-Sethi-Ullman a documento XML exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas Aho-Sethi-Ullman correctamente.
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

		esperado = toString("eliminar.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman en vista previa.",
				esperado, encontrado);

		// Fichero HTML
		ficheroTemporal = ficheroTemporal("eliminar.html");

		documento.exportaHTML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman en documento HTML exportado.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminar.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Borrado err�neo de problemas Aho-Sethi-Ullman en documento XML exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas Aho-Sethi-Ullman correctamente.
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

		esperado = toString("sustituir.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman en vista previa.",
				esperado, encontrado);

		// Fichero HTML
		ficheroTemporal = ficheroTemporal("sustituir.html");

		documento.exportaHTML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman en documento HTML exportado.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituir.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals(
				"Sustituci�n err�nea de problemas Aho-Sethi-Ullman en documento XML exportado.",
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
		InputStream entrada;
		BufferedReader lector = null;
		StringBuilder contenido;
		String linea;

		try {
			entrada = new FileInputStream(fichero);
			lector = new BufferedReader(new InputStreamReader(entrada, "UTF16"));

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			fail("Error al abrir el archivo temporal " + fichero.getName());
		} finally {
			try {
				if (lector != null)
					lector.close();
			} catch (IOException e) {
				fail("Error al cerrar el archivo temporal " + fichero.getName());
			}
		}

		return "";
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
		InputStream entrada;
		BufferedReader lector;
		StringBuilder contenido;
		String linea;

		entrada = getClass().getResourceAsStream(fichero);
		try {
			lector = new BufferedReader(new InputStreamReader(entrada, "UTF16"));

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			fail("Error al abrir el archivo " + fichero);
		}

		return "";
	}
}
