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
import utils.FileUtils;


class DBBackup
{
	public static void main(String[] args) throws Exception
	{
		new DBBackup(args[0], args[1]);
	}

	DBBackup(String tablesListFile, String insName) throws Exception
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
				System.out.println("Database Successfully connected from "+insName);
			}

			long startTime = System.currentTimeMillis();

			ArrayList<String> tablesList = FileUtils.getFileData(tablesListFile);

			System.out.println("Number of tables : "+tablesList.size()+" from "+insName);

			for(int i=0; i<tablesList.size(); i++)
			{
				String tableName = tablesList.get(i).trim();

				if(tableName.length() == 0)
				{
					continue;
				}

				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery("select * from "+tableName);

				System.out.print("\ntableName : "+tableName+" from "+insName);			

				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tables/"+tableName+".table"));
				DBUtils.writeResultSet(rs, oos);
				oos.close();
				
				stmt.close();
			}

			long timeTaken = System.currentTimeMillis() - startTime;

			System.out.println("\nTime taken : "+timeTaken+" from "+insName+"\n");

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
