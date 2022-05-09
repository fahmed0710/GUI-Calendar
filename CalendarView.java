/*
 * A class that incorporates the view and controller of the calendar
 * 
 * @author Fariha Ahmed
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;

public class CalendarView implements ChangeListener{
	private int prevSelected = -1;
	private ArrayList<JButton> days;
	private CalendarModel model;
	private JFrame f;
	private JButton prev, next, create, quit;
	private JPanel monthView, buttonsPanel;
	private JTextArea dayView;
	
	/*
	 * Constructs a CalendarView object
	 * 
	 * @param cm the calendar model that manipulates calendar data
	 */
	public CalendarView(CalendarModel cm) {
		model = cm;
		
		f = new JFrame();
		f.setLayout(new BorderLayout());
		f.setPreferredSize(new Dimension(650,350));
		
		LocalDate current = LocalDate.now();
		
		// Create the day view
		dayView = new JTextArea();
		eventsOnThisDay(model.getCurrentDate());
		dayView.setPreferredSize(new Dimension(300,150));
		dayView.setEditable(false);
		f.add(dayView, BorderLayout.EAST);
		
		// Create the month view
		JPanel calendar = createCalendar(current);
		monthView = new JPanel();
		monthView.setLayout(new GridLayout(2,0));
		monthView.add(calendar);
		monthView.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
	                         current.getMonth() + " " + current.getYear(),
	                         TitledBorder.CENTER, TitledBorder.TOP));
		f.add(monthView, BorderLayout.WEST);
		
		// Create the buttons
		prev = new JButton("<");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.prev();
				eventsOnThisDay(model.getCurrentDate());
			}
		});

		create = new JButton("Create Event");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createEvent();
			}
		});
		
		next = new JButton(">");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.next();
				eventsOnThisDay(model.getCurrentDate());
			}
		});
		
		buttonsPanel = new JPanel();
		buttonsPanel.add(prev, FlowLayout.LEFT);
		buttonsPanel.add(create, FlowLayout.CENTER);
		buttonsPanel.add(next, FlowLayout.RIGHT);
		f.add(buttonsPanel, BorderLayout.NORTH);
		
		quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					model.saveEvents();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		quit.setPreferredSize(new Dimension(20,40));
		f.add(quit,BorderLayout.SOUTH);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}
	
	/*
	 * Called when the data in the model is changed/updated
	 * 
	 * @param e the event representing the change/update
	 */
	public void stateChanged(ChangeEvent e) {
		if(model.hasMonthChanged() == true) {
			model.monthChanged(false);
			monthView.removeAll();
			LocalDate current = model.getCurrentDate();
			LocalDate ld = LocalDate.of(current.getYear(), current.getMonthValue(), 1);
			JPanel calendar = createCalendar(ld);
			monthView.add(calendar);
			monthView.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
		                         current.getMonth() + " " + current.getYear(),
		                         TitledBorder.CENTER, TitledBorder.TOP));
			
			f.pack();
			f.repaint();
		}
		else
			selectDay(model.getCurrentDate().getDayOfMonth() - 1);
			eventsOnThisDay(model.getCurrentDate());
	}
	
	/*
	 * Sets up a calendar with a given date
	 * 
	 * @param ld the date on which to center the calendar around
	 */
	public JPanel createCalendar(LocalDate ld) {
		JPanel calendar = new JPanel();
		calendar.setLayout(new GridLayout(0,7));
		calendar.setPreferredSize(new Dimension(300,200));
		
		days = new ArrayList<JButton>();
		
		for(int i = 1; i <= ld.lengthOfMonth(); i++) {
			final int n = i;
			JButton b = new JButton(Integer.toString(i));
			
			if(i == LocalDate.now().getDayOfMonth() && (ld.getMonth() == LocalDate.now().getMonth())) {
				b.setForeground(Color.RED);
			}
			
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.setCurrentDate(n);
					eventsOnThisDay(model.getCurrentDate());
					selectDay(n - 1);
				}
			});
			days.add(b);
		}
		
		calendar.add(new JLabel("   Sun"));
		calendar.add(new JLabel("  Mon"));
		calendar.add(new JLabel(" Tues"));
		calendar.add(new JLabel("  Wed"));
		calendar.add(new JLabel(" Thurs"));
		calendar.add(new JLabel("   Fri"));
		calendar.add(new JLabel("   Sat"));
		
		LocalDate d = LocalDate.of(ld.getYear(), ld.getMonthValue(), 1);
		if(d.getDayOfWeek().getValue() != 7) {
			for(int i = 1; i <= d.getDayOfWeek().getValue(); i++) {
				JButton b = new JButton();
				b.setEnabled(false);
				calendar.add(b);
			}
		}
		
		for(JButton b : days) {
			b.setPreferredSize(new Dimension(40,40));
			calendar.add(b);
		}
		return calendar;
	}
	
	/*
	 * Creates an event based on user input
	 */
	private void createEvent() {
		JDialog createEvent = new JDialog();
		createEvent.setPreferredSize(new Dimension(400,200));
		createEvent.setTitle("Create new event");
		createEvent.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		JTextField name = new JTextField(20);
		JTextField st = new JTextField(10);
		JTextField et = new JTextField(10);
		
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Check if all text fields have been filled
				if(name.getText().isEmpty() || st.getText().isEmpty() || et.getText().isEmpty()) {
					JDialog error = new JDialog();
					error.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
					error.setLayout(new GridLayout(2,0));
					error.add(new JLabel("Please enter all parameters in the correct fashion."));
					JButton okay = new JButton("OK");
					okay.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							error.dispose();
						}
					});
					error.add(okay);
					error.pack();
					error.setVisible(true);
				}
				else {
					String n = name.getText();
					LocalTime start = LocalTime.parse(st.getText());
					LocalTime end = LocalTime.parse(et.getText());
					TimeInterval ti = new TimeInterval(start,end);
					
					// Check for time conflict
					if(model.checkConflict(ti) == true) {
						JDialog conflict = new JDialog();
						conflict.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
						conflict.setLayout(new GridLayout(2,0));
						conflict.add(new JLabel("The event you tried to create has a time conflict with an existing event."));
						JButton okay = new JButton("OK");
						okay.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								conflict.dispose();
							}
						});
						conflict.add(okay);
						conflict.pack();
						conflict.setVisible(true);
					}
					
					// Create the event
					else {
						model.createEvent(n,start,end);
						eventsOnThisDay(model.getCurrentDate());
					}
				}
			}
		});
		
		createEvent.setLayout(new GridBagLayout());
		JLabel date = new JLabel();
		date.setText(model.getCurrentDate() + "");
		date.setBorder(BorderFactory.createEmptyBorder());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		createEvent.add(date, constraints);
		constraints.gridy = 1;
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.LINE_START;
		createEvent.add(new JLabel("Event"), constraints);
		constraints.gridy = 2;
		createEvent.add(name, constraints);
		constraints.gridy = 3;
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.LINE_START;
		createEvent.add(new JLabel("Time Start (00:00)"), constraints);
		constraints.anchor = GridBagConstraints.CENTER;
		createEvent.add(new JLabel("Time End (23:00)"), constraints);
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.LINE_START;
		createEvent.add(st, constraints);
		constraints.anchor = GridBagConstraints.CENTER;
		createEvent.add(et, constraints);
		constraints.anchor = GridBagConstraints.LINE_END;
		createEvent.add(save, constraints);
		
		createEvent.pack();
		createEvent.setVisible(true);
	}
	
	/*
	 * Highlights the user selected day on the calendar with a different border
	 * 
	 * @param day the day on the calendar that is currently selected
	 */
	public void selectDay(int day) {
		days.get(day).setBorder(BorderFactory.createLineBorder(Color.RED));
		if(prevSelected != -1)
			days.get(prevSelected).setBorder(new JButton().getBorder());
		prevSelected = day;
	}
	
	/*
	 * Shows the given date and lists any events occurring on the date
	 * 
	 * @param d the selected date
	 */
	public void eventsOnThisDay(LocalDate d) {
		dayView.setText(d.getDayOfWeek() + " " + d.getMonthValue() + "/" + d.getDayOfMonth());
		dayView.append("\n" + model.onThisDay(d));
	}
}