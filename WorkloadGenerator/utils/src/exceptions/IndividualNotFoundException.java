package exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Aug 25, 2010
 * Time: 11:51:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndividualNotFoundException extends Exception {
    public IndividualNotFoundException(String individualName) {
        super("Individual <<" + individualName + ">> not found");
    }
}
