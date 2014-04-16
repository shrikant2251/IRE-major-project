package miniprojpkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

public class PageRankSearch {
	static HashMap<Integer,Long> inMemorySecPageRank = new HashMap<Integer,Long>();
	TreeMap<Integer,String> topResults = new TreeMap<Integer,String>();
	void keepSecPageRankInMemory(String dataPath){
		try{
			BufferedReader pageRankSecIndFile = new BufferedReader(new FileReader(dataPath+"/secIndexPageRank"));
			String str=null;
			String []strArray = {"",""};
			while((str=pageRankSecIndFile.readLine())!=null){
				strArray = str.split(" ");
				inMemorySecPageRank.put(Integer.parseInt(strArray[0]), Long.parseLong(strArray[1]));
			}
			pageRankSecIndFile.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	void sortResult(){
		
	}
}