package classes;

import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.Arrays;

/** Classe controladora de les classes de domini.
 * @author Alexandre Ros i Roger (alexandre.ros.roger@estudiantat.upc.edu)
 */
public class CtrlDomini {

	private Vocabulari vocab;
	private Llibreria lib;
	private ExpressioBooleanaCtrl EBC;
	private CtrlPersistencia DISK;
	private static CtrlPresentacio UI;

	static private 
	Comparator<Document> documentDataComparator = new Comparator<Document>(){
		@Override
		public int compare(Document d1, Document d2){
			LocalDate v1 = d1.getData(); 
			LocalDate v2 = d2.getData();

			if (v1.isBefore(v2)) return -1;
			else if (v1.isAfter(v2)) return 1;
			else return 0;
		}
	};

	static private
	Comparator<Document> documentFavouriteComparator = new Comparator<Document>(){
		@Override
		public int compare(Document d1, Document d2){
			boolean f1 = d1.getFavourite();
			boolean f2 = d2.getFavourite();

			if (f1 && !f2) return -1;
			else if (f2 && !f1) return 1;
			else return 0;
		}
	};

	static private 
	Comparator<Document> documentAuthorComparator = new Comparator<Document>(){
		@Override
		public int compare(Document d1, Document d2){
			String a1 = d1.getAutor().toString();
			String a2 = d2.getAutor().toString();

			return a1.compareTo(a2);
		}
	};

	static private 
	Comparator<Document> documentTitleComparator = new Comparator<Document>(){
		@Override
		public int compare(Document d1, Document d2){
			String a1 = d1.getTitol().toString();
			String a2 = d2.getTitol().toString();

			return a1.compareTo(a2);
		}
	};

	/** Mètode public per a determinar si un document existeix o no.
	 * 
	 * @param title Títol del document a cercar
	 * @param author Autor del document a cercar
	 * 
	 * @return Booleà que estarà a <b>true</b> si, i només si,  
	 */
	public boolean docExists(String title, String author){
		return lib.getDocument(author, title).getR();
	}

	/** Mètode static per a ordenar llistes de documents segons un tipus d'ordenació.
	 * 
	 * Els tipus d'ordenació són per dates (0), per preferit (1), per autor alfabèticament (2) o per títol alfabèticament (3).
	 * 
	 * @param docs La llista de Documents a ordenar per un cert criteri.
	 * @param type Enter que representa el criteri d'ordenació.
	 * 
	 */
	static public ArrayList<Document> sortDocuments(ArrayList<Document> docs, int type){
		
		switch (type){
			case 0:
			// Per dates
			docs.sort(documentDataComparator);
			break;
			
			case 1:
			// Per preferit
			docs.sort(documentFavouriteComparator);
			break;

			case 2:
			// Per autor
			docs.sort(documentAuthorComparator);
			break;

			case 3:
			// Per títol
			docs.sort(documentTitleComparator);
		}

		return docs;
	}

	/** Mètode static per a ordenar conjunts de documents segons un tipus d'ordenació.
	 * 
	 * Els tipus d'ordenació són per dates (0), per preferit (1), per autor alfabèticament (2) o per títol alfabèticament (3)
	 * 
	 * @param docs El conjunt de Documents a ordenar per un cert criteri.
	 * @param type Enter que representa el criteri d'ordenació.
	 * @return La llista de Documents ordenada.
	 */
	static public ArrayList<Document> sortDocuments(Set<Document> docs, int type){

		ArrayList<Document> doclist = new ArrayList<Document>();

		for (Document doc : docs) doclist.add(doc);

		return sortDocuments(doclist, type);
	}

	/** Constructora del Controlador de Document.
	 * 
	 */
	public CtrlDomini () {
		vocab = new Vocabulari();
		lib = new Llibreria();
		EBC = new ExpressioBooleanaCtrl();
		DISK = new CtrlPersistencia(this);
	};

