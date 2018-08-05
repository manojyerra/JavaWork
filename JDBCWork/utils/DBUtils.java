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
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class DBUtils
{
	public static void backup(String dirPath, Connection conn, String... schemas) throws SQLException, IOException
	{
		File dirPathFileObj = new File(dirPath);
		dirPathFileObj.mkdirs();

		ArrayList<String> tablesList = getTablesList(conn, false, schemas);

		String metaFilePath = dirPath+"/metadata.txt";
		DBUtils.writeListToFile(tablesList, metaFilePath, false);

		for(int i=0; i<tablesList.size(); i++)
		{
			String tableName = tablesList.get(i);
			System.out.println("TableName : "+tableName);
			DBUtils.writeTableIntoFile(conn, tableName, dirPath+"/"+tableName);			
		}
	}


	public static void restore(String dirPath, Connection conn, String... schemas) throws SQLException, IOException, ClassNotFoundException
	{
		ArrayList<String> tablesListFromFile = FileUtils.getFileData(dirPath+"/metadata.txt", true, false);
		ArrayList<String> tablesList = getTablesList(conn, false, schemas);

		for(int i=0; i<tablesListFromFile.size(); i++)
		{
			String tableName = tablesListFromFile.get(i);
			System.out.println("TableName : "+tableName);
			DBUtils.writeTableFromFile(conn, tableName, dirPath+"/"+tableName);			
		}
	}

	public static void writeListToFile(ArrayList<String> list, String metaFilePath, boolean includeListSize) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(metaFilePath));

		if(includeListSize)
		{
			String listSizeStr = ""+list.size();
			bw.write(listSizeStr, 0, listSizeStr.length());
		}

		for(int i=0; i<list.size(); i++)
		{
			bw.newLine();
			bw.write(list.get(i), 0, list.get(i).length());
		}

		bw.flush();
		bw.close();
	}


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


	public static ArrayList<String> getTableSchemaInfo(Connection conn, String tableName, String... selectTypes) throws SQLException
	{
		String selectStr = "";

		for(int i=0; i<selectTypes.length; i++)
		{
			if(selectStr.length() > 0)
			{
				selectStr += ", ";
			}
			
			selectStr += selectTypes[i];
		}

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT "+selectStr+" FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+tableName+"' ORDER BY ORDINAL_POSITION");

		ArrayList<String> colNamesList = new ArrayList<String>();

		while(rs.next())
		{
			colNamesList.add(rs.getString(1));
			colNamesList.add(rs.getString(2));
		}

		stmt.close();

		//for(int i=0; i<colNamesList.size(); i++)
		//{
		//	System.out.println(colNamesList.get(i));
		//}	

		return colNamesList;
	}


	public static String getPreparedStatementToInsert(Connection conn, String tableName) throws SQLException
	{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+tableName+"' ORDER BY ORDINAL_POSITION");

		String colNames = "";
		String questionMarks = "";

		while(rs.next())
		{
			if(colNames.length() > 0)
			{
				colNames += ", ";
				questionMarks += ", ";				
			}

			colNames += rs.getString(1);
			questionMarks += "?";
		}

		stmt.close();

		return "INSERT INTO "+tableName+"("+colNames+") VALUES ("+questionMarks+")";	
	}


	public static void deleteAllRows(Connection conn, String schema, String tableName) throws SQLException
	{
		tableName = appendSchema(schema, tableName);

		String query = "DELETE FROM "+tableName;
		Statement stmt = conn.createStatement();
		stmt.execute(query);
		stmt.close();
	}


	public static void deleteTable(Connection conn, String schema, String tableName) throws SQLException
	{
		try{
			tableName = appendSchema(schema, tableName);

			String query = "DROP TABLE "+tableName;
			Statement stmt = conn.createStatement();
			stmt.execute(query);
			stmt.close();
		}catch(SQLException e){e.printStackTrace();}
	}


	public static void createTable(Connection conn, String schema, String tableName, String args) throws SQLException
	{
		tableName = appendSchema(schema, tableName);

		String sqlQuery = "create table "+tableName+"("+args+")";
		Statement stmt = conn.createStatement();
		stmt.execute(sqlQuery);
		stmt.close();
	}


	private static String appendSchema(String schema, String tableName)
	{
		if(schema != null)
		{
			tableName = schema+"."+tableName;
		}

		return tableName;
	}


	public static ArrayList<String> getSchemaList(Connection conn) throws SQLException
	{
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet schemas = meta.getSchemas();

		ArrayList<String> list = new ArrayList<String>();

		while (schemas.next()) 
		{
			String schema = schemas.getString(1);	// "TABLE_SCHEM"
			//String tableCatalog = schemas.getString(2);	//"TABLE_CATALOG"
			//System.out.println("tableSchema : "+tableSchema);

			list.add(schema);
		}
		
		return list;
	}


	public static ArrayList<String> getPrimaryKeys(Connection conn, String tableName) throws SQLException
	{
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet rs = meta.getPrimaryKeys(null, null, "survey");

		ArrayList<String> list = new ArrayList<String>();

		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			//System.out.println("getPrimaryKeys(): columnName=" + columnName);
			list.add(columnName);
		}

		return list;
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


	public static void writeTableIntoFile(Connection conn, String tableName, String filePath) throws SQLException, IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));

		ArrayList<String> metaDataList = DBUtils.getTableSchemaInfo(conn, tableName, "COLUMN_NAME", "DATA_TYPE");

		oos.writeObject(new Integer(metaDataList.size()/2));

		//for(int i=0; i<metaDataList.size(); i++)
		//{
		//	oos.writeObject(metaDataList.get(i));
		//}

		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery("select * from "+tableName);

		writeResultSet(rs, oos);

		oos.close();
		stmt.close();
	}


	public static void writeTableFromFile(Connection conn, String tableName, String filePath) throws SQLException, IOException, ClassNotFoundException
	{
		DBUtils.deleteAllRows(conn, null, tableName);

		String query = DBUtils.getPreparedStatementToInsert(conn, tableName);

		System.out.println("query:"+query);

		PreparedStatement pStmt = conn.prepareStatement(query);

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));

		//System.out.println("available bytes : "+ois.available());

		int numCols = ((Integer)ois.readObject()).intValue();

		System.out.println("numCols:"+numCols);
		
		boolean run = true;
		while(run)
		{
			//System.out.println("available bytes : "+ois.available());

			for(int col=1; col<=numCols; col++)
			{
				Object obj = null;
				
				try
				{				
					obj = ois.readObject();
				}
				catch(Exception e)
				{
					run = false;
					break;
				}

				pStmt.setObject(col, obj);
			}

			if(!run)
			{
				break;
			}

			pStmt.addBatch();
		}

		int[] updateCounts = pStmt.executeBatch();
		pStmt.close();

		ois.close();
	}


	public static void writeResultSet(ResultSet rs, ObjectOutputStream oos) throws SQLException, IOException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();                     

		while (rs.next())
		{
			for(int i=1 ; i<=numCols; i++)
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



//Start writing header data

//ResultSetMetaData rsmd = rs.getMetaData();
//int numCols = rsmd.getColumnCount();

//oos.writeObject(tableName);
//oos.writeObject(new Integer(numCols));

//for(int i = 1 ; i <= numCols; i++)
//{
//	String colName = rsmd.getColumnName(i);
	//int colType = rsmd.getColumnType(i);
	//String colTypeName = rsmd.getColumnTypeName(i);
	//String colLabel = rsmd.getColumnLabel(i);
	//int colDisplaySize = rsmd.getColumnDisplaySize(i);

	//rs.getString("PKCOLUMN_NAME");

	//System.out.println("colName:"+colName);
	//System.out.println("colType:"+colType);
	//System.out.println("colTypeName:"+colTypeName);
	//System.out.println("colDisplaySize:"+colDisplaySize);
	
	//System.out.println("colLabel:"+colLabel+"\n\n");
//	oos.writeObject(colName);
//}
