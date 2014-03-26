package es.ubu.inf.tfg.doc.datos;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Implementa un traductor al formato propietario Moodle XML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorMoodleXML extends Traductor {

	private static final Logger log = LoggerFactory
			.getLogger(TraductorMoodleXML.class);
	private static final Random random = new Random(new Date().getTime());

	/**
	 * Genera un documento en formato Moodle XML a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento Moodle XML completo.
	 */
	@Override
	public String documento(List<String> problemas) {
		log.info(
				"Generando documento Moodle XML a partir de una lista de {} problemas",
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (String problema : problemas)
			documento.append(MessageFormat.format(formatoIntermedio(problema),
					n++));

		String plantilla = formatoIntermedio(plantilla("plantilla.xml"));
		plantilla = MessageFormat.format(plantilla, documento.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public String traduce(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML un problema de tipo Aho-Sethi-Ullman con expresi�n {}",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASU.xml"));

		// Funci�n de transici�n
		fTrans.append("\n\t<tr><th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>");
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>");
				}
			}
			fTrans.append("\n\t<td>");
			fTrans.append(opcionesPosiciones(problema.estado(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>");
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), fTrans.toString(), eFinales.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos a formato Moodle
	 * XML.
	 * 
	 * @param problema
	 *            Problema de construcci�n de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public String traduce(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML un problema de tipo construcci�n de subconjuntos con expresi�n {}",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCS.xml"));

		// Funci�n de transici�n
		fTrans.append("\n\t<tr>\n\t<th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>");
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>");
				}
			}
			fTrans.append("\n\t<td>");
			fTrans.append(opcionesPosiciones(problema.posiciones(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>");
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), fTrans.toString(), eFinales.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Devuelve la lista de opciones posibles a la hora de resolver el estado de
	 * destino en una tabla de transici�n, a partir de la soluci�n y de la lista
	 * de estados existentes.
	 * <p>
	 * La lista de opciones incluir� la soluci�n correcta, dos soluciones
	 * similares y una soluci�n completamente distinta.
	 * 
	 * @param solucion
	 *            Estado de destino correcto.
	 * @param estados
	 *            Estados existentes.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesTransicion(char solucion, Set<Character> estados) {
		log.debug(
				"Generando opciones para transici�n a estado {} con estados {}",
				solucion, estados);

		estados.remove(solucion);

		List<Character> similares = new ArrayList<>();
		for (char estado : estados) {
			if (Math.abs(estado - solucion) <= 2)
				similares.add(estado);
		}
		List<Character> diferentes = new ArrayList<>(estados);
		diferentes.removeAll(similares);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(solucion);

		int index;
		if (similares.size() > 0) { // Opcion similar 1
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("A�adiendo opcion {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}
		if (similares.size() > 0) { // Opcion similar 2
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("A�adiendo opcion {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}
		if (diferentes.size() > 0) { // Opcion diferente a ser posible
			index = random.nextInt(diferentes.size());
			opciones.append("~");
			log.debug("A�adiendo opcion {} (diferente)", diferentes.get(index));
			opciones.append(diferentes.remove(index));
		} else {
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("A�adiendo opcion {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * Genera una lista de opciones para resolver el conjunto de estados
	 * finales, a partir del conjunto de estados finales real y del conjunto
	 * total de estados del problema.
	 * <p>
	 * Las opciones alternativas se obtienen modificando la soluci�n original,
	 * ya sea a�adiendo elementos, elimin�ndolos, o intercambiandolos por otros
	 * nuevos.
	 * 
	 * @param solucion
	 *            Conjunto de estados finales del problema.
	 * @param estados
	 *            Conjunto de estados del problema.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesFinales(Set<Character> solucion,
			Set<Character> estados) { // TODO no da una opci�n diferente y 2
										// similares
		log.debug(
				"Generando opciones para estados finales con finales {} y estados {}",
				solucion, estados);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(setToString(solucion));

		List<Character> finales = new ArrayList<>(solucion);
		List<Character> noFinales = new ArrayList<>(estados);
		noFinales.removeAll(finales);

		List<Set<Character>> conjuntos = new ArrayList<>();
		Set<Character> conjunto;
		// A�adir
		for (char estado : noFinales) {
			conjunto = new TreeSet<>(solucion);
			conjunto.add(estado);
			conjuntos.add(conjunto);
		}
		// Eliminar
		for (char estado : finales) {
			conjunto = new TreeSet<>(solucion);
			conjunto.remove(estado);
			conjuntos.add(conjunto);
		}
		// Permutar
		for (char viejo : finales) {
			for (char nuevo : noFinales) {
				conjunto = new TreeSet<>(solucion);
				conjunto.remove(viejo);
				conjunto.add(nuevo);
				conjuntos.add(conjunto);
			}
		} // TODO muchos m�s permutados que de otros tipos.

		int index;
		for (int i = 0; i < 3; i++) {
			index = random.nextInt(conjuntos.size());
			opciones.append("~");
			log.debug("A�adiendo opcion {}", conjuntos.get(index));
			opciones.append(setToString(conjuntos.remove(index)));
		}

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * Genera una lista de opciones para resolver el conjunto de posiciones, a
	 * partir del conjunto de posiciones real y del conjunto total de posiciones
	 * del problema.
	 * <p>
	 * Las opciones alternativas se obtienen modificando la soluci�n original,
	 * ya sea a�adiendo elementos, elimin�ndolos, o intercambiandolos por otros
	 * nuevos.
	 * 
	 * @param solucion
	 *            Conjunto de posiciones real.
	 * @param posiciones
	 *            Conjunto de posiciones del problema.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesPosiciones(Set<Integer> solucion,
			Set<Integer> posiciones) {
		log.debug(
				"Generando opciones para posiciones {} con posiciones totales {}",
				solucion, posiciones);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(setToString(solucion));

		List<Integer> incluidos = new ArrayList<>(solucion);
		List<Integer> noIncluidos = new ArrayList<>(posiciones);
		noIncluidos.removeAll(incluidos);

		List<Set<Integer>> conjuntos = new ArrayList<>();
		Set<Integer> conjunto;
		// A�adir
		for (int estado : noIncluidos) {
			conjunto = new TreeSet<>(solucion);
			conjunto.add(estado);
			conjuntos.add(conjunto);
		}
		// Eliminar
		for (int estado : incluidos) {
			conjunto = new TreeSet<>(solucion);
			conjunto.remove(estado);
			conjuntos.add(conjunto);
		}
		// Permutar
		for (int viejo : incluidos) {
			for (int nuevo : noIncluidos) {
				conjunto = new TreeSet<>(solucion);
				conjunto.remove(viejo);
				conjunto.add(nuevo);
				conjuntos.add(conjunto);
			}
		} // TODO muchos m�s permutados que de otros tipos.

		int index;
		for (int i = 0; i < 3; i++) {
			index = random.nextInt(conjuntos.size());
			opciones.append("~");
			log.debug("A�adiendo opcion {}", conjuntos.get(index));
			opciones.append(setToString(conjuntos.remove(index)));
		}

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * Devuelve una representaci�n de un conjunto de elementos separados con
	 * comas.
	 * 
	 * @param set
	 *            Conjunto de elementos.
	 * @return Representaci�n del conjunto como elementos separados por comas.
	 */
	private String setToString(Set<?> set) {
		StringBuilder setToString = new StringBuilder();

		if (set.size() > 0) {
			String prefijo = "";
			for (Object elemento : set) {
				setToString.append(prefijo);
				prefijo = ", ";
				setToString.append(elemento.toString());
			}
		} else {
			setToString.append("Cjto. vac�o");
		}
		return setToString.toString();
	}
}
