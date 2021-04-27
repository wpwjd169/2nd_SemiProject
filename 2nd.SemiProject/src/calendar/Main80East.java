package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import calculator.MyCalcView;

public class Main80East extends JPanel{

	JPanel top 		= new JPanel(new BorderLayout());
	JPanel bottom 	= new JPanel(new BorderLayout());
	JPanel sd 		= new JPanel(new GridLayout());
	JTextArea jta 	= new JTextArea();
	JScrollPane jsp = new JScrollPane(jta);
	JButton exit 	= new JButton("나가기");
	JButton btn_work = new JButton("근태관리");
	JButton btn_send = new JButton("메일쓰기");
	JButton save 	= new JButton("저장");
	JButton delete 	= new JButton("삭제");
	Font dayfont 	= new Font("맑은 고딕", Font.BOLD, 18);// 요일 폰트
	Calendar curr 		= Calendar.getInstance();
	int year			= curr.get(Calendar.YEAR);// 현재 년
	int month 			= curr.get(Calendar.MONTH) + 1;
	int date 			= curr.get(Calendar.DAY_OF_MONTH);
	JLabel 	jlb_cur = new JLabel("Wellcome");
	Main80 m80 = null;
	Map<String, String> callData = null;
	MyCalcView Mycal = null;
	
	ImageIcon normal = new ImageIcon("../2nd.SemiProject/src/image/calculator1.png");
	Image img = normal.getImage();
	Image changeImg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
	ImageIcon changeIcon = new ImageIcon(changeImg);
	JButton jbtn_calculator = new JButton(changeIcon); 
	
	public Main80East(Main80 m80) {
		this.m80 = m80;
		initDisplay();
	}
	
	private void initDisplay() {
		jta.setFont(new Font("맑은 고딕",Font.PLAIN,18));
		jta.setLineWrap(true);
		jsp.setPreferredSize(new Dimension(200,300));
		jlb_cur.setFont(dayfont);
		jlb_cur.setHorizontalAlignment(JLabel.CENTER);// 상단 라벨 가운데 정렬
		jlb_cur.setPreferredSize(new Dimension(200,45));
		save.setPreferredSize(new Dimension(100,35));
		save.setBackground(new Color(255, 255, 255));
		delete.setPreferredSize(new Dimension(100,35));
		delete.setBackground(new Color(255, 255, 255));
		jbtn_calculator.setPreferredSize(new Dimension(100,35));
		jbtn_calculator.setBackground(new Color(255, 255, 255));
		btn_send.setPreferredSize(new Dimension(150,100));
		btn_send.setBackground(new Color(255, 255, 255));
		btn_work.setPreferredSize(new Dimension(150,100));
		btn_work.setBackground(new Color(255, 255, 255));
		exit.setPreferredSize(new Dimension(150,50));
		exit.setBackground(new Color(255, 255, 255));
		sd.add("West", save);
		sd.add("Center", delete);
		sd.add("East", jbtn_calculator);
		top.add("North", jlb_cur);
		top.add("Center", jsp);
		top.add("South", sd);
		top.setBackground(new Color(255, 255, 255));
		bottom.add("East", btn_work);
		bottom.add("West", btn_send);
		bottom.add("South", exit);
		bottom.setBackground(new Color(255, 255, 255));

		this.setLayout(new BorderLayout());
		this.add("North", top);
		this.add("South", bottom);
		this.setBackground(new Color(255, 255, 255));
		
		jbtn_calculator.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Mycal = new MyCalcView();
			}
		});

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btn_work.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				new EastWork(m80);
			}
		});
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent b) {
				new EastMessage(m80);
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(jlb_cur.getText());
				try {
					File f= new File("PlanData");
					if(!f.isDirectory()) f.mkdir();
					
					String memo = jta.getText();
					if(memo.length()>0){
						BufferedWriter out = new BufferedWriter(new FileWriter("PlanData/"+jlb_cur.getText()+".txt"));
						String str = jta.getText();
						out.write(str);  
						out.close();
					}
				} catch (IOException e) {}
			}					
		});
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jta.setText("");
				File f =new File("PlanData/"+jlb_cur.getText()+".txt");
				if(f.exists()){
					f.delete();
				}
			}					
		});
	}	
}
