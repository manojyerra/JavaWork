import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.DatabaseMetaData;
import java.util.ArrayList;

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

			//Driver and JDBC URL for mssql db
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			//conn = DriverManager.getConnection("jdbc:sqlserver://192.168.43.130:1433;databaseName=head6024", "prpcuser", "prpcuser");

			if(conn!=null)
			{
				System.out.println("Database Successfully connected");
			}

			
			long startTime = System.currentTimeMillis();

			ArrayList<String> tablesList = DBUtils.getTablesList(conn, false);


			for(int i=0; i<tablesList.size(); i++)
			{
				String tableName = tablesList.get(i);
				System.out.println("tableName : "+tableName);			
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from "+tableName);
				DBUtils.printTable(rs);
				stmt.close();
			}


			long timeTaken = System.currentTimeMillis() - startTime;

			System.out.println("Time taken : "+timeTaken+", number of tables : "+tablesList.size());

			


      			//stmt.execute("create table Employeee123(id integer)");
			//stmt.execute("INSERT INTO Employeee123 VALUES (123)");

			//if(stmt != null)
			//{	
			//	stmt.close();
			//}

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






