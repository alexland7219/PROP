package classes;
import java.util.Set;
import java.util.HashSet;


/** Estructura de dades per contenir el diccionari de paraules.
 * @author Bernat Borràs Civil (bernat.borras.civil@estudiantat.upc.edu)
 */
public class TernaryTree<T> {

    /**Node fill de l'esquerra. */
    TernaryTree<T> esquerra;

    /**Node fill de la dreta. */
    TernaryTree<T> dreta;

    /**Node fill del centre. */
    TernaryTree<T> centre;

    /**Apuntador de la paraula que el node fa referència. */
    T contingut;

    /** Lletra que conté el node. */
    char lletra;

    /**Constructora per defecte de TernaryTree. */
    public TernaryTree() {
        esquerra = null;
        dreta = null;
        centre = null;
        contingut = null;
        lletra = ' ';
    }

    /**Constructora per als nodes fills.
     * @param c lletra que representa el node nou.
     */
    private TernaryTree(char c) {
        esquerra = null;
        dreta = null;
        centre = null;
        contingut = null;
        lletra = c;
    }

    /**Obtenir el contingut que correspon a la seqüència s.
     * @param s seqüencia de caràcters que forma la clau.
     * @param i index de la paraula des d'on falta fer la cerca.
     * @return T : Contingut.
     */
    public T obtenir(String s, int i) {
        if (s == "") return null;
        if (s.charAt(i) > lletra && dreta != null) return dreta.obtenir(s, i);
        if (s.charAt(i) < lletra && esquerra != null) return esquerra.obtenir(s, i);
        if (s.charAt(i) == lletra) {
            if (i < s.length() - 1 && centre != null) return centre.obtenir(s, i + 1);
            if (i == s.length() - 1) return contingut;
        }
        return null;
    }

    /**Obtenir tots els valors que conté el subarbre.
     * @param resultat valors de l'arbre que contenen el prefix s.
     */
    private void recorreArbre(Set<T> resultat) {
        if (contingut != null) resultat.add(contingut);
        if (dreta != null) dreta.recorreArbre(resultat);
        if (centre != null) centre.recorreArbre(resultat);
        if (esquerra != null) esquerra.recorreArbre(resultat);
    }

    /**Obtenir el contingut que correspon conté el prefix s.
     * @param s seqüencia de caràcters que forma el prefix.
     * @param i index del prefix des d'on falta fer la cerca.
     * @param resultat valors de l'arbre que contenen el prefix s.
     */
    private void obtenirValors(Set<T> resultat, String s, int i) {
        if (s == "") return;
        if (s.charAt(i) > lletra && dreta != null) dreta.obtenirValors(resultat, s, i);
        if (s.charAt(i) < lletra && esquerra != null) esquerra.obtenirValors(resultat, s, i);
        if (s.charAt(i) == lletra) {
            if (i < s.length() - 1 && centre != null) centre.obtenirValors(resultat, s, i + 1);
            if (i == s.length() - 1) recorreArbre(resultat);
        }
    }

    /**Obtenir el contingut que correspon conté el prefix s.
     * @param s seqüencia de caràcters que forma el prefix.
     * @return Set<T> : Valors de l'arbre que contenen el prefix s.
     */
    public Set<T> obtenirPerPrefix(String s) {
        Set<T> resultat = new HashSet<>();
        obtenirValors(resultat, s, 0);
        return resultat;
    }

    /**Obtenir el contingut que correspon a la seqüència s, el crea si no existeix aquest.
     * @param s seqüencia de caràcters que forma la clau.
     * @param i index de la paraula des d'on falta fer la cerca.
     * @return T : Contingut.
     */
    public T inserirObtenir(String s, int i, T cont) {
        if (s.length() <= 0) return null;
        if (lletra == ' ') lletra = s.charAt(i);
        if (s.charAt(i) == lletra) {
            if (i == s.length() - 1) {
                if (contingut == null) contingut = cont;
                return contingut;
            }
            if (centre == null) centre = new TernaryTree<T>(s.charAt(i + 1));
            return centre.inserirObtenir(s, i + 1, cont);
        }
        else if (s.charAt(i) > lletra) {
            if (dreta == null) dreta = new TernaryTree<T>(s.charAt(i));
            return dreta.inserirObtenir(s, i, cont);
        }
        else {
            if (esquerra == null) esquerra = new TernaryTree<T>(s.charAt(i));
            return esquerra.inserirObtenir(s, i, cont);
        }
    }

        /**Esborra el corresponent a s juntament amb els nodes innecessaris.
     * @param s seqüencia de caràcters que forma la clau.
     * @param i index de la paraula des d'on falta fer la cerca.
     * @param esborrable node pare des d'on es pot esborrar l'arbre.
     * @param dir direcció del node fill que es pot esborrar.
     */
    public void esborrar(String s, int i, TernaryTree<T> esborrable, int dir) {
        if (s.charAt(i) > lletra && dreta != null) {
            if (centre != null || esquerra != null || contingut != null) {
                esborrable = this;
                dir = 2;
            }
            dreta.esborrar(s, i, esborrable, dir);
        }
        else if (s.charAt(i) < lletra && esquerra != null) {
            if (dreta != null || centre != null || contingut != null) {
                esborrable = this;
                dir = 0;
            }
            esquerra.esborrar(s, i, esborrable, dir);
        }
        else if (s.charAt(i) == lletra) {
            if (i < s.length() - 1 && centre != null) {
                if (dreta != null || esquerra != null || contingut != null) {
                    esborrable = this;
                    dir = 1;
                }
                centre.esborrar(s, i + 1, esborrable, dir);
            }
            if (i == s.length() - 1 && contingut != null) {
                if (esborrable == null || centre != null) contingut = null;
                else {
                    if (dir == 0) esborrable.esquerra = null;
                    else if (dir == 1) esborrable.centre = null;
                    else esborrable.dreta = null;
                }
            }
        }
    }

}
