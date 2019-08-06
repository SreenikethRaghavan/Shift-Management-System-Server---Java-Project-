package shiftman.server;

public class Staff implements Comparable<Staff>{

	private String _givenName;
	private String _familyName;

	public Staff(String givenName, String familyName) {

		_givenName = givenName;
		_familyName = familyName;
	}


	public String getGivenName() {

		return _givenName;
	}

	public String getFamilyName() {

		return _familyName;
	}

	// Method returns the name of the staff in the format: family name, given name
	public String getDifferentNameFormat() {

		return _familyName + ", " + _givenName;
	}


	@Override
	public String toString() {

		return _givenName + " " + _familyName;
	}


	@Override
	public int compareTo(Staff staff) {
		return _familyName.compareTo(staff.getFamilyName());
	}	
}
