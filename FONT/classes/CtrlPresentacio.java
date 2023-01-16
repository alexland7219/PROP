package classes;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Set;

/**
 * Classe que inicialitza i controla la capa de presentació.
 * @author Alexandre Ros i Roger (alexandre.ros.roger@estudiantat.upc.edu)
 */
public class CtrlPresentacio {

    CtrlDomini CD;
    esquema ESQ;
    PopupError ERR;

    /** Constructora del CtrlPresentacio.
     * 
     * @param cd Referència al CtrlDomini.
     * @param esq Referència al Esquema (capa més superficial)
     * @param err Referència al PopupError (classe que proporciona missatges d'error)
     */
    private CtrlPresentacio(CtrlDomini cd, esquema esq, PopupError err)
    {
        CD = cd; ESQ = esq; ERR = err;
    }

    /** Mètode que crea un document.
     * 
     * @param title Títol del document a crear
     * @param author Autor del document a crear
     * @param contingut Contingut del document a crear
     */
    public void nouDocument(String title, String author, String contingut){

        if (CD.docExists(title, author)){
            mostraError("Document already exists!");
            return;
        }


        String[] contarray = contingut.split("\\p{Punct}");
        ArrayList<String> content = new ArrayList<String>(Arrays.asList(contarray));

        //for (int i = 0; i < content.size(); ++i){
        //    System.out.println(content.get(i));
        //}

        CD.crearDocument(title, author, content, contingut, LocalDate.now(), false);

        System.out.println("\nDocument successfully added!");

    }

    /** Mètode que elimina un document
     * 
     * @param titol Títol del document a eliminar
     * @param autor Autor del document a eliminar
     */
    public void eliminarDoc(String titol, String autor){
        CD.eliminarDocument(titol, autor);
    }

    /** Mètode per a exportar un document
     * 
     * @param titol Títol del document a exportar.
     * @param autor Autor del document a exportar.
     * @param ext Número de l'extensió a exportar (0=txt 1=xml 2=yay)
     * @param fname Nom del fitxer per a crear
     */
    public void exporta(String titol, String autor, int ext, String fname){
        CD.exportarDocument(titol, autor, ext, fname);
    }

    /** Mètode que importa un document donat el path
     * 
     * @param path Path absolut del document a importar.
     */
    public void importa(String path){
        CD.importFile(path);
    }

	/** Mètode públic que implementa la consulta per Semblança de documents.
	 * 
	 * @param titol Títol del document referència per a cercar.
	 * @param autor Autor del document referència per a cercar.
	 * @param ndocs Nombre de documents a retornar (String)
	 * @param mode Mode de Consulta per Semblança (0=tf-idf, 1=ocurrències).
	 * @return Una ArrayList amb els preview dels <i>n</i> documents més semblants al referència.
	 */
    public ArrayList<String> consultaSemb(String titol, String autor, String ndocs, int mode){

        Integer N;

        try {
            N = Integer.parseInt(ndocs);
        } catch (Exception e){
            System.out.println(e);
            return new ArrayList<>();
        }


        return CD.consultaSemb(titol, autor, N, mode);
    }

    /** Mètode que implementa la consulta d'un sol document.
     * 
     * @param autor Autor del document a cercar
     * @param titol Titol del document a cercar
     * @return Una llista que conté l'autor, el títol, el booleà preferit ("Y"/"N"), la data i el contingut, tots en String i en aquest ordre.
     */
    public ArrayList<String> consultaDocument(String autor, String titol){
        // La "consultaDocument" retornarà:
        // Autor + Titol + Booleà ("Y"/"N") preferit + data en ISO + Contingut en string
        if (!CD.docExists(titol, autor)){
            mostraError("Document doesn't exist");
            return new ArrayList<>();
        }

        ArrayList<String> myList = new ArrayList<>();

        myList.add(autor); myList.add(titol);
        boolean isFav = CD.getFavourite(titol, autor);

        if (isFav) myList.add("Y");
        else myList.add("N");

        myList.add(CD.getData(titol, autor).toString());
        
        myList.add(CD.getContingut(titol, autor));

        return myList;
    }

	/** Mètode públic que implementa la consulta d'Autors per Prefix.
	 * 
	 * @param consulta Prefix amb el qual cercar els autors
	 * @return Una llista d'Strings que contindrà tots els autors començant pel prefix.
	 */
    public ArrayList<String> consultaAutor(String consulta){
        Set<String> mySet = CD.donaAutors(consulta);
        ArrayList<String> listauthors = new ArrayList<String>();
        for (String s : mySet) listauthors.add(s);

        return listauthors;
    }

