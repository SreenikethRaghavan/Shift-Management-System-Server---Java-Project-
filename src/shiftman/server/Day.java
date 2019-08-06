package shiftman.server;

public enum Day implements Comparable<Day> {

	Monday("Monday", 0), Tuesday("Tuesday", 1), Wednesday("Wednesday", 2), Thursday("Thursday", 3), Friday("Friday", 4), Saturday("Saturday", 5), Sunday("Sunday", 6);


	private final String _day;
	private final int _dayValue; // Integer value assigned to each day which makes sorting them easier


	Day(String day, int dayValue) {

		_day = day;
		_dayValue = dayValue;
	}


	@Override
	public String toString() {
		return _day;
	}


	// The integer value assigned to each day is also used to index arrays in other classes
	public int getDayValue() {

		return _dayValue;

	}


	// Method takes the name of a day as an input and returns the corresponding Day enum value
	public Day findDay(String givenDay) throws WrongDayInputException {

		for(Day day : Day.values()) {

			if (day.toString().equals(givenDay)) {

				return day;
			}
		}			

		throw new WrongDayInputException("ERROR: Day given (" + givenDay + ") is invalid.");
	}	
}




