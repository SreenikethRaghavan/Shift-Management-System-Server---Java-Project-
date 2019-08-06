package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaffRegistry {

	private Staff[] _registeredStaff;
	private int _numRegStaff;


	public StaffRegistry() {

		_registeredStaff = new Staff[100]; // Maximum of 100 staff can be registered with the shop
		_numRegStaff = 0;

	}



	public void registerStaff(String firstName, String lastName) {	


		Staff staff = new Staff(firstName, lastName);
		_registeredStaff[_numRegStaff] = staff;
		_numRegStaff++;

	}



	public List<String> getRegisteredStaff(){

		List<Staff> staff = new ArrayList<Staff>();

		List<String> registeredStaff = new ArrayList<String>();

		if (_numRegStaff == 0) { // If no staff are registered, return an empty list of strings

			return registeredStaff;
		}

		for (int i = 0; i < _numRegStaff; i++) {

			staff.add(_registeredStaff[i]);
		}

		Collections.sort(staff);


		for (Staff employee : staff) {

			registeredStaff.add(employee.toString());
		}

		return registeredStaff;	
	}




	public List<String> getUnassignedStaff(ShiftSchedule shiftSchedule){

		List<Staff> staff = new ArrayList<Staff>();

		List<String> unassignedStaff = new ArrayList<String>();


		for (int i = 0; i < _numRegStaff; i++) {

			if (shiftSchedule.isStaffNotOnAnyShift(_registeredStaff[i])) {

				staff.add(_registeredStaff[i]);

			}			
		}

		if (staff.isEmpty()) { // If all the staff are assigned to a shift, return an empty list of strings

			return unassignedStaff;
		}

		Collections.sort(staff);


		for (Staff employee : staff) {

			unassignedStaff.add(employee.toString());		

		}

		return unassignedStaff;	
	}




	public void checkStaffRegistration(String givenName, String familyName) throws UnregisteredStaffException{



		for (int i = 0; i < _numRegStaff; i++) {

			String firstName = _registeredStaff[i].getGivenName();
			String lastName = _registeredStaff[i].getFamilyName();

			if ((firstName.equals(givenName)) && (lastName.equals(familyName))) {

				return;
			}

		}


		throw new UnregisteredStaffException("ERROR: The given staff member (" + givenName + " " + familyName + ") is not registered with the shop.");


	}




	public Staff getStaffObject(String name) throws UnregisteredStaffException {

		for (int i = 0; i < _numRegStaff; i++) {

			if (_registeredStaff[i].toString().equals(name)){

				return _registeredStaff[i];
			}

		}

		throw new UnregisteredStaffException("ERROR: The given staff (" + name + ") is not registered with the shop.");

	}

}
