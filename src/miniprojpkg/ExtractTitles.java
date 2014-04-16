package miniprojpkg;

import java.io.File;
import java.io.FileWriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExtractTitles extends DefaultHandler
{
    StringBuilder titleStr = new StringBuilder();
    StringBuilder titleListStr = new StringBuilder();
    int page_count=0;
    int page_id;
    int local_page_count = 0;
    boolean revisionStarted;
    boolean contributorStarted;
    boolean idStarted;
    boolean titleStarted;
    boolean tFirst=true;
    FileWriter tWriter;
    
    public ExtractTitles() 
    {
        super();
    }
    public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException 
    {    
            if (qName.equalsIgnoreCase("page")) 
            {
                page_count++;
                local_page_count++;
            }
            else if (qName.equalsIgnoreCase("revision")) 
            {
               revisionStarted = true;
            }
            else if (qName.equalsIgnoreCase("contributor")) 
            {
                contributorStarted = true;
            }
            else if (qName.equalsIgnoreCase("id")) 
            {
                idStarted = true;
            }
            else if (qName.equalsIgnoreCase("title")) 
            {
               titleStarted = true;
               if (titleStr.length()>0)
               {
                  titleStr.delete(0, titleStr.length());
               }
            }
    }
    
    public void endElement(String uri, String localName,
            String qName) throws SAXException 
    {
            if(qName.equalsIgnoreCase("file")) 
            {
                if (titleListStr.length() > 0) 
                {
                    try
                    {
                       tWriter.write(titleListStr.toString());
                       titleListStr.delete(0, titleListStr.length());
                    }
                    catch(Exception e) 
                    {
                        
                    }
                }
            }
            if(qName.equalsIgnoreCase("page"))
            {  
               if (titleListStr.length() > 50000) 
               {
                   try
                   {
                      tWriter.write(titleListStr.toString());
                      titleListStr.delete(0, titleListStr.length());
                   }
                   catch(Exception e) 
                   {
                       
                   }
               }
               
               if (tFirst == true)
               {
                   titleListStr.append(page_id+"~"+titleStr.toString());
                   tFirst = false;
               }
               else 
               {
                   titleListStr.append("\n"+page_id+"~"+titleStr.toString());
               }
                titleStr.delete(0, titleStr.length());
            }
            else if (qName.equalsIgnoreCase("revision")) 
            {
               revisionStarted = false;
            }
            else if (qName.equalsIgnoreCase("contributor")) 
            {
                contributorStarted = false;
            }
            else if (qName.equalsIgnoreCase("id")) 
            {
               idStarted = false;    
            }
            else if (qName.equalsIgnoreCase("title")) 
            {
               titleStarted = false;    
            }
    }
    
    public void characters(char ch[], int start, int length) throws SAXException 
    {
          String chStr = new String(ch, start, length);
          if (idStarted == true)
          {
             int iId = Integer.parseInt(chStr.toString());
             if (revisionStarted == false && contributorStarted == false) 
             {
                page_id = iId;
             }
          }
          else if (titleStarted == true) 
          {
              titleStr.append(chStr);
          }
    }
    public void parseDocument(File inpXmlFile) 
    {
       // parse
       SAXParserFactory factory = SAXParserFactory.newInstance();
       try 
       {
          SAXParser parser = factory.newSAXParser();
          parser.parse(inpXmlFile, this);
       } 
       catch (Exception e) 
       {
           e.printStackTrace();
       }
    }
    
    public static void main(String[] args) 
    {
        ExtractTitles extractTitles = new ExtractTitles();
        try
        {
            File inpXmlFile = new File(args[0]);
            if(inpXmlFile.exists()==false) 
            {
               System.out.println("Error: "+args[0]+" not found");    
               System.exit(1);
            }
            else 
            {
               extractTitles.tWriter = new FileWriter(args[1] + "//primaryindex.tnd", true);
               extractTitles.parseDocument(inpXmlFile);
               extractTitles.tWriter.close(); 
            }
        }
        catch(Exception e) 
        {
            
        }
    }
}
