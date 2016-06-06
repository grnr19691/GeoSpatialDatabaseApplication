/**
 * Created by grnr1 on 6/6/2016.
 */


import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;



class labeld extends JLabel implements MouseMotionListener,MouseListener
{
    Connection c=null;

    Statement srb12cb1=null;
    Statement srb12cb2=null;
    Statement srb12cb3=null;
    Statement srb3=null;



    boolean rb1=false;
    boolean rb2=false;
    boolean rb3=false;
    boolean rb4=false;

    boolean cb1=false;
    boolean cb2=false;
    boolean cb3=false;

    boolean submitbutton=false;

    boolean points_default=false;

    boolean polygonclose=false;

    boolean rb44=false;

    String queryb;
    String queryfb;
    String queryh;
    String query3;
    String query3f;
    String q4ordinates;

    Point trackPoint=new Point();



    int x,y;
    ArrayList points=new ArrayList();
    ArrayList bq4=new ArrayList();
    ArrayList bq4ordinates=new ArrayList();




    labeld(ImageIcon g)
    {
        super(g);
        addMouseMotionListener(this);
        addMouseListener(this);

    }

    public void paintComponent(Graphics g)
    {
        if(!rb2 && !points_default)
        {
            points.clear();
            points.add(new Point(0,0));
            points.add(new Point(820,0));
            points.add(new Point(820,580));
            points.add(new Point(0,580));
            polygonclose=true;
            points_default=true;
        }

        Polygon poly=new Polygon();
        super.paintComponent(g);

        if(rb2)
        {
            int numpoints=points.size();
            g.setColor(Color.red);
            if(numpoints==0)
            {
                return;
            }

            Point prevPoint=(Point)points.get(0);
            poly.addPoint(prevPoint.x, prevPoint.y);

            Iterator it=points.iterator();

            while(it.hasNext())
            {
                poly.addPoint(prevPoint.x, prevPoint.y);
                Point curPoint=(Point)it.next();
                g.setColor(Color.red);
                //g.fillRect(prevPoint.x-4, prevPoint.y-4, 8, 8);
                //g.fillRect(curPoint.x-4, curPoint.y-4, 8, 8);
                g.drawLine(prevPoint.x,prevPoint.y,curPoint.x,curPoint.y);
                prevPoint=curPoint;
                poly.addPoint(prevPoint.x, prevPoint.y);
            }
            if(polygonclose)
            {
                g.drawLine(prevPoint.x,prevPoint.y,((Point)points.get(0)).x,((Point)points.get(0)).y);
                g.drawPolygon(poly);
            }
        }
        if(rb4)
        {
            int numpoints=points.size();
            g.setColor(Color.red);
            if(numpoints==0)
            {
                return;
            }

            for(int b=0;b<points.size();b++)
            {

                String query4p="select b.shape.sdo_ordinates,b.name from building b where sdo_anyinteract(b.shape,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+((Point)points.get(b)).x+","+((Point)points.get(b)).y+",NULL),NULL,NULL))='TRUE'";
                try
                {
                    srb12cb1=c.createStatement();
                    srb12cb1.execute(query4p);
                    ResultSet res=srb12cb1.getResultSet();

                    if(res!=null)
                    {
                        while(res.next())
                        {
                            Array coordsInfo=res.getArray(1);

                            Object[] vertice = (Object[]) coordsInfo.getArray();
                            int qx1[]=new int[vertice.length/2-1];
                            int qy1[]=new int[vertice.length/2-1];
                            for(int i=0,j=0; j < qx1.length;i=i+2,j++)
                            {
                                qx1[j]=((BigDecimal)vertice[i]).intValue();
                                qy1[j]=((BigDecimal)vertice[i+1]).intValue();
                            }
                            Polygon qp=new Polygon(qx1,qy1,qx1.length);
                            g.setColor(Color.red);
                            g.drawPolygon(qp);
                            String tempString=res.getString(2);
                            int checkString=0;
                            for(int c=0;c<bq4.size();c++)
                            {
                                if(((String)bq4.get(c)).equals(tempString))
                                {
                                    checkString++;
                                }
                            }
                            if(checkString==0)
                            {
                                bq4.add(tempString);
                                {
                                    for(int f=0;f<vertice.length;f++)
                                    {
                                        if(f==0)
                                            q4ordinates=""+vertice[f];
                                        else
                                            q4ordinates+=","+vertice[f];

                                    }
                                    bq4ordinates.add(q4ordinates);
                                }
                            }
                        }
                    }
                    srb12cb1.close();
                }
                catch (SQLException e)
                {
                    System.out.println("SQLException: "+ e.getMessage());
                    System.out.println("SQLState: "+ e.getSQLState());
                    System.out.println("VendorError: " + e.getErrorCode());
                    e.printStackTrace();
                }

            }
            if(submitbutton)
            {
                rb44=false;
                for(int d=0;d<bq4.size();d++)
                {
                    String query4h="select b.X,b.Y from (select sdo_nn_distance(1) dist,t.X X,t.Y Y,rank() over (order by sdo_nn_distance(1)) rank from hydrant h,TABLE(SDO_UTIL.GETVERTICES(h.location)) t WHERE SDO_NN(h.location,SDO_Geometry (2003,null,null,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY("+bq4ordinates.get(d)+")),1) = 'TRUE' order by sdo_nn_distance(1)) b where b.rank=1";
                    String temp="select t.X,t.Y from hydrant h,TABLE(SDO_UTIL.GETVERTICES(h.location)) t WHERE SDO_NN(h.location,SDO_Geometry (2003,null,null,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY("+bq4ordinates.get(d)+")),'sdo_num_res=1') = 'TRUE'";
                    try
                    {
                        srb12cb3=c.createStatement();
                        srb12cb3.execute(query4h);

                        ResultSet res=srb12cb3.getResultSet();
                        if(res!=null)
                        {
                            while(res.next())
                            {
                                int qx1=res.getInt(1);
                                int qy1=res.getInt(2);
                                g.setColor(Color.green);
                                g.fillRect(qx1-4,qy1-4,8,8);
                            }
                        }
                        srb12cb3.close();

                    }
                    catch (SQLException e)
                    {
                        System.out.println("SQLException: "+ e.getMessage());
                        System.out.println("SQLState: "+ e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
            }
        }
        if(submitbutton)
        {
            if(rb1)
            {
                if(cb1)
                {
                    queryb="SELECT B.shape.sdo_ordinates FROM building B";

                    try
                    {
                        srb12cb1=c.createStatement();
                        srb12cb1.execute(queryb);
                        ResultSet res=srb12cb1.getResultSet();
                        if(res!=null)
                        {
                            while(res.next())
                            {
                                Array coordsInfo=res.getArray(1);
                                Object[] vertice = (Object[]) coordsInfo.getArray();
                                int qx1[]=new int[vertice.length/2-1];
                                int qy1[]=new int[vertice.length/2-1];
                                for(int i=0,j=0; j < qx1.length;i=i+2,j++)
                                {
                                    qx1[j]=((BigDecimal)vertice[i]).intValue();
                                    qy1[j]=((BigDecimal)vertice[i+1]).intValue();
                                }
                                Polygon qp=new Polygon(qx1,qy1,qx1.length);
                                g.setColor(Color.yellow);
                                g.drawPolygon(qp);
                            }
                        }
                        srb12cb1.close();
                    }
                    catch (SQLException e)
                    {
                        System.out.println("SQLException: "+ e.getMessage());
                        System.out.println("SQLState: "+ e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
                if(cb2)
                {
                    queryfb="select b.shape.sdo_ordinates from building b,firebuilding f where f.name=b.name";
                    try
                    {
                        srb12cb2=c.createStatement();
                        srb12cb2.execute(queryfb);



                        ResultSet res=srb12cb2.getResultSet();
                        if(res!=null)
                        {
                            while(res.next())
                            {

                                Array coordsInfo=res.getArray(1);

                                Object[] vertice = (Object[]) coordsInfo.getArray();
                                int qx1[]=new int[vertice.length/2-1];
                                int qy1[]=new int[vertice.length/2-1];
                                for(int i=0,j=0; j < qx1.length;i=i+2,j++)
                                {
                                    qx1[j]=((BigDecimal)vertice[i]).intValue();
                                    qy1[j]=((BigDecimal)vertice[i+1]).intValue();
                                }
                                Polygon qp=new Polygon(qx1,qy1,qx1.length);
                                g.setColor(Color.red);
                                g.drawPolygon(qp);
                                //	System.out.println("Success");
                            }

                        }
                        srb12cb2.close();

                    } catch (SQLException e)
                    {
                        System.out.println("SQLException: "+ e.getMessage());
                        System.out.println("SQLState: "+ e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                        e.printStackTrace();
                    }

                }
                if(cb3)
                {
                    queryh="select t.X,t.Y from hydrant h,TABLE(SDO_UTIL.GETVERTICES(h.location)) t";
                    try
                    {
                        srb12cb3=c.createStatement();
                        srb12cb3.execute(queryh);



                        ResultSet res=srb12cb3.getResultSet();
                        if(res!=null)
                        {
                            while(res.next())
                            {
                                int qx1=res.getInt(1);
                                int qy1=res.getInt(2);
                                g.setColor(Color.green);
                                g.fillRect(qx1-4,qy1-4,8,8);
                            }
                        }
                        srb12cb3.close();
                    }
                    catch (SQLException e)
                    {
                        System.out.println("SQLException: "+ e.getMessage());
                        System.out.println("SQLState: "+ e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
            }
            else if(rb2)
            {
                if(cb1)
                {
                    queryb="select b.shape.sdo_ordinates from building b where sdo_anyinteract(b.shape,sdo_geometry(2003,null,null,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(";
                    for(int m=0;m<points.size();m++)
                    {
                        queryb+=((Point)points.get(m)).x+","+((Point)points.get(m)).y+",";
                    }
                    queryb+=((Point)points.get(0)).x+","+((Point)points.get(0)).y;
                    queryb+=")))='TRUE'";
                    try
                    {
                        srb12cb1=c.createStatement();
                        srb12cb1.execute(queryb);
                        ResultSet res=srb12cb1.getResultSet();
                        if(res!=null)
                        {
                            while(res.next())
                            {
                                Array coordsInfo=res.getArray(1);
                                Object[] vertice = (Object[]) coordsInfo.getArray();
                                int qx1[]=new int[vertice.length/2-1];
                                int qy1[]=new int[vertice.length/2-1];
                                for(int i=0,j=0; j < qx1.length;i=i+2,j++)
                                {
                                    qx1[j]=((BigDecimal)vertice[i]).intValue();
                                    qy1[j]=((BigDecimal)vertice[i+1]).intValue();
                                }
                                Polygon qp=new Polygon(qx1,qy1,qx1.length);
                                g.setColor(Color.yellow);
                                g.drawPolygon(qp);
                            }
                        }
                        srb12cb1.close();
                    }
                    catch (SQLException e)
                    {
                        System.out.println("SQLException: "+ e.getMessage());
                        System.out.println("SQLState: "+ e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
                if(cb2)
                {
                    queryfb="select b.shape.sdo_ordinates from building b,firebuilding f where f.name=b.name and sdo_anyinteract(b.shape,sdo_geometry(2003,null,null,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(";
                    for(int m=0;m<points.size();m++)
                    {
                        queryfb+=((Point)points.get(m)).x+","+((Point)points.get(m)).y+",";
                    }
                    queryfb+=((Point)points.get(0)).x+","+((Point)points.get(0)).y;
                    queryfb+=")))='TRUE'";
                    try
                    {
                        srb12cb2=c.createStatement();
                        srb12cb2.execute(queryfb);
                        ResultSet res=srb12cb2.getResultSet();
                        if(res!=null)
                        {
                            while(res.next())
                            {
                                Array coordsInfo=res.getArray(1);
                                Object[] vertice = (Object[]) coordsInfo.getArray();
                                int qx1[]=new int[vertice.length/2-1];
                                int qy1[]=new int[vertice.length/2-1];
                                for(int i=0,j=0; j < qx1.length;i=i+2,j++)
                                {
                                    qx1[j]=((BigDecimal)vertice[i]).intValue();
                                    qy1[j]=((BigDecimal)vertice[i+1]).intValue();
                                }
                                Polygon qp=new Polygon(qx1,qy1,qx1.length);
                                g.setColor(Color.red);
                                g.drawPolygon(qp);
                            }
                        }
                        srb12cb2.close();
                    }
                    catch (SQLException e)
                    {
                        System.out.println("SQLException: "+ e.getMessage());
                        System.out.println("SQLState: "+ e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
                if(cb3)
                {
                    queryh="select t.X,t.Y from hydrant h,TABLE(SDO_UTIL.GETVERTICES(h.location)) t where sdo_anyinteract(h.location,sdo_geometry(2003,null,null,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(";
                    for(int m=0;m<points.size();m++)
                    {
                        queryh+=((Point)points.get(m)).x+","+((Point)points.get(m)).y+",";
                    }
                    queryh+=((Point)points.get(0)).x+","+((Point)points.get(0)).y;
                    queryh+=")))='TRUE'";
                    try
                    {
                        srb12cb3=c.createStatement();
                        srb12cb3.execute(queryh);
                        ResultSet res=srb12cb3.getResultSet();
                        if(res!=null)
                        {
                            while(res.next())
                            {
                                int qx1=res.getInt(1);
                                int qy1=res.getInt(2);
                                g.setColor(Color.green);
                                g.fillRect(qx1-4,qy1-4,8,8);
                            }
                        }
                        srb12cb3.close();
                    }
                    catch (SQLException e)
                    {
                        System.out.println("SQLException: "+ e.getMessage());
                        System.out.println("SQLState: "+ e.getSQLState());
                        System.out.println("VendorError: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
            }
            else if(rb3)
            {
                query3="select b.shape.sdo_ordinates from building b,building fb,firebuilding f where sdo_within_distance(b.shape,fb.shape,'distance=100')='TRUE' and fb.name=f.name";
                try
                {
                    srb3=c.createStatement();
                    srb3.execute(query3);
                    ResultSet res=srb3.getResultSet();
                    if(res!=null)
                    {
                        while(res.next())
                        {
                            Array coordsInfo=res.getArray(1);
                            Object[] vertice = (Object[]) coordsInfo.getArray();
                            int qx1[]=new int[vertice.length/2-1];
                            int qy1[]=new int[vertice.length/2-1];
                            for(int i=0,j=0; j < qx1.length;i=i+2,j++)
                            {
                                qx1[j]=((BigDecimal)vertice[i]).intValue();
                                qy1[j]=((BigDecimal)vertice[i+1]).intValue();
                            }
                            Polygon qp=new Polygon(qx1,qy1,qx1.length);
                            g.setColor(Color.yellow);
                            g.drawPolygon(qp);
                        }
                    }
                    srb3.close();
                }
                catch (SQLException e)
                {
                    System.out.println("SQLException: "+ e.getMessage());
                    System.out.println("SQLState: "+ e.getSQLState());
                    System.out.println("VendorError: " + e.getErrorCode());
                    e.printStackTrace();
                }
                query3f="select b.shape.sdo_ordinates from building b,firebuilding f where f.name=b.name";
                try
                {
                    srb12cb2=c.createStatement();
                    srb12cb2.execute(query3f);
                    ResultSet res=srb12cb2.getResultSet();
                    if(res!=null)
                    {
                        while(res.next())
                        {
                            Array coordsInfo=res.getArray(1);
                            Object[] vertice = (Object[]) coordsInfo.getArray();
                            int qx1[]=new int[vertice.length/2-1];
                            int qy1[]=new int[vertice.length/2-1];
                            for(int i=0,j=0; j < qx1.length;i=i+2,j++)
                            {
                                qx1[j]=((BigDecimal)vertice[i]).intValue();
                                qy1[j]=((BigDecimal)vertice[i+1]).intValue();
                            }
                            Polygon qp=new Polygon(qx1,qy1,qx1.length);
                            g.setColor(Color.red);
                            g.drawPolygon(qp);
                        }
                    }
                    srb12cb2.close();
                }
                catch (SQLException e)
                {
                    System.out.println("SQLException: "+ e.getMessage());
                    System.out.println("SQLState: "+ e.getSQLState());
                    System.out.println("VendorError: " + e.getErrorCode());
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e)
    {
        x=e.getX();
        y=e.getY();
        if(rb2 && !polygonclose)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                points.add(new Point(x,y));
                repaint();
            }
            else
            {
                polygonclose=true;
                repaint();
            }
        }
        else if(rb4 && rb44)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                points.add(new Point(x,y));
                repaint();
            }
        }
    }
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e)
    {

    }
    @Override
    public void mouseExited(java.awt.event.MouseEvent e)
    {

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e)
    {

    }
}