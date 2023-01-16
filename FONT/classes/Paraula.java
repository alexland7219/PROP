package classes;


/** Paraula que pot ser continguda a algun document.
 * @author Bernat Borràs Civil (bernat.borras.civil@estudiantat.upc.edu)
 */
public class Paraula {

    /** Mot repressentat per la classe. */
    private String mot;

    /** Número d'ocurrencies. */
    private int ocurrencia;

    /** Identificador de la paraula. */
    private int index;

    /** Pròxim identificador a assignar. */
    private static int proxim_index = 0;

    /** Constructora per defecte de paraula.
     *  @param p Paraula representada per la classe.
    */
    public Paraula(String p) {
        mot = p;
        ocurrencia = 0;
        index = proxim_index;
        ++proxim_index;
    }

    /** Retorna el nombre d'ocurrencies total.
     * @return int : Nombre d'ocurrencies.
     */
    public int getOcurrencia() {
        return ocurrencia;
    }

    /** Retorna la paraula representada per la classe.
     * @return String : Paraula.
     */
    public String getParaula() {
        return mot;
    }

    /** Decrementa en una unitat el nombre d'ocurrencies.*/
    public void decrementarOcurrencia() {
        ocurrencia--;
        if (ocurrencia < 0) ocurrencia = 0;
    }

    /** Incrementa en una unitat el nombre d'ocurrencies. */
    public void incrementarOcurrencia() {
        ocurrencia++;
    }

    /** Retorna l'índex de la paraula.
     * @return int : índex de la paraula.
     */
    public int getId() {
        return index;
    }
}
