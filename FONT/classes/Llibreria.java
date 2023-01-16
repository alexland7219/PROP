package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/** Llibreria (conjunt) de documents.
 * @author Alexandre Ros i Roger (alexandre.ros.roger@estudiantat.upc.edu)
 */
public class Llibreria {

	// Sparse vector representation as pairs of ints (idx, tf-idf)
	//private ArrayList< Pair<Document, ArrayList< Pair<Integer, Double> >> > docs;
	private ArrayList< Pair<Document, HashMap<Integer, Double>>> docs0;

	// Sparse vector representation as pairs of ints (idx, ocurrences)
	// Second strategy of weight assignment
	private ArrayList<Pair<Document, HashMap<Integer, Double>>> docs1;


	// HashMap that maps all words in the library to the number of documents it appears in.
	private HashMap<Integer, Integer> word_ocurrences;

	// Matriu de TF-IDF precalculats. 
	private HashMap<Document, HashMap<Document, Double>> precalculat0;
	private HashMap<Document, HashMap<Document, Double>> precalculat1;


	// Mapa que ens mapeja cada document amb el seu índex als vectors tfidf / ocurrències.
	private HashMap<Document, Integer> docMapper;

	private int nDocs;

	//Arbre d'autors i de documents. Cada autor té el seu map de documents, amb el títol com a clau.
	private TernaryTree<Pair<Frase, HashMap<String, Document>>> autor_documents;

	//Llistat de documents ordenats per data
	private ArrayList<Document> documents_per_data;
	
	/**
	 * Constructora d'una Llibreria per defecte.
	 */
	public Llibreria(){
		docs0 = new ArrayList< Pair<Document, HashMap<Integer, Double>>>();
		docs1 = new ArrayList<Pair<Document, HashMap<Integer, Double>>>();
		nDocs = 0;
		word_ocurrences = new HashMap<>();
		precalculat0 = new HashMap<>();
		precalculat1 = new HashMap<>();

		docMapper = new HashMap<>();
		autor_documents = new TernaryTree<>();
		documents_per_data = new ArrayList<>();
	}
	
	/** Mètode que ens calcula el cosinus entre dos documents existents en la llibreria.
	 * 
	 * @param d1 Referència al primer document.
	 * @param d2 Referència al segon document.
	 * @return El cosinus entre els vectors tf-idf que representen d1 i d2.
	 */
	public Double computeCosinus(Document d1, Document d2, int mode){

		//int i = docMapper.get(d1);
		//int j = docMapper.get(d2);

		int i = 0; int j = 0;
		while (docs0.get(i).getL() != d1) ++i;
		while (docs0.get(j).getL() != d2) ++j; 

		HashMap<Document, HashMap<Document, Double>> precalculat;
		if (mode == 0) precalculat = precalculat0;
		else precalculat = precalculat1;


		if (precalculat.containsKey(d1) && precalculat.get(d1).containsKey(d2)){
			return precalculat.get(d1).get(d2);
		}
		else if (precalculat.containsKey(d2) && precalculat.get(d2).containsKey(d1)){
			return precalculat.get(d2).get(d1);
		}

		ArrayList<Pair<Document, HashMap<Integer, Double>>> docs;

		if (mode == 0) docs = docs0;
		else docs = docs1;
		

		double len1 = 0;
		double len2 = 0;

		for (int w : docs.get(i).getR().keySet()){
			double val = docs.get(i).getR().get(w);
			len1 += (val*val);
		}

		for (int w : docs.get(j).getR().keySet()){
			double val = docs.get(j).getR().get(w);
			len2 += (val*val);
		}

		len1 = Math.sqrt(len1);
		len2 = Math.sqrt(len2);

		double dotproduct = 0;

		for (int w : docs.get(i).getR().keySet()){
			if (!docs.get(j).getR().containsKey(w)) continue;

			dotproduct += docs.get(i).getR().get(w) * docs.get(j).getR().get(w);
		}


		if (precalculat.containsKey(d1)){
			precalculat.get(d1).put(d2, dotproduct / (len1*len2));
		} else {
			precalculat.put(d1, new HashMap<>());
			precalculat.get(d1).put(d2, dotproduct / (len1*len2));
		}

		return (Double) dotproduct / (len1 * len2);
	}
	
	/** Mètode per a afegir un document a la llibreria.
	 * 
	 * @param d Document a afegir.
	 */
	public void addDocument(Document d){
		HashMap<Integer, Double> tfs = d.getTF();
		HashMap<Integer, Double> TFIDF = new HashMap<Integer, Double>();
		HashMap<Integer, Double> OCURR = new HashMap<Integer, Double>();

		++nDocs;

		// Since there can't be repeated keys, no need to check if the word has already been processed
		for (int index : tfs.keySet()){
			//System.out.println("Processing word #"+index+"...");

			if (word_ocurrences.containsKey(index)) word_ocurrences.put(index, word_ocurrences.get(index) + 1);
			else word_ocurrences.put(index, 1);

			TFIDF.put(index, tfs.get(index) * ((double) nDocs / word_ocurrences.get(index)));
			OCURR.put(index, (double) d.getContingut().getWords().get(index));

		}

		// Now we need to update the IDF of every word added
		for (int j = 0; j < docs0.size(); ++j){

			HashMap<Integer, Double> words_of_doc = docs0.get(j).getR();
			
			for (int w : words_of_doc.keySet()){
									
				words_of_doc.put(w, docs0.get(j).getL().getTFofWord(w) * (double) nDocs / word_ocurrences.get(w));
			}
		}

		docs0.add(new Pair<>(d, TFIDF));
		docs1.add(new Pair<>(d, OCURR));

		docMapper.put(d, docs0.size()-1);
		Pair<Frase,HashMap<String, Document>> node = autor_documents.inserirObtenir(d.getAutor().toString() ,0, new Pair<Frase, HashMap<String, Document>>(d.getAutor(), new HashMap<>()));
		node.getR().put(d.getTitol().toString(), d);

		afegir_document_ordenat(d);
	} 

