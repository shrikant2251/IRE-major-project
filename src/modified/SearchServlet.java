package modified;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//		System.out.println("Server started success!!!!!");
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("In doGetMethod modified");
		tfIdfsearchMethod(request, response);
		// pageRanksearchMethod(request, response);
		//response.sendRedirect("index.jsp");
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		/*
		 * for(int i=0;i<10;i++){ tfidfOutputArray[i]="Hello";
		 * pageRankOutputArray[i]="Hi"; }
		 */
		for (int i = 0; i < 10; i++) {
			System.out.println(tfidfOutputArray[i]);
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
//		filePath = "/media/shrikant/Software/Shrikant/Murali_43Index/index_43GB";
		filePath = "/media/shrikant/01CCDF7A341669B0/IRE_Data/Major/MuraliData";
		System.out.println(query);
		// int i=1;
		// if(query!=null)
		/*
		 * for(String tempStr:query.split(" ")){ System.out.println(tempStr);
		 * args[i++]=tempStr; }
		 */
//		System.out.println("In tfidf search");
		if (query != null) {
			modified.QueryClassTestMain queryClassTestMain = new modified.QueryClassTestMain();
			modified.QueryClass qryCls = new modified.QueryClass();
			qryCls.Query(filePath, query);
			System.out.println("Query executed in modified");
		}
		System.out.println("Main executed tfidf search");
	}

	void pageRanksearchMethod(HttpServletRequest request,
			HttpServletResponse response) {

	}

}
