package exam;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


class StudentExam implements ActionListener{

	JFrame frame;
	JButton bExam,bSearch;
	JLabel luser,ltime,lclass,luserID;
	JLabel lluser,lltime,llclass,lluserID;
	String StudentNum;
	String StudentName;
	Timestamp Time;
	Timestamp startExam;
    String className;
	
	StudentExam(String userName,String userNum,String cclassName){
		Time = new Timestamp(new Date().getTime()); 
		frame = new JFrame("学生");
		bExam = new JButton("开始考试");
		bSearch = new JButton("成绩查询");
		lluser = new JLabel("用户名:");
		lltime = new JLabel("登录时间:");
		llclass = new JLabel("班级:");
		lluserID = new JLabel("学号:");
		StudentNum = userNum;
		StudentName = userName;
		className = cclassName;
		luser = new JLabel(StudentName);
		lclass = new JLabel(className);
		luserID = new JLabel(StudentNum);
		ltime = new JLabel(Time+"");
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(lluserID);
		p1.add(luserID);
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		p2.add(llclass);
		p2.add(lclass);
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout());
		p3.add(lluser);
		p3.add(luser);
		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout());
		p4.add(lltime);
		p4.add(ltime);
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout());
		p5.add(bExam);
		p5.add(bSearch);
		JPanel pm = new JPanel();
		pm.setLayout(new GridLayout(5,1));
		pm.add(p1);
		pm.add(p2);
		pm.add(p3);
		pm.add(p4);
		pm.add(p5);
		frame.add(pm);
		frame.setVisible(true);
		frame.setSize(400, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bExam.addActionListener(this);
		bSearch.addActionListener(this);
		
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == bExam){
			startExam = new Timestamp(new Date().getTime());
			new Exam(className,startExam,StudentNum);
		}else if(e.getSource() == bSearch){
			new StudentSearch(StudentNum);
		}
	}

}
