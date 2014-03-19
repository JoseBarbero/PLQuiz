package es.ubu.inf.tfg.doc.datos;

import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.Thompson;

/**
 * Traductor presenta una interfaz com�n para aquellas clases que se encargen de
 * traducir los problemas a una representaci�n textual especifica, con la
 * funci�n de agruparlos en un documento de tipo concreto.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public interface Traductor {
	/**
	 * Genera un documento de un formato concreto a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento completo.
	 */
	public String documento(List<String> problemas);

	/**
	 * Traduce un problema de tipo AhoSethiUllman a un formato concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public String traduce(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos a un formato
	 * concreto.
	 * 
	 * @param problema
	 *            Problema construcci�n de subconjuntos.
	 * @return Problema traducido.
	 */
	public String traduce(Thompson problema);
}