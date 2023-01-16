package classes;
import java.util.ArrayList;
import java.util.HashMap;


/** Classe que representa un conjunt de paraules.
 * @author Oscar Ramos Nuñez (oscar.ramos.nunez@estudiantat.upc.edu)
 */
public class Frase {

    /** Conjunt de paraules de la frase. */
    private Paraula[] Oracio;


    /** Número de paraules de la frase. */
    private int n_paraules;


    /** Frase en un String. */
    private String text;

    /** Constructora per defecte. 
     * @return Frase inicialitzada amb un Array de Paraules i un String.
     * @param words
     * @param frase
     */
    public Frase(ArrayList<Paraula> words, String frase) {
        //paràmetre
        text = frase;
        n_paraules = words.size();
        Oracio = new Paraula[n_paraules];
        //posem les words
        for (int j = 0; j < words.size(); ++j) Oracio[j] = words.get(j);
    }

    /** Constructora amb Paraula[] i la frase en un string. 
     * @return Frase inicialitzada amb Paraula[] i un String.
     * @param words
     * @param frase
    */
    public Frase(Paraula[] words, String frase) {
        //paràmetre
        text = frase;
        n_paraules = words.length;
        Oracio = new Paraula[n_paraules];
        //posem les words
        Oracio = words;

    }

    /** Constructora nomes amb la frase en un string.
     * @return Frase inicialitzada amb un String.
     * @param frase
     */
    public Frase(String frase) {
        text = frase;
        ArrayList<Paraula> words = stringToParaules(frase);

        n_paraules = words.size();

        Oracio = new Paraula[n_paraules];
        //posem les words
        for (int j = 0; j < words.size(); ++j) Oracio[j] = words.get(j);

    }


    /** Retorna un vector de les paraules que formen la frase.
     * @return String[]
     */
    public Paraula[] getOracio() {
        return Oracio;
    }



    /** Retorna true si la frase conté les paraules "Paraules" concatenades (és a dir una darrera de l'altre).
     * @param Paraules
     * @return boolean
     */
    public boolean conteSequencia(String[] Paraules) {
        int it = 0;
        int tamany_sequencia = Paraules.length;
        for (int i = 0; i < n_paraules; ++i) {
            if ((Oracio[i].getParaula()).equals(Paraules[it])) {
                /* Hem trobat una word de Paraules en Oracio */
                ++it;
                /* Si era l'ultima ja hem acabat */
                if (it == tamany_sequencia) return  true;
            }
            else {
                /* Es trenca la sequencia. Cal mirar, però, si es reseteja a 0 o a 1 */
                if ((Oracio[i].getParaula()).equals(Paraules[0])) it = 1;
                else it = 0;
                
            }
        }
        return false;
    }


    /** Retorna true si la frase conté una seqüència de lletres concatenades.
     * @return bool 
     * @param lletres
     */
    public boolean conteCaracters(String lletres) {
        if (lletres.equals("")) {
            System.out.println("Sequencia empty");
            return true;
        }
        int it = 0;
        for (char c: text.toCharArray()) {
            if (c == lletres.charAt(it)) ++it;

            else if (c == lletres.charAt(0)) it = 1;
            else it = 0;

            if (it == lletres.length()) return true;
        }
        return false;
    }




    /** Retorna true si la frase conté la paraula passada per paràmetre. 
     * @param paraula
     * @return bool
     */
    public boolean conteParaula(String paraula) {
        for (Paraula p : Oracio) {
            if (p.getParaula().equals(paraula)) return true;
        }
        return false;

    }

    /** Retorna la frase en format String.
     * @return String
     */
    public String toString() {
        return text;
    }


    /** Retorna una HashMap<Integer,Integer>, amt tants elements com a paraules diferents té la frase; la key correspón amb l'index de la paraula i el value amb el número d'ocurrències d'aquesta en la frase.
     * @return HashMap
     */
    public HashMap<Integer, Integer> donaWords() {
        HashMap<Integer, Integer> q = new HashMap<Integer, Integer>();
        //parella id-n_aparicions
        HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
        Paraula actual; int id;
        for (int i = 0; i < n_paraules; ++i) {
            actual = Oracio[i];
            id = actual.getId();
            if (map.containsKey(id)) map.put(id, map.get(id) +1);
            else map.put(id, 1);
            
        }
        //leemos todas las pairs.

        for (Integer i : map.keySet()) {
            Integer nombre = map.get(i);
            q.put(i,nombre);
        }
        return q;

    }


    /** Retorna el nombre de paraules de la frase.
     * @return: int 
     */
    public int getNparaules() {
        return n_paraules;
    }


    /** Retorna true si c paràmetre és un signe de puntuació (d'entre uns que ens són d'interès identificar).
     * @param c
     * @return boolean
    */
    private boolean isPuntuacio(char c) {
        return c == '.' || c == ',' || c == ';' || c == ':' || c == '?' || c == '¿' || c == '!' || c == '¡' || c == '(' || c == ')' ||
         c == '{' || c == '}' || c == '[' || c == ']' || c == ' ';
    }

    /** Decomposa la frase en paraules en format String.
     * @param frase
     * @return ArrayList<String>
     */
    private ArrayList<String> decompose(String frase) {
        ArrayList<String> paraules = new ArrayList<>();
        String a_insert = "";

        for (char c: frase.toCharArray()) {
            if (isPuntuacio(c)) {
                if (a_insert.length() != 0) {
                    paraules.add(a_insert);
                    a_insert = "";
                }
            }
            else a_insert += c;
        }

        return paraules;
    }


    /** Donat un string que representa una frase dona les paraules d'aquesta en un ArrayList.
     * @param frase
     * @return ArrayList<Paraula>
     */
    private ArrayList<Paraula> stringToParaules(String frase) {
        ArrayList<String> words = decompose(frase);

        ArrayList<Paraula> paraules = new ArrayList<Paraula>();
        for (String s : words) {
            paraules.add(new Paraula(s));
        }
        return paraules;
    }
}