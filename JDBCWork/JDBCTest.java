import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 

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
			//Driver and JDBC URL for postgres db
			Class.forName("org.postgresql.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");

			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			//conn = DriverManager.getConnection("jdbc:sqlserver://10.224.212.18:1433;databaseName=head6024", "prpcuser", "prpcuser");

			if(conn!=null)
			{
				System.out.println("Database Successfully connected");
			}

			Statement stmt = conn.createStatement();

      			//stmt.execute("create table Employeee123(id integer)");
			
			stmt.execute("INSERT INTO Employeee123 VALUES (123)");

			if(stmt != null)
			{
				stmt.close();
			}

			if(conn != null)
			{
				conn.close();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
	}
}



