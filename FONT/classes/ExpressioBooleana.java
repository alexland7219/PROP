package classes;
import java.util.*;

/** ExpressioBooleana
* @author Salvador Moya Bartolome (salvador.moya@estudiantat.upc.edu)
*/
public class ExpressioBooleana{

    
    private String cos;
    private String nom;
    private Node root;
    private Set<Document> resultat;

    

    public String getNom (){
        return nom;
    }

    public void setNom (String setnom){
        nom = setnom;
    }

    public String getCos (){
        return cos;
    }

    public void setCos (String setcos){
        cos = setcos;
        ExpressionTree Et = new ExpressionTree();
        root = Et.GeneraTree(cos);
    }
    
    public Node getRoot (){
        return root;
    }
    
    public void setRoot (Node n){
        root = n;
    }

    public Set<Document> getResultat(Llibreria l){
        resultat = ConsultaBooleana (l, root);
        return resultat;
    }
    
    public  ExpressioBooleana (String nom, String cos){
    
        this.setCos(cos);
        this.setNom(nom);
        ExpressionTree Et = new ExpressionTree();
        this.setRoot(Et.GeneraTree(cos));
    }
    
    public  ExpressioBooleana ExpressioBooleana (String nom, String cos){
    
        this.setCos(cos);
        this.setNom(nom);
        ExpressionTree Et = new ExpressionTree();
        this.setRoot(Et.GeneraTree(cos));
        return this;
    }
    
    public   ExpressioBooleana (String cos){
        
        this.setCos(cos);
        ExpressionTree Et = new ExpressionTree();
        this.setRoot(Et.GeneraTree(cos));
    }
    
    public  ExpressioBooleana (){
        
        this.cos = null;
        this.nom = null;
        this.setRoot(new Node());
    }
    
    public ExpressioBooleana getExpressioBooleana(){
    
        return this;
    }
    
    public Set<Document> ConsultaBooleana (Llibreria l, Node consulta){
        
        Set<Document> resultats = new HashSet<>();
        Set<Document> tots = l.getSetDocuments();
        resultats = ConsultaBooleanaRec(consulta, tots);
        return resultats;
    }
    
    public Boolean ConteParaula (String query, Document doc){
    
        Boolean b = doc.conteSequencia(query);
        return b;

    }
    public Set<Document> ConsultaBooleanaRec (Node consulta, Set<Document> tots ){
        
        Set<Document>  resultats = new HashSet<>();
        String query = consulta.data;
        Set<Document>  temp;
        
        if(query.charAt(0) == '!'){
            temp = tots;
            temp.removeAll(ConsultaBooleanaRec(consulta.left, tots));
            resultats = temp;
        }
        
        else if (query.charAt(0) == '|'){
            temp = ConsultaBooleanaRec(consulta.left, tots);
            temp.addAll(ConsultaBooleanaRec(consulta.right, tots));
            resultats = temp;
        }
        
        else if (query.charAt(0) == '&'){
            temp = ConsultaBooleanaRec(consulta.left, tots);
            temp.retainAll(ConsultaBooleanaRec(consulta.right, tots));
            resultats = temp;
        }
        
        else if (query.charAt(0) == '{'){
            Iterator docs = tots.iterator();
            query = query.substring(1, (query.length()-2));
            String queries[] = query.split(" ");
            Document current; 
            while (docs.hasNext()){
                current = (Document) docs.next();
                for (int i = 0; i < queries.length; ++i){
                    if (ConteParaula(queries[i], current)) {
                        resultats.add(current);
                    }
                }
            }
        }
        
        else if (query.charAt(0) == '"'){
            Iterator docs = tots.iterator();
            query = query.substring(1, (query.length()-1));
            Document current;
            while (docs.hasNext()){
                current = (Document) docs.next();
                if (current.conteSequencia(query))
                    resultats.add(current);
            }
        }
        
        else {
        
            Iterator docs = tots.iterator(); 
            Document current;
            while (docs.hasNext()){
                current = (Document) docs.next();
                if (ConteParaula(query,current)) {
                    resultats.add(current);}
                }
            }
            
        return resultats;
    }
}
