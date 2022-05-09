/*
 * A class that contains the mechanisms of the calendar
 * 
 * @author Fariha Ahmed
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.*;

public class CalendarModel {
	private LocalDate currentDate;
	private HashMap<LocalDate, ArrayList<Event>> events;
	private ArrayList<ChangeListener> listeners;
	private boolean monthChanged = false;
	
	/*
	 * Constructs a DataModel object
	 * 
	 * @throws IOException 
	 */
	public CalendarModel() throws IOException {
		currentDate = LocalDate.now();
		events = new HashMap<>();
		listeners = new ArrayList<ChangeListener>();
		loadEvents();
	}
	
	/*
	 * Attach a listener to the DataModel
	 * 
	 * @param c the listener
	 */
	public void attach(ChangeListener c) {
		listeners.add(c);
	}
	
	/*
	 * Updates the listeners of the model
	 */
	public void update() {
		for(ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	/*
	 * Sets the current date of the calendar with a single day
	 * 
	 * @param day the user selected day to change the current date to
	 */
	public void setCurrentDate(int day) {
		currentDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), day);
	}
	
	/*
	 * Sets the current date of the calendar with a date
	 * 
	 * @param date the date to change the current date to
	 */
	public void setCurrentDate(LocalDate date) {
		currentDate = date;
	}
	
	/*
	 * Returns the current date
	 * 
	 * @return the current date
	 */
	public LocalDate getCurrentDate() {
		return currentDate;
	}
	
	/*
	 * Sets the current date back a day
	 */
	public void prev() {
		LocalDate minus = currentDate.minusDays(1);
		if(minus.getMonth() != currentDate.getMonth())
			monthChanged(true);
		currentDate = minus;
		update();
	}
	
	/*
	 * Sets the current date forward a day
	 */
	public void next() {
		LocalDate plus = currentDate.plusDays(1);
		if(plus.getMonth() != currentDate.getMonth())
			monthChanged(true);
		currentDate = plus;
		update();
	}
	
	/*
	 * Updates whether the month of the current date has changed or not
	 * 
	 * @param b a boolean based on whether the month has changed
	 */
	public void monthChanged(Boolean b) {
		monthChanged = b;
	}
	
	/*
	 * Returns whether the month has changed or not
	 * 
	 * @return a boolean based on whether the month has changed
	 */
	public boolean hasMonthChanged() {
		return monthChanged;
	}
	
	/*
	 * Checks if the specified date has any events scheduled
	 * 
	 * @param date the selected date
	 * @return a boolean based on if there are any events scheduled for the date
	 */
	public Boolean hasEvent(LocalDate date) {
		return events.containsKey(date);
	}
	
	/*
	 * Creates an event on the currently selected date
	 * 
	 * @param name the name of the event
	 * @param st the starting time of the event
	 * @param et the ending time of the event
	 */
	public void createEvent(String name, LocalTime st, LocalTime et) {
		LocalDate d = currentDate;
		Event e = new Event(name, d, st, et);
		
		ArrayList<Event> l = new ArrayList<>();
		if(hasEvent(e.getDate()))
			l = events.get(d);
		l.add(e);
		events.put(d,l);
	}
	
	/*
	 * Returns a list of events on the given date
	 * 
	 * @param date the selected date
	 * @return the list of events on the date in string format
	 */
	public String onThisDay(LocalDate date) {
		if(events.get(date) == null) {
			return "\nThere are no events scheduled for this day.";
		}
		ArrayList<Event> onThisDay = events.get(date);
		
		//Sort the events on the given day by starting time
		for(int i = 0; i < onThisDay.size(); i++) {
			int min = i;
			for(int j = i + 1; j < onThisDay.size(); j++) {
				Event e1 = onThisDay.get(j);
				int compare = e1.getTimeInterval().getStart().compareTo(onThisDay.get(min).getTimeInterval().getStart());
					if(compare < 0)
						min = j;
						
				Event temp = onThisDay.get(min);
				onThisDay.set(min, onThisDay.get(i));
				onThisDay.set(i, temp);
			}
		}
		
		String eventsList = "\n";
		for(Event e : onThisDay) {
			eventsList += e.getName() + ": " + e.getTimeInterval().toString() + "\n";
		}
		
		return eventsList;
	}
	
	/*
	 * Checks if an event has any time conflicts with other events on the current date
	 * 
	 * @param ti the time interval of the event
	 */
	public Boolean checkConflict(TimeInterval ti) {
		Boolean conflict = false;
		
		ArrayList<Event> onSameDay = events.get(currentDate);
		
		if(onSameDay != null) {
			for(int i = 0; i < onSameDay.size(); i++) {
				TimeInterval ti2 = onSameDay.get(i).getTimeInterval();
				if(ti.overlap(ti2) == true) {
					conflict = true;
					break;
				}
			}
		}
		return conflict;
	}
	
	/*
	 * Loads events from an input file
	 * 
	 * @throws IOException
	 */
	public void loadEvents() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("files/events.txt"));
		ArrayList<String> fileContent = new ArrayList<String>();
		String line = br.readLine();
		
		while(line != null) {
			fileContent.add(line);
			line = br.readLine();
		}
		
		br.close();
		
		for(int i = 0; i < fileContent.size(); i+= 2) {
			String name = fileContent.get(i);
			String[] otherInfo = fileContent.get(i + 1).split(" ");
			
			LocalDate date = LocalDate.parse(otherInfo[0]);
			LocalTime st = LocalTime.parse(otherInfo[1]);
			LocalTime et = LocalTime.parse(otherInfo[2]);
			
			Event e = new Event(name,date,st,et);
			
			ArrayList<Event> l = new ArrayList<>();
			if(hasEvent(e.getDate()))
				l = events.get(date);
			l.add(e);
			events.put(date,l);
		}
	}
	
	/*
	 * Saves events in an output file
	 * 
	 * @throws IOException
	 */
	public void saveEvents() throws IOException {
		FileWriter output = new FileWriter("files/events.txt");
		for(LocalDate date : events.keySet()) {
			ArrayList<Event> l = events.get(date);
			for(Event e : l) {
				output.write(e.getName() + "\n" );
				output.write(e.getDate().toString() + " " + e.getTimeInterval().getStart() + " " + e.getTimeInterval().getEnd() + "\n" );
			}
		}
		output.close();
	}
}