	/** Mètode privat que ens descomposa una String 'frase' en una llista de Strings, que són les paraules
	 *  que extraiem de 'frase'. Només es tenen en compte cadenes de lletres per a identificar les paraules.
	 * 
	 * @param frase La String que volem descomposar en paraules
	 * @return Una llista de Strings, que representa la descomposició de 'frase' en paraules.
	 */
	static private ArrayList<String> decomposeWords(String frase) {
        ArrayList<String> paraules = new ArrayList<>();
        String a_insert = "";

        for (char c: frase.toCharArray()) {
            if (!Character.isLetter(c)) {
                if (a_insert.length() != 0) {
                    paraules.add(a_insert);
                    a_insert = "";
                }
            }
            else a_insert += c;
        }
		if (a_insert.length() != 0) paraules.add(a_insert);

        return paraules;
    }

	/** Getter d'una referència a un document donat el seu autor i títol en Strings.
	 * 
	 * @param nomAutor String del nom de l'autor.
	 * @param nomTitol String del títol del document.
	 * @return Un parell de Document (el document cercat) i booleà, on el booleà és <b>false</b> si el document no existeix.
	 */
	public Pair<Document, Boolean> getDocument(String nomAutor, String nomTitol){
		return lib.getDocument(nomAutor, nomTitol);
	}
	
	/** Mètode públic per a generar les frases a partir del plaintext d'un document.
	 * 
	 * @param plaintext El plaintext del qual volem extreure'n les frases.
	 * @return Una array de referències a objectes Frase, la unió dels quals composa el plaintext.
	 */
	public Frase[] generatePhrases(String plaintext){
		String[] contarray = plaintext.split("\\p{Punct}");
        ArrayList<String> content = new ArrayList<String>(Arrays.asList(contarray));

		ArrayList<ArrayList<String>> contentdecomp = new ArrayList<ArrayList<String>>();

		ArrayList<Paraula> arrWords = new ArrayList<Paraula>();

		for (int s = 0; s < content.size(); ++s){
			contentdecomp.add(decomposeWords(content.get(s)));
		}

		Frase[] cont = new Frase[contentdecomp.size()];

		for (int w = 0; w < contentdecomp.size(); ++w){
			ArrayList<String> currphrase = contentdecomp.get(w);
			arrWords.clear();

			for (int y = 0; y < currphrase.size(); ++y){
				Paraula wd = vocab.inserirObtenirParaula(currphrase.get(y));
				arrWords.add(wd);
			}

			cont[w] = new Frase(arrWords, content.get(w));
		}

		return cont;
	}

	/** Mètode públic per a generar un objecte Contingut donat el seu plaintext.
	 * 
	 * @return Una referència a l'objecte Contingut generat.
	 */
	public Contingut generateContent(String plaintext){
		return new Contingut(plaintext, generatePhrases(plaintext));
	}

