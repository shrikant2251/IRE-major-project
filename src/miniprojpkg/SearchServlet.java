package miniprojpkg;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import miniprojpkg.Query;
import miniprojpkg.QueryClass;
import miniprojpkg.QueryClassTestMain;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String[] tfidfOutputArray = new String[11];
	static String[] pageRankOutputArray = new String[11];
	String tfidfLink = "tfidfLink";
	String pagerankLink = "pagerankLink";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
		super();
		System.out.println("Server started success!!!!!");
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("In doGetMethod");
		tfIdfsearchMethod(request, response);
		pageRanksearchMethod(request, response);
		System.out.println("pageRank execution done");
		//response.sendRedirect("index.jsp");
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		/*
		 * for(int i=0;i<10;i++){ tfidfOutputArray[i]="Hello";
		 * pageRankOutputArray[i]="Hi"; }
		 */
		for (int i = 0; i < 10; i++) {
//			System.out.println(tfidfOutputArray[i]);
			request.setAttribute(tfidfLink + "" + i, tfidfOutputArray[i] + "\n");
			// set your String value in the attribute
			request.setAttribute(pagerankLink + "" + i, pageRankOutputArray[i]+ "\n");
		}
		dispatcher.forward(request, response);
		//response.sendRedirect("index.jsp");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("In doGetMethod");
		tfIdfsearchMethod(request, response);
		pageRanksearchMethod(request, response);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("Search.jsp");
		for (int i = 0; i < 10; i++) {
			tfidfOutputArray[i] = "Hello";
			pageRankOutputArray[i] = "Hi";
		}
		for (int i = 0; i < 10; i++) {
			request.setAttribute(tfidfLink + "" + i, tfidfOutputArray[i] + "\n"); 
			request.setAttribute(pagerankLink + "" + i, pageRankOutputArray[i]
					+ "\n");
		}
		dispatcher.forward(request, response);
	}

	void tfIdfsearchMethod(HttpServletRequest request,HttpServletResponse response) {
		String filePath = new String();
		String query = request.getParameter("searchbox");
		filePath = "/media/shrikant/01CCDF7A341669B0/IRE_Data/Major/MuraliData";
		System.out.println(query);
		// int i=1;
		// if(query!=null)
		/*
		 * for(String tempStr:query.split(" ")){ System.out.println(tempStr);
		 * args[i++]=tempStr; }
		 */
		System.out.println("In tfidf search");
		if (query != null) {
			QueryClassTestMain queryClassTestMain = new QueryClassTestMain();
			QueryClass qryCls = new QueryClass();
			qryCls.Query(filePath, query);
		}
		System.out.println("Main executed tfidf search");
	}

	void pageRanksearchMethod(HttpServletRequest request,
			HttpServletResponse response)   {
		/*try {
			System.out.println("Executing pageRank");
		     String []cmd=new String[3];
		     cmd[0] = "bash";
		     cmd[1] = "run.sh";
		     cmd[2] = request.getParameter("searchbox");
			String[] cmds = {
		    		  "/bin/sh", "-c","run.sh"
		       };		      
		     Process p = Runtime.getRuntime().exec(cmds);
		      p.waitFor ();
		      System.out.println ("Done.");
		    }
		    catch (Exception e) {
		      System.out.println ("Err: " + e.getMessage());
		    }
		*/
		double s = System.currentTimeMillis();
		String []cmd = new String[3];
		cmd[0] = "bash";
//		cmd[1] = "/home/shrikant/IIIT/Mtech2Sem/IRE/Aditya_pgrank/run.sh";
		cmd[1] = "/home/shrikant/IIIT/Mtech2Sem/IRE/Aditya_pgrank/run.sh";
		cmd[2] = request.getParameter("searchbox");
		 try{
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
//		pr.waitFor();
		// retrieve output from python script
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
		System.out.println("In pageRank execution");
		String readLine = "";
		int i=0;
		while((readLine = bfr.readLine()) != null) {
		// display each output line form python script
		System.out.println(readLine +"*********	");
		if(i<10)
		pageRankOutputArray[i] = readLine;
		System.out.println(pageRankOutputArray[i++]);
		}
		bfr.close();
		 }
		 catch(IOException e){
			 e.printStackTrace();
		 } 
		 double e = System.currentTimeMillis();
		 System.out.println("While excuted ");
		 System.out.println("PageRank time="+(e-s)/1000);
	}

}
