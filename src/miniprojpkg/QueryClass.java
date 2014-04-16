package miniprojpkg;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class QueryClass 
{
    int totDoc = 14000000;
    HashMap relationHash = new HashMap();
    TreeMap relationReverseHash = new TreeMap();
    String sFile = "";
    String pFile = "";
    File sfFile = null;
    File pfFile = null;
    String stFile = "";
    String ptFile = "";
    File sftFile = null;
    File pftFile = null;
    Map resultsHash = new HashMap();
    Map qWordHash;
    ArrayList resultsList = new ArrayList();
    BSTNode tTree = null;
    BSTNode cTree = null;
    BSTNode bTree = null;
    BSTNode lTree = null;
    BSTNode rTree = null;
    BSTNode iTree = null;
    int noElements = 1000;
    ArrayList orderList = new ArrayList();
    ArrayList typeGList = new ArrayList();
    ArrayList wghtList = new ArrayList();
    HashMap pHash = new HashMap();
    HashMap FinalRevResults = new HashMap();
    TreeMap FinalResults = new TreeMap();
    HashMap FinalRevResultsHash = new HashMap();
    public QueryClass() 
    {
        super();
        typeGList.add("t");
        typeGList.add("c");
        typeGList.add("b");
        typeGList.add("i");
        typeGList.add("l");
        typeGList.add("r");
        

        wghtList.add(new Float(900000));
        wghtList.add(new Float(9000));
        wghtList.add(new Float(900));
        wghtList.add(new Float(9));
        wghtList.add(new Float(0.09));
        wghtList.add(new Float(0.0009));

    }

   public void insert(HashMap lHash, String str) 
   {
      if (lHash.isEmpty() || lHash.size() < 11) 
      {
          lHash.put("t", new Integer(Integer.parseInt(str)));
      }
      else 
      {
          
      }
   }

    public void addToResults(String word, String list)
    {
        StringTokenizer strT = new StringTokenizer(list, ",");
        if (strT.countTokens() == 0) 
        {
            return;
        }
        float tmpFreq = (float)Math.log10(totDoc/strT.countTokens());
        Float invFreq = (Float)tmpFreq;
        invFreq = new Float(1.0);
        int noOfPages = 0;
        while (strT.hasMoreElements()) 
        {
           String token = strT.nextToken();
           int pos = token.indexOf(":");
           String pageId = token.substring(0, pos);
//           Integer it = (Integer)relationHash.get(pageId);
        if (pageId.equals("21194740") || pageId.equals("32430896")) 
        {
 //          System.out.println("One more break");    
        }
/*            if (it == null) 
            {
                relationHash.put(pageId, new Integer(2));    
            }
            else 
            {
                relationHash.put(pageId, new Integer(it.intValue()+1));
            }
           noOfPages++;
 */
           String typeList = token.substring(pos+1);
           int indPosition;
           int strLen = typeList.length();
           char typeC = 'X';
           StringBuffer valBuffer = new StringBuffer(); 
           String val = "";
            int value = 0;
           for(indPosition=0; indPosition<strLen; indPosition++) 
           {
               if(Character.isLetter(typeList.charAt(indPosition)))
               {
                  if (typeC != 'X') 
                  {
                      value = Integer.parseInt(valBuffer.toString());

                      if (pageId.equalsIgnoreCase("5856167")) 
                      {
//                        System.out.println("one more break");    
                      }

                         try
                         {
                            int sp=1;
                            HashMap it = (HashMap)qWordHash.get(word);
                            Integer intvl = (Integer)it.get(String.valueOf(typeC));
                            if (intvl != null)
                            {
                                HashMap pt = (HashMap)relationHash.get(pageId);
                                if (pt == null) 
                                {
                                    pt = new HashMap();
                                    pt.put(String.valueOf(typeC), new Integer(2));
                                    relationHash.put(pageId, pt);
                                    sp = 2;
                                }
                                else 
                                {
                                    Integer vval = (Integer)pt.get(String.valueOf(typeC));
                                    if (vval == null) 
                                    {
                                        pt.put(String.valueOf(typeC), new Integer(2));
                                        sp = 2;
                                    }
                                    else 
                                    {
                                        int k = vval.intValue();
                                        sp = k+1;
                                      pt.put(String.valueOf(typeC), new Integer(sp));
                                    }
                                }
                                initialInsert(pageId, String.valueOf(typeC), new Float(Math.pow(10, sp)) * invFreq*value*((Float)pHash.get(String.valueOf(typeC))));
                            }
                            else 
                            {
                                initialInsert(pageId, String.valueOf(typeC), invFreq*value*((Float)pHash.get(String.valueOf(typeC))));
                            }
                         }
                         catch(Exception s) 
                         {
                          
                         }
                  }
                  typeC = typeList.charAt(indPosition);
                  val = "";
                  valBuffer.delete(0, valBuffer.length());
               }
               else 
               {
                   valBuffer.append(typeList.charAt(indPosition));
//                   val = val + typeList.charAt(indPosition);
               }
           }
           value = Integer.parseInt(valBuffer.toString());
            try
              {
                  int sp=1;
                  HashMap it = (HashMap)qWordHash.get(word);
                  Integer intvl = (Integer)it.get(String.valueOf(typeC));
                  if (intvl != null)
                  {
                      HashMap pt = (HashMap)relationHash.get(pageId);
                      if (pt == null) 
                      {
                          pt = new HashMap();
                          pt.put(String.valueOf(typeC), new Integer(2));
                          relationHash.put(pageId, pt);
                          sp = 2;
                      }
                      else 
                      {
                          Integer vval = (Integer)pt.get(String.valueOf(typeC));
                          if (vval == null) 
                          {
                              pt.put(String.valueOf(typeC), new Integer(2));
                              sp = 2;
                          }
                          else 
                          {
                              int k = vval.intValue();
                              sp = k+1;
                              pt.put(String.valueOf(typeC), new Integer(sp));
                          }
                      }
                      initialInsert(pageId, String.valueOf(typeC), new Float(Math.pow(10, sp)) * invFreq*value*((Float)pHash.get(String.valueOf(typeC))));
                  }
                  else 
                  {
                      initialInsert(pageId, String.valueOf(typeC), invFreq*value*((Float)pHash.get(String.valueOf(typeC))));
                  }

              }
              catch(Exception s) 
              {
                 s.printStackTrace();
                 System.exit(1);
              }
        }
    }

    public void initialInsert(String pageId, String typeC, Float value) 
    {
        if (pageId.equals("21194740") || pageId.equals("32430896"))  
        {
//           System.out.println("One more break");    
        }
        /*
        Iterator typeItr = FinalRevResultsHash.keySet().iterator();
        while(typeItr.hasNext()) 
        {
           String typeStr1 = (String)typeItr.next();
           HashMap hmap = (HashMap)FinalRevResultsHash.get(typeStr1);
           Float valv = (Float)hmap.get(pageId);
           if (valv != null) 
           {
              if (valv.compareTo(value) < 0) 
              {
                  hmap.remove(pageId);
                  break;
              }
              else
              {
                 return;
              }
           }
        }
*/
        HashMap frevRev = (HashMap)FinalRevResultsHash.get(typeC);
        if (frevRev != null)
        {
           Float val = (Float)frevRev.get(pageId);
           if (val == null) 
           {
              frevRev.put(pageId, value);  
           }
           else 
           {
              frevRev.put(pageId, val+value);
           }
        }
        else 
        {
           frevRev = new HashMap(); 
           frevRev.put(pageId, value);  
           FinalRevResultsHash.put(typeC, frevRev);
        }
    }
    
    public void revFinalRevResults() 
    {
        Iterator revItr = FinalRevResultsHash.keySet().iterator();
        int ctrl=0;
        while(revItr.hasNext()) 
        {
           ctrl++;
           String typeC = (String)revItr.next();
           HashMap frevResHsh = (HashMap)FinalRevResultsHash.get(typeC);
           Iterator revpItr =  frevResHsh.keySet().iterator();
           while(revpItr.hasNext()) 
           {
               String pageIds = (String)revpItr.next();
               Float valF = (Float)frevResHsh.get(pageIds);
               if (FinalResults.size() < 10)
               {
                   String pids = (String)FinalResults.get(valF);
                   if (pids == null)
                   {
                      FinalResults.put(valF, pageIds);
                   }
                   else
                   {
                       StringTokenizer stkn = new StringTokenizer(pids, ",");
                       if (stkn.countTokens() < 10) 
                       {   
                          StringBuffer nn = new StringBuffer(pids);
                          nn.append(",");
                          nn.append(pageIds);
                          FinalResults.put(valF, nn.toString()); 
                       }
                   }
               }
               else 
               {
                   Float lastKey = (Float)FinalResults.lastKey(); 
                   if (valF.compareTo(lastKey) > 0)
                   {
                      String pageIdStr = (String)FinalResults.get(valF);
                      if (pageIdStr != null)
                      {
                         StringTokenizer stknr = new StringTokenizer(pageIdStr, ",");
                         if (stknr.countTokens() <= 10)
                         {
                            StringBuffer stb = new StringBuffer(pageIdStr);
                            stb.append(",");
                            stb.append(pageIds);
                            FinalResults.put(valF, stb.toString()); 
                         }
                      }
                      else 
                      {
                          FinalResults.put(valF, pageIds);
                          FinalResults.remove(lastKey);
                      }
                   }                
               }         
           }
        }
    }
    
    public String getResPages(String word, boolean wordTitle) 
    {
        String secFileName;
        String priFileName;
        if (wordTitle == true)
        {
           secFileName = sFile;    
           priFileName = pFile;
        }
        else 
        {
           secFileName = stFile; 
           priFileName = ptFile;
        }
        String result = searchTokenInSecondary(word, secFileName, wordTitle);
        String result1;
        if (result.equalsIgnoreCase("FIRST")) 
        {
            result1 = searchToken(word, priFileName, new String("0"));
        }
        else 
        {
            result1 = searchToken(word, priFileName, result.substring(result.indexOf("~")+1)) ;    
        }
        return result1;
    }
    
    public void reverseRelationHash() 
    {
       if (relationHash.isEmpty() == false) 
       {
          System.out.println("DEBUG: size = " + relationHash.size()); 
          Iterator itr = relationHash.keySet().iterator();
          while(itr.hasNext()) 
          {
             String pgid = (String)itr.next();
             Integer k = (Integer)relationHash.get(pgid);
             String pgsId = (String)relationReverseHash.get(k);
             if (pgsId == null) 
             {
                 relationReverseHash.put(k ,pgid);
             }
             else
             {
                 StringBuffer stb1 = new StringBuffer(pgsId);
                 stb1.append(",");
                 stb1.append(pgid);
                 relationReverseHash.put(k, stb1.toString());
             }
          }
       }
    }

    public void Query(String args1, String args2) 
    {
        QueryClass query = new QueryClass();
        ObjMethod obj = new ObjMethod();
        query.sFile = args1 + "/secondaryindex.ind";
        query.pFile = args1 + "/primaryindex.ind";
        query.stFile = args1 + "/secondaryindex.tnd";
        query.ptFile = args1 + "/primaryindex.tnd";
        File sfFile = new File(query.sFile);
        File sftFile = new File(query.stFile);
        if(sfFile.exists() == false || sfFile.canRead() == false) 
        {
            System.out.println("Cannot read secondaryindex.ind in the given directory");
            System.exit(1);
        }
        if(sftFile.exists() == false || sftFile.canRead() == false) 
        {
            System.out.println("Cannot read secondaryindex.tnd in the given directory");
            System.exit(1);
        }
        File pfFile = new File(query.pFile);
        File pftFile = new File(query.ptFile);
        if(pfFile.exists() == false || pfFile.canRead() == false) 
        {
            System.out.println("Cannot read primaryindex.ind in the given directory");
            System.exit(1);
        }
        if(pftFile.exists() == false || pftFile.canRead() == false) 
        {
            System.out.println("Cannot read primaryindex.tnd in the given directory");
            System.exit(1);
        }
        double startTime = System.currentTimeMillis();
        query.qWordHash = new HashMap();
        Stemmer stemmer = new Stemmer();
        ObjMethod obm = new ObjMethod();
        StringTokenizer strtknr = new StringTokenizer(args2, " ");
        while(strtknr.hasMoreTokens())
        {
  //      for(int i=1; i<args.length; i++)
  //      {
            String tknArg = strtknr.nextToken();
            int pos = tknArg.indexOf(":");
            if (pos==1 && tknArg.length() > 2) 
            {
                String type = tknArg.substring(0, 1);
                String token = tknArg.substring(2).toLowerCase();
                if (obm.stopWords.containsKey(token) == true)
                {
                    continue;
                }
                String word = stemmer.stemString(token);
                HashMap typeHash = (HashMap)query.qWordHash.get(word);
                if (typeHash != null) 
                {
                    typeHash.put(type, new Integer(1));
                }
                else
                {
                   typeHash = new HashMap();
                   typeHash.put(type, new Integer(1));
                   query.qWordHash.put(word, typeHash);
                }
            }
            else
            {
               //Ignore the word and continue;
                String token = tknArg.toLowerCase();
                if (obm.stopWords.containsKey(token) == true)
                {
                    continue;
                }
                String word = stemmer.stemString(token);
                HashMap typeHash = (HashMap)query.qWordHash.get(word);
                if (typeHash == null)
                {
                   typeHash = new HashMap();
                   typeHash.put("t", new Integer(1));
                   query.qWordHash.put(word, typeHash);
                }
                else
                {
                  typeHash.put("t", new Integer(1));
                }
               
            }
        }
        double midTime = System.currentTimeMillis();
//        System.out.println("First Mid : " + (double)(midTime-startTime)/1000.0);
        Iterator itr = query.qWordHash.keySet().iterator();
        while(itr.hasNext()) 
        {
            String word = (String)itr.next();
//            System.out.println("DEBUG: Processing word <"+word+">");
            query.pHash = new HashMap();
            HashMap typeHash = (HashMap)query.qWordHash.get(word);
            Iterator itr1 = typeHash.keySet().iterator();
            Iterator wghtItr = query.wghtList.iterator();
            while(wghtItr.hasNext())
            {
               boolean found = false; 
               Float wt = (Float)wghtItr.next();
               Iterator typeGListItr = query.typeGList.iterator();
                while(typeGListItr.hasNext()) 
                {
                   String typ = (String)typeGListItr.next();
                   if (typeHash.containsKey(typ) && !query.pHash.containsKey(typ)) 
                   {
                      query.pHash.put(typ, wt);
                      found = true;
                      break;
                   }
                }
                Iterator typeGListItr1 = query.typeGList.iterator();
                while(found == false && typeGListItr1.hasNext()) 
                {
                   String typ = (String)typeGListItr1.next();
                   if (!query.pHash.containsKey(typ)) 
                   {
                      query.pHash.put(typ, wt);                     
                      break;
                   }
                }
            }
            Iterator phitr = query.pHash.keySet().iterator();
            while(phitr.hasNext()) 
            {
               String strk = (String) phitr.next();
//               System.out.println("     " + strk + "     " + (Float)query.pHash.get(strk));    
            }
            String tmpResultsList = query.getResPages(word, true);
            query.addToResults(word, tmpResultsList);
        }
        double midTime2 = System.currentTimeMillis();
//        System.out.println("Second Mid : " + (double)(midTime2-midTime)/1000.0);
        query.revFinalRevResults();
        double midTime3 = System.currentTimeMillis();
//        System.out.println("Third Mid : " + (double)(midTime3-midTime2)/1000.0);
/*        query.reverseRelationHash();
        double midTime4 = System.currentTimeMillis();
        System.out.println("Third Mid : " + (double)(midTime4-midTime3)/1000.0);
*/
        itr = query.FinalResults.descendingKeySet().iterator();
        int maxCnt = 10;
        int curCnt = 0;
        while(itr.hasNext())
        {
            if (curCnt == maxCnt) 
            {
               break;    
            }
            Float fltRank = (Float)itr.next();
            
            String pageIdstComaSep = (String)query.FinalResults.get(fltRank);
            String[] pageIdAry = pageIdstComaSep.split(",");
            for (int par=0; par<pageIdAry.length && curCnt < maxCnt; par++)
            {
               String titleS = query.getResPages(pageIdAry[par], false);
               System.out.println(fltRank + "   "+pageIdAry[par] + " " + titleS);
               System.out.println("miprog package");
               	SearchServlet.tfidfOutputArray[curCnt] = titleS;
//                System.out.println(((Integer)query.relationHash.get(pageIdAry[par])).intValue() + "  " +  fltRank + "   "+pageIdAry[par] + " " + titleS);                
                curCnt++;
            }
        }
        double endTime = System.currentTimeMillis();
        System.out.println("Total time = "+(double)(endTime-startTime)/1000.0);
       // System.exit(0);
    }
    
    public File[] finderTxt( File dir)
    {
            return dir.listFiles(new FilenameFilter() {
                     public boolean accept(File dir, String filename)
                          { return filename.endsWith(".txt"); }
            } );    
    }
    
    public File[] finder( File dir)
    {
            return dir.listFiles(new FilenameFilter() {
                     public boolean accept(File dir, String filename)
                          { return filename.endsWith(".ind"); }
            } );    
    }

    public String searchToken(String token, String fileName, String offset)
    {
        try
        {
           long lng = Long.valueOf(offset).longValue(); 
           OptimizedRandomAccessFile br = new OptimizedRandomAccessFile(fileName, "r");
           String line;
            Pattern tokenPattern =  Pattern.compile("^" + token + "~");
            int lineno=0;
            br.seek(lng);
            while ((line = br.readLine()) != null) 
            {
                lineno++;
                if (line.length() < 1 || line.charAt(0) == '~')
                {
                   continue;    
                }
                try
                {
                   String subStr = line.substring(0, line.indexOf('~'));
                   if(subStr.compareTo(token) > 0)
                   {
                      return (new String(""));    
                   }
                }
                catch(Exception ee) 
                {
                    System.out.println("Error at line : " + lineno);
                    ee.printStackTrace();    
                    return("");
                }
                Matcher matcher = tokenPattern.matcher(line);
                if(matcher.find()) 
                {
                    // Uncomment the below line DEBUG
                   return(line.substring(line.indexOf('~')+1));    
                }

               // process the line.
            }
            br.close();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return (new String(""));
    }    
    
    public String searchTokenInSecondary(String token, String fileName, boolean wordTitle)
    {
        boolean firstTime = true;
        String prev = "";
        try
        {
           OptimizedRandomAccessFile br = new OptimizedRandomAccessFile(fileName, "r");
           String line;
            Pattern tokenPattern =  Pattern.compile("^" + token + "~");
            int lineno=0;
            while ((line = br.readLine()) != null) 
            {
                lineno++;
                if (line.length() < 1 || line.charAt(0) == '~')
                {
                   continue;    
                }
                try
                {
                   String subStr = line.substring(0, line.indexOf('~'));
                   if (wordTitle == true)
                   {
                      if (subStr.startsWith("Su") || subStr.startsWith("su")) 
                      {
//                         System.out.println("break here");    
                      }
                      if(subStr.compareToIgnoreCase(token) > 0)
                      {
                          if (firstTime == true)
                          {
                             br.close();
                             return (new String("FIRST"));  
                          }
                          return (prev); 
                      }
                      else if (subStr.equalsIgnoreCase(token)) 
                      {
                         br.close();
                         return(line);    
                      }
                   }
                   else 
                   {
                       int subStrint = Integer.parseInt(subStr);
                       int tokenint = Integer.parseInt(token);
                       if(subStrint > tokenint)
                       {
                           if (firstTime == true)
                           {
                              br.close();
                              return (new String("FIRST"));  
                           }
                           return (prev); 
                       }
                       else if (subStrint == tokenint) 
                       {
                          br.close();
                          return(line);    
                       }                       
                   }
                   if (firstTime == true) 
                   {
                      firstTime = false;    
                   }
                }
                catch(Exception ee) 
                {
                    System.out.println("Error at line : " + lineno);
                    ee.printStackTrace();
                    return("");
                }
                prev = line;
            }
            br.close();
            return(prev);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return (new String(""));
    }
}
