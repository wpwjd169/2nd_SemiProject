package login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import login.CreateAccount;
import calendar.Main80;
import common.CreateAccountVO;
import common.DBConnectionMgr;
import login.LoginDao;
import javazoom.jl.player.Player;

public class LoginView extends JFrame implements Runnable, ActionListener, KeyListener {
	// 2차 정보 확인용
	Connection con = null;
	CallableStatement cstmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	DBConnectionMgr dbMgr = null;
	JLabel jlb_daoimport = new JLabel("계정 정보:");
	JLabel jlb_daoimportcheck = new JLabel("Not yet");
	String nameVO = null;
	String departmentVO = null;
	String genderVO = null;
	String emailVO = null;
	String idVO = null;
	public Map<String, String> check_ok = new HashMap<String, String>();
	
	// 로그인 창 관련 선언
	LoginDao ld = null;
	CreateAccountVO caVO = null;
	JPanel jpl = new JPanel();
	JLabel jlb_id = new JLabel("ID :");
	JLabel jlb_pw = new JLabel("PW :");
	JTextField jtf_id = new JTextField();
	JPasswordField jpf_pw = new JPasswordField();
	JButton jbtn_add = new JButton("회원가입");
	JButton jbtn_login = new JButton("로그인");
	JButton jbtn_restart = new JButton("음악 재시작");
	JButton jbtn_stop = new JButton("음악 중지");
	Font font = new Font("맑은 고딕", Font.BOLD, 15);
	Font soundfont = new Font("맑은 고딕", Font.CENTER_BASELINE, 13);
	String name = "";
	String email = "";

	// 배경화면 관련 선언
	String imgPath = "../2nd.SemiProject/src/image/";
	String soundPath = "../2nd.SemiProject/src/sound/";
	ImageIcon imgIcon = new ImageIcon(imgPath + "Background.png");

	// 음악 관련 선언
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;
	private Player player;
	private Thread thread;
	private boolean isLoop;

	class BackGroundPanel extends JPanel { // 배경화면을 위해서 내부에 클래스 지정
		public void paintComponent(Graphics g) {
			g.drawImage(imgIcon.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponent(g);

		}
	}

	public LoginView() {
	}

	public LoginView(CreateAccountVO caVO) {
		this.caVO = caVO;
	}

	public void start() {
		Display();
	}