	/** Mètode públic per a crear un document.
	 * 
	 * @param title Títol del document a crear
	 * @param author Autor del docuemnt a crear
	 * @param content Contingut separat per frases del document a crear.
	 * @param plaintext_cont Contingut sencer sense separar.
	 * @param dia LocalDate de la data de creació del document.
	 * @param isFav Booleà que indica si el document hauria de ser preferit o no.
	 */
	public void crearDocument(String title, String author, ArrayList<String> content, String plaintext_cont, LocalDate dia, boolean isFav){
		
		ArrayList<String> titledecomp = decomposeWords(title);
		
		ArrayList<String> authordecomp = decomposeWords(author);
		
		ArrayList<ArrayList<String>> contentdecomp = new ArrayList<ArrayList<String>>();

		for (int s = 0; s < content.size(); ++s){
			contentdecomp.add(decomposeWords(content.get(s)));
		}
		
		ArrayList<Paraula> arrWords = new ArrayList<Paraula>();

		for (int w = 0; w < titledecomp.size(); ++w){
			Paraula wd = vocab.inserirObtenirParaula(titledecomp.get(w));
			arrWords.add(wd);
		}

		Frase titlePhrase = new Frase(arrWords, title); 

		arrWords.clear();
		for (int w = 0; w < authordecomp.size(); ++w){
			Paraula wd = vocab.inserirObtenirParaula(authordecomp.get(w));
			arrWords.add(wd);
		}

		Frase authorPhrase = new Frase(arrWords, author);
				 
		Frase[] cont = new Frase[contentdecomp.size()];

		for (int w = 0; w < contentdecomp.size(); ++w){
			ArrayList<String> currphrase = contentdecomp.get(w);
			arrWords.clear();

			for (int y = 0; y < currphrase.size(); ++y){
				Paraula wd = vocab.inserirObtenirParaula(currphrase.get(y));
				arrWords.add(wd);
			}

			cont[w] = new Frase(arrWords, content.get(w));
		}

		Contingut contentFinal = new Contingut(plaintext_cont, cont);
		
		Document doc = new Document(authorPhrase, titlePhrase, isFav, dia, contentFinal, this);

		lib.addDocument(doc);

		DISK.crearDocument(title, author, plaintext_cont, dia, isFav);

		doc.oblidaContingut();
	}

	/** Funció que retorna el booleà de preferit d'un document
	 * 
	 * @param title Títol del document a consultar.
	 * @param author Autor del docuemnt a consultar.
	 * @return Booleà <i>isFav</i>. Si el document no existeix retorna fals.
	 */
	public boolean getFavourite(String title, String author)
	{
		Pair<Document, Boolean> doc = getDocument(author, title);
		if (!doc.getR()) return false;

		return doc.getL().getFavourite();
	}

	/** Funció que retorna la Data d'un document
	 *
	 * @param title Títol del document a consultar
	 * @param author Autor del document a consultar
	 * @return LocalDate de creació del document.
	 */
	 public LocalDate getData(String title, String author)
	 {
		Pair<Document, Boolean> doc = getDocument(author, title);
		if (!doc.getR()) return LocalDate.MIN;

		return doc.getL().getData();
	 }	

    /** Funció que retorna el contingut d'un document
	 *
	 * @param title Títol del document a consultar
	 * @param author Autor del document a consultar
	 * @return String que representa el contingut del document.
	 */

	public String getContingut(String title, String author){
		String ptext = DISK.getContingut(title, author);

		return ptext;
	}

	/** Mètode públic per a fer un preview d'una sola línia d'un document
	 * 
	 * <i>Nota: el document (title, author) hauria d'existir</i>
	 * 
	 * @param title Títol del document
	 * @param author Autor del document
	 * @return String del preview del document
	 */
	public String preview(String title, String author){
		return getDocument(author, title).getL().toString();
	}

	/** Mètode públic que implementa la consulta d'Autors per Prefix.
	 * 
	 * @param prefix Prefix amb el qual cercar els autors
	 * @return Un Set d'Strings que contindrà tots els autors començant pel prefix.
	 */
	public Set<String> donaAutors(String prefix){
		return ConsultaAutors.donaAutors(prefix, lib.getArbre());
	}

	/** Mètode públic que implementa la modificació de la data d'un document.
	 * 
	 * @param title Títol del document a modificar
	 * @param author Autor del document a modificar
	 * @param dat Nova data del document
	 */
	public void modificarData(String title, String author, LocalDate dat){
		Document d = getDocument(author, title).getL();
		
		d.setData(dat);
		DISK.crearDocument(d.getTitol().toString(), d.getAutor().toString(), d.getContingut().toString(), d.getData(), d.getFavourite());
	}

