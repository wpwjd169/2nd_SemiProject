package calendar;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;



public class CalendarLogic extends JFrame{

	// 선언부
	Calendar c = null;
	public final static String[] months = { "January", "Febrary", "March", "April", "May", "June", "July", "August",
			"September", "Octber", "November", "December" };

	public final static String[] weeks = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	
	public static int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	
	public String schedule;

	// 연도를 가져오는 메소드
	public int thisYear() {
		c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	// 월을 가져오는 메소드
	public int thisMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH);

	}

	
	//월 1일 이전의 공백 개수 계산하기
	
	public int leadGap() {
		int year = 0;
		int month = 0;
		GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
		int leadGap = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			return leadGap;
		}
	 


	// 오늘 날짜 구하기
	public int today() {
		int day = 0;
		return day;

	}

	// 다음 월로이동
	public void nextMonth() {
		
		Calendar c = Calendar.getInstance();
		c.get(Calendar.MONTH+1);

	}

	// 이전 월로 이동
	public void prevMonth() { 
		
		Calendar c = Calendar.getInstance();
		c.get(Calendar.MONTH-1);
	}

	// 일정 적기
	public String writeSchedule(int day, String schedule) {
		
		return schedule;
	}

	// 일정 읽기
	public String readSchedule(int day) {

		return schedule;
	}
	
	//오늘, 현재 출근시간 찍기
	public Calendar getToWork() { 
		return Calendar.getInstance();
	}
	
	//오늘, 현재 퇴근시간 찍기
	public Calendar getOffWork() {
		
		return Calendar.getInstance();
	}

	
	
	

	public static void main(String args[]) {

	Calendar c = Calendar.getInstance();
	Calendar g = Calendar.getInstance();

	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);

	GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
	CalendarLogic cl = new CalendarLogic();
	CalendarPrint cp = new CalendarPrint();
	
	cp.print(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));	
	
}

}
