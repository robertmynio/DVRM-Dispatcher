package commonInterfaces.rii;

import commonDtos.ExtendedServerDto;
import commonDtos.ExtendedTaskDto;
import misc.Pair;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Dec 20, 2010
 * Time: 4:48:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RTEInfoInterface extends Remote, Serializable {

    public List<ExtendedServerDto> getServersInfo() throws RemoteException;

    public List<ExtendedTaskDto> getTasksInfo() throws RemoteException;

    public void setServersInfo(List<ExtendedServerDto> servers) throws RemoteException;

    public void setTasksInfo(List<ExtendedTaskDto> tasks) throws RemoteException;

    public void setInstantEnergyConsumptionInfo(Date measurementTime, Number withGames, Number withoutGames) throws RemoteException;

    //Number[0] = WithGames, Number[1] = withoutGames

    public Pair<Date, Number[]> getInstantEnergyConsumptionInfo() throws RemoteException;

    //Number[0] = WithGames, Number[1] = withoutGames

    public List<Pair<Date, Number[]>> getRecordedEnergyConsumption() throws RemoteException;

    public boolean isDataAboutTasksChanged() throws RemoteException;

    public void setDataAboutTasksChanged(boolean dataAboutTasksChanged) throws RemoteException;

    public boolean isDataAboutServersChanged() throws RemoteException;

    public void setDataAboutServersChanged(boolean dataAboutServersChanged) throws RemoteException;

    public boolean hasMessages() throws RemoteException;

    public void setHasMessages(boolean hasMessages) throws RemoteException;

    public List<Pair<Date, String>> getMessages() throws RemoteException;

    public void setMessages(List<Pair<Date, String>> messages) throws RemoteException;

    public void addMessage(Date messageTime, String message) throws RemoteException;

    public Number getEnergySavings() throws RemoteException;

    public void setEnergySavings(Number nr) throws RemoteException;

    public Date getMeasurementBeginning() throws RemoteException;

    public void setMeasurementBeginning(Date d) throws RemoteException;

    public Date getMeasurementEnding() throws RemoteException;

    public void setMeasurementEnding(Date d) throws RemoteException;

    public boolean saveFile(Serializable data, String fileName) throws RemoteException;

    public Serializable loadFile(String fileName) throws RemoteException;

    public String getCurrentlyExecutingCommand() throws RemoteException;

    public void setCurrentlyExecutingCommand(String command) throws RemoteException;

    public boolean hasExecutingCommandInfoChanged() throws RemoteException;

    public void setExecutingCommandInfoChanged(boolean val) throws RemoteException;

    public java.util.Date getCurrentTime() throws RemoteException;

    public void setInfoFromLocalLoop(List<Pair<String, Double>> freq, List<Pair<String, Integer>> states) throws RemoteException;

    public List<Pair<String, Double>> getCpuFrequencies() throws RemoteException;

    public void setCpuFrequencies(List<Pair<String, Double>> cpuFrequencies) throws RemoteException;

    public List<Pair<String, Integer>> getCpuStates() throws RemoteException;

    public void setCpuStates(List<Pair<String, Integer>> cpuStates) throws RemoteException;

}
