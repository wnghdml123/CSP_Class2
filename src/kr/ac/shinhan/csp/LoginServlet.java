package kr.ac.shinhan.csp;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.jdo.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginServlet  extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		
		String id = req.getParameter("id");
		String password = req.getParameter("password");
		boolean success = false;
		
		MyPersistenceManager.getManager();
		
		Query qry = MyPersistenceManager.getManager().newQuery(UserAccount.class);
		
		qry.setFilter("userID == idParam");
		qry.declareParameters("String idParam");
		
		List<UserAccount> userAccount = (List<UserAccount>) qry.execute(id);

		if(userAccount.size()==0)
		{
			success=false;
		}
		
		else if(userAccount.get(0).getPassword().equals(password))
		{
			success=true;
		}
		
		else
		{
			success=false;
		}

		resp.getWriter().println("<html>");
		resp.getWriter().println("<body>");

		if(!success)
		{
			resp.getWriter().println("Fail to login");
			resp.getWriter().println("<a href='login.html'>Login Again</a>");
		}
		
		if(success)
		{
			HttpSession session = req.getSession(true);
			session.setAttribute("id", id);
			/*if (checked == true) {
				token = UUID.randomUUID().toString();
				
				Cookie cookie = new Cookie("t_id", token);
				cookie.setMaxAge(60 * 60 * 24 * 30);
				resp.addCookie(cookie);
			}*/
			resp.sendRedirect("/index.html");
			if(req.getParameter("remember")!=null)
			{
				String token = UUID.randomUUID().toString();
			Cookie cookie = new Cookie("login_token, token",token);
			cookie.setMaxAge(60*60*24*30);
			resp.addCookie(cookie);
			
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, 30);
			String expireDate = now.getTime().toString();
			
			UserLoginToken ult = new UserLoginToken(id,token,new Date().toString());
			
			
			MyPersistenceManager.getManager().makePersistent(ult);
			
			
			
			}
		}
		
		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");

	}

}
