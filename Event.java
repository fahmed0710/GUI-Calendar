/*
 * A class that represents an event, which consists of a name and a TimeInterval
 * 
 * @author Fariha Ahmed
 * @version 1.0
 */

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
	
	private String name;
	private ArrayList<Integer> daysOfWeek;
	private LocalDate date;
	private TimeInterval ti;
	
	/*
	 * Constructor for one-time events
	 * 
	 * @param n the name of the event
	 * @param d the date of the event
	 * @param st the starting time of the event
	 * @param et the ending time of the event
	 */
	public Event(String n, LocalDate d, LocalTime st, LocalTime et) {
		this.name = n;
		this.date = d;
		this.ti = new TimeInterval(st,et);
		
		this.daysOfWeek = null;
	}

	/*
	 * Returns the name of the event
	 * 
	 * @return the event's name
	 */
	public String getName() {
		return this.name;
	}
	
	/*
	 * Returns the array list of the values of the days of the week the event occurs on
	 * 
	 * @return the array list of the enum of the days of the week
	 */
	public ArrayList<Integer> getDaysOfTheWeek(){
		return this.daysOfWeek;
	}
	
	
	/*
	 * Returns the start to end time of the event
	 * 
	 * @return the event's time interval
	 */
	public TimeInterval getTimeInterval() {
		return this.ti;
	}
	
	/*
	 * Returns the date of the event
	 * 
	 * @return the event's starting date
	 */
	public LocalDate getDate() {
		return this.date;
	}
}