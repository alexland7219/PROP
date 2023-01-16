package classes;


/** Diccionari de les paraules.
 * @author Bernat Borràs Civil (bernat.borras.civil@estudiantat.upc.edu)
 */
public class Vocabulari {

    /**Node arrel de l'arbre. */
    TernaryTree<Paraula> arrel;

    /**Constructora per defecte de Vocabulari. */
    public Vocabulari() {
        arrel = new TernaryTree<Paraula>();
    }

    /**Obtenir la classe Paraula que correspon a la seqüència s.
     * @param s seqüencia de caràcters que forma la paraula.
     * @return Paraula : Classe paraula.
     */
    public Paraula obtenirParaula(String s) {
        if (s.length() > 0) return arrel.obtenir(s, 0);
        return null;
    }

    /**Obtenir la classe Paraula que correspon a la seqüència s, la crea si no existeix aquesta.
     * @param s paraula a inserir/obtenir.
     * @return Paraula : Classe paraula.
     */
    public Paraula inserirObtenirParaula(String s) {
        if (s == "") return null;
        Paraula p = arrel.inserirObtenir(s, 0, new Paraula(s));
        p.incrementarOcurrencia();
        return p;
    }

    /**Esborra una paraula del diccionari.
     * @param p paraula que es vol esborrar.
     */
    public void esborrarParaula(Paraula p) {
        arrel.esborrar(p.getParaula(), 0, arrel, 1);
    }

    /**Decrementa en una unitat el nombre d'ocurrencies de la paraula p. En el cas que posteriorment el número sigui 0, s'esborra la paraula.
     * @param p paraula que es vol decrementar el número d'ocurrències.
     */
    public void decrementarOcurrencia(Paraula p) {
        Paraula dec = arrel.obtenir(p.getParaula(), 0);
        if (dec == null) return;
        dec.decrementarOcurrencia();
        if (dec.getOcurrencia() == 0) esborrarParaula(dec);
    }
}
