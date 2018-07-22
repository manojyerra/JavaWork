import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


class JDBCTest
{
	public static void main(String[] args) throws Exception
	{
		new JDBCTest();
	}

	JDBCTest() throws Exception
	{
		Connection conn=null;

		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();

			conn = DriverManager.getConnection("jdbc:sqlserver://10.224.212.18:1433;databaseName=head6024", "prpcuser", "prpcuser");

			if(conn!=null)
			{
				System.out.println("Database Successfully connected");
				conn.close();
			}

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
	}
}
