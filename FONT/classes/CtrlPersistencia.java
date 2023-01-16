package classes;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.nio.file.Paths;
import java.nio.file.Files;

/** Classe controladora de la capa de persistencia.
 * @author Bernat Borràs Civil (bernat.borras.civil@estudiantat.upc.edu)
 */
public class CtrlPersistencia {

    /**Controlador de domini. */
    CtrlDomini domini;

    /**Path del directori DATA. */
    final String PATH;

    /**
     * Constructora per defecte.
     * @param controladorDomini Controlador de la capa de domini.
     */
    public CtrlPersistencia(CtrlDomini controladorDomini) {
        domini = controladorDomini;
        PATH = new String("../DATA/");
    }
    
    /**
     * Importa un fitxer a l'aplicació.
     * @param path path del document.
     */
    public void importFile(String path) {
        File f = new File(path);
        if (getExtension(path).equals("xml")) importXML(f);
        else if (getExtension(path).equals("yay")) importYAY(f);
        else importTXT(f);
    }

    /**
     * Retorna l'extensió d'un fitxer.
     * @param path path del document.
     * @return extensió del document.
     */
    private String getExtension(String path) {
        String extension = new String("");
        for (int i = path.length() - 1; i >= 0; --i) {
            char c = path.charAt(i);
            if (c == '.') break;
            extension = c + extension;
        }
        return extension;
    }

