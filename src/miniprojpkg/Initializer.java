package miniprojpkg;
import java.util.Iterator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
public class Initializer implements ServletContextListener{
 
		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			System.out.println("ServletContextListener destroyed");
		}
	 
		@Override
		public void contextInitialized(ServletContextEvent arg0) {
			PageRankSearch pgObj = new PageRankSearch();
			pgObj.keepSecPageRankInMemory("/media/shrikant/01CCDF7A341669B0/IRE_Data/Major/MuraliData");
//			Iterator<Integer> it = pgObj.inMemorySecPageRank.keySet().iterator();
//			while(it.hasNext()){
//				System.out.println(it.next());
//			}
			System.out.println("ServletContextListener started");	
		}


}
