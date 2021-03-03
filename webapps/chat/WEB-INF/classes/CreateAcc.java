import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.Passwords;

@WebServlet("/servlet/New")
public class CreateAcc extends HttpServlet{

	private static final Random RANDOM = new SecureRandom();


	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter out = res.getWriter();
		out.println("<!doctype html>");
		out.println("<link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">");
		out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css\">");
		out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js\"></script>");
		out.println("<title>Create Account</title></head><body>");
		out.println(" <div class=\"container\">"
				+ "			<div class=\"page-header\">"
				+ "		    	<h1>Créer un compte</h1>"
				+ "		    </div>"
				+ "	    	<div class=\"row\">"
				+ "	        	<div class=\"col-xs-6 col-xs-offset-3\">"
				+ "					<form id=\"createForm\" method=\"POST\" action=\"New\">"
				+ "						<div class=\"row\">"
				+ "						<div class=\"input-field col s6\">"
				+ "							<label for=\"login\" class=\"control-label\">Login</label>"
				+ "							<input type=\"text\" class=\"form-control\" id=\"login\" name=\"login\" value=\"\">"
				+ "						</div>"
				+ "						</div>"
				+ "						<div class=\"row\">"
				+ "							<div class=\"input-field col s6\">"
				+ "							<label for=\"password\" class=\"control-label\">Mot de passe</label>"
				+ "							<input type=\"password\" class=\"form-control\" id=\"password\" name=\"mdp\" value=\"\">"
				+ "						</div>"
				+ "						</div>"
				+ "						<button type=\"submit\" class=\"waves-effect waves-light btn\">Créer</button>"
				+ "						<a href=\"/chat/login.html\" class=\"waves-effect waves-light btn\">Retour</a>"
				+ "					</form>"
				+ "				</div>"
				+ "			</div>"
				+ "		</div>"
				+ "	</body>"
				+ "</html>");
	}



	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException {
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
		String query="insert into users values(?,?,?);";

		try{
			byte[] salt = Passwords.getNextSalt();
			byte[] saltedPassword = Passwords.hash(mdp.toCharArray(), salt);
			PreparedStatement ps=con.prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2, Base64.getEncoder().encodeToString(salt));
			ps.setString(3, Base64.getEncoder().encodeToString(saltedPassword));
			ps.executeUpdate();
			out.println(Base64.getEncoder().encodeToString(salt)+" "+Base64.getEncoder().encodeToString(saltedPassword));
			//res.sendRedirect("/chat/login.html");	
			con.close();
		}catch(SQLException e) {
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

}
