package es.ubu.inf.tfg.doc.datos;

import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Traductor presenta una interfaz com�n para aquellas clases que se encarguen de
 * traducir los problemas a una representaci�n textual espec�fica, con la
 * funci�n de agruparlos en un documento de tipo concreto.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public abstract class Traductor {

	/**
	 * Genera un documento de un formato concreto a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento completo.
	 */
	public abstract String documento(List<Plantilla> problemas);

	/**
	 * Genera un documento de un formato concreto a partir de un �nico problema
	 * ya traducido.
	 * 
	 * @param problema
	 *            Problema traducido.
	 * @param num
	 *            N�mero del problema.
	 * @return Documento completo.
	 */
	public abstract String traduceProblema(Plantilla problema, int num);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcci�n a un
	 * formato concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUConstruccion(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo etiquetado a un
	 * formato concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUEtiquetado(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo tablas a un formato
	 * concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUTablas(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos subtipo
	 * construcci�n a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcci�n de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSConstruccion(
			ConstruccionSubconjuntos problema);

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos subtipo
	 * expresi�n a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcci�n de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSExpresion(
			ConstruccionSubconjuntos problema);

	/**
	 * Traduce un problema de tipo construcci�n de subconjuntos subtipo aut�mata
	 * a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcci�n de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSAutomata(
			ConstruccionSubconjuntos problema);
}