	/** Mètode públic que implementa la modificació de l'autor d'un document
	 * 
	 * @param title Títol del document a modificar.
	 * @param oldAuth Autor del document a modificar.
	 * @param newAuth Nou autor del document
	 */
	public void modificarAutor(String title, String oldAuth, String newAuth){
		Document d = getDocument(oldAuth, title).getL();
		
		ArrayList<String> authordecomp = decomposeWords(newAuth);
		ArrayList<Paraula> arrWords = new ArrayList<Paraula>();

		for (int w = 0; w < authordecomp.size(); ++w){
			Paraula wd = vocab.inserirObtenirParaula(authordecomp.get(w));
			arrWords.add(wd);
		}

		Frase authorPhrase = new Frase(arrWords, newAuth);

		String cont = DISK.getContingut(d.getTitol().toString(), d.getAutor().toString());

		d.setAutor(authorPhrase);

		HashMap<String, Document> documentsOldAuthor = lib.getArbre().obtenir(oldAuth, 0).getR();
		Pair<Frase, HashMap<String, Document>> documentsNewAuthor = lib.getArbre().obtenir(newAuth, 0);

		if (documentsNewAuthor == null)
		{
			// Nou autor
			documentsNewAuthor = lib.getArbre().inserirObtenir(newAuth, 0, new Pair<Frase, HashMap<String, Document>>(d.getAutor(), new HashMap<>()));
		}

		//HashMap<String, Document> documentsNewAuthor = lib.getArbre().obtenir(newAuth, 0).getR();
		documentsOldAuthor.remove(title);
		documentsNewAuthor.getR().put(title, d);
		DISK.esborrarDocument(title, oldAuth);
		DISK.crearDocument(d.getTitol().toString(), d.getAutor().toString(), cont, d.getData(), d.getFavourite());
	}

	/** Mètode públic que implementa la modificació del contingut d'un document.
	 * 
	 * @param title Títol del document a modificar.
	 * @param author Autor del document a modificar.
	 * @param content Nou contingut separat per frases.
	 * @param plaintext_content El nou contingut sencer, sense separar.
	 */
	public void modificarContingut(String title, String author, ArrayList<String> content, String plaintext_content){

		// Suposarem que d existeix.
		Document d = getDocument(author, title).getL();

		boolean isFav = d.getFavourite();
		LocalDate dat = d.getData();

		eliminarDocument(title, author);
		crearDocument(title, author, content, plaintext_content, dat, isFav);

		DISK.crearDocument(d.getTitol().toString(), d.getAutor().toString(), DISK.getContingut(d.getTitol().toString(), d.getAutor().toString()), d.getData(), d.getFavourite());
	}

	/** Mètode públic que implementa la modificació del títol d'un document
	 * 
	 * @param oldTitle Títol del document a modificar.
	 * @param author Autor del document a modificar.
	 * @param newTitle Nou títol del document
	 */

	public void modificarTitol(String oldTitle, String author, String newTitle){
		Document d = getDocument(author, oldTitle).getL();
		
		ArrayList<String> titledecomp = decomposeWords(newTitle);
		ArrayList<Paraula> arrWords = new ArrayList<Paraula>();

		for (int w = 0; w < titledecomp.size(); ++w){
			Paraula wd = vocab.inserirObtenirParaula(titledecomp.get(w));
			arrWords.add(wd);
		}

		Frase titolFrase = new Frase(arrWords, newTitle);

		String cont = DISK.getContingut(d.getTitol().toString(), d.getAutor().toString());


		d.setTitol(titolFrase);

		HashMap<String, Document> documentsAutor = lib.getArbre().obtenir(author, 0).getR();
		documentsAutor.remove(oldTitle);
		documentsAutor.put(newTitle, d);

		DISK.esborrarDocument(oldTitle, author);
		DISK.crearDocument(d.getTitol().toString(), d.getAutor().toString(), cont, d.getData(), d.getFavourite());

	}

