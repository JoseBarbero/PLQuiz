package es.ubu.inf.tfg.regex.thompson;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstruccionSubconjuntosGeneradorTest {
	
	private static final Logger log = LoggerFactory
			.getLogger(ConstruccionSubconjuntosGeneradorTest.class);

	private final int MIN_CORRECTOS = 99; // M�nimo porcentaje de correctos
	private final int N_ITERACIONES = 10; // Total de problemas generados por
											// test

	private static final Random random = new Random(new Date().getTime());
	ConstruccionSubconjuntosGenerador generador;

	@Before
	public void setUp() throws Exception {
		generador = new ConstruccionSubconjuntosGenerador();
	}

	@After
	public void tearDown() throws Exception {
		generador = null;
	}
	
	@Ignore
	@Test
	public void testTime() {
		int estados, simbolos;
		long tiempo;
		ConstruccionSubconjuntos problema;

		log.warn("No vac�o");
		for (simbolos = 2; simbolos <= 6; simbolos++) {
			for (estados = 3; estados <= 15; estados++) {
				tiempo = System.nanoTime();
				problema = generador.nuevo(simbolos, estados, false);
				tiempo = System.nanoTime() - tiempo;
				log.warn("{} {} {}", simbolos, estados, tiempo);
				
				assertTrue("Fallo en e" + estados + "s" + simbolos, (problema.simbolos().size() == simbolos + 1) && (problema.estados().size() == estados));
			}
		}

		log.warn("vac�o");
		for (simbolos = 2; simbolos <= 6; simbolos++) {
			for (estados = 3; estados <= 15; estados++) {
				tiempo = System.nanoTime();
				problema = generador.nuevo(simbolos, estados, true);
				tiempo = System.nanoTime() - tiempo;
				log.warn("{} {} {}", simbolos, estados, tiempo);
				
				assertTrue("Fallo en e" + estados + "s" + simbolos, (problema.simbolos().size() == simbolos + 1) && (problema.estados().size() == estados));
			}
		}
	}

	/**
	 * Comprueba que la clase genera un problema sin incluir nodos vac�os y con
	 * los par�metros pedidos. Debe generar el problema pedido en al menos un
	 * 99% de los casos.
	 */
//	@Ignore
	@Test
	public void testNuevoNoVacio() {
		ConstruccionSubconjuntos problema;
		int correctos = 0;
		int estados, simbolos;
		boolean estadosCorrectos;
		boolean simbolosCorrectos;

		for (int i = 0; i < N_ITERACIONES; i++) {
			estados = random.nextInt(19) + 3;
			simbolos = random.nextInt(10) + 1;

			problema = generador.nuevo(simbolos, estados, false);

			simbolosCorrectos = problema.simbolos().size() == simbolos + 1;
			estadosCorrectos = problema.estados().size() == estados;

			if (estadosCorrectos && simbolosCorrectos)
				correctos++;
		}

		assertTrue("Probabilidad insuficiente de generar problemas v�lidos: "
				+ (correctos * 100 / N_ITERACIONES) + "%",
				(correctos * 100 / N_ITERACIONES) >= MIN_CORRECTOS);
	}

	/**
	 * Comprueba que la clase genera un problema incluyendo nodos vac�os y con
	 * los par�metros pedidos o similares. Debe generar el problema pedido en al
	 * menos un 99% de los casos.
	 */
	@Ignore
	@Test
	public void testNuevoVacio() {
		ConstruccionSubconjuntos problema;
		int correctos = 0;
		int estados, simbolos;
		boolean estadosCorrectos;
		boolean simbolosCorrectos;

		for (int i = 0; i < N_ITERACIONES; i++) {
			estados = random.nextInt(19) + 3;
			simbolos = random.nextInt(10) + 1;

			problema = generador.nuevo(simbolos, estados, true);

			simbolosCorrectos = problema.simbolos().size() == simbolos + 1;
			estadosCorrectos = problema.estados().size() == estados;

			if (estadosCorrectos && simbolosCorrectos)
				correctos++;
		}

		assertTrue("Probabilidad insuficiente de generar problemas v�lidos: "
				+ (correctos * 100 / N_ITERACIONES) + "%",
				(correctos * 100 / N_ITERACIONES) >= MIN_CORRECTOS);
	}
}
