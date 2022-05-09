import java.io.IOException;

/*
 * A class that tests the CalendarModel and CalendarView classes
 * 
 * @author Fariha Ahmed
 * @version 1.0
 */
public class SimpleCalendarTester {
	public static void main(String[] args) throws IOException {
		CalendarModel cm = new CalendarModel();
		CalendarView cv = new CalendarView(cm);
		cm.attach(cv);
	}
}