package exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: utcn
 * Date: Nov 4, 2010
 * Time: 2:25:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceCenterAccessException extends Exception {
    public ServiceCenterAccessException(String message) {
        super(message);
    }

    public ServiceCenterAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
