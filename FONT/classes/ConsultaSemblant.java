package classes;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Alexandre Ros Roger (alexandre.ros.roger@estudiantat.upc.edu)
 *                             (alexland7219@gmail.com) 
 **/
public class ConsultaSemblant {

    /**
     * Mètode static de la classe que ens permetrà executar querys.
     * Aquest és l'únic mètode de la classe.
     * 
     * @param lib La llibreria en la qual fem la consulta
     * @param doc El document amb el qual volem comparar i ordenar els <b>k</b> més semblants
     * @param k El número de documents a retornar. Si <i>k > |lib|</i> llavors es retornen <i>|lib|-1</i> documents
     * @param mode El mètode amb el qual volem donar pesos a les paraules (0=tf-idf; 1=ocurrències).
     * @return Es retorna una llista de parells <cosinus, Document D> ordenats. El cosinus és el grau de semblança (entre -1 i 1) dels
     *         documents <i>doc</i> i <i>D</i>, aquest grau és el cosinus de la representació dels documents en un vector TF-IDF.
     * 
     *         Els documents s'ordenen en ordre decreiexent de semblança amb <i>doc</i>. <i>doc</i> no pot ser retornat.
     */
    public static ArrayList<Pair<Double, Document>> executeQuery(Llibreria lib, Document doc, int k, int mode){
        Set<Document> mySet = lib.getSetDocuments();

        // En ordre decreixent per el cosinus (els primers k seran els q es retornen)
        ArrayList<Pair<Double, Document>> orderedDocs = new ArrayList<>();

        if (!mySet.contains(doc)){
            CtrlDomini.mostraError("Document is not in the library!");
            return (new ArrayList<Pair<Double, Document>>());
        }

        for (Document T : mySet){
            if (T == doc) continue;

            double cosinus = lib.computeCosinus(T, doc, mode);

            boolean inserted = false;
            // Now insert document T!
            for (int i = 0; i < orderedDocs.size() && !inserted; ++i){
                if (orderedDocs.get(i).getL() >= cosinus) continue;
                if (orderedDocs.get(i).getL() < cosinus){
                    orderedDocs.add(i, new Pair<>(cosinus, T));
                    inserted = true;
                }
            }

            if (!inserted) orderedDocs.add(new Pair<>(cosinus, T));
        }

        // Now we return the k first elements
        ArrayList<Pair<Double, Document>> returnSet = new ArrayList<>();

        for (int i = 0; i < k && i < orderedDocs.size(); ++i){
            returnSet.add(orderedDocs.get(i));
        }

        return returnSet;
    }

}