package shiftman.server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Shop {

	private String _name;
	private Day _day;
	private Time[] _openingTimes;
	private Time[] _closingTimes;
	private StaffRegistry _staffRegistry;
	private ShiftSchedule _shiftSchedule;
	private Shop.Roster _roster;


	public Shop(String shopName) {

		_day = Day.valueOf("Monday"); // Initialised to point to the Monday Day object as we need a reference to a Day object to call the findDay() method on later
		_name = shopName;
		_openingTimes = new Time[7];
		_closingTimes = new Time[7];
		_staffRegistry = new StaffRegistry();
		_shiftSchedule = new ShiftSchedule();
		_roster = this.new Roster(); // Create a Roster object and store it as a field so that the Inner class can be made private and the Shop object can call all its methods 

	}


	public void setWorkingHours(String day, String openingTime, String closingTime) throws WrongDayInputException, InvalidTimesException {

		_day = _day.findDay(day);
		invalidTimes(openingTime, closingTime);		
		_openingTimes[_day.getDayValue()] = new Time(openingTime); // Using the values assigned to the Day objects for indexing purposes 
		_closingTimes[_day.getDayValue()] = new Time(closingTime);	

	}


	public void addShift(String day, String startTime, String endTime, String minWorkers) throws WrongDayInputException, ShiftOverlapException, ShiftOutsideWorkingHoursException, InvalidTimesException {

		invalidTimes(startTime, endTime);
		Shift shift = new Shift(day, startTime, endTime, minWorkers);		
		shiftInsideWorkingHours(day, startTime, endTime); // Check if the shift is within working hours of the shop

		_shiftSchedule.addShift(shift);
	}



	public void registerStaff(String firstName, String lastName) {

		_staffRegistry.registerStaff(firstName, lastName);

	}


	public void assignStaff(String day, String startTime, String endTime, String givenName, String familyName, boolean isManager) throws InvalidTimesException, WrongDayInputException, UnregisteredStaffException, ShiftDoesNotExistException {


		invalidTimes(startTime, endTime); // Check if the given times are invalid in any way
		_staffRegistry.checkStaffRegistration(givenName, familyName); // Check if the given staff member is registered with the shop

		_shiftSchedule.assignStaff(day, startTime, endTime, givenName, familyName, isManager, _staffRegistry);

	}



	public List<String> getRegisteredStaff(){

		return _staffRegistry.getRegisteredStaff();
	}



	public List<String> understaffedShifts(){

		return _shiftSchedule.understaffedShifts();
	}



	public List<String> overstaffedShifts(){

		return _shiftSchedule.overstaffedShifts();
	}



	public List<String> shiftsWithoutManagers(){

		return _shiftSchedule.shiftsWithoutManagers();
	}



	public List<String> getUnassignedStaff(){

		return _staffRegistry.getUnassignedStaff(_shiftSchedule);
	}



	public List<String> getShiftsManagedBy(String managerName) throws UnregisteredStaffException {

		return _shiftSchedule.getShiftsManagedBy(managerName, _staffRegistry);
	}



	public List<String> getRosterForWorker(String name) throws UnregisteredStaffException {

		return _roster.getRosterForWorker(name);
	}


	public List<String> getRosterForDay(String day) throws WrongDayInputException {

		return _roster.getRosterForDay(day);
	}



	public String displayRoster() throws WrongDayInputException {

		return _roster.displayRoster();
	}



	public void invalidTimes(String startTime, String endTime) throws InvalidTimesException {


		if (startTime.compareTo(endTime) >= 0) { // If the start time is the same as or after the end time

			throw new InvalidTimesException("ERROR: The start time cannot be after or the same as the end time.");
		}

		else if ((startTime.compareTo("23:59") >= 0) || (endTime.compareTo("23:59") > 0)) { // If the start time is at or after 23:59 or the end time is after 23:59

			throw new InvalidTimesException("ERROR: Any time input greater than 23:59 is invalid and the start time cannot be at 23:59.");

		}

		else if (startTime.equals("00:00")) { // If the start time is at midnight 

			throw new InvalidTimesException("ERROR: The shop cannot be open at midnight.");
		}
	}



	public void shiftInsideWorkingHours(String day, String startTime, String endTime) throws WrongDayInputException, ShiftOutsideWorkingHoursException {

		_day = _day.findDay(day);
		int dayNumber = _day.getDayValue();
		String openingTime = _openingTimes[dayNumber].getTime(); // get opening time for day
		String closingTime = _closingTimes[dayNumber].getTime(); // get closing time for day

		int compare1 = startTime.compareTo(openingTime);
		int compare2 = endTime.compareTo(closingTime);

		if ((compare1 < 0) || (compare2 > 0)) { // if either the shift start time is before the shop opening time or the shift end time is after the shop closing time

			throw new ShiftOutsideWorkingHoursException("ERROR: The given shift cannot be added as it is outside the shop's working hours on " + day + "." );
		}

		return;

	}


	public String getWorkingHoursForDay(String day) throws WrongDayInputException {

		_day = _day.findDay(day);
		int dayNumber = _day.getDayValue();
		String openingTime = _openingTimes[dayNumber].getTime();
		String closingTime = _closingTimes[dayNumber].getTime();

		return day + " " + openingTime + "-" + closingTime ;
	}



	public String getShopName() {

		return _name;
	}



	// A private roster Inner class for dealing with all the roster related functionality such as getting rosters for workers and days and displaying it 
	private class Roster {


		public List<String> getRosterForWorker(String name) throws UnregisteredStaffException {


			Staff worker = _staffRegistry.getStaffObject(name);

			List<Shift> shifts = _shiftSchedule.getListOfShiftsForWorker(worker);

			List<String> workerShifts = new ArrayList<String>();			


			if (shifts.isEmpty()) { // If the given worker has no shifts , return an empty list of strings

				return workerShifts;
			}

			Collections.sort(shifts);	


			workerShifts.add(worker.getDifferentNameFormat());

			for (Shift shift : shifts) {

				workerShifts.add(shift.toString());		

			}

			return workerShifts;

		}



		public List<String> getRosterForDay(String day) throws WrongDayInputException {

			List<String> dayRoster = new ArrayList<String>();

			_day = _day.findDay(day);

			List<Shift> shifts = _shiftSchedule.getListOfShiftsForDay(_day);

			if (shifts.isEmpty()) { // If the given day has no shifts, return an empty list of strings

				return dayRoster;
			}

			Collections.sort(shifts);

			dayRoster.add(_name);
			dayRoster.add(getWorkingHoursForDay(day));

			String details;

			for (Shift shift : shifts) {	

				details = shift.toString() + " " + shift.getManagerNameFormat() + " " + shift.getWorkerNames();

				dayRoster.add(details);

			}

			return dayRoster;

		}



		public String displayRoster() throws WrongDayInputException {

			String shopRoster = "";
			List<String> days = new ArrayList<String>();

			days.add("Monday");
			days.add("Tuesday");
			days.add("Wednesday");
			days.add("Thursday");
			days.add("Friday");
			days.add("Saturday");
			days.add("Sunday");

			for (String day : days) {

				List<String> roster = getRosterForDay(day);

				for (String details : roster) {

					shopRoster += details + "\n";
				}
			}

			return shopRoster;

		}
	}

}