    /**
     * Importa un fitxer TXT a l'aplicació.
     * @param f fitxer a importar.
     */
    private void importTXT(File f) {
        try {
            Scanner s = new Scanner(f);
            String title = s.nextLine();
            String author = s.nextLine();
            ArrayList<String> content = new ArrayList<>();
            String plain_content = new String();
            while (s.hasNextLine()) {
                String line = s.nextLine();
                content.add(line);
                plain_content = plain_content + "\n" + line;
            }
            s.close();
            if(domini.getDocument(author, title).getR()) {
                CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": Ja existeix un document amb aquest títol i autor.");
                return;
            }
            domini.crearDocument(title, author, content, plain_content, LocalDate.now(), false);
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": error de lectura.");
        }
    }

    /**
     * Transforma una string a un arraylist delimitats per salts de línia.
     * @param s contingut a separar.
     * @return : ArrayList<String> contingut separat per salts de línia
     */
    private ArrayList<String> stringToArrayList(String s) {
        return new ArrayList<>(Arrays.asList(s.split("\n")));
    }

    /**
     * Importa un fitxer XML a l'aplicació.
     * @param f fitxer a importar.
     */
    private void importXML(File f) {
        try {
            Scanner s = new Scanner(f);
            String file = new String("");
            while (s.hasNextLine()) file += s.nextLine()  + '\n';;
            s.close();

            String author = new String("");
            String title = new String("");
            LocalDate date = LocalDate.now();
            String plain_content = new String();
            Boolean favourite = false;
            int i = 0;
            while (i < file.length()) {
                if (file.charAt(i) != '<') {
                    ++i;
                    continue;
                }
                //Llegir tag
                String tag = new String("");
                ++i;
                while (file.charAt(i) != '>') {
                    tag += file.charAt(i);
                    ++i;
                }
                ++i;
                if (tag.equals("document") || tag.equals("/document")) continue;

                //Llegir string contingut
                String contingut = new String("");
                while (file.charAt(i) != '<') {
                    contingut += file.charAt(i);
                    ++i;
                }

                while (file.charAt(i) != '>') ++i;
                
                if (tag.equals("title")) title = contingut;
                else if (tag.equals("author")) author = contingut;
                else if (tag.equals("content")) plain_content = new String(contingut);
                else if (tag.equals("date")) date = stringToDate(contingut);
                else if (tag.equals("bool name=\"favourite\"") && contingut.equals("true")) favourite = true;
            }
            if(domini.getDocument(author, title).getR()) {
                CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": Ja existeix un document amb aquest títol i autor.");
                return;
            }
            domini.crearDocument(title, author, stringToArrayList(plain_content), plain_content, date, favourite);
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": error de lectura.");
        }
    }

    /**
     * Converteix la data en format string a LocalDate.
     * @param date data en format string.
     * @return LocalDate : data en format LocalDate.
     */
    private LocalDate stringToDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException e){
            CtrlDomini.mostraError("S'ha produit un error al traduir la data " + date + ". Aquesta data no existeix, o bé està en un format incorrecte.");
        }
        return null;
    }

    /**
     * Importa un fitxer YAY a l'aplicació.
     * @param f fitxer a importar.
     */
    private void importYAY(File f) {
        try {
            Scanner s = new Scanner(f);
            String file = new String("");
            while (s.hasNextLine()) file += s.nextLine() + '\n';
            s.close();
            
            String author = new String("");
            String title = new String("");
            String plain_content = new String();
            LocalDate date = LocalDate.now();
            Boolean favourite = false;
            int i = 0;
            while (i < file.length()) {
                if (file.charAt(i) != '#') {
                    ++i;
                    continue;
                }
                //Llegir tag
                String tag = new String("");
                ++i;
                while (file.charAt(i) != ':') {
                    tag += file.charAt(i);
                    ++i;
                }
                ++i;
                //Llegir string contingut
                String contingut = new String("");
                while (file.charAt(i) != '#') {
                    contingut += file.charAt(i);
                    ++i;
                }
                ++i;
                if (tag.equals("TITLE")) title = contingut;
                else if (tag.equals("AUTHOR")) author = contingut;
                else if (tag.equals("CONTENT")) plain_content = new String(contingut);
                else if (tag.equals("DATE")) date = stringToDate(contingut);
                else if (tag.equals("FAVOURITE") && contingut.equals("True")) favourite = true;
            }
            if(domini.getDocument(author, title).getR()) {
                CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": Ja existeix un document amb aquest títol i autor.");
                return;
            }
            domini.crearDocument(title, author, stringToArrayList(plain_content), plain_content, date, favourite);
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": error de lectura.");
        }
    }

    /**
     * Exporta un document.
     * @param title Títol a imprimir.
     * @param author Autor a imprimir.
     * @param content Contingut a imprimir.
     * @param date Data a imprimir.
     * @param preferit Marcat com a preferit.
     * @param path path del document.
     */
    public void export(String title, String author, String content, LocalDate date, boolean preferit, String path) {
        Thread thread1 = new Thread();
        thread1.start();
        if (getExtension(path).equals("xml")) exportXML(title, author, content, date, preferit, path);
        else if (getExtension(path).equals("yay")) copiarYAY(title, author, path);
        else exportTXT(title, author, content, path);
    }

    /**
     * Exporta un document txt.
     * @param title Títol a imprimir.
     * @param author Autor a imprimir.
     * @param content Contingut a imprimir.
     * @param path path del document.
     */
    private void exportTXT(String title, String author, String content, String path) {
        try {
            FileWriter f = new FileWriter(path);
            f.write(title + "\n");
            f.write(author + "\n");
            f.write(content + "\n");
            f.close();
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al exportar el fitxer " + path + ": error d'escriptura.");
        }
    }

    /**
     * Exporta un document xml.
     * @param title Títol a imprimir.
     * @param author Autor a imprimir.
     * @param content Contingut a imprimir.
     * @param date Data a imprimir.
     * @param preferit Marcat com a preferit.
     * @param path path del document.
     */
    private void exportXML(String title, String author, String content, LocalDate date, Boolean preferit, String path) {
        try {
            FileWriter f = new FileWriter(path);
            f.write("<document>\n");
            f.write("<title>" + title + "</title>\n");
            f.write("<author>" + author + "</author>\n");
            f.write("<date>" + date + "</date>\n");
            if (preferit) f.write("<bool name=\"favourite\">true</bool>\n");
            else f.write("<bool name=\"favourite\">false</bool>\n");
            f.write("<content>\n");
            f.write(content + "\n");
            f.write("</content>\n");
            f.write("</document>\n");
            f.close();
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al exportar el fitxer " + path + ": error d'escriptura.");
        }
    }

    /**
     * Copia un fitxer del directori DATA/Documents al path indicat
     * @param title títol del document
     * @param author autor del document
     * @param path path destinació del fitxer
     */
    private void copiarYAY(String title, String author, String path) {
        try {
            Files.copy(Paths.get(getPathDoc(title, author)), Paths.get(path));
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al exportar el fitxer " + path + ": error al copiar el document.");
        }
    }

    /**
     * Importa una expressió booleana a l'applicació.
     * @param f fitxer de l'expressió.
     */
    private void importarExpressio(File f) {
        try {
            Scanner s = new Scanner(f);
            String file = new String("");
            while (s.hasNextLine()) file += s.nextLine();
            s.close();
            
            String name = new String("");
            String expression = new String("");
            int i = 0;
            while (i < file.length()) {
                if (file.charAt(i) != '#') {
                    ++i;
                    continue;
                }
                //Llegir tag
                String tag = new String("");
                ++i;
                while (file.charAt(i) != ':') {
                    tag += file.charAt(i);
                    ++i;
                }
                ++i;
                //Llegir string contingut
                String contingut = new String("");
                while (file.charAt(i) != '#') {
                    contingut += file.charAt(i);
                    ++i;
                }
                ++i;
                if (tag.equals("NAME")) name = contingut;
                else if (tag.equals("EXPRESSION")) expression = contingut;
            }
            domini.novaEB(name, expression);
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": error de lectura.");
        }
    }

    /**
     * Importa tots els documents i expressions booleanes desades a l'applicació.
     */
    public void importarDades() {
        File dataDirectory = new File(PATH);
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
            File doc_folder = new File(PATH + "Documents/");
            doc_folder.mkdir();
            File exp_folder = new File(PATH + "Expressions/");
            exp_folder.mkdir();
            return;
        }
        File doc_folder = new File(PATH + "Documents/");
        File[] documents = doc_folder.listFiles();

        if (documents != null) for (File d : documents) importYAY(d);

        File exp_folder = new File(PATH + "Expressions/");
        File[] expressions = exp_folder.listFiles();
        
        if (expressions != null) for (File e : expressions) importarExpressio(e);
    }
    
    /**
     * Retorna el path del document.
     * @param title Titol del document.
     * @param author Autor del document
     * @return String : Path del document.
     */
    private String getPathDoc(String title, String author) {
        return new String(PATH + "Documents/" + title + "_" + author + ".yay");
    }

    /**
     * Retorna el path de l'expressio booleana.
     * @param nom_expressio Nom de l'expressió boobleana.
     * @return String : Path de l'expressió.
     */
    private String getPathExp(String nom_expressio) {
        return new String(PATH + "Expressions/" + nom_expressio + ".yae");
    }

    /**
     * Crea un fitxer nou per al document creeat, o sobreescriu si ja existeix al directori DATA/Documents.
     * @param title Títol.
     * @param author Autor.
     * @param content Contingut.
     * @param date Data.
     * @param preferit Marcat com a preferit.
     */
    public void crearDocument(String title, String author, String content, LocalDate date, Boolean preferit) {
        try {
            FileWriter f = new FileWriter(getPathDoc(title, author));
            f.write("#TITLE:" + title + "#\n");
            f.write("#AUTHOR:" + author + "#\n");
            f.write("#DATE:" + date + "#\n");
            if (preferit) f.write("#FAVOURITE:True#\n");
            else f.write("#FAVOURITE:False#\n");
            f.write("#CONTENT:");
            f.write(content + "#\n");
            f.close();
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al exportar el fitxer " + getPathDoc(title, author) + ": error d'escriptura.");
        }
    }


    /**
     * Esborra el document del directori DATA/Documents.
     * @param title Títol.
     * @param author Autor.
     */
    public void esborrarDocument(String title, String author) {
        File f = new File(getPathDoc(title, author));
        f.delete();
    }

    /**
     * Crea un fitxer nou per a l'expressió creada al directori DATA/Expressions.
     * @param nom Nom de l'expressió boobleana.
     * @param expressio Expressió booleana
     */
    public void crearExpressio(String nom, String expressio) {
        try {
            FileWriter f = new FileWriter(getPathExp(nom));
            f.write("#NAME:" + nom + "#\n");
            f.write("#EXPRESSION:" + expressio + "#\n");
            f.close();
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al exportar el crear l'expressió booleana " + nom + ": error d'escriptura.");
        }
    }

    /**
     * Esborra el fitxer de l'expressió creada del directori DATA/Expressions.
     * @param nom Nom de l'expressió boobleana.
     */
    public void esborrarExpressio(String nom) {
        File f = new File(getPathExp(nom));
        f.delete();
    }

    /**
     * Retorna el contingut d'un document identificat pel títol i autor.
     * @param title Títol del document.
     * @param author Autor del contingut
     * @return String : contingut del document.
     */
    public String getContingut(String title, String author) {
        File f = new File(getPathDoc(title, author));
        try {
            Scanner s = new Scanner(f);
            String file = new String("");
            while (s.hasNextLine()) file += s.nextLine() + "\n";
            s.close();

            int i = 0;
            while (i < file.length()) {
                if (file.charAt(i) != '#') {
                    ++i;
                    continue;
                }
                //Llegir tag
                String tag = new String("");
                ++i;
                while (file.charAt(i) != ':') {
                    tag += file.charAt(i);
                    ++i;
                }
                ++i;
                if (tag.equals("CONTENT")) {
                    //Llegir contingut
                    String contingut = new String("");
                    while (file.charAt(i) != '#') {
                        contingut += file.charAt(i);
                        ++i;
                    }
                    return contingut;
                }
                else {
                    while (file.charAt(i) != '#') ++i;
                }
                ++i;
            }
        }
        catch(Exception e) {
            CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": error de lectura.");
        }
        CtrlDomini.mostraError("S'ha produit un error al importar el fitxer " + f.getAbsolutePath() + ": no s'ha trobat el contingut del fitxer.");
        return null;
    }
}
