//package miniprojpkg;
package modified;
import java.util.Hashtable;
import java.util.Iterator;

public class Page 
{
    public static enum PageType 
    {
    TITLE('t'), INFOBOX('i'), CATEGORY('c'), ELINK('l'), REFERENCE('r'), BODY('b');
    private char value;
        private PageType(char v) 
        {
           value = v;    
        }
        public char getValue() 
        {
           return value;    
        }
    };
    
    public Hashtable typeCountHash = new Hashtable();
    public Page() 
    {
        super();
    }
    
    public void populate(int page_id, PageType block_type, int count) 
    {
        typeCountHash.put(block_type, new Integer(count));
    }
    public Page(int page_id, PageType block_type, int count) 
    {
        super();
        populate(page_id, block_type, count);
    }
    
    public Page(int page_id, PageType block_type) 
    {
        super();
        populate(page_id, block_type, 1);
    }
    public void increment(PageType block_type)
    {
       increment(block_type, 1);
    }
    
    public void increment(PageType block_type, int incr) 
    {
        if (typeCountHash.containsKey(block_type)) 
        {
            Integer countObj = (Integer)typeCountHash.get(block_type);
            int count = countObj.intValue();
            typeCountHash.put(block_type, new Integer(count+incr));
        }
        else
        {
            typeCountHash.put(block_type, new Integer(1));           
        }
    }
    
    public int getTotal() 
    {
       int sum = 0;
       Iterator itr = typeCountHash.keySet().iterator();
       while(itr.hasNext()) 
       {
          PageType pageType = (PageType)itr.next();
          Integer countObj = (Integer)typeCountHash.get(pageType);
          sum = sum + countObj.intValue();
       }
       return(sum);
    }
    public String getIndexString() 
    {
        String str = "";
        Iterator itr = typeCountHash.keySet().iterator();
        while(itr.hasNext()) 
        {
          PageType pageType = (PageType)itr.next();
          Integer countObj = (Integer)typeCountHash.get(pageType);
          str = str + pageType.getValue() + countObj.intValue();
        }
        return str;
    }
}

