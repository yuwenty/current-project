package exam;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


class TeaAdmin implements ActionListener{
	JFrame frame;
	JTable table;
	DefaultTableModel tableModel;
	Vector vcolumn = new Vector();
	Vector vdata = new Vector();
	JButton jButtonInsert,jButtonSearch,jButtonUpdate,jButtonDelete;
	JTextField jTextFieldSno,jTextFieldName,jTextFieldCode;
	JLabel lSno,lName,lCode;
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	TeaAdmin(){
		frame = new JFrame("教师管理");
		jButtonInsert = new JButton("添加");
		jButtonSearch = new JButton("查找");
		jButtonUpdate = new JButton("修改");
		jButtonDelete = new JButton("删除");
		jTextFieldSno = new JTextField(20) ;
		jTextFieldName = new JTextField(20) ;
		jTextFieldCode = new JTextField(20);
		lSno = new JLabel("教工号");
		lName = new JLabel("姓名");
		lCode = new JLabel("密码");
		JPanel pButton = new JPanel();
		pButton.setLayout(new GridLayout(5,1));
		pButton.add(jButtonInsert);
		pButton.add(jButtonSearch);
		pButton.add(jButtonUpdate);
		pButton.add(jButtonDelete);
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(3,2));
		p2.add(lSno);
		p2.add(jTextFieldSno);
		p2.add(lName);
		p2.add(jTextFieldName);
		p2.add(lCode);
		p2.add(jTextFieldCode);
		pButton.add(p2);
		vcolumn.add("教工号");
		vcolumn.add("姓名");
		vcolumn.add("密码");
		tableModel = new DefaultTableModel(vdata,vcolumn);
		table = new JTable(tableModel);
		JScrollPane sp = new JScrollPane(table);
		JPanel pm = new JPanel();
		pm.setLayout(new BorderLayout(20,20));
		pm.add(BorderLayout.CENTER, sp);
		pm.add(BorderLayout.WEST,pButton);
		frame.add(pm);
		frame.setVisible(true);
		frame.setSize(1200, 600);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		jButtonInsert.addActionListener(this);
		jButtonSearch.addActionListener(this);
		jButtonUpdate.addActionListener(this);
		jButtonDelete.addActionListener(this);
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
		ssql = "select * from Teacher";
		vdata.removeAllElements();
		try{
			rs = stmt.executeQuery(ssql);
			while(rs.next()){
				Vector vrow = new Vector();
				vrow.add(rs.getString(1));
				vrow.add(rs.getString(2));
				vrow.add(rs.getString(3));
				vdata.addElement(vrow);
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		tableModel = new DefaultTableModel( vdata, vcolumn);
		table.setModel(tableModel);
	}
	public void actionPerformed(ActionEvent e){
		String StuNum = jTextFieldSno.getText();
		String StuName = jTextFieldName.getText();
		String Code = jTextFieldCode.getText();
		if(e.getSource() == jButtonInsert){
			if(StuNum.equals("") ){
				JOptionPane.showMessageDialog(frame, "教工号不能为空！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
			}else{
				String ssql;
				ssql = "select * from Teacher where TeaNum='"+StuNum+"'";
				try{
					rs = stmt.executeQuery(ssql);
					if(rs.next()){					
						JOptionPane.showMessageDialog(frame, "教工号重复！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
					}else{
						ssql = "insert into Teacher values('"+StuNum+"','"+StuName+"','"+Code+"')"; 
						stmt.executeUpdate(ssql);
						JOptionPane.showMessageDialog(frame, "添加成功！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
					}		
				}catch(SQLException sqle){
					sqle.printStackTrace();
				}
			}
		}else if(e.getSource() == jButtonSearch){
			String ssql;
			vdata.removeAllElements();
			if(StuNum.equals("")){
				ssql = "select * from Teacher";
			}else{
				ssql = "select * from Teacher where TeaNum='"+StuNum+"'";
			}
			try{
				rs = stmt.executeQuery(ssql);
				while(rs.next()){
					Vector vrow = new Vector();
					vrow.add(rs.getString(1));
					vrow.add(rs.getString(2));
					vrow.add(rs.getString(3));
					vdata.addElement(vrow);
				}
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
			tableModel = new DefaultTableModel( vdata, vcolumn);
			table.setModel(tableModel);
		}else if(e.getSource() == jButtonUpdate){
			if(StuNum.equals("") ){
				JOptionPane.showMessageDialog(frame, "教工号不能为空！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
			}else{
				String ssql;
				ssql = "select * from Teacher where TeaNum='"+StuNum+"'";
				try{
					rs = stmt.executeQuery(ssql);
					if(rs.next()){
						ssql = "update Teacher set TeaName='"+StuName+"' where TeaNum='"+StuNum+"'";
						stmt.executeUpdate(ssql);
						ssql = "update Teacher set Code='"+Code+"' where TeaNum='"+StuNum+"'";
						stmt.executeUpdate(ssql);
						JOptionPane.showMessageDialog(frame, "修改成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(frame, "该教工号不存在！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
					}		
				}catch(SQLException sqle){
					sqle.printStackTrace();
				}
			}
		}else if(e.getSource() == jButtonDelete){
			if(StuNum.equals("") ){
				JOptionPane.showMessageDialog(frame, "教工号不能为空！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
			}else{
				String ssql;
				ssql = "select * from Teacher where TeaNum='"+StuNum+"'";
				try{
					rs = stmt.executeQuery(ssql);
					if(rs.next()){
						ssql = "delete from Teacher where TeaNum='"+StuNum+"'";
						stmt.executeUpdate(ssql);
						JOptionPane.showMessageDialog(frame, "删除成功", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(frame, "该教工号不存在！", "消息对话框", JOptionPane.INFORMATION_MESSAGE);
					}		
				}catch(SQLException sqle){
					sqle.printStackTrace();
				}
			}
		}
	}
}
