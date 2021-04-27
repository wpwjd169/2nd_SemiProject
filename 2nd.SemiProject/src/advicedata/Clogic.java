package advicedata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Clogic {
      
      String abcd(int month) {
         String result = "";
         int resInt = 0;
         Calendar thisCal = Calendar.getInstance(); //현재년도
         Calendar cal = new GregorianCalendar(thisCal.get(Calendar.YEAR), month, 1); //해당달의 최대일수를 알아내기위한 캘린더
         result = Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            
         return result;
         
      }
      
      public static void main(String[] args) {
      Clogic k = new Clogic();
      
      List<String> a = null;
      a = new ArrayList<String>();
      for(int i=0;i<12;i++) {
         a.add(k.abcd(i));
      }
      
      for(int i=0;i<a.size();i++) {
         System.out.println((i+1)+"월 최대 일수 : " + a.get(i));
      }
      
   }

}