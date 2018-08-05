import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;

import utils.DBUtils;

class JDBCTest
{
	Connection conn = null;

	public static void main(String[] args) throws Exception
	{
		new JDBCTest();
	}

	JDBCTest() throws Exception
	{
		try
		{
			conn = connectToDatabase("postgres");

			long startTime = System.currentTimeMillis();

			//DBUtils.backup("backup", conn, "data");
			DBUtils.restore("backup", conn, "data");

			//DBUtils.getTableSchemaInfo(conn, "pr4_rule", "COLUMN_NAME", "DATA_TYPE");
			

			/*
			DBUtils.deleteTable(conn, "data", "abc");
			DBUtils.createTable(conn, "data", "abc", "id int, name1 varchar(25), PRIMARY KEY(id)");
			
			String query = DBUtils.getPreparedStatementToInsert(conn, "abc");
			PreparedStatement pStmt = conn.prepareStatement(query);

			System.out.println(query);

			for(int i=1; i<10; i++)
			{
				pStmt.setObject(1, i);
				pStmt.setObject(2, "y"+i);
				pStmt.addBatch();
			}

			int[] updateCounts = pStmt.executeBatch();
			pStmt.close();			
			*/
			

			//DBUtils.writeTableIntoFile(conn, "pc_data_adm_seccorspol", "tables/atab.txt");
			//DBUtils.writeTableFromFile(conn, "atab", "tables/atab.txt");

			/*
			String sqlQuery = "Create table testtable(id varchar, name1 varchar(25), name2 varchar(26), PRIMARY KEY (id))";
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.executeQuery(sqlQuery);

			ArrayList<String> tablesList = DBUtils.getTablesList(conn, false, "data");
			
			//invokeJVMs(tablesList, 4);

			System.out.println("Number of tables : "+tablesList.size());

			for(int i=0; i<tablesList.size(); i++)
			{
				String tableName = tablesList.get(i);
				System.out.println("TableName : "+tableName);
				DBUtils.writeTableInFile(conn, tableName, "tables/"+tableName+".table");
			}
			*/

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

	Connection connectToDatabase(String dbName) throws Exception	
	{
		Connection conn = null;

		if(dbName.equals("postgres"))
		{
			//Driver and JDBC URL for postgres db
			Class.forName("org.postgresql.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
		}
		else if(dbName.equals("mssql"))
		{
			//Driver and JDBC URL for mssql db 
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			conn = DriverManager.getConnection("jdbc:sqlserver://10.224.212.18:1433;databaseName=head6024", "prpcuser", "prpcuser");
		}

		if(conn!=null)
		{
			System.out.println("Database Successfully connected");
		}

		return conn;
	}


	void writeIntoFiles(ArrayList<String> list, int numFiles) throws IOException
	{
		BufferedWriter bw[] = new BufferedWriter[numFiles];

		for(int i=0; i<numFiles; i++)
		{
			bw[i] = new BufferedWriter(new FileWriter("list"+i+".txt"));
		}
	
		for(int i=0; i<list.size(); i++)
		{
			bw[i%numFiles].write(list.get(i), 0, list.get(i).length());
			bw[i%numFiles].newLine();
		}

		for(int i=0; i<numFiles; i++)
		{
			bw[i].flush();
			bw[i].close();
		}
	}


	void invokeJVMs(ArrayList<String> tablesList, int numJVMs) throws IOException
	{
		int numIns = numJVMs;

		writeIntoFiles(tablesList, numIns);

		for(int i=0; i<numIns; i++)
		{
			ProcessBuilder pb = new ProcessBuilder("java", "-cp", "postgres.jar:", "DBBackup", "list"+i+".txt", "   INS_"+i);
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			Process p = pb.start();
		}

		System.out.println("End of method invokeJVMs");
	}
}


/*

      String query = " insert into users (first_name, last_name, date_created, is_admin, num_points) values (?, ?, ?, ?, ?)";

      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.setString (1, "Barney");
      preparedStmt.setString (2, "Rubble");
      preparedStmt.setDate   (3, startDate);
      preparedStmt.setBoolean(4, false);
      preparedStmt.setInt    (5, 5000);

      preparedStmt.execute();
*/



/*
Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
ResultSet rs = stmt.executeQuery("select * from "+tableName);

int rowsCount = DBUtils.getNumberOfRows(rs);

if(rowsCount > 0)
{
	System.out.print("\ntableName : "+tableName+" ("+rowsCount+")");			
}

ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tables/"+tableName+".table"));
DBUtils.writeResultSet(rs, oos);
oos.close();

stmt.close();
*/


/*
int numIns = 4;

writeIntoFiles(tablesList, numIns);


for(int i=0; i<numIns; i++)
{
	ProcessBuilder pb = new ProcessBuilder("java", "-cp", "postgres.jar:", "DBBackup", "list"+i+".txt", "INS_"+i);
	pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	pb.redirectError(ProcessBuilder.Redirect.INHERIT);
	Process p = pb.start();

	//Runtime.getRuntime().exec("java CopyPaste list_"+i+".txt");
}

System.out.println("CopyPaste : Done");
*/



//stmt.execute("create table Employeee123(id integer)");
//stmt.execute("INSERT INTO Employeee123 VALUES (123)");

//if(stmt != null)
//{	
//	stmt.close();
//}



/*
String tableName = "pr_sys_msg_qp_brokenitems";
Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
ResultSet rs = stmt.executeQuery("select * from "+tableName);

ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("pr_sys_msg_qp_brokenitems.txt"));
DBUtils.writeResultSet(rs, oos);
oos.close();
*/


/*
String createQuery = "pzinskey character varying(255) NOT NULL,"
+"pxobjclass character varying(96), "
+"pxinsname character varying(128), "
+"pxsavedatetime timestamp without time zone, "
+"pxcommitdatetime timestamp without time zone, "
+"pxcreatedatetime timestamp without time zone, "
+"pxupdatedatetime timestamp without time zone, "
+"pyrulesetname character varying(128), "
+"pxcreateoperator character varying(128), "
+"pxcreateopname character varying(128), "
+"pxcreatesystemid character varying(32), "
+"pxupdateoperator character varying(128), "
+"pxupdateopname character varying(128), "
+"pxupdatesystemid character varying(32), "
+"pylabel character varying(64), "
+"pypurpose character varying(255), "
+"pzpvstream bytea, "
+"PRIMARY KEY (pzinskey)";


			DBUtils.createTable(conn, "data", "aTab", createQuery);
*/

