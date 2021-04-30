package calculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import calendar.Main80;

public class MyCalcView extends JFrame implements ActionListener {
   
   // 최대한 입력 가능한 길이를 제한
   final int      MAX_INPUT_LENGTH   = 20;

   // 각 모드별로 index를 부여
   final int      INPUT_MODE         = 0;
   final int      RESULT_MODE         = 1;
   final int      ERROR_MODE         = 2;
   int            displayMode;

   boolean         clearOnNextDigit;         // 화면에 표시될 숫자를 지울지 말지 결정

   // 선언부
   double         lastNumber;               // 마지막에 기억될 수
   String         lastOperator;            // 마지막에 누른 연산자를 기억

   public JLabel   jl_output;               // 숫자가 표시되는 레이블
   public JButton   jb_btns[];               // 버튼 배열
   public JPanel   jp_btns;               // 버튼과 레이블을 배치할 공간

   public MyCalcView(){
           MyCalcLogic mcl = new MyCalcLogic(this);
 
      // 배경
            setBackground(Color.gray);

            jp_btns = new JPanel();// 버튼 패널

            // 계산기 숫자 표시 레이블
            jl_output = new JLabel("0", JLabel.RIGHT);
            jl_output.setFont(new Font("Arial", Font.BOLD, 40));
            jl_output.setBackground(Color.WHITE);
            jl_output.setOpaque(true);

            // 버튼 생성
            jb_btns = new JButton[20];

            for (int i = 0; i <= 9; i++) {
               jb_btns[i] = new JButton(String.valueOf(i));
               jb_btns[i].setBackground(new Color(255, 255, 255));
            }

            jb_btns[10] = new JButton("+");
            jb_btns[11] = new JButton("-");
            jb_btns[12] = new JButton("*");
            jb_btns[13] = new JButton("/");

            jb_btns[14] = new JButton(".");
            jb_btns[15] = new JButton("√");
            jb_btns[16] = new JButton("C");
            jb_btns[17] = new JButton("±");
            jb_btns[18] = new JButton("←");
            jb_btns[19] = new JButton("=");

            for (int i = 0; i <= 19; i++) {
               jb_btns[i].setFont(new Font("Arial", Font.BOLD, 35));
               jb_btns[i].setBackground(new Color(255, 255, 255));
            }

            // 버튼 글자 색상지정
            for (int i = 0; i < jb_btns.length; i++) {

               if (i < 10) {
                  jb_btns[i].setForeground(Color.black);
               }
               else if (10 <= i && i <= 15) {
                  jb_btns[i].setForeground(Color.blue);
               }
               else {
                  jb_btns[i].setForeground(Color.red);
               }
            }

            // 버튼 패널들의 레이아웃

            jp_btns.setLayout(new GridLayout(5, 4, 0, 0));

            for (int i = 16; i <= 19; i++) {
               jp_btns.add(jb_btns[i]);
            }

            // 숫자버튼들, 사칙연산 버튼들 배치
            for (int i = 1; i <= 3; i++) {
               jp_btns.add(jb_btns[i]);
            }
            jp_btns.add(jb_btns[10]);

            for (int i = 4; i <= 6; i++) {
               jp_btns.add(jb_btns[i]);
            }
            jp_btns.add(jb_btns[11]);

            for (int i = 7; i <= 9; i++) {
               jp_btns.add(jb_btns[i]);
            }
            jp_btns.add(jb_btns[12]);

            jp_btns.add(jb_btns[14]);
            jp_btns.add(jb_btns[0]);
            jp_btns.add(jb_btns[15]);
            jp_btns.add(jb_btns[13]);

            super.setPreferredSize(new Dimension(500, 500));
            // 레이블과 버튼 패널들 레이아웃

            getContentPane().add(jl_output, BorderLayout.NORTH);
            getContentPane().add(jp_btns, BorderLayout.CENTER);

            requestFocus();// ???무엇일까?
            for (int i = 0; i < jb_btns.length; i++) {
              jb_btns[i].addActionListener(this);
           }
           mcl.clearAll();
           this.dispose();
      
           this.setTitle("Calculator");
           this.pack(); // 무엇일까??
           this.setLocation(700, 300);
           this.setVisible(true);
           this.setResizable(false);
  }

   @Override
   public void actionPerformed(ActionEvent ae) {
     MyCalcLogic mcl = new MyCalcLogic(this);
      double result = 0;

      for (int i = 0; i < jb_btns.length; i++) {

         if (ae.getSource() == jb_btns[i]) {

            if (i < 10) {
               mcl.addToDisplay(i);
               break;
            }

            else if (i >= 10 && i <= 13) {
               mcl.processOperator(jb_btns[i].getText());
         
               break;
            }
            else {

               switch (i) {
               case 14:
                  mcl.addPoint();
                  break;
               case 15:
                  mcl.sqrRoot();
                  break;
               case 16:
                  mcl.clearAll();
                  break;
               case 17:
                  mcl.processSignChange();
                  break;
               case 18:
                  mcl.backspace();
                  break;
               case 19:
                  mcl.processEquals();
                  break;
               }
            }
         }

      }
   }

}