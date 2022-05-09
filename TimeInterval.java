/*
 * A class that represents an interval of time, suitable for events
 * 
 * @author Fariha Ahmed
 * @version 1.0
 */

import java.time.LocalTime;

public class TimeInterval {
	private LocalTime st, et;
	
	/*
	 * Constructs a time interval for use in events
	 * 
	 * @param start the starting time of the event
	 * @param end the ending time of the event
	 */
	public TimeInterval(LocalTime start, LocalTime end) {
		this.st = start;
		this.et = end;
	}
	
	/*
	 * Returns the start time of the event
	 * 
	 * @return the start time
	 */
	public LocalTime getStart() {
		return this.st;
	}
	
	/*
	 * Returns the end time of the event
	 * 
	 * @return the end time
	 */
	public LocalTime getEnd() {
		return this.et;
	}
	
	/*
	 * Returns the start to end time as a string
	 * 
	 * @return the string form of the time interval
	 */
	public String toString() {
		return st.toString() + " - " + et.toString();
	}
	
	/*
	 * Checks whether two time intervals overlap
	 * 
	 * @param start the start time of the other event being compared
	 * @return true/false depending on whether the time intervals overlap
	 */
	public boolean overlap(TimeInterval ti) {
		boolean overlaps = (this.st.isBefore(ti.et) && this.et.isAfter(ti.st));
		return overlaps;
	}
}
