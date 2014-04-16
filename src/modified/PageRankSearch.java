package modified;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class PageRankSearch {
	static TreeMap<Integer,Long> inMemorySecPageRank = new TreeMap<Integer,Long>();
	TreeMap<PageRankSort,String> topResults = new TreeMap<PageRankSort,String>();
	TreeMap<Integer,String> topResultitle = new TreeMap<Integer,String>();
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
	void pageRankSearch(){
		int cnt=0;
		Iterator<PageRankSort> it = topResults.keySet().iterator();
		PageRankSort tmp = null;
		while(it.hasNext() && cnt<10){
			tmp = it.next();
			System.out.println(tmp.pageId+" "+tmp.pageRank+" "+topResultitle.get(tmp.pageId));
			SearchServlet.pageRankOutputArray[cnt++] = topResultitle.get(tmp.pageId);
		}
	}
	float getPageRank(int pageId,String dataPath){
		long offset = inMemorySecPageRank.floorEntry(pageId).getValue();
		try{
		RandomAccessFile convFile = new RandomAccessFile(dataPath+"/convergedPageRankFile", "r");
		convFile.seek(offset);
		String str=null;
		String []strArray = {"",""};
		float result =0;
		while((str=convFile.readLine())!=null){
			strArray = str.split("#");
			if(pageId == Integer.parseInt(strArray[0])){
				convFile.close();
				result = Float.parseFloat(strArray[1]);
				return result;
			}
			else if(pageId < Integer.parseInt(strArray[0])){
				convFile.close();
				return 0;
			}
		}
		convFile.close();
		
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return 0;
	}
}

class PageRankSort implements Comparable<PageRankSort>{
	float pageRank;
	int pageId;
	@Override
	public int compareTo(PageRankSort p){
		if(this.pageRank<p.pageRank)
			return 1;
		else if(this.pageRank>p.pageRank)
			return -1;
		else
		return 0;
	}
}