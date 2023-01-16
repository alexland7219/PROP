package classes;
import java.util.*;

/** Node
* @author Salvador Moya Bartolome (salvador.moya@estudiantat.upc.edu)
*/
public class Node{
    String data;
    Node left,right;
    
    public Node(){}
    
     public Node(String data){
        this.data = data;
        left = right = null;
    }
    
}
