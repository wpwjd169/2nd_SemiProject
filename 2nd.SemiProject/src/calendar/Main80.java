package calendar;

import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;

import common.CreateAccountVO;
import login.LoginDao;
import login.LoginView;

public class Main80 extends JFrame {
   Main80East me = null;
   Main80Center mc = null;
   CreateAccountVO caVO = null;
   //계정정보를 받기 위한 선언.
   Map<String, String> callData = null;
   LoginView lv = null;
 
   
   public Main80(LoginView lv) {
	  this.lv = lv;
	  callData = lv.check_ok;
      me = new Main80East(this);
      mc = new Main80Center(me);
      initDisplay();
   }

private void initDisplay() {
      this.add("Center", mc);
      this.add("East", me);
      this.setTitle(callData.get("name")+"님의 일정관리");
      this.setSize(1200, 700);
      this.setVisible(true);
      this.setResizable(false);// 크기조정 불가
      this.setLocationRelativeTo(null);// 가운데 띄우게하기
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }


}