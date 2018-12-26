package exam;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class StudentSearch implements ActionListener{

    JFrame frame;
    String StuNum;
    JLabel title;
    JComboBox cbSubject,cbCredit,cbPass;
    JLabel lSubject,lCredit,lPass;
    JButton bSearchSubject,bSearchCredit,bSearchPass,bDisplay;
	JTable table;
	DefaultTableModel tableModel;
	Vector vcolumn = new Vector();
	Vector vdata = new Vector();
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
    StudentSearch(String StudentNum){
    	StuNum = StudentNum;
    	frame = new JFrame("成绩查询");
        title = new JLabel("考试成绩查询");
        title.setHorizontalAlignment(JLabel.CENTER);
        cbSubject = new JComboBox();
        cbCredit = new JComboBox();
        cbPass = new JComboBox();
        lSubject = new JLabel("科目：");
        lSubject.setHorizontalAlignment(JLabel.RIGHT);
        lCredit = new JLabel("学分：");
        lCredit.setHorizontalAlignment(JLabel.RIGHT);
        lPass = new JLabel("是否通过：");
        lPass.setHorizontalAlignment(JLabel.RIGHT);
        bSearchSubject = new JButton("查询");
        bSearchCredit = new JButton("查询");
        bSearchPass = new JButton("查询");
        bDisplay = new JButton("显示全部");
        JPanel pm = new JPanel();
        pm.setLayout(new BorderLayout(10,10));
        JPanel pSearch = new JPanel();
        pSearch.setLayout(new GridLayout(4,1));
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(lSubject);
        p1.add(cbSubject);
        p1.add(bSearchSubject);
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.add(lCredit);
        p2.add(cbCredit);
        p2.add(bSearchCredit);
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(lPass);
        p3.add(cbPass);
        p3.add(bSearchPass);
        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout());
        p4.add(bDisplay);
        pSearch.add(p1);
        pSearch.add(p2);
        pSearch.add(p3);
        pSearch.add(p4);
		vcolumn.add("科目");
		vcolumn.add("成绩");
		vcolumn.add("考试时间");
		vcolumn.add("学分");
		tableModel = new DefaultTableModel(vdata,vcolumn);
		table = new JTable(tableModel);
		JScrollPane sp = new JScrollPane(table);
	    pm.add(BorderLayout.CENTER,sp);
	    pm.add(BorderLayout.NORTH,title);
	    pm.add(BorderLayout.WEST,pSearch);
	    frame.add(pm);
		frame.setVisible(true);
		frame.setSize(1000, 650);
		frame.setLocationRelativeTo(null);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		bSearchSubject.addActionListener(this);
		bSearchCredit.addActionListener(this);
		bSearchPass.addActionListener(this);
		bDisplay.addActionListener(this);
		
		  String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		  String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=exam";
		  String userName="sa";
		  String userPwd="971204";
		 try
		{
			Class.forName(driverName);
			System.out.println("加载驱动成功！");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("加载驱动失败！");
		}
		try{
			conn=DriverManager.getConnection(dbURL,userName,userPwd);
			stmt = conn.createStatement();
			System.out.println("连接数据库成功！");
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.print("SQL Server连接失败！");
		}
		String ssql;
		ssql="select distinct subjectName from v_student_grade";
		try{
			rs = stmt.executeQuery(ssql);
			while(rs.next()){
				cbSubject.addItem(rs.getString(1));
			}
		}
		catch(SQLException sqle){
				sqle.printStackTrace();
		}
		ssql="select distinct Credit from v_student_grade";
		try{
			rs = stmt.executeQuery(ssql);
			while(rs.next()){
				cbCredit.addItem(rs.getString(1));
			}
		}
		catch(SQLException sqle){
				sqle.printStackTrace();
		}
		cbPass.addItem("是");
		cbPass.addItem("否");
		ssql="select SubjectName,score,Time,Credit from v_student_grade where StuNum='"+StuNum+"'";
		display(ssql);
    }
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == bSearchSubject){
			String SubjectName = (String)cbSubject.getSelectedItem();
			String ssql;
			ssql = "select SubjectName,score,Time,Credit from v_student_grade where StuNum='"+StuNum+"' and SubjectName='"+SubjectName+"'";
			display(ssql);
		}
		if(e.getSource() == bSearchCredit){
			String Credit = (String)cbCredit.getSelectedItem();
			String ssql;
			ssql = "select SubjectName,score,Time,Credit from v_student_grade where StuNum='"+StuNum+"' and Credit="+Credit;
			display(ssql);
		}
		if(e.getSource() == bSearchPass){
			String isPass = (String)cbPass.getSelectedItem();
			String ssql;
			if(isPass.equals("是")){
				ssql = "select SubjectName,score,Time,Credit from v_student_grade where StuNum='"+StuNum+"' and score>=60";
			}else{
				ssql = "select SubjectName,score,Time,Credit from v_student_grade where StuNum='"+StuNum+"' and score<60";
			}
			display(ssql);
		}
		if(e.getSource() == bDisplay){
			String ssql;
			ssql="select SubjectName,score,Time,Credit from v_student_grade where StuNum='"+StuNum+"'";
			display(ssql);
		}
	}
	void display(String sql){
		vdata.removeAllElements();
		String ssql = sql;
		try{
			rs = stmt.executeQuery(ssql);
			while(rs.next()){
				Vector vrow = new Vector();
				vrow.add(rs.getString(1));
				vrow.add(rs.getInt(2));
				vrow.add(rs.getTimestamp(3));
				vrow.add(rs.getDouble(4));
				vdata.addElement(vrow);
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		tableModel = new DefaultTableModel( vdata, vcolumn);
		table.setModel(tableModel);
	}

}
