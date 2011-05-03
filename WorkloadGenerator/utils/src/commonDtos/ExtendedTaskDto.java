package commonDtos;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Sep 8, 2010
 * Time: 12:52:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedTaskDto extends TaskDto implements Cloneable {
    private float cpuWeight;
    private float memWeight;
    private float hddWeight;
    private int id;
    private int associatedServerID;
    private String associatedServerIP;
    //in seconds
    private int life;

    private Collection<TaskDiskDto> diskDtoCollection;

    private String ip;
    private int vncPort;

    private int networkID;
    private String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    {
        diskDtoCollection = new ArrayList<TaskDiskDto>();
    }

    public int getNetworkID() {
        return networkID;
    }

    public void setNetworkID(int networkID) {
        this.networkID = networkID;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Collection<TaskDiskDto> getDiskDtoCollection() {
        return diskDtoCollection;
    }

    public void setDiskDtoCollection(Collection<TaskDiskDto> diskDtoCollection) {
        this.diskDtoCollection = diskDtoCollection;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getVncPort() {
        return vncPort;
    }

    public void setVncPort(int vncPort) {
        this.vncPort = vncPort;
    }

    public float getCpuWeight() {
        return cpuWeight;
    }

    public void setCpuWeight(float cpuWeight) {
        this.cpuWeight = cpuWeight;
    }

    public float getMemWeight() {
        return memWeight;
    }

    public void setMemWeight(float memWeight) {
        this.memWeight = memWeight;
    }

    public float getHddWeight() {
        return hddWeight;
    }

    public void setHddWeight(float hddWeight) {
        this.hddWeight = hddWeight;
    }

    public double distanceTo(ExtendedTaskDto task) {
        double minDistance = 1000000f;
        double memory1[] = {this.getRequestedMemoryMin(), this.getRequestedMemoryMax(), this.getRequestedMemoryMax(), this.getRequestedMemoryMin(), this.getRequestedMemoryMin(), this.getRequestedMemoryMax(), this.getRequestedMemoryMax(), this.getRequestedMemoryMin()};
        double cpu1[] = {this.getRequestedCPUMin(), this.getRequestedCPUMin(), this.getRequestedCPUMax(), this.getRequestedCPUMax(), this.getRequestedCPUMin(), this.getRequestedCPUMin(), this.getRequestedCPUMax(), this.getRequestedCPUMax()};
        // double hdd1[] = {this.getRequestedStorageMax(), this.getRequestedStorageMax(), this.getRequestedStorageMax(), this.getRequestedStorageMax(), this.getRequestedStorageMin(), this.getRequestedStorageMin(), this.getRequestedStorageMin(), this.getRequestedStorageMin()};
        double memory2[] = {task.getRequestedMemoryMin(), task.getRequestedMemoryMax(), task.getRequestedMemoryMax(), task.getRequestedMemoryMin(), task.getRequestedMemoryMin(), task.getRequestedMemoryMax(), task.getRequestedMemoryMax(), task.getRequestedMemoryMin()};
        double cpu2[] = {task.getRequestedCPUMin(), task.getRequestedCPUMin(), task.getRequestedCPUMax(), task.getRequestedCPUMax(), task.getRequestedCPUMin(), task.getRequestedCPUMin(), task.getRequestedCPUMax(), task.getRequestedCPUMax()};
        //double hdd2[] = {task.getRequestedStorageMax(), task.getRequestedStorageMax(), task.getRequestedStorageMax(), task.getRequestedStorageMax(), task.getRequestedStorageMin(), task.getRequestedStorageMin(), task.getRequestedStorageMin(), task.getRequestedStorageMin()};
        double totalDistance = 0;
        for (int i = 0; i < 8; i++) {
            {
                double dist = Math.sqrt((Math.pow(memory1[i] - memory2[i], 2) + Math.pow((cpu1[i] - cpu2[i]), 2)));//+ Math.pow((hdd1[i] - hdd2[j]), 2)));
                totalDistance += dist;

            }
        }
        return totalDistance;
    }

    @Override
    public boolean equals(Object dto) {
        if (!(dto instanceof TaskDto)) return false;
        ExtendedTaskDto etd = (ExtendedTaskDto) dto;

        if (etd.getCpuWeight() != cpuWeight) return false;
        if (etd.getHddWeight() != hddWeight) return false;
        if (etd.getMemWeight() != memWeight) return false;
        return super.equals(dto);

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ExtendedTaskDto clone = (ExtendedTaskDto) super.clone();
        clone.setRequestedCPUMax(this.getRequestedCPUMax());
        clone.setRequestedCPUMin(this.getRequestedCPUMin());
        clone.setRequestedMemoryMax(this.getRequestedMemoryMax());
        clone.setRequestedMemoryMin(this.getRequestedMemoryMin());
        clone.setRequestedStorageMax(this.getRequestedStorageMax());
        clone.setRequestedStorageMin(this.getRequestedStorageMin());
        clone.setRequestedCores(this.getRequestedCores());
        clone.setCpuWeight(this.getCpuWeight());
        clone.setMemWeight(this.getMemWeight());
        clone.setHddWeight(this.getHddWeight());
        clone.setDiskDtoCollection(this.getDiskDtoCollection());
        clone.setTaskName(this.getTaskName());
        clone.setIp(this.getIp());
        clone.setLife(this.getLife());
        clone.setNetworkID(this.getNetworkID());
        clone.setServiceName(this.getServiceName());
        clone.setVncPort(this.getVncPort());
        return clone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssociatedServerID() {
        return associatedServerID;
    }

    public void setAssociatedServerID(int associatedServerID) {
        this.associatedServerID = associatedServerID;
    }

    public String getAssociatedServerIP() {
        return associatedServerIP;
    }

    public void setAssociatedServerIP(String associatedServerIP) {
        this.associatedServerIP = associatedServerIP;
    }

}