	public void afegir_document_ordenat(Document D) {
        int index = 0; boolean tie = false, putted = false;
        for (int i = 0; i < documents_per_data.size(); ++i) {
            if (documents_per_data.get(i).getData().isAfter(D.getData())) break;
            else if (documents_per_data.get(i).getData().isEqual(D.getData())) tie = true;
            else ++index;
        }
        if (tie) {
            //n'hi ha minim 1 document amb la mateixa data -> s'inserta per ordre alfabètic de títol
            while (index < documents_per_data.size() && documents_per_data.get(index).getData().isEqual(D.getData())) {
                if (documents_per_data.get(index).getTitol().toString().compareTo(D.getTitol().toString()) < 0) ++index;
                else {
                    documents_per_data.add(index,D); 
                    putted = true;
                    break;
                }
            }
            if (!putted) documents_per_data.add(index,D);
        }
        
        else documents_per_data.add(index,D);
    }
	
	/** Mètode per a eliminar un document de la llibreria.
	 * 
	 * @param d Document a eliminar.
	 */
	public void deleteDocument(Document d){
		docMapper.remove(d);
		documents_per_data.remove(d);
		Pair<Frase,HashMap<String, Document>> node = autor_documents.obtenir(d.getAutor().toString() ,0);
		node.getR().remove(d.getTitol().toString());

		for (int i = 0; i < docs0.size(); ++i){
			if (d == docs0.get(i).getL()){

				HashMap<Integer, Double> words_to_remove = docs0.get(i).getR();

				for (int index : words_to_remove.keySet()){
					word_ocurrences.put(index, word_ocurrences.get(index) - 1);
					if (word_ocurrences.get(index) == 0) word_ocurrences.remove(index);
				}

				docs0.remove(i);
				docs1.remove(i);
				--nDocs;

				// Now we need to update all IDFs
				for (int j = 0; j < docs0.size(); ++j){
					
					HashMap<Integer, Double> words_of_doc = docs0.get(j).getR();

					for (int w : words_of_doc.keySet()){
													
						words_of_doc.put(w, docs0.get(j).getL().getTFofWord(w) * (double) nDocs / word_ocurrences.get(w));
					}

				}

				return;
			}
		}
	}

	/** Getter d'una referència a un document donat el seu autor i títol en Strings.
	 * 
	 * @param nomAutor String del nom de l'autor.
	 * @param nomTitol String del títol del document.
	 * @return Un parell de Document (el document cercat) i booleà, on el booleà és <b>false</b> si el document no existeix.
	 */
	public Pair<Document, Boolean> getDocument(String author, String title){
		Pair<Frase, HashMap<String, Document>> documentsAutor = autor_documents.obtenir(author, 0);
		if (documentsAutor == null || documentsAutor.getR() == null || documentsAutor.getL() == null) return new Pair<Document,Boolean>(null, false);
		Document d = documentsAutor.getR().get(title);
		if (d == null) return new Pair<Document,Boolean>(null, false);
		return new Pair<Document,Boolean>(d, true);
	}
	
	/** Mètode que retorna una nova llibreria amb tots els documents marcats com a preferits.
	 * 
	 * @return Llibreria (subconjunt de la llibreria actual) que conté només els documents preferits.
	 */
	public Llibreria getPreferits(){
		Llibreria favLib = new Llibreria();
		
		
		for (int i = 0; i< docs0.size(); ++i){
			if (docs0.get(i).getL().getFavourite()){
				// Si el document és preferit
				favLib.addDocument(docs0.get(i).getL());
			}
		}
		
		return favLib;
	}
	
	/** Mètode que retorna l'i-éssim document inserit a la llibreria.
	 * 
	 * @param i Índex del document a la llibreria.
	 * @return Una referència al Document i-éssim.
	 */
	public Document getIessim(int i){
		return docs0.get(i).getL();
	}

	/** Getter del nombre de documents totals a la llibreria.
	 * 
	 * @return Natural que indica el nombre de documents actuals a la llibreria.
	 */
	public int getNdocs(){
		return nDocs;
	}
	
	/** Mètode que retorna el conjunt de documents de la llibreria amb un Set.
	 * 
	 * @return Un Set de referències als documents de la llibreria.
	 */
	public Set<Document> getSetDocuments(){
		Set<Document> mySet = new HashSet<Document>();

		for (int i = 0; i < docs0.size(); ++i){
			mySet.add(docs0.get(i).getL());
		}

		return mySet;
	}

	/** Mètode que retorna la Llibreria representada con una arrayList de Documents (en String)
	 * 
	 * @return La llista de Strings que representa la llibreria.
	 */
	public ArrayList<String> toStringArray(){
		ArrayList<String> list = new ArrayList<>();

		for (int i = 0; i < docs0.size(); ++i){
			list.add(docs0.get(i).getL().toString());
		}

		return list;
	}

	public String toString(){
		StringBuilder str = new StringBuilder("");

		for (int i = 0; i < docs0.size(); ++i){
			str.append(docs0.get(i).getL().getTitol());
			str.append("\n");
			str.append(docs0.get(i).getL().getAutor());
			str.append("\n");
			str.append("---------------------\n");
		}

		return str.toString();
	}

	public TernaryTree<Pair<Frase, HashMap<String, Document>>> getArbre() {
		return autor_documents;
	}

	public ArrayList<Document> getDocArray() {
		return documents_per_data;
	}
}
