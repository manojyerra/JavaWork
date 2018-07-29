package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.sql.ResultSetMetaData;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class DBUtils
{
	public static ArrayList<String> getTablesList(Connection conn, boolean printTables, String... schemaNames) throws SQLException
	{
		DatabaseMetaData md = conn.getMetaData();
		String[] types = {"TABLE"};
		ArrayList<String> tablesList = new ArrayList<String>();

		for(int i=0; i<schemaNames.length; i++)
		{
			ResultSet rs = md.getTables(null, schemaNames[i], "%", types);

			while (rs.next())
			{
				tablesList.add(rs.getString(3));
			}

			if(printTables)
			{
				System.out.println("\n\n   ***   Begin : Printing all table names   ***\n\n");

				for(int num=0; num<tablesList.size(); num++)
				{
					System.out.println(tablesList.get(num));
				}

				System.out.println("\n\n   ***   End : Printing all table names   ***\n\n");
			}
		}

		return tablesList;	
	}

	public static void printTable(Connection conn, String tableName, boolean printColNames) throws SQLException
	{
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery("select * from "+tableName);
		printResultSet(rs, printColNames);
		stmt.close();
	}

	public static void printResultSet(ResultSet rs, boolean printColNames) throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();                     

		if(printColNames)
		{
			System.out.println("Begin : Printing column types");

			for(int i=1; i<=columnsNumber; i++)
			{
				System.out.println(rsmd.getColumnTypeName(i));			
			}

			System.out.println("End : Printing column types");
		}

		while (rs.next())
		{
			for(int i = 1 ; i <= columnsNumber; i++)
			{
				//System.out.print(rs.getObject(i) + " "); //Print one element of a row
				Object obj = rs.getObject(i);
			}

			//System.out.println();//Move to the next line to print the next row.           
		}
	}

	public static void writeResultSet(ResultSet rs, ObjectOutputStream oos) throws SQLException, IOException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();                     

		while (rs.next())
		{
			for(int i = 1 ; i <= columnsNumber; i++)
			{
				Object obj = rs.getObject(i);
				oos.writeObject(obj);
			}
		}

		oos.flush();
	}

	public static int getNumberOfRows(ResultSet resultSet) throws SQLException
	{
		resultSet.last();
		int count = resultSet.getRow();
		resultSet.first();		
		return count;
	}
}



//System.out.println(rsMaster.getMetaData().getColumnName(1));
//System.out.println(rsMaster.getMetaData().getColumnType(1));
//System.out.println(rsMaster.getMetaData().getColumnTypeName(1));
//System.out.println(rsMaster.getMetaData().getColumnDisplaySize(1));
//System.out.println(rsMaster.getMetaData().getColumnLabel(1));

