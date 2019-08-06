package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShiftSchedule {

	private Shift[] _shifts;
	private int _numShifts;


	public ShiftSchedule() {

		_numShifts = 0;
		_shifts = new Shift[100]; // Maximum of 100 shifts can be added for a shop
	}


	public void addShift(Shift shift) throws ShiftOverlapException {

		doesShiftOverlap(shift); // Check if the shift overlaps with an existing shift
		_shifts[_numShifts] = shift;
		_numShifts++;

	}



	public void assignStaff(String day, String startTime, String endTime, String givenName, String familyName, boolean isManager, StaffRegistry staffRegistry) throws WrongDayInputException, ShiftDoesNotExistException, UnregisteredStaffException {



		for (int i = 0; i < _numShifts; i++) {

			Day checkDay = _shifts[i].getDay();

			String fullName = givenName + " " + familyName;

			Staff staff = staffRegistry.getStaffObject(fullName);

			if ((_shifts[i].getStartTimeString().equals(startTime)) && (_shifts[i].getEndTimeString().equals(endTime)) && (checkDay == checkDay.findDay(day))) { // If given shift details correspond to an existing shift

				if (isManager) {

					_shifts[i].addManager(staff);

					return;
				}

				else {

					_shifts[i].addWorker(staff);

					return;
				}
			}
		}

		throw new ShiftDoesNotExistException("ERROR: The given shift details do not correspond to an existing shift.");
	}




	public List<String> understaffedShifts(){

		List<Shift> shifts = new ArrayList<Shift>();

		List<String> understaffed = new ArrayList<String>();

		for (int i = 0; i < _numShifts; i++) {

			if (_shifts[i].getNumWorkers() < _shifts[i].getMinWorkers()) { // If the number of workers working is less than the number of minimum required workers

				shifts.add(_shifts[i]);
			}
		}

		if (shifts.isEmpty()) { // If no such shifts exist, return an empty list of strings

			return understaffed;
		}

		Collections.sort(shifts);		

		for (Shift shift : shifts) {

			understaffed.add(shift.toString());	
		}

		return understaffed;	
	}	





	public List<String> overstaffedShifts(){

		List<Shift> shifts = new ArrayList<Shift>();

		List<String> overstaffed = new ArrayList<String>();

		for (int i = 0; i < _numShifts; i++) {

			if (_shifts[i].getNumWorkers() > _shifts[i].getMinWorkers()) { // If the number of workers working is more than the number of minimum required workers


				shifts.add(_shifts[i]);
			}		
		}

		if (shifts.isEmpty()) { // If no such shifts exist, return an empty list of strings

			return overstaffed;
		}

		Collections.sort(shifts);		

		for (Shift shift : shifts) {

			overstaffed.add(shift.toString());		

		}

		return overstaffed;	

	}	





	public List<String> shiftsWithoutManagers(){

		List<Shift> shifts = new ArrayList<Shift>();

		List<String> withoutManagers = new ArrayList<String>();

		for (int i = 0; i < _numShifts; i++) {

			if (!_shifts[i].getManagerStatus()) { // If the shift doesn't have a manager

				shifts.add(_shifts[i]);
			}		
		}

		if (shifts.isEmpty()) { // If no such shifts exist, return an empty list of strings

			return withoutManagers;
		}

		Collections.sort(shifts);

		for (Shift shift : shifts) {

			withoutManagers.add(shift.toString());		

		}

		return withoutManagers;	

	}





	public List<String> getShiftsManagedBy(String managerName, StaffRegistry staffRegistry) throws UnregisteredStaffException {

		List<Shift> shifts = new ArrayList<Shift>();

		List<String> managerShifts = new ArrayList<String>();

		for (int i = 0; i < _numShifts; i++) {

			if (_shifts[i].getManagerStatus()) {

				if (managerName.equals(_shifts[i].getManagerName())) {

					shifts.add(_shifts[i]);

				}
			}		
		}

		if (shifts.isEmpty()) { // If no such shifts exist, return an empty list of strings

			return managerShifts;
		}

		Collections.sort(shifts);		

		Staff manager = staffRegistry.getStaffObject(managerName);

		managerShifts.add(manager.getDifferentNameFormat());

		for (Shift shift : shifts) {

			managerShifts.add(shift.toString());		

		}

		return managerShifts;

	}




	public boolean isStaffNotOnAnyShift (Staff staff) {	

		int counter = 0;

		for (int i = 0; i < _numShifts; i++) {

			if (!_shifts[i].isStaffOnShift(staff)) {

				counter++;
			}				
		}


		if (counter == _numShifts) {

			return true;

		}


		return false;	

	}



	// Method checks if a given shift overlaps with any of the existing shifts
	public void doesShiftOverlap(Shift shift) throws ShiftOverlapException{

		boolean overlap = false;

		for (int i = 0; i < _numShifts; i++) {

			overlap = shift.checkOverlap(_shifts[i]);

			if (overlap && (shift.getDay() == _shifts[i].getDay())) { // If the times overlap and the shifts are on the same day

				throw new ShiftOverlapException("ERROR: Given shift cannot be added as it overlaps with an existing shift.");
			}
		}
	}


	public List<Shift> getListOfShiftsForWorker(Staff worker) throws UnregisteredStaffException {


		List<Shift> shifts = new ArrayList<Shift>();

		for (int i = 0; i < _numShifts; i++) {

			if (_shifts[i].isWorkerOnShift(worker)) {

				shifts.add(_shifts[i]);
			}
		}		

		return shifts;
	}




	public List<Shift> getListOfShiftsForDay (Day day) {		

		List<Shift> shifts = new ArrayList<Shift>();

		for(int i = 0; i < _numShifts; i++) {

			if (day == _shifts[i].getDay()) {	

				shifts.add(_shifts[i]);
			}
		}

		return shifts;
	}

}
