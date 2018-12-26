package exam;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


class adminLog implements ActionListener{
	JFrame frame;
	JLabel title;
	JButton bStu,bSubject,bTeacher;
	adminLog(){
		frame = new JFrame("��ӭ��¼����ϵͳ");
		title = new JLabel("��ӭ����Ա");
		title.setHorizontalAlignment(JLabel.CENTER);
		bStu = new JButton("ѧ������");
		bSubject = new JButton("��Ŀ����");
		bTeacher = new JButton("��ʦ����");
		JPanel pm = new JPanel();
		pm.setLayout(new BorderLayout(10,10));
		pm.add(BorderLayout.NORTH, title);
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(bStu);
		p1.add(bTeacher);
		p1.add(bSubject);
		pm.add(BorderLayout.CENTER, p1);
		frame.add(pm);
		frame.setVisible(true);
		frame.setSize(400, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bStu.addActionListener(this);
		bSubject.addActionListener(this);
		bTeacher.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == bStu){
			new StuAdmin();
		}else if(e.getSource() == bSubject){
			new SubjectAdmin();
		}else if(e.getSource() == bTeacher){
			new TeaAdmin();
		}
	}
}
