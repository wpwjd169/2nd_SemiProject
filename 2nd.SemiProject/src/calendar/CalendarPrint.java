package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CalendarPrint extends JFrame {
	
	//화면에 출력하기
		public void print(int year, int month) {
			int leadGap=0;
			CalendarLogic cl = new CalendarLogic();
			
			GregorianCalendar calendar=new GregorianCalendar(year, month, 1);
			String cols[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	        String data[][] = new String[5][7];
			DefaultTableModel dtm = new DefaultTableModel(data,cols);
	        JTable jtb = new JTable(dtm);
	        JScrollPane jsp = new JScrollPane(jtb);
	        this.add("North", jsp);
	        this.add("Center", jsp);
	        this.setSize(1200, 700);
	        this.setVisible(true);
	        leadGap = calendar.get(Calendar.DAY_OF_WEEK)-1;
	        
	        int daysInMonth = cl.days[month];
	        
	        //윤달일 경우.
	        if(calendar.isLeapYear(calendar.get(Calendar.YEAR)) && month == 1)
	           ++daysInMonth;
	        
	      //해당하는 달의 첫째날 이전을 비워준다.
	        int j = 0;
	        for(int i = 0; i < leadGap; i++)
	        {
	           dtm.setValueAt(null, 0, i);
	        }
	       
	        for(int i = 1; i <= daysInMonth; i++)
	        {
	           
//	           if(i>6) {
	           if(leadGap > 6) {
	              if((Math.floor(leadGap/7)) == 1) {
	                 j = 1;
	              }
	              else if((Math.floor(leadGap/7)) == 2) {
	                 j = 2;
	              }
	              else if((Math.floor(leadGap/7)) == 3) {
	                 j = 3;
	              }
	              else if((Math.floor(leadGap/7)) == 4) {
	                 j = 4;
	              }
	              //System.out.println("if : j===>"+j+", leadGap===>"+leadGap%7);   
	              dtm.setValueAt(" "+i, j, leadGap%7);//i=1,x=1 2,2 3,3 4,4 5,5 6,6 7,7    
	              leadGap++;
	           }else {
	              System.out.println("else : j===>"+j+", leadGap===>"+leadGap);
	              //dtm.setValueAt("55", 0, 4);//i=1,x=1 2,2 3,3 4,4 5,5 6,6 7,7 
	              dtm.setValueAt(" "+i, j, leadGap++);//i=1,x=1 2,2 3,3 4,4 5,5 6,6 7,7 
	           }
	        }
	        
	       
	        }
	
	

}
