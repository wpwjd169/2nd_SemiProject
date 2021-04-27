package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main80Center extends JPanel implements ActionListener {
	Main80East me 		= null;
	JPanel top 			= new JPanel(new BorderLayout());// 전체 레이아웃
	JPanel center 		= new JPanel(new BorderLayout());
	JPanel mon_to_sun 	= new JPanel(new GridLayout(1, 7));// 요일 처리(sun~mon)
	JPanel one_to_thirty = new JPanel(new GridLayout(0, 7));// 달력 처리(1~31)
	JLabel yearmonth	= new JLabel();// 상단 년월
	JButton prev 		= new JButton("이전");
	JButton next 		= new JButton("다음");
	JButton[] btn_num 	= new JButton[31];// 달력 버튼
	JLabel[] lbl_day 	= new JLabel[7];// 요일 라벨
	Font dayfont 		= new Font("", Font.BOLD, 18);// 요일 폰트
	GregorianCalendar c = new GregorianCalendar();// 동양력
	Calendar curr 		= Calendar.getInstance();
	int year			= curr.get(Calendar.YEAR);// 현재 년
	int month 			= curr.get(Calendar.MONTH) + 1;// 월은 0부터 계산 고로 +1 해주기
	int date 			= curr.get(Calendar.DAY_OF_MONTH);
	int dom[]			= { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };// 월별 마지막 날짜
	String[] day 		= { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };// 요일 라벨 처리를 위한 배열
	String[] num   		= {"1","2","3","4","5","6","7","8","9","10","11","12"//날짜 버튼 처리를 위한 배열
						  ,"13","14","15","16","17","18","19","20","21","22"
            			  ,"23","24","25","26","27","28","29","30","31"};


	public Main80Center(Main80East e) {
		this.me = e;
		initDisplay(0, 0);
	}

	public void initDisplay(int mm, int yy) {
		int dayInMonth = dom[mm];//
		if (c.isLeapYear(c.get(Calendar.YEAR)) && mm == 1)// 윤달
			++dayInMonth;
		for (int i = 0; i < lbl_day.length; i++) {// 요일의 갯수만큼 라벨 생성
			lbl_day[i] = new JLabel(day[i]);
			mon_to_sun.add(lbl_day[i]);// 패널에 라벨 배치
			lbl_day[i].setFont(dayfont);// 라벨 폰트
			lbl_day[i].setHorizontalAlignment(JLabel.CENTER);// 라벨 가운데 정렬
		}
		lbl_day[0].setForeground(Color.RED);// 일요일 글자 색상 설정
		lbl_day[6].setForeground(Color.BLUE);// 토요일 글자 색상 설정

		this.setLayout(new BorderLayout());// 패널에 레이아웃 위치 설정 및 배치
		this.add("North", top);
		this.add("Center", center);
		center.add("North", mon_to_sun);
		center.add("Center", one_to_thirty);
		yearmonth.setHorizontalAlignment(JLabel.CENTER);// 상단 라벨 가운데 정렬
		yearmonth.setText(String.valueOf(year) + "년 " + String.valueOf(month) + "월");// 현재 달 상단 연월 표시
		yearmonth.setFont(new Font("맑은 고딕", Font.BOLD, 30));// 상단 연월 폰트 및 크기
		top.add(yearmonth, BorderLayout.CENTER);// 패널에 레이아웃 위치 설정 및 배치
		top.add(prev, BorderLayout.WEST);
		top.add(next, BorderLayout.EAST);
		top.setBackground(new Color(255, 255, 255));
		prev.setPreferredSize(new Dimension(100, 45));// prev, next버튼 크기설정
		prev.setBackground(new Color(255, 255, 255));
		prev.addActionListener(this);
		next.setPreferredSize(new Dimension(100, 45));
		next.setBackground(new Color(255, 255, 255));
		next.addActionListener(this);
		mon_to_sun.setBackground(new Color(255, 255, 255));
		one_to_thirty.setBackground(new Color(255, 255, 255));

		dayshow(year, month);

	}

	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		if (obj instanceof JButton) {
			JButton eventBtn = (JButton) obj;
			if (eventBtn.equals(prev)) {
				if (month == 1) {// 1월일때 prev버튼을 누르면
					year--;// 이전 년으로 나오도록 하게 하고
					month = 12;// month를 12월로 전환
				} else {
					month--;
				}
			} else if (eventBtn.equals(next)) {
				if (month == 12) {// 위와 동일한 if문
					year++;
					month = 1;
				} else {
					month++;
				}
			}
			yearmonth.setText(String.valueOf(year) + "년 " + String.valueOf(month) + "월");// 상단 라벨에 년월 표시

			newshow();// newshow 실행
		}
	}

	public void newshow() {
		one_to_thirty.setVisible(false);// 기존 달력 패널 숨기고
		one_to_thirty.removeAll();// 지우기
		dayshow(year, month);// 새로운 달력 출력
		one_to_thirty.setVisible(true);// 새로운 달력 패널 나타내기
	}
	public void dayshow(int y, int m) {
		c.set(y, m - 1, 1); // 출력할 달력
		int week = c.get(Calendar.DAY_OF_WEEK);// 시작 요일
												// ex)Calendar.SUNDAY 1, Calendar.TUESDAY 2......Calendar MONDAY 7
		for (int i = 1; i < week; i++) {
			one_to_thirty.add(new JLabel(""));// i개수만큼의 공백 출력
		}
		int lastDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);// 해당 달의 마지막 날
		for (int i = 0; i < lastDate; i++) {
			one_to_thirty.add(btn_num[i] = new JButton(num[i]));// i개수만큼의 버튼 출력
			btn_num[i].setBackground(new Color(255, 255, 255));
			btn_num[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String date = ((JButton) e.getSource()).getText();
					String cur_date = (String.valueOf(year) + ". " + String.valueOf(month) + ". "+ date + ".");
					me.jlb_cur.setText(cur_date);
					
					try{
						File f = new File("PlanData/"+me.jlb_cur.getText()+".txt");
						System.out.println(f);
						if(f.exists()){
							System.out.println(me.jlb_cur.getText()+"메모 확인");
							BufferedReader in = new BufferedReader(new FileReader("PlanData/"+me.jlb_cur.getText()+".txt"));
							String memoAreaText= new String();
							while(true){
								String tempStr = in.readLine();
								if(tempStr == null) break;
								memoAreaText = memoAreaText + tempStr + System.getProperty("line.separator");
							}
							me.jta.setText(memoAreaText);
							System.out.println(memoAreaText);
							in.close();	
						}
						else me.jta.setText(null);
					} catch (IOException ie) {
						ie.printStackTrace();
					}
				}
			});
		}
	}
}
