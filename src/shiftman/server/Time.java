package shiftman.server;

public class Time {

	private String _time;

	public Time(String time) {

		_time = time;
	}


	public String getTime() {

		return _time;
	}

	// Method returns the string representation of the latter(greater) time between this object and the input time string
	public String greaterTime(String time2) {

		int compare = _time.compareTo(time2);

		if (compare > 0) {
			return _time;
		}

		else if (compare < 0) {
			return time2;
		}

		else {
			return _time; // If both the times are the same, return the first time as the greater time in order for the overlap checking to function properly 
		}
	}	
}
