// CRUD operations using an interface to chage between various databases.

import java.sql.*;
import java.util.Scanner;

interface iCRUD
{
	public void create();
	public void read();
    public void exit();
}

class crudMySQL implements iCRUD
{
	static String[] columnNames;
    static Connection conn;
    static Scanner scanner = new Scanner(System.in);
    static int choice;
    static Statement stmt;
    static String uniqueValue;
    static String tableName;
    static int columnCount;
    static String header = "";
    static String values = "";
    static String activeStatus = "a";

    crudMySQL()
    {
        System.out.print("Enter database name: ");
        String databaseName = scanner.nextLine();
        String url = "jdbc:mysql://165.22.14.77/" + databaseName + "?A autoReconnect=true&useSSL=false";
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter the table name to work on: ");
        tableName = scanner.nextLine();


        try
        {
            conn = DriverManager.getConnection(url, userName, password);  
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            printErrorMessage(e);
            System.out.println("Please recheck username, password, database name, table name and try again!");
            System.exit(0);
        }
    }

    public static void printErrorMessage(Exception e)
    {
        System.out.println("Got an exception!");
        System.out.println(e.getMessage());
    }

    public static void printMessage()
    {
        System.out.println("Invalid input!");

    }

    public static void getColumnNames()
    {

        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData metadata = rs.getMetaData();
            columnCount = metadata.getColumnCount();
            columnNames = new String[columnCount];
            for (int counter = 0; counter < columnCount; counter ++)
            {
                columnNames[counter] = metadata.getColumnName(counter + 1);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            printErrorMessage(e);
        }
    }
    public static void drawLine(String header)
    {
        System.out.println(header);
        for(int counter = 0; counter < header.length(); counter++)
        {
            System.out.print('-');
        }
        System.out.println();
    }


    public void create()
    {   
        String query = "insert into "+ tableName +" values(";
        try
        {
            getColumnNames();

            for(int columnCounter = 0; columnCounter < columnCount - 1; columnCounter++)
            {
                System.out.print("Please enter " + columnNames[columnCounter] + ": ");
                query += "'" + scanner.nextLine() + "', ";
            }
            query += "'" + activeStatus + "')";
            stmt.executeUpdate(query);

            System.out.println(query);
            System.out.println(tableName + " added to database!");
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("ABC");
            printErrorMessage(e);
        }
    }


    public void read()
    {
        try
        {
            getColumnNames();
            ResultSet rs = stmt.executeQuery("select * from " + tableName + " where " + columnNames[columnNames.length - 1]+ " = '" + activeStatus + "'");
            header = "";
            printColumnNames();
            printValues(rs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            printErrorMessage(e);
        }
    }
    public static void printColumnNames()
    {
        for(int columnNameCounter = 0; columnNameCounter < columnCount - 1 ; columnNameCounter++)
        {
            if(columnNameCounter != columnCount - 2)
            {
                header += String.format("%-27s", columnNames[columnNameCounter]);   
            }
            else
            {
                header += String.format(columnNames[columnNameCounter]);
            }
        }
        drawLine(header);
    }

    public static void printValues(ResultSet rs)
    {
        try
        {
            while(rs.next())
            {
                for(int valueCounter = 1; valueCounter < columnCount; valueCounter++)
                {
                    System.out.print(String.format("%-27s", rs.getString(valueCounter))); 
                }
                System.out.println();
            } 
        }
        catch (Exception e)
        {
            printErrorMessage(e);
        }
    }
    public void exit()
    {
        try
        {
            conn.close();
            System.out.println("Exited Successfully.");
            System.exit(0);

        }
        catch(Exception e)
        {
            printErrorMessage(e);
        }
    }

}

class crudSQLite implements iCRUD
{
    static String[] columnNames;
    static Connection conn;
    static Scanner scanner = new Scanner(System.in);
    static int choice;
    static String uniqueValue;
    static String tableName;
    static Statement stmt;
    static int columnCount;
    static String header = "";
    static String values = "";
    static String activeStatus = "a";

    crudSQLite()
    {
        System.out.print("Enter database name: ");
        String databaseName = scanner.nextLine();
        String url = "jdbc:sqlite:" + databaseName;
        System.out.print("Enter the table name to work on: ");
        tableName = scanner.nextLine();


        try
        {
            conn = DriverManager.getConnection(url);  
            
        }
        catch (Exception e)
        {
            printErrorMessage(e);
            System.out.println("Please recheck database name, table name and try again!");
            System.exit(0);
        }
    }

