package calendar;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import common.DBConnectionMgr;

public class EastWork extends JFrame implements ActionListener {
   String workdata = null;
   String leavedata = null;
   public Calendar cal = Calendar.getInstance();
   //Vector<String> cols = new Vector<String>();
   //Vector<String> data = new Vector<String>();
   String wcols[] = {"출근 시간"};
   String wdata[][] = new String[0][1];
   String lcols[] = {"퇴근 시간"};
   String ldata[][] = new String[0][1];
   Font font = new Font("맑은 고딕", Font.BOLD, 15);
   Font font_time = new Font("맑은 고딕", Font.PLAIN, 11);
   //db에 연걸하기 위한 필요자원 선언
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	DBConnectionMgr dbMgr = null;
	Map<String, String> callData = null;
	Main80 m80 =null;
   
   
   public String time_work = null;
   public String time_leave = null;

   DefaultTableModel wdtm = new DefaultTableModel(wdata, wcols) {
      public boolean isCellEditable(int d, int c) {
         return false;

      }
   };
   DefaultTableModel ldtm = new DefaultTableModel(ldata, lcols) {
      public boolean isCellEditable(int d, int c) {
         return false;
         
      }
   };
   JTable wjtb = new JTable(wdtm);
   JScrollPane wjsp = new JScrollPane(wjtb);
   JTable ljtb = new JTable(ldtm);
   JScrollPane ljsp = new JScrollPane(ljtb);
   JButton work = new JButton("출근");
   JButton leave = new JButton("퇴근");

   int workrow = 0;
   int leaverow = 0;

   String imgPath = "../2nd.SemiProject/src/image/";
   ImageIcon imgIcon = new ImageIcon(imgPath + "watercolor.jpg");

   class BackGroundPanel extends JPanel { // 배경화면을 위해서 내부에 클래스 지정
      public void paintComponent(Graphics g) {
         g.drawImage(imgIcon.getImage(), 0, 0, null);
         setOpaque(false);
         super.paintComponent(g);

      }
   }

	public EastWork(Main80 m80) {
		this.m80 = m80;
		callData = m80.lv.check_ok;
		initDisplay();
	}

   public void initDisplay() {

      //cols.addElement("출근시간");
      //cols.addElement("퇴근시간");


      //data.addElement("1");
      //data.addElement("1");
      
      this.setContentPane(new BackGroundPanel());
      wjsp.setBounds(50, 20, 150, 300);
      ljsp.setBounds(200, 20, 150, 300);
      work.setBounds(50, 340, 140, 50);
      work.setFont(font);
      leave.setBounds(210, 340, 140, 50);
      leave.setFont(font);
      wjtb.getTableHeader().setReorderingAllowed(false); // 이동 불가
      wjtb.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
      ljtb.getTableHeader().setReorderingAllowed(false); // 이동 불가
      ljtb.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
      //wjtb.setFont(font_time);// ?? 테이블은 폰트 적용이 안되고 있음
      
      this.add(wjsp);
      this.add(ljsp);
      this.add(work);
      this.add(leave);
      this.setLayout(null);// 패널에 레이아웃 위치 설정 및 배치
      this.setTitle("근태관리");
      this.setResizable(false);// 크기조정 불가
      this.setSize(420, 450);
      this.setVisible(true);
      this.setLocationRelativeTo(null);// 가운데 띄우게하기
      work.addActionListener(this);
      leave.addActionListener(this);

   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy / MM / dd / HH:mm:ss");
      String time = sdf.format(cal.getTime());
      Object obj = ae.getSource();
      if (work == obj) {
         //dtm.addRow(new Object[] {time, workrow, 0});
         wdtm.addRow(new Object[] {time, null, 0});
         time_work= time;
         workdata = new String(time);
         workrow++;
         work.setEnabled(false);
         leave.setEnabled(true);

      } else if (leave == obj) {
         //dtm.addRow(new Object[] {time, leaverow, 1});
         ldtm.addRow(new Object[] {time, null, 0});
         time_leave = time;
         leavedata = new String(time);
         leaverow++;
         leave.setEnabled(false);
         work.setEnabled(true);
         recordWork();

      }

   }
   public void recordWork() {//근태기록 저장
		StringBuilder sql_insert = new StringBuilder();
		sql_insert.append("insert into record_work_data(SELECT (SELECT member_code From account_info where id =?), ?, ? FROM dual)");
		dbMgr = DBConnectionMgr.getInstance();
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql_insert.toString());
			int i = 1;
			pstmt.setString(i++, callData.get("id"));
			pstmt.setString(i++, workdata);
			pstmt.setString(i++, leavedata);
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		try {
			int result = pstmt.executeUpdate(); //executeUpdate()는 영향을 받는 row(세로)줄을 숫자로 표시함. 예를 들어 추가 되면 1개의 로우가 반응되어 1의  값을 가짐. 실패하면 0을 받음.
			if(result > 0) {
				System.out.println("ok");
			}else {
				System.out.println("최종 등록 실패");
			}
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		dbMgr.freeConnection(con, pstmt); //사용한 건 자원 반납.
	}
		
	}

}