//package miniprojpkg;
package modified;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BSTNode 
{
    public String pageId;
    public int leftCount;
    public int rightCount;
    public Float rank;
    public static int level=0;
    public BSTNode left = null;
    public BSTNode right = null;
    public BSTNode parent = null;
    public static ArrayList pageIds = new ArrayList();
    public static HashMap pageIdsHash = new HashMap();
    public BSTNode()
    {
        super();
    }
    public BSTNode(String pageId, Float rank)
    {
        super();
        this.pageId = pageId;
        this.rank = rank;
        this.leftCount = 0;
        this.rightCount = 0;
    }
    public void insert(BSTNode node)
    {
        if (this.level >= 1000) 
        {
           return;    
        }
        if (this.level >= 500 && (node.rank==1 || node.rank == 2))
        {
           return;    
        }
//        System.out.println("Inserting " + node.rank + " level = " + this.level);
        if (node.rank < this.rank) 
        {
            if (this.right == null) 
            {
               this.right = node;    
            }
            else 
            {
                if (this.level <= 200)
                {
                   this.rightCount++;
                   this.level++;
                   this.right.insert(node);
                   this.level--;
                }
            }
        }
        else if (node.rank >= this.rank) 
        {
            if (this.pageId.equalsIgnoreCase(node.pageId) && this.rank == node.rank) 
            {
               return;    
            }
            if (this.left == null) 
            {
               this.left = node;    
            }
            else 
            {
                this.leftCount++;
                this.level++;
                this.left.insert(node);
                this.level--;
            }            
        }
    }
    
    public void insertPageIdsHash(String pageId) 
    {
        Integer pageIdStr = (Integer)this.pageIdsHash.get(pageId);
        if (pageIdStr != null) 
        {
            int intval = pageIdStr.intValue();
            this.pageIdsHash.put(pageId, new Integer(++intval));
        }
        else 
        {
            this.pageIdsHash.put(pageId, new Integer(1));
            this.pageIds.add(this.pageId);   
        }
    }
    
    public void getPreOrder(int size)
    {
        if (this.left == null) 
        {
           if (this.pageIds.size() < size) 
           {
//              System.out.println(this.rank + "   " + this.pageId);
              insertPageIdsHash(this.pageId);
           }
           else 
           {
              return;
           }
        }
        else 
        {
           if (this.pageIds.size() < size) 
           {
              this.left.getPreOrder(size-this.pageIds.size()); 
              if (this.pageIds.size() < size)
              {
//                 this.pageIds.add(this.pageId);
                 insertPageIdsHash(this.pageId);
//                 System.out.println(this.rank + "   " + this.pageId);  
              }
           }
        }
        if(this.pageIds.size() <size && this.right != null) 
        {
           this.right.getPreOrder(size-this.pageIds.size());    
        }
    }
}
