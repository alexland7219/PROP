package classes;

import java.time.LocalDate;
import java.util.HashMap;

/** Document.
 * @author Alexandre Ros i Roger (alexandre.ros.roger@estudiantat.upc.edu)
 */
public class Document {
	
	private Frase author;
	private Frase title;
	private boolean isFav;
	private LocalDate date;
	private Contingut cont;
	private CtrlDomini CD;
	
	/**
	 * Constructora d'un Document.
	 * 
	 * @param author Frase que representa l'autor del document.
	 * @param title Frase que representa el títol del document.
	 * @param isFav Booleà que ens indica si inicialment el document està marcat com a preferit o no.
	 * @param path Path que té el document en el Sistema de Fitxers.
	 * @param date Data de creació del document.
	 * @param cont Contingut del document.
	 */
	public Document(Frase author, Frase title, boolean isFav, LocalDate date, Contingut cont, CtrlDomini CD){
		this.author = author;
		this.title = title;
		this.isFav = isFav;
		this.date = date;
		this.cont = cont;
		this.CD = CD;
	}
	
	/** Mètode setter per a l'atribut <i>isFav</i>
	 * 
	 * @param val Booleà setter.
	 */
	public void setFavourite(boolean val){
		isFav = val;
	}
	
	/** Getter de la Frase Autor del document.
	 * 
	 * @return Frase de l'Autor.
	 */
	public Frase getAutor(){
		return author;
	}

	/** Setter de la Frase Autor del document.
	 * 
	 * @param newAutor Frase que representa el nou autor del document.
	 */
	public void setAutor(Frase newAutor){
		author = newAutor;
	}

	/** Setter de la Frase Titol del document.
	 * 
	 * @param newTitol Frase que representa el nou títol del document.
	 */
	public void setTitol(Frase newTitol){
		title = newTitol;
	}

	/** Getter del Contingut del document.
	 * 
	 * @return Contingut del document.
	 */
	public Contingut getContingut(){
		if (cont == null) return CD.generateContent(CD.getContingut(title.toString(), author.toString()));
		else return cont;
	}

	/** Funció que elimina el contingut del document
	 * 
	 * @return void
	 */
	public void oblidaContingut(){
		cont = null;
	}
	
	/** Getter de la Frase Títol del document.
	 * 
	 * @return Frase del Títol.
	 */
	public Frase getTitol(){
		return title;
	}
	
	/** Getter de la data de creació del document.
	 * 
	 * @return Data (LocalDate) de creació del document.
	 */
	public LocalDate getData(){
		return date; // LocalDate is immutable type
	}

	/** Setter de la data de creació del document.
	 * 
	 * @param d LocalDate de la nova data de creació.
	 */
	public void setData(LocalDate d){
		date = d;
	}
	
	/** Getter del booleà <i>isFav</i>
	 * 
	 * @return Booleà que ens indica si el document està marcat com a preferit.
	 */
	public boolean getFavourite(){
		return isFav;
	}

	/** Funció que retorna la Term Frequency (TF) d'una paraula al contingut del document.
	 * 
	 * @param index Índex de la paraula de la qual volem obtenir el TF.
	 * @return Retorna un double, el TF de la paraula 'índex'. Si la paraula no està al contingut retorna -1.
	 */
	public double getTFofWord(int index)
	{
		if (cont == null) return getContingut().getTFofWord(index);
		return cont.getTFofWord(index);
	}

	/** Getter de les Term Frequencies de totes les paraules del contingut del document.
	 * 
	 * @return Reotnra un HashMap que mapeja cada índex de paraula amb el seu TF (double) al contingut.
	 */
	public HashMap<Integer, Double> getTF(){
		if (cont == null) return getContingut().getTF();
		return cont.getTF();
	}
	
	/** Mètode que ens indica si la seqüència 'seq' existeix al títol, autor o contingut del nostre document.
	 * 
	 * @param seq La seqüència que volem cercar.
	 * @return Booleà que indica si la seqüència 'seq' es troba al document (o al títol, o a l'autor, o al contingut).
	 */
	public boolean conteSequencia(String seq){
		if (cont != null) return (author.conteCaracters(seq) || title.conteCaracters(seq) || cont.conteSequencia(seq));
		return (author.conteCaracters(seq) || title.conteCaracters(seq) || getContingut().conteSequencia(seq));
	}



	public String toString(){
		return title + " ~ " + author;
	}

}
