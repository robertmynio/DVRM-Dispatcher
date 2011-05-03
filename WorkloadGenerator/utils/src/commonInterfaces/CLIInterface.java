package commonInterfaces;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 22, 2010
 * Time: 11:05:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CLIInterface extends Remote {
    String processCommand(CLICommand command, Serializable[] commandArguments) throws RemoteException, exceptions.ApplicationException;
}
