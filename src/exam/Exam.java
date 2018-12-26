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
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Exam implements ActionListener{ 
	JFrame frame;
	JButton bSubmit;
	JLabel title;
	JLabel ltime;
	JTextArea textExam;
	JLabel [] titleAnswer = new JLabel[20];
	JTextField [] tAnswer = new JTextField[20];
	String [] questionNum = new String[20];
	String [] standardAns = new String[20];
	int totalQues;
	String className;
	Timestamp Time;
	Timestamp beginTime;
	Timestamp endTime;
	String subjectNum;
	String stuNum;
	Connection conn;
	Statement stmt;
	ResultSet rs;
	int score;
	TimeThread r;	
	Exam(String cclassName,Timestamp userTime,String studentNum){
		frame = new JFrame("考试界面");
		bSubmit = new JButton("提交");
		JPanel jbutton = new JPanel();
		jbutton.setLayout(new FlowLayout());
		jbutton.add(bSubmit);
		className = cclassName;
		Time = userTime;
		stuNum = studentNum;
		score= 0;
		beginTime = new Timestamp(new Date().getTime());
		endTime = new Timestamp(new Date().getTime());
		JPanel pm = new JPanel();
		pm.setLayout(new BorderLayout(10,10));
		ltime = new JLabel();
		ltime.setHorizontalAlignment(JLabel.RIGHT);		
		title = new JLabel();
		title.setHorizontalAlignment(JLabel.CENTER);
		JPanel ptext = new JPanel();
		ptext.setLayout(new GridLayout(1,2));
		ptext.add(title);
		ptext.add(ltime);
		textExam = new JTextArea(200,200);
		textExam.setLineWrap(true);
		textExam.append("\r\n");
		JScrollPane sp = new JScrollPane(textExam);
		pm.add(BorderLayout.NORTH,ptext);
		pm.add(BorderLayout.CENTER, sp);
		pm.add(BorderLayout.SOUTH, jbutton);
		JPanel pAnswer = new JPanel();
		pAnswer.setLayout(new GridLayout(20,2));
		for(int i=0;i<20;i++){
			titleAnswer[i] = new JLabel(i+1+"");
			titleAnswer[i].setHorizontalAlignment(JLabel.RIGHT);
			pAnswer.add(titleAnswer[i]);
			tAnswer[i] = new JTextField(20);
			pAnswer.add(tAnswer[i]);
		}
		pm.add(BorderLayout.EAST, pAnswer);
		frame.add(pm);
		frame.setVisible(true);
		frame.setSize(1250, 850);
		frame.setLocationRelativeTo(null);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		bSubmit.addActionListener(this);
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
		ssql = "select * from Exam where Time<'"+Time+"' and EndTime>'"+Time+"' and Class='"+className+"'";
		System.out.println(ssql);
		try{
			rs = stmt.executeQuery(ssql);
			if(rs.next()){
				subjectNum = rs.getString(3);
				beginTime = rs.getTimestamp(1);
				endTime = rs.getTimestamp(4);
				for(int i=0;i<20;i++){
					questionNum[i]=rs.getString(i+5);
				}
				System.out.println(beginTime+"   "+endTime);
			}
			else{ 
				frame.dispose();
				return;
			}
		}
		catch(SQLException sqle){
				sqle.printStackTrace();
				frame.dispose();
		}
		ssql = "select SubjectName from Subject where SubjectNum='"+subjectNum+"'";
		try{
			rs = stmt.executeQuery(ssql);
			if(rs.next()){
				title.setText(rs.getString(1)+"考试");	
			}
		}
		catch(SQLException sqle){
				sqle.printStackTrace();
		}
		totalQues=0;
		for(int i=0;i<20;i++){
			ssql = "select * from Question where QuesNum='"+ questionNum[i] +"'";
			try{
				rs = stmt.executeQuery(ssql);
				if(rs.next()){					
				    if(rs.getString(4) != null){
				    	standardAns[totalQues]=rs.getString(5);
						textExam.append(totalQues+1+". ");
						textExam.append(rs.getString(4)+"\r\n\r\n");
						totalQues++;
				    }
				}
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}			
		}
		System.out.println(totalQues+"");
		r = new TimeThread();
		r.start();
	}
	void calculateScore(){
		for(int i=0;i<totalQues;i++){
//			System.out.println(tAnswer[i].getText()+" "+standardAns[i]);
			if(tAnswer[i].getText().equalsIgnoreCase(standardAns[i])){
				score = score+5;
			};
		}
		String ssql;
		ssql = "insert into Grade values('"+stuNum+"','"+Time+"','"+className+"','"+subjectNum+"',"+score+")";
		System.out.println(ssql);
		try{
			stmt.executeUpdate(ssql);
		}catch(SQLException sqle){
			sqle.printStackTrace();
			frame.dispose();
		}
		JOptionPane.showMessageDialog(frame, "提交成功!\r\n考试成绩为"+score, "消息对话框", JOptionPane.INFORMATION_MESSAGE);
		frame.dispose();	
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == bSubmit){
			calculateScore();
		}
	}
	class TimeThread extends Thread{
		public void run(){
			while(true){
				Timestamp nowTime = new Timestamp(new Date().getTime());
				if(endTime.getTime()>nowTime.getTime()){
				try{	int temp = (int)(endTime.getTime() - nowTime.getTime())/(1000*60);//共多少分钟
					int hours = temp/60;
					int minutes = temp-hours*60;
					int seconds = (int)(endTime.getTime() - nowTime.getTime())/1000-temp*60;
					ltime.setText("距考试结束还有  "+hours+"小时"+minutes+"分"+seconds+"秒    ");
					
						Thread.sleep(1000);
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}else{
					break;
				}
			}
			calculateScore();				
		}
	}
}