	/** Mètode públic que implementa la consulta Avançada
	 * 
	 * @param query Cadena de caràcters a cercar.
	 * @param criteri Criteri d'ordenació pel qual ordenar els documents resultants.
	 * @return Una ArrayList amb els preview dels documents que contenen la seqüència <i>seq</i>
	 */
    public ArrayList<String> consultaAvancada(String query, int criteri){
        return CD.consultaSeq(query, criteri);
    }

    /** Mètode públic que implementa la consulta dels Títols d'un Document.
	 * 
	 * @param autor Autor dels documents a cercar.
	 * @param criteri Criteri d'ordenació pel qual ordenar els documents resultants.
	 * @return Una ArrayList amb els preview dels documents de l'autor <i>autor</i>
	 */

    public ArrayList<String> consultaTit(String autor, int criteri){
        return CD.consultaTit(autor, criteri);
    }

    /** Mètode públic que implementa la consulta per Rellevància de documents
	 * 
	 * @param query String amb totes les paraules a cercar amb espais.
	 * @param ndocs Nombre de documents a retornar.
	 * @param firstMode Booleà que ens indica si fer servir el mètode 1 de rellevància (true) o el 2 (false).
	 * @return Una ArrayList amb els preview dels <i>k</i> documents més rellevants segons la query.
	 */

    public ArrayList<String> consultaRell(String ndocs, String query, Boolean firstMode){
        Integer N;

        try {
            N = Integer.parseInt(ndocs);
        } catch (Exception e){
            System.out.println(e);
            return new ArrayList<>();
        }

        if (firstMode) return CD.consultaRell(query, N, 1);
        else return CD.consultaRell(query, N, 2);
    }

    /** Mètode públic que retorna tots els documents.
	 * 
	 * @return Una ArrayList amb els preview de tots els documents.
	 */

    public ArrayList<String> getAllDocs(){
        return CD.getAllDocs();
    }

	/** Mètode públic per a crear una Expressió Booleana.
	 * 
	 * @param nom Nom de l'EB a crear
	 * @param cos Cos de l'EB a crear
	 */
    public void novaEB(String cos, String nom){
        CD.novaEB(nom, cos);
    }

    /** Mètode públic per a retornar el cos d'una Expressió Booleana.
	 * 
	 * @param name Expressió Booleana a cercar.
	 * @return String del cos de l'EB "name".
	 */
    public String getCos(String nom){
        return CD.getCos(nom);
    }

    /** Mètode públic per a modificar el cos d'una Expressió Booleana.
	 * 
	 * @param nom Nom de l'EB a modificar
	 * @param noucos Nou cos de l'EB
	 */

    public void canviaEB(String nom, String noucos){
        CD.canviarEB(nom, noucos);
    }

    /** Mètode públic per a eliminar una Expressió Booleana.
	 * 
	 * @param nom Nom de l'EB a eliminar
	 */
    public void eliminaEB(String nom)
    {
        CD.eliminarEB(nom);
    }

    /** Mètode públic que implementa la Consulta per Expressió Booleana
	 * 
	 * @param cos Cos de l'expressió booleana directa.
	 * @param nom Nom de l'expressió booleana emmagatzemada.
	 * @param mode Mode de consulta (0=per expressió amb nom, 1=expressió directa amb cos)
	 * @param criteria Criteri d'ordenació dels documents resultants.
	 * @return Una ArrayList dels documents que compleixin l'expressió booleana.
	 */
    public ArrayList<String> consultaEB(String cos, String nom, int mode, int criteria)
    {
        return CD.consultaEB(cos, nom, mode, criteria);
    }

    /** Mètode públic que implementa la consulta per Data.
	 * 
	 * @param ant Límit inferior de les dates (yyyy-mm-dd).
	 * @param post Límit superior de les dates (yyyy-mm-dd).
     * @param option String que codifca quin dels tres mètodes fer servir ("P"=sense limit superior, "A"=sense inferior, "E"=amb els dos)
	 * @param criteri Criteri d'ordenació dels resultats
	 * @return Una ArrayList amb els preview dels documents creats entre ant i post.
	 */
    public ArrayList<String> consultaData(String ant, String post, String option, int criteri){
        LocalDate before = LocalDate.MIN;
        LocalDate after  = LocalDate.MAX;
        
        if (option == "E" || option == "A"){
            try {after = LocalDate.parse(post);}
            catch (Exception e){
                System.out.println(e);
                return new ArrayList<>();
            }
        }

        if (option == "E" || option == "P"){
            try {before= LocalDate.parse(ant);}
            catch (Exception e){
                System.out.println(e);
                return new ArrayList<>();
            }
        }

        return CD.consultaData(before, after, criteri);
    }

