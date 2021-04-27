package advicedata;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
public class CalendarPage202103 extends JFrame
{
      //해당하는 달을 배열에 담자.
      String[] months={"January","Febrary","March","April","May","June","July","August","September",
                               "Octber","November","December"};
      int leadGap=0;                           
      public final static int dom[]={31,28,31,30,31,30,31,31,30,31,30,31};
      public void nextMonth(int mm, int yy)
      {
         int leadGap=0;
         System.out.print(months[mm]);
         System.out.print(" ");
         System.out.print(yy);
         System.out.println();
         
         if(mm<0 || mm>11)
            throw new IllegalArgumentException("Month"+mm+"bad, must be 0-11");
         
         GregorianCalendar calendar=new GregorianCalendar(yy,mm,1);
         System.out.println("Su Mo Tu We Th Fr Sa");
         leadGap=calendar.get(Calendar.DAY_OF_WEEK)-1;
         int dayInMonth=dom[mm];
         //윤달일 경우.
         if(calendar.isLeapYear(calendar.get(Calendar.YEAR))&&mm==1)
            ++dayInMonth;
         //해당하는 달의 첫째날 이전을 비워준다.
         for(int i=0;i<leadGap;i++)
         {
            System.out.print("   ");
         }
         //그 달을 해당하는 날 수로 채운다.
         for(int i=1;i<=dayInMonth;i++)
         {
            if(i<10)
               System.out.print(" ");
               System.out.print(i);
            if((leadGap+i)%7==0)
               System.out.println();
            else
            System.out.print(" ");
         }
         System.out.println();         
      }
      public void prevMonth(int mm, int yy)
      {
         int leadGap=0;
         System.out.print(months[mm]);
         System.out.print(" ");
         System.out.print(yy);
         System.out.println();
         
         if(mm<0 || mm>11)
            throw new IllegalArgumentException("Month"+mm+"bad, must be 0-11");
         
         GregorianCalendar calendar=new GregorianCalendar(yy,mm,1);
         System.out.println("Su Mo Tu We Th Fr Sa");
         leadGap=calendar.get(Calendar.DAY_OF_WEEK)-1;
         int dayInMonth=dom[mm];
         //윤달일 경우.
         if(calendar.isLeapYear(calendar.get(Calendar.YEAR))&&mm==1)
            ++dayInMonth;
         //해당하는 달의 첫째날 이전을 비워준다.
         for(int i=0;i<leadGap;i++)
         {
            System.out.print("   ");
         }
         //그 달을 해당하는 날 수로 채운다.
         for(int i=1;i<=dayInMonth;i++)
         {
            if(i<10)
               System.out.print(" ");
            System.out.print(i);
            if((leadGap+i)%7==0)
               System.out.println();
            else
               System.out.print(" ");
         }
         System.out.println();         
      }
      
      //카렌다의 어느곳에 어느날을 넣을것인지를 계산한다.                                                
      public void print(int mm,int yy)
      {
         leadGap=0;
         System.out.print(months[mm]);
         System.out.print(" ");
         System.out.print(yy);
         System.out.println();
         
         if(mm<0 || mm>11)
            throw new IllegalArgumentException("Month"+mm+"bad, must be 0-11");
         
         GregorianCalendar calendar=new GregorianCalendar(yy,mm,1);
         //System.out.println("Su Mo Tu We Th Fr Sa");
         String cols[] = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
         String data[][] = new String[5][7];
         DefaultTableModel dtm = new DefaultTableModel(data,cols);
         JTable jtb = new JTable(dtm);
         JScrollPane jsp = new JScrollPane(jtb);
         this.add("Center",jsp);
         this.setSize(800,500);
         this.setVisible(true);
         leadGap=calendar.get(Calendar.DAY_OF_WEEK)-1;
         System.out.println(yy+", "+mm+": leadGap==>"+leadGap);
         int dayInMonth=dom[mm];
         //윤달일 경우.
         if(calendar.isLeapYear(calendar.get(Calendar.YEAR))&&mm==1)
            ++dayInMonth;
         //해당하는 달의 첫째날 이전을 비워준다.
         int j = 0;
         for(int i=0;i<leadGap;i++)
         {
            //System.out.print("   ");
            //dtm.setValueAt("null", 0, i);
            //System.out.println("leadGap==>"+leadGap);
//            y++;
            //y = 7-leadGap;
         }
         //그 달을 해당하는 날 수로 채운다.
         for(int x=0;x<5;x++) {
            for(int y=0;y<7;y++) {
               dtm.setValueAt("", x, y);
            }
         }
         for(int i=1;i<=dayInMonth;i++)
         {
            
//            if(i>6) {
            if(leadGap>6) {
               if((Math.floor(leadGap/7))==1) {
                  j=1;
               }
               else if((Math.floor(leadGap/7))==2) {
                  j=2;
               }
               else if((Math.floor(leadGap/7))==3) {
                  j=3;
               }
               else if((Math.floor(leadGap/7))==4) {
                  j=4;
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
         
         //System.out.println();
      }
      public static void main(String args[])
      {
         int year,month;
         CalendarPage202103 cp=new CalendarPage202103();
         Calendar c=Calendar.getInstance();
         if(args.length==2)
         {
            //print()에 달과 년도를 입력한다.
            System.out.println("사용자가 달과 년도를 입력한다.");
            cp.print(Integer.parseInt(args[0])-1,Integer.parseInt(args[1]));
            //cp.setTitle(args[1]+"년 "+(Integer.parseInt(args[0])-1)+"월");
         }
         else
         {
            System.out.println("사용자가 달과 년도를 입력 안했다.");
            cp.print(c.get(Calendar.MONTH),c.get(Calendar.YEAR));
            //cp.setTitle(c.get(Calendar.YEAR)+"년 "+(c.get(Calendar.MONTH))+"월");
         }
      }
   }



