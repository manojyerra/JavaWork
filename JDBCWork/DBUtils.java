import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.sql.ResultSetMetaData;

class DBUtils
{
	public static ArrayList<String> getTablesList(Connection conn, boolean printTables) throws SQLException
	{
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		
		ArrayList<String> tablesList = new ArrayList<String>();

		boolean start = false;

		while (rs.next())
		{
			String tableName = rs.getString(3);

			if(!start)
			{
				if(tableName.equals("pc_index_flowref"))
				{
					start = true;
				}
			}

			//System.out.println(rs.getString(3));
	
			if(start)			
				tablesList.add(rs.getString(3));

			if(tableName.equals("prpc_install_log"))
				break;
		}

		if(printTables)
		{
			System.out.println("\n\n   ***   Begin : Printing all table names   ***\n\n");

			for(int i=0; i<tablesList.size(); i++)
			{
				System.out.println(tablesList.get(i));
			}

			System.out.println("\n\n   ***   End : Printing all table names   ***\n\n");
		}

		return tablesList;	
	}


	public static void printTable(ResultSet rs) throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();                     

		// Iterate through the data in the result set and display it. 

		while (rs.next())
		{
			//Print one row
			for(int i = 1 ; i <= columnsNumber; i++)
			{
			      //System.out.print(rs.getObject(i) + " "); //Print one element of a row
				Object obj = rs.getObject(i);
			}

			//System.out.println();//Move to the next line to print the next row.           
		}
	}

}