	/** Mètode públic que elimina un document.
	 * 
	 * @param title Títol del document a eliminar.
	 * @param author Autor del document a eliminar.
	 */
	public void eliminarDocument(String title, String author){
		Document doc = getDocument(author, title).getL();;
		
		// Rebaixar per 1 les ocurrències de cada paraula
		Frase authorFrase = doc.getAutor();
		Frase titleFrase  =doc.getTitol();
		String plaintext = DISK.getContingut(title, author);
		//Contingut content = doc.getContingut();
		Contingut content = new Contingut(plaintext, generatePhrases(plaintext));

		Frase[] frasesContingut = content.getFrases();
		ArrayList<Paraula[]> wordsContingut = new ArrayList<>();

		for (int i = 0; i < frasesContingut.length; ++i){
			wordsContingut.add(frasesContingut[i].getOracio());
		}

		Paraula[] wordsAutor = authorFrase.getOracio();
		Paraula[] wordsTitol = titleFrase.getOracio();
		
		lib.deleteDocument(doc);

		for (int i = 0; i < wordsAutor.length; ++i){
			vocab.decrementarOcurrencia(wordsAutor[i]);
			
		}

		for (int i = 0; i < wordsTitol.length; ++i){
			vocab.decrementarOcurrencia(wordsTitol[i]);
		}


		for (int i = 0; i < wordsContingut.size(); ++i){
			for (int j = 0; j < wordsContingut.get(i).length; ++j){
				vocab.decrementarOcurrencia(wordsContingut.get(i)[j]);
			}
		}
		
		System.gc();
		DISK.esborrarDocument(title, author);
	}

	/** Mètode públic que implementa la consulta per Data.
	 * 
	 * @param ant Límit inferior de les dates.
	 * @param post Límit superior de les dates.
	 * @param criteria Criteri d'ordenació dels resultats
	 * @return Una ArrayList amb els preview dels documents creats entre ant i post.
	 */
	public ArrayList<String> consultaData(LocalDate ant, LocalDate post, int criteria)
	{
		ArrayList<Document> docs = ConsultaData.consulta(lib.getDocArray(), ant, post);
		docs = sortDocuments(docs, criteria);

		ArrayList<String> myList = new ArrayList<String>();

		for (Document d : docs){
			myList.add(d.toString());
		}

		return myList;
	}

	/** Mètode públic que implementa la consulta per Seqüència
	 * 
	 * @param seq Cadena de caràcters a cercar.
	 * @param criteria Criteri d'ordenació pel qual ordenar els documents resultants.
	 * @return Una ArrayList amb els preview dels documents que contenen la seqüència <i>seq</i>
	 */
	public ArrayList<String> consultaSeq(String seq, int criteria){
		Set<Document> setdocs = ConsultaAvancada.obtenirDocuments(lib, seq);
		if (setdocs == null) return new ArrayList<>();
		ArrayList<Document> result = sortDocuments(setdocs, criteria);

		ArrayList<String> myList = new ArrayList<>();

		for (Document d : result){
			myList.add(d.toString());
		}

		return myList;
	}

	/** Mètode públic que retorna tots els documents.
	 * 
	 * @return Una ArrayList amb els preview de tots els documents.
	 */
	public ArrayList<String> getAllDocs(){
		return lib.toStringArray();
	}

	/** Mètode públic que implementa la consulta dels Títols d'un Document.
	 * 
	 * @param autor Autor dels documents a cercar.
	 * @param criteria Criteri d'ordenació pel qual ordenar els documents resultants.
	 * @return Una ArrayList amb els preview dels documents de l'autor <i>autor</i>
	 */
	public ArrayList<String> consultaTit(String autor, int criteria){
		Set<Document> setdocs = ConsultaTitol.getDocAutor(new Frase(autor), lib.getArbre());
		//if (setdocs == null) return new ArrayList<>();
		ArrayList<Document> result = sortDocuments(setdocs, criteria);

		ArrayList<String> myList = new ArrayList<>();

		for (Document d : result){
			myList.add(d.toString());
		}

		return myList;

	}

