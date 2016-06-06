/**
 * Created by grnr1 on 6/6/2016.
 */


import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;

public class Main
{
    public static void main(String[] args)
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
        String mysqlURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        String username = "rgoginen";
        String password = "Vpsvpsvps1";
        Polygon queryPolygon=null;
        int x1[]={865,1136,1136,865};
        int y1[]={180,180,301,301};
        final Polygon poly1=new Polygon(x1,y1,x1.length);
        int x2[]={865,1136,1136,865};
        int y2[]={70,70,147,147};
        final Polygon poly2=new Polygon(x2,y2,x2.length);
        final JFrame window=new JFrame();
        final JPanel panel=new JPanel()
        {
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawPolygon(poly1);
                g.drawPolygon(poly2);
            }
        };
        final Button b=new Button();
        window.setLayout(null);
        panel.setLayout(null);
        window.setContentPane(panel);
        ImageIcon image = new ImageIcon("map.jpg");
        final JLabel l4=new JLabel();
        final labeld l1=new labeld(image);
        try
        {
            l1.c = DriverManager.getConnection(oracleURL,username,password);
        }
        catch (SQLException E)
        {
            System.out.println("SQLException: "+ E.getMessage());
            System.out.println("SQLState: "+ E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            E.printStackTrace();
        }
        l1.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e)
            {
                l4.setText("Current mouse position : ("+e.getX()+","+e.getY()+")");
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e)
            {
                l4.setText("Current mouse position : ("+e.getX()+","+e.getY()+")");
                l1.x=e.getX();
                l1.y=e.getY();
            }
        });
        final JLabel l2=new JLabel("Query");
        l2.setFont(new Font("Serif", Font.BOLD, 20));
        final JLabel l3=new JLabel("Active Feature Type");
        l3.setFont(new Font("Serif", Font.BOLD, 20));
        final JTextArea t1=new JTextArea();
        t1.setEditable(false);
        JScrollPane s1=new JScrollPane(t1);
        s1.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        final JCheckBox c1=new JCheckBox("Buildings");
        final JCheckBox c2=new JCheckBox("Buildings on Fire");
        final JCheckBox c3=new JCheckBox("Hydrants");
        c1.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(c1.isSelected())
                {
                    l1.cb1=true;
                }
                else
                {
                    l1.cb1=false;
                    l1.queryb=null;
                }
            }

        });
        c2.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(c2.isSelected())
                {
                    l1.cb2=true;
                }
                else
                {
                    l1.cb2=false;
                    l1.queryfb=null;
                }

            }

        });
        c3.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(c3.isSelected())
                {
                    l1.cb3=true;
                }
                else
                {
                    l1.cb3=false;
                    l1.queryh=null;
                }
            }
        });
        final JButton bb1=new JButton("Submit Query");
        bb1.setFont(new Font("Serif", Font.BOLD, 20));
        bb1.addActionListener(new ActionListener()
        {
            int counter=0;
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!l1.polygonclose && (l1.points).size()==0)
                    JOptionPane.showMessageDialog(null, "No points are selected yet. Please draw the polygon");
                else if(!l1.polygonclose)
                    JOptionPane.showMessageDialog(null, "Polygon is not closed. Please close the polygon");
                else
                {
                    counter++;
                    l1.submitbutton=true;
                    l1.repaint();
                    if(l1.rb1)
                    {
                        if(l1.cb1)
                        {
                            String queryb1;
                            queryb1="select b.shape.sdo_ordinates from building b";
                            t1.append("Q.no-"+counter+":"+"Whole region:Query for building:"+queryb1+"\n");
                        }
                        if(l1.cb2)
                        {
                            String queryfb1;
                            queryfb1="select b.shape.sdo_ordinates from building b,firebuilding f where f.name=b.name";
                            t1.append("Q.no-"+counter+":"+"Whole region:Query for firebuilding:"+queryfb1+"\n");
                        }
                        if(l1.cb3)
                        {
                            String queryh1;
                            queryh1="select t.X,t.Y from hydrant h,TABLE(SDO_UTIL.GETVERTICES(h.location)) t from hydrant h";
                            t1.append("Q.no-"+counter+":"+"Whole region:Query for hydrant:"+queryh1+"\n");
                        }
                    }
                    else if(l1.rb2)
                    {
                        if(l1.cb1)
                        {
                            String queryb1;
                            queryb1="select b.shape.sdo_ordinates,b.name from building b where sdo_anyinteract(b.shape,sdo_geometry(2003,null,null,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(";
                            for(int m=0;m<l1.points.size();m++)
                            {
                                queryb1+=((Point)l1.points.get(m)).x+","+((Point)l1.points.get(m)).y+",";
                            }
                            queryb1+=((Point)l1.points.get(0)).x+","+((Point)l1.points.get(0)).y;
                            queryb1+=")))='TRUE'";
                            t1.append("Q.no-"+counter+":"+"Range query:Query for building:"+queryb1+"\n");
                        }
                        if(l1.cb2)
                        {
                            String queryfb1;
                            queryfb1="select b.shape.sdo_ordinates from building b,firebuilding f where f.name=b.name and sdo_anyinteract(b.shape,sdo_geometry(2003,null,null,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(";
                            for(int m=0;m<l1.points.size();m++)
                            {
                                queryfb1+=((Point)l1.points.get(m)).x+","+((Point)l1.points.get(m)).y+",";
                            }
                            queryfb1+=((Point)l1.points.get(0)).x+","+((Point)l1.points.get(0)).y;
                            queryfb1+=")))='TRUE'";
                            t1.append("Q.no-"+counter+":"+"Range query:Query for firebuilding:"+queryfb1+"\n");
                        }
                        if(l1.cb3)
                        {
                            String queryh1;
                            queryh1="select t.X,t.Y from hydrant h,TABLE(SDO_UTIL.GETVERTICES(h.location)) t where sdo_anyinteract(h.location,sdo_geometry(2003,null,null,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(";
                            for(int m=0;m<l1.points.size();m++)
                            {
                                queryh1+=((Point)l1.points.get(m)).x+","+((Point)l1.points.get(m)).y+",";
                            }
                            queryh1+=((Point)l1.points.get(0)).x+","+((Point)l1.points.get(0)).y;
                            queryh1+=")))='TRUE'";
                            t1.append("Q.no-"+counter+":"+"Range query:Query for hydrant:"+queryh1+"\n");
                        }
                    }
                    else if(l1.rb3)
                    {
                        String query3="select b.shape.sdo_ordinates from building b,building fb,firebuilding f where sdo_nn(b.shape,fb.shape,'distance=100')='TRUE' and fb.name=f.name";
                        String query3f="select b.shape.sdo_ordinates from building b,firebuilding f where f.name=b.name";
                        t1.append("Q.no-"+counter+":Neighbor to fire building:"+query3+"\n"+"Q.no-"+counter+":Neighbor to fire building:"+query3f+"\n");
                    }
                    else if(l1.rb4)
                    {
                        for(int b=0;b<l1.points.size();b++)
                        {
                            String query4p="select b.shape.sdo_ordinates,b.name from building b where sdo_anyinteract(b.shape,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+((Point)l1.points.get(b)).x+","+((Point)l1.points.get(b)).y+",NULL),NULL,NULL))='TRUE'";
                            t1.append("Q.no-"+counter+":closest hydrant:"+query4p+"\n");
                        }
                        for(int d=0;d<l1.bq4.size();d++)
                        {
                            String query4h="select t.X,t.Y from hydrant h,TABLE(SDO_UTIL.GETVERTICES(h.location)) t WHERE SDO_NN(h.location,SDO_Geometry (2003,null,null,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY("+l1.bq4ordinates.get(d)+")),'sdo_num_res=1') = 'TRUE'";
                            t1.append("Q.no-"+counter+":closest hydrant:"+query4h+"\n");
                        }
                    }
                }
            }
        });
        final JRadioButton b1=new JRadioButton("Whole Region");
        final JRadioButton b2=new JRadioButton("Range Query");
        final JRadioButton b3=new JRadioButton("Find Neighbor Buildings");
        final JRadioButton b4=new JRadioButton("Find Closets Fire Hydrants");
        b1.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(((JRadioButton) e.getSource()).isSelected())
                {
                    l1.rb1=true;
                }
                else
                {
                    l1.rb1=false;
                    l1.submitbutton=false;
                    l1.repaint();
                }
            }
        });
        b2.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(((JRadioButton) e.getSource()).isSelected())
                {
                    l1.points_default=false;
                    l1.rb2=true;
                    l1.points.clear();
                    l1.polygonclose=false;
                }
                else
                {
                    l1.rb2=false;
                    l1.points.clear();
                    l1.polygonclose=false;
                    l1.submitbutton=false;
                    l1.repaint();
                }
            }
        });
        b3.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(((JRadioButton) e.getSource()).isSelected())
                {
                    l1.rb3=true;
                }
                else
                {
                    l1.rb3=false;
                    l1.repaint();
                    l1.submitbutton=false;
                }
            }

        });
        b4.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if(((JRadioButton) e.getSource()).isSelected())
                {
                    l1.points.clear();
                    l1.rb4=true;
                    l1.rb44=true;
                }
                else
                {
                    l1.rb4=false;
                    l1.rb44=false;
                    l1.submitbutton=false;
                    l1.points.clear();
                    l1.bq4.clear();
                    l1.q4ordinates=null;
                    l1.bq4ordinates.clear();
                    l1.repaint();
                }
            }
        });
        final ButtonGroup g=new ButtonGroup();
        g.add(b1);
        g.add(b2);
        g.add(b3);
        g.add(b4);
        panel.add(l1);
        panel.add(l2);
        panel.add(l3);
        panel.add(l4);
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(c1);
        panel.add(c2);
        panel.add(c3);
        panel.add(s1);
        panel.add(bb1);
        l1.setBounds(10,10,820,580);
        l2.setBounds(880,180,180,20);
        l3.setBounds(880,70,180,20);
        l4.setBounds(10,590,200,20);
        b1.setBounds(880,205,180,20);
        b2.setBounds(880,227,180,20);
        b3.setBounds(880,249,180,20);
        b4.setBounds(880,271,180,20);
        c1.setBounds(880,95,180,20);
        c2.setBounds(880,117,118,20);
        c3.setBounds(998,117,118,20);
        s1.setBounds(10,630,1126,60);
        bb1.setBounds(880,405,250,25);
        window.addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent)
            {
                try
                {
                    (l1.c).close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        window.setTitle("Raja Nageswara Rao Gogineni 00001138177");
        window.setSize(1000,1000);
        window.setVisible(true);
    }
}
