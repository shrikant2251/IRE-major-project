package miniprojpkg;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator 
{
    Map base;
    public ValueComparator(Map base) {
        super();
        this.base = base;
    }

    public int compare(Object a, Object b) 
    {
        String o1 = (String)a;
        String o2 = (String)b;
        if (((Integer)base.get(o1)).intValue() >= ((Integer)base.get(o2)).intValue()) 
        {
            return -1;
        } 
        else 
        {
            return 1;
        } // returning 0 would merge keys
    }
}
