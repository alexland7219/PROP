package classes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/** Classe per a la consulta de documents per autor.
 * @author Bernat Borràs Civil (bernat.borras.civil@estudiantat.upc.edu)
 */
 public class ConsultaTitol {
    /**
     * Retorna el conjunt de documents corresponent a l'autor.
     * @param autor autor dels documents retornats.
     * @param autor_documents Arbre d'autors i documents.
     * @return Conjunt de documents corresponent a l'autor.
     */
    public static Set<Document> getDocAutor(Frase autor, TernaryTree<Pair<Frase, HashMap<String, Document>>> autor_documents) {
        Pair<Frase, HashMap<String, Document>> autorObtingut = autor_documents.obtenir(autor.toString(), 0);
        Set<Document> documents = new HashSet<>();
        if (autorObtingut == null) return documents; // Si no hi ha titols de l'autor

        for (Document d : autorObtingut.getR().values()) documents.add(d);
        return documents;
    }
 }