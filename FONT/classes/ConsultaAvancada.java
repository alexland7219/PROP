package classes;
import java.util.Set;
import java.util.HashSet;

/** Consulta documents que conte la seqüència indicada.
 * @author Bernat Borràs Civil (bernat.borras.civil@estudiantat.upc.edu)
 */
public class ConsultaAvancada {

    /**
     * Retorna el conjunt de documents dins de l que contenen la seqüència s.
     * @param l conjunt de documents del sistema.
     * @param s seqüència que es vol cercar.
     * @return Conjunt de documents que contenen la seqüència s.
     */
    public static Set<Document> obtenirDocuments(Llibreria l, String s) {
        Set<Document> documents = l.getSetDocuments();
        Set<Document> docs = new HashSet<>();
        for (Document d : documents) {
            if (d.conteSequencia(s)) docs.add(d);
        }
        return docs;
    }
}
