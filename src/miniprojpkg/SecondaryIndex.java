package miniprojpkg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
//import java.io.RandomAccessFile;

import java.util.RandomAccess;

public class SecondaryIndex 
{
    public SecondaryIndex() 
    {
        super();
    }

    public void createSecondaryIndex(String primaryIndexFileName, String secondaryIndexFileName) 
    {
        BufferedWriter out = null;
        FileWriter fstream = null;
        OptimizedRandomAccessFile pIndex = null;
        StringBuffer content = new StringBuffer("");
        int lineCount = 0;
        int lineChunkCount = 0;
        int lineChunkSizeToWrite = 1000;
        int lineChunkSizeInIndex = 500;
        try
        {
           pIndex = new OptimizedRandomAccessFile(primaryIndexFileName, "r");
           out = null;
           fstream = new FileWriter(new File(secondaryIndexFileName), true); //true tells to append data.
           out = new BufferedWriter(fstream);
           String line = pIndex.readLine();
           long offset = 0; 
            Merger qry = new Merger();
//            System.out.println(qry.getDate());
           while (line != null) 
           {
              lineCount++;
              if (lineCount%lineChunkSizeInIndex == (lineChunkSizeInIndex-1))
              {  
                 offset = pIndex.getFilePointer();
                 line = pIndex.readLine();
                 if (line != null)
                 {
                    lineCount++;
/*                    if (lineCount % 100000 == 0)
                    {
                       System.out.println(qry.getDate() + " " + lineCount);
                    }
*/
                    String word = line.substring(0, line.indexOf('~')); 
//                    content = content + word + "~" + offset + "\n";
                    content.append(word + "~" + offset + "\n");
                    lineChunkCount++;
//                     System.out.println(lineChunkCount + "   " + lineChunkSizeToWrite);
                     if (lineChunkCount == lineChunkSizeToWrite) 
                     {
                        // Write content to secondary file    
                        out.write(content.toString());
                        out.flush();
                        content.delete(0, content.length());
                        lineChunkCount = 0;
//                        content = "";
                     }
                 }
                 else 
                 {
                    break;    
                 }
              }
              line = pIndex.readLine();   
           }
            if (lineChunkCount > 0) 
            {
                out.write(content.toString());
                lineChunkCount = 0;
                content.delete(0, content.length());
//                content = "";
            }
//            System.out.println(qry.getDate());
           if(pIndex != null) 
           {
              pIndex.close();    
           }
           if (out != null) 
           {
              out.close();    
           }
           if (fstream != null) 
           {
              fstream.close();    
           }
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) 
    {
        SecondaryIndex secondaryIndex = new SecondaryIndex();
        if (args.length!=1) 
        {
           System.out.println("Invalid usage");    
        }
        else 
        { 
            secondaryIndex.createSecondaryIndex(args[0] + "\\primaryindex.tnd", args[0] + "\\secondaryindex.tnd");
   //           secondaryIndex.createSecondaryIndex("D:\\ms\\primaryindex.ind", "D:\\ms\\secondaryindex.ind");
        }
    }
}
