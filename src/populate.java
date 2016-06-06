import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class populate 
{

    public static void main(String[] args)
    {
    	if(args.length != 3)
    	{
    		JOptionPane.showMessageDialog(null, "Incorrect number of arguments are entered. Program will exit now"); 
    		System.exit(0); 
    	}	
        else
        {	
             try 
             { 
            	 Class.forName("oracle.jdbc.driver.OracleDriver"); 
             } 
             catch(ClassNotFoundException cnfe) 
             { 
            	 System.out.println("Error loading driver: " + cnfe); 
             }
             String host = "dagobah.engr.scu.edu"; 
             String dbName = "db11g"; 
             int port = 1521; 
             String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
             String username = "rgoginen";
             String password = "Vpsvpsvps1";
             try
             {
            	 Connection c = DriverManager.getConnection(oracleURL,username,password);
            	 Statement s1=c.createStatement();
            	 String delete_from_building_table="DELETE from building";
            	 s1.executeQuery(delete_from_building_table);
            	 s1.close();
            	 Statement s2=c.createStatement();
            	 String delete_from_firebuilding_table="DELETE from firebuilding";
            	 s2.executeQuery(delete_from_firebuilding_table);
            	 s2.close();
            	 Statement s3=c.createStatement();
            	 String delete_from_hydrant_table="DELETE from hydrant";
            	 s3.executeQuery(delete_from_hydrant_table);
            	 s3.close();
            	 File f1=new File(args[0]);
                 File f2=new File(args[1]);
                 File f3=new File(args[2]);
                 try
                 {
                	 FileInputStream fstream1 = new FileInputStream(f1);
                	 BufferedReader b1=new BufferedReader(new InputStreamReader(fstream1));
                	 String l1;
                	 String insert_into_building_table;
                	 String values1[];
                	 while((l1=b1.readLine())!=null)
                	 {
                		 values1=l1.split(",");
                		 insert_into_building_table="insert into building values('"+values1[0]+"','"+values1[1].substring(1)+"',"+values1[2].replace(" ","")+",SDO_GEOMETRY(2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY(";
                		 for(int i=3;i<values1.length;i++)
                        	 insert_into_building_table+=""+values1[i]+",";
                         insert_into_building_table+=values1[3]+","+values1[4]+")))";
                         Statement s4=c.createStatement();
                		 s4.executeUpdate(insert_into_building_table);
                		 s4.close();
                	 } 
                	 b1.close();
                	 fstream1.close();
                	 FileInputStream fstream2 = new FileInputStream(f2);
                	 BufferedReader b2=new BufferedReader(new InputStreamReader(fstream2));
                	 String l2;
                	 String insert_into_hydrant_table;
                	 String values2[];
                	 while((l2=b2.readLine())!=null)
                	 {
                		 values2=l2.split(",");
                		 insert_into_hydrant_table="insert into hydrant values('"+values2[0]+"',SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+values2[1].replace(" ", "")+","+values2[2].replace(" ", "")+",NULL),NULL,NULL))";
                         
                		 Statement s5=c.createStatement();
                		 s5.executeUpdate(insert_into_hydrant_table);
                		 s5.close();
                	 } 
                	 b2.close();
                	 fstream2.close();
                	 FileInputStream fstream3 = new FileInputStream(f3);
                	 BufferedReader b3=new BufferedReader(new InputStreamReader(fstream3));
                	 String l3;
                	 String value3;
                	 String insert_into_firebuilding_table;
                	 while((l3=b3.readLine())!=null)
                	 {
                		 if(!l3.equals(""))
                		 {
                		 	value3=l3.substring(0, 3);
                		 	insert_into_firebuilding_table="insert into firebuilding values('"+value3+"')";
	                		 Statement s6=c.createStatement();
    	            		 s6.executeUpdate(insert_into_firebuilding_table);
        	        		 s6.close();
                		 }
                	 } 
                	 b3.close();
                	 fstream3.close();
                 }
                 catch(IOException e)
                 {
                	System.out.println("IOException :"+e.getMessage()); 
                 }
            	 c.close();
             }
             catch (SQLException E) 
             { 
            	 System.out.println("SQLException: "+ E.getMessage());
            	 System.out.println("SQLState: "+ E.getSQLState());
            	 System.out.println("VendorError: " + E.getErrorCode());
            	 E.printStackTrace();
             }
        }
    }
}   