	/** Mètode públic que implementa la consulta per Semblança de documents.
	 * 
	 * @param titol Títol del document referència per a cercar.
	 * @param autor Autor del document referència per a cercar.
	 * @param n Nombre de documents a retornar
	 * @param mode Mode de Consulta per Semblança (0=tf-idf, 1=ocurrències).
	 * @return Una ArrayList amb els preview dels <i>n</i> documents més semblants al referència.
	 */
	public ArrayList<String> consultaSemb(String titol, String autor, int n, int mode){
		Document doc = getDocument(autor, titol).getL();

		ArrayList<Pair<Double, Document>> result = ConsultaSemblant.executeQuery(lib, doc, n, mode);

		ArrayList<String> stg = new ArrayList<>();
		
		for (Pair<Double, Document> d: result) {
			stg.add(d.getR().toString() + " (" + d.getL() + ")");
        }

		return stg;

	}

	/** Mètode públic que implementa la consulta per Rellevància de documents
	 * 
	 * @param wordsSepBlank String amb totes les paraules a cercar amb espais.
	 * @param k Nombre de documents a retornar.
	 * @param modeConsulta Mode propi de consulta per Rellevància.
	 * @return Una ArrayList amb els preview dels <i>k</i> documents més rellevants segons la query.
	 */
	public ArrayList<String> consultaRell(String wordsSepBlank, int k, int modeConsulta)
	{
		String[] words = wordsSepBlank.split(" ");

		Paraula[] arrWords = new Paraula[words.length];
		for (int i = 0; i < words.length; ++i) arrWords[i] = vocab.inserirObtenirParaula(words[i]);

		if (modeConsulta == 1) return ConsultaRellevancia.ConsultaPerRellevancia(k, arrWords, wordsSepBlank, 1, lib).toStringArray();
		else return ConsultaRellevancia.ConsultaPerRellevancia(k, arrWords, wordsSepBlank, 2, lib).toStringArray();
	}

	/** Mètode públic que implementa la cerca dels documents preferits.
	 * 
	 * @param criteria Criteri d'ordenació dels documents resultants.
	 * @return Una ArrayList amb els preview dels documents preferits.
	 */
	public ArrayList<String> consultaPref(int criteria){
		Set<Document> docSet = ConsultaPreferit.getDocPreferit(lib.getSetDocuments());
		ArrayList<Document> docs = sortDocuments(docSet, criteria);

		ArrayList<String> myList = new ArrayList<String>();

		for (Document d : docs){
			myList.add(d.toString());
		}

		return myList;

	}

	/** Mètode que canvia l'estat del booleà preferit d'un document.
	 * 
	 * @param title Títol en String del document en el qual volem fer toggle del booleà <i>isFav</i>.
	 * @param author Autor en String del document en el qual volem fer toggle del booleà <i>isFav</i>.
	 */
	public void togglePreferit(String title, String author){

		// Fem el toggle
		Document d = getDocument(author, title).getL();

		d.setFavourite(!d.getFavourite());
		DISK.crearDocument(d.getTitol().toString(), d.getAutor().toString(), DISK.getContingut(title, author), d.getData(), d.getFavourite());
		return;
	}

	/** Mètode públic per a crear una Expressió Booleana.
	 * 
	 * @param nom Nom de l'EB a crear
	 * @param cos Cos de l'EB a crear
	 */
	public void novaEB(String nom, String cos){
		EBC.AddExpressioBooleana(nom, cos);
		DISK.crearExpressio(nom, cos);
	}

	/** Mètode públic per a modificar el cos d'una Expressió Booleana.
	 * 
	 * @param nom Nom de l'EB a modificar
	 * @param noucos Nou cos de l'EB
	 */
	public void canviarEB(String nom, String noucos){
		EBC.SetExpressioBooleana(nom, noucos);
		DISK.crearExpressio(nom, noucos);
	}

	/** Mètode públic que retorna totes les Expressions Booleanes guardades.
	 * 
	 * @return Una ArrayList dels noms de totes les EBs emmagatzemades.
	 */
	public ArrayList<String> getAllEBS(){
		return EBC.getAllEBS();
	}

