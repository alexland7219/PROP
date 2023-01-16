package classes;
import java.util.*;

public class ExpressionTree {	

    public static String AND = "&";
    public static String OR = "|";
    public static String NOT = "!";

    Stack <String> Operands = new Stack<>();
    List <String> postfix = new Stack<>();

    public static Map<String, Integer> precedencemap = new HashMap<>();
    static{
            precedencemap = new HashMap<>();
            precedencemap.put(AND , 2);
            precedencemap.put(OR, 2);
            precedencemap.put(NOT, 1);
    }

    public Boolean isOperator(String current){
        return precedencemap.containsKey(current);

    }
    
    public Node GeneraTree (String cos){
        List <String> postfix = new Stack<>();
        postfix = cos2postfix(cos);
        return list2tree(postfix);
    }

   public List<String> cos2postfix (String cos){
        String lastOp = null;
        String[] input = cos.split(" ");

        String current;
       
        for (Integer i = 0; i < input.length; ++i)
        {
            current = input[i];
            
            if (isOperator(current)){
                if (Operands.empty()) Operands.push(current);
                
                else if (Operands.peek().endsWith("(")){
                
                Operands.push(current);
                
                }
                else if  (precedencemap.get(Operands.peek()) >= precedencemap.get(current)){

                Operands.push(current);
                }
                else {
                    postfix.add(Operands.pop());
                    Operands.push(current);
                
                }
            }
            else if (current.endsWith("(")) {
               
                Operands.push(current);
            }
            else if (current.endsWith(")")){
                while (!Operands.peek().endsWith("(")){
                    postfix.add(Operands.pop());
                    
                }
            Operands.pop();
            }
            else postfix.add(current);
           
    }
        while(!Operands.empty()){
            postfix.add(Operands.pop());
        }
        return postfix;

    }
    public  Node list2tree (List <String> postfix){
        Stack<Node> stackNodes = new Stack<Node>();
        Node temp, t1, t2;
        ListIterator<String> iter = postfix.listIterator();
        while (iter.hasNext()){
        String current = iter.next();            
            //Per a crear una fulla conjunt de paraules
            if(current.charAt(0) == '{'){

                while (current.charAt(current.length()-1) != '}'){
                    current+=" "+iter.next();
                }
                
                temp = new Node(current);
                stackNodes.push(temp);
            }
            
            //Per a crear una fulla sequencia
            else if(current.charAt(0) == '"'){
                
                while (current.charAt(current.length()-1) != '"'){
                    current+= " " + iter.next();
                    System.out.print(current);
                }
                temp = new Node(current);
                stackNodes.push(temp);
            }
        
            
            //Per a crear un node fulla d'una sola paraula
            else if(!isOperator(current)){
            
                temp = new Node(current);
                stackNodes.push(temp);
            }
            
            //Per a crear un node pare amb els fills que li pertoquen depenent del operand
            else{
                temp = new Node(current);
 
                if (current.charAt(0)!= '!'){
                
                    t1 = stackNodes.pop();
                    temp.right = t1;
                }
                
                t2 = stackNodes.pop();
                temp.left = t2;
                stackNodes.push(temp);
            }
        }
        temp = stackNodes.pop();
        return temp;
    }

}
