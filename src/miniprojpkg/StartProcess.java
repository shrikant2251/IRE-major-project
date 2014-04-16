package miniprojpkg;

import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StartProcess 
{
    public static final int SUCCESS_EXECUTION = 0;
    public static final int MISSING_XMLFILE_ARGUMENT = 1;
    public static final int INPUT_XMLFILE_NOTFOUND = 2;
    
    public StartProcess() 
    {
        super();
    }

    public static void main(String[] args) 
    {
        StartProcess startProcess = new StartProcess();
        File inpXmlFile;
//        ObjMethod objMethod1 = new ObjMethod();
//        objMethod1.printDate();
//        ObjMethod objMethod2 = new ObjMethod();
//        ObjMethod objMethod3 = new ObjMethod();
//        startProcess.printDate();
        SecondaryIndex sec = new SecondaryIndex();
        int exit_code = SUCCESS_EXECUTION;
        if(args.length!=2)
        {
           System.out.println("Error: Please provide the corpus file in xml format in the command line"); 
           exit_code = MISSING_XMLFILE_ARGUMENT;
        }
        else
        {
//           objMethod1.indexDir = args[1];
//           objMethod2.indexDir = args[1];
//           objMethod3.indexDir = args[1];
           inpXmlFile = new File(args[0]);
           if(inpXmlFile.exists()==false) 
           {
              System.out.println("Error: "+args[0]+" not found");    
              exit_code = INPUT_XMLFILE_NOTFOUND;
           }
           else 
           {
               int mergersCount = 2;
               long startTime = System.currentTimeMillis();
               ObjMethod objMethod = new ObjMethod(inpXmlFile, args[1]); 
               try
               {
                   for(int pctr=1; pctr<=mergersCount; pctr++)
                   {
                      Process p = Runtime.getRuntime().exec("java miniprojpkg.Merger " + args[1] + " " + pctr + " " + mergersCount);
                   }
                   objMethod.parseDocument(inpXmlFile);
               }
               catch(Exception e) 
               {
                  e.printStackTrace();    
               }
               for(int pctr=1; pctr<=mergersCount; pctr++)
               {
                  File completedFile = new File(args[1] + "//index_completed" + pctr+".txt");
                  try
                  {
                     completedFile.createNewFile();
                  }
                  catch(Exception e) 
                  {
                     e.printStackTrace();
                  }
               }
               Query qry = new Query();
               int noOfFiles = mergersCount;
               while(noOfFiles > 0)
               {
                  try
                  {
                     TimeUnit.SECONDS.sleep(60);
                  }
                  catch(Exception e) 
                  {
                       
                  }
                  File[] txtAry = qry.finderTxt(new File(args[1]));
                  noOfFiles = txtAry.length;
               }
               long endTime = System.currentTimeMillis();
               System.out.println("Total time = "+(endTime-startTime)/1000);
           }
        }
        Query q1 = new Query();
        File[] indAry = q1.finder(new File(args[1]));
        File newFile = new File(args[1] + "/primaryIndex.ind");
        indAry[0].renameTo(newFile);
        sec.createSecondaryIndex(args[1]+"/primaryindex.ind", args[1]+"/secondaryindex.ind");
        sec.createSecondaryIndex(args[1]+"/primaryindex.tnd", args[1]+"/secondaryindex.tnd");
//        startProcess.printDate();
        System.exit(exit_code);
    }
    public void printDate() 
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);   
    }    
}
