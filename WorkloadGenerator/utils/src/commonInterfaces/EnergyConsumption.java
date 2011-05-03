package commonInterfaces;

import exceptions.ApplicationException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Sep 9, 2010
 * Time: 10:58:58 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EnergyConsumption extends Remote {
    public Number getValueWithRunningAlgorithm() throws ApplicationException, RemoteException;

    public Number getValueWithoutAlgorithm() throws ApplicationException, RemoteException;

    public boolean isSimulation() throws ApplicationException, RemoteException;

    public void setSimulation(boolean t) throws ApplicationException, RemoteException;

    public void setNumberOfDeployActions(int nr) throws RemoteException;


    public int getNumberOfDeployActions() throws RemoteException;

    public int getNumberOfMigrateActions() throws RemoteException;

    public void setNumberOfMigrateActions(int numberOfMigrateActions) throws RemoteException;
}
