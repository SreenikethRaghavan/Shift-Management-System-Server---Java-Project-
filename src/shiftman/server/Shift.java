package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shift implements Comparable<Shift>{

	private Day _day;
	private Time _startTime;
	private Time _endTime;
	private int _minWorkers;
	private Staff[] _workers;
	private Staff _manager;
	private int _numWorkers;
	private boolean _hasManager;

	public Shift(String day, String startTime, String endTime, String minWorkers) throws WrongDayInputException {

		_day = Day.valueOf("Monday"); // Initialised to point to the Monday Day object as we need a reference to a Day object to call the findDay() method on
		_day = _day.findDay(day); // Made to point to the Day object corresponding to the given day string
		_startTime = new Time(startTime);
		_endTime = new Time(endTime);			
		_minWorkers = Integer.parseInt(minWorkers);
		_numWorkers = 0;	
		_workers = new Staff[100]; // Can have a maximum of 100 workers for any given shift
		_hasManager = false;

	}

	public void addWorker(Staff worker) {

		_workers[_numWorkers] = worker;
		_numWorkers++;

	}

	public void addManager(Staff manager) {

		_manager = manager;
		_hasManager = true;
	}

	public boolean checkOverlap(Shift existingShift) {

		String startTime1 = getStartTimeString();
		String endTime1 = getEndTimeString();
		String startTime2 = existingShift.getStartTimeString();
		String endTime2 = existingShift.getEndTimeString();

		String later = _startTime.greaterTime(startTime2); // Which shift starts later

		if ((startTime1.equals(startTime2))||(endTime1.equals(endTime2))) {  // If both shifts have exactly the same times
			return true;
		}		

		if ((startTime1.equals(endTime2))||(startTime2.equals(endTime1))) {  // If a shift starts at the same time at which another shift ends
			return true;
		}		

		if (later.equals(startTime1)) {  // If shift 1 starts later

			String finishesAfter = _startTime.greaterTime(endTime2); 

			if (finishesAfter.equals(endTime2)) { // If shift 2 ends after shift 1 starts
				return true;
			}
		}

		else if (later.equals(startTime2)) {  // If shift 2 starts later

			String finishesAfter = existingShift.getStartTimeObject().greaterTime(endTime1);

			if (finishesAfter.equals(endTime1)) {  // If shift 1 ends after shift 2 starts
				return true;
			}
		}

		return false; 
	}



	public Time getStartTimeObject() {

		return _startTime;
	}


	public String getStartTimeString() {

		return _startTime.getTime();	

	}

	public String getEndTimeString() {

		return _endTime.getTime();	

	}

	public Day getDay() {

		return _day;
	}



	public boolean isStaffOnShift(Staff staff) {

		for (int i = 0; i < _numWorkers; i++) {

			if (_workers[i].equals(staff)) {

				return true;
			}
		}

		if (_hasManager) {

			if (_manager.equals(staff)) {

				return true;			

			}		
		}

		return false;	
	}



	public boolean isWorkerOnShift(Staff staff) {

		for (int i = 0; i < _numWorkers; i++) {

			if (_workers[i].equals(staff)) {

				return true;
			}
		}

		return false;	
	}



	@Override
	public String toString() {

		String day = _day.toString();
		String startTime = _startTime.getTime();
		String endTime = _endTime.getTime();

		return day + "[" + startTime + "-" + endTime + "]";
	}


	@Override
	public int compareTo(Shift shift) {	

		String startTime1 =  _startTime.getTime();
		String startTime2 = shift.getStartTimeString();

		if (_day != shift.getDay()) {

			return _day.compareTo(shift.getDay());

		}

		else { // if the day is the same, order by time

			return startTime1.compareTo(startTime2);		
		}
	}	


	public int getNumWorkers() {
		return _numWorkers;
	}


	public int getMinWorkers() {
		return _minWorkers;
	}


	public boolean getManagerStatus() {

		return _hasManager;
	}


	public String getManagerName() {

		return _manager.toString();

	}


	public String getWorkerNames() {

		if (_numWorkers == 0) {

			return "[No workers assigned]";
		}

		List<Staff> workers = new ArrayList<Staff>();

		String workerList = "[";

		for(int i = 0; i < _numWorkers; i++) {		

			workers.add(_workers[i]);					
		}

		Collections.sort(workers);

		for(Staff worker : workers) {

			if (workerList.length() != 1) {

				workerList += ", ";
			}

			workerList += worker.toString();
		}

		workerList += "]";	


		return workerList;

	}


	// Returns properly formatted manager name which is used in the getRosterForDay() method in the ShiftManServer class
	public String getManagerNameFormat() {

		if (_hasManager) {

			return "Manager:" + _manager.getDifferentNameFormat();	
		}

		return "[No manager assigned]";

	}
}
