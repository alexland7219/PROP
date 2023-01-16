package classes;
import java.util.HashSet;
import java.util.Set;

/** Consulta documents preferits.
 * @author Bernat Borràs Civil (bernat.borras.civil@estudiantat.upc.edu)
 */
 public class ConsultaPreferit {

    /**
     * Retorna el conjunt de documents preferits.
     * @param documents conjunt de tots els documents.
     * @return conjunt de documents preferits.
     */
    public static Set<Document> getDocPreferit(Set<Document> documents) {
        Set<Document> preferits = new HashSet<>();
        for (Document d : documents) if (d.getFavourite()) preferits.add(d);
        return preferits;
    }
 }