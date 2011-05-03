package exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 5, 2010
 * Time: 11:01:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationException extends Exception{
    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
