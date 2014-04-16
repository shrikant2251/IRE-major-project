//package miniprojpkg;
package modified;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Merger implements Runnable
{
    int ctr = -1;
    String indexDir;
    File file1 = null;
    File file2 = null;
    int mergerId;
    int mergersCount=1;
    FileWriter logfs = null;
    File newFile1 = null;
    File newFile2 = null;

    public Merger() 
    {
        super();
    }
    public Merger(String indexDir, int mergerId, int total)
    {
        super();
        this.mergerId = mergerId;
        this.indexDir = indexDir;
        this.mergersCount = total;
    }
    public Merger(String indexDir, String mergerId, String total)
    {
        super();
        this.mergerId = Integer.parseInt(mergerId);
        this.indexDir = indexDir;
        this.mergersCount = Integer.parseInt(total);
    }
    
    public void run() 
    {
        this.mergerProcess();
    }
    
	public void logMsg(String logmsg)
	{
	   try
	   {
//	      logfs.write(getDate() + ": "+logmsg+"\n");
//	      logfs.flush();
	   }
	   catch(Exception e)
	   {
	      
	   }
	}
        
    public File[]  sortFiles(File filesList[]) 
    {
        // Sort files by size.
        Arrays.sort(filesList, new Comparator()
        {
          public int compare(Object f1, Object f2)
          {
            if (((File) f1).length() < ((File) f2).length())
            {
              return -1;
            }
            else if (((File) f1).length() > ((File) f2).length())
            {
              return 1;
            }
            else
            {
              return 0;
            }
          }
        });
        return filesList;
    }
    
    public void mergerProcess() 
    {
        Query query = new Query();
        OptimizedRandomAccessFile br1 = null;
        OptimizedRandomAccessFile br2 = null;
        BufferedWriter out = null;
        File dir = new File(indexDir);
        
        if (dir.exists() && dir.isDirectory()==true && dir.canRead()) 
        {
            boolean merge_completed = false;
            logMsg("Starting...");
            while(merge_completed == false) 
            {
               File[] filesList = query.finder(dir);
               int noOfFiles = filesList.length;
               logMsg("Number of ind files to merge = "+noOfFiles);
                
                if (noOfFiles < 2) 
                {
                    File completeFile = new File(indexDir + "/index_completed"+mergerId+".txt");
                    if (completeFile.exists() == true) 
                    {
                        logMsg("Deleting index completed file "+indexDir + "/index_completed"+mergerId+".txt");
                        merge_completed = true;
                        completeFile.delete();
                    }
                    else 
                    {
                        try
                        {
                           logMsg("Sleeping for 5 seconds...");
                           TimeUnit.SECONDS.sleep(5); 
                        }
                        catch(Exception eo) 
                        {
                           logMsg("Exception while sleeping "+eo.getMessage());    
                        }
                    }
                }
                else
                {
                   try
                   {
                       newFile1=null;
                       newFile2=null;
                       int skipPtr=-1;
                       file1=null;
                       file2=null;
                       File[] sortedFilesList = this.sortFiles(filesList);
                       int filesLength = sortedFilesList.length;
                       for(int ptr=0; ptr<filesLength; ptr++)
                       {
                          try
                          {
                            newFile1 = filesList[ptr];
                            file1 = new File(newFile1.getAbsolutePath() + mergerId);
                            if (newFile1.renameTo(file1)==false) 
                            {
			       file1 = null;
                               continue;    
                            }
                            skipPtr = ptr;
                            logMsg("1Successfully renamed from " + newFile1.getAbsolutePath() + " to " + file1.getAbsolutePath());
                            break;
                          }
                          catch(Exception se) 
                          {
                            logMsg("EXCEPTION 1");
                            file1=null;
                          }
                       }
                       if (file1 == null)
		       {
			     continue;
		       }
                       for(int ptr=0; ptr<filesLength; ptr++)
                       {
                          try
                          {
                            if (ptr!=skipPtr)
                            {
                               newFile2 = filesList[ptr];
                               file2 = new File(newFile2.getAbsolutePath() + mergerId);
                               if (newFile2.renameTo(file2)==false) 
                               {
	                          file2=null;
                                  continue;    
                               }
                               logMsg("2Successfully renamed from " + newFile2.getAbsolutePath() + " to " + file2.getAbsolutePath());
                               break;
                            }
                          }
                          catch(Exception se) 
                          {
                             logMsg("EXCEPTION 2");
                             file2=null;
                          }
                       }
                       logMsg("DEBUG: check point1");
                       if (file2==null) 
                       {
                           logMsg("Renaming back file1 since file2 is null... from " + file1.getAbsolutePath() + " to " + newFile1.getAbsolutePath());
                           file1.renameTo(newFile1);
                           file1=null;
                       }
                       logMsg("DEBUG: check point2");
                       logMsg("1Going to open "+file1.getAbsolutePath()+" for reading");
                       br1 = new OptimizedRandomAccessFile(file1.getAbsolutePath(), "r");
                       logMsg("2Going to open "+file2.getAbsolutePath()+" for reading");
                       br2 = new OptimizedRandomAccessFile(file2.getAbsolutePath(), "r");
                       out = null;
                       logMsg("DEBUG: check point4");
                       int tmpInt = (++(this.ctr)*mergersCount)+mergerId;
                       String fileName = new String(indexDir + "/index_m" + tmpInt + ".indm"+mergerId);
                       String fileReName = new String(indexDir + "/index_m" + tmpInt + ".ind");
                       FileWriter fstream = new FileWriter(fileName, true); //true tells to append data.
                       out = new BufferedWriter(fstream);
                       logMsg("DEBUG: check point5");
                       StringBuilder strBldr = new StringBuilder();
                       int lineCount = 0;
                       String line1 = null;
                       String line2 = null;
                       String subStr1 = null;
                       String subStr2 = null;
                       boolean file1Completed = false;
                       boolean file2Completed = false;
                       boolean readFile1Nextline = true;
                       boolean readFile2Nextline = true;
                       while(file1Completed == false || file2Completed == false) 
                       {
                           try
                           {
                               if (file1Completed == false)
                               {
                                  logMsg("file1Completed is false ");
                               }
                               else 
                               {
                                   logMsg("file1Completed is true "); 
                               }
                               if (readFile1Nextline == false)
                               {
                                  logMsg("readFile1Nextline is false ");
                               }
                               else 
                               {
                                   logMsg("readFile1Nextline is true "); 
                               }
                               if (file2Completed == false)
                               {
                                  logMsg("file2Completed is false ");
                               }
                               else 
                               {
                                   logMsg("file2Completed is true "); 
                               }
                               if (readFile2Nextline == false)
                               {
                                  logMsg("readFile2Nextline is false");
                               }
                               else 
                               {
                                   logMsg("readFile2Nextline is true"); 
                               }
                           }
                           catch(Exception hp) 
                           {
                               
                           }
                           if (lineCount >= 50)
                           {
                              out.write(strBldr.toString());
                              lineCount = 0;
                              strBldr.delete(0, strBldr.length());
                           }

                           if (file1Completed == false && readFile1Nextline == true)
                           {
                              logMsg("file1 is false readFile1Nextline is true");
                              line1 = br1.readLine();
                              readFile1Nextline = false;
                              if (line1 == null)
                              {
                                  file1Completed = true;
                              }
                           }
                           if (file2Completed == false && readFile2Nextline == true)
                           {
                              logMsg("file2 is false readFile2Nextline is true");
                              line2 = br2.readLine();
                              readFile2Nextline = false;
                              if (line2 == null)
                              {
                                  file2Completed = true;
                              }
                              else 
                              {
                                 logMsg("line2 is not null <"+line2+">");    
                              }
                           }
                           if (file1Completed == false  || file2Completed == false)
                           {
                              if (file1Completed == false)
                              {
                                 logMsg("1file1Completed is false ");
                              }
                              else 
                              {
                                  logMsg("1file1Completed is true "); 
                              }
                              if (readFile1Nextline == false)
                              {
                                 logMsg("1readFile1Nextline is false ");
                              }
                              else 
                              {
                                  logMsg("1readFile1Nextline is true "); 
                              }
                              if (file2Completed == false)
                              {
                                 logMsg("1file2Completed is false ");
                              }
                              else 
                              {
                                  logMsg("1file2Completed is true "); 
                              }
                              if (readFile2Nextline == false)
                              {
                                 logMsg("1readFile2Nextline is false");
                              }
                              else 
                              {
                                  logMsg("1readFile2Nextline is true"); 
                              }
                              if (line1 != null && line2 == null)
                              {
                                  lineCount++;
//                                  out.write(line1 + "\n");
                                  strBldr.append(line1 + "\n");
                                  readFile1Nextline = true;
                              }
                              else if (line2 != null && line1 == null)
                              {
                                  lineCount++;
//                                  out.write(line2 + "\n"); 
                                  strBldr.append(line2 + "\n");
                                  readFile2Nextline = true;
                              }
                              else
                              {
                                  if (line1.length() < 2 || line1.charAt(0) == '~')
                                  {
                                     readFile1Nextline = true;
                                  }
                                  if (line2.length() < 2 || line2.charAt(0) == '~')
                                  {
                                     readFile2Nextline = true;
                                  }
                                  if(readFile1Nextline == false && readFile2Nextline == false)
                                  {
                                      if (subStr1 == null)
                                      {
                                         subStr1 = line1.substring(0, line1.indexOf('~'));
                                      }
                                      if (subStr2 == null)
                                      {
                                         subStr2 = line2.substring(0, line2.indexOf('~'));
                                      }
                                     int cmpResult = subStr1.compareTo(subStr2);
                                     if(cmpResult > 0)
                                     {
                                         lineCount++;
//                                         out.write(line2 + "\n");
                                         strBldr.append(line2 + "\n");
                                         readFile2Nextline = true;
                                         subStr2 = null;
                                     }
                                     else if (cmpResult == 0)
                                     {
                                         lineCount++;
//                                         out.write(line1+","+line2.substring(line2.indexOf('~')+1) + "\n");
                                         strBldr.append(line1+","+line2.substring(line2.indexOf('~')+1) + "\n");
                                         readFile1Nextline = true;
                                         readFile2Nextline = true;
                                         subStr1 = null;
                                         subStr2 = null;
                                     }
                                     else
                                     {
                                         lineCount++;
//                                         out.write(line1 + "\n");
                                         strBldr.append(line1 + "\n");
                                         readFile1Nextline = true;
                                         subStr1 = null;
                                     }
                                  }
                              }
                          }
                       }
                       logMsg("merging might be completed");
                       
                       if (lineCount > 0)
                       {
                          out.write(strBldr.toString());
                          lineCount = 0;
                          strBldr.delete(0, strBldr.length());
                       }
                       if (br1 != null)
                       {
                          br1.close();
                       }
                       if (br2 != null)
                       {
                          br2.close();
                       }
                       if (out != null)
                       {
                          out.close();
                          File f = new File(fileName);
                          File s = new File(fileReName);
                          f.renameTo(s);
                          logMsg("4Successfully renamed from "+f.getAbsolutePath()+" to "+s.getAbsolutePath());
                       }
                   }
                   catch(Exception es)
                   {
                       logMsg(es.getMessage());
                       try
                       {
                           if (br1 != null) 
                           {
                              br1.close();    
                           }
                          logMsg("Last1 Renaming back from "+file1.getAbsolutePath()+" to "+newFile1.getAbsolutePath());
                          file1.renameTo(newFile1);
                       }
                       catch(Exception fs) 
                       {
                           logMsg("Last Exception1: "+fs.getMessage());    
                       }
                       try
                       {
                           if (br2 != null) 
                           {
                              br2.close();    
                           }
                           logMsg("Last2 Renaming back from "+file2.getAbsolutePath()+" to "+newFile2.getAbsolutePath());
                           file2.renameTo(newFile2);
                       }
                       catch(Exception fs) 
                       {
                           logMsg("Last Exception2: "+fs.getMessage()); 
                       }
                       continue;
                   }
                   logMsg("Deleting " + file1.getAbsolutePath() + " and " + file2.getAbsolutePath());
                   file1.delete();
                   file2.delete();
                }
            } // While Loop endds here till. merge_completed becomes true
        } // If index directory can be readable
    }
    
    public static void main(String[] args)
    {
        if(args.length!=3) 
        {
           System.out.println("Invalid Usage");
           System.exit(1);
        }
        Merger merger = new Merger(args[0], args[1], args[2]);
        merger.mergerId = Integer.parseInt(args[1]);
        try
        {
//           merger.logfs = new FileWriter(args[0] + "/"+merger.mergerId+".txt", true); //true tells to append data.
        }
        catch(Exception loge) 
        {
   //        loge.printStackTrace();    
        }
        
        merger.mergerProcess();
        try
        {
  //          merger.logMsg("merging should be completed");
  //          merger.logfs.flush();
  //          merger.logfs.close();
        }
        catch(Exception hp) 
        {
            
        }

    }
}
