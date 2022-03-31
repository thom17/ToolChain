package sqlManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB 
{
	Connection  conn;
	public DB()
	{
		try {
			Class.forName("SQLite.JDBCDriver").newInstance();
			try {
				conn = DriverManager.getConnection("jdbc:sqite:/datalist.db");
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

}
