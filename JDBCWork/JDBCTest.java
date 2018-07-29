import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import utils.DBUtils;

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

			ArrayList<String> tablesList = DBUtils.getTablesList(conn, false, "data");

			System.out.println("Number of tables : "+tablesList.size());

/*
			String tableName = "pr_sys_msg_qp_brokenitems";
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("select * from "+tableName);

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("pr_sys_msg_qp_brokenitems.txt"));
			DBUtils.writeResultSet(rs, oos);
			oos.close();
*/


			for(int i=0; i<tablesList.size(); i++)
			{
				String tableName = tablesList.get(i);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery("select * from "+tableName);

				int rowsCount = DBUtils.getNumberOfRows(rs);

				if(rowsCount > 0)
				{
					System.out.print("\ntableName : "+tableName+" ("+rowsCount+")");			
				}

				//DBUtils.printResultSet(rs, false);

				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("allTables/"+tableName+".table"));
				DBUtils.writeResultSet(rs, oos);
				oos.close();
				
				stmt.close();
			}

			long timeTaken = System.currentTimeMillis() - startTime;

			System.out.println("\nTime taken : "+timeTaken);

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


      			//stmt.execute("create table Employeee123(id integer)");
			//stmt.execute("INSERT INTO Employeee123 VALUES (123)");

			//if(stmt != null)
			//{	
			//	stmt.close();
			//}
