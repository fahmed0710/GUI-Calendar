# GUI-Calendar
This simple graphical calendar user interface follows the MVC model of design 
and uses the Java LocalDate and LocalTime APIs. 
The user is able to move the calendar back and forth as far as the Gregorian calendar can go.

If an 'events.txt' file exists, the starting program will load events from the file to the calendar.

The initial screen presents the current month view on the left side 
and the current day view on the right side. 
The current date is highlighted.
The day view has all scheduled events for the current day presented, in order of starting time.
Each day on the month view is clickable. When a user clicks on a day on the month view, 
the day view is changed to represent the day the user clicked on.

The calendar comes with previous ('<') and next ('>') buttons to move the calendar backwards and forward.
Each click on the buttons will change the calendar one day at a time and change the current highlighted day.

The calendar comes with a 'Create' button, through which the user can enter events to be scheduled in the calendar. 
To schedule an event on a particular day, a user first clicks on the day of the month view, and then clicks on the 'Create' button. 
The 'Create' button makes a dialog box pop up, thorugh which the user can enter the name, starting time, and ending time of an event.
After the user clicks the 'Save' button on the dialog box, the event is stored in the calendar on the date that the user had selected previously.

The calendar checks time conflicts when a user tries to create an event.
If the event the user is trying to create conflicts with an existing event, the calendar generates an error message.

The calendar comes with a 'Quit' button, which terminates the program and saves the events in 'events.txt'.
