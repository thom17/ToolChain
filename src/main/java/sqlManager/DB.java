package sqlManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB 
{
	Connection  conn;
	Statement stmt;
	public DB()
	{
		try {
			Class.forName("org.sqlite.JDBC").newInstance();
			try {
				File file = new File("db/test.db");
				conn = DriverManager.getConnection("jdbc:sqlite:db/test.db");
				this.stmt = conn.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String args[])
	{
		String sql = "CREATE TABLE \"Class\" (\r\n"
				+ "	\"name\"	TEXT,\r\n"
				+ "	\"srcName\"	TEXT,\r\n"
				+ "	\"extendSrcName\"	TEXT,\r\n"
				+ "	\"implementSrcName\"	TEXT,\r\n"
				+ "	\"packageSrc\"	TEXT,\r\n"
				+ "	\"isAbstrct\"	TEXT,\r\n"
				+ "	\"isInterface\"	TEXT,\r\n"
				+ "	FOREIGN KEY(\"implementSrcName\") REFERENCES \"Class\"(\"srcName\"),\r\n"
				+ "	FOREIGN KEY(\"extendSrcName\") REFERENCES \"Class\",\r\n"
				+ "	CONSTRAINT \"ownClass\" PRIMARY KEY(\"srcName\")\r\n"
				+ ")";
		DB db =  new DB();
		try {
			ResultSet rs = db.stmt.executeQuery(sql);
			while(rs.next())
			{
				System.out.println(rs.getString("Name"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
