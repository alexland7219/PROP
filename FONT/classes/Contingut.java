package classes;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

/** Contingut d'un document.
 * @author Alexandre Ros i Roger (alexandre.ros.roger@estudiantat.upc.edu)
 */
public class Contingut {
	
	private String plaintext;
	private Frase[] phrases;
	private HashMap<Integer, Integer> words;
	private int n_paraules;


	/** Constructora per a la classe Contingut, que representa un contingut d'un document.
	 * 
	 * @param plaintext El text de tot el contingut com a String, tal i com va ser escrita per l'usuari.
	 * @param phrases L'array de frases que componen el contingut.
	 */
	public Contingut(String plaintext, Frase[] phrases){
		this.plaintext = plaintext;

		n_paraules = 0;

		this.phrases = phrases.clone();

		words = new HashMap<Integer, Integer>();

		for (int p = 0; p < phrases.length; ++p){
			HashMap<Integer, Integer> arrWords = phrases[p].donaWords();
			
			for (int index : arrWords.keySet()){

				int ocurr = arrWords.get(index);

				if (words.containsKey(index)){
					// If the word has already been inserted
					words.put(index, words.get(index) + ocurr);
				} else {
					words.put(index, ocurr);
				}

				n_paraules += ocurr;
			}
		}
	}

	/** Funció que retorna la Term Frequency (TF) d'una paraula al contingut
	 * 
	 * @param index Índex de la paraula de la qual volem obtenir el TF.
	 * @return Retorna un double, el TF de la paraula 'índex'. Si la paraula no està al contingut retorna -1.
	 */
	public double getTFofWord(int index){
		if (!words.containsKey(index)) return -1.0;

		return (double) words.get(index) / n_paraules;
	}

	/** Getter de l'array de frases
	 * 
	 * @return Retorna l'array de frases que compon el contingut
	 */
	public Frase[] getFrases(){
		return phrases;
	}
	
	/** Getter de les ocurrències de cada paraula del contingut.
	 * 
	 * @return Retorna un HashMap que mapeja cada índex de paraula amb el seu nombre d'ocurrències totals al contingut.
	 */
	public HashMap<Integer, Integer> getWords(){
		return words;
	}

	/** Getter de les Term Frequencies de totes les paraules del contingut.
	 * 
	 * @return Reotnra un HashMap que mapeja cada índex de paraula amb el seu TF (double) al contingut.
	 */
	public HashMap<Integer, Double> getTF(){

		HashMap<Integer, Double> tf_map = new HashMap<Integer, Double>();

		for (int index : words.keySet()){
			tf_map.put(index, getTFofWord(index));
		}

		return tf_map;
	}


	/** Mètode que indica si una seqüència 'str' existeix en el nostre contingut.
	 * 
	 * @param str La seqüència que volem cercar
	 * @return Retorna un booleà, on es retorna <b>true</b> <i>iff</i> la seqüència 'str' apareix al contingut.
	 */
	public boolean conteSequencia(String str){
		if (str.equals("")){
			CtrlDomini.mostraError("La seqüència no conté cap caràcter.");
			return false;
		}

		int it = 0;

		for (char c : plaintext.toCharArray()){
			if (c == str.charAt(it)) ++it;
			else if (c == str.charAt(0)) it = 1;
			else it = 0;

			if (it == str.length()) return true;
		}
		
		return false;
	}

	public String toString(){
		return plaintext;
	}

}
