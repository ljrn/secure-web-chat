import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/servlet/Authent")
public class Authent extends HttpServlet{
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter out = res.getWriter();
		Connection con=null;
		try
		{

			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:tcp://localhost/~/chat";
			String nom = "sa";
			String pwd = "";
			con = DriverManager.getConnection(url,nom,pwd);
		}
		catch(Exception e){
			out.println("Pb de connexion ..."+e.getMessage());
		}
		String login=req.getParameter("login");
		String mdp=req.getParameter("mdp");
		String query="Select * from clients where login=? and mdp=?;";

		try{
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2, mdp);
			out.println(ps.toString());
			ResultSet rs=ps.executeQuery();
			/*String salt ="";
			int saltedMdp=0;
			String compare="";
			while(rs.next()) {
				salt=rs.getString("salt");
				saltedMdp=rs.getInt("mdp");
			}
			compare = salt+mdp;*/
			rs.next();
			HttpSession session=req.getSession();
			session.setAttribute("login", rs.getString(1));
			res.sendRedirect("Chat");	
			con.close();


		}catch(SQLException e) {
			res.sendRedirect("/chat/login.html");
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
