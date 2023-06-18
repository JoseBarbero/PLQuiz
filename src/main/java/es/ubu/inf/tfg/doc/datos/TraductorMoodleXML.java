package es.ubu.inf.tfg.doc.datos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
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
	public String documento(List<Plantilla> problemas) {
		log.info("Generando documento Moodle XML a partir de {} problemas.",
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (Plantilla problema : problemas) {
			problema.set("numero", "" + n++);
			documento.append(problema.toString());
		}

		Plantilla plantilla = new Plantilla("plantilla.xml");
		plantilla.set("documento", documento.toString());

		return plantilla.toString();
	}
	
	/**
	 * Genera un documento Moodle XML a partir de un �nico problema ya traducido.
	 * 
	 * @param problema
	 *            Problema traducido.
	 * @return Documento Moodle XML completo.
	 */
	@Override
	public String traduceProblema(Plantilla problema, int num){
		log.info("Generando documento Moodle XML de un �nico problema.");
		
		problema.set("numero", "" + num);
		
		Plantilla plantilla = new Plantilla("plantilla.xml");
		plantilla.set("documento", problema.toString());
	
		return plantilla.toString();
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcci�n a formato
	 * Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public Plantilla traduceASUConstruccion(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresi�n {}, formato construcci�n",
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaASUConstruccion.xml");
		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		Collections.shuffle(alternativas);
		String[] alternativasBase64 = new String[4];

		for (int i = 0; i < 4; i++) {
			imagenes[i] = Math.abs(alternativas.get(i).hashCode()) + ".jpg";
			alternativasBase64[i] = imageToBase64(alternativas.get(i));
		}

		char solucion = (char) ('a' + alternativas.indexOf(problema
				.alternativas().get(0)));
		Set<Character> opciones = new HashSet<>();
		opciones.add('a');
		opciones.add('b');
		opciones.add('c');
		opciones.add('d');

		plantilla.set("expresion", problema.problema());
		plantilla.set("urlA", imagenes[0]);
		plantilla.set("urlB", imagenes[1]);
		plantilla.set("urlC", imagenes[2]);
		plantilla.set("urlD", imagenes[3]);
		plantilla.set("imagenA", alternativasBase64[0]);
		plantilla.set("imagenB", alternativasBase64[1]);
		plantilla.set("imagenC", alternativasBase64[2]);
		plantilla.set("imagenD", alternativasBase64[3]);
		plantilla.set("solucion", opcionesTransicion(solucion, opciones));

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo etiquetado a formato
	 * Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */

	@Override
	public Plantilla traduceASUEtiquetado(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresi�n {}, formato etiquetado",
				problema.problema());

		String url = Math.abs(problema.arbolVacio().hashCode()) + ".jpg";
		Plantilla plantilla = new Plantilla("plantillaASUEtiquetado.xml");
		StringBuilder soluciones = new StringBuilder();

		// cabecera
		soluciones.append("");
		// contenido
		char simboloActual = 'A';
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append("<tr><td>" + simboloActual + "</td>");
			soluciones.append("<td>\n"
					+ opcionesAnulables(problema.esAnulable(simboloActual))
					+ "\n</td>");
			soluciones.append("<td>\n"
					+ opcionesPosiciones(problema.primeraPos(simboloActual),
							problema.posiciones()) + "\n</td>");
			soluciones.append("<td>\n"
					+ opcionesPosiciones(problema.ultimaPos(simboloActual),
							problema.posiciones()) + "\n</td>");
			soluciones.append("</tr>");

			simboloActual++;
		}

		String solucionesXML = soluciones.toString();
		solucionesXML = solucionesXML.replace("\u2027", "·");
		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "·");

		plantilla.set("expresion", expresion);
		plantilla.set("url", url);
		plantilla.set("imagen", imageToBase64(problema.arbolVacio()));
		plantilla.set("tabla", solucionesXML);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo tablas a formato
	 * Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */

	@Override
	public Plantilla traduceASUTablas(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresi�n {}, formato tablas",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaASUTablas.xml");

		// siguiente-pos
		for (int n : problema.posiciones()) {
			stePos.append("<tr><td>");
			stePos.append(n);
			stePos.append("</td><td>");
			stePos.append(opcionesPosiciones(problema.siguientePos(n),
					problema.posiciones()));
			stePos.append("</td></tr>");
		}

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

		plantilla.set("expresion", problema.problema());
		plantilla.set("siguientePos", stePos.toString());
		plantilla.set("transicion", fTrans.toString());
		plantilla.set("finales", eFinales.toString());

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos subtipo
	 * construcci�n a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema de construcci�n de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public Plantilla traduceCSConstruccion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcci�n de subconjuntos con expresi�n {}, formato construcci�n",
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaCSConstruccion.xml");

		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		Collections.shuffle(alternativas);
		String[] alternativasBase64 = new String[4];

		for (int i = 0; i < 4; i++) {
			imagenes[i] = Math.abs(alternativas.get(i).hashCode()) + ".jpg";
			alternativasBase64[i] = imageToBase64(alternativas.get(i));
		}

		char solucion = (char) ('a' + alternativas.indexOf(problema
				.alternativas().get(0)));
		Set<Character> opciones = new HashSet<>();
		opciones.add('a');
		opciones.add('b');
		opciones.add('c');
		opciones.add('d');

		plantilla.set("expresion", problema.problema());
		plantilla.set("urlA", imagenes[0]);
		plantilla.set("urlB", imagenes[1]);
		plantilla.set("urlC", imagenes[2]);
		plantilla.set("urlD", imagenes[3]);
		plantilla.set("imagenA", alternativasBase64[0]);
		plantilla.set("imagenB", alternativasBase64[1]);
		plantilla.set("imagenC", alternativasBase64[2]);
		plantilla.set("imagenD", alternativasBase64[3]);
		plantilla.set("solucion", opcionesTransicion(solucion, opciones));

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos subtipo
	 * expresi�n a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema de construcci�n de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public Plantilla traduceCSExpresion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcci�n de subconjuntos con expresi�n {}, formato expresi�n",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSExpresion.xml");

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

		plantilla.set("expresion", problema.problema());
		plantilla.set("transicion", fTrans.toString());
		plantilla.set("finales", eFinales.toString());

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos subtipo aut�mata
	 * a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema de construcci�n de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public Plantilla traduceCSAutomata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcci�n de subconjuntos con expresi�n {}, formato aut�mata",
				problema.problema());

		String url = Math.abs(problema.automata().hashCode()) + ".jpg";
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSAutomata.xml");

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

		plantilla.set("url", url);
		plantilla.set("imagen", imageToBase64(problema.automata()));
		plantilla.set("transicion", fTrans.toString());
		plantilla.set("finales", eFinales.toString());

		return plantilla;
	}

	/**
	 * Devuelve la lista de opciones posibles a la hora de resolver el estado de
	 * destino en una tabla de transici�n, a partir de la soluci�n y de la lista
	 * de estados existentes.
	 * <p>
	 * La lista de opciones incluirá la soluci�n correcta, dos soluciones
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
		if (similares.size() > 0) { // Opci�n similar 1
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("A�adiendo opci�n {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}
		if (similares.size() > 0) { // Opci�n similar 2
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("A�adiendo opci�n {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}
		if (diferentes.size() > 0) { // Opci�n diferente a ser posible
			index = random.nextInt(diferentes.size());
			opciones.append("~");
			log.debug("A�adiendo opci�n {} (diferente)", diferentes.get(index));
			opciones.append(diferentes.remove(index));
		} else {
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("A�adiendo opci�n {} (similar)", similares.get(index));
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
	 * La lista de opciones incluirá la soluci�n correcta, dos soluciones
	 * similares y una soluci�n completamente distinta. Las soluciones similares
	 * se obtienen añadiendo y quitando un elemento del conjunto
	 * respectivamente. La soluci�n distinta es el conjunto complementario al
	 * dado.
	 * 
	 * @param solucion
	 *            Conjunto de estados finales del problema.
	 * @param estados
	 *            Conjunto de estados del problema.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesFinales(Set<Character> solucion,
			Set<Character> estados) {
		log.debug(
				"Generando opciones para estados finales con finales {} y estados {}",
				solucion, estados);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(listToString(new ArrayList<>(solucion)));

		List<Character> complementarios = new ArrayList<>(estados);
		complementarios.removeAll(solucion);
		List<Character> conjunto;
		int index;
		// Opci�n similar 1 (eliminamos un estado)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() > 0) {
			index = random.nextInt(conjunto.size());
			conjunto.remove(index);
			log.debug("Añadiendo opci�n {} (similar)", conjunto);
			opciones.append("~" + listToString(conjunto)); 
		}

		// Opci�n similar 2 (añadimos un estado)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() < solucion.size()) {
			index = random.nextInt(complementarios.size());
			conjunto.add(complementarios.get(index));
			log.debug("Añadiendo opci�n {} (similar)", conjunto);
			opciones.append("~" + listToString(conjunto)); 
		}

		// Opci�n diferente
		log.debug("Añadiendo opci�n {} (diferente)", complementarios);
		opciones.append("~" + listToString(complementarios)); 

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * Genera una lista de opciones para resolver el conjunto de posiciones, a
	 * partir del conjunto de posiciones real y del conjunto total de posiciones
	 * del problema.
	 * <p>
	 * La lista de opciones incluirá la soluci�n correcta, dos soluciones
	 * similares y una soluci�n completamente distinta. Las soluciones similares
	 * se obtienen añadiendo y quitando un elemento del conjunto
	 * respectivamente. La soluci�n distinta es el conjunto complementario al
	 * dado.
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
		opciones.append(listToRanges(new ArrayList<>(solucion)));

		List<Integer> complementarios = new ArrayList<>(posiciones);
		complementarios.removeAll(solucion);
		List<Integer> conjunto;
		int index;
		// Opci�n similar 1 (eliminamos un estado si es posible)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() > 0) {
			index = random.nextInt(conjunto.size());
			conjunto.remove(index);
			log.debug("Añadiendo opci�n {} (similar)", conjunto);
			opciones.append("~" + listToRanges(conjunto));
		}

		// Opci�n similar 2 (añadimos un estado si es posible)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() < solucion.size()) {
			index = random.nextInt(complementarios.size());
			conjunto.add(complementarios.get(index));
			log.debug("Añadiendo opci�n {} (similar)", conjunto);
			opciones.append("~" + listToRanges(conjunto));
		}

		// Opci�n diferente
		log.debug("Añadiendo opci�n {} (diferente)", complementarios);
		opciones.append("~" + listToRanges(complementarios));

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * Genera opciones para decidir si un nodo es anulable o no (Si/No),
	 * recibiendo la opci�n correcta como parámetro booleano (<code>true</code>
	 * para sí, <code>false</code> para no).
	 * 
	 * @param esAnulable
	 *            Opci�n correcta.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesAnulables(boolean esAnulable) {
		log.debug("Generando opciones para anulable {}", esAnulable);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append((esAnulable ? "Si" : "No"));
		opciones.append("~");
		opciones.append((esAnulable ? "No" : "Si"));
		opciones.append("}");

		return opciones.toString();
	}

	/**
	 * Devuelve una representaci�n de una lista de elementos separados con
	 * comas.
	 * 
	 * @param lista
	 *            Lista de elementos.
	 * @return Representaci�n de la lista como elementos separados por comas.
	 */
	@SuppressWarnings("unused")
	private String listToString(List<?> lista) {
		StringBuilder setToString = new StringBuilder();

		if (lista.size() > 0) {
			String prefijo = "";
			for (Object elemento : lista) {
				setToString.append(prefijo);
				prefijo = ", ";
				setToString.append(elemento.toString());
			}
		} else {
			setToString.append("Cjto. vacío");
		}
		return setToString.toString();
	}

	/**
	 * Devuelve una representaci�n de un conjunto de elementos como rangos separados por comas.
	 * 
	 * @param lista
	 *            Conjunto de elementos.
	 * @return Representaci�n del conjunto como rangos separados por comas.
	 */
	// TODO: �se podr�a mover a la clase padre?
	// TODO: �setToRanges y listToRanges se podr�an fusionar?
	private String listToRanges(List<?> lista) {
		StringBuilder out = new StringBuilder();
		int last=0, first=0, length=0; // inicializo para evitar warnings
		
		log.info("***** lista {}.", lista);

		
		if (lista.size() == 0) {
			out.append("\u2205"); // Unicode empty set
		} else {
			String sep = "";
			boolean isFirst = true;
			for (Object e: lista) {
				log.info("***** lista {}.", e);
				if (isFirst) {
					last = first = (int) e;
					length = 1;
					isFirst = false;
					continue;
				}
				if ((int) e - last == 1) {
					last = (int) e;
					length++;
				} else {
					out.append(sep);
					sep = ", ";
					if (length == 1) {
						out.append(""+last);
					} else if (length == 2) {
						out.append(""+first+", "+last);
					} else {
						out.append(""+first+"-"+last);
					}
					last = first = (int) e;
					length = 1;
				}
			}
			out.append(sep);
			if (length == 1) {
				out.append(""+last);
			} else if (length == 2) {
				out.append(""+first+", "+last);
			} else {
				out.append(""+first+"-"+last);
			}
		}
		return out.toString();
	}

	/**
	 * Convierte una imagen en una cadena representando a la misma en base 64.
	 * 
	 * @param imagen
	 *            Imagen a convertir.
	 * @return Cadena en base 64 representando la imagen dada.
	 */
	private String imageToBase64(BufferedImage imagen) {
		String imagenBase64 = "";

		try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			ImageIO.write(imagen, "JPG", output);
			byte[] imageBytes = output.toByteArray();

			imagenBase64 = Base64.getEncoder().encodeToString(imageBytes);

		} catch (IOException e) {
			log.error("Error convirtiendo imagen a base 64", e);
		}

		return imagenBase64;
	}
}
