package classes;
import java.time.LocalDate;
import java.util.ArrayList;


/** Consulta utilitzada per obtenir els documents que es troben "dintre" d'un interval de dues dates, segons el seu atribut data.
 * @author Oscar Ramos Nuñez (oscar.ramos.nunez@estudiantat.upc.edu)
 */
public class ConsultaData {

    /** Consulta per l'interval. Ídem que la consulta sense paràmetres però amb la possibilitat de posar nous paràmetres en la pròpia crida. 
     * @return ArrayList de documents els quals la seva data és igual o posterior a "anterior" i igual o anterior a "posterior".
     * @param anterior fita superior de l'interval.
     * @param posterior fita inferior de l'interval.
     */
    public static ArrayList<Document> consulta(ArrayList<Document> documents, LocalDate anterior, LocalDate posterior) {
        int i = 0;
        int j = documents.size() - 1;
        /** intent de fer cerca dicotòmica però no s'ha aconseguit que funcioni correctament. */
        /*int iaux = 0; //m en la primera cerca
        while (i <= j) {
            iaux = i + (j - i) / 2;

            if (documents.get(iaux).getData().isEqual(anterior)) {
                 en cas d'empat anem a buscar el primer element 
                while (iaux > 0 && documents.get(iaux-1).getData().isEqual(anterior)) --iaux;
                break;
            }

            if (documents.get(iaux).getData().isAfter(anterior)) {
                j = iaux-1;
            }

            else i = iaux+1;

        }

        i = 0;
        j = documents.size() - 1;
        int jaux = 0;
        while (i <= j) {
            jaux = i + (j - i) / 2;

            if (documents.get(jaux).getData().isEqual(posterior)) {
                 en cas d'empat anem a buscar el primer element 
                while (jaux < documents.size() -1 && documents.get(jaux+1).getData().isEqual(posterior)) ++jaux;
                break;
            }

            if (documents.get(jaux).getData().isAfter(posterior)) {
                j = jaux-1;
            }

            else i = jaux+1;

        }

        i = iaux;
        j = jaux;*/

        while (i < documents.size() && documents.get(i).getData().isBefore(anterior)) ++i;
        while (j >= 0 && documents.get(j).getData().isAfter(posterior)) --j;

        ArrayList<Document> interval = new ArrayList<>();

        for (int k = i; k <= j; ++k){
            interval.add(documents.get(k));
        }

        return interval;
    }
}
