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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class StuAdmin implements ActionListener{
	JFrame frame;
	JTable table;
	DefaultTableModel tableModel;
	Vector vcolumn = new Vector();
	Vector vdata = new Vector();
	JButton jButtonInsert,jButtonSearch,jButtonUpdate,jButtonDelete;
	JTextField jTextFieldSno,jTextFieldName,jTextFieldClass,jTextFieldCode;
	JLabel lSno,lName,lClass,lCode;
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	StuAdmin(){
		frame = new JFrame("ѧ������");
		jButtonInsert = new JButton("���");
		jButtonSearch = new JButton("����");
		jButtonUpdate = new JButton("�޸�");
		jButtonDelete = new JButton("ɾ��");
		jTextFieldSno = new JTextField(20) ;
		jTextFieldName = new JTextField(20) ;
		jTextFieldClass = new JTextField(20);
		jTextFieldCode = new JTextField(20);
		lSno = new JLabel("ѧ��");
		lName = new JLabel("����");
		lClass = new JLabel("�༶");
		lCode = new JLabel("����");
		JPanel pButton = new JPanel();
		pButton.setLayout(new GridLayout(5,1));
		pButton.add(jButtonInsert);
		pButton.add(jButtonSearch);
		pButton.add(jButtonUpdate);
		pButton.add(jButtonDelete);
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(4,2));
		p2.add(lSno);
		p2.add(jTextFieldSno);
		p2.add(lName);
		p2.add(jTextFieldName);
		p2.add(lClass);
		p2.add(jTextFieldClass);
		p2.add(lCode);
		p2.add(jTextFieldCode);
		pButton.add(p2);
		vcolumn.add("ѧ��");
		vcolumn.add("����");
		vcolumn.add("�༶");
		vcolumn.add("����");
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
			System.out.println("���������ɹ���");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("��������ʧ�ܣ�");
		}
		try{
			conn=DriverManager.getConnection(dbURL,userName,userPwd);
			stmt = conn.createStatement();
			System.out.println("�������ݿ�ɹ���");
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.print("SQL Server����ʧ�ܣ�");
		}
		String ssql;
		ssql = "select * from Student";
		vdata.removeAllElements();
		try{
			rs = stmt.executeQuery(ssql);
			while(rs.next()){
				Vector vrow = new Vector();
				vrow.add(rs.getString(1));
				vrow.add(rs.getString(2));
				vrow.add(rs.getString(3));
				vrow.add(rs.getString(4));
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
		String ClassName = jTextFieldClass.getText();
		String Code = jTextFieldCode.getText();
		if(e.getSource() == jButtonInsert){
			if(StuNum.equals("") ){
				JOptionPane.showMessageDialog(frame, "ѧ�Ų���Ϊ�գ�", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
			}else{
				String ssql;
				ssql = "select * from Student where StuNum='"+StuNum+"'";
				try{
					rs = stmt.executeQuery(ssql);
					if(rs.next()){					
						JOptionPane.showMessageDialog(frame, "ѧ���ظ���", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
					}else{
						ssql = "insert into Student values('"+StuNum+"','"+StuName+"','"+ClassName+"','"+Code+"')"; 
						stmt.executeUpdate(ssql);
						JOptionPane.showMessageDialog(frame, "��ӳɹ���", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
					}		
				}catch(SQLException sqle){
					sqle.printStackTrace();
				}
			}
		}else if(e.getSource() == jButtonSearch){
			String ssql;
			vdata.removeAllElements();
			if(StuNum.equals("")){
				ssql = "select * from Student";
			}else{
				ssql = "select * from Student where StuNum='"+StuNum+"'";
			}
			try{
				rs = stmt.executeQuery(ssql);
				while(rs.next()){
					Vector vrow = new Vector();
					vrow.add(rs.getString(1));
					vrow.add(rs.getString(2));
					vrow.add(rs.getString(3));
					vrow.add(rs.getString(4));
					vdata.addElement(vrow);
				}
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}
			tableModel = new DefaultTableModel( vdata, vcolumn);
			table.setModel(tableModel);
		}else if(e.getSource() == jButtonUpdate){
			if(StuNum.equals("") ){
				JOptionPane.showMessageDialog(frame, "ѧ�Ų���Ϊ�գ�", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
			}else{
				String ssql;
				ssql = "select * from Student where StuNum='"+StuNum+"'";
				try{
					rs = stmt.executeQuery(ssql);
					if(rs.next()){
						ssql = "update Student set StuName='"+StuName+"' where StuNum='"+StuNum+"'";
						stmt.executeUpdate(ssql);
						ssql = "update Student set Class='"+ClassName+"' where StuNum='"+StuNum+"'";
						stmt.executeUpdate(ssql);
						ssql = "update Student set Code='"+Code+"' where StuNum='"+StuNum+"'";
						stmt.executeUpdate(ssql);
						JOptionPane.showMessageDialog(frame, "�޸ĳɹ�", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(frame, "��ѧ�Ų����ڣ�", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
					}		
				}catch(SQLException sqle){
					sqle.printStackTrace();
				}
			}
		}else if(e.getSource() == jButtonDelete){
			if(StuNum.equals("") ){
				JOptionPane.showMessageDialog(frame, "ѧ�Ų���Ϊ�գ�", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
			}else{
				String ssql;
				ssql = "select * from Student where StuNum='"+StuNum+"'";
				try{
					rs = stmt.executeQuery(ssql);
					if(rs.next()){
						ssql = "delete from Student where StuNum='"+StuNum+"'";
						stmt.executeUpdate(ssql);
						JOptionPane.showMessageDialog(frame, "ɾ���ɹ�", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(frame, "��ѧ�Ų����ڣ�", "��Ϣ�Ի���", JOptionPane.INFORMATION_MESSAGE);
					}		
				}catch(SQLException sqle){
					sqle.printStackTrace();
				}
			}
		}
	}
}
