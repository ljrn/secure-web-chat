import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;

import pojos.Messages;
import pojos.Personne;

@WebServlet("/servlet/Chat")
public class Chat extends HttpServlet{
	private List<Messages> messages=new ArrayList<>();
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out=res.getWriter();
		out.println("<!doctype html>");
		//out.println("<head><link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl\" crossorigin=\"anonymous\">");
		//out.println("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0\" crossorigin=\"anonymous\"></script>");
		out.println("<link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">");
		out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css\">");
		out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js\"></script>");
		out.println("<link rel=\"stylesheet\" href=/chat/styles.css>");
		out.println("<script src=/chat/scripts.js defer></script>");
		out.println("<title>Chat</title></head><body>");
		HttpSession session=req.getSession();
		if(session.getAttribute("login")==null) {
			res.sendRedirect("/chat/login.html");

		}
		else {
			out.println("<nav>"
					+ "    <div class=\"nav-wrapper blue darken-1\">"
					+ "      <a href=\"#\" class=\"brand-logo\">Chat</a>"
					+ "      <ul id=\"nav-mobile\" class=\"right hide-on-med-and-down\">"
					+ "        <li><a href=Disconnect>  <i class=\"material-icons\">exit_to_app</i></a></li>"
					+ "      </ul>"
					+ "    </div>"
					+ "  </nav>"
					+ "<div class=container>"
					+ "<div class=chat>");
			String login=session.getAttribute("login").toString();
			for(Messages m:messages) {
				if(!m.getExpediteur().getLogin().equals(session.getAttribute("login"))) {
					try {
						String affiche=dechiffreRSA.dechiffre(m.getContenu());
						out.println("<div class=\"row chatmsg\"><span class=\"rounded grey lighten-2 left\" >&nbsp;&nbsp;"+m.getExpediteur().getLogin()+": "+affiche+"&nbsp;&nbsp;</span></div>");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					try {
						String affiche=dechiffreRSA.dechiffre(m.getContenu());
						out.println("<div class=\"row chatmsg\" ><span class=\"rounded right blue darken-1 \" >&nbsp;&nbsp;"+m.getExpediteur().getLogin()+" : "+affiche+"&nbsp;&nbsp;</span></div>");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			out.println("</div>"
					+ "<div class=\"input-group row\">"
					+ "<form action=Chat method=post>"
					+ "<div class=\"row\">"
					+ "        <div class=\"input-field col s6\">"
					+ "<a id=send class=prefix href=#><i class=\"material-icons\">send</i></a>"
					+ "<textarea class=\"materialize-textarea\" type=text id=message name=message></textarea>"
					+ "<input type=hidden name=login value="+login+">"
					+ "</div></div></form>"
					+ "</div></div></div></html>");
		}
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException {
		CharSequenceTranslator cst = StringEscapeUtils.ESCAPE_HTML4;
		String message=cst.translate(req.getParameter("message"));
		String login=req.getParameter("login");
		Personne p=new Personne(login);
		byte[] encrypted;
		try {
			encrypted = chiffreRSA.chiffre(message.getBytes());
			messages.add(new Messages(encrypted,p));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		res.sendRedirect("Chat");
	}

}
