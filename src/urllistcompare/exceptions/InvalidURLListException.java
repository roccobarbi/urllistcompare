package urllistcompare.exceptions;

@SuppressWarnings("serial")
public class InvalidURLListException extends RuntimeException {

	public InvalidURLListException() {
		super("InvalidURLListException!");
	}

	public InvalidURLListException(String msg) {
		super(msg);
	}

}
