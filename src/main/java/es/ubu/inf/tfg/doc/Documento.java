package es.ubu.inf.tfg.doc;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

public interface Documento {
	public void a�adirProblema(AhoSethiUllman problema);
	public void eliminarProblema(AhoSethiUllman problema);
	public String toString();
}