	/** Mètode públic per a eliminar una Expressió Booleana.
	 * 
	 * @param nom Nom de l'EB a eliminar
	 */
	public void eliminarEB(String nom){
		EBC.DeleteExpressioBooleana(nom);
		DISK.esborrarExpressio(nom);
	}

	/** Mètode públic que retorna el nombre d'Expressions Booleanes emmagatzemades.
	 * 
	 * @return Nombre d'EBs emmagatzemades.
	 */
	public int numberOfEBS(){
		return EBC.getNEBS();
	}

	/** Mètode públic per a comprovar si l'Expressió Booleana amb un nom exiseix.
	 * 
	 * @param name Nom de l'EB a cercar
	 * @return Booleà que serà <i>true</i> si, i només si, l'EB "name" existeix.
	 */
	public boolean existsEB(String name){
		return EBC.existsEB(name);
	}

	/** Mètode públic per a retornar el cos d'una Expressió Booleana.
	 * 
	 * @param name Expressió Booleana a cercar.
	 * @return String del cos de l'EB "name".
	 */
	public String getCos(String name){
		return EBC.getCos(name);
	}

	/** Mètode públic que implementa la Consulta per Expressió Booleana
	 * 
	 * @param cos Cos de l'expressió booleana directa.
	 * @param nom Nom de l'expressió booleana emmagatzemada.
	 * @param mode Mode de consulta (0=per expressió amb nom, 1=expressió directa amb cos)
	 * @param criteria Criteri d'ordenació dels documents resultants.
	 * @return Una ArrayList dels documents que compleixin l'expressió booleana.
	 */
	public ArrayList<String> consultaEB(String cos, String nom, int mode, int criteria)
	{
		Set<Document> setdoc;
		ExpressioBooleana eb;
		
		if (mode == 1) eb = EBC.ExpressioBooleanaTemporal(cos);
		else eb = EBC.GetExpressioBooleana(nom);

		setdoc = eb.getResultat(lib);

		ArrayList<Document> docs = sortDocuments(setdoc, criteria);

		ArrayList<String> myList = new ArrayList<String>();

		for (Document d : docs){
			myList.add(d.toString());
		}

		return myList;

	}

	/** Mètode per a exportar un document.
	 *  @param titol El títol del document a exportar.
	 *  @param autor L'autor del document a exportar.
	 *  @param ext Extensió a emprar (0 = TXT 1 = XML 2 = YAY)
	 *  @param fname Nom del fitxer
	 */
	public void exportarDocument(String titol, String autor, int ext, String fname){
		if (!docExists(titol, autor)){
			mostraError("No existeix el document que vols exportar.");
			return;
		}

		Pair<Document, Boolean> docboolean = getDocument(autor, titol);
		//String content = docboolean.getL().getContingut().toString();
		String content = DISK.getContingut(docboolean.getL().getTitol().toString(), docboolean.getL().getAutor().toString());
		LocalDate data = docboolean.getL().getData();
		boolean isFav = docboolean.getL().getFavourite();

		String extension = ".txt";

		switch(ext){
			case 0:
			extension = ".txt";
			break;
			case 1:
			extension = ".xml";
			break;
			case 2:
			extension = ".yay";
			break;
		}

		DISK.export(titol, autor, content, data, isFav, fname+extension);
	}

	/**
     * Importa un fitxer a l'aplicació.
     * @param path path del document.
     */
    public void importFile(String path) {
		DISK.importFile(path);
    }

	/**
	 * Importa les dades desades a l'aplicació.
	 */
	public void importSaved() {
		DISK.importarDades();
	}

	/** Setter per al Controlador de Presentació
	 * 
	 * @param presentacio Referència al Controlador Presentació
	 */
	public void setControladorPresentacio(CtrlPresentacio presentacio) {
		UI = presentacio;
	}

	/** Mètode públic i estàtic per a mostrar un error per pantalla.
	 * 
	 * @param missatge Missatge d'error a mostrar.
	 */
	public static void mostraError(String missatge) {
		UI.mostraError(missatge);
	}
}
