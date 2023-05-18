import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionFood {

	public Connection con;
	String server = "jdbc:mysql://140.119.19.73:3315/";
	String database = "111306008"; // change to your own database
	String url = server + database + "?useSSL=false";
	String username = "111306008"; // change to your own user name
	String password = "tch2m";   // change to your own password
	
	 public Connection mkDataBase() throws SQLException{
	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	            con = DriverManager.getConnection(url, username, password);
	           
	        } catch (ClassNotFoundException ex) {
	            Logger.getLogger(DBConnectionFood.class.getName()).log(Level.SEVERE, null, ex);
	     
	        }
	        return con;
	    }
	 
	
}
