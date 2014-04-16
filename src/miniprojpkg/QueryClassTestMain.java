package miniprojpkg;

public class QueryClassTestMain 
{
    public QueryClassTestMain() 
    {
        super();
    }

    public static void main(String[] args) 
    {
        QueryClassTestMain queryClassTestMain = new QueryClassTestMain();
        QueryClass qryCls = new QueryClass();
        qryCls.Query("D:\\projects\\MS\\index_43GB", "gandhi");
    }
}
