//package miniprojpkg;
package modified;
import java.io.File;

public class TestClass {
    public TestClass() {
        super();
    }

    public static void main(String[] args) 
    {
        TestClass testClass = new TestClass();
        Query q = new Query();
        File dir = new File("D:\\ms\\index");
        File f[] = q.finder(dir);
        System.out.println("Before sort:-");
        for(File ptr: f) 
        {
            System.out.println(ptr.length() + "   " + ptr.getAbsolutePath());
        }
        Merger m = new Merger();
        File s[] = m.sortFiles(f);
        for(File ptr: s) 
        {
            System.out.println(ptr.length() + "   " + ptr.getAbsolutePath());
        }        
    }
}
