package classes;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

/** Consulta utilitzada per obtenir els noms dels Autors que començen per un prefix (ja sigui el nom o un cognom).
 * @author Oscar Ramos Nuñez (oscar.ramos.nunez@estudiantat.upc.edu)
 */
public class ConsultaAutors {
    /**
     * Funció consulta. Retorna els autors els quals compleixen el prefix.
     * @param prefix Prefix de l'autor.
     * @param autor_documents Arbre d'autors i documents.
     * @return Set de Frases on cada frase és un autor.
     */
    public static Set<String> donaAutors(String prefix, TernaryTree<Pair<Frase, HashMap<String, Document>>> autor_documents) {
        Set<Pair<Frase, HashMap<String, Document>>> autors = autor_documents.obtenirPerPrefix(prefix);
        Set<String> set_autors = new HashSet<>();
        for (Pair<Frase, HashMap<String, Document>> autor : autors) set_autors.add(autor.getL().toString());
        return set_autors;
    }
}