    /** Mètode públic que retorna totes les Expressions Booleanes guardades.
	 * 
	 * @return Una ArrayList dels noms de totes les EBs emmagatzemades.
	 */
    public ArrayList<String> getEBS(){

        return CD.getAllEBS();
    }

    /** Mètode que proporciona el poder de modificar qualsevol cosa d'un document
     * 
     * @param autor Autor del document a modificar
     * @param titol Titol del document a modificar
     * @param contingut Contingut modificat (sense canviar si no es vol modificar)
     * @param isFav Nou Booleà preferit del document
     * @param date Nova data del document
     * @param newautor Nou autor del document (sense canviar si no es vol modificar)
     * @param newtitol Nou títol del document (sense canviar si no es vol modificar)
     */
    public void modificar_general(String autor, String titol, String contingut, Boolean isFav, String date, String newautor, String newtitol){
        if (!CD.docExists(titol, autor)){
            mostraError("Document no existeix");
            return;
        }

        LocalDate dat = LocalDate.MIN;

        try {dat = LocalDate.parse(date);}
        catch (DateTimeParseException e){
            mostraError(date + " is not a valid date yyyy-mm-dd");
            return;
        }

        String oldcontent = CD.getContingut(titol, autor);

        if (!oldcontent.equals(contingut)){
            System.out.println("Has modificat el contingut");

            String[] contarray = contingut.split("\\p{Punct}");
            ArrayList<String> content = new ArrayList<String>(Arrays.asList(contarray));
    
            CD.modificarContingut(titol, autor, content, contingut);
        }

        CD.modificarData(titol, autor, dat);

        // Modificar preferit
        if (isFav != CD.getFavourite(titol, autor)) CD.togglePreferit(titol, autor);


        if (!autor.equals(newautor)){
            // S'ha modificat l'autor
            System.out.println("Modificació de l'autor");

            CD.modificarAutor(titol, autor, newautor);
        }

        if (!titol.equals(newtitol)){
            System.out.println("Modificació del titol.");
            CD.modificarTitol(titol, newautor, newtitol);
        }

        // Modificar data

        ESQ.update_document();

        System.out.println("Sha modificat tot correctament");
    }

	/** Mètode públic que implementa la cerca dels documents preferits.
	 * 
	 * @param criteria Criteri d'ordenació dels documents resultants.
	 * @return Una ArrayList amb els preview dels documents preferits.
	 */
    public ArrayList<String> consultaPref(int criteri){
        return CD.consultaPref(criteri);
    }

    /** Mètode que proporciona el poder de mostrar per pantalla un error
     * 
     * @param missatge String del missatge d'error
     */
    public void mostraError(String missatge) {
        ERR.SetMissatge(missatge);
        ERR.setSize(800, 200);
        ERR.setVisible(true);
    }

    /**
     *  Mètode que inicialitza l'esquema i realitza les tasques inicials del projecte,
     *  com són importar els documents i expressions d'una sessió anterior, i 
     *  inicialitzar l'Esquema
     */
    private void run(){
        ESQ.initComponents();
        ESQ.sendCP(this); // to be used with signals and slots
        JFrame mainframe = ESQ.retorna();
        mainframe.setSize(900,900);
        mainframe.setVisible(true);
        CD.importSaved();
        ESQ.update_doc_lists();
        ESQ.mostra_ebs_guardades(null);
    }

    /** Main del Projecte. El Main crea un nou CtrlDomini, un nou Esquema, un nou PopupError,
     *  li passa al CtrlPresentacio aquests tres i, després de linkar el CDomini amb el CPresentació i viceversa,
     *  comença l'execució amb el CtrlPresentacio.run().
     * 
     * @param args No estan implementats
     */
    public static void main(String[] args){
        CtrlDomini CD = new CtrlDomini();
        esquema ESQ = new esquema();
        PopupError ERR = new PopupError();

        CtrlPresentacio CP = new CtrlPresentacio(CD, ESQ, ERR);
        CD.setControladorPresentacio(CP);
        CP.run();
    }

}