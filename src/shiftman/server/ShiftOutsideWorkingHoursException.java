package shiftman.server;

public class ShiftOutsideWorkingHoursException extends Exception{

	public ShiftOutsideWorkingHoursException(String message) {

		super(message);
	}
}
