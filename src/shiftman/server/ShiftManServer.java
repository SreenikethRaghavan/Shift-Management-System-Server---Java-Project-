package shiftman.server;
import java.util.ArrayList;
import java.util.List;

public class ShiftManServer implements ShiftMan {

	private Shop _shop; 


	public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {

		String result = "";

		try {

			_shop.addShift(dayOfWeek, startTime, endTime, minimumWorkers);

		}

		catch (WrongDayInputException d) {	

			result = d.getMessage();			
		}

		catch (ShiftOverlapException o) {	

			result = o.getMessage();
		}

		catch (ShiftOutsideWorkingHoursException h) {

			result = h.getMessage();
		}

		catch (InvalidTimesException i) {

			result = i.getMessage();
		}

		return result;

	}



	public String registerStaff(String givenName, String familyName) {

		_shop.registerStaff(givenName, familyName);

		return "";
	}



	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, String familyName, boolean isManager) {

		String result = "";

		try {

			_shop.assignStaff(dayOfWeek, startTime, endTime, givenName, familyName, isManager);

		}

		catch (InvalidTimesException i) {	

			result = i.getMessage();
		}

		catch (WrongDayInputException d) {	

			result = d.getMessage();			
		}

		catch (UnregisteredStaffException r) {	

			result = r.getMessage();			
		}

		catch (ShiftDoesNotExistException s) {

			result = s.getMessage();
		}	

		return result;

	}



	public List<String> getRegisteredStaff(){

		try {

			if (_shop == null) {

				throw new RosterNotCreatedException("ERROR: Please create a new roster before trying to get the list of registered staff.");
			}

			return _shop.getRegisteredStaff();

		}

		catch (RosterNotCreatedException r) {

			String result = r.getMessage();

			List<String> registeredStaff = new ArrayList<String>();

			registeredStaff.add(result);

			return registeredStaff;		

		}

	}


	public List<String> understaffedShifts(){

		return _shop.understaffedShifts();
	}


	public List<String> overstaffedShifts(){

		return _shop.overstaffedShifts();
	}


	public List<String> shiftsWithoutManagers(){

		return _shop.shiftsWithoutManagers();
	}


	public String setWorkingHours(String dayOfWeek, String startTime, String endTime) {

		String result = "";

		try {

			if (_shop == null) {

				throw new RosterNotCreatedException("ERROR: Please create a new roster before trying to set the working hours for a shop.");
			}

			_shop.setWorkingHours(dayOfWeek, startTime, endTime);
		}

		catch (InvalidTimesException i) {	

			result = i.getMessage();
		}

		catch (WrongDayInputException d) {	

			result = d.getMessage();			
		}

		catch (RosterNotCreatedException r) {

			result = r.getMessage();
		}

		return result;
	}



	public List<String> getUnassignedStaff(){

		return _shop.getUnassignedStaff();
	}



	public List<String> getShiftsManagedBy(String managerName){

		try {

			return _shop.getShiftsManagedBy(managerName);
		}


		catch (UnregisteredStaffException r) {

			String result = r.getMessage();
			List<String> managerShifts = new ArrayList<String>();
			managerShifts.add(result);

			return managerShifts;
		}

	}



	public List<String> getRosterForWorker(String workerName){

		try {

			return _shop.getRosterForWorker(workerName);
		}


		catch (UnregisteredStaffException r) {

			List<String> workerShifts = new ArrayList<String>();
			String result = r.getMessage();
			workerShifts.add(result);

			return workerShifts;
		}
	}



	public List<String> getRosterForDay(String dayOfWeek){

		try {

			return _shop.getRosterForDay(dayOfWeek);
		}

		catch (WrongDayInputException d) {

			List<String> dayRoster = new ArrayList<String>();
			String result = d.getMessage();
			dayRoster.add(result);

			return dayRoster;
		}
	}



	public String newRoster(String shopName) {	

		_shop = new Shop(shopName);		

		return "";

	}



	public String displayRoster() {

		try {

			return _shop.displayRoster();
		}

		catch (WrongDayInputException d) {

			return d.getMessage();
		}
	}


	public String reportRosterIssues() {


		return "";
	}

}
