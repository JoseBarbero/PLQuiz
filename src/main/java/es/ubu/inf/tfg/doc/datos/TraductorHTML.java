package es.ubu.inf.tfg.doc.datos;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Implementa un traductor HTML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorHTML extends Traductor {

	private static final Logger log = LoggerFactory
			.getLogger(TraductorHTML.class);

	/**
	 * Genera un documento HTML a partir de una lista de problemas ya
	 * traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento HTML completo.
	 */
	@Override
	public String documento(List<String> problemas) {
		log.info("Generando documento HTML a partir de {} problemas.",
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (String problema : problemas)
			documento.append(MessageFormat.format(formatoIntermedio(problema),
					n++));

		String plantilla = formatoIntermedio(plantilla("plantilla.html"));
		plantilla = MessageFormat.format(plantilla, documento.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman a formato HTML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduce(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresion {}",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASU.html"));

		// siguiente-pos
		stePos.append("<p><table>");
		stePos.append("<tr><th>n</th><th>stePos(n)</th></tr>");
		for (int n : problema.posiciones()) {
			stePos.append("<tr><td>");
			stePos.append(n);
			stePos.append("</td><td>");
			if (problema.siguientePos(n).size() > 0) {
				String prefijo = "";
				for (int pos : problema.siguientePos(n)) {
					stePos.append(prefijo);
					prefijo = ", ";
					stePos.append(pos);
				}
			} else {
				stePos.append("-");
			}
			stePos.append("</td></tr>");
		}
		stePos.append("</table></p>");

		// Funci�n de transici�n
		fTrans.append("<table><tr><th></th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>");
		fTrans.append("<th></th></tr>");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>");
			else
				fTrans.append("<tr><td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo)
							+ "</td>");
			}
			fTrans.append("<td>");
			for (int posicion : problema.estado(estado))
				fTrans.append(posicion + " ");
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), problema.expresionAumentada(),
				stePos.toString(), fTrans.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos a formato HTML.
	 * 
	 * @param problema
	 *            Problema de construcci�n de subconjuntos.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduce(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a HTML problema tipo construcci�n de subconjuntos con expresion {}",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCS.html"));

		// Funci�n de transici�n
		fTrans.append("<table><tr><th></th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>");
		fTrans.append("<th></th></tr>");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>");
			else
				fTrans.append("<tr><td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo)
							+ "</td>");
			}
			fTrans.append("<td>");
			for (int posicion : problema.posiciones(estado))
				fTrans.append(posicion + " ");
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), fTrans.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	@Override
	public String traduceASU(AhoSethiUllman problema) {
		return traduce(problema);
	}

	@Override
	public String traduceCS_Expresion(ConstruccionSubconjuntos problema) {
		return traduce(problema);
	}

	@Override
	public String traduceCS_Automata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a HTML problema tipo construcci�n de subconjuntos con expresion {}",
				problema.problema());

		String url = "http:\\" + problema.automata().hashCode() + ".jpg";
		StringBuilder imagen = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCS.html"));

		// Imagen
		imagen.append("<p><img src=\"" + url + "\"></p>");

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), imagen.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}
}