    public static void printErrorMessage(Exception e)
    {
        System.out.println("Got an exception!");
        System.out.println(e.getMessage());
    }

    public static void printMessage()
    {
        System.out.println("Invalid input!");

    }

    public static void getColumnNames()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData metadata = rs.getMetaData();
            columnCount = metadata.getColumnCount();
            columnNames = new String[columnCount];
            for (int counter = 0; counter < columnCount; counter ++)
            {
                columnNames[counter] = metadata.getColumnName(counter + 1);
            }

        }
        catch (Exception e)
        {
            printErrorMessage(e);
        }
    }
    public static void drawLine(String header)
    {
        System.out.println(header);
        for(int counter = 0; counter < header.length(); counter++)
        {
            System.out.print('-');
        }
        System.out.println();
    }


    public void create()
    {   
        String query = "insert into "+ tableName +" values(";
        try
        {
            getColumnNames();

            for(int columnCounter = 0; columnCounter < columnCount - 1; columnCounter++)
            {
                System.out.print("Please enter " + columnNames[columnCounter] + ": ");
                query += "'" + scanner.nextLine() + "', ";
            }
            query += "'" + activeStatus + "')";
            stmt.executeUpdate(query);

            System.out.println(tableName + " added to database!");
        }
        
        catch(Exception e)
        {
            printErrorMessage(e);
        }
    }


    public void read()
    {
        try
        {
            getColumnNames();
            ResultSet rs = stmt.executeQuery("select * from " + tableName + " where " + columnNames[columnNames.length - 1]+ " = '" + activeStatus + "'");
            header = "";
            printColumnNames();
            printValues(rs);
        }
        catch(Exception e)
        {
            printErrorMessage(e);
        }
    }
    public static void printColumnNames()
    {
        for(int columnNameCounter = 0; columnNameCounter < columnCount - 1 ; columnNameCounter++)
        {
            if(columnNameCounter != columnCount - 2)
            {
                header += String.format("%-27s", columnNames[columnNameCounter]);   
            }
            else
            {
                header += String.format(columnNames[columnNameCounter]);
            }
        }
        drawLine(header);
    }

    public static void printValues(ResultSet rs)
    {
        try
        {
            while(rs.next())
            {
                for(int valueCounter = 1; valueCounter < columnCount; valueCounter++)
                {
                    System.out.print(String.format("%-27s", rs.getString(valueCounter))); 
                }
                System.out.println();
            } 
        }
        catch (Exception e)
        {
            printErrorMessage(e);
        }
    }
    public void exit()
    {
        try
        {
            conn.close();
            System.out.println("Exited Successfully.");
            System.exit(0);

        }
        catch(Exception e)
        {
            printErrorMessage(e);
        }
    }    
}


















class FrameWork
{
	public static void main(String[] args)
	{
        Scanner scanner = new Scanner(System.in);
		try
		{
			String className = args[0];
			iCRUD interfaceCRUD = (iCRUD)Class.forName(className).newInstance();

			while(true)
	        {
	            String welcome = "Welcome to database";
	            drawLine(welcome);
	            System.out.print("1. Add a record\n2. Display all available records\n3. Exit\nEnter your choice: ");
	            try
	            {
	                int choice = scanner.nextInt();
	                scanner.nextLine();
	                switch (choice)
	                {
	                    case 1: interfaceCRUD.create();
	                            break;
	                    case 2: interfaceCRUD.read();
	                            break;
	                    case 3: interfaceCRUD.exit();

	                    default: printMessage();
	                }
	            }
	            catch (Exception e)
	            {
	                scanner.next();
	                printMessage();
	            }
	            System.out.println();
	        }
		}
		catch(Exception e)
        {
            printErrorMessage(e);
            System.out.println("Enter class name as an argument and try again!");
            System.exit(0);
        }
	}

    public static void printMessage()
    {
        System.out.println("Invalid input!");

    }
    public static void drawLine(String header)
    {
        System.out.println(header);
        for(int counter = 0; counter < header.length(); counter++)
        {
            System.out.print('-');
        }
        System.out.println();
    }
    public static void printErrorMessage(Exception e)
    {
        System.out.println("Got an exception!");
        System.out.println(e.getMessage());
    }

}