	public void Display() {
		// 화면 표시 관련
		this.setContentPane(new BackGroundPanel());
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("로그인");
		this.setSize(360, 760);
		this.setVisible(true);
		this.setResizable(false);// 크기조정 불가
		this.setLocation(800, 250);

		// id 부분
		jlb_id.setBounds(48, 250, 100, 30);
		jlb_id.setFont(font);
		jtf_id.setBounds(108, 250, 190, 30);
		jtf_id.addKeyListener(this);
		this.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent w) {
				jtf_id.requestFocus();
			}
		});
		this.add(jlb_id);
		this.add(jtf_id);

		// pw 부분
		jlb_pw.setBounds(48, 300, 80, 30);
		jlb_pw.setFont(font);
		jpf_pw.setBounds(108, 300, 190, 30);
		jpf_pw.addKeyListener(this);
		this.add(jlb_pw);
		this.add(jpf_pw);

		// 로그인
		jbtn_login.addActionListener(this); // 효과음 + 연결
		jbtn_login.setBounds(178, 350, 120, 40);
		jbtn_login.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.add(jbtn_login);

		// 회원가입
		jbtn_add.addActionListener(this); // 효과음 + 연결
		jbtn_add.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jbtn_add.setBounds(48, 350, 120, 40);
		this.add(jbtn_add);

		// 음악 정지 이벤트
		jbtn_stop.addActionListener(action);
		jbtn_stop.setContentAreaFilled(false);
		jbtn_stop.setBorderPainted(false);
		jbtn_stop.setFocusPainted(false);
		jbtn_stop.setFont(soundfont);
		jbtn_stop.setBounds(125, 400, 100, 30);
		jbtn_stop.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.add(jbtn_stop);

		// 음악 재시작 이벤트
		jbtn_restart.addActionListener(action);

		// 데이터 받은 공간 확인
		jlb_daoimport.setBounds(110, 425, 150, 30);
		jlb_daoimport.setFont(soundfont);
		jlb_daoimportcheck.setBounds(180, 425, 60, 30);
		jlb_daoimportcheck.setFont(soundfont);
		jlb_daoimportcheck.setForeground(Color.red);
		this.add(jlb_daoimport);
		this.add(jlb_daoimportcheck);
		jtf_id.addKeyListener(new KeyListener() {//계정정보 db에서 받았는지 확인하기 위한 이벤트

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				String datail= jtf_id.getText();
				check_ok = check(datail);
				if (check_ok != null) {
					jlb_daoimportcheck.setText("ok");
					jlb_daoimportcheck.setForeground(Color.blue);
				} else {
					jlb_daoimportcheck.setText("Not yet");
					jlb_daoimportcheck.setForeground(Color.red);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		////// thread 선정 - JFrame에 영향을 줄 수 있는 독립적인 작업을 한다고 생각하면 좋음.
		thread = new Thread(() -> {// ArrowFuntion(공부하기에는 안좋음)를 이용해 이 스레드는 이것만 플레이 하도록함.
			try {
				do { // 무조건 실행하는 곳.
					isLoop = true;
					file = new File(soundPath + "MapleLoginMusic.mp3");
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					player = new Player(bis);
					player.play();
				} while (isLoop); // 반복재생을 위해서 while문 사용
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		thread.start();

	}// end of Display();
		// 버튼 효과음 부분(thread 받는 부분)

	public void run() {
		synchronized (jbtn_add) {
			try {
				file = new File(soundPath + "PaperSound.mp3");
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
				player.play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}// end of run(effect sound);

	public Map<String, String> check(String p_id) { //map으로 계정의 정보를 가져오는 메소드
		StringBuilder sql_check = new StringBuilder();
		sql_check.append("SELECT * From account_info");
		sql_check.append(" WHERE id = ?");
		dbMgr = DBConnectionMgr.getInstance();
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql_check.toString());
			int i = 1;
			pstmt.setString(i++, jtf_id.getText());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String department = rs.getString("department");
				String gender = rs.getString("gender");
				String email = rs.getString("email");
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("name", name);
				map.put("department", department);
				map.put("gender", gender);
				map.put("email", email);
				return map;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return null;
	}

	@SuppressWarnings("deprecation") // 인터페이스를 선언해서 사용하는 것 인스턴스화 1-5의 원리를 이용한 것.
	public ActionListener action = (ActionEvent e) -> { // ArrowFuntion - 파이선 등에서 사용하던 것을 자바에서 이용해 사용하는 것
														// new actionListener(){}의 원리, 묵시적인 것이라서 되도록이면 쓰지 말자.
		if (e.getSource() == jbtn_stop) {
			thread.suspend();
			isLoop = false;
			this.remove(jbtn_stop);
			this.revalidate();
			this.repaint();
			jbtn_restart.setContentAreaFilled(false);
			jbtn_restart.setBorderPainted(false);
			jbtn_restart.setFocusPainted(false);
			jbtn_restart.setFont(soundfont);
			jbtn_restart.setBounds(118, 400, 120, 30);
			jbtn_restart.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.add(jbtn_restart);
		} else if (e.getSource() == jbtn_restart) {
			thread.resume();
			isLoop = true;
			this.remove(jbtn_restart);
			this.revalidate();
			this.repaint();
			jbtn_stop.setContentAreaFilled(false);
			jbtn_stop.setBorderPainted(false);
			jbtn_stop.setFocusPainted(false);
			jbtn_stop.setFont(soundfont);
			jbtn_stop.setBounds(125, 400, 100, 30);
			this.add(jbtn_stop);
		}
	};// end of actionListener(sound)
	
	public static void main(String[] args) {
		LoginView lv = new LoginView();
		lv.start();
	}

	@Override
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		ld = new LoginDao();
		if (e.getSource() == jbtn_add) {
			Runnable addR1 = new LoginView();
			Thread th_add = new Thread(addR1);
			th_add.start();
			CreateAccount ca = new CreateAccount();
		} else if (e.getSource() == jbtn_login) {
			Runnable loginR1 = new LoginView();
			Thread th_login = new Thread(loginR1);
			th_login.start();
			if ("".equals(jtf_id.getText()) || "".equals(jpf_pw.getText())) {
				JOptionPane.showMessageDialog(null, "아이디와 비번을 확인해주세요.");
				return;
			} // 공백 상태에서 로그인 버튼 누르면 에러 호출
			try {
				String p_id = jtf_id.getText(); // id 적은 문자를 문자열로 받는 것을 새로 변수선언함.
				String p_pw = jpf_pw.getText(); // pw 적은 문자를 문자열로 받는 것을 새로 변수선언함.
				name = ld.login(p_id, p_pw);// LoginDao에 있는 login(p_id, p_pw)메소드를 실행하고 그 값을 받음. - msg의 값을 받음.
				if ("아이디가 존재하지 않습니다.".equals(name)) {// name(msg값을 가진)의 값이 ""와 같다면
					JOptionPane.showMessageDialog(null, "아이디가 일치하지 않습니다.");
					return;// 여기서 if문 끝냄. - 밑에 있는 것은 실행 안함.
				} else if ("비밀번호가 틀립니다.".equals(name)) {
					JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
					return;
				} else {
					JOptionPane.showMessageDialog(null, name + "님 환영합니다.");
					this.setVisible(false);
					thread.stop();
					if ("이스터에그".equals(name)) {
						System.out.println("이스터에그 넣을거임.");
						return;
					} else {
						Main80 m80 = new Main80(this);
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void keyPressed(KeyEvent e) {//enter 누르면 실행하는 이벤튼
		ld=new LoginDao();
		int getkey = e.getKeyCode();
		if(getkey==KeyEvent.VK_ENTER && jtf_id != null && jpf_pw != null) {
			try {
				String p_id = jtf_id.getText(); // id 적은 문자를 문자열로 받는 것을 새로 변수선언함.
				String p_pw = jpf_pw.getText(); // pw 적은 문자를 문자열로 받는 것을 새로 변수선언함.
				name = ld.login(p_id, p_pw);// LoginDao에 있는 login(p_id, p_pw)메소드를 실행하고 그 값을 받음. - msg의 값을 받음.
				if ("아이디가 존재하지 않습니다.".equals(name)) {// name(msg값을 가진)의 값이 ""와 같다면
					JOptionPane.showMessageDialog(null, "아이디가 일치하지 않습니다.");
					return;// 여기서 if문 끝냄. - 밑에 있는 것은 실행 안함.
				} else if ("비밀번호가 틀립니다.".equals(name)) {
					JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
					return;
				} else {
					JOptionPane.showMessageDialog(null, name + "님 환영합니다.");
					this.setVisible(false);
					thread.stop();
					if ("이스터에그".equals(name)) {
						System.out.println("이스터에그 넣을거임.");
						return;
					} else {
						Main80 m80 = new Main80(this);
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
