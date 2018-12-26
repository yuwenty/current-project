package exam;

import java.sql.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;


public class login implements ActionListener{

	JFrame frame;
	JLabel luser,lpassword;
	JTextField tuser;
	JPasswordField tpassword;
	JRadioButton rstudent,rteacher,radmin;
	JButton bOK,bcancel;
	JPanel panel1,panel2,panel3,panel4,panel5;
	String UserName,Password,ClassName,Name;
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	class teacher implements ActionListener{
		JFrame tcon_frame;
		JLabel tcon_name;
		JButton tcon_score,tcon_build,tcon_edit;
		JPanel tcon_panel1,tcon_panel2,tcon_panel3,tcon_panel4,tcon_panel5,tcon_panel6;
		public class teacher_edit implements ActionListener {
			JFrame Tedit_frame;
			JComboBox Tedit_type,Tedit_subject;
			JButton Tedit_search,Tedit_add,Tedit_delete,Tedit_modify;
			JTable Tedit_table;
			DefaultTableModel Tedit_dtm;
			JScrollPane Tedit_scrollpane;
			JLabel Tedit_ltype,Tedit_lsubject;
			Vector Tedit_vcolumn = new Vector();
			Vector Tedit_vdata = new Vector();
			JPanel Tedit_panel1,Tedit_panel2,Tedit_panel3;
			Connection conn;
			Statement stmt;
			ResultSet rs;
			public String question,answer,str_type,str_subject,keys;			
			class AM implements ActionListener{
				JFrame Tedit_AMframe;
				JTextField Tedit_Answer;
				JTextArea Tedit_Area;
				JButton Tedit_submit;
				JScrollPane Tedit_AMscrollpane;
				JLabel Tedit_tip1,Tedit_tip2;
				JPanel Tedit_AMpanel,Tedit_AMpane2;
				String AM_title;
				
				AM(String title,String question,String answer,String keys){
					Tedit_AMframe=new JFrame(title);
					AM_title=title;
					Tedit_AMframe.setSize(600, 600);
					Tedit_Area=new JTextArea(20,50);
					Tedit_Answer=new JTextField(20);
					Tedit_Area.setLineWrap(true);
					Tedit_AMscrollpane=new JScrollPane(Tedit_Area);
					Tedit_submit=new JButton("提交");
					Tedit_tip1=new JLabel("题目：");
					Tedit_tip2=new JLabel("答案：");
					Tedit_Answer.setText(answer);
					Tedit_Area.setText(question);
					Tedit_AMpanel=new JPanel();
					Tedit_AMpane2=new JPanel();
					
					Tedit_AMpanel.setLayout(new BorderLayout(12,12));
					Tedit_AMpane2.setLayout(new FlowLayout(5,0,30));
					Tedit_AMpane2.add(Tedit_tip1);
					Tedit_AMpane2.add(Tedit_AMscrollpane);
					Tedit_AMpane2.add(Tedit_tip2);
					Tedit_AMpane2.add(Tedit_Answer);
					Tedit_AMpanel.add(BorderLayout.CENTER,Tedit_AMpane2);
					Tedit_AMpanel.add(BorderLayout.SOUTH,Tedit_submit);
					Tedit_AMframe.add(Tedit_AMpanel);
					
					
					Tedit_AMframe.setLocationRelativeTo(null);
					Tedit_AMframe.setResizable(false);
					Tedit_AMframe.setVisible(true);
					Tedit_AMframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

					Tedit_submit.addActionListener(this);
					
					
					
				}
				
				public void actionPerformed(ActionEvent e){
					
					
					if(e.getSource() == Tedit_submit)
					{
						answer=Tedit_Answer.getText();
						question=Tedit_Area.getText();
						
						if(AM_title=="教师_题库维护_新增")
						{
							String sql_add,sql_key,key,sql_subnum;
							int int_key;
							
							sql_key="SELECT COUNT(*) AS Expr1 FROM Question";
							
							try {
								rs = stmt.executeQuery(sql_key);
								rs.next();
								key=rs.getString(1);
								int_key=Integer.valueOf(key);
								int_key++;
								key=String.valueOf(int_key);
								
								while(rs.next())
								{
									rs = stmt.executeQuery("SELECT QuesNum FROM Question WHERE (QuesNum = '"+key+"')");
									int_key=Integer.valueOf(key);
									int_key++;
									key=String.valueOf(int_key);
								}
								
								sql_subnum="SELECT SubjectNum FROM Subject WHERE (SubjectName = '"+str_subject+"')";
								rs = stmt.executeQuery(sql_subnum);
								rs.next();
								str_subject=rs.getString(1);
								System.out.println(str_subject);
								sql_add="INSERT INTO Question (QuesNum, SubjectNum, Type, Problem, Answer) VALUES ('"+key+"','"+str_subject+"','"+str_type+"','"+question+"','"+answer+"')";
								stmt.executeUpdate(sql_add);
							
						
								
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if(AM_title=="教师_题库维护_修改")
						{
							String sql_modify,sql_keys;
							sql_modify="UPDATE Question SET Problem = '"+question+"', Answer = '"+answer+"' WHERE (QuesNum = '"+keys+"')";
							try {
								stmt.executeUpdate(sql_modify);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						Tedit_AMframe.dispose() ;
					}
				}
			}

			teacher_edit() throws SQLException
			{
				  String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
				  String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=exam";
				  String userName="sa";
				  String userPwd="971204";
				  conn = DriverManager.getConnection(dbURL,userName,userPwd);
					stmt = conn.createStatement();
				 try
				{
					Class.forName(driverName);
					System.out.println("加载驱动成功！");
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("加载驱动失败！");
				}
				try{
					Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
						System.out.println("连接数据库成功！");
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.print("SQL Server连接失败！");
				}		
				
				Tedit_frame=new JFrame("教师_题库维护");
				Tedit_frame.setSize(600, 550);
				String sql_subject;
				sql_subject="SELECT Subject.SubjectName FROM Subject INNER JOIN Teach ON Subject.SubjectNum = Teach.SubjectNum WHERE (Teach.TeaNum = '"
						+UserName+"')GROUP BY Subject.SubjectName";
				Vector subjectname = new Vector();
				rs = stmt.executeQuery(sql_subject);
				while(rs.next()){
					subjectname.add(rs.getString(1));

					}
				
				String typename[]={"选择","填空"};
				Tedit_type=new JComboBox(typename);
				Tedit_subject=new JComboBox(subjectname);

				Tedit_search=new JButton("查询");
				Tedit_add=new JButton("新增");
				Tedit_delete=new JButton("删除");
				Tedit_modify=new JButton("修改");
				Tedit_vcolumn.add("题号");
				Tedit_vcolumn.add("题目");
				Tedit_vcolumn.add("答案");
				Tedit_dtm=new DefaultTableModel(Tedit_vdata,Tedit_vcolumn);
				Tedit_table = new JTable(Tedit_dtm);
				Tedit_scrollpane = new JScrollPane(Tedit_table);
				Tedit_ltype=new JLabel("题型：");
				Tedit_lsubject=new JLabel("科目：");
				Tedit_panel1=new JPanel();
				Tedit_panel2=new JPanel();
				Tedit_panel3=new JPanel();
				question=" ";
				answer=" ";
				
				
				Tedit_panel1.setLayout(new FlowLayout(1,15,5));
				Tedit_panel1.add(Tedit_lsubject);
				Tedit_panel1.add(Tedit_subject);
				Tedit_panel1.add(Tedit_ltype);
				Tedit_panel1.add(Tedit_type);
				Tedit_panel1.add(Tedit_search);
				Tedit_panel2.setLayout(new FlowLayout(1,85,5));
				Tedit_panel2.add(Tedit_add);
				Tedit_panel2.add(Tedit_delete);
				Tedit_panel2.add(Tedit_modify);
				Tedit_panel3.setLayout(new BorderLayout(12,12));
				Tedit_panel3.add(BorderLayout.NORTH, Tedit_panel1);
				Tedit_panel3.add(BorderLayout.CENTER , Tedit_scrollpane);
				Tedit_panel3.add(BorderLayout.SOUTH, Tedit_panel2);
				Tedit_frame.add(Tedit_panel3);
				
				Tedit_frame.setLocationRelativeTo(null);
				Tedit_frame.setResizable(false);
				Tedit_frame.setVisible(true);
				Tedit_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				Tedit_search.addActionListener((ActionListener) this);
				Tedit_add.addActionListener((ActionListener) this);
				Tedit_delete.addActionListener((ActionListener) this);
				Tedit_modify.addActionListener((ActionListener) this);
				
			}
			
			public void actionPerformed(ActionEvent e){
				str_type=(String)Tedit_type.getSelectedItem();
				str_subject=(String)Tedit_subject.getSelectedItem();
				
				if(e.getSource() == Tedit_search)
				{
					
					String sql_search;
					sql_search="SELECT Question.QuesNum,Question.Problem,Question.Answer FROM Question INNER JOIN Subject ON Question.SubjectNum = Subject.SubjectNum WHERE     (Question.Type = '"
							+str_type+"') AND (Subject.SubjectName = '"+str_subject+"')";
					Vector Tedit_vdata = new Vector();
					
					
					try
					{
						rs = stmt.executeQuery(sql_search);
						while(rs.next())
						{
							Vector vrow = new Vector();
							vrow.add(rs.getString(1));
							vrow.add(rs.getString(2));
							vrow.add(rs.getString(3));
							Tedit_vdata.addElement(vrow);
						}
					}
					catch(SQLException sqle){
						sqle.printStackTrace();
					}
					Tedit_dtm = new DefaultTableModel( Tedit_vdata, Tedit_vcolumn);
					Tedit_table.setModel(Tedit_dtm);
					
				}
				if(e.getSource() == Tedit_add)
				{
					question=" ";
					answer=" ";
					new AM("教师_题库维护_新增",question,answer," ");
					
					
				}
				if(e.getSource() == Tedit_modify)
				{
					int selectRow=Tedit_table.getSelectedRow();
					keys=(String) Tedit_table.getValueAt(selectRow, 0);
					question=(String) Tedit_table.getValueAt(selectRow, 1);
					answer=(String) Tedit_table.getValueAt(selectRow, 2);
					new AM("教师_题库维护_修改",question,answer,keys);
					
				}
				if(e.getSource() == Tedit_delete)
				{
					int selectRow=Tedit_table.getSelectedRow();
					keys=(String) Tedit_table.getValueAt(selectRow, 0);
					String sql_delete;
					sql_delete="DELETE FROM Question WHERE (QuesNum = '"+keys+"')";
					try {
						stmt.executeUpdate(sql_delete);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Tedit_dtm.removeRow(selectRow);
					
				}

			}
			


		}
		public class teacher_build implements ActionListener{

			public int number;
			JFrame Tbuild_frame;
			JComboBox Tbuild_type,Tbuild_subject, Tbuild_class;
			JButton Tbuild_search,Tbuild_select,Tbuild_submit,Tbuild_delete;
			JTable Tbuild_untable,Tbuild_table;
			DefaultTableModel Tbuild_undtm,Tbuild_dtm;
			JScrollPane Tbuild_unscrollpane,Tbuild_scrollpane;
			JLabel Tbuild_ltype,Tbuild_lsubject,Tbuild_tip1,Tbuild_tip2,Tbuild_tip3,Tbuild_lclass,Tbuild_ltime1,Tbuild_ltime2;
			JTextField Tbuild_time1,Tbuild_time2;
			Vector Tbuild_vcolumn = new Vector();
			Vector Tbuild_vdata = new Vector();
			Vector Tbuild_unvcolumn = new Vector();
			Vector Tbuild_unvdata = new Vector();
			JPanel Tbuild_panel1,Tbuild_panel2,Tbuild_panel3,Tbuild_panel4,Tbuild_panel5,
			Tbuild_panel6,Tbuild_panel7,Tbuild_panel8,Tbuild_panel9,Tbuild_panel10,Tbuild_panel11,Tbuild_panel12,Tbuild_panel13;
			Connection conn;
			Statement stmt;
			ResultSet rs;
			
			
			teacher_build()throws SQLException{
				  String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
				  String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=exam";
				  String userName="sa";
				  String userPwd="971204";
				  conn = DriverManager.getConnection(dbURL,userName,userPwd);
					stmt = conn.createStatement();
				 try
				{
					Class.forName(driverName);
					System.out.println("加载驱动成功！");
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("加载驱动失败！");
				}
				try{
					Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
						System.out.println("连接数据库成功！");
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.print("SQL Server连接失败！");
				}		
				
				Tbuild_frame=new JFrame("教师_组卷");
				Tbuild_frame.setSize(1000, 700);
				String sql_subject;
				sql_subject="SELECT Subject.SubjectName FROM Subject INNER JOIN Teach ON Subject.SubjectNum = Teach.SubjectNum WHERE (Teach.TeaNum = '"
						+UserName+"')GROUP BY Subject.SubjectName";
				Vector subjectname = new Vector();
				rs = stmt.executeQuery(sql_subject);
				while(rs.next()){
					subjectname.add(rs.getString(1));

					}
				String typename[]={"选择","填空"};
				String classname[]={"计算1501","计算1502"};
				Tbuild_type=new JComboBox(typename);
				Tbuild_subject=new JComboBox(subjectname);
				Tbuild_class=new JComboBox(classname);
				
				
				Tbuild_vcolumn.add("题号");
				Tbuild_vcolumn.add("题目");
				Tbuild_vcolumn.add("答案");
				Tbuild_unvcolumn.add("题号");
				Tbuild_unvcolumn.add("题目");
				Tbuild_unvcolumn.add("答案");
				Tbuild_dtm=new DefaultTableModel(Tbuild_vdata,Tbuild_vcolumn);
				Tbuild_table = new JTable(Tbuild_dtm);
				Tbuild_scrollpane = new JScrollPane(Tbuild_table);
				Tbuild_undtm=new DefaultTableModel(Tbuild_unvdata,Tbuild_unvcolumn);
				Tbuild_untable = new JTable(Tbuild_undtm);
				Tbuild_unscrollpane = new JScrollPane(Tbuild_untable);
				Tbuild_ltype=new JLabel("题型：");
				Tbuild_lsubject=new JLabel("科目：");
				Tbuild_tip1=new JLabel("题目预览：");
				Tbuild_tip2=new JLabel("已选题目：");
				Tbuild_lclass=new JLabel("班级：");
				Tbuild_ltime1=new JLabel("考试开始时间：");
				Tbuild_ltime2=new JLabel("考试结束时间：");
				Tbuild_tip3=new JLabel("时间格式：2011-05-09 11:49:45");
				Tbuild_search=new JButton("查询");
				Tbuild_select=new JButton("选择");
				Tbuild_submit=new JButton("生成");
				Tbuild_delete=new JButton("删除选中行");
				Tbuild_time1=new JTextField(20);
				Tbuild_time2=new JTextField(20);
				Tbuild_panel1=new JPanel();
				Tbuild_panel2=new JPanel();
				Tbuild_panel3=new JPanel();
				Tbuild_panel4=new JPanel();
				Tbuild_panel5=new JPanel();
				Tbuild_panel6=new JPanel();
				Tbuild_panel7=new JPanel();
				Tbuild_panel8=new JPanel();
				Tbuild_panel9=new JPanel();
				Tbuild_panel10=new JPanel();
				Tbuild_panel11=new JPanel();
				Tbuild_panel12=new JPanel();
				Tbuild_panel13=new JPanel();
				
				Tbuild_panel1.setLayout(new FlowLayout(1,15,5));
				Tbuild_panel1.add(Tbuild_lsubject);
				Tbuild_panel1.add(Tbuild_subject);
				Tbuild_panel1.add(Tbuild_ltype);
				Tbuild_panel1.add(Tbuild_type);
				Tbuild_panel1.add(Tbuild_search);
				Tbuild_panel2.add(Tbuild_tip1);
				Tbuild_panel2.add(Tbuild_unscrollpane);
				Tbuild_panel3.add(Tbuild_select);
				Tbuild_panel10.setLayout(new GridLayout(5,1));
				Tbuild_panel4.add(Tbuild_ltime1);
				Tbuild_panel4.add(Tbuild_time1);
				Tbuild_panel11.add(Tbuild_ltime2);
				Tbuild_panel11.add(Tbuild_time2);
				Tbuild_panel12.add(Tbuild_tip3);
				Tbuild_panel13.add(Tbuild_lclass);
				Tbuild_panel13.add(Tbuild_class);
				Tbuild_panel10.add(Tbuild_panel13);
				Tbuild_panel10.add(Tbuild_panel4);
				Tbuild_panel10.add(Tbuild_panel11);
				Tbuild_panel10.add(Tbuild_panel12);
				Tbuild_panel10.add(Tbuild_tip2);
				Tbuild_panel5.add(Tbuild_scrollpane);
				Tbuild_panel6.add(Tbuild_submit);
				Tbuild_panel6.add(Tbuild_delete);
				Tbuild_panel7.setLayout(new BorderLayout());
				Tbuild_panel7.add(BorderLayout.NORTH, Tbuild_panel1);
				Tbuild_panel7.add(BorderLayout.CENTER, Tbuild_panel2);
				Tbuild_panel7.add(BorderLayout.SOUTH, Tbuild_panel3);
				Tbuild_panel8.setLayout(new FlowLayout(1,2,2));
				Tbuild_panel8.add( Tbuild_panel10);
				Tbuild_panel8.add(Tbuild_panel5);
				Tbuild_panel8.add( Tbuild_panel6);
				Tbuild_panel9.setLayout(new GridLayout(1,2));
				Tbuild_panel9.add(Tbuild_panel7);
				Tbuild_panel9.add(Tbuild_panel8);
				Tbuild_frame.add(Tbuild_panel9);
				
				Tbuild_frame.setLocationRelativeTo(null);
				Tbuild_frame.setResizable(false);
				Tbuild_frame.setVisible(true);
				Tbuild_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				

				Tbuild_search.addActionListener((ActionListener) this);
				Tbuild_select.addActionListener((ActionListener) this);
				Tbuild_submit.addActionListener((ActionListener) this);
				Tbuild_delete.addActionListener((ActionListener) this);
				
			}
			public void actionPerformed(ActionEvent e){
				String str_type=(String)Tbuild_type.getSelectedItem();
				String str_subject=(String)Tbuild_subject.getSelectedItem();
				String str_class=(String)Tbuild_class.getSelectedItem();
				if(e.getSource() == Tbuild_search)
				{
					
					String sql_search;
					sql_search="SELECT Question.QuesNum,Question.Problem,Question.Answer FROM Question INNER JOIN Subject ON Question.SubjectNum = Subject.SubjectNum WHERE     (Question.Type = '"
							+str_type+"') AND (Subject.SubjectName = '"+str_subject+"')";
					Vector Tbuild_unvdata = new Vector();
					
					
					try
					{
						rs = stmt.executeQuery(sql_search);
						while(rs.next())
						{
							Vector vrow = new Vector();
							vrow.add(rs.getString(1));
							vrow.add(rs.getString(2));
							vrow.add(rs.getString(3));
							Tbuild_unvdata.addElement(vrow);
						}
					}
					catch(SQLException sqle){
						sqle.printStackTrace();
					}
					Tbuild_undtm = new DefaultTableModel(Tbuild_unvdata, Tbuild_unvcolumn);
					Tbuild_untable.setModel(Tbuild_undtm);
					
				}
				if(e.getSource() == Tbuild_select)
				{
					if(Tbuild_dtm.getRowCount()>20)
					{
						JOptionPane.showMessageDialog(Tbuild_frame, "最多添加20题！","提示",JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						int selectRow=Tbuild_untable.getSelectedRow();
						Vector Tbuild_vdata = new Vector();
						Vector vrow = new Vector();
						vrow.add(Tbuild_untable.getValueAt(selectRow, 0));
						vrow.add(Tbuild_untable.getValueAt(selectRow, 1));
						vrow.add(Tbuild_untable.getValueAt(selectRow, 2));
						Tbuild_dtm.addRow(vrow);
						Tbuild_undtm.removeRow(selectRow);	
					}
					
				}
				if(e.getSource() == Tbuild_delete)
				{
					int selectRow=Tbuild_untable.getSelectedRow();
					Tbuild_dtm.removeRow(selectRow);
				}

				if(e.getSource() == Tbuild_submit)
				{
					if(Tbuild_time1.getText().equals("")&&Tbuild_time2.getText().equals(""))
					{
						JOptionPane.showMessageDialog(Tbuild_frame, "请输入时间！","提示",JOptionPane.INFORMATION_MESSAGE);
					} 
					else
					{

						Timestamp timestart = new Timestamp(System.currentTimeMillis()) ;
						timestart = Timestamp.valueOf(Tbuild_time1.getText());
						Timestamp timeend = new Timestamp(System.currentTimeMillis()) ;
						timeend = Timestamp.valueOf(Tbuild_time2.getText());
						
						String sql_build,sql_subnums,sql_check;
						String data[] =new String[20];
						for(int i=0;i<Tbuild_dtm.getRowCount();i++)
						{
							data[i]=(String)Tbuild_table.getValueAt(i, 0);
						}
						try {
							sql_check="SELECT * FROM Exam WHERE (Time = '"+Tbuild_time1.getText()+"') AND (Class = '"+str_class+"')";
							rs = stmt.executeQuery(sql_check);
							if(rs.next()){
								JOptionPane.showMessageDialog(Tbuild_frame, "该时间段该班级已有考试！","提示",JOptionPane.INFORMATION_MESSAGE);
							}
							else{
								sql_subnums="SELECT SubjectNum FROM Subject WHERE (SubjectName = '"+str_subject+"')";
								rs = stmt.executeQuery(sql_subnums);
								rs.next();
								str_subject=rs.getString(1);
								sql_build="INSERT INTO Exam (Time, Class, SubjectNum, EndTime, Q1, Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q14,Q15,Q16,Q17,Q18,Q19,Q20)VALUES ('"+timestart+"','"+str_class+"','"+str_subject+"','"+timeend+"','"
								+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"','"+data[7]+"','"+data[8]+"','"+data[9]+"','"+data[10]+"','"
										+data[11]+"','"+data[12]+"','"+data[13]+"','"+data[14]+"','"+data[15]+"','"+data[16]+"','"+data[17]+"','"+data[18]+"','"+data[19]+"')";
								stmt.executeUpdate(sql_build);

								Tbuild_time1.setText("");
								Tbuild_time2.setText("");
								
								Tbuild_dtm.setRowCount( 0 );
								JOptionPane.showMessageDialog(Tbuild_frame, "生成成功！","提示",JOptionPane.INFORMATION_MESSAGE);
							}
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				}
						
			}
		}
		public class teacher_score implements ActionListener{
			public int number;
			JFrame Tscore_frame;
			JComboBox Tscore_class,Tscore_subject;
			JButton Tscore_search;
			JTable Tscore_table;
			DefaultTableModel Tscore_dtm;
			JScrollPane Tscore_scrollpane;
			JLabel Tscore_avg,Tscore_rate,Tscore_lclass,Tscore_lsubject,Tscore_lcount,Tscore_sum;
			Vector Tscore_vcolumn = new Vector();
			Vector Tscore_vdata = new Vector();
			JPanel Tscore_panel1,Tscore_panel2,Tscore_panel3;
			Connection conn;
			Statement stmt;
			ResultSet rs;
			
			
			teacher_score() throws SQLException{
				
				  String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
				  String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=exam";
				  String userName="sa";
				  String userPwd="971204";
				  conn = DriverManager.getConnection(dbURL,userName,userPwd);
					stmt = conn.createStatement();
				 try
				{
					Class.forName(driverName);
					System.out.println("加载驱动成功！");
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("加载驱动失败！");
				}
				try{
					Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
						System.out.println("连接数据库成功！");
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.print("SQL Server连接失败！");
				}		
				
			
				
				Tscore_frame=new JFrame("教师_成绩查询");
				Tscore_frame.setSize(600, 550);
				String sql_subject;
				sql_subject="SELECT Subject.SubjectName FROM Subject INNER JOIN Teach ON Subject.SubjectNum = Teach.SubjectNum WHERE (Teach.TeaNum = '"
						+UserName+"')GROUP BY Subject.SubjectName";
				Vector subjectname = new Vector();
				rs = stmt.executeQuery(sql_subject);
				while(rs.next()){
					subjectname.add(rs.getString(1));

					}
				Tscore_subject=new JComboBox(subjectname);
				String classname[]={"计算1501","计算1502"};
				
				Tscore_class=new JComboBox(classname);
				

				Tscore_search=new JButton("查询");
				Tscore_vcolumn.add("学号");
				Tscore_vcolumn.add("姓名");
				Tscore_vcolumn.add("成绩");
				Tscore_dtm=new DefaultTableModel(Tscore_vdata,Tscore_vcolumn);
				Tscore_table = new JTable(Tscore_dtm);
				Tscore_scrollpane = new JScrollPane(Tscore_table);
				
				Tscore_avg=new JLabel("平均分：    ");
				Tscore_lcount=new JLabel("及格人数：    ");
				Tscore_sum=new JLabel("总人数：    ");
				Tscore_lclass=new JLabel("班级：");
				Tscore_lsubject=new JLabel("科目：");
				Tscore_panel1=new JPanel();
				Tscore_panel2=new JPanel();
				Tscore_panel3=new JPanel();
				
				Tscore_panel1.setLayout(new BorderLayout(12,12));
				Tscore_panel2.add(Tscore_lsubject);
				Tscore_panel2.add(Tscore_subject);
				Tscore_panel2.add(Tscore_lclass);
				Tscore_panel2.add(Tscore_class);
				Tscore_panel2.add(Tscore_search);
				Tscore_panel1.add(BorderLayout.NORTH,Tscore_panel2);
				Tscore_panel1.add(BorderLayout.CENTER,Tscore_scrollpane);
				Tscore_panel3.add(Tscore_sum);
				Tscore_panel3.add(Tscore_avg);
				Tscore_panel3.add(Tscore_lcount);
			
				Tscore_panel1.add(BorderLayout.SOUTH,Tscore_panel3);
				Tscore_frame.add(Tscore_panel1);
				
				Tscore_frame.setLocationRelativeTo(null);
				Tscore_frame.setResizable(false);
				Tscore_frame.setVisible(true);
				Tscore_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				
				
				Tscore_search.addActionListener((ActionListener) this);
				
		}
			public void actionPerformed(ActionEvent e){
				if(e.getSource() == Tscore_search)
				{
					String str_class=(String)Tscore_class.getSelectedItem();
					String str_subject=(String)Tscore_subject.getSelectedItem();
					String sql_search,sql_avg,sql_sum,sql_count;
					sql_search="SELECT Student.StuNum, Student.StuName, Grade.Score FROM Student INNER JOIN Grade ON Student.StuNum = Grade.StuNum INNER JOIN Subject ON Grade.SubjectNum = Subject.SubjectNum WHERE (Student.Class = '"
					+str_class+"') AND (Subject.SubjectName = '"+str_subject+"')";
					sql_avg="SELECT  AVG(Grade.Score) AS 平均分 FROM Student INNER JOIN Grade ON Student.StuNum = Grade.StuNum INNER JOIN Subject ON Grade.SubjectNum = Subject.SubjectNum WHERE (Student.Class = '"
							+str_class+"') AND (Subject.SubjectName = '"+str_subject+"')";
					sql_count="SELECT  COUNT(*) AS 及格人数 FROM Student INNER JOIN Grade ON Student.StuNum = Grade.StuNum INNER JOIN Subject ON Grade.SubjectNum = Subject.SubjectNum WHERE (Student.Class = '"
							+str_class+"') AND (Subject.SubjectName = '"+str_subject+"')AND (Grade.Score >= 60)";
					sql_sum="SELECT  COUNT(*) AS 总人数 FROM Student INNER JOIN Grade ON Student.StuNum = Grade.StuNum INNER JOIN Subject ON Grade.SubjectNum = Subject.SubjectNum WHERE (Student.Class = '"
							+str_class+"') AND (Subject.SubjectName = '"+str_subject+"')";
					Vector Tscore_vdata = new Vector();
					try{
						rs = stmt.executeQuery(sql_search);
						while(rs.next()){
							Vector vrow = new Vector();
							vrow.add(rs.getString(1));
							vrow.add(rs.getString(2));
							vrow.add(rs.getString(3));
							Tscore_vdata.addElement(vrow);
						}
						rs = stmt.executeQuery(sql_avg);
						rs.next();
						Tscore_avg.setText("平均分："+rs.getString(1)+"  ");
						
						rs = stmt.executeQuery(sql_count);
						rs.next();
						Tscore_lcount.setText("及格人数："+rs.getString(1)+"  ");
					
						rs = stmt.executeQuery(sql_sum);
						rs.next();
						Tscore_sum.setText("总人数："+rs.getString(1)+"  ");
					
						
						
						
					}catch(SQLException sqle){
						sqle.printStackTrace();
					}
					
					Tscore_dtm = new DefaultTableModel( Tscore_vdata, Tscore_vcolumn);
					Tscore_table.setModel(Tscore_dtm);
				}
			}
		}
		
		
		teacher(String Name){
			tcon_frame=new JFrame("教师界面");
			tcon_frame.setSize(250, 400);
			tcon_name=new JLabel("欢迎      "+Name+" 老师");
			tcon_score=new JButton("成绩查询");
			tcon_build=new JButton("组卷");
			tcon_edit=new JButton("试题编辑");
			tcon_panel1=new JPanel();
			tcon_panel2=new JPanel();
			tcon_panel3=new JPanel();
			tcon_panel4=new JPanel();
			tcon_panel5=new JPanel();
			tcon_panel6=new JPanel();

			
			tcon_panel1.add(tcon_name);
			tcon_panel6.setLayout(new GridLayout(3,1));
			tcon_panel2.add(tcon_score);
			tcon_panel4.add(tcon_build);
			tcon_panel5.add(tcon_edit);
			tcon_panel6.add(tcon_panel2);
			tcon_panel6.add(tcon_panel4);
			tcon_panel6.add(tcon_panel5);
			tcon_panel3.setLayout(new BorderLayout(25,25));
			tcon_panel3.add(BorderLayout.NORTH,tcon_panel1);
			tcon_panel3.add(BorderLayout.CENTER,tcon_panel6);
			tcon_frame.add(tcon_panel3);
			
			tcon_frame.setLocationRelativeTo(null);
			tcon_frame.setResizable(false);
			tcon_frame.setVisible(true);
			tcon_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			tcon_score.addActionListener(this);
			tcon_build.addActionListener(this);
			tcon_edit.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e){
			if(e.getSource()== tcon_score)
			{
				try {
					new teacher_score();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(e.getSource()== tcon_build)
			{
				try {
					new teacher_build();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(e.getSource()== tcon_edit)
			{
				try {
					new teacher_edit();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		}
		
		
	}
	
	
	login() throws SQLException{
		  String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		  String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=exam";
		  String userName="sa";
		  String userPwd="971204";
		  conn = DriverManager.getConnection(dbURL,userName,userPwd);
			stmt = conn.createStatement();
		 try
		{
			Class.forName(driverName);
			System.out.println("加载驱动成功！");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("加载驱动失败！");
		}
		try{
			Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
				System.out.println("连接数据库成功！");
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.print("SQL Server连接失败！");
		}		
		frame=new JFrame("用户登录");
		frame.setSize(280, 200);
		luser=new JLabel("用户名：");
		lpassword=new JLabel("密    码：");
		tuser=new JTextField(15);
		tpassword=new JPasswordField(15);
		rstudent=new JRadioButton("学生",true);
		rteacher=new JRadioButton("教师");
		radmin=new JRadioButton("管理员");
		bOK=new JButton("登录");
		bcancel=new JButton("取消");
		JPanel panel1=new JPanel();
		JPanel panel2=new JPanel();
		JPanel panel3=new JPanel();
		JPanel panel4=new JPanel();
		JPanel panel5=new JPanel();
		
		
		panel1.setLayout(new GridLayout(2,1));
		FlowLayout flow1=new FlowLayout();
		FlowLayout flow2=new FlowLayout();
		flow1.setHgap(8);
		flow1.setVgap(15);
		flow2.setHgap(35);
		flow2.setVgap(2);
		panel2.setLayout(flow1);
		panel3.setLayout(new GridLayout(2,1,25,10));
		panel4.setLayout(flow1);
		panel5.setLayout(flow2);
		ButtonGroup bgroup=new ButtonGroup();
		bgroup.add(rstudent);
		bgroup.add(rteacher);
		bgroup.add(radmin);
		
		panel5.add(bOK);
		panel5.add(bcancel);
		panel4.add(rstudent);
		panel4.add(rteacher);
		panel4.add(radmin);
		panel3.add(panel4);
		panel3.add(panel5);
		panel2.add(luser);
		panel2.add(tuser);
		panel2.add(lpassword);
		panel2.add(tpassword);
		panel1.add(panel2);
		panel1.add(panel3);
		frame.add(panel1);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		bOK.addActionListener(this);
		bcancel.addActionListener(this);
		
		
	}
	public void actionPerformed(ActionEvent e){
		UserName=tuser.getText();
		Password=tpassword.getText();

		if(e.getSource() == bOK)
		{
			if(rstudent.isSelected())
			{
				
				try {
					String sql;
					sql="SELECT  StuNum, Code,StuName,Class FROM Student WHERE (StuNum = '"+UserName+"')";
					rs = stmt.executeQuery(sql);
					if(rs.next())
					{
						int length=Password.length();
						if(Password.equals(rs.getString(2).substring(0,length)))
						{
							
							Name=rs.getString(3);			//学生姓名
							ClassName=rs.getString(4);      //班级
							new StudentExam(Name,UserName,ClassName);
//、、、、、、、、、、、、、、、、、、、、、、、、、、	////////////////////////////////////////////////////////////						
						}
						else
						{
							JOptionPane.showMessageDialog(frame, "密码或用户名错误！");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "密码或用户名错误！");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(rteacher.isSelected())
			{
				try {
					String sql;
					sql="SELECT  TeaNum, Code,TeaName FROM Teacher WHERE (TeaNum = '"+UserName+"')";
					rs = stmt.executeQuery(sql);
					if(rs.next())
					{
						int length=Password.length();
						if(Password.equals(rs.getString(2).substring(0,length)))
						{
					////////////////////////////////////////////////////////////////////////////////////
							Name=rs.getString(3);
							System.out.println(Name);
							frame.dispose();
							new teacher(Name);
						}
						else
						{
							JOptionPane.showMessageDialog(frame, "密码或用户名错误！");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "密码或用户名错误！");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(radmin.isSelected())
			{
				if(UserName.equals("admin"))
				{
					if(Password.equals("666666"))
					{
						new adminLog();
						System.out.println("管理员登陆成功！");
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "密码或用户名错误！");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "密码或用户名错误！");
				}
				
			}
		}
		if(e.getSource() == bcancel)
		{
			System.exit(0);
		}
	}


	
	public static void main(String[] args) throws Exception {
		
		new login();
	}

}
