package calendar;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.CreateAccountVO;
import common.DBConnectionMgr;

public class EastMessage extends JFrame {
	//db에 연결하기 위해 사용하는 자원들 변수 선언
	Connection con = null;
	CallableStatement cstmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	DBConnectionMgr dbMgr = null;
	Map<String, String> callData = null; //loginView에서 받은 계정 사용하기 위한 선언.
	//////////////////////////
	Main80 m80 = null;
	CreateAccountVO caVO = null;
	JLabel jlb_sen = new JLabel("보내는 사람");
	JLabel jlb_rec = new JLabel("받는 사람");
	JLabel jlb_title = new JLabel("제목");
	JTextField jtf_sen_mail = new JTextField(20);
	JTextField jtf_rec_mail = new JTextField(20);
	JTextField jtf_title = new JTextField(20);
	JTextArea jta = new JTextArea();
	JScrollPane jsp = new JScrollPane(jta);
	JButton send = new JButton("보내기");
	
	Font font = new Font("맑은 고딕", Font.BOLD, 15);
	
	String imgPath = "../2nd.SemiProject/src/image/";
	ImageIcon imgIcon = new ImageIcon(imgPath + "blossom.jpg");
	
	//보내는 사람
	String smtpServer = "smtp.naver.com";
	private String sendId;//보내는 사람 네이버 아이디 입력
	private String sendPass;//보내는 사람 네이버 비밀번호 입력
	String sendEmailAddress;//메일입력 ex)jks5117@naver.com
	

	public class BackGroundPanel extends JPanel { // 배경화면을 위해서 내부에 클래스 지정
		public void paintComponent(Graphics g) {
			g.drawImage(imgIcon.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponent(g);

		}
	}
	
	public EastMessage() {}
	
	public EastMessage(Main80 m80) {
		this.m80=m80;
		callData = m80.lv.check_ok;
		initDisplay();

	}
	public void initDisplay() {

		this.setContentPane(new BackGroundPanel());
		jta.setLineWrap(true);
		jsp.setBounds(70, 150, 440, 330);
		jlb_sen.setBounds(25, 20, 100, 50);
		jlb_sen.setFont(font);
		jlb_rec.setBounds(25, 50, 100, 50);
		jlb_rec.setFont(font);
		jlb_title.setBounds(25, 80, 100, 50);
		jlb_title.setFont(font);
		jtf_sen_mail.setBounds(120, 38, 300, 20);
		jtf_rec_mail.setBounds(120, 68, 300, 20);
		jtf_title.setBounds(120, 98, 300, 20);
		send.setBounds(440, 40, 100, 60);
		send.setFont(font);
		jlb_sen.setHorizontalAlignment(JLabel.CENTER);
		jlb_rec.setHorizontalAlignment(JLabel.CENTER);
		jlb_title.setHorizontalAlignment(JLabel.CENTER);
		jtf_sen_mail.setEditable(false);
		jtf_sen_mail.setText(callData.get("email"));
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			boolean	notSendMail = sendMail();
			boolean notRecordMail =	recordMail();
			System.out.println(notRecordMail);
			if(notSendMail == false && notRecordMail == false) {
				JOptionPane.showMessageDialog(null, "이메일 발송 기록 저장을 실패했습니다.");
				return;
			}else if(notSendMail == true && notRecordMail == false){
				JOptionPane.showMessageDialog(null, "이미 발신자가 저장되어 있습니다.");
				return;
			}else{
				JOptionPane.showMessageDialog(null, "이메일 발송 기록이 저장되었습니다.");				
				return;
			}
		}
	});
		this.add(jsp);
		this.add(send);
		this.add(jlb_sen);
		this.add(jlb_rec);
		this.add(jlb_title);
		this.add(jtf_sen_mail);
		this.add(jtf_rec_mail);
		this.add(jtf_title);
		this.setLayout(null);
		this.setTitle("메일쓰기");
		this.setResizable(false);
		this.setSize(600, 600);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	public boolean password() {
		boolean test;
		sendId = jtf_sen_mail.getText();
		sendEmailAddress = jtf_sen_mail.getText();
		JPanel jp_sendPass = new JPanel();
		JLabel jl_sendPass = new JLabel("비밀번호를 입력하세요 :");
		JPasswordField jpf_pw = new JPasswordField(10);
		String check_ok = jpf_pw.getText();
		String[] options = {"확인", "닫기"};
		jp_sendPass.add(jl_sendPass);
		jp_sendPass.add(jpf_pw);
		int option = JOptionPane.showOptionDialog(null, jp_sendPass, "비밀번호를 입력하세요.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if(option == 0) {
			char[] password = jpf_pw.getPassword();
			sendPass = new String(password);
			test = true;
			return test;
		}else if(option == 1) {
			JOptionPane.getRootFrame().dispose();
		}return false;
	}
	public boolean sendMail() {
		boolean sendMail = false;
		boolean check_password = password();
		if(check_password == true) {
			int smtpPort=465;
			
			String receiveEmailAddress = jtf_rec_mail.getText();
			
			String subject = jtf_title.getText();
			//내용
			String content = jta.getText();
			
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpServer);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.ssl.enable", true);
			props.put("mail.smtp.ssl.trust", smtpServer);
			
			Session session5 = Session.getDefaultInstance(props, new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(sendId, sendPass);
				}
			});
			//session2.setDebug(true);
			try{
				Message mimeMessage = new MimeMessage(session5);
				mimeMessage.setFrom(new InternetAddress(sendEmailAddress));
				System.out.println(sendEmailAddress);
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveEmailAddress));
				mimeMessage.setSubject(subject);
				mimeMessage.setText(content);
				Transport.send(mimeMessage);
				if (mimeMessage !=null) {
					sendMail = true;
					System.out.println("message sent successfully..."); 
					JOptionPane.showMessageDialog(null, "메일 전송에 성공했습니다.");
					return sendMail;
				}
			} catch (AddressException e) { 
				JOptionPane.showMessageDialog(null, "메일 전송에 실패했습니다.");
				e.printStackTrace(); 
			} catch (MessagingException e) { 
				// TODO Auto-generated catch block 
				e.printStackTrace(); 
				JOptionPane.showMessageDialog(null, "메일 전송에 실패했습니다.");
			}
		}
		return sendMail; 			
	}
	protected boolean recordMail() {//이메일 저장
		boolean sql_insertcheck;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
		String sql_time = dayTime.format(cal.getTime());
		StringBuilder sql_check = new StringBuilder();
		sql_check.append("INSERT into");
		sql_check.append(" save_rec_email(SELECT (SELECT member_code From account_info where id =?), ?, ? FROM dual");
		sql_check.append(" WHERE not exists(SELECT * FROM save_rec_email WHERE receiver = ?))");
		dbMgr = DBConnectionMgr.getInstance();
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql_check.toString());
			int i = 1;
			pstmt.setString(i++, callData.get("id"));
			pstmt.setString(i++, sql_time);
			pstmt.setString(i++, jtf_rec_mail.getText());
			pstmt.setString(i++, jtf_rec_mail.getText());
			pstmt.setString(i++, jtf_rec_mail.getText());
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int result = pstmt.executeUpdate(); //executeUpdate()는 영향을 받는 row(세로)줄을 숫자로 표시함. 예를 들어 추가 되면 1개의 로우가 반응되어 1의  값을 가짐. 실패하면 0을 받음.
				if(result > 0) {
					System.out.println("ok");
					sql_insertcheck = true;
					return sql_insertcheck;
				}else {
					System.out.println("최종 등록 실패");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, pstmt); //사용한 건 자원 반납.
		}
		return false;
	}
